/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parallel;

import java.util.logging.Level;
import java.util.logging.Logger;
import static parallel.CalculPI_Parallel.N;

/**
 *
 * @author sicap
 */
public class CalculPI_Parallel {
    /// i)Identifier la boucle for à paralléliser, ainsi que les indexes de début et de fin
    ////ii)Déclarer une classe dénommée Task qui implémente une interface Runnable et  Copier la boucle for à paralléliser et coller dans public void run(){}.
    ////iii)Créer deux taches Task1 et Task2, qui sont des instances de la classe Task et qui, au moment de l’instanciation, prennent en paramètre les indexes de début et de fin issus de la partition des itérations de la boucle for à paralléliser.
    ////iv) Créer 2 threads Th1 et Th2, qui sont des instances de la classe Thread et qui, au moment de l’instanciation, prennent en paramètre Task1 et Task2.
    ////v) Lancer les threads1 Th1 et Th2 (Fork), puis bloquer (Join) le thread main le temps que Th1 et Th2 terminent leur exécution.

    
    final static int N = 2_000_000_000;
    public static void main(String args[]) { //thread principal
        long startTime = System.currentTimeMillis(); //Lancement du chrono        
        double pi = 1.0;
//////////////////////////////////////////
        //boucle for à paralléliser ; index_de_debut= 1 ; index_de_fin= N
        Task task1= new Task(1,N/2);
        Task task2=new Task((N/2)+1, N);
        
        Thread th1= new Thread(task1); //th1 est créé par Thread principal
        Thread th2= new Thread(task2);//th2 est créé par Thread principal
        
        th1.start();//th1 est lancé par le Thread principal
        th2.start();//th2 est lancé par le Thread principal
        try {
            th1.join(); //Thread main execute cette instruction 
        } catch (InterruptedException ex) {}
        try {
            th2.join();
        } catch (InterruptedException ex) {}
        
        
        pi=pi+ task1.pi+task2.pi; //thread effectue l'opérationde réduction
//////////////////////////////////////

        long stopTime = System.currentTimeMillis();//Arret du chrono
        long elapsedTime = stopTime - startTime;
        System.out.println("PI= " + 4 * pi + " avec N=" + N + ""
                + " Temps d'exécution " + elapsedTime / 1000 + " s"); //Affichage du résultat et de la performance
    }
    static class Task implements  Runnable{
    double pi = 0.0;
    int index_debut, index_fin;
    Task(int index_debut, int index_fin){
        this.index_debut=index_debut;
        this.index_fin=index_fin;
    }
    @Override
    public void run() {//les threads (th1 et th2) excéutent cette méthode
        for (int i = index_debut; i <= index_fin; i++) { //boucle for à paralléliser ; index_de_debut= 1 ; index_de_fin= N
            pi = pi + Math.pow(-1, i % 2) * 1.0 / (2.0 * ((double) i) + 1.0);
        }
    }
    
}
}


