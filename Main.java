import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import checker.SemanticChecker;
import parser.PascalLexer;
import parser.PascalParser;
import ast.AST;
import codegen.CodegenVisitor;

// Classe principal para compilação Pascal -> MIPS
public class Main {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <pascal_file>");
            return;
        }
        
        String filename = args[0];
        
        try {
            // Cria o input stream a partir do arquivo
            CharStream input = CharStreams.fromFileName(filename);
            
            // === ANÁLISE LÉXICA ===
            PascalLexer lexer = new PascalLexer(input);
            lexer.removeErrorListeners();
            lexer.addErrorListener(new PascalErrorListener());
            
            // Cria o token stream
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            
            // === ANÁLISE SINTÁTICA ===
            PascalParser parser = new PascalParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new PascalErrorListener());
            
            // Faz o parsing começando pela regra 'program'
            ParseTree tree = parser.program();
            
            // === ANÁLISE SEMÂNTICA + CONSTRUÇÃO DA AST ===
            SemanticChecker checker = new SemanticChecker();
            AST ast = checker.visit(tree);
            
            if (ast == null) {
                System.err.println("Semantic analysis failed.");
                System.exit(1);
            }
            
            // === GERAÇÃO DE CÓDIGO MIPS ===
            CodegenVisitor codegen = new CodegenVisitor();
            String mipsCode = codegen.generate(ast, checker.getSymbolTable(), checker.getStrTable());
            
            // Imprime o código MIPS gerado
            System.out.println(mipsCode);

        } catch (IOException e) {
            System.err.printf("ERROR: Could not read file '%s': %s\n", filename, e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.printf("RUNTIME ERROR: %s\n", e.getMessage());
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
            System.err.printf("LEXICAL ERROR (%d): %s\n", line, msg);
        } else if (recognizer instanceof PascalParser) {
            System.err.printf("SYNTAX ERROR (%d): %s\n", line, msg);
        }
        System.exit(1);
    }
}