package typing;

/**
 * Enumeração dos tipos primitivos de Pascal e classe para tipos compostos.
 * Versão melhorada com métodos de compatibilidade de tipos.
 */
public enum Type {
    INTEGER,
    REAL,
    CHAR,
    STRING,
    BOOLEAN,
    ARRAY,
    NO_TYPE;

    @Override
    public String toString() {
        return switch (this) {
            case INTEGER -> "integer";
            case REAL -> "real";
            case CHAR -> "char";
            case STRING -> "string";
            case BOOLEAN -> "boolean";
            case ARRAY -> "array";
            case NO_TYPE -> "no_type";
        };
    }

    /**
     * Verifica se este tipo é compatível para atribuição com outro tipo
     */
    public boolean isAssignableFrom(Type other) {
        if (this == other) return true;
        
        // Real pode receber Integer (widening)
        if (this == REAL && other == INTEGER) return true;
        
        return false;
    }

    /**
     * Verifica se dois tipos são compatíveis para operações aritméticas
     */
    public static boolean areArithmeticCompatible(Type t1, Type t2) {
        return (t1 == INTEGER || t1 == REAL) && (t2 == INTEGER || t2 == REAL);
    }

    /**
     * Retorna o tipo resultante de uma operação aritmética entre dois tipos
     */
    public static Type getArithmeticResultType(Type t1, Type t2) {
        if (!areArithmeticCompatible(t1, t2)) {
            return NO_TYPE;
        }
        
        // Se qualquer um for REAL, resultado é REAL
        if (t1 == REAL || t2 == REAL) {
            return REAL;
        } else {
            return INTEGER;
        }
    }

    /**
     * Verifica se dois tipos são compatíveis para comparação
     */
    public static boolean areComparableTypes(Type t1, Type t2) {
        // Tipos numéricos podem ser comparados entre si
        if (areArithmeticCompatible(t1, t2)) return true;
        
        // Tipos idênticos podem ser comparados (exceto arrays)
        if (t1 == t2 && t1 != ARRAY) return true;
        
        return false;
    }

    /**
     * Verifica se dois tipos são compatíveis para operações lógicas
     */
    public static boolean areLogicalCompatible(Type t1, Type t2) {
        return t1 == BOOLEAN && t2 == BOOLEAN;
    }

    /**
     * Classe para representar tipos array com informações completas
     */
    public static class ArrayType {
        private final Type elementType;
        private final int startIndex;
        private final int endIndex;

        public ArrayType(Type elementType, int startIndex, int endIndex) {
            this.elementType = elementType;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public Type getElementType() { return elementType; }
        public int getStartIndex() { return startIndex; }
        public int getEndIndex() { return endIndex; }
        public int getSize() { return endIndex - startIndex + 1; }

        /**
         * Verifica se um índice está dentro dos limites válidos
         */
        public boolean isValidIndex(int index) {
            return index >= startIndex && index <= endIndex;
        }

        /**
         * Verifica compatibilidade entre dois tipos array
         */
        public boolean isCompatible(ArrayType other) {
            return this.elementType == other.elementType &&
                   this.startIndex == other.startIndex &&
                   this.endIndex == other.endIndex;
        }

        @Override
        public String toString() {
            return String.format("array[%d..%d] of %s", startIndex, endIndex, elementType);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ArrayType)) return false;
            ArrayType other = (ArrayType) obj;
            return elementType == other.elementType &&
                   startIndex == other.startIndex &&
                   endIndex == other.endIndex;
        }

        @Override
        public int hashCode() {
            return elementType.hashCode() ^ startIndex ^ endIndex;
        }
    }

    /**
     * Tabelas de unificação para diferentes operadores (similar ao exemplo EZLang)
     * Adaptadas para Pascal ISO 7185
     */
    
    // Tabela de compatibilidade para operações aritméticas (+, -, *, /, div, mod)
    private static final Type[][] ARITHMETIC_COMPATIBILITY = {
        //       INT      REAL     CHAR     STRING   BOOLEAN  ARRAY    NO_TYPE
        /*INT*/ {INTEGER, REAL,    NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*REAL*/{REAL,    REAL,    NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*CHAR*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*STR*/ {NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*BOOL*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*ARR*/ {NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*NONE*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE}
    };

    // Tabela de compatibilidade para comparações (=, <>, <, >, <=, >=)
    private static final Type[][] COMPARISON_COMPATIBILITY = {
        //       INT      REAL     CHAR     STRING   BOOLEAN  ARRAY    NO_TYPE
        /*INT*/ {BOOLEAN, BOOLEAN, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*REAL*/{BOOLEAN, BOOLEAN, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*CHAR*/{NO_TYPE, NO_TYPE, BOOLEAN, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*STR*/ {NO_TYPE, NO_TYPE, NO_TYPE, BOOLEAN, NO_TYPE, NO_TYPE, NO_TYPE},
        /*BOOL*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, BOOLEAN, NO_TYPE, NO_TYPE},
        /*ARR*/ {NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*NONE*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE}
    };

    // Tabela de compatibilidade para operações lógicas (and, or, not)
    private static final Type[][] LOGICAL_COMPATIBILITY = {
        //       INT      REAL     CHAR     STRING   BOOLEAN  ARRAY    NO_TYPE
        /*INT*/ {NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*REAL*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*CHAR*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*STR*/ {NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*BOOL*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, BOOLEAN, NO_TYPE, NO_TYPE},
        /*ARR*/ {NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*NONE*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE}
    };

    // Tabela de compatibilidade para atribuições
    private static final Type[][] ASSIGNMENT_COMPATIBILITY = {
        //       INT      REAL     CHAR     STRING   BOOLEAN  ARRAY    NO_TYPE
        /*INT*/ {INTEGER, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*REAL*/{REAL,    REAL,    NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*CHAR*/{NO_TYPE, NO_TYPE, CHAR,    NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE},
        /*STR*/ {NO_TYPE, NO_TYPE, NO_TYPE, STRING,  NO_TYPE, NO_TYPE, NO_TYPE},
        /*BOOL*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, BOOLEAN, NO_TYPE, NO_TYPE},
        /*ARR*/ {NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE}, // Arrays requerem verificação especial
        /*NONE*/{NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE, NO_TYPE}
    };

    /**
     * Métodos de unificação usando as tabelas
     */
    public Type unifyArithmetic(Type other) {
        if (this.ordinal() >= ARITHMETIC_COMPATIBILITY.length || 
            other.ordinal() >= ARITHMETIC_COMPATIBILITY[0].length) {
            return NO_TYPE;
        }
        return ARITHMETIC_COMPATIBILITY[this.ordinal()][other.ordinal()];
    }

    public Type unifyComparison(Type other) {
        if (this.ordinal() >= COMPARISON_COMPATIBILITY.length || 
            other.ordinal() >= COMPARISON_COMPATIBILITY[0].length) {
            return NO_TYPE;
        }
        return COMPARISON_COMPATIBILITY[this.ordinal()][other.ordinal()];
    }

    public Type unifyLogical(Type other) {
        if (this.ordinal() >= LOGICAL_COMPATIBILITY.length || 
            other.ordinal() >= LOGICAL_COMPATIBILITY[0].length) {
            return NO_TYPE;
        }
        return LOGICAL_COMPATIBILITY[this.ordinal()][other.ordinal()];
    }

    public Type unifyAssignment(Type other) {
        if (this.ordinal() >= ASSIGNMENT_COMPATIBILITY.length || 
            other.ordinal() >= ASSIGNMENT_COMPATIBILITY[0].length) {
            return NO_TYPE;
        }
        return ASSIGNMENT_COMPATIBILITY[this.ordinal()][other.ordinal()];
    }

    /**
     * Métodos de conveniência para verificações específicas do Pascal
     */
    public boolean isNumeric() {
        return this == INTEGER || this == REAL;
    }

    public boolean isOrdinal() {
        return this == INTEGER || this == CHAR || this == BOOLEAN;
    }

    public boolean isScalar() {
        return this == INTEGER || this == REAL || this == CHAR || this == BOOLEAN;
    }

    /**
     * Verifica se o tipo suporta operações aritméticas
     */
    public boolean supportsArithmetic() {
        return this == INTEGER || this == REAL;
    }

    /**
     * Verifica se o tipo suporta comparações ordenadas (<, >, <=, >=)
     */
    public boolean supportsOrderedComparison() {
        return this == INTEGER || this == REAL || this == CHAR;
    }
}