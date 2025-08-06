package typing;

// Enumeração dos tipos primitivos de Pascal.
public enum Type {
	INTEGER, 
    REAL, 
    CHAR, 
    STRING, 
    BOOLEAN, 
    NO_TYPE;

	@Override
    public String toString() {
        return switch (this) {
            case INTEGER -> "integer";
            case REAL -> "real";
            case CHAR -> "char";
            case STRING -> "string";
            case BOOLEAN -> "boolean";
            case NO_TYPE -> "Sem tipo";
        };
    }
}
