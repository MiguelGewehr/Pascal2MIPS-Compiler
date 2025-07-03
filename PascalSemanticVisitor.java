import parser.*;
import java.util.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PascalSemanticVisitor extends PascalParserBaseVisitor<Void> {

    private final Map<String, Map<String, Simbolo>> scopes = new HashMap<>();
    private String currentScope = "global";
    private final Set<String> stringLiterals = new HashSet<>();
    private boolean hasErrors = false;

    public PascalSemanticVisitor() {
        // Inicializa o escopo global
        scopes.put("global", new HashMap<>());
    }

    @Override
    public Void visitVarDeclaration(PascalParser.VarDeclarationContext ctx) {
        String tipo = ctx.typeDenoter().getText();
        boolean isVetor = tipo.toLowerCase().contains("array");

        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            String nome = id.getText();
            Simbolo simbolo = new Simbolo(
                nome,
                tipo,
                isVetor ? "vetor" : "variável",
                id.getSymbol().getLine()
            );
            
            // Adiciona no escopo atual
            scopes.get(currentScope).put(nome, simbolo);
        }

        return super.visitVarDeclaration(ctx);
    }

    @Override
    public Void visitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx) {
        String nomeFuncao = ctx.getChild(1).getText(); // IDENTIFIER
        String tipoRetorno = "desconhecido";

        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().equals(":") && i + 1 < ctx.getChildCount()) {
                tipoRetorno = ctx.getChild(i + 1).getText();
                break;
            }
        }

        // Adiciona a função no escopo global
        Simbolo simboloFuncao = new Simbolo(
            nomeFuncao, 
            tipoRetorno, 
            "função", 
            ctx.getStart().getLine()
        );
        scopes.get("global").put(nomeFuncao, simboloFuncao);

        // Cria um novo escopo para a função
        scopes.put(nomeFuncao, new HashMap<>());
        String escopoAnterior = currentScope;
        currentScope = nomeFuncao;

        // Visita os parâmetros e o corpo da função
        super.visitFunctionDeclaration(ctx);

        // Restaura o escopo anterior
        currentScope = escopoAnterior;
        return null;
    }

    @Override
    public Void visitProcedureDeclaration(PascalParser.ProcedureDeclarationContext ctx) {
        String nomeProcedimento = ctx.getChild(1).getText(); // IDENTIFIER

        // Adiciona o procedimento no escopo global
        Simbolo simboloProc = new Simbolo(
            nomeProcedimento, 
            "void", 
            "procedimento", 
            ctx.getStart().getLine()
        );
        scopes.get("global").put(nomeProcedimento, simboloProc);

        // Cria um novo escopo para o procedimento
        scopes.put(nomeProcedimento, new HashMap<>());
        String escopoAnterior = currentScope;
        currentScope = nomeProcedimento;

        // Visita os parâmetros e o corpo do procedimento
        super.visitProcedureDeclaration(ctx);

        // Restaura o escopo anterior
        currentScope = escopoAnterior;
        return null;
    }

    @Override
    public Void visitFormalParameterSection(PascalParser.FormalParameterSectionContext ctx) {
        // Adiciona parâmetros no escopo atual (que será da função/procedimento)
        String tipo = ctx.IDENTIFIER().getText();
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            String nome = id.getText();
            Simbolo simbolo = new Simbolo(
                nome,
                tipo,
                "parâmetro",
                id.getSymbol().getLine()
            );
            scopes.get(currentScope).put(nome, simbolo);
        }
        return null;
    }

    @Override
    public Void visitAssignmentStatement(PascalParser.AssignmentStatementContext ctx) {
        String varName = ctx.variable().IDENTIFIER().getText();
        
        // Procura a variável nos escopos, começando pelo atual
        Simbolo simbolo = findSymbol(varName);
        
        if (simbolo == null) {
            System.err.println("Erro: variável '" + varName + "' não declarada");
            hasErrors = true;
        } else if (simbolo.categoria.equals("constante")) {
            System.err.println("Erro: tentativa de modificar constante '" + varName + "'");
            hasErrors = true;
        }
        
        visit(ctx.expression());
        return null;
    }

    private Simbolo findSymbol(String name) {
        // Primeiro procura no escopo atual
        Simbolo simbolo = scopes.get(currentScope).get(name);
        if (simbolo != null) return simbolo;
        
        // Se não encontrou, procura no escopo global
        return scopes.get("global").get(name);
    }

    @Override
    public Void visitProcedureCall(PascalParser.ProcedureCallContext ctx) {
        String procName = ctx.IDENTIFIER().getText();

        if (procName.equals("read") || procName.equals("readln") || 
            procName.equals("write") || procName.equals("writeln")) {
            if (ctx.expressionList() != null) {
                for (var exprItem : ctx.expressionList().expressionItem()) {
                    visit(exprItem);
                }
            }
            return null;
        }

        // Verifica se o procedimento está declarado no escopo global
        Simbolo s = scopes.get("global").get(procName);
        if (s == null || (!s.categoria.equals("procedimento") && !s.categoria.equals("função"))) {
            System.err.println("Erro: chamada para procedimento ou função não declarado: " + procName);
            hasErrors = true;
        }

        return null;
    }

    @Override
    public Void visitConstDefinition(PascalParser.ConstDefinitionContext ctx) {
        String nome = ctx.IDENTIFIER().getText();
        String tipo = inferConstantType(ctx.constant());
        
        Simbolo simbolo = new Simbolo(
            nome,
            tipo,
            "constante",
            ctx.getStart().getLine()
        );
        scopes.get(currentScope).put(nome, simbolo);
        return super.visitConstDefinition(ctx);
    }

    private String inferConstantType(PascalParser.ConstantContext ctx) {
        if (ctx.signedNumber() != null) {
            if (ctx.signedNumber().REAL() != null) {
                return "real";
            }
            return "integer";
        }
        if (ctx.CHARACTER() != null) return "char";
        if (ctx.STRING() != null) return "string";
        if (ctx.IDENTIFIER() != null) return "identifier"; // constante definida anteriormente
        return "desconhecido";
    }

    @Override
    public Void visitFactor(PascalParser.FactorContext ctx) {
        if (ctx.STRING() != null) {
            stringLiterals.add(ctx.STRING().getText());
        }
        return super.visitFactor(ctx);
    }

    public void printAnalysis() {
        System.out.println("\n=== Análise Semântica ===");
        
        System.out.println("Símbolos declarados por escopo:");
        scopes.forEach((escopo, tabela) -> {
            System.out.println("\nEscopo: " + escopo);
            tabela.forEach((nome, simbolo) -> {
                System.out.printf("  %s : %s (%s, linha: %d)\n",
                    simbolo.nome, simbolo.tipo, simbolo.categoria, simbolo.linha);
            });
        });

        //System.out.println("\nStrings literais encontradas:");
        //stringLiterals.forEach(str -> System.out.println("  " + str));

        System.out.println("\nStatus: " + (hasErrors ? "ERROS ENCONTRADOS" : "OK"));
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}