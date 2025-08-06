public class FuncEntry implements Entry {
    private final String name;
    private final int line;
    private final Type returnType;
    private final List<ParamEntry> parameters;
    
    public FuncEntry(String name, int line, Type returnType, List<ParamEntry> params) {
        this.name = name;
        this.line = line;
        this.returnType = returnType;
        this.parameters = params; 
    }

    public String getName() { return name; }
    public int getLine() { return line; }
    public Type getVarType() { return varType; }
}