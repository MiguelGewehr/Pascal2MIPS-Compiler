package entries;

import typing.Type;

public class VarEntry implements Entry {
    protected final String name;
    protected final int line;
    protected final Type varType;

    public VarEntry(String name, int line, Type varType) {
        this.name = name;
        this.line = line;
        this.varType = varType;
    }

    @Override
    public String getName() { return name; }
    
    @Override
    public int getLine() { return line; }
    
    @Override
    public Type getEntryType() { return varType; }
}