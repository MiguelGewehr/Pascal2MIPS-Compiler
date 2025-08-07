package entries;

import typing.Type;

/**
 * Entrada para variáveis do tipo array na tabela de símbolos.
 */
public class ArrayEntry implements Entry {
    private final String name;
    private final int line;
    private final Type.ArrayType arrayType;
    
    public ArrayEntry(String name, int line, Type.ArrayType arrayType) {
        this.name = name;
        this.line = line;
        this.arrayType = arrayType;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public int getLine() { return line; }
    
    @Override
    public Type getEntryType() { return Type.ARRAY; }
    
    public Type.ArrayType getArrayType() { return arrayType; }
    
    public Type getElementType() { return arrayType.getElementType(); }
    
    public int getStartIndex() { return arrayType.getStartIndex(); }
    
    public int getEndIndex() { return arrayType.getEndIndex(); }
    
    public int getArraySize() { return arrayType.getSize(); }
    
    /**
     * Verifica se um índice está dentro dos limites do array
     */
    public boolean isValidIndex(int index) {
        return index >= arrayType.getStartIndex() && index <= arrayType.getEndIndex();
    }
    
    @Override
    public String toString() {
        return String.format("ArrayEntry{name='%s', line=%d, type=%s}", 
                           name, line, arrayType.toString());
    }
}