package interpreter;

import java.util.HashMap;
import java.util.Map;
import ast.AST;
import entries.FuncEntry;
import entries.ParamEntry;

/**
 * Classe que representa o contexto de execução de uma função/procedimento
 */
public class FunctionContext {
    private final String name;
    private final AST body;
    private final FuncEntry entry;
    private final Map<String, Object> localMemory;
    private Object returnValue;

    public FunctionContext(String name, AST body, FuncEntry entry) {
        this.name = name;
        this.body = body;
        this.entry = entry;
        this.localMemory = new HashMap<>();
        this.returnValue = null;
    }

    public String getName() {
        return name;
    }

    public AST getBody() {
        return body;
    }

    public FuncEntry getEntry() {
        return entry;
    }

    public Map<String, Object> getLocalMemory() {
        return localMemory;
    }

    public void setReturnValue(Object value) {
        this.returnValue = value;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
