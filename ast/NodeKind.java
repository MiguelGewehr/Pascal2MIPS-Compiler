package ast;

// Enumeração dos tipos de nós de uma AST para Pascal ISO 7185.
// Baseado na estrutura do EZLang mas adaptado para Pascal
public enum NodeKind {
    // Nós do programa e estrutura
    PROGRAM_NODE,
    BLOCK_NODE,
    
    // Declarações
    CONST_SECTION_NODE,
    CONST_DECL_NODE,
    VAR_SECTION_NODE,
    VAR_DECL_NODE,
    VAR_LIST_NODE,
    
    // Tipos e arrays
    ARRAY_TYPE_NODE,
    
    // Subrotinas
    PROC_DECL_NODE,
    FUNC_DECL_NODE,
    PARAM_LIST_NODE,
    PARAM_NODE,
    
    // Statements
    COMPOUND_STMT_NODE,
    ASSIGN_NODE,
    PROC_CALL_NODE,
    IF_NODE,
    WHILE_NODE,
    EMPTY_STMT_NODE,
    
    // Expressões aritméticas
    PLUS_NODE,
    MINUS_NODE,
    TIMES_NODE,
    DIVIDE_NODE,
    DIV_NODE,
    MOD_NODE,
    
    // Expressões lógicas
    AND_NODE,
    OR_NODE,
    NOT_NODE,
    
    // Comparações
    EQ_NODE,
    NEQ_NODE,
    LT_NODE,
    GT_NODE,
    LE_NODE,
    GE_NODE,
    
    // Valores constantes
    INT_VAL_NODE,
    REAL_VAL_NODE,
    CHAR_VAL_NODE,
    STR_VAL_NODE,
    BOOL_VAL_NODE,
    
    // Variáveis e acesso
    VAR_USE_NODE,
    ARRAY_ACCESS_NODE,
    FUNC_CALL_NODE,
    
    // Conversões de tipo (para futuro uso)
    I2R_NODE,    // Integer to Real
    C2S_NODE,    // Char to String
    
    // Expressões parentizadas
    PAREN_EXPR_NODE;

    @Override
    public String toString() {
        return switch(this) {
            case PROGRAM_NODE -> "program";
            case BLOCK_NODE -> "block";
            case CONST_SECTION_NODE -> "const_section";
            case CONST_DECL_NODE -> "const_decl";
            case VAR_SECTION_NODE -> "var_section";
            case VAR_DECL_NODE -> "var_decl";
            case VAR_LIST_NODE -> "var_list";
            case ARRAY_TYPE_NODE -> "array_type";
            case PROC_DECL_NODE -> "procedure";
            case FUNC_DECL_NODE -> "function";
            case PARAM_LIST_NODE -> "param_list";
            case PARAM_NODE -> "parameter";
            case COMPOUND_STMT_NODE -> "compound";
            case ASSIGN_NODE -> ":=";
            case PROC_CALL_NODE -> "proc_call";
            case IF_NODE -> "if";
            case WHILE_NODE -> "while";
            case EMPTY_STMT_NODE -> "empty";
            case PLUS_NODE -> "+";
            case MINUS_NODE -> "-";
            case TIMES_NODE -> "*";
            case DIVIDE_NODE -> "/";
            case DIV_NODE -> "div";
            case MOD_NODE -> "mod";
            case AND_NODE -> "and";
            case OR_NODE -> "or";
            case NOT_NODE -> "not";
            case EQ_NODE -> "=";
            case NEQ_NODE -> "<>";
            case LT_NODE -> "<";
            case GT_NODE -> ">";
            case LE_NODE -> "<=";
            case GE_NODE -> ">=";
            case INT_VAL_NODE -> "";
            case REAL_VAL_NODE -> "";
            case CHAR_VAL_NODE -> "";
            case STR_VAL_NODE -> "";
            case BOOL_VAL_NODE -> "";
            case VAR_USE_NODE -> "var_use";
            case ARRAY_ACCESS_NODE -> "array_access";
            case FUNC_CALL_NODE -> "func_call";
            case I2R_NODE -> "I2R";
            case C2S_NODE -> "C2S";
            case PAREN_EXPR_NODE -> "()";
        };
    }

    public static boolean hasData(NodeKind kind) {
        return switch(kind) {
            case INT_VAL_NODE, REAL_VAL_NODE, CHAR_VAL_NODE, STR_VAL_NODE, 
                 BOOL_VAL_NODE, VAR_USE_NODE, VAR_DECL_NODE, CONST_DECL_NODE,
                 PROC_DECL_NODE, FUNC_DECL_NODE, PROC_CALL_NODE, FUNC_CALL_NODE,
                 PARAM_NODE -> true;
            default -> false;
        };
    }
}