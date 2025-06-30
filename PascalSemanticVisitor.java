import parser.*;
import java.util.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PascalSemanticVisitor extends PascalParserBaseVisitor<Void> {

    private final Map<String, Simbolo> symbolTable = new HashMap<>();
    private final Set<String> stringLiterals = new HashSet<>();
    private boolean hasErrors = false;

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
                "global",
                id.getSymbol().getLine()
            );
            symbolTable.put(nome, simbolo);
        }

        return super.visitVarDeclaration(ctx);
    }

    @Override
    public Void visitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx) {
        String nome = ctx.getChild(1).getText(); // IDENTIFIER
        String tipoRetorno = "desconhecido";

        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().equals(":") && i + 1 < ctx.getChildCount()) {
                tipoRetorno = ctx.getChild(i + 1).getText();
                break;
            }
        }

        Simbolo simbolo = new Simbolo(nome, tipoRetorno, "função", "global", ctx.getStart().getLine());
        symbolTable.put(nome, simbolo);

        return super.visitFunctionDeclaration(ctx);
    }



    @Override
    public Void visitAssignmentStatement(PascalParser.AssignmentStatementContext ctx) {
        String varName = ctx.variable().IDENTIFIER().getText();
        if (!symbolTable.containsKey(varName)) {
            System.err.println("Erro: variável '" + varName + "' não declarada");
            hasErrors = true;
        }
        visit(ctx.expression());
        return null;
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

        // Verifica se o procedimento está declarado
        Simbolo s = symbolTable.get(procName);
        if (s == null || (!s.categoria.equals("procedimento") && !s.categoria.equals("função"))) {
            System.err.println("Erro: chamada para procedimento ou função não declarado: " + procName);
            hasErrors = true;
        }

        return null;
    }

    @Override
    public Void visitExpressionItem(PascalParser.ExpressionItemContext ctx) {
        if (ctx.expression() != null) {
            visit(ctx.expression());
        } else if (ctx.formattedExpression() != null) {
            visit(ctx.formattedExpression());
        }
        return null;
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
        System.out.println("Símbolos declarados:");
        symbolTable.forEach((nome, simbolo) -> {
            System.out.printf("  %s : %s (%s, escopo: %s, linha: %d)\n",
                simbolo.nome, simbolo.tipo, simbolo.categoria, simbolo.escopo, simbolo.linha);
        });

        System.out.println("\nStrings literais encontradas:");
        stringLiterals.forEach(str -> System.out.println("  " + str));

        System.out.println("\nStatus: " + (hasErrors ? "ERROS ENCONTRADOS" : "OK"));
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}
