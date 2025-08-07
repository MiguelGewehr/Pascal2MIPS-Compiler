package typing;

/**
 * Enumeração dos tipos primitivos de Pascal e classe para tipos compostos.
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
            case NO_TYPE -> "Sem tipo";
        };
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
    }
}