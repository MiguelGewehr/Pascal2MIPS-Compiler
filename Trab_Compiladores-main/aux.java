import parser.*;
import java.util.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PascalSemanticVisitor extends PascalParserBaseVisitor<Void> {
    private final Map<String, String> symbolTable = new HashMap<>();
    private final Set<String> stringLiterals = new HashSet<>();
    private boolean hasErrors = false;

    @Override
    public Void visitVarDeclaration(PascalParser.VarDeclarationContext ctx) {
        String type = ctx.typeDenoter().getText();
        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            symbolTable.put(id.getText(), type);
            System.out.println("[DEBUG] Variável declarada: " + id.getText() + " : " + type);
        }
        return super.visitVarDeclaration(ctx);
    }

    @Override
    public Void visitProcedureCall(PascalParser.ProcedureCallContext ctx) {
        String procName = ctx.IDENTIFIER().getText();
        
        // Adiciona suporte para 'read' e 'write'
        if (procName.equals("read") || procName.equals("readln") || 
            procName.equals("write") || procName.equals("writeln")) {
            
            if (ctx.expressionList() != null) {
                for (var exprItem : ctx.expressionList().expressionItem()) {
                    if (exprItem.expression() != null) {
                        checkVariableUsage(exprItem.expression(), procName);
                    }
                }
            }
            return null;
        }
        
        System.err.println("Error: procedimento não reconhecido: " + procName);
        hasErrors = true;
        return null;
    }

    private void checkVariableUsage(PascalParser.ExpressionContext expr, String context) {
        if (expr.simpleExpression() != null) {
            for (var simpleExpr : expr.simpleExpression()) {
                for (var term : simpleExpr.term()) {
                    for (var factor : term.factor()) {
                        if (factor.IDENTIFIER() != null) {
                            String varName = factor.IDENTIFIER().getText();
                            if (!symbolTable.containsKey(varName)) {
                                System.err.println("Error in " + context + 
                                    ": variável '" + varName + "' não declarada");
                                hasErrors = true;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Void visitIfStatement(PascalParser.IfStatementContext ctx) {
        // Verifica a condição do IF
        checkExpression(ctx.expression());
        
        // Verifica o bloco THEN
        visit(ctx.statement(0));
        
        // Verifica o bloco ELSE se existir
        if (ctx.ELSE() != null && ctx.statement().size() > 1) {
            visit(ctx.statement(1));
        }
        return null;
    }

    @Override
    public Void visitAssignmentStatement(PascalParser.AssignmentStatementContext ctx) {
        String varName = ctx.variable().IDENTIFIER().getText();
        if (!symbolTable.containsKey(varName)) {
            System.err.println("Error: variável '" + varName + "' não declarada");
            hasErrors = true;
        }
        checkExpression(ctx.expression());
        return null;
    }

        @Override
    public Void visitRepeatStatement(PascalParser.RepeatStatementContext ctx) {
        // Verifica as instruções dentro do repeat
        visit(ctx.statementList());
        
        // Verifica a condição do until
        checkExpression(ctx.expression());
        
        return null;
    }

    private void checkExpression(PascalParser.ExpressionContext expr) {
        // Verifica expressões simples (aritméticas, comparações, etc.)
        if (expr.simpleExpression() != null) {
            for (var simpleExpr : expr.simpleExpression()) {
                for (var term : simpleExpr.term()) {
                    for (var factor : term.factor()) {
                        if (factor.IDENTIFIER() != null) {
                            String varName = factor.IDENTIFIER().getText();
                            if (!symbolTable.containsKey(varName)) {
                                System.err.println("Error: variable '" + varName + "' not declared");
                                hasErrors = true;
                            }
                        }
                        else if (factor.STRING() != null) {
                            stringLiterals.add(factor.STRING().getText());
                        }
                    }
                }
            }
        }
    }

    public void printAnalysis() {
        System.out.println("\n=== Análise Semântica ===");
        System.out.println("Variáveis declaradas:");
        symbolTable.forEach((name, type) -> System.out.println("  " + name + " : " + type));
        
        System.out.println("\nLiterais de string encontrados:");
        stringLiterals.forEach(str -> System.out.println("  " + str));
        
        System.out.println("\nStatus: " + (hasErrors ? "ERROS ENCONTRADOS" : "OK"));
    }
}