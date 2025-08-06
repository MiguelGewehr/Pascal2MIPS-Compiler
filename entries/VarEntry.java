public class VarEntry implements Entry {
    private final String name;
    private final int line;
    private final Type varType;
    private final boolean isArray;
    private final Type arrayElementType;
    private final int arrayStart;
    private final int arrayEnd;
    
    // Construtor para variável simples
    public VarEntry(String name, int line, Type varType) {
        this(name, line, varType, false, null, 0, 0);
    }
    
    // Construtor para array
    public VarEntry(String name, int line, Type elementType, int start, int end) {
        this(name, line, Type.ARRAY, true, elementType, start, end);
    }
    
    private VarEntry(String name, int line, Type varType, boolean isArray, 
                    Type elementType, int start, int end) {
        // Inicialização...
    }
    
}