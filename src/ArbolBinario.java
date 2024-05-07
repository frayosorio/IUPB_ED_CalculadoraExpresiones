public class ArbolBinario {
    private Nodo raiz;

    public ArbolBinario() {
        raiz = null;
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public ArbolBinario(Nodo raiz) {
        this.raiz = raiz;
    }

    public String recorrerInorden(Nodo n) {
        if (n != null) {
            return recorrerInorden(n.izquierdo) + " " +
                    n.getValor() + " " +
                    recorrerInorden(n.derecho);
        }
        return "";
    }

    public String recorrerPostorden(Nodo n) {
        if (n != null) {
            return recorrerPostorden(n.izquierdo) + " " +
                    recorrerPostorden(n.derecho) + " " +
                    n.getValor();
        }
        return "";
    }

    public String recorrerPreorden(Nodo n) {
        if (n != null) {
            return n.getValor() + " " +
                    recorrerPreorden(n.izquierdo) + " " +
                    recorrerPreorden(n.derecho);
        }
        return "";
    }

}
