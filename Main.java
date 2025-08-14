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
    
    private static boolean debugMode = true;
    private static boolean interpretMode = true;
    
    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            return;
        }
        
        // Processa argumentos da linha de comando
        String filename = null;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-debug", "-d" -> debugMode = true;
                case "-no-debug", "-nd" -> debugMode = false;
                case "-interpret", "-i" -> interpretMode = true;
                case "-no-interpret", "-ni" -> interpretMode = false;
                case "-help", "-h" -> {
                    printUsage();
                    return;
                }
                default -> {
                    if (!args[i].startsWith("-")) {
                        filename = args[i];
                    } else {
                        System.err.println("Unknown option: " + args[i]);
                        printUsage();
                        return;
                    }
                }
            }
        }
        
        if (filename == null) {
            System.err.println("Error: No input file specified.");
            printUsage();
            return;
        }
        
        // Configura modo debug no interpretador
        Interpreter.setDebugMode(debugMode);
        
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
            if (debugMode) {
                System.out.println("SUCCESS.");
                
                // Imprime as tabelas de símbolos e strings
                checker.printTables();
                
                // Imprime a AST em formato DOT (para stderr)
                checker.printAST(ast);
            }
            
            // === INTERPRETAÇÃO ===
            if (interpretMode && ast != null) {
                if (debugMode) {
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("STARTING INTERPRETATION");
                    System.out.println("=".repeat(50));
                }
                
                Interpreter interpreter = new Interpreter(debugMode);
                try {
                    interpreter.interpret(ast);
                } finally {
                    interpreter.cleanup(); // Fecha resources
                }
                
                if (debugMode) {
                    System.out.println("=".repeat(50));
                    System.out.println("INTERPRETATION COMPLETED");
                    System.out.println("=".repeat(50));
                }
            }
            
        } catch (IOException e) {
            System.err.printf("ERROR: Could not read file '%s': %s\n", filename, e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.printf("RUNTIME ERROR: %s\n", e.getMessage());
            if (debugMode) {
                e.printStackTrace();
            }
            System.exit(1);
        } catch (Exception e) {
            System.err.printf("ERROR: %s\n", e.getMessage());
            if (debugMode) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }
    
    private static void printUsage() {
        System.out.println("Usage: java Main [options] <pascal_file>");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  -debug, -d         Enable debug mode (shows compilation details)");
        System.out.println("  -no-debug, -nd     Disable debug mode (default)");
        System.out.println("  -interpret, -i     Enable interpretation (default)");
        System.out.println("  -no-interpret, -ni Disable interpretation (compile only)");
        System.out.println("  -help, -h          Show this help message");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java Main program.pas                  # Run with default settings");
        System.out.println("  java Main -debug program.pas           # Run with debug information");
        System.out.println("  java Main -no-interpret program.pas    # Compile only, don't run");
        System.out.println("  java Main -d -ni program.pas           # Debug compile-only mode");
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