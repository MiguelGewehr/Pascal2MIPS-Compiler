package checker;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.PascalParser;
import parser.PascalParserBaseVisitor;
import tables.StrTable;
import tables.SymbolTable;
import typing.Type;
import entries.*;

import java.util.ArrayList;
import java.util.List;

// Analisador semântico de Pascal implementado como um visitor da ParseTree do ANTLR.
public class SemanticChecker extends PascalParserBaseVisitor<Type> {
    
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
    
    @Override
    public Type visitProgram(PascalParser.ProgramContext ctx) {
        visit(ctx.block());
        return Type.NO_TYPE;
    }
    
    @Override
    public Type visitBlock(PascalParser.BlockContext ctx) {
        
        // Processa seções na ordem correta
        if (ctx.constSection() != null) {
            visit(ctx.constSection());
        }
        
        if (ctx.varSection() != null) {
            visit(ctx.varSection());
        }
        
        // Processa declarações de subrotinas
        for (PascalParser.SubroutineDeclarationPartContext subCtx : ctx.subroutineDeclarationPart()) {
            visit(subCtx);
        }
        
        // Processa o statement composto
        if (ctx.compoundStatement() != null) {
            visit(ctx.compoundStatement());
        }
        
        return Type.NO_TYPE;
    }
    
    @Override
    public Type visitProcedureDeclaration(PascalParser.ProcedureDeclarationContext ctx) {
        
        String procName = ctx.IDENTIFIER().getText();
        int line = ctx.IDENTIFIER().getSymbol().getLine();
        
        // Verifica redeclaração
        Entry existing = symbolTable.lookupCurrentScope(procName);
        if (existing != null) {
            System.err.printf("SEMANTIC ERROR (%d): procedure '%s' already declared at line %d.\n", 
                             line, procName, existing.getLine());
            System.exit(1);
        }
        
        FuncEntry procEntry = new FuncEntry(procName, line, Type.NO_TYPE, new ArrayList<>());
        symbolTable.addEntry(procName, procEntry);
        
        // Abre novo escopo para a procedure
        symbolTable.openScope(procName + "_PROC");
        
        if (ctx.formalParameterList() != null) {
            visit(ctx.formalParameterList());
        }
        
        visit(ctx.block());
        symbolTable.closeScope();
        
        return Type.NO_TYPE;
    }
    
    @Override
    public Type visitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx) {
        
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
        
        FuncEntry funcEntry = new FuncEntry(funcName, line, returnType, new ArrayList<>());
        symbolTable.addEntry(funcName, funcEntry);
        
        // Abre novo escopo para a function
        symbolTable.openScope(funcName + "_FUNC");
        
        if (ctx.formalParameterList() != null) {
            visit(ctx.formalParameterList());
        }
        
        visit(ctx.block());
        symbolTable.closeScope();
        
        return Type.NO_TYPE;
    }
    
    @Override
    public Type visitFormalParameterSection(PascalParser.FormalParameterSectionContext ctx) {
        
        String typeName = ctx.IDENTIFIER().getText();
        Type paramType = getTypeFromName(typeName);
        
        if (paramType == Type.NO_TYPE) {
            System.err.printf("SEMANTIC ERROR: unknown parameter type '%s'.\n", typeName);
            System.exit(1);
        }
        
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
            
            ParamEntry paramEntry = new ParamEntry(paramName, line, paramType, false);
            symbolTable.addEntry(paramName, paramEntry);
        }
        
        return Type.NO_TYPE;
    }

    @Override
    public Type visitVarSection(PascalParser.VarSectionContext ctx) {
        super.visitVarSection(ctx);
        return Type.NO_TYPE;
    }

    @Override
    public Type visitVarDeclaration(PascalParser.VarDeclarationContext ctx) {
        
        // Processa o tipo da declaração
        processTypeDenoter(ctx.typeDenoter());
        
        // Para cada variável na lista
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            newVar(id.getSymbol());
        }
        return Type.NO_TYPE;
    }
    
    @Override
    public Type visitConstSection(PascalParser.ConstSectionContext ctx) {
        super.visitConstSection(ctx);
        return Type.NO_TYPE;
    }
    
    @Override
    public Type visitConstDefinition(PascalParser.ConstDefinitionContext ctx) {
        
        // Visita a constante para determinar seu tipo
        Type constType = visit(ctx.constant());
        lastDeclType = constType;
        lastArrayType = null; // Constantes não são arrays
        
        Token constToken = ctx.IDENTIFIER().getSymbol();
        Object value = null; // TODO: extrair valor real da constante
        
        newConst(constToken, value);
        
        return Type.NO_TYPE;
    }

    @Override
    public Type visitConstant(PascalParser.ConstantContext ctx) {
        if (ctx.signedNumber() != null) {
            return visit(ctx.signedNumber());
        } else if (ctx.CHARACTER() != null) {
            return Type.CHAR;
        } else if (ctx.STRING() != null) {
            st.add(ctx.STRING().getText());
            return Type.STRING;
        } else if (ctx.IDENTIFIER() != null) {
            // Referência a outra constante
            TypeInfo typeInfo = checkVar(ctx.IDENTIFIER().getSymbol());
            return typeInfo.type;
        }
        return Type.NO_TYPE;
    }

    @Override
    public Type visitSignedNumber(PascalParser.SignedNumberContext ctx) {
        if (ctx.INTEGER() != null) {
            return Type.INTEGER;
        } else if (ctx.REAL() != null) {
            return Type.REAL;
        }
        return Type.NO_TYPE;
    }

    @Override
    public Type visitVariable(PascalParser.VariableContext ctx) {
        if (ctx.LBRACK() != null) {
            // Acesso a array: IDENTIFIER[expression]
            Type indexType = visit(ctx.expression());
            TypeInfo resultType = checkArrayAccess(ctx.IDENTIFIER().getSymbol(), indexType, 
                                                  ctx.IDENTIFIER().getSymbol().getLine());
            return resultType.type;
        } else {
            // Variável simples
            TypeInfo typeInfo = checkVar(ctx.IDENTIFIER().getSymbol());
            return typeInfo.type;
        }
    }

    @Override
    public Type visitFactor(PascalParser.FactorContext ctx) {
        if (ctx.variable() != null) {
        // Variável ou acesso a array
        return visit(ctx.variable());
        
        } else if (ctx.IDENTIFIER() != null) {
            if (ctx.LPAREN() != null) {
                // Chamada de função
                return checkFunctionCall(ctx.IDENTIFIER().getSymbol(), ctx.expressionList());
            } else {
                // Variável simples
                TypeInfo typeInfo = checkVar(ctx.IDENTIFIER().getSymbol());
                return typeInfo.type;
            }
        } else if (ctx.INTEGER() != null) {
            return Type.INTEGER;
        } else if (ctx.REAL() != null) {
            return Type.REAL;
        } else if (ctx.CHARACTER() != null) {
            return Type.CHAR;
        } else if (ctx.STRING() != null) {
            st.add(ctx.STRING().getText());
            return Type.STRING;
        } else if (ctx.LPAREN() != null) {
            // Expressão entre parênteses
            return visit(ctx.expression());
        } else if (ctx.TRUE() != null || ctx.FALSE() != null) {
            return Type.BOOLEAN; 
        } else if (ctx.NOT() != null) {
            // Negação lógica - CORREÇÃO APLICADA AQUI
            Type factorType = visit(ctx.factor()); // Sem o (0)
            if (factorType != Type.BOOLEAN) {
                System.err.printf("SEMANTIC ERROR (%d): 'not' operator requires boolean operand, got '%s'.\n", 
                                 ctx.NOT().getSymbol().getLine(), factorType.toString());
                System.exit(1);
            }
            return Type.BOOLEAN;
        }
        
        return Type.NO_TYPE;
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
        
        return funcEntry.getEntryType();
    }
    
    @Override
    public Type visitProcedureCall(PascalParser.ProcedureCallContext ctx) {
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
        
        return Type.NO_TYPE;
    }

    /**
     * Processa um typeDenoter e define lastDeclType e lastArrayType adequadamente
     */
    private void processTypeDenoter(PascalParser.TypeDenoterContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            // Tipo simples
            String typeName = ctx.IDENTIFIER().getText();
            lastDeclType = getTypeFromName(typeName);
            lastArrayType = null;
            
            if (lastDeclType == Type.NO_TYPE) {
                System.err.printf("SEMANTIC ERROR: unknown type '%s'.\n", typeName);
                System.exit(1);
            }
        } else if (ctx.arrayType() != null) {
            // Tipo array
            lastDeclType = Type.ARRAY;
            lastArrayType = processArrayType(ctx.arrayType());
        } else {
            lastDeclType = Type.NO_TYPE;
            lastArrayType = null;
        }
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
}