public class Simbolo {
    public String nome;
    public TipoSimbolo tipo;
    public CategoriaSimbolo categoria;
    public int linha;
    public TipoArray tipoArray; 

    public Simbolo(String nome, TipoSimbolo tipo, CategoriaSimbolo categoria, int linha) {
        this.nome = nome;
        this.tipo = tipo;
        this.tipoArray = null; // Inicializa como null, pois não é um array
        this.categoria = categoria;
        this.linha = linha;
    }

    public Simbolo(String nome, TipoArray tipoArray, CategoriaSimbolo categoria, int linha) {
        this.nome = nome;
        this.tipo = tipoArray.getTipoBase(); // Define o tipo base do array
        this.tipoArray = tipoArray; // Armazena o tipo array completo
        this.categoria = categoria;
        this.linha = linha;
        this.tipoArray = tipoArray;
    }
}
