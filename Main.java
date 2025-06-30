import org.antlr.v4.runtime.*;
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
}