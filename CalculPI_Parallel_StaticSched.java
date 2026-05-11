/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parallel;

import java.util.logging.Level;
import java.util.logging.Logger;
import static parallel.CalculPI_Parallel_StaticSched.N;
import schedule.staticSchedule;

/**
 *
 * @author sicap
 */
public class CalculPI_Parallel_StaticSched {

    final static int N = 2_000_000_000;
    final static int numThreads = 2;
    static staticSchedule staticSched = new staticSchedule(1, N, numThreads);

    public static void main(String args[]) { //thread principal
        long startTime = System.currentTimeMillis(); //Lancement du chrono        
        double pi = 1.0;

        Task[] task_array = new Task[numThreads];
        Thread[] thread_array = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            task_array[i] = new Task();
        }
        for (int i = 0; i < numThreads; i++) {
            thread_array[i] = new Thread(task_array[i]);
        }

        for (int i = 0; i < numThreads; i++) {
            thread_array[i].start();
        }
        for (int i = 0; i < numThreads; i++) try {
            thread_array[i].join();
        } catch (InterruptedException ex) {

        }

        for (int i = 0; i < numThreads; i++) {
            pi = pi + task_array[i].pi;
        }


        long stopTime = System.currentTimeMillis();//Arret du chrono
        long elapsedTime = stopTime - startTime;
        System.out.println("PI= " + 4 * pi + " avec N=" + N + ""
                + " Temps d'exécution " + elapsedTime / 1000 + " s"); //Affichage du résultat et de la performance
    }

    static class Task implements Runnable {
        double pi = 0.0;

        @Override
        public void run() {//les threads (th1 et th2) excéutent cette méthode
            staticSchedule.LoopRange range;
            while ((range = staticSched.loopGetRange()) != null) {
                System.out.println(Thread.currentThread().getName() + "Start: " + range.start + " , " + "End: " + range.end + "-->" + "[" + range.start + "," + range.end + "]");
                for (int i = range.start; i <= range.end; i++) { //boucle for à paralléliser ; index_de_debut= 1 ; index_de_fin= N
                    pi = pi + Math.pow(-1, i % 2) * 1.0 / (2.0 * ((double) i) + 1.0);
                }
            }
        }

    }
}
