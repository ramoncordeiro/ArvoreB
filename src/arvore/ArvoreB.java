package arvore;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;


public class ArvoreB<T extends Comparable<T>> {

   

   

    private NO raiz;

    /**
     * cria arvore vazia.
     */
    public ArvoreB() {
        raiz = null;
    }

    /**
     * Criar arvore balanceada
     */
    public ArvoreB(NO raiz) {
        this.raiz = raiz;
    }

    /**
     * insere um elemento na arvore.
     */
    public void inserir(T info) {
        inserir(info, raiz, null, false);
    }

    /**
     * veriica se o elemento ja está na arvore
     */
    public boolean verificarMembro(T info) { //verifica se um elemento ja ta na arvore
        return verificaMembro(info, raiz);
    }

    /**
     * remove elemento da arvore.
     */
    public void delete(T info) {
        delete(info, raiz);
        //root.informacao+" deletado");
    }

    /**
     * retorna texto representando a arvore.
     */
    public String toString() {
        return em_Ordem();
    }

    /**
     * Retorna todos elementos da arvore  em-order.
     */
    public String em_Ordem() {
        return ArvoreB.this.em_Ordem(raiz);
    }

    /**
     * Retorna todos elementos da arvore em pre-ordem.
     */
    public String preOrdem() {
        return preOrdem(raiz);
    }

    /**
     * Returna todos  elementos da arvore em pos-order.
     * @return 
     */
    public String posOrdem() {
        return posOrdem(raiz);
    }

    /**
     * Retorna  altura da arvore
     */
    public int getAltura() {
        return ArvoreB.this.getAltura(raiz);
    }

    private void inserir(T info, NO no, NO pai, boolean direita) {

        if (no == null) {
            if (pai == null) {
                raiz = no = new NO(info, pai);
            } else if (direita) {
                pai.direita = no = new NO(info, pai);
            } else {
                pai.esquerda = no = new NO(info, pai);
            }
            reorganizarInserir(no, false);
        } else if (info.compareTo(no.informacao) == 0) {
            no.informacao = info;
        } else if (info.compareTo(no.informacao) > 0) {
            inserir(info, no.direita, no, true);
        } else {
            inserir(info, no.esquerda, no, false);
        }
    }

    private boolean verificaMembro(T info, NO no) { 

        boolean membro = false;

        if (no == null) {
            membro = false;
        } else if (info.compareTo(no.informacao) == 0) {
            membro = true;
        } else if (info.compareTo(no.informacao) > 0) {
            membro = verificaMembro(info, no.direita);
        } else {
            membro = verificaMembro(info, no.esquerda);
        }

        return membro;
    }

    private void delete(T info, NO no) throws NoSuchElementException {

        if (no == null) {
            throw new NoSuchElementException();
        } else if (info.compareTo(no.informacao) == 0) {
            deleteNO(no);
        } else if (info.compareTo(no.informacao) > 0) { //compareTo é nativo JAVA
            delete(info, no.direita);
        } else {
            delete(info, no.esquerda);
        }
    }

    private void deleteNO(NO no) {

        NO eNode, minMaxNO, delete_no = null;
        boolean noDireita = false;

        if (no.folha()) {
            if (no.pai == null) {
                raiz = null;
            } else if (no.verificaDireita()) {
                no.pai.direita = null;
                noDireita = true;
            } else if (no.verificarEsquerda()) {
                no.pai.esquerda = null;
            }
            delete_no = no;
        } else if (no.filhoEsquerda()) {
            minMaxNO = no.esquerda;
            for (eNode = no.esquerda; eNode != null; eNode = eNode.direita) {
                minMaxNO = eNode;
            }
            delete_no = minMaxNO;
            no.informacao = minMaxNO.informacao;

            if (no.esquerda.direita != null) {
                minMaxNO.pai.direita = minMaxNO.esquerda;
                noDireita = true;
            } else {
                minMaxNO.pai.esquerda = minMaxNO.esquerda;
            }

            if (minMaxNO.esquerda != null) {
                minMaxNO.esquerda.pai = minMaxNO.pai;
            }
        } else if (no.filhoDireita()) {
            minMaxNO = no.direita;
            delete_no = minMaxNO;
            noDireita = true;

            no.informacao = minMaxNO.informacao;

            no.direita = minMaxNO.direita;
            if (no.direita != null) {
                no.direita.pai = no;
            }
            no.esquerda = minMaxNO.esquerda;
            if (no.esquerda != null) {
                no.esquerda.pai = no;
            }
        }
        reorganizarDelete(delete_no.pai, noDireita);//organiza a arvore apos deletar um elemento.
    }

    private int getAltura(NO node) {
        int altura = 0;

        if (node == null) {
            altura = -1;
        } else {
            altura = 1 + Math.max(ArvoreB.this.getAltura(node.esquerda), ArvoreB.this.getAltura(node.direita));
        }
        return altura;
    }

    private String em_Ordem(NO no) {

        String resultado = "";
        if (no != null) {
            resultado = resultado + ArvoreB.this.em_Ordem(no.esquerda) + " ";
            resultado = resultado + no.informacao.toString();
            resultado = resultado + ArvoreB.this.em_Ordem(no.direita);
        }
        return resultado;
    }

    private String preOrdem(NO no) {

        String resultado = "";
        if (no != null) {
            resultado = resultado + no.informacao.toString() + " ";
            resultado = resultado + preOrdem(no.esquerda);
            resultado = resultado + preOrdem(no.direita);
        }
        return resultado;
    }

    private String posOrdem(NO no) {

        String resultado = "";
        if (no != null) {
            resultado = resultado + posOrdem(no.esquerda);
            resultado = resultado + posOrdem(no.direita);
            resultado = resultado + no.informacao.toString() + " "; //converte para string o to_string
        }
        return resultado;
    }

    private void reorganizarInserir(NO no, boolean antigoDireita) { //antigoDireita é a posição anterior em relacao ao pai.

        if (no != raiz) {
            if (no.pai.balancear == '_') {
                if (no.verificarEsquerda()) {
                    no.pai.balancear = '/';
                    reorganizarInserir(no.pai, false);
                } else {
                    no.pai.balancear = '\\';
                    reorganizarInserir(no.pai, true);
                }
            } else if (no.pai.balancear == '/') {
                if (no.verificaDireita()) {
                    no.pai.balancear = '_';
                } else {
                    if (!antigoDireita) {
                        rotacaoDireita(no.pai);
                    } else {
                        duplaRotacaoDireita(no.pai);
                    }
                }
            } else if (no.pai.balancear == '\\') {
                if (no.verificarEsquerda()) {
                    no.pai.balancear = '_';
                } else {
                    if (antigoDireita) {
                        rotacionarEsquerda(no.pai);
                    } else {
                        duplaRotacaoEsquerda(no.pai);
                    }
                }
            }
        }
    }

    private void reorganizarDelete(NO z, boolean direita_antigo) {

        NO parent;
        boolean verificarDireita = false;
        boolean subir = false; //subir = ir para nó do pai.
        boolean permissaoSubida;

        if (z == null) {
            return;
        }

        parent = z.pai;
        permissaoSubida = (parent != null);

        if (permissaoSubida) {
            verificarDireita = z.verificaDireita();
        }

        if (z.balancear == '_') {
            if (direita_antigo) { //direita_antigo é a posição anterior, isto é, estava a dreita do pai.
                z.balancear = '/';
            } else {
                z.balancear = '\\';
            }
        } else if (z.balancear == '/') {
            if (direita_antigo) {
                if (z.esquerda.balancear == '\\') {
                    duplaRotacaoDireita(z);
                    subir = true;
                } else {
                    rotacaoDireita(z);
                    if (z.balancear == '_') {
                        subir = true;
                    }
                }
            } else {
                z.balancear = '_';
                subir = true;
            }
        } else {
            if (direita_antigo) {
                z.balancear = '_';
                subir = true;
            } else {
                if (z.direita.balancear == '/') {
                    duplaRotacaoEsquerda(z);
                    subir = true;
                } else {
                    rotacionarEsquerda(z);
                    if (z.balancear == '_') {
                        subir = true;
                    }
                }
            }
        }

        if (permissaoSubida && subir) {
            reorganizarDelete(parent, verificarDireita);
        }
    }

    private void rotacionarEsquerda(NO a) {

        NO b = a.direita;

        if (a.pai == null) {
            raiz = b;
        } else {
            if (a.verificarEsquerda()) {
                a.pai.esquerda = b;
            } else {
                a.pai.direita = b;
            }
        }

        a.direita = b.esquerda;
        if (a.direita != null) {
            a.direita.pai = a;
        }

        b.pai = a.pai;
        a.pai = b;
        b.esquerda = a;

        if (b.balancear == '_') {
            a.balancear = '\\';
            b.balancear = '/';
        } else {
            a.balancear = '_';
            b.balancear = '_';
        }
    }

    private void rotacaoDireita(NO a) {

        NO b = a.esquerda;

        if (a.pai == null) {
            raiz = b;
        } else {
            if (a.verificarEsquerda()) {
                a.pai.esquerda = b;
            } else {
                a.pai.direita = b;
            }
        }

        a.esquerda = b.direita;
        if (a.esquerda != null) {
            a.esquerda.pai = a;
        }

        b.pai = a.pai;
        a.pai = b;
        b.direita = a;

        if (b.balancear == '_') {
            a.balancear = '/';
            b.balancear = '\\';
        } else {
            a.balancear = '_';
            b.balancear = '_';
        }
    }

    private void duplaRotacaoEsquerda(NO a) {

        NO b = a.direita;
        NO c = b.esquerda;

        if (a.pai == null) {
            raiz = c;
        } else {
            if (a.verificarEsquerda()) {
                a.pai.esquerda = c;
            } else {
                a.pai.direita = c;
            }
        }

        c.pai = a.pai;

        a.direita = c.esquerda;
        if (a.direita != null) {
            a.direita.pai = a;
        }
        b.esquerda = c.direita;
        if (b.esquerda != null) {
            b.esquerda.pai = b;
        }

        c.esquerda = a;
        c.direita = b;

        a.pai = c;
        b.pai = c;

        if (c.balancear == '/') {
            a.balancear = '_';
            b.balancear = '\\';
        } else if (c.balancear == '\\') {
            a.balancear = '/';
            b.balancear = '_';
        } else {
            a.balancear = '_';
            b.balancear = '_';
        }

        c.balancear = '_';
    }

    private void duplaRotacaoDireita(NO a) {

        NO b = a.esquerda;
        NO c = b.direita;

        if (a.pai == null) {
            raiz = c;
        } else {
            if (a.verificarEsquerda()) {
                a.pai.esquerda = c;
            } else {
                a.pai.direita = c;
            }
        }

        c.pai = a.pai;

        a.esquerda = c.direita;
        if (a.esquerda != null) {
            a.esquerda.pai = a;
        }
        b.direita = c.esquerda;
        if (b.direita != null) {
            b.direita.pai = b;
        }

        c.direita = a;
        c.esquerda = b;

        a.pai = c;
        b.pai = c;

        if (c.balancear == '/') {
            b.balancear = '_';
            a.balancear = '\\';
        } else if (c.balancear == '\\') {
            b.balancear = '/';
            a.balancear = '_';
        } else {
            b.balancear = '_';
            a.balancear = '_';
        }
        c.balancear = '_';
    }

    class NO {

        T informacao;

        NO pai;

        NO esquerda;

        NO direita;

        char balancear;

        public NO(T information, NO pai) {
            this.informacao = information;
            this.pai = pai;
            this.esquerda = null;
            this.direita = null;
            this.balancear = '_';
        }

        boolean folha() {
            return ((esquerda == null) && (direita == null));
        }

        boolean verificaNO() { //verifica se é folha ou nó intermediario
            return !folha();
        }

        boolean filhoEsquerda() { //verifica se possui filho a esquerda
            return (null != esquerda);
        }

        boolean filhoDireita() { //verifica se possui filho a direita
            return (direita != null);
        }

        boolean verificarEsquerda() { //verifica se o Nó atual está na esquerda do pai
            return (pai.esquerda == this);
        }

        boolean verificaDireita() { //verifica se Nó atual está a direita do pai.
            return (pai.direita == this);
        }
        
       
        
       
              
    }
}