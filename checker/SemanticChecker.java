package checker;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.PascalParser;
import parser.PascalParserBaseVisitor;
import tables.StrTable;
import tables.SymbolTable;
import typing.Type;
import typing.Conv;
import entries.*;
import ast.AST;
import ast.NodeKind;

import java.util.ArrayList;
import java.util.List;

// Analisador semântico de Pascal implementado como um visitor da ParseTree do ANTLR.
public class SemanticChecker extends PascalParserBaseVisitor<AST> {
    
    private StrTable st = new StrTable();
    private SymbolTable symbolTable = new SymbolTable();
    private Type lastDeclType;
    private Type.ArrayType lastArrayType;
    
    // Classe interna para armazenar informações de tipo inferidas
    private static class TypeInfo {
        Type type;
        Type.ArrayType arrayType; // Para quando type == ARRAY
        
        TypeInfo(Type type) {
            this.type = type;
            this.arrayType = null;
        }
        
        TypeInfo(Type.ArrayType arrayType) {
            this.type = Type.ARRAY;
            this.arrayType = arrayType;
        }
        
        @Override
        public String toString() {
            return type == Type.ARRAY && arrayType != null ? 
                   arrayType.toString() : type.toString();
        }
    }

    /**
     * Construtor - inicializa procedimentos e funções built-in
     */
    public SemanticChecker() {
        initializeBuiltins();
    }
    
    /**
     * Inicializa procedimentos e funções built-in do Pascal
     */
    private void initializeBuiltins() {
        // Procedimentos built-in
        symbolTable.addEntry("writeln", new FuncEntry("writeln", 0, Type.NO_TYPE, new ArrayList<>()));
        symbolTable.addEntry("write", new FuncEntry("write", 0, Type.NO_TYPE, new ArrayList<>()));
        symbolTable.addEntry("readln", new FuncEntry("readln", 0, Type.NO_TYPE, new ArrayList<>()));
        symbolTable.addEntry("read", new FuncEntry("read", 0, Type.NO_TYPE, new ArrayList<>()));
    }

    /**
     * Verifica se um procedimento é built-in
     */
    private boolean isBuiltinProcedure(String procedureName) {
        return procedureName.equals("writeln") || procedureName.equals("write") || 
               procedureName.equals("readln") || procedureName.equals("read");
    }

    /**
     * Testa se o dado token foi declarado antes e retorna seu tipo.
     */
    private TypeInfo checkVar(Token token) {
        String text = token.getText();
        int line = token.getLine();
        Entry entry = symbolTable.lookupEntry(text);
        
        if (entry == null) {
            System.err.printf("SEMANTIC ERROR (%d): variable '%s' was not declared.\n", line, text);
            System.exit(1);
        }
        
        if (entry instanceof ArrayEntry) {
            ArrayEntry arrayEntry = (ArrayEntry) entry;
            return new TypeInfo(arrayEntry.getArrayType());
        } else {
            return new TypeInfo(entry.getEntryType());
        }
    }

    /**
     * Cria uma nova variável a partir do dado token.
     */
    private void newVar(Token token) {
        String text = token.getText();
        int line = token.getLine();
        
        // Verifica se já existe no escopo atual
        Entry existing = symbolTable.lookupCurrentScope(text);
        if (existing != null) {
            System.err.printf("SEMANTIC ERROR (%d): variable '%s' already declared at line %d.\n", 
                             line, text, existing.getLine());
            System.exit(1);
        }
        
        Entry entry;
        if (lastDeclType == Type.ARRAY && lastArrayType != null) {
            entry = new ArrayEntry(text, line, lastArrayType);
        } else {
            entry = new VarEntry(text, line, lastDeclType);
        }
        
        symbolTable.addEntry(text, entry);
    }
    
    /**
     * Cria uma nova constante
     */
    private void newConst(Token token, Object value) {
        String text = token.getText();
        int line = token.getLine();
        
        Entry existing = symbolTable.lookupCurrentScope(text);
        if (existing != null) {
            System.err.printf("SEMANTIC ERROR (%d): constant '%s' already declared at line %d.\n", 
                             line, text, existing.getLine());
            System.exit(1);
        }
        
        ConstEntry constEntry = new ConstEntry(text, line, lastDeclType, value);
        symbolTable.addEntry(text, constEntry);
    }
    
    /**
     * Valida acesso a array e retorna o tipo do elemento
     */
    private TypeInfo checkArrayAccess(Token arrayToken, Type indexType, int line) {
        String arrayName = arrayToken.getText();
        
        Entry entry = symbolTable.lookupEntry(arrayName);
        if (entry == null) {
            System.err.printf("SEMANTIC ERROR (%d): array '%s' was not declared.\n", line, arrayName);
            System.exit(1);
        }
        
        if (!(entry instanceof ArrayEntry)) {
            System.err.printf("SEMANTIC ERROR (%d): '%s' is not an array.\n", line, arrayName);
            System.exit(1);
        }
        
        // Verifica se o índice é do tipo correto (integer)
        if (indexType != Type.INTEGER) {
            System.err.printf("SEMANTIC ERROR (%d): array index must be integer, got '%s'.\n", 
                             line, indexType.toString());
            System.exit(1);
        }
        
        ArrayEntry arrayEntry = (ArrayEntry) entry;
        return new TypeInfo(arrayEntry.getElementType());
    }

    /**
     * Exibe o conteúdo das tabelas em stdout.
     */
    public void printTables() {
        System.out.print("\n\n");
        System.out.print(st);
        System.out.print("\n\n");
        System.out.print(symbolTable);
        System.out.print("\n\n");
    }
    
    /**
     * Imprime a AST em formato DOT
     */
    public void printAST(AST root) {
        if (root != null) {
            AST.printDot(root, symbolTable);
        }
    }
    
    @Override
    public AST visitProgram(PascalParser.ProgramContext ctx) {
        String programName = ctx.IDENTIFIER().getText();
        
        AST blockNode = visit(ctx.block());
        
        AST programNode = new AST(NodeKind.PROGRAM_NODE, programName, Type.NO_TYPE);
        programNode.addChild(blockNode);
        
        return programNode;
    }
    
    @Override
    public AST visitBlock(PascalParser.BlockContext ctx) {
        AST blockNode = new AST(NodeKind.BLOCK_NODE, Type.NO_TYPE);
        
        // Processa seções na ordem correta
        if (ctx.constSection() != null) {
            AST constSectionNode = visit(ctx.constSection());
            blockNode.addChild(constSectionNode);
        }
        
        if (ctx.varSection() != null) {
            AST varSectionNode = visit(ctx.varSection());
            blockNode.addChild(varSectionNode);
        }
        
        // Processa declarações de subrotinas
        for (PascalParser.SubroutineDeclarationPartContext subCtx : ctx.subroutineDeclarationPart()) {
            AST subroutineNode = visit(subCtx);
            blockNode.addChild(subroutineNode);
        }
        
        // Processa o statement composto
        if (ctx.compoundStatement() != null) {
            AST compoundNode = visit(ctx.compoundStatement());
            blockNode.addChild(compoundNode);
        }
        
        return blockNode;
    }
    
    @Override
    public AST visitSubroutineDeclarationPart(PascalParser.SubroutineDeclarationPartContext ctx) {
        if (ctx.procedureDeclaration() != null) {
            return visit(ctx.procedureDeclaration());
        } else if (ctx.functionDeclaration() != null) {
            return visit(ctx.functionDeclaration());
        }
        return null;
    }
    
    @Override
    public AST visitProcedureDeclaration(PascalParser.ProcedureDeclarationContext ctx) {
        
        String procName = ctx.IDENTIFIER().getText();
        int line = ctx.IDENTIFIER().getSymbol().getLine();
        
        // Verifica redeclaração
        Entry existing = symbolTable.lookupCurrentScope(procName);
        if (existing != null) {
            System.err.printf("SEMANTIC ERROR (%d): procedure '%s' already declared at line %d.\n", 
                            line, procName, existing.getLine());
            System.exit(1);
        }
        
        // COLETA OS PARÂMETROS ANTES DE CRIAR A ENTRADA 
        List<ParamEntry> paramList = new ArrayList<>();
        
        if (ctx.formalParameterList() != null) {
            // Processa parâmetros temporariamente para coletar informações
            for (PascalParser.FormalParameterSectionContext paramCtx : ctx.formalParameterList().formalParameterSection()) {
                String typeName = paramCtx.IDENTIFIER().getText();
                Type paramType = getTypeFromName(typeName);
                boolean isVarParam = paramCtx.VAR() != null;
                
                for (TerminalNode id : paramCtx.identifierList().IDENTIFIER()) {
                    String paramName = id.getText();
                    int paramLine = id.getSymbol().getLine();
                    
                    ParamEntry paramEntry = new ParamEntry(paramName, paramLine, paramType, isVarParam);
                    paramList.add(paramEntry);
                }
            }
        }
        
        FuncEntry procEntry = new FuncEntry(procName, line, Type.NO_TYPE, paramList);
        symbolTable.addEntry(procName, procEntry);
        
        // Cria nó da procedure
        AST procNode = new AST(NodeKind.PROC_DECL_NODE, procName, Type.NO_TYPE);
        
        // Abre novo escopo para a procedure
        symbolTable.openScope(procName + "_PROC");
        
        // Adiciona parâmetros se existirem
        if (ctx.formalParameterList() != null) {
            AST paramListNode = visit(ctx.formalParameterList());
            procNode.addChild(paramListNode);
        }
        
        // Adiciona bloco da procedure
        AST blockNode = visit(ctx.block());
        procNode.addChild(blockNode);
        
        symbolTable.closeScope();
        
        return procNode;
    }
    
    @Override
    public AST visitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx) {
        
        String funcName = ctx.IDENTIFIER(0).getText();
        String returnTypeName = ctx.IDENTIFIER(1).getText();
        int line = ctx.IDENTIFIER(0).getSymbol().getLine();
        
        // Verifica redeclaração
        Entry existing = symbolTable.lookupCurrentScope(funcName);
        if (existing != null) {
            System.err.printf("SEMANTIC ERROR (%d): function '%s' already declared at line %d.\n", 
                            line, funcName, existing.getLine());
            System.exit(1);
        }
        
        Type returnType = getTypeFromName(returnTypeName);
        if (returnType == Type.NO_TYPE) {
            System.err.printf("SEMANTIC ERROR (%d): unknown return type '%s' for function '%s'.\n", 
                            line, returnTypeName, funcName);
            System.exit(1);
        }
        
        // === COLETA OS PARÂMETROS ANTES DE CRIAR A ENTRADA ===
        List<ParamEntry> paramList = new ArrayList<>();
        
        if (ctx.formalParameterList() != null) {
            // Processa parâmetros temporariamente para coletar informações
            for (PascalParser.FormalParameterSectionContext paramCtx : ctx.formalParameterList().formalParameterSection()) {
                String typeName = paramCtx.IDENTIFIER().getText();
                Type paramType = getTypeFromName(typeName);
                boolean isVarParam = paramCtx.VAR() != null;
                
                for (TerminalNode id : paramCtx.identifierList().IDENTIFIER()) {
                    String paramName = id.getText();
                    int paramLine = id.getSymbol().getLine();
                    
                    ParamEntry paramEntry = new ParamEntry(paramName, paramLine, paramType, isVarParam);
                    paramList.add(paramEntry);
                }
            }
        }
        
        FuncEntry funcEntry = new FuncEntry(funcName, line, returnType, paramList);
        symbolTable.addEntry(funcName, funcEntry);
        
        // Cria nó da function
        AST funcNode = new AST(NodeKind.FUNC_DECL_NODE, funcName, returnType);
        
        // Abre novo escopo para a function
        symbolTable.openScope(funcName + "_FUNC");
        
        // Adiciona parâmetros se existirem
        if (ctx.formalParameterList() != null) {
            AST paramListNode = visit(ctx.formalParameterList());
            funcNode.addChild(paramListNode);
        }
        
        // Adiciona bloco da function
        AST blockNode = visit(ctx.block());
        funcNode.addChild(blockNode);
        
        symbolTable.closeScope();
        
        return funcNode;
    }
    
    @Override
    public AST visitFormalParameterList(PascalParser.FormalParameterListContext ctx) {
        AST paramListNode = new AST(NodeKind.PARAM_LIST_NODE, Type.NO_TYPE);
        
        for (PascalParser.FormalParameterSectionContext paramCtx : ctx.formalParameterSection()) {
            AST paramSectionNode = visit(paramCtx);
            paramListNode.addChild(paramSectionNode);
        }
        
        return paramListNode;
    }
    
    @Override
    public AST visitFormalParameterSection(PascalParser.FormalParameterSectionContext ctx) {
        
        String typeName = ctx.IDENTIFIER().getText();
        Type paramType = getTypeFromName(typeName);
        
        if (paramType == Type.NO_TYPE) {
            System.err.printf("SEMANTIC ERROR: unknown parameter type '%s'.\n", typeName);
            System.exit(1);
        }

        // Verifica se é parâmetro por referência (VAR)
        boolean isVarParam = ctx.VAR() != null;
        
        // Cria um nó para agrupar todos os parâmetros desta seção
        AST paramSectionNode = new AST(NodeKind.PARAM_LIST_NODE, Type.NO_TYPE);
        
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            String paramName = id.getText();
            int line = id.getSymbol().getLine();
            
            // Verifica redeclaração no escopo atual
            Entry existing = symbolTable.lookupCurrentScope(paramName);
            if (existing != null) {
                System.err.printf("SEMANTIC ERROR (%d): parameter '%s' already declared at line %d.\n", 
                                 line, paramName, existing.getLine());
                System.exit(1);
            }
            
            ParamEntry paramEntry = new ParamEntry(paramName, line, paramType, isVarParam);
            symbolTable.addEntry(paramName, paramEntry);
            
            // Cria nó AST para o parâmetro
            AST paramNode = new AST(NodeKind.PARAM_NODE, paramName, paramType);
            paramSectionNode.addChild(paramNode);
        }
        
        return paramSectionNode;
    }

    @Override
    public AST visitConstSection(PascalParser.ConstSectionContext ctx) {
        AST constSectionNode = new AST(NodeKind.CONST_SECTION_NODE, Type.NO_TYPE);
        
        for (PascalParser.ConstDefinitionContext constDef : ctx.constDefinition()) {
            AST constDeclNode = visit(constDef);
            constSectionNode.addChild(constDeclNode);
        }
        
        return constSectionNode;
    }
    
    @Override
    public AST visitConstDefinition(PascalParser.ConstDefinitionContext ctx) {
        
        // Visita a constante para determinar seu tipo e obter nó AST
        AST constantNode = visit(ctx.constant());
        Type constType = constantNode.type;
        
        lastDeclType = constType;
        lastArrayType = null; // Constantes não são arrays
        
        Token constToken = ctx.IDENTIFIER().getSymbol();
        String constName = constToken.getText();
        Object value = extractConstantValue(ctx.constant(), constType);
        
        newConst(constToken, value);
        
        // Cria nó de declaração de constante
        AST constDeclNode = new AST(NodeKind.CONST_DECL_NODE, constName, constType);
        constDeclNode.addChild(constantNode);
        
        return constDeclNode;
    }
    
    /**
     * Extrai o valor de uma constante para armazenamento na tabela de símbolos
     */
    private Object extractConstantValue(PascalParser.ConstantContext ctx, Type type) {
        if (ctx.signedNumber() != null) {
            if (ctx.signedNumber().INTEGER() != null) {
                String text = ctx.signedNumber().INTEGER().getText();
                int value = Integer.parseInt(text);
                if (ctx.signedNumber().MINUS() != null) {
                    value = -value;
                }
                return value;
            } else if (ctx.signedNumber().REAL() != null) {
                String text = ctx.signedNumber().REAL().getText();
                float value = Float.parseFloat(text);
                if (ctx.signedNumber().MINUS() != null) {
                    value = -value;
                }
                return value;
            }
        } else if (ctx.CHARACTER() != null) {
            return ctx.CHARACTER().getText();
        } else if (ctx.STRING() != null) {
            return ctx.STRING().getText();
        } else if (ctx.TRUE() != null) {
            return true;
        } else if (ctx.FALSE() != null) {
            return false;
        }
        return null;
    }

    @Override
    public AST visitConstant(PascalParser.ConstantContext ctx) {
        if (ctx.signedNumber() != null) {
            return visit(ctx.signedNumber());
        } else if (ctx.CHARACTER() != null) {
            String charValue = ctx.CHARACTER().getText();
            return new AST(NodeKind.CHAR_VAL_NODE, charValue, Type.CHAR);
        } else if (ctx.STRING() != null) {
            String strValue = ctx.STRING().getText();
            int strIndex = st.add(strValue) ? st.size() - 1 : st.indexOf(strValue);
            return new AST(NodeKind.STR_VAL_NODE, strValue, Type.STRING);
        } else if (ctx.TRUE() != null) {
            return new AST(NodeKind.BOOL_VAL_NODE, 1, Type.BOOLEAN);
        } else if (ctx.FALSE() != null) {
            return new AST(NodeKind.BOOL_VAL_NODE, 0, Type.BOOLEAN);
        } else if (ctx.IDENTIFIER() != null) {
            // Referência a outra constante
            TypeInfo typeInfo = checkVar(ctx.IDENTIFIER().getSymbol());
            String varName = ctx.IDENTIFIER().getText();
            return new AST(NodeKind.VAR_USE_NODE, varName, typeInfo.type);
        }
        return new AST(NodeKind.INT_VAL_NODE, 0, Type.NO_TYPE);
    }

    @Override
    public AST visitSignedNumber(PascalParser.SignedNumberContext ctx) {
        if (ctx.INTEGER() != null) {
            String text = ctx.INTEGER().getText();
            int value = Integer.parseInt(text);
            if (ctx.MINUS() != null) {
                value = -value;
            }
            return new AST(NodeKind.INT_VAL_NODE, value, Type.INTEGER);
        } else if (ctx.REAL() != null) {
            String text = ctx.REAL().getText();
            float value = Float.parseFloat(text);
            if (ctx.MINUS() != null) {
                value = -value;
            }
            return new AST(NodeKind.REAL_VAL_NODE, value, Type.REAL);
        }
        return new AST(NodeKind.INT_VAL_NODE, 0, Type.NO_TYPE);
    }

    @Override
    public AST visitVarSection(PascalParser.VarSectionContext ctx) {
        AST varSectionNode = new AST(NodeKind.VAR_SECTION_NODE, Type.NO_TYPE);
        
        for (PascalParser.VarDeclarationContext varDecl : ctx.varDeclaration()) {
            AST varDeclNode = visit(varDecl);
            varSectionNode.addChild(varDeclNode);
        }
        
        return varSectionNode;
    }

    @Override
    public AST visitVarDeclaration(PascalParser.VarDeclarationContext ctx) {
        
        // Processa o tipo da declaração
        AST typeNode = processTypeDenoter(ctx.typeDenoter());
        
        // Cria nó de lista de variáveis
        AST varListNode = new AST(NodeKind.VAR_LIST_NODE, Type.NO_TYPE);
        
        // Para cada variável na lista
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            newVar(id.getSymbol());
            
            // Cria nó AST para a declaração da variável
            String varName = id.getText();
            AST varDeclNode = new AST(NodeKind.VAR_DECL_NODE, varName, lastDeclType);
            
            // Se é um array, adiciona as informações do tipo como filho
            if (lastDeclType == Type.ARRAY && typeNode != null) {
                varDeclNode.addChild(typeNode);
            }
            
            varListNode.addChild(varDeclNode);
        }
        return varListNode;
    }

    @Override
    public AST visitVariable(PascalParser.VariableContext ctx) {
        if (ctx.LBRACK() != null) {
            // Acesso a array: IDENTIFIER[expression]
            AST indexNode = visit(ctx.expression());
            Type indexType = indexNode.type;
            
            TypeInfo resultType = checkArrayAccess(ctx.IDENTIFIER().getSymbol(), indexType, 
                                                  ctx.IDENTIFIER().getSymbol().getLine());
            
            String arrayName = ctx.IDENTIFIER().getText();
            AST arrayAccessNode = new AST(NodeKind.ARRAY_ACCESS_NODE, arrayName, resultType.type);
            arrayAccessNode.addChild(indexNode);
            
            return arrayAccessNode;
        } else {
            // Variável simples
            TypeInfo typeInfo = checkVar(ctx.IDENTIFIER().getSymbol());
            String varName = ctx.IDENTIFIER().getText();
            return new AST(NodeKind.VAR_USE_NODE, varName, typeInfo.type);
        }
    }

    @Override
    public AST visitFactor(PascalParser.FactorContext ctx) {
        if (ctx.variable() != null) {
            // Variável ou acesso a array
            return visit(ctx.variable());

        } else if (ctx.IDENTIFIER() != null) {
            if (ctx.LPAREN() != null) {
                // Chamada de função
                Type returnType = checkFunctionCall(ctx.IDENTIFIER().getSymbol(), ctx.expressionList());
                String funcName = ctx.IDENTIFIER().getText();
                
                AST funcCallNode = new AST(NodeKind.FUNC_CALL_NODE, funcName, returnType);
                
                if (ctx.expressionList() != null) {
                    AST exprListNode = visit(ctx.expressionList());
                    funcCallNode.addChild(exprListNode);
                }
                
                return funcCallNode;
            } else {
                // Variável simples
                TypeInfo typeInfo = checkVar(ctx.IDENTIFIER().getSymbol());
                String varName = ctx.IDENTIFIER().getText();
                return new AST(NodeKind.VAR_USE_NODE, varName, typeInfo.type);
            }
        } else if (ctx.INTEGER() != null) {
            int value = Integer.parseInt(ctx.INTEGER().getText());
            return new AST(NodeKind.INT_VAL_NODE, value, Type.INTEGER);
        } else if (ctx.REAL() != null) {
            float value = Float.parseFloat(ctx.REAL().getText());
            return new AST(NodeKind.REAL_VAL_NODE, value, Type.REAL);
        } else if (ctx.CHARACTER() != null) {
            String charValue = ctx.CHARACTER().getText();
            return new AST(NodeKind.CHAR_VAL_NODE, charValue, Type.CHAR);
        } else if (ctx.STRING() != null) {
            String strValue = ctx.STRING().getText();
            st.add(strValue);
            return new AST(NodeKind.STR_VAL_NODE, strValue, Type.STRING);
        } else if (ctx.LPAREN() != null) {
            // Expressão entre parênteses
            AST exprNode = visit(ctx.expression());
            AST parenNode = new AST(NodeKind.PAREN_EXPR_NODE, exprNode.type);
            parenNode.addChild(exprNode);
            return parenNode;
        } else if (ctx.TRUE() != null) {
            return new AST(NodeKind.BOOL_VAL_NODE, 1, Type.BOOLEAN);
        } else if (ctx.FALSE() != null) {
            return new AST(NodeKind.BOOL_VAL_NODE, 0, Type.BOOLEAN);
        } else if (ctx.NOT() != null) {
            // Negação lógica
            AST factorNode = visit(ctx.factor());
            if (factorNode.type != Type.BOOLEAN) {
                System.err.printf("SEMANTIC ERROR (%d): 'not' operator requires boolean operand, got '%s'.\n", 
                                 ctx.NOT().getSymbol().getLine(), factorNode.type.toString());
                System.exit(1);
            }
            
            AST notNode = new AST(NodeKind.NOT_NODE, Type.BOOLEAN);
            notNode.addChild(factorNode);
            return notNode;
        }
        
        return new AST(NodeKind.INT_VAL_NODE, 0, Type.NO_TYPE);
    }
    
    @Override
    public AST visitExpressionList(PascalParser.ExpressionListContext ctx) {
        AST exprListNode = new AST(NodeKind.BLOCK_NODE, Type.NO_TYPE); // Usando BLOCK_NODE como container
        
        for (PascalParser.ExpressionItemContext exprItem : ctx.expressionItem()) {
            if (exprItem.expression() != null) {
                AST exprNode = visit(exprItem.expression());
                exprListNode.addChild(exprNode);
            }
        }
        
        return exprListNode;
    }
    
    /**
     * Verifica chamada de função e retorna seu tipo de retorno
     */
    private Type checkFunctionCall(Token functionToken, PascalParser.ExpressionListContext exprList) {
        String functionName = functionToken.getText();
        int line = functionToken.getLine();
        
        Entry entry = symbolTable.lookupEntry(functionName);
        if (entry == null) {
            System.err.printf("SEMANTIC ERROR (%d): function '%s' was not declared.\n", line, functionName);
            System.exit(1);
        }
        
        if (!(entry instanceof FuncEntry)) {
            System.err.printf("SEMANTIC ERROR (%d): '%s' is not a function.\n", line, functionName);
            System.exit(1);
        }
        
        FuncEntry funcEntry = (FuncEntry) entry;
        
        // Verifica se é uma função (tem tipo de retorno) ou procedure (não tem)
        if (funcEntry.getEntryType() == Type.NO_TYPE) {
            System.err.printf("SEMANTIC ERROR (%d): '%s' is a procedure, cannot be used in expression.\n", 
                            line, functionName);
            System.exit(1);
        }
        
        // ===== NOVA VALIDAÇÃO DE PARÂMETROS =====
        
        // Obtém os parâmetros esperados da função
        List<ParamEntry> expectedParams = funcEntry.getParameters();
        
        // Conta argumentos fornecidos
        int providedArgCount = 0;
        List<Type> providedArgTypes = new ArrayList<>();
        
        if (exprList != null) {
            for (PascalParser.ExpressionItemContext exprItem : exprList.expressionItem()) {
                if (exprItem.expression() != null) {
                    AST exprNode = visit(exprItem.expression());
                    providedArgTypes.add(exprNode.type);
                    providedArgCount++;
                }
            }
        }
        
        // Verifica número de argumentos
        if (providedArgCount != expectedParams.size()) {
            System.err.printf("SEMANTIC ERROR (%d): function '%s' expects %d arguments, but %d were provided.\n",
                            line, functionName, expectedParams.size(), providedArgCount);
            System.exit(1);
        }
        
        // Verifica tipos dos argumentos
        for (int i = 0; i < expectedParams.size(); i++) {
            ParamEntry expectedParam = expectedParams.get(i);
            Type expectedType = expectedParam.getEntryType();
            Type providedType = providedArgTypes.get(i);
            
            // Verifica compatibilidade de tipos
            Type unifiedType = expectedType.unifyAssignment(providedType);
            
            if (unifiedType == Type.NO_TYPE) {
                System.err.printf("SEMANTIC ERROR (%d): argument %d of function '%s' expects type '%s', but got '%s'.\n",
                                line, i + 1, functionName, expectedType.toString(), providedType.toString());
                System.exit(1);
            }
            
            // Para parâmetros VAR, o tipo deve ser exatamente igual
            if (expectedParam.isReference() && expectedType != providedType) {
                System.err.printf("SEMANTIC ERROR (%d): VAR parameter %d of function '%s' requires exact type '%s', but got '%s'.\n",
                                line, i + 1, functionName, expectedType.toString(), providedType.toString());
                System.exit(1);
            }
        }
        
        return funcEntry.getEntryType();
    }
    
    @Override
    public AST visitProcedureCall(PascalParser.ProcedureCallContext ctx) {
        String procName = ctx.IDENTIFIER().getText();
        int line = ctx.IDENTIFIER().getSymbol().getLine();
        
        Entry entry = symbolTable.lookupEntry(procName);
        if (entry == null) {
            System.err.printf("SEMANTIC ERROR (%d): procedure '%s' was not declared.\n", line, procName);
            System.exit(1);
        }
        
        if (!(entry instanceof FuncEntry)) {
            System.err.printf("SEMANTIC ERROR (%d): '%s' is not a procedure or function.\n", line, procName);
            System.exit(1);
        }
        
        FuncEntry funcEntry = (FuncEntry) entry;
        
        // ===== VALIDAÇÃO DE PARÂMETROS =====
        
        // Para procedimentos built-in, pula a validação rigorosa de parâmetros
        if (isBuiltinProcedure(procName)) {
            // Built-ins podem aceitar qualquer número de parâmetros
            // Apenas processa as expressões para garantir que são válidas
            if (ctx.expressionList() != null) {
                for (PascalParser.ExpressionItemContext exprItem : ctx.expressionList().expressionItem()) {
                    if (exprItem.expression() != null) {
                        visit(exprItem.expression()); // Processa a expressão mas não valida tipo
                    }
                }
            }
        } else {
            // Obtém os parâmetros esperados
            List<ParamEntry> expectedParams = funcEntry.getParameters();
            
            // Conta argumentos fornecidos
            int providedArgCount = 0;
            List<Type> providedArgTypes = new ArrayList<>();
            
            if (ctx.expressionList() != null) {
                for (PascalParser.ExpressionItemContext exprItem : ctx.expressionList().expressionItem()) {
                    if (exprItem.expression() != null) {
                        AST exprNode = visit(exprItem.expression());
                        providedArgTypes.add(exprNode.type);
                        providedArgCount++;
                    }
                }
            }
            
            // Verifica número de argumentos
            if (providedArgCount != expectedParams.size()) {
                System.err.printf("SEMANTIC ERROR (%d): procedure '%s' expects %d arguments, but %d were provided.\n",
                                line, procName, expectedParams.size(), providedArgCount);
                System.exit(1);
            }
            
            // Verifica tipos dos argumentos
            for (int i = 0; i < expectedParams.size(); i++) {
                ParamEntry expectedParam = expectedParams.get(i);
                Type expectedType = expectedParam.getEntryType();
                Type providedType = providedArgTypes.get(i);
                
                // Verifica compatibilidade de tipos
                Type unifiedType = expectedType.unifyAssignment(providedType);
                
                if (unifiedType == Type.NO_TYPE) {
                    System.err.printf("SEMANTIC ERROR (%d): argument %d of procedure '%s' expects type '%s', but got '%s'.\n",
                                    line, i + 1, procName, expectedType.toString(), providedType.toString());
                    System.exit(1);
                }
                
                // Para parâmetros VAR, o tipo deve ser exatamente igual
                if (expectedParam.isReference() && expectedType != providedType) {
                    System.err.printf("SEMANTIC ERROR (%d): VAR parameter %d of procedure '%s' requires exact type '%s', but got '%s'.\n",
                                    line, i + 1, procName, expectedType.toString(), providedType.toString());
                    System.exit(1);
                }
            }
        }
        
        AST procCallNode = new AST(NodeKind.PROC_CALL_NODE, procName, Type.NO_TYPE);
        
        if (ctx.expressionList() != null) {
            AST exprListNode = visit(ctx.expressionList());
            procCallNode.addChild(exprListNode);
        }
        
        return procCallNode;
    }

    /**
     * Processa um typeDenoter e define lastDeclType e lastArrayType adequadamente
     * NOVA VERSÃO: retorna também o nó AST correspondente ao tipo
     */
    private AST processTypeDenoter(PascalParser.TypeDenoterContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            // Tipo simples
            String typeName = ctx.IDENTIFIER().getText();
            lastDeclType = getTypeFromName(typeName);
            lastArrayType = null;
            
            if (lastDeclType == Type.NO_TYPE) {
                System.err.printf("SEMANTIC ERROR: unknown type '%s'.\n", typeName);
                System.exit(1);
            }
            
            return null; // Tipos simples não precisam de nó AST adicional
            
        } else if (ctx.arrayType() != null) {
            // Tipo array
            lastDeclType = Type.ARRAY;
            lastArrayType = processArrayType(ctx.arrayType());
            
            // Criar nó AST para o tipo array que inclui as informações de range
            AST arrayTypeNode = new AST(NodeKind.ARRAY_TYPE_NODE, Type.ARRAY);
            
            // Adicionar nó de range como filho
            AST rangeNode = createRangeNode(lastArrayType.getStartIndex(), lastArrayType.getEndIndex());
            arrayTypeNode.addChild(rangeNode);
            
            // Adicionar informação do tipo do elemento
            AST elementTypeNode = new AST(NodeKind.VAR_DECL_NODE, lastArrayType.getElementType().toString(), lastArrayType.getElementType());
            arrayTypeNode.addChild(elementTypeNode);
            
            return arrayTypeNode;
        } else {
            lastDeclType = Type.NO_TYPE;
            lastArrayType = null;
            return null;
        }
    }
    
    /**
     * Cria um nó RANGE_NODE para representar ranges de array
     */
    private AST createRangeNode(int startIndex, int endIndex) {
        // Cria nó range com string representativa
        String rangeString = startIndex + ".." + endIndex;
        AST rangeNode = new AST(NodeKind.RANGE_NODE, rangeString, Type.INTEGER);
        
        // Adiciona nós filhos para start e end
        AST startNode = new AST(NodeKind.INT_VAL_NODE, startIndex, Type.INTEGER);
        AST endNode = new AST(NodeKind.INT_VAL_NODE, endIndex, Type.INTEGER);
        
        rangeNode.addChild(startNode);
        rangeNode.addChild(endNode);
        
        return rangeNode;
    }
    
    /**
     * Processa um arrayType e retorna o ArrayType correspondente
     */
    private Type.ArrayType processArrayType(PascalParser.ArrayTypeContext ctx) {
        // Extrai os índices do range
        PascalParser.IndexRangeContext rangeCtx = ctx.indexRange();
        int startIndex = extractNumber(rangeCtx.signedNumber(0));
        int endIndex = extractNumber(rangeCtx.signedNumber(1));
        
        // Valida o range
        if (startIndex > endIndex) {
            System.err.printf("SEMANTIC ERROR: Invalid array range [%d..%d] - start index must be <= end index.\n", 
                             startIndex, endIndex);
            System.exit(1);
        }
        
        // Processa o tipo do elemento recursivamente
        Type elementType;
        if (ctx.typeDenoter().IDENTIFIER() != null) {
            elementType = getTypeFromName(ctx.typeDenoter().IDENTIFIER().getText());
            if (elementType == Type.NO_TYPE) {
                System.err.printf("SEMANTIC ERROR: unknown array element type '%s'.\n", 
                                 ctx.typeDenoter().IDENTIFIER().getText());
                System.exit(1);
            }
        } else {
            // Array multidimensional - por enquanto não suportado completamente
            System.err.println("SEMANTIC ERROR: Multidimensional arrays not yet fully supported.");
            System.exit(1);
            elementType = Type.NO_TYPE;
        }
        
        return new Type.ArrayType(elementType, startIndex, endIndex);
    }
    
    /**
     * Extrai um número de um signedNumber
     */
    private int extractNumber(PascalParser.SignedNumberContext ctx) {
        String numberText;
        if (ctx.INTEGER() != null) {
            numberText = ctx.INTEGER().getText();
        } else {
            // Para REAL, pega só a parte inteira
            numberText = ctx.REAL().getText().split("\\.")[0];
        }
        
        int number = Integer.parseInt(numberText);
        
        // Aplica o sinal se necessário
        if (ctx.MINUS() != null) {
            number = -number;
        }
        
        return number;
    }
    
    /**
     * Converte nome de tipo em enum Type
     */
    private Type getTypeFromName(String typeName) {
        return switch (typeName.toLowerCase()) {
            case "integer" -> Type.INTEGER;
            case "real" -> Type.REAL;
            case "char" -> Type.CHAR;
            case "string" -> Type.STRING;
            case "boolean" -> Type.BOOLEAN;
            default -> Type.NO_TYPE;
        };
    }

    @Override
    public AST visitAssignmentStatement(PascalParser.AssignmentStatementContext ctx) {
        // Obtém o nó AST da variável de destino
        AST varNode = visit(ctx.variable());
        Type varType = varNode.type;
        
        // Obtém o nó AST da expressão do lado direito
        AST exprNode = visit(ctx.expression());
        Type exprType = exprNode.type;
        
        // Verifica compatibilidade de tipos para atribuição
        Type resultType = varType.unifyAssignment(exprType);
        
        if (resultType == Type.NO_TYPE) {
            int line = ctx.ASSIGN().getSymbol().getLine();
            System.err.printf("SEMANTIC ERROR (%d): cannot assign '%s' to '%s'.\n", 
                            line, exprType.toString(), varType.toString());
            System.exit(1);
        }
        
        // Cria nó de atribuição
        AST assignNode = new AST(NodeKind.ASSIGN_NODE, Type.NO_TYPE);
        assignNode.addChild(varNode);
        
        // Se precisa de conversão, cria nó de conversão
        if (varType == Type.REAL && exprType == Type.INTEGER) {
            AST convNode = Conv.createConvNode(Conv.I2R, exprNode);
            assignNode.addChild(convNode);
        } else {
            assignNode.addChild(exprNode);
        }
        
        return assignNode;
    }

    // Método para verificar expressões (comparações)
    @Override
    public AST visitExpression(PascalParser.ExpressionContext ctx) {
        AST leftNode = visit(ctx.simpleExpression(0));
        Type leftType = leftNode.type;
        
        // Se não há operador de comparação, retorna o nó da expressão simples
        if (ctx.simpleExpression().size() == 1) {
            return leftNode;
        }
        
        // Há operador de comparação
        AST rightNode = visit(ctx.simpleExpression(1));
        Type rightType = rightNode.type;
        Type resultType = leftType.unifyComparison(rightType);
        
        if (resultType == Type.NO_TYPE) {
            // Determina qual operador foi usado para a mensagem de erro
            String operator = "comparison";
            if (ctx.EQUAL() != null) operator = "=";
            else if (ctx.NOTEQUAL() != null) operator = "<>";
            else if (ctx.LESS() != null) operator = "<";
            else if (ctx.GREATER() != null) operator = ">";
            else if (ctx.LESSEQUAL() != null) operator = "<=";
            else if (ctx.GREATEREQUAL() != null) operator = ">=";
            
            int line = getComparisonOperatorLine(ctx);
            System.err.printf("SEMANTIC ERROR (%d): operator '%s' cannot be applied to '%s' and '%s'.\n", 
                            line, operator, leftType.toString(), rightType.toString());
            System.exit(1);
        }
        
        // Cria nó do operador de comparação
        NodeKind opKind = NodeKind.EQ_NODE;
        if (ctx.EQUAL() != null) opKind = NodeKind.EQ_NODE;
        else if (ctx.NOTEQUAL() != null) opKind = NodeKind.NEQ_NODE;
        else if (ctx.LESS() != null) opKind = NodeKind.LT_NODE;
        else if (ctx.GREATER() != null) opKind = NodeKind.GT_NODE;
        else if (ctx.LESSEQUAL() != null) opKind = NodeKind.LE_NODE;
        else if (ctx.GREATEREQUAL() != null) opKind = NodeKind.GE_NODE;
        
        AST compNode = new AST(opKind, Type.BOOLEAN);
        
        // Adiciona conversões se necessárias
        if (leftType == Type.INTEGER && rightType == Type.REAL) {
            leftNode = Conv.createConvNode(Conv.I2R, leftNode);
        } else if (leftType == Type.REAL && rightType == Type.INTEGER) {
            rightNode = Conv.createConvNode(Conv.I2R, rightNode);
        }
        
        compNode.addChild(leftNode);
        compNode.addChild(rightNode);
        
        return compNode;
    }

    // Método auxiliar para obter a linha do operador de comparação
    private int getComparisonOperatorLine(PascalParser.ExpressionContext ctx) {
        if (ctx.EQUAL() != null) return ctx.EQUAL().getSymbol().getLine();
        if (ctx.NOTEQUAL() != null) return ctx.NOTEQUAL().getSymbol().getLine();
        if (ctx.LESS() != null) return ctx.LESS().getSymbol().getLine();
        if (ctx.GREATER() != null) return ctx.GREATER().getSymbol().getLine();
        if (ctx.LESSEQUAL() != null) return ctx.LESSEQUAL().getSymbol().getLine();
        if (ctx.GREATEREQUAL() != null) return ctx.GREATEREQUAL().getSymbol().getLine();
        return ctx.start.getLine(); // fallback
    }

    // Método para verificar expressões simples (+, -, OR)
    @Override
    public AST visitSimpleExpression(PascalParser.SimpleExpressionContext ctx) {
        AST currentNode = visit(ctx.term(0));
        Type currentType = currentNode.type;
        
        // Processa os operadores da esquerda para a direita
        for (int i = 1; i < ctx.term().size(); i++) {
            AST rightNode = visit(ctx.term(i));
            Type rightType = rightNode.type;
            String operator;
            int operatorLine;
            NodeKind opKind;
            Type resultType = Type.NO_TYPE;
            
            // Determina qual operador foi usado
            if (ctx.PLUS() != null && ctx.PLUS().size() >= i) {
                operator = "+";
                opKind = NodeKind.PLUS_NODE;
                operatorLine = ctx.PLUS(i-1).getSymbol().getLine();
                resultType = currentType.unifyArithmetic(rightType);
            } else if (ctx.MINUS() != null && ctx.MINUS().size() >= i) {
                operator = "-";
                opKind = NodeKind.MINUS_NODE;
                operatorLine = ctx.MINUS(i-1).getSymbol().getLine();
                resultType = currentType.unifyArithmetic(rightType);
            } else if (ctx.OR() != null && ctx.OR().size() >= i) {
                operator = "or";
                opKind = NodeKind.OR_NODE;
                operatorLine = ctx.OR(i-1).getSymbol().getLine();
                resultType = currentType.unifyLogical(rightType);
            } else {
                // Fallback - determina o operador pela análise dos tokens
                operator = getSimpleExpressionOperator(ctx, i-1);
                opKind = NodeKind.PLUS_NODE; // default
                operatorLine = ctx.start.getLine();
                
                // Tenta ambos os tipos de unificação
                Type arithmeticResult = currentType.unifyArithmetic(rightType);
                Type logicalResult = currentType.unifyLogical(rightType);
                
                if (arithmeticResult != Type.NO_TYPE) {
                    resultType = arithmeticResult;
                } else if (logicalResult != Type.NO_TYPE) {
                    resultType = logicalResult;
                    opKind = NodeKind.OR_NODE;
                }
            }
            
            if (resultType == Type.NO_TYPE) {
                System.err.printf("SEMANTIC ERROR (%d): operator '%s' cannot be applied to operands.\n", 
                                operatorLine, operator);
                System.exit(1);
            }
            
            // Cria nó do operador
            AST opNode = new AST(opKind, resultType);
            
            // Adiciona conversões se necessárias
            if (resultType == Type.REAL) {
                if (currentType == Type.INTEGER) {
                    currentNode = Conv.createConvNode(Conv.I2R, currentNode);
                }
                if (rightType == Type.INTEGER) {
                    rightNode = Conv.createConvNode(Conv.I2R, rightNode);
                }
            }
            
            opNode.addChild(currentNode);
            opNode.addChild(rightNode);
            
            currentNode = opNode;
            currentType = resultType;
        }
        
        return currentNode;
    }

    // Método auxiliar para identificar operador em simpleExpression
    private String getSimpleExpressionOperator(PascalParser.SimpleExpressionContext ctx, int index) {
        if (ctx.PLUS() != null && index < ctx.PLUS().size()) return "+";
        if (ctx.MINUS() != null && index < ctx.MINUS().size()) return "-";
        if (ctx.OR() != null && index < ctx.OR().size()) return "or";
        return "unknown";
    }

    // Método para verificar termos (*, /, DIV, MOD, AND)
    @Override
    public AST visitTerm(PascalParser.TermContext ctx) {
        AST currentNode = visit(ctx.factor(0));
        Type currentType = currentNode.type;
        
        // Processa os operadores da esquerda para a direita
        for (int i = 1; i < ctx.factor().size(); i++) {
            AST rightNode = visit(ctx.factor(i));
            Type rightType = rightNode.type;
            String operator;
            int operatorLine;
            NodeKind opKind;
            Type resultType = Type.NO_TYPE;
            
            // Determina qual operador foi usado e faz a verificação apropriada
            if (ctx.STAR() != null && ctx.STAR().size() >= i) {
                operator = "*";
                opKind = NodeKind.TIMES_NODE;
                operatorLine = ctx.STAR(i-1).getSymbol().getLine();
                resultType = currentType.unifyArithmetic(rightType);
            } else if (ctx.SLASH() != null && ctx.SLASH().size() >= i) {
                operator = "/";
                opKind = NodeKind.DIVIDE_NODE;
                operatorLine = ctx.SLASH(i-1).getSymbol().getLine();
                resultType = currentType.unifyArithmetic(rightType);
                // Divisão real sempre retorna REAL
                if (resultType != Type.NO_TYPE) {
                    resultType = Type.REAL;
                }
            } else if (ctx.DIV() != null && ctx.DIV().size() >= i) {
                operator = "div";
                opKind = NodeKind.DIV_NODE;
                operatorLine = ctx.DIV(i-1).getSymbol().getLine();
                // DIV só funciona com inteiros
                if (currentType == Type.INTEGER && rightType == Type.INTEGER) {
                    resultType = Type.INTEGER;
                }
            } else if (ctx.MOD() != null && ctx.MOD().size() >= i) {
                operator = "mod";
                opKind = NodeKind.MOD_NODE;
                operatorLine = ctx.MOD(i-1).getSymbol().getLine();
                // MOD só funciona com inteiros
                if (currentType == Type.INTEGER && rightType == Type.INTEGER) {
                    resultType = Type.INTEGER;
                }
            } else if (ctx.AND() != null && ctx.AND().size() >= i) {
                operator = "and";
                opKind = NodeKind.AND_NODE;
                operatorLine = ctx.AND(i-1).getSymbol().getLine();
                resultType = currentType.unifyLogical(rightType);
            } else {
                // Fallback
                operator = getTermOperator(ctx, i-1);
                opKind = NodeKind.TIMES_NODE; // default
                operatorLine = ctx.start.getLine();
                
                // Tenta diferentes tipos de unificação
                Type arithmeticResult = currentType.unifyArithmetic(rightType);
                Type logicalResult = currentType.unifyLogical(rightType);
                
                if (arithmeticResult != Type.NO_TYPE) {
                    resultType = arithmeticResult;
                } else if (logicalResult != Type.NO_TYPE) {
                    resultType = logicalResult;
                    opKind = NodeKind.AND_NODE;
                }
            }
            
            if (resultType == Type.NO_TYPE) {
                System.err.printf("SEMANTIC ERROR (%d): operator '%s' cannot be applied to '%s' and '%s'.\n", 
                                operatorLine, operator, currentType.toString(), rightType.toString());
                System.exit(1);
            }
            
            // Cria nó do operador
            AST opNode = new AST(opKind, resultType);
            
            // Adiciona conversões se necessárias
            if (resultType == Type.REAL && opKind != NodeKind.DIV_NODE && opKind != NodeKind.MOD_NODE) {
                if (currentType == Type.INTEGER) {
                    currentNode = Conv.createConvNode(Conv.I2R, currentNode);
                }
                if (rightType == Type.INTEGER) {
                    rightNode = Conv.createConvNode(Conv.I2R, rightNode);
                }
            }
            
            opNode.addChild(currentNode);
            opNode.addChild(rightNode);
            
            currentNode = opNode;
            currentType = resultType;
        }
        
        return currentNode;
    }

    // Método auxiliar para identificar operador em term
    private String getTermOperator(PascalParser.TermContext ctx, int index) {
        if (ctx.STAR() != null && index < ctx.STAR().size()) return "*";
        if (ctx.SLASH() != null && index < ctx.SLASH().size()) return "/";
        if (ctx.DIV() != null && index < ctx.DIV().size()) return "div";
        if (ctx.MOD() != null && index < ctx.MOD().size()) return "mod";
        if (ctx.AND() != null && index < ctx.AND().size()) return "and";
        return "unknown";
    }

    // Método para verificar statements compostos
    @Override
    public AST visitCompoundStatement(PascalParser.CompoundStatementContext ctx) {
        AST compoundNode = visit(ctx.statementList());
        // Encapsula em um nó compound para diferenciação estrutural
        AST wrapperNode = new AST(NodeKind.COMPOUND_STMT_NODE, Type.NO_TYPE);
        wrapperNode.addChild(compoundNode);
        return wrapperNode;
    }

    // Método para verificar lista de statements
    @Override
    public AST visitStatementList(PascalParser.StatementListContext ctx) {
        AST stmtListNode = new AST(NodeKind.BLOCK_NODE, Type.NO_TYPE);
        
        for (PascalParser.StatementContext stmtCtx : ctx.statement()) {
            AST stmtNode = visit(stmtCtx);
            if (stmtNode != null) { // Pode ser null para emptyStatement
                stmtListNode.addChild(stmtNode);
            }
        }
        
        return stmtListNode;
    }

    // Método para verificar statements genéricos
    @Override
    public AST visitStatement(PascalParser.StatementContext ctx) {
        if (ctx.compoundStatement() != null) {
            return visit(ctx.compoundStatement());
        } else if (ctx.assignmentStatement() != null) {
            return visit(ctx.assignmentStatement());
        } else if (ctx.procedureCall() != null) {
            return visit(ctx.procedureCall());
        } else if (ctx.ifStatement() != null) {
            return visit(ctx.ifStatement());
        } else if (ctx.whileStatement() != null) {
            return visit(ctx.whileStatement());
        } else if (ctx.emptyStatement() != null) {
            // emptyStatement retorna null
            return new AST(NodeKind.EMPTY_STMT_NODE, Type.NO_TYPE);
        }
        return null;
    }

    // Método para verificar statements IF
    @Override
    public AST visitIfStatement(PascalParser.IfStatementContext ctx) {
        AST conditionNode = visit(ctx.expression());
        Type conditionType = conditionNode.type;
        
        if (conditionType != Type.BOOLEAN) {
            int line = ctx.IF().getSymbol().getLine();
            System.err.printf("SEMANTIC ERROR (%d): if condition must be boolean, got '%s'.\n", 
                            line, conditionType.toString());
            System.exit(1);
        }
        
        AST ifNode = new AST(NodeKind.IF_NODE, Type.NO_TYPE);
        ifNode.addChild(conditionNode);
        
        // Adiciona statement THEN
        AST thenNode = visit(ctx.statement(0));
        ifNode.addChild(thenNode);
        
        // Adiciona statement ELSE se existir
        if (ctx.statement().size() > 1) {
            AST elseNode = visit(ctx.statement(1));
            ifNode.addChild(elseNode);
        }
        
        return ifNode;
    }

    // Método para verificar statements WHILE
    @Override
    public AST visitWhileStatement(PascalParser.WhileStatementContext ctx) {
        AST conditionNode = visit(ctx.expression());
        Type conditionType = conditionNode.type;
        
        if (conditionType != Type.BOOLEAN) {
            int line = ctx.WHILE().getSymbol().getLine();
            System.err.printf("SEMANTIC ERROR (%d): while condition must be boolean, got '%s'.\n", 
                            line, conditionType.toString());
            System.exit(1);
        }
        
        AST whileNode = new AST(NodeKind.WHILE_NODE, Type.NO_TYPE);
        whileNode.addChild(conditionNode);
        
        // Adiciona o statement do corpo do loop
        AST bodyNode = visit(ctx.statement());
        whileNode.addChild(bodyNode);
        
        return whileNode;
    }
}