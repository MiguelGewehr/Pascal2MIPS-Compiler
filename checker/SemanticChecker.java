// checker/SemanticChecker.java
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
 */
public class SemanticChecker extends PascalParserBaseVisitor<Void> {
    
    private StrTable st = new StrTable(); // Tabela de strings.
    private SymbolTable symbolTable = new SymbolTable(); // Nova tabela de símbolos
    private Type lastDeclType; // Variável "global" com o último tipo declarado.

    // Testa se o dado token foi declarado antes.
    private void checkVar(Token token) {
        String text = token.getText();
        int line = token.getLine();
        Entry entry = symbolTable.lookupEntry(text);
        
        if (entry == null) {
            System.err.printf("SEMANTIC ERROR (%d): variable '%s' was not declared.\n", line, text);
            System.exit(1);
        }
    }

    // Cria uma nova variável a partir do dado token.
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
        
        // Cria nova entrada de variável
        VarEntry varEntry = new VarEntry(text, line, lastDeclType);
        symbolTable.addEntry(text, varEntry);
    }
    
    // Cria uma nova constante
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
        
        // Cria nova entrada de constante
        ConstEntry constEntry = new ConstEntry(text, line, lastDeclType, value);
        symbolTable.addEntry(text, constEntry);
    }

    // Exibe o conteúdo das tabelas em stdout.
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
        // Programa já tem escopo global, apenas visita o bloco principal
        return super.visitProgram(ctx);
    }
    
    @Override
    public Void visitBlock(PascalParser.BlockContext ctx) {
        System.out.println("DEBUG: Visiting block");
        
        // Para o bloco principal do programa, não abrir novo escopo
        // Para blocos de functions/procedures, o escopo já foi aberto
        
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
        
        // Cria entrada da procedure (sem tipo de retorno)
        FuncEntry procEntry = new FuncEntry(procName, line, Type.NO_TYPE, new ArrayList<>());
        
        // Adiciona no escopo atual (antes de abrir o escopo da procedure)
        if (!symbolTable.addEntry(procName, procEntry)) {
            System.err.printf("SEMANTIC ERROR (%d): procedure '%s' already declared.\n", line, procName);
            System.exit(1);
        }
        
        // Abre escopo para a procedure
        symbolTable.openScope(procName + "_PROC");
        
        // Processa parâmetros se existirem
        if (ctx.formalParameterList() != null) {
            visit(ctx.formalParameterList());
        }
        
        // Visita o bloco da procedure
        visit(ctx.block());
        
        // Fecha escopo da procedure (será salvo no histórico)
        symbolTable.closeScope();
        
        return null;
    }
    
    @Override
    public Void visitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx) {
        String funcName = ctx.IDENTIFIER(0).getText(); // Primeiro IDENTIFIER é o nome
        String returnTypeName = ctx.IDENTIFIER(1).getText(); // Segundo é o tipo de retorno
        int line = ctx.IDENTIFIER(0).getSymbol().getLine();
        
        System.out.println("DEBUG: Declaring function: " + funcName);
        
        Type returnType = getTypeFromName(returnTypeName);
        
        // Cria entrada da função
        FuncEntry funcEntry = new FuncEntry(funcName, line, returnType, new ArrayList<>());
        
        // Adiciona no escopo atual (antes de abrir o escopo da função)
        if (!symbolTable.addEntry(funcName, funcEntry)) {
            System.err.printf("SEMANTIC ERROR (%d): function '%s' already declared.\n", line, funcName);
            System.exit(1);
        }
        
        // Abre escopo para a função
        symbolTable.openScope(funcName + "_FUNC");
        
        // Processa parâmetros se existirem
        if (ctx.formalParameterList() != null) {
            visit(ctx.formalParameterList());
        }
        
        // Visita o bloco da função
        visit(ctx.block());
        
        // Fecha escopo da função (será salvo no histórico)
        symbolTable.closeScope();
        
        return null;
    }
    
    @Override
    public Void visitFormalParameterSection(PascalParser.FormalParameterSectionContext ctx) {
        String typeName = ctx.IDENTIFIER().getText();
        Type paramType = getTypeFromName(typeName);
        lastDeclType = paramType;
        
        System.out.println("DEBUG: Declaring parameters of type: " + paramType);
        
        // Adiciona cada parâmetro como uma entry
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            String paramName = id.getText();
            int line = id.getSymbol().getLine();
            
            ParamEntry paramEntry = new ParamEntry(paramName, line, paramType, false); // TODO: detectar VAR
            
            if (!symbolTable.addEntry(paramName, paramEntry)) {
                System.err.printf("SEMANTIC ERROR (%d): parameter '%s' already declared.\n", line, paramName);
                System.exit(1);
            }
        }
        
        return null;
    }

    @Override
    public Void visitFactor(PascalParser.FactorContext ctx) {
        // Caso 1: Verifica se é uma string e adiciona na tabela de strings
        if (ctx.STRING() != null) {
            String str = ctx.STRING().getText();
            st.add(str);
        }

        // Caso 2: Verifica se é uma variável (ID sem parênteses) e checa declaração
        if (ctx.IDENTIFIER() != null && ctx.LPAREN() == null) {
            checkVar(ctx.IDENTIFIER().getSymbol());
        }

        // Continua a visitação padrão
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
        
        // Obtém o tipo da declaração
        lastDeclType = getTypeFromDenoter(ctx.typeDenoter());
        System.out.println("DEBUG: Variable type: " + lastDeclType);

        // Para cada variável na lista
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            System.out.println("DEBUG: Declaring variable: " + id.getText());
            newVar(id.getSymbol()); // Adiciona à tabela de símbolos
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
        
        // TODO: avaliar a constante para obter o valor e tipo
        lastDeclType = Type.INTEGER; // Por enquanto, assumindo integer para teste
        
        Token constToken = ctx.IDENTIFIER().getSymbol();
        Object value = null; // TODO: extrair valor da constante
        
        newConst(constToken, value);
        
        return super.visitConstDefinition(ctx);
    }

    private Type getTypeFromDenoter(PascalParser.TypeDenoterContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            String typeName = ctx.IDENTIFIER().getText();
            return getTypeFromName(typeName);
        }
        if (ctx.arrayType() != null) {
            // TODO: implementar tipos array
            return Type.NO_TYPE;
        }
        return Type.NO_TYPE;
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
        checkVar(ctx.IDENTIFIER().getSymbol()); // Verifica se variável foi declarada
        return super.visitVariable(ctx);
    }
}