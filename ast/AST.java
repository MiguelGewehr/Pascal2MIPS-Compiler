package ast;

import static typing.Type.NO_TYPE;

import java.util.ArrayList;
import java.util.List;

import tables.SymbolTable;
import typing.Type;

// Implementação dos nós da AST para Pascal.
public class AST {

    // Todos os campos são finais para simplificar
    public final NodeKind kind;
    public final int intData;
    public final float floatData;
    public final String stringData;  // Para identificadores e strings
    public final Type type;
    private final List<AST> children;

    // Construtor completo privado
    private AST(NodeKind kind, int intData, float floatData, String stringData, Type type) {
        this.kind = kind;
        this.intData = intData;
        this.floatData = floatData;
        this.stringData = stringData;
        this.type = type;
        this.children = new ArrayList<AST>();
    }

    // Cria o nó com um dado inteiro
    public AST(NodeKind kind, int intData, Type type) {
        this(kind, intData, 0.0f, null, type);
    }

    // Cria o nó com um dado float
    public AST(NodeKind kind, float floatData, Type type) {
        this(kind, 0, floatData, null, type);
    }

    // Cria o nó com uma string
    public AST(NodeKind kind, String stringData, Type type) {
        this(kind, 0, 0.0f, stringData, type);
    }

    // Cria o nó só com tipo
    public AST(NodeKind kind, Type type) {
        this(kind, 0, 0.0f, null, type);
    }

    // Adiciona um novo filho ao nó
    public void addChild(AST child) {
        if (child != null) {
            this.children.add(child);
        }
    }

    // Retorna o filho no índice passado
    public AST getChild(int idx) {
        if (idx >= 0 && idx < children.size()) {
            return this.children.get(idx);
        }
        return null;
    }

    // Retorna o número de filhos
    public int getChildCount() {
        return this.children.size();
    }

    // Cria um nó e pendura todos os filhos passados como argumento
    public static AST newSubtree(NodeKind kind, Type type, AST... children) {
        AST node = new AST(kind, type);
        for (AST child : children) {
            if (child != null) {
                node.addChild(child);
            }
        }
        return node;
    }

    // Cria um nó com string e filhos
    public static AST newSubtree(NodeKind kind, String stringData, Type type, AST... children) {
        AST node = new AST(kind, stringData, type);
        for (AST child : children) {
            if (child != null) {
                node.addChild(child);
            }
        }
        return node;
    }

    // Variáveis internas usadas para geração da saída em DOT
    private static int nr;
    private static SymbolTable symbolTable;

    // Imprime recursivamente a codificação em DOT da subárvore começando no nó atual
    private int printNodeDot() {
        int myNr = nr++;

        System.err.printf("node%d[label=\"", myNr);
        if (this.type != NO_TYPE) {
            System.err.printf("(%s) ", this.type.toString());
        }

        // Tratamento especial para nós com identificadores
        if ((this.kind == NodeKind.VAR_DECL_NODE || this.kind == NodeKind.VAR_USE_NODE ||
             this.kind == NodeKind.PROC_DECL_NODE || this.kind == NodeKind.FUNC_DECL_NODE ||
             this.kind == NodeKind.PROC_CALL_NODE || this.kind == NodeKind.FUNC_CALL_NODE ||
             this.kind == NodeKind.PARAM_NODE || this.kind == NodeKind.CONST_DECL_NODE) && 
             this.stringData != null) {
            System.err.printf("%s", this.stringData);
        } else {
            System.err.printf("%s", this.kind.toString());
        }

        // Adiciona dados específicos do nó
        if (NodeKind.hasData(this.kind)) {
            if (this.kind == NodeKind.REAL_VAL_NODE) {
                System.err.printf(" %.2f", this.floatData);
            } else if (this.kind == NodeKind.STR_VAL_NODE && this.stringData != null) {
                System.err.printf(" \\\"%s\\\"", this.stringData.replace("\"", ""));
            } else if (this.kind == NodeKind.CHAR_VAL_NODE && this.stringData != null) {
                System.err.printf(" '%s'", this.stringData.replace("'", ""));
            } else if (this.kind == NodeKind.BOOL_VAL_NODE) {
                System.err.printf(" %s", this.intData == 1 ? "true" : "false");
            } else if (this.stringData != null && 
                      (this.kind == NodeKind.VAR_USE_NODE || this.kind == NodeKind.VAR_DECL_NODE ||
                       this.kind == NodeKind.PROC_DECL_NODE || this.kind == NodeKind.FUNC_DECL_NODE ||
                       this.kind == NodeKind.PROC_CALL_NODE || this.kind == NodeKind.FUNC_CALL_NODE ||
                       this.kind == NodeKind.CONST_DECL_NODE)) {
                // Já foi tratado acima
            } else if (this.kind == NodeKind.INT_VAL_NODE) {
                System.err.printf(" %d", this.intData);
            }
        }
        System.err.printf("\"];\n");

        for (int i = 0; i < this.children.size(); i++) {
            int childNr = this.children.get(i).printNodeDot();
            System.err.printf("node%d -> node%d;\n", myNr, childNr);
        }
        return myNr;
    }

    // Imprime a árvore toda em stderr
    public static void printDot(AST tree, SymbolTable table) {
        nr = 0;
        symbolTable = table;
        System.err.printf("digraph {\ngraph [ordering=\"out\"];\n");
        if (tree != null) {
            tree.printNodeDot();
        }
        System.err.printf("}\n");
    }
}