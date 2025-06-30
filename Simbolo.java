public class Simbolo {
    public String nome;
    public String tipo;
    public String categoria; // variável, vetor, função, procedimento
    public String escopo;
    public int linha;

    public Simbolo(String nome, String tipo, String categoria, String escopo, int linha) {
        this.nome = nome;
        this.tipo = tipo;
        this.categoria = categoria;
        this.escopo = escopo;
        this.linha = linha;
    }
}
