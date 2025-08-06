package entries;

import typing.Type;

public class ParamEntry implements Entry {
    private final String name;
    private final int line;
    private final Type paramType;
    private final boolean isReference; // Para parâmetros var

    public ParamEntry(String name, int line, Type paramType, boolean isReference) {
        this.name = name;
        this.line = line;
        this.paramType = paramType;
        this.isReference = isReference;
    }

    @Override
    public String getName() { return name; }
    
    @Override
    public int getLine() { return line; }
    
    @Override
    public Type getEntryType() { return paramType; }
    
    public boolean isReference() { return isReference; }
}