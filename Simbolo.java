public class Simbolo {
    public String nome;
    public String tipo;
    public String categoria; // variável, vetor, função, procedimento, parâmetro, constante
    public int linha;

    public Simbolo(String nome, String tipo, String categoria, int linha) {
        this.nome = nome;
        this.tipo = tipo;
        this.categoria = categoria;
        this.linha = linha;
    }
}