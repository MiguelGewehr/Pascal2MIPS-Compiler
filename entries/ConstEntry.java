public class ConstEntry extends Entry {
    private final Object value;

    public ConstEntry(String name, int line, Type constType, Object value) {
        this.name = name
        this.line = line 
        this.constType = constType;
        this.value = value;
    }

    public String getName() { return name; }
    public int getLine() { return line; }
    public Type getVarType() { return varType; }
    public Object getValue() { return value; }
}