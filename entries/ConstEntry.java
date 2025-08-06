package entries;

import typing.Type;

public class ConstEntry implements Entry {
    private final String name;
    private final int line;
    private final Type constType;
    private final Object value;

    public ConstEntry(String name, int line, Type constType, Object value) {
        this.name = name;
        this.line = line;
        this.constType = constType;
        this.value = value;
    }

    @Override
    public String getName() { return name; }
    
    @Override
    public int getLine() { return line; }
    
    @Override
    public Type getEntryType() { return constType; }
    
    public Object getValue() { return value; }
}