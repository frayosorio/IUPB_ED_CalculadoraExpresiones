public class Nodo {

    private TipoOperando tipo;
    private String valor;
    Nodo izquierdo;
    Nodo derecho;

    public Nodo(TipoOperando tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public TipoOperando getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public double getValorNUmerico() {
        try {
            if (tipo == TipoOperando.CONSTANTE) {
                return Double.parseDouble(valor);
            }
        } catch (Exception ex) {
        }
        return 0;
    }

}
