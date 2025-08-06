package checker;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.PascalParser;
import parser.PascalParserBaseVisitor;
import tables.StrTable;
import tables.VarTable;
import typing.Type;

/*
 * Analisador semântico de Pascal implementado como um visitor da ParseTree do ANTLR.   
*/
public class SemanticChecker extends PascalParserBaseVisitor<Void> {

	private StrTable st = new StrTable();   // Tabela de strings.
    private VarTable vt = new VarTable();   // Tabela de variáveis.
    
    private Type lastDeclType;  // Variável "global" com o último tipo declarado.

    // Testa se o dado token foi declarado antes.
    private void checkVar(Token token) {
    	String text = token.getText();
    	int line = token.getLine();
   		int idx = vt.lookupVar(text);
    	if (idx == -1) {
    		System.err.printf("SEMANTIC ERROR (%d): variable '%s' was not declared.\n", line, text);
    		System.exit(1);
            return; // Never reached.
        }
    }
    
    // Cria uma nova variável a partir do dado token.
    private void newVar(Token token) {
    	String text = token.getText();
    	int line = token.getLine();
   		int idx = vt.lookupVar(text);
        if (idx != -1) {
        	System.err.printf("SEMANTIC ERROR (%d): variable '%s' already declared at line %d.\n", line, text, vt.getLine(idx));
        	System.exit(1);
            return; // Never reached.
        }
        vt.addVar(text, line, lastDeclType);
    }

    // Exibe o conteúdo das tabelas em stdout.
    public void printTables() {
        System.out.print("\n\n");
        System.out.print(st);
        System.out.print("\n\n");
    	System.out.print(vt);
    	System.out.print("\n\n");
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
		
		// Continua a visitação padrão para outros casos (números, expressões entre parênteses, etc.)
		return super.visitFactor(ctx);
	}

	@Override
	public Void visitVarDeclaration(PascalParser.VarDeclarationContext ctx) {
		// Obtém o tipo da declaração
		lastDeclType = getTypeFromDenoter(ctx.typeDenoter());
		
		// Para cada variável na lista
		for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
			newVar(id.getSymbol());  // Adiciona à tabela de símbolos
		}
		return null;
	}

	private Type getTypeFromDenoter(PascalParser.TypeDenoterContext ctx) {
		if (ctx.IDENTIFIER() != null) {
			String typeName = ctx.IDENTIFIER().getText();
			return Type.valueOf(typeName.toUpperCase());
		}
		return Type.NO_TYPE;  // Caso de array (não implementado)
	}

	@Override
	public Void visitVariable(PascalParser.VariableContext ctx) {
		checkVar(ctx.IDENTIFIER().getSymbol());  // Verifica se variável foi declarada
		return super.visitVariable(ctx);
	}

}
