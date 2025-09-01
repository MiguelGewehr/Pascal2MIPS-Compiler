package entries;

import typing.Type;

/**
 * Entrada para variáveis do tipo array na tabela de símbolos.
 */
public class ArrayEntry implements Entry {
    private final String name;
    private final int line;
    private final Type elementType;
    private final int startIndex;
    private final int endIndex;
    
    public ArrayEntry(String name, int line, Type elementType, int startIndex, int endIndex) {
        this.name = name;
        this.line = line;
        this.elementType = elementType;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public int getLine() { return line; }
    
    @Override
    public Type getEntryType() { return Type.ARRAY; }
    
    public Type getElementType() { return elementType; }
    
    public int getStartIndex() { return startIndex; }
    
    public int getEndIndex() { return endIndex; }
    
    public int getArraySize() { return endIndex - startIndex + 1; }
    
    /**
     * Verifica se um índice está dentro dos limites do array
     */
    public boolean isValidIndex(int index) {
        return index >= startIndex && index <= endIndex;
    }
    
    /**
     * Verifica compatibilidade entre dois arrays
     */
    public boolean isCompatible(ArrayEntry other) {
        return this.elementType == other.elementType &&
               this.startIndex == other.startIndex &&
               this.endIndex == other.endIndex;
    }
    
    @Override
    public String toString() {
        return String.format("ArrayEntry{name='%s', line=%d, type=array[%d..%d] of %s}", 
                           name, line, startIndex, endIndex, elementType);
    }
}