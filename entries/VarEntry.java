public abstract class VarEntry implements Entry {
    protected final String name;
    protected final int line;
    protected final Type varType;

    public VarEntry(String name, int line, Type varType) {
        this.name = name;
        this.line = line;
        this.varType = varType;
    }

    public String getName() { return name; }
    public int getLine() { return line; }
    public Type getVarType() { return varType; }
}