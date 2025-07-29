public class TipoArray{
    private final TipoSimbolo tipoBase;
    private final int inicio;
    private final int fim;

    public TipoArray(TipoSimbolo tipoBase, int inicio, int fim) {
        this.tipoBase = tipoBase;
        this.inicio = inicio;
        this.fim = fim;
    }

    public TipoSimbolo getTipoBase() {
        return tipoBase;
    }

    public int getInicio() {
        return inicio;
    }

    public int getFim() {
        return fim;
    }

    @Override
    public String toString() {
        return "array[" + inicio + ".." + fim + "] of " + tipoBase;
    }
}