public class Simbolo {
    public String nome;
    public TipoSimbolo tipo;
    public CategoriaSimbolo categoria;
    public int linha;

    public Simbolo(String nome, TipoSimbolo tipo, CategoriaSimbolo categoria, int linha) {
        this.nome = nome;
        this.tipo = tipo;
        this.categoria = categoria;
        this.linha = linha;
    }
}
