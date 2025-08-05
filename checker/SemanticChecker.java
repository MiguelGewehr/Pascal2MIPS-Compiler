package checker;

import org.antlr.v4.runtime.Token;

import parser.PascalParser;
import parser.PascalParser.Assign_stmtContext;
import parser.PascalParser.ExprIdContext;
import parser.PascalParser.ExprStrValContext;
import parser.PascalParser.Read_stmtContext;
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
    	
}
