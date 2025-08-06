public class ArrayEntry extends Entry {
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

    public Type getElementType() { return elementType; }
    public int getStart() { return start; }
    public int getEnd() { return end; }
}
