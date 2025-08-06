package entries;

import typing.Type;

public class ArrayEntry implements Entry {
    private final String name;
    private final int line;
    private final Type elementType;
    private final int start;
    private final int end;

    public ArrayEntry(String name, int line, Type elementType, int start, int end) {
        this.name = name;
        this.line = line;
        this.elementType = elementType;
        this.start = start;
        this.end = end;
    }

    @Override
    public String getName() { return name; }
    
    @Override
    public int getLine() { return line; }
    
    @Override
    public Type getEntryType() { return elementType; }
    
    public Type getElementType() { return elementType; }
    public int getStart() { return start; }
    public int getEnd() { return end; }
}