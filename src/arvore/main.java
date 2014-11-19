/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arvore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

/**
 *
 * @author Asus
 */
public class main {

    private static ArvoreB<Integer> arvore = new ArvoreB<Integer>();

    private static BufferedReader leitor = new BufferedReader(
            new InputStreamReader(System.in));

    
     public static void main(String args[]) throws IOException {

        //System.out.println("Programa Arvore B");
        //System.out.println("*****************************");

        String entrada;
        Integer valor;

        do {
            entrada = stringInput("Por favor insira uma letra: [i]-inserir, [d]-deletar, [e]exit");
            switch (entrada.charAt(0)) {
            case 'i':
                valor = Integer.parseInt(stringInput("inserir: "), 10);
                if (arvore.verificarMembro(valor)) {
                    System.out.println("valor " + valor + " valor já existe na arvore");
                    
                } else {
                    arvore.inserir(valor);
                    
                }
                break;
            case 'd':
                valor = Integer.parseInt(stringInput("delete: "), 10);
                if (arvore.verificarMembro(valor)) {
                    arvore.delete(valor);
                    System.out.println(valor+" deletado");
                    
                } else {
                    System.out.println(valor + " elemento nao encontrado");
                    
                }
                break;
                
               }
        
        arvore.preOrdem();
        } while ((entrada.charAt(0) != 'e'));
   
        
        
    }
     //Esse método faz tratamento da  excessão acima.
     private static String stringInput(String inputRequest) throws IOException {
        System.out.println(inputRequest);
        return leitor.readLine();
    }

}
