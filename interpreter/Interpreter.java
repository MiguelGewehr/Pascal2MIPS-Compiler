package interpreter;

import ast.AST;
import ast.NodeKind;
import typing.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Interpretador simples para Pascal ISO 7185
 * Atualmente suporta apenas assignments básicos
 */
public class Interpreter {
    
    // Memória para armazenar valores das variáveis
    private Map<String, Object> memory = new HashMap<>();
    
    /**
     * Interpreta o programa a partir da AST
     */
    public void interpret(AST programNode) {
        if (programNode.kind != NodeKind.PROGRAM_NODE) {
            throw new RuntimeException("Root node must be a PROGRAM_NODE");
        }
        
        System.out.println("[INTERPRETER] Starting execution...");
        
        // O programa tem um filho: o BLOCK_NODE
        if (programNode.getChildCount() > 0) {
            AST blockNode = programNode.getChild(0);
            executeBlock(blockNode);
        }
        
        System.out.println("[INTERPRETER] Execution finished.");
        printMemoryState();
    }
    
    /**
     * Executa um bloco (pode conter declarações e statements)
     */
    private void executeBlock(AST blockNode) {
        if (blockNode.kind != NodeKind.BLOCK_NODE) {
            throw new RuntimeException("Expected BLOCK_NODE");
        }
        
        // Processa todos os filhos do bloco
        for (int i = 0; i < blockNode.getChildCount(); i++) {
            AST child = blockNode.getChild(i);
            
            switch (child.kind) {
                case CONST_SECTION_NODE -> executeConstSection(child);
                case VAR_SECTION_NODE -> executeVarSection(child);
                case COMPOUND_STMT_NODE -> executeCompoundStatement(child);
                case PROC_DECL_NODE, FUNC_DECL_NODE -> {
                    // Por enquanto ignora declarações de subrotinas
                    System.out.println("[INTERPRETER] Skipping subroutine declaration: " + child.stringData);
                }
                default -> {
                    System.out.println("[INTERPRETER] Skipping unsupported block child: " + child.kind);
                }
            }
        }
    }
    
    /**
     * Executa seção de constantes
     */
    private void executeConstSection(AST constSectionNode) {
        for (int i = 0; i < constSectionNode.getChildCount(); i++) {
            AST constDecl = constSectionNode.getChild(i);
            executeConstDeclaration(constDecl);
        }
    }
    
    /**
     * Executa declaração de constante
     */
    private void executeConstDeclaration(AST constDeclNode) {
        if (constDeclNode.kind != NodeKind.CONST_DECL_NODE) {
            return;
        }
        
        String constName = constDeclNode.stringData;
        
        // O valor da constante está no primeiro filho
        if (constDeclNode.getChildCount() > 0) {
            Object value = evaluateExpression(constDeclNode.getChild(0));
            memory.put(constName, value);
            System.out.println("[INTERPRETER] Constant " + constName + " = " + value);
        }
    }
    
    /**
     * Executa seção de variáveis (por enquanto só inicializa com valores padrão)
     */
    private void executeVarSection(AST varSectionNode) {
        for (int i = 0; i < varSectionNode.getChildCount(); i++) {
            AST varList = varSectionNode.getChild(i);
            executeVarList(varList);
        }
    }
    
    /**
     * Executa lista de variáveis
     */
    private void executeVarList(AST varListNode) {
        for (int i = 0; i < varListNode.getChildCount(); i++) {
            AST varDecl = varListNode.getChild(i);
            executeVarDeclaration(varDecl);
        }
    }
    
    /**
     * Executa declaração de variável (inicializa com valor padrão)
     */
    private void executeVarDeclaration(AST varDeclNode) {
        if (varDeclNode.kind != NodeKind.VAR_DECL_NODE) {
            return;
        }
        
        String varName = varDeclNode.stringData;
        Object defaultValue = getDefaultValue(varDeclNode.type);
        memory.put(varName, defaultValue);
        
        System.out.println("[INTERPRETER] Variable " + varName + " declared and initialized to " + defaultValue);
    }
    
    /**
     * Retorna valor padrão para um tipo
     */
    private Object getDefaultValue(Type type) {
        return switch (type) {
            case INTEGER -> 0;
            case REAL -> 0.0f;
            case BOOLEAN -> false;
            case CHAR -> '\0';
            case STRING -> "";
            default -> null;
        };
    }
    
    /**
     * Executa statement composto
     */
    private void executeCompoundStatement(AST compoundNode) {
        if (compoundNode.kind != NodeKind.COMPOUND_STMT_NODE) {
            throw new RuntimeException("Expected COMPOUND_STMT_NODE");
        }
        
        // O compound statement tem um filho que é um BLOCK_NODE contendo a lista de statements
        if (compoundNode.getChildCount() > 0) {
            AST stmtList = compoundNode.getChild(0);
            executeStatementList(stmtList);
        }
    }
    
    /**
     * Executa lista de statements
     */
    private void executeStatementList(AST stmtListNode) {
        for (int i = 0; i < stmtListNode.getChildCount(); i++) {
            AST stmt = stmtListNode.getChild(i);
            executeStatement(stmt);
        }
    }
    
    /**
     * Executa um statement individual
     */
    private void executeStatement(AST stmtNode) {
        switch (stmtNode.kind) {
            case ASSIGN_NODE -> executeAssignment(stmtNode);
            case PROC_CALL_NODE -> executeProcedureCall(stmtNode);
            case COMPOUND_STMT_NODE -> executeCompoundStatement(stmtNode);
            case IF_NODE -> executeIfStatement(stmtNode);
            case WHILE_NODE -> executeWhileStatement(stmtNode);
            case EMPTY_STMT_NODE -> {
                // Não faz nada para statement vazio
            }
            default -> {
                System.out.println("[INTERPRETER] Skipping unsupported statement: " + stmtNode.kind);
            }
        }
    }
    
    /**
     * Executa assignment
     */
    private void executeAssignment(AST assignNode) {
        if (assignNode.kind != NodeKind.ASSIGN_NODE || assignNode.getChildCount() < 2) {
            throw new RuntimeException("Invalid assignment node");
        }
        
        // Primeiro filho: variável de destino
        AST varNode = assignNode.getChild(0);
        
        // Segundo filho: expressão a ser avaliada
        AST exprNode = assignNode.getChild(1);
        
        // Avalia a expressão
        Object value = evaluateExpression(exprNode);
        
        // Obtém o nome da variável
        String varName = getVariableName(varNode);
        
        // Armazena o valor na memória
        memory.put(varName, value);
        
        System.out.println("[INTERPRETER] Assignment: " + varName + " := " + value);
    }
    
    /**
     * Obtém o nome da variável a partir do nó
     */
    private String getVariableName(AST varNode) {
        switch (varNode.kind) {
            case VAR_USE_NODE -> {
                return varNode.stringData;
            }
            case ARRAY_ACCESS_NODE -> {
                // Por enquanto não suporta arrays completamente
                throw new RuntimeException("Array access not yet supported in interpreter");
            }
            default -> throw new RuntimeException("Invalid variable node: " + varNode.kind);
        }
    }
    
    /**
     * Executa chamada de procedimento (basic support)
     */
    private void executeProcedureCall(AST procCallNode) {
        String procName = procCallNode.stringData;
        
        // Suporte básico para procedimentos built-in
        switch (procName.toLowerCase()) {
            case "writeln" -> {
                System.out.print("[OUTPUT] ");
                if (procCallNode.getChildCount() > 0) {
                    AST exprList = procCallNode.getChild(0);
                    for (int i = 0; i < exprList.getChildCount(); i++) {
                        Object value = evaluateExpression(exprList.getChild(i));
                        System.out.print(value);
                        if (i < exprList.getChildCount() - 1) {
                            System.out.print(" ");
                        }
                    }
                }
                System.out.println();
            }
            case "write" -> {
                System.out.print("[OUTPUT] ");
                if (procCallNode.getChildCount() > 0) {
                    AST exprList = procCallNode.getChild(0);
                    for (int i = 0; i < exprList.getChildCount(); i++) {
                        Object value = evaluateExpression(exprList.getChild(i));
                        System.out.print(value);
                        if (i < exprList.getChildCount() - 1) {
                            System.out.print(" ");
                        }
                    }
                }
            }
            default -> {
                System.out.println("[INTERPRETER] Skipping procedure call: " + procName);
            }
        }
    }
    
    /**
     * Executa statement IF (básico)
     */
    private void executeIfStatement(AST ifNode) {
        if (ifNode.getChildCount() < 2) {
            throw new RuntimeException("Invalid if statement");
        }
        
        // Primeiro filho: condição
        AST conditionNode = ifNode.getChild(0);
        Object conditionValue = evaluateExpression(conditionNode);
        
        boolean condition = convertToBoolean(conditionValue);
        
        if (condition) {
            // Segundo filho: statement THEN
            executeStatement(ifNode.getChild(1));
        } else if (ifNode.getChildCount() > 2) {
            // Terceiro filho: statement ELSE (se existir)
            executeStatement(ifNode.getChild(2));
        }
    }
    
    /**
     * Executa statement WHILE (básico)
     */
    private void executeWhileStatement(AST whileNode) {
        if (whileNode.getChildCount() < 2) {
            throw new RuntimeException("Invalid while statement");
        }
        
        while (true) {
            // Primeiro filho: condição
            AST conditionNode = whileNode.getChild(0);
            Object conditionValue = evaluateExpression(conditionNode);
            
            boolean condition = convertToBoolean(conditionValue);
            
            if (!condition) {
                break;
            }
            
            // Segundo filho: corpo do loop
            executeStatement(whileNode.getChild(1));
        }
    }
    
    /**
     * Avalia uma expressão e retorna seu valor
     */
    private Object evaluateExpression(AST exprNode) {
        switch (exprNode.kind) {
            // Valores constantes
            case INT_VAL_NODE -> {
                return exprNode.intData;
            }
            case REAL_VAL_NODE -> {
                return exprNode.floatData;
            }
            case BOOL_VAL_NODE -> {
                return exprNode.intData == 1;
            }
            case CHAR_VAL_NODE -> {
                String charStr = exprNode.stringData;
                return charStr != null && !charStr.isEmpty() ? charStr.charAt(0) : '\0';
            }
            case STR_VAL_NODE -> {
                return exprNode.stringData != null ? exprNode.stringData : "";
            }
            
            // Uso de variável
            case VAR_USE_NODE -> {
                String varName = exprNode.stringData;
                if (!memory.containsKey(varName)) {
                    throw new RuntimeException("Variable '" + varName + "' not initialized");
                }
                return memory.get(varName);
            }
            
            // Operações aritméticas
            case PLUS_NODE -> {
                return evaluateArithmeticOperation(exprNode, (a, b) -> {
                    if (a instanceof Integer && b instanceof Integer) {
                        return (Integer) a + (Integer) b;
                    } else {
                        return convertToFloat(a) + convertToFloat(b);
                    }
                });
            }
            case MINUS_NODE -> {
                return evaluateArithmeticOperation(exprNode, (a, b) -> {
                    if (a instanceof Integer && b instanceof Integer) {
                        return (Integer) a - (Integer) b;
                    } else {
                        return convertToFloat(a) - convertToFloat(b);
                    }
                });
            }
            case TIMES_NODE -> {
                return evaluateArithmeticOperation(exprNode, (a, b) -> {
                    if (a instanceof Integer && b instanceof Integer) {
                        return (Integer) a * (Integer) b;
                    } else {
                        return convertToFloat(a) * convertToFloat(b);
                    }
                });
            }
            case DIVIDE_NODE -> {
                return evaluateArithmeticOperation(exprNode, (a, b) -> {
                    // Divisão sempre retorna float
                    return convertToFloat(a) / convertToFloat(b);
                });
            }
            case DIV_NODE -> {
                return evaluateArithmeticOperation(exprNode, (a, b) -> {
                    return convertToInt(a) / convertToInt(b);
                });
            }
            case MOD_NODE -> {
                return evaluateArithmeticOperation(exprNode, (a, b) -> {
                    return convertToInt(a) % convertToInt(b);
                });
            }
            
            // Operações lógicas
            case AND_NODE -> {
                return evaluateLogicalOperation(exprNode, (a, b) -> a && b);
            }
            case OR_NODE -> {
                return evaluateLogicalOperation(exprNode, (a, b) -> a || b);
            }
            case NOT_NODE -> {
                if (exprNode.getChildCount() < 1) {
                    throw new RuntimeException("NOT operation requires one operand");
                }
                Object operand = evaluateExpression(exprNode.getChild(0));
                return !convertToBoolean(operand);
            }
            
            // Operações de comparação
            case EQ_NODE -> {
                return evaluateComparisonOperation(exprNode, (a, b) -> compareValues(a, b) == 0);
            }
            case NEQ_NODE -> {
                return evaluateComparisonOperation(exprNode, (a, b) -> compareValues(a, b) != 0);
            }
            case LT_NODE -> {
                return evaluateComparisonOperation(exprNode, (a, b) -> compareValues(a, b) < 0);
            }
            case GT_NODE -> {
                return evaluateComparisonOperation(exprNode, (a, b) -> compareValues(a, b) > 0);
            }
            case LE_NODE -> {
                return evaluateComparisonOperation(exprNode, (a, b) -> compareValues(a, b) <= 0);
            }
            case GE_NODE -> {
                return evaluateComparisonOperation(exprNode, (a, b) -> compareValues(a, b) >= 0);
            }
            
            // Conversões de tipo
            case I2R_NODE -> {
                if (exprNode.getChildCount() < 1) {
                    throw new RuntimeException("I2R conversion requires one operand");
                }
                Object operand = evaluateExpression(exprNode.getChild(0));
                return convertToFloat(operand);
            }
            
            // Expressão parentizada
            case PAREN_EXPR_NODE -> {
                if (exprNode.getChildCount() < 1) {
                    throw new RuntimeException("Parenthesized expression requires one operand");
                }
                return evaluateExpression(exprNode.getChild(0));
            }
            
            default -> throw new RuntimeException("Unsupported expression node: " + exprNode.kind);
        }
    }
    
    /**
     * Interface funcional para operações aritméticas
     */
    @FunctionalInterface
    private interface ArithmeticOperation {
        Object apply(Object a, Object b);
    }
    
    /**
     * Interface funcional para operações lógicas
     */
    @FunctionalInterface
    private interface LogicalOperation {
        boolean apply(boolean a, boolean b);
    }
    
    /**
     * Interface funcional para operações de comparação
     */
    @FunctionalInterface
    private interface ComparisonOperation {
        boolean apply(Object a, Object b);
    }
    
    /**
     * Avalia operação aritmética binária
     */
    private Object evaluateArithmeticOperation(AST opNode, ArithmeticOperation operation) {
        if (opNode.getChildCount() < 2) {
            throw new RuntimeException("Arithmetic operation requires two operands");
        }
        
        Object left = evaluateExpression(opNode.getChild(0));
        Object right = evaluateExpression(opNode.getChild(1));
        
        return operation.apply(left, right);
    }
    
    /**
     * Avalia operação lógica binária
     */
    private boolean evaluateLogicalOperation(AST opNode, LogicalOperation operation) {
        if (opNode.getChildCount() < 2) {
            throw new RuntimeException("Logical operation requires two operands");
        }
        
        Object left = evaluateExpression(opNode.getChild(0));
        Object right = evaluateExpression(opNode.getChild(1));
        
        return operation.apply(convertToBoolean(left), convertToBoolean(right));
    }
    
    /**
     * Avalia operação de comparação binária
     */
    private boolean evaluateComparisonOperation(AST opNode, ComparisonOperation operation) {
        if (opNode.getChildCount() < 2) {
            throw new RuntimeException("Comparison operation requires two operands");
        }
        
        Object left = evaluateExpression(opNode.getChild(0));
        Object right = evaluateExpression(opNode.getChild(1));
        
        return operation.apply(left, right);
    }
    
    /**
     * Compara dois valores numericamente
     */
    private int compareValues(Object a, Object b) {
        if (a instanceof Number && b instanceof Number) {
            float fa = convertToFloat(a);
            float fb = convertToFloat(b);
            return Float.compare(fa, fb);
        }
        
        // Para outros tipos, converte para string e compara
        String sa = a.toString();
        String sb = b.toString();
        return sa.compareTo(sb);
    }
    
    /**
     * Converte valor para int
     */
    private int convertToInt(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Float) {
            return ((Float) value).intValue();
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        }
        throw new RuntimeException("Cannot convert " + value.getClass().getSimpleName() + " to integer");
    }
    
    /**
     * Converte valor para float
     */
    private float convertToFloat(Object value) {
        if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).floatValue();
        }
        throw new RuntimeException("Cannot convert " + value.getClass().getSimpleName() + " to float");
    }
    
    /**
     * Converte valor para boolean
     */
    private boolean convertToBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Integer) {
            return (Integer) value != 0;
        }
        throw new RuntimeException("Cannot convert " + value.getClass().getSimpleName() + " to boolean");
    }
    
    /**
     * Imprime o estado atual da memória
     */
    private void printMemoryState() {
        System.out.println("\n[INTERPRETER] Memory state:");
        if (memory.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (Map.Entry<String, Object> entry : memory.entrySet()) {
                System.out.println("  " + entry.getKey() + " = " + entry.getValue() + 
                                 " (" + entry.getValue().getClass().getSimpleName() + ")");
            }
        }
    }
}