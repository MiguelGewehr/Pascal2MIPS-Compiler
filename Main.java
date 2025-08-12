import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import checker.SemanticChecker;
import interpreter.Interpreter;
import parser.PascalLexer;
import parser.PascalParser;
import ast.AST;

// Classe principal que demonstra como usar os analisadores em conjunto
// para Pascal ISO 7185 com construção de AST e interpretação
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <pascal_file>");
            return;
        }
        
        String filename = args[0];
        
        try {
            // Cria o input stream a partir do arquivo
            CharStream input = CharStreams.fromFileName(filename);
            
            // === ANÁLISE LÉXICA ===
            PascalLexer lexer = new PascalLexer(input);
            // Configura o ErrorListener personalizado se necessário
            lexer.removeErrorListeners(); // Remove o listener padrão
            lexer.addErrorListener(new PascalErrorListener());
            
            // Cria o token stream
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            
            // === ANÁLISE SINTÁTICA ===
            PascalParser parser = new PascalParser(tokens);
            // Configura o ErrorListener personalizado se necessário
            parser.removeErrorListeners(); // Remove o listener padrão
            parser.addErrorListener(new PascalErrorListener());
            
            // Faz o parsing começando pela regra 'program'
            ParseTree tree = parser.program();
            
            // === ANÁLISE SEMÂNTICA + CONSTRUÇÃO DA AST ===
            SemanticChecker checker = new SemanticChecker();
            // Visita a árvore de parsing e constrói a AST
            AST ast = checker.visit(tree);
            
            // Se chegou até aqui, não houve erros
            System.out.println("SUCCESS.");
            
            // Imprime as tabelas de símbolos e strings
            checker.printTables();
            
            // Imprime a AST em formato DOT (para stderr)
            checker.printAST(ast);
            
            // === INTERPRETAÇÃO ===
            if (ast != null) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("STARTING INTERPRETATION");
                System.out.println("=".repeat(50));
                
                Interpreter interpreter = new Interpreter();
                interpreter.interpret(ast);
                
                System.out.println("=".repeat(50));
                System.out.println("INTERPRETATION COMPLETED");
                System.out.println("=".repeat(50));
            }
            
        } catch (IOException e) {
            System.err.printf("ERROR: Could not read file '%s': %s\n", filename, e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        }
    }
}

// Classe para tratamento personalizado de erros léxicos e sintáticos
class PascalErrorListener extends org.antlr.v4.runtime.BaseErrorListener {
    @Override
    public void syntaxError(org.antlr.v4.runtime.Recognizer<?, ?> recognizer,
                           Object offendingSymbol,
                           int line,
                           int charPositionInLine,
                           String msg,
                           org.antlr.v4.runtime.RecognitionException e) {
        if (recognizer instanceof PascalLexer) {
            // Erro léxico
            System.err.printf("LEXICAL ERROR (%d): %s\n", line, msg);
        } else if (recognizer instanceof PascalParser) {
            // Erro sintático
            System.err.printf("SYNTAX ERROR (%d): %s\n", line, msg);
        }
        System.exit(1);
    }
}