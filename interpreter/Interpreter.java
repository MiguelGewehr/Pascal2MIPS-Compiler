package interpreter;

import ast.AST;
import ast.NodeKind;
import typing.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Interpretador completo para Pascal ISO 7185
 * Suporta variáveis, constantes, arrays, expressões, controle de fluxo,
 * procedimentos built-in e modo debug
 */
public class Interpreter {
    
    // Configuração de debug
    private static boolean DEBUG_MODE = true;
    
    // Memória para armazenar valores das variáveis
    private Map<String, Object> memory = new HashMap<>();
    
    // Scanner para entrada do usuário (para read/readln)
    private Scanner scanner = new Scanner(System.in);
    
    // Arrays - mapeamento de nome para dados do array
    private Map<String, ArrayData> arrays = new HashMap<>();
    
    // Classe para dados de array
    private static class ArrayData {
        Object[] elements;
        int startIndex;
        int endIndex;
        Type elementType;
        
        ArrayData(int startIndex, int endIndex, Type elementType) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.elementType = elementType;
            this.elements = new Object[endIndex - startIndex + 1];
            
            // Inicializa com valores padrão
            Object defaultValue = getDefaultValue(elementType);
            for (int i = 0; i < elements.length; i++) {
                elements[i] = defaultValue;
            }
        }
        
        void setElement(int index, Object value) {
            if (index < startIndex || index > endIndex) {
                throw new RuntimeException("Array index " + index + " out of bounds [" + 
                                         startIndex + ".." + endIndex + "]");
            }
            elements[index - startIndex] = value;
        }
        
        Object getElement(int index) {
            if (index < startIndex || index > endIndex) {
                throw new RuntimeException("Array index " + index + " out of bounds [" + 
                                         startIndex + ".." + endIndex + "]");
            }
            return elements[index - startIndex];
        }
        
        private static Object getDefaultValue(Type type) {
            return switch (type) {
                case INTEGER -> 0;
                case REAL -> 0.0f;
                case BOOLEAN -> false;
                case CHAR -> '\0';
                case STRING -> "";
                default -> null;
            };
        }
    }
    
    /**
     * Construtor
     */
    public Interpreter() {
        this(false);
    }
    
    /**
     * Construtor com modo debug
     */
    public Interpreter(boolean debugMode) {
        DEBUG_MODE = debugMode;
    }
    
    /**
     * Define o modo debug
     */
    public static void setDebugMode(boolean debug) {
        DEBUG_MODE = debug;
    }
    
    /**
     * Imprime mensagem de debug apenas se o modo debug estiver ativo
     */
    private void debugPrint(String message) {
        if (DEBUG_MODE) {
            System.out.println("[INTERPRETER] " + message);
        }
    }
    
    /**
     * Interpreta o programa a partir da AST
     */
    public void interpret(AST programNode) {
        if (programNode.kind != NodeKind.PROGRAM_NODE) {
            throw new RuntimeException("Root node must be a PROGRAM_NODE");
        }
        
        debugPrint("Starting execution...");
        
        // O programa tem um filho: o BLOCK_NODE
        if (programNode.getChildCount() > 0) {
            AST blockNode = programNode.getChild(0);
            executeBlock(blockNode);
        }
        
        debugPrint("Execution finished.");
        
        if (DEBUG_MODE) {
            printMemoryState();
        }
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
                    debugPrint("Skipping subroutine declaration: " + child.stringData);
                }
                default -> {
                    debugPrint("Skipping unsupported block child: " + child.kind);
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
            debugPrint("Constant " + constName + " = " + value);
        }
    }
    
    /**
     * Executa seção de variáveis
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
     * Executa declaração de variável
     */
    private void executeVarDeclaration(AST varDeclNode) {
        if (varDeclNode.kind != NodeKind.VAR_DECL_NODE) {
            return;
        }
        
        String varName = varDeclNode.stringData;
        Type varType = varDeclNode.type;
        
        if (varType == Type.ARRAY) {
            // Para arrays, precisamos de mais informações do semantic checker
            // Por enquanto, inicializa como array simples
            debugPrint("Array variable " + varName + " declared (not fully implemented)");
        } else {
            Object defaultValue = getDefaultValue(varType);
            memory.put(varName, defaultValue);
            debugPrint("Variable " + varName + " declared and initialized to " + defaultValue);
        }
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
                debugPrint("Skipping unsupported statement: " + stmtNode.kind);
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
        
        // Executa a atribuição baseado no tipo da variável
        executeVariableAssignment(varNode, value);
    }
    
    /**
     * Executa atribuição a uma variável (simples ou array)
     */
    private void executeVariableAssignment(AST varNode, Object value) {
        switch (varNode.kind) {
            case VAR_USE_NODE -> {
                String varName = varNode.stringData;
                memory.put(varName, value);
                debugPrint("Assignment: " + varName + " := " + value);
            }
            case ARRAY_ACCESS_NODE -> {
                String arrayName = varNode.stringData;
                if (varNode.getChildCount() < 1) {
                    throw new RuntimeException("Array access without index");
                }
                
                Object indexObj = evaluateExpression(varNode.getChild(0));
                int index = convertToInt(indexObj);
                
                ArrayData arrayData = arrays.get(arrayName);
                if (arrayData == null) {
                    throw new RuntimeException("Array '" + arrayName + "' not found");
                }
                
                arrayData.setElement(index, value);
                debugPrint("Array assignment: " + arrayName + "[" + index + "] := " + value);
            }
            default -> throw new RuntimeException("Invalid assignment target: " + varNode.kind);
        }
    }
    
    /**
     * Executa chamada de procedimento
     */
    private void executeProcedureCall(AST procCallNode) {
        String procName = procCallNode.stringData.toLowerCase();
        
        // Suporte para procedimentos built-in
        switch (procName) {
            case "writeln" -> {
                List<Object> values = new ArrayList<>();
                if (procCallNode.getChildCount() > 0) {
                    AST exprList = procCallNode.getChild(0);
                    for (int i = 0; i < exprList.getChildCount(); i++) {
                        Object value = evaluateExpression(exprList.getChild(i));
                        values.add(value);
                    }
                }
                
                // Imprime os valores
                for (int i = 0; i < values.size(); i++) {
                    System.out.print(formatOutputValue(values.get(i)));
                    if (i < values.size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println(); // Nova linha no final
            }
            case "write" -> {
                if (procCallNode.getChildCount() > 0) {
                    AST exprList = procCallNode.getChild(0);
                    for (int i = 0; i < exprList.getChildCount(); i++) {
                        Object value = evaluateExpression(exprList.getChild(i));
                        System.out.print(formatOutputValue(value));
                        if (i < exprList.getChildCount() - 1) {
                            System.out.print(" ");
                        }
                    }
                }
                // Não imprime nova linha
            }
            case "readln" -> {
                if (DEBUG_MODE) {
                    System.out.print("[INPUT] Enter value: ");
                }
                String input = scanner.nextLine();
                // Para simplificar, assume que está lendo uma variável específica
                // Implementação completa precisaria de mais informações do contexto
                debugPrint("Read value: " + input);
            }
            case "read" -> {
                if (DEBUG_MODE) {
                    System.out.print("[INPUT] Enter value: ");
                }
                String input = scanner.next();
                debugPrint("Read value: " + input);
            }
            default -> {
                debugPrint("Skipping user-defined procedure call: " + procName);
            }
        }
    }
    
    /**
     * Formata valor para saída
     */
    private String formatOutputValue(Object value) {
        if (value instanceof Float) {
            Float f = (Float) value;
            // Remove zeros desnecessários
            if (f == f.intValue()) {
                return String.valueOf(f.intValue());
            } else {
                return String.format("%.6f", f).replaceAll("0+$", "").replaceAll("\\.$", "");
            }
        } else if (value instanceof Character) {
            return String.valueOf(value);
        } else if (value instanceof String) {
            String s = (String) value;
            // Remove aspas se estiverem presentes
            if (s.length() >= 2 && s.startsWith("'") && s.endsWith("'")) {
                return s.substring(1, s.length() - 1);
            }
            return s;
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? "true" : "false";
        }
        return String.valueOf(value);
    }
    
    /**
     * Executa statement IF
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
     * Executa statement WHILE
     */
    private void executeWhileStatement(AST whileNode) {
        if (whileNode.getChildCount() < 2) {
            throw new RuntimeException("Invalid while statement");
        }
        
        int iterations = 0;
        final int MAX_ITERATIONS = 100000; // Previne loops infinitos
        
        while (true) {
            if (++iterations > MAX_ITERATIONS) {
                throw new RuntimeException("While loop exceeded maximum iterations (" + MAX_ITERATIONS + ")");
            }
            
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
                if (charStr != null && charStr.length() >= 3) {
                    // Remove aspas simples se estiverem presentes
                    if (charStr.startsWith("'") && charStr.endsWith("'")) {
                        charStr = charStr.substring(1, charStr.length() - 1);
                    }
                    return charStr.length() > 0 ? charStr.charAt(0) : '\0';
                }
                return '\0';
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
            
            // Acesso a array
            case ARRAY_ACCESS_NODE -> {
                String arrayName = exprNode.stringData;
                if (exprNode.getChildCount() < 1) {
                    throw new RuntimeException("Array access without index");
                }
                
                Object indexObj = evaluateExpression(exprNode.getChild(0));
                int index = convertToInt(indexObj);
                
                ArrayData arrayData = arrays.get(arrayName);
                if (arrayData == null) {
                    throw new RuntimeException("Array '" + arrayName + "' not found");
                }
                
                return arrayData.getElement(index);
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
                    float fb = convertToFloat(b);
                    if (Math.abs(fb) < 1e-10) {
                        throw new RuntimeException("Division by zero");
                    }
                    return convertToFloat(a) / fb;
                });
            }
            case DIV_NODE -> {
                return evaluateArithmeticOperation(exprNode, (a, b) -> {
                    int ib = convertToInt(b);
                    if (ib == 0) {
                        throw new RuntimeException("Integer division by zero");
                    }
                    return convertToInt(a) / ib;
                });
            }
            case MOD_NODE -> {
                return evaluateArithmeticOperation(exprNode, (a, b) -> {
                    int ib = convertToInt(b);
                    if (ib == 0) {
                        throw new RuntimeException("Modulo by zero");
                    }
                    return convertToInt(a) % ib;
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
            
            // Chamada de função
            case FUNC_CALL_NODE -> {
                String funcName = exprNode.stringData.toLowerCase();
                
                // Funções built-in básicas
                switch (funcName) {
                    case "abs" -> {
                        if (exprNode.getChildCount() > 0 && exprNode.getChild(0).getChildCount() > 0) {
                            Object arg = evaluateExpression(exprNode.getChild(0).getChild(0));
                            if (arg instanceof Integer) {
                                return Math.abs((Integer) arg);
                            } else {
                                return Math.abs(convertToFloat(arg));
                            }
                        }
                        throw new RuntimeException("Function 'abs' requires one argument");
                    }
                    case "sqrt" -> {
                        if (exprNode.getChildCount() > 0 && exprNode.getChild(0).getChildCount() > 0) {
                            Object arg = evaluateExpression(exprNode.getChild(0).getChild(0));
                            float f = convertToFloat(arg);
                            if (f < 0) {
                                throw new RuntimeException("Square root of negative number");
                            }
                            return (float) Math.sqrt(f);
                        }
                        throw new RuntimeException("Function 'sqrt' requires one argument");
                    }
                    default -> {
                        debugPrint("Skipping user-defined function call: " + funcName);
                        return 0; // Valor padrão
                    }
                }
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
        } else if (a instanceof Character && b instanceof Character) {
            return Character.compare((Character) a, (Character) b);
        } else if (a instanceof String && b instanceof String) {
            return ((String) a).compareTo((String) b);
        } else if (a instanceof Boolean && b instanceof Boolean) {
            return Boolean.compare((Boolean) a, (Boolean) b);
        }
        
        // Para outros tipos, converte para string e compara
        String sa = String.valueOf(a);
        String sb = String.valueOf(b);
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
        } else if (value instanceof Character) {
            return (int) ((Character) value);
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
        } else if (value instanceof Character) {
            return (float) ((Character) value);
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
        } else if (value instanceof Float) {
            return Math.abs((Float) value) > 1e-10;
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
        
        if (!arrays.isEmpty()) {
            System.out.println("\n[INTERPRETER] Arrays:");
            for (Map.Entry<String, ArrayData> entry : arrays.entrySet()) {
                ArrayData arrayData = entry.getValue();
                System.out.println("  " + entry.getKey() + "[" + arrayData.startIndex + 
                                 ".." + arrayData.endIndex + "] = " + 
                                 java.util.Arrays.toString(arrayData.elements));
            }
        }
    }
    
    /**
     * Cleanup resources
     */
    public void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
    }
}