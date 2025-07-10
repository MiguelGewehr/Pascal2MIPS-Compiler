import parser.*;
import java.util.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PascalSemanticVisitor extends PascalParserBaseVisitor<TipoSimbolo> {

    private final Map<String, Map<String, Simbolo>> scopes = new HashMap<>();
    private String currentScope = "global";
    private final Set<String> stringLiterals = new HashSet<>();
    private boolean hasErrors = false;

    public PascalSemanticVisitor() {
        scopes.put("global", new HashMap<>());
    }

    // --- Buscadores e Conversores ---

    private Simbolo findSymbol(String name) {
        Simbolo simbolo = scopes.get(currentScope).get(name.toLowerCase());
        if (simbolo != null)
            return simbolo;
        return scopes.get("global").get(name.toLowerCase());
    }

    private TipoSimbolo converterTipo(String tipoStr) {
        tipoStr = tipoStr.toLowerCase();
        return switch (tipoStr) {
            case "integer" -> TipoSimbolo.INTEGER;
            case "real" -> TipoSimbolo.REAL;
            case "char" -> TipoSimbolo.CHAR;
            case "string" -> TipoSimbolo.STRING;
            case "boolean" -> TipoSimbolo.BOOLEAN;
            default -> tipoStr.contains("array") ? TipoSimbolo.ARRAY : TipoSimbolo.DESCONHECIDO;
        };
    }

    private TipoSimbolo inferirTipoConstante(PascalParser.ConstantContext ctx) {
        if (ctx.signedNumber() != null) {
            if (ctx.signedNumber().REAL() != null)
                return TipoSimbolo.REAL;
            return TipoSimbolo.INTEGER;
        }
        if (ctx.CHARACTER() != null)
            return TipoSimbolo.CHAR;
        if (ctx.STRING() != null)
            return TipoSimbolo.STRING;
        if (ctx.IDENTIFIER() != null) {
            Simbolo s = findSymbol(ctx.IDENTIFIER().getText());
            if (s != null && s.categoria == CategoriaSimbolo.CONSTANTE) {
                return s.tipo;
            }
        }
        return TipoSimbolo.DESCONHECIDO;
    }

    // --- Visitantes de Declarações ---

    @Override
    public TipoSimbolo visitVarDeclaration(PascalParser.VarDeclarationContext ctx) {
        String tipoStr = ctx.typeDenoter().getText();
        boolean isVetor = tipoStr.toLowerCase().contains("array");
        TipoSimbolo tipo = converterTipo(tipoStr);

        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            String nome = id.getText().toLowerCase();
            if (scopes.get(currentScope).containsKey(nome)) {
                reportarErro(id.getSymbol().getLine(), "Identificador '" + nome + "' já declarado neste escopo.");
            } else {
                CategoriaSimbolo categoria = isVetor ? CategoriaSimbolo.VETOR : CategoriaSimbolo.VARIAVEL;
                Simbolo simbolo = new Simbolo(nome, tipo, categoria, id.getSymbol().getLine());
                scopes.get(currentScope).put(nome, simbolo);
            }
        }
        return null; // Declarações não têm tipo de retorno
    }

    @Override
    public TipoSimbolo visitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx) {
        String nomeFuncao = ctx.getChild(1).getText().toLowerCase();
        String tipoRetornoStr = ctx.getChild(ctx.getChildCount() - 3).getText();

        TipoSimbolo tipoRetorno = converterTipo(tipoRetornoStr);
        Simbolo simboloFuncao = new Simbolo(nomeFuncao, tipoRetorno, CategoriaSimbolo.FUNCAO, ctx.getStart().getLine());

        if (scopes.get("global").containsKey(nomeFuncao)) {
            reportarErro(ctx.getStart().getLine(), "Identificador '" + nomeFuncao + "' já declarado no escopo global.");
        } else {
            scopes.get("global").put(nomeFuncao, simboloFuncao);
        }

        scopes.put(nomeFuncao, new HashMap<>());
        String escopoAnterior = currentScope;
        currentScope = nomeFuncao;

        visitChildren(ctx); // Visita parâmetros e o bloco da função

        currentScope = escopoAnterior;
        return null;
    }

    @Override
    public TipoSimbolo visitProcedureDeclaration(PascalParser.ProcedureDeclarationContext ctx) {
        String nomeProc = ctx.getChild(1).getText().toLowerCase();
        Simbolo simboloProc = new Simbolo(nomeProc, TipoSimbolo.DESCONHECIDO, CategoriaSimbolo.PROCEDIMENTO,
                ctx.getStart().getLine());

        if (scopes.get("global").containsKey(nomeProc)) {
            reportarErro(ctx.getStart().getLine(), "Identificador '" + nomeProc + "' já declarado no escopo global.");
        } else {
            scopes.get("global").put(nomeProc, simboloProc);
        }

        scopes.put(nomeProc, new HashMap<>());
        String escopoAnterior = currentScope;
        currentScope = nomeProc;

        visitChildren(ctx); // Visita parâmetros e o bloco do procedimento

        currentScope = escopoAnterior;
        return null;
    }

    @Override
    public TipoSimbolo visitFormalParameterSection(PascalParser.FormalParameterSectionContext ctx) {
        // A gramática parece ter um erro aqui, assumindo que o tipo é o último
        // IDENTIFIER
        String tipoStr = ctx.IDENTIFIER().getText();
        TipoSimbolo tipo = converterTipo(tipoStr);

        for (TerminalNode id : ctx.identifierList().IDENTIFIER()) {
            String nome = id.getText().toLowerCase();
            Simbolo simbolo = new Simbolo(nome, tipo, CategoriaSimbolo.PARAMETRO, id.getSymbol().getLine());
            scopes.get(currentScope).put(nome, simbolo);
        }
        return null;
    }

    @Override
    public TipoSimbolo visitConstDefinition(PascalParser.ConstDefinitionContext ctx) {
        String nome = ctx.IDENTIFIER().getText().toLowerCase();
        TipoSimbolo tipo = inferirTipoConstante(ctx.constant());
        Simbolo simbolo = new Simbolo(nome, tipo, CategoriaSimbolo.CONSTANTE, ctx.getStart().getLine());
        scopes.get(currentScope).put(nome, simbolo);
        return null;
    }

    // --- Visitantes de Comandos ---

    @Override
    public TipoSimbolo visitAssignmentStatement(PascalParser.AssignmentStatementContext ctx) {
        TipoSimbolo tipoVariavel = visit(ctx.variable());
        TipoSimbolo tipoExpressao = visit(ctx.expression());

        if (tipoVariavel != null && tipoExpressao != null) {
            verificarCompatibilidadeAtribuicao(tipoVariavel, tipoExpressao, ctx.getStart().getLine());
        }
        return null;
    }

    @Override
    public TipoSimbolo visitIfStatement(PascalParser.IfStatementContext ctx) {
        TipoSimbolo tipoCondicao = visit(ctx.expression());
        verificarExpressaoBooleana("IF", tipoCondicao, ctx.getStart().getLine());

        visit(ctx.statement(0));
        if (ctx.statement(1) != null) {
            visit(ctx.statement(1));
        }
        return null;
    }

    @Override
    public TipoSimbolo visitWhileStatement(PascalParser.WhileStatementContext ctx) {
        TipoSimbolo tipoCondicao = visit(ctx.expression());
        verificarExpressaoBooleana("WHILE", tipoCondicao, ctx.getStart().getLine());
        visit(ctx.statement());
        return null;
    }

    @Override
    public TipoSimbolo visitRepeatStatement(PascalParser.RepeatStatementContext ctx) {
        visit(ctx.statementList());
        TipoSimbolo tipoCondicao = visit(ctx.expression());
        verificarExpressaoBooleana("REPEAT-UNTIL", tipoCondicao, ctx.getStart().getLine());
        return null;
    }

    @Override
    public TipoSimbolo visitForStatement(PascalParser.ForStatementContext ctx) {
        visit(ctx.assignmentStatement()); // Isso já vai checar a atribuição inicial
        // Uma checagem mais robusta validaria se a variável de controle é ordinal.

        TipoSimbolo tipoLimite = visit(ctx.expression());
        if (tipoLimite != TipoSimbolo.INTEGER) {
            reportarErro(ctx.expression().getStart().getLine(),
                    "A expressão de limite do FOR deve ser do tipo INTEGER.");
        }

        visit(ctx.statement());
        return null;
    }

    @Override
    public TipoSimbolo visitProcedureCall(PascalParser.ProcedureCallContext ctx) {
        String procName = ctx.IDENTIFIER().getText().toLowerCase();

        // Tratamento de procedimentos built-in
        if (procName.equals("read") || procName.equals("readln") || procName.equals("write")
                || procName.equals("writeln")) {
            if (ctx.expressionList() != null) {
                visit(ctx.expressionList());
            }
            return null;
        }

        Simbolo s = findSymbol(procName);
        if (s == null) {
            reportarErro(ctx.getStart().getLine(), "Procedimento ou função '" + procName + "' não declarado.");
        } else if (s.categoria != CategoriaSimbolo.PROCEDIMENTO && s.categoria != CategoriaSimbolo.FUNCAO) {
            reportarErro(ctx.getStart().getLine(), "'" + procName + "' não é um procedimento ou função.");
        }
        // Uma análise completa validaria os parâmetros da chamada
        return null;
    }

    // --- Visitantes de Expressões ---

    @Override
    public TipoSimbolo visitExpression(PascalParser.ExpressionContext ctx) {
        TipoSimbolo tipoEsq = visit(ctx.simpleExpression(0));
        if (ctx.simpleExpression().size() > 1) {
            TipoSimbolo tipoDir = visit(ctx.simpleExpression(1));
            String operador = ctx.getChild(1).getText();

            if (!verificarCompatibilidadeRelacional(tipoEsq, tipoDir)) {
                reportarErroTipo(ctx.getStart().getLine(), operador, tipoEsq, tipoDir);
                return TipoSimbolo.DESCONHECIDO;
            }
            return TipoSimbolo.BOOLEAN;
        }
        return tipoEsq;
    }

    @Override
    public TipoSimbolo visitSimpleExpression(PascalParser.SimpleExpressionContext ctx) {
        TipoSimbolo tipoResultante = visit(ctx.term(0));
        for (int i = 1; i < ctx.term().size(); i++) {
            TipoSimbolo tipoDireita = visit(ctx.term(i));
            String operador = ctx.getChild(2 * i - 1).getText().toUpperCase();
            tipoResultante = unificarTipos(tipoResultante, tipoDireita, operador, ctx.getStart().getLine());
        }
        return tipoResultante;
    }

    @Override
    public TipoSimbolo visitTerm(PascalParser.TermContext ctx) {
        TipoSimbolo tipoResultante = visit(ctx.factor(0));
        for (int i = 1; i < ctx.factor().size(); i++) {
            TipoSimbolo tipoDireita = visit(ctx.factor(i));
            String operador = ctx.getChild(2 * i - 1).getText().toUpperCase();
            tipoResultante = unificarTipos(tipoResultante, tipoDireita, operador, ctx.getStart().getLine());
        }
        return tipoResultante;
    }

    @Override
    public TipoSimbolo visitFactor(PascalParser.FactorContext ctx) {
        if (ctx.INTEGER() != null)
            return TipoSimbolo.INTEGER;
        if (ctx.REAL() != null)
            return TipoSimbolo.REAL;
        if (ctx.CHARACTER() != null)
            return TipoSimbolo.CHAR;
        if (ctx.STRING() != null) {
            stringLiterals.add(ctx.STRING().getText());
            return TipoSimbolo.STRING;
        }
        if (ctx.LPAREN() != null)
            return visit(ctx.expression());
        if (ctx.NOT() != null) {
            TipoSimbolo tipo = visit(ctx.factor());
            if (tipo != TipoSimbolo.BOOLEAN) {
                reportarErroTipoUnario(ctx.getStart().getLine(), "NOT", tipo);
                return TipoSimbolo.DESCONHECIDO;
            }
            return TipoSimbolo.BOOLEAN;
        }
        if (ctx.IDENTIFIER() != null) {
            String nome = ctx.IDENTIFIER().getText().toLowerCase();
            Simbolo s = findSymbol(nome);
            if (s == null) {
                reportarErro(ctx.getStart().getLine(), "Identificador '" + nome + "' não declarado.");
                return TipoSimbolo.DESCONHECIDO;
            }
            if (ctx.expressionList() != null) { // Chamada de função
                if (s.categoria != CategoriaSimbolo.FUNCAO) {
                    reportarErro(ctx.getStart().getLine(), "'" + nome + "' não é uma função e não pode ser chamada.");
                    return TipoSimbolo.DESCONHECIDO;
                }
                // Validar parâmetros aqui se necessário
                return s.tipo;
            } else { // Variável ou constante
                if (s.categoria == CategoriaSimbolo.FUNCAO) {
                    reportarErro(ctx.getStart().getLine(), "Função '" + nome + "' usada sem chamada de parênteses.");
                    return TipoSimbolo.DESCONHECIDO;
                }
                return s.tipo;
            }
        }
        return TipoSimbolo.DESCONHECIDO;
    }

    @Override
    public TipoSimbolo visitVariable(PascalParser.VariableContext ctx) {
        String nome = ctx.IDENTIFIER().getText().toLowerCase();
        Simbolo s = findSymbol(nome);
        if (s == null) {
            reportarErro(ctx.getStart().getLine(), "Variável '" + nome + "' não declarada.");
            return null;
        }

        if (ctx.expression() != null) { // Acesso a vetor
            if (s.categoria != CategoriaSimbolo.VETOR) {
                reportarErro(ctx.getStart().getLine(), "Tentativa de indexar '" + nome + "', que não é um vetor.");
                return null;
            }
            TipoSimbolo tipoIndice = visit(ctx.expression());
            if (tipoIndice != TipoSimbolo.INTEGER) {
                reportarErro(ctx.getStart().getLine(),
                        "indice do vetor deve ser um inteiro, mas foi '" + tipoIndice + "'.");
                return null;
            }
            // Em Pascal, o tipo de `a[i]` é o tipo base do array `a`.
            // Para simplificar, estamos retornando o tipo 'ARRAY', mas o correto
            // seria ter um tipo base associado no símbolo. Retornar s.tipo está OK
            // para a verificação de atribuição se o tipo base for o mesmo.
            return s.tipo;
        }
        return s.tipo;
    }

    // --- Funções Auxiliares de Verificação e Erro ---

    private void reportarErro(int linha, String mensagem) {
        System.err.printf("ERRO SEMÂNTICO (linha %d): %s\n", linha, mensagem);
        hasErrors = true;
    }

    private void reportarErroTipo(int linha, String op, TipoSimbolo esq, TipoSimbolo dir) {
        reportarErro(linha,
                "Tipos incompatíveis para o operador '" + op + "'. LHS é '" + esq + "' e RHS é '" + dir + "'.");
    }

    private void reportarErroTipoUnario(int linha, String op, TipoSimbolo tipo) {
        reportarErro(linha, "Tipo incompatível para o operador unário '" + op + "'. Esperado BOOLEAN, mas encontrado '"
                + tipo + "'.");
    }

    private void verificarExpressaoBooleana(String comando, TipoSimbolo tipo, int linha) {
        if (tipo != TipoSimbolo.BOOLEAN && tipo != TipoSimbolo.DESCONHECIDO) {
            reportarErro(linha,
                    "A expressão do comando '" + comando + "' deve ser do tipo BOOLEAN, mas é do tipo '" + tipo + "'.");
        }
    }

    private void verificarCompatibilidadeAtribuicao(TipoSimbolo tipoVar, TipoSimbolo tipoExpr, int linha) {
        if (tipoVar == tipoExpr)
            return; // OK
        if (tipoVar == TipoSimbolo.REAL && tipoExpr == TipoSimbolo.INTEGER)
            return; // Widening OK

        reportarErroTipo(linha, ":=", tipoVar, tipoExpr);
    }

    private boolean verificarCompatibilidadeRelacional(TipoSimbolo esq, TipoSimbolo dir) {
        boolean ambosNumericos = (esq == TipoSimbolo.INTEGER || esq == TipoSimbolo.REAL)
                && (dir == TipoSimbolo.INTEGER || dir == TipoSimbolo.REAL);
        boolean ambosMesmoTipo = esq == dir;
        return ambosNumericos || (ambosMesmoTipo && esq != TipoSimbolo.ARRAY && esq != TipoSimbolo.DESCONHECIDO);
    }

    private TipoSimbolo unificarTipos(TipoSimbolo esq, TipoSimbolo dir, String op, int linha) {
        switch (op.toUpperCase()) {
            case "AND":
            case "OR":
                if (esq == TipoSimbolo.BOOLEAN && dir == TipoSimbolo.BOOLEAN)
                    return TipoSimbolo.BOOLEAN;
                break;
            case "+":
                if (esq == TipoSimbolo.STRING && dir == TipoSimbolo.STRING)
                    return TipoSimbolo.STRING;
                // Fall-through para lógica numérica
            case "-":
            case "*":
                if ((esq == TipoSimbolo.INTEGER || esq == TipoSimbolo.REAL)
                        && (dir == TipoSimbolo.INTEGER || dir == TipoSimbolo.REAL)) {
                    return (esq == TipoSimbolo.REAL || dir == TipoSimbolo.REAL) ? TipoSimbolo.REAL
                            : TipoSimbolo.INTEGER;
                }
                break;
            case "/": // Divisão real sempre resulta em real
                if ((esq == TipoSimbolo.INTEGER || esq == TipoSimbolo.REAL)
                        && (dir == TipoSimbolo.INTEGER || dir == TipoSimbolo.REAL)) {
                    return TipoSimbolo.REAL;
                }
                break;
            case "DIV":
            case "MOD":
                if (esq == TipoSimbolo.INTEGER && dir == TipoSimbolo.INTEGER)
                    return TipoSimbolo.INTEGER;
                break;
        }
        reportarErroTipo(linha, op, esq, dir);
        return TipoSimbolo.DESCONHECIDO;
    }

    // --- Saída da Análise ---

    public void printAnalysis() {
        System.out.println("\n=== Análise Semântica ===");
        if (!hasErrors) {
            System.out.println("Nenhum erro semântico encontrado.");
        } else {
            System.out.println("Foram encontrados erros semânticos.");
        }

        System.out.println("\n--- Tabela de Símbolos ---");
        scopes.forEach((escopo, tabela) -> {
            if (!tabela.isEmpty()) {
                System.out.println("\nEscopo: " + escopo);
                System.out.printf("%-20s | %-15s | %-15s | %s\n", "Nome", "Categoria", "Tipo", "Linha");
                System.out.println(String.join("", Collections.nCopies(70, "-")));
                tabela.forEach((nome, simbolo) -> {
                    System.out.printf("%-20s | %-15s | %-15s | %d\n",
                            simbolo.nome, simbolo.categoria.toString(), simbolo.tipo.toString(), simbolo.linha);
                });
            }
        });

        System.out.println("\n--- Literais de String Encontradas ---");
        if (stringLiterals.isEmpty()) {
            System.out.println("Nenhuma literal de string encontrada.");
        } else {
            stringLiterals.forEach(System.out::println);
        }

        //System.out.println("\nStatus Final: " + (hasErrors ? "COMPILAÇÃO FALHOU" : "OK"));
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}