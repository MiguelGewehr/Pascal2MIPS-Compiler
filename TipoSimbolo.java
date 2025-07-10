public enum TipoSimbolo {
    INTEGER, REAL, CHAR, STRING, BOOLEAN, IDENTIFIER, ARRAY, DESCONHECIDO;

    // Para literais (com base no tokenType do lexer)
    public static TipoSimbolo fromTokenType(int tokenType) {
        return switch (tokenType) {
            case PascalLexer.INTEGER -> INTEGER;
            case PascalLexer.REAL -> REAL;
            case PascalLexer.CHARACTER -> CHAR;
            case PascalLexer.STRING -> STRING;
            case PascalLexer.IDENTIFIER -> IDENTIFIER;
            default -> DESCONHECIDO;
        };
    }

    // Para tipos vindos de declarações (ex: "integer", "real", etc.)
    public static TipoSimbolo fromString(String tipoStr) {
        return switch (tipoStr.toLowerCase()) {
            case "integer" -> INTEGER;
            case "real" -> REAL;
            case "char" -> CHAR;
            case "string" -> STRING;
            case "boolean" -> BOOLEAN;
            default -> tipoStr.contains("array") ? ARRAY : DESCONHECIDO;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case INTEGER -> "integer";
            case REAL -> "real";
            case CHAR -> "char";
            case STRING -> "string";
            case BOOLEAN -> "boolean";
            case IDENTIFIER -> "identifier";
            case ARRAY -> "array";
            case DESCONHECIDO -> "desconhecido";
        };
    }
}
