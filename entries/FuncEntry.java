package entries;

import typing.Type;
import java.util.List;

public class FuncEntry implements Entry {
    private final String name;
    private final int line;
    private final Type returnType;
    private final List<ParamEntry> parameters;

    public FuncEntry(String name, int line, Type returnType, List<ParamEntry> parameters) {
        this.name = name;
        this.line = line;
        this.returnType = returnType;
        this.parameters = parameters != null ? parameters : new java.util.ArrayList<>();
    }

    @Override
    public String getName() { return name; }
    
    @Override
    public int getLine() { return line; }
    
    @Override
    public Type getEntryType() { return returnType; }
    
    public List<ParamEntry> getParameters() { return parameters; }
}