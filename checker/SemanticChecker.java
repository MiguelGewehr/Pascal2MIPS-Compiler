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

/**
 * Analisador semântico de Pascal implementado como um visitor da ParseTree do ANTLR.
 * Versão melhorada com suporte completo para arrays.
 */
public class SemanticChecker extends PascalParserBaseVisitor<Void> {
    
    private StrTable st = new StrTable(); // Tabela de strings (ainda útil para otimização)
    private SymbolTable symbolTable = new SymbolTable();
    private Type lastDeclType; // Para tipos primitivos
    private Type.ArrayType lastArrayType; // Para tipos array

    /**
     * Testa se o dado token foi declarado antes.
     */
    private void checkVar(Token token) {
        String text = token.getText();
        int line = token.getLine();
        Entry entry = symbolTable.lookupEntry(text);
        
        if (entry == null) {
            System.err.printf("SEMANTIC ERROR (%d): variable '%s' was not declared.\n", line, text);
            System.exit(1);
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
            // Cria entrada de array
            entry = new ArrayEntry(text, line, lastArrayType);
        } else {
            // Cria entrada de variável normal
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
        
        // Verifica se já existe no escopo atual
        Entry existing = symbolTable.lookupCurrentScope(text);
        if (existing != null) {
            System.err.printf("SEMANTIC ERROR (%d): constant '%s' already declared at line %d.\n", 
                             line, text, existing.getLine());
            System.exit(1);
        }
        
        // Constantes não podem ser arrays
        ConstEntry constEntry = new ConstEntry(text, line, lastDeclType, value);
        symbolTable.addEntry(text, constEntry);
    }
    
    /**
     * Valida acesso a array
     */
    private void checkArrayAccess(Token arrayToken, PascalParser.ExpressionContext indexExpr) {
        String arrayName = arrayToken.getText();
        int line = arrayToken.getLine();
        
        Entry entry = symbolTable.lookupEntry(arrayName);
        if (entry == null) {
            System.err.printf("SEMANTIC ERROR (%d): array '%s' was not declared.\n", line, arrayName);
            System.exit(1);
        }
        
        if (!(entry instanceof ArrayEntry)) {
            System.err.printf("SEMANTIC ERROR (%d): '%s' is not an array.\n", line, arrayName);
            System.exit(1);
        }
        
        // TODO: Aqui poderia implementar verificação de tipos da expressão do índice
        // Por enquanto, apenas verifica se o array existe e é realmente um array
        
        System.out.println("DEBUG: Valid array access to " + arrayName);
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
    public Void visitProgram(PascalParser.ProgramContext ctx) {
        System.out.println("DEBUG: Visiting program");
        return super.visitProgram(ctx);
    }
    
    @Override
    public Void visitBlock(PascalParser.BlockContext ctx) {
        System.out.println("DEBUG: Visiting block");
        
        // Visita as seções na ordem correta
        if (ctx.constSection() != null) {
            System.out.println("DEBUG: Visiting const section");
            visit(ctx.constSection());
        }
        
        if (ctx.varSection() != null) {
            System.out.println("DEBUG: Visiting var section");
            visit(ctx.varSection());
        }
        
        // Visita declarações de subrotinas
        for (PascalParser.SubroutineDeclarationPartContext subCtx : ctx.subroutineDeclarationPart()) {
            visit(subCtx);
        }
        
        // Visita o statement composto
        if (ctx.compoundStatement() != null) {
            System.out.println("DEBUG: Visiting compound statement");
            visit(ctx.compoundStatement());
        }
        
        return null;
    }
    
    @Override
    public Void visitProcedureDeclaration(PascalParser.ProcedureDeclarationContext ctx) {
        String procName = ctx.IDENTIFIER().getText();
        int line = ctx.IDENTIFIER().getSymbol().getLine();
        
        System.out.println("DEBUG: Declaring procedure: " + procName);
        
        FuncEntry procEntry = new FuncEntry(procName, line, Type.NO_TYPE, new ArrayList<>());
        
        if (!symbolTable.addEntry(procName, procEntry)) {
            System.err.printf("SEMANTIC ERROR (%d): procedure '%s' already declared.\n", line, procName);
            System.exit(1);
        }
        
        symbolTable.openScope(procName + "_PROC");
        
        if (ctx.formalParameterList() != null) {
            visit(ctx.formalParameterList());
        }
        
        visit(ctx.block());
        symbolTable.closeScope();
        
        return null;
    }
    
    @Override
    public Void visitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx) {
        String funcName = ctx.IDENTIFIER(0).getText();
        String returnTypeName = ctx.IDENTIFIER(1).getText();
        int line = ctx.IDENTIFIER(0).getSymbol().getLine();
        
        System.out.println("DEBUG: Declaring function: " + funcName);
        
        Type returnType = getTypeFromName(returnTypeName);
        FuncEntry funcEntry = new FuncEntry(funcName, line, returnType, new ArrayList<>());
        
        if (!symbolTable.addEntry(funcName, funcEntry)) {
            System.err.printf("SEMANTIC ERROR (%d): function '%s' already declared.\n", line, funcName);
            System.exit(1);
        }
        
        symbolTable.openScope(funcName + "_FUNC");
        
        if (ctx.formalParameterList() != null) {
            visit(ctx.formalParameterList());
        }
        
        visit(ctx.block());
        symbolTable.closeScope();
        
        return null;
    }
    
    @Override
    public Void visitFormalParameterSection(PascalParser.FormalParameterSectionContext ctx) {
        String typeName = ctx.IDENTIFIER().getText();
        Type paramType = getTypeFromName(typeName);
        lastDeclType = paramType;
        
        System.out.println("DEBUG: Declaring parameters of type: " + paramType);
        
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            String paramName = id.getText();
            int line = id.getSymbol().getLine();
            
            ParamEntry paramEntry = new ParamEntry(paramName, line, paramType, false);
            
            if (!symbolTable.addEntry(paramName, paramEntry)) {
                System.err.printf("SEMANTIC ERROR (%d): parameter '%s' already declared.\n", line, paramName);
                System.exit(1);
            }
        }
        
        return null;
    }

    @Override
    public Void visitFactor(PascalParser.FactorContext ctx) {
        // Caso 1: String literal
        if (ctx.STRING() != null) {
            String str = ctx.STRING().getText();
            st.add(str);
        }

        // Caso 2: Variável simples (ID sem parênteses)
        if (ctx.IDENTIFIER() != null && ctx.LPAREN() == null) {
            checkVar(ctx.IDENTIFIER().getSymbol());
        }

        return super.visitFactor(ctx);
    }

    @Override
    public Void visitVarSection(PascalParser.VarSectionContext ctx) {
        System.out.println("DEBUG: Processing var section with " + ctx.varDeclaration().size() + " declarations");
        return super.visitVarSection(ctx);
    }

    @Override
    public Void visitVarDeclaration(PascalParser.VarDeclarationContext ctx) {
        System.out.println("DEBUG: Processing var declaration");
        
        // Processa o tipo da declaração
        processTypeDenoter(ctx.typeDenoter());
        System.out.println("DEBUG: Variable type: " + 
                          (lastDeclType == Type.ARRAY ? lastArrayType.toString() : lastDeclType.toString()));

        // Para cada variável na lista
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            System.out.println("DEBUG: Declaring variable: " + id.getText());
            newVar(id.getSymbol());
        }
        return null;
    }
    
    @Override
    public Void visitConstSection(PascalParser.ConstSectionContext ctx) {
        System.out.println("DEBUG: Processing const section");
        return super.visitConstSection(ctx);
    }
    
    @Override
    public Void visitConstDefinition(PascalParser.ConstDefinitionContext ctx) {
        System.out.println("DEBUG: Processing const definition: " + ctx.IDENTIFIER().getText());
        
        // TODO: avaliar a constante para obter o valor e tipo real
        lastDeclType = Type.INTEGER; // Por enquanto, assumindo integer
        lastArrayType = null; // Constantes não são arrays
        
        Token constToken = ctx.IDENTIFIER().getSymbol();
        Object value = null; // TODO: extrair valor da constante
        
        newConst(constToken, value);
        
        return super.visitConstDefinition(ctx);
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
        } else {
            // TODO: Array de array (multidimensional) - por enquanto não suportado
            System.err.println("SEMANTIC ERROR: Multidimensional arrays not yet supported.");
            System.exit(1);
            elementType = Type.NO_TYPE;
        }
        
        System.out.println("DEBUG: Array type: [" + startIndex + ".." + endIndex + "] of " + elementType);
        
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
            // Para REAL, pega só a parte inteira (pode haver problemas aqui)
            numberText = ctx.REAL().getText().split("\\.")[0];
        }
        
        int number = Integer.parseInt(numberText);
        
        // Aplica o sinal se necessário
        if (ctx.MINUS() != null) {
            number = -number;
        }
        
        return number;
    }
    
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
    public Void visitVariable(PascalParser.VariableContext ctx) {
        if (ctx.LBRACK() != null) {
            // Acesso a array: IDENTIFIER[expression]
            checkArrayAccess(ctx.IDENTIFIER().getSymbol(), ctx.expression());
        } else {
            // Variável simples
            checkVar(ctx.IDENTIFIER().getSymbol());
        }
        return super.visitVariable(ctx);
    }
}