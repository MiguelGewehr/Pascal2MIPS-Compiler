/*import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Uso: java Main <arquivo.pas>");
            System.exit(1);
        }

        CharStream input = CharStreams.fromFileName(args[0]);
        PascalLexer lexer = new PascalLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PascalParser parser = new PascalParser(tokens);

        ParseTree tree = parser.program();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Erros sintáticos encontrados!");
            System.exit(1);
        }

        PascalSemanticVisitor visitor = new PascalSemanticVisitor();
        visitor.visit(tree);

        visitor.printAnalysis();
        System.exit(visitor.hasErrors() ? 1 : 0);
    }
}*/

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.PascalLexer;
import parser.PascalParser;
import checker.SemanticChecker;

public class Main {

	/*
	 *  Esta função espera um único argumento: o nome do
	 *  programa a ser compilado. Em um código real certamente
	 *  deveria haver alguma verificação de erro mas ela foi
	 *  omitida aqui para simplificar o código e facilitar a leitura.
	 */
	public static void main(String[] args) throws IOException {
		
        // Cria um CharStream que lê os caracteres de um arquivo.
		CharStream input = CharStreams.fromFileName(args[0]);

		// Cria um lexer que consome a entrada do CharStream.
		PascalLexer lexer = new PascalLexer(input);

		// Cria um buffer de tokens vindos do lexer.
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Cria um parser que consome os tokens do buffer.
		PascalParser parser = new PascalParser(tokens);

		// Começa o processo de parsing na regra 'program'.
		ParseTree tree = parser.program();

		if (parser.getNumberOfSyntaxErrors() != 0) {
            // Houve algum erro sintático. Termina a compilação aqui.
			System.out.println("ERRO SINTATICO ENCONTRADO");
			return;
		}

		// Cria o analisador semântico e visita a ParseTree para fazer a análise.
		SemanticChecker checker = new SemanticChecker();
		checker.visit(tree);

		// Saída final.
		System.out.println("PARSE SUCCESSFUL!");
		checker.printTables();
	}

}
