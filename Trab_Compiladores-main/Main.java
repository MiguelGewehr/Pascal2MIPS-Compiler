import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import parser.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando análise do programa Pascal...");
        
        if (args.length < 1) {
            System.err.println("Uso: java Main <arquivo.pas>");
            System.exit(1);
        }

        // Análise léxica e sintática
        CharStream input = CharStreams.fromFileName(args[0]);
        PascalLexer lexer = new PascalLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PascalParser parser = new PascalParser(tokens);
        ParseTree tree = parser.program();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("\n[ERRO] Análise sintática contém erros.");
            System.exit(1);
        }

        // Análise semântica
        PascalSemanticVisitor visitor = new PascalSemanticVisitor();
        visitor.visit(tree);

        // Verificação de erros
        if (visitor.hasErrors()) {
            System.err.println("\n[ERRO] Análise semântica encontrou problemas.");
            System.exit(1);
        }

        // Exibir resultados se não houver erros
        System.out.println("\n[OK] Análise concluída sem erros.");
        visitor.printSymbols();
        System.exit(0);
    }
}