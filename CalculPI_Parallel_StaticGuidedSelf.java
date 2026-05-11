package parallel;

import schedule.guidedSelfSchedule;

/**
 * TP1 - Calcul parallèle de PI avec l'algorithme du Guided Self Scheduling.
 *
 * Principe : au début, les blocs alloués sont grands (N/P itérations).
 * Progressivement, les blocs deviennent de plus en plus petits (jusqu'à
 * minSize), ce qui réduit la synchronisation au départ et équilibre la
 * charge en fin d'exécution.
 *
 * C'est un compromis entre Static Scheduling (peu de synchro) et
 * Self Scheduling (bon équilibrage).
 *
 * Pour le TP : tester avec différents nombres de cœurs (2, 4, 6, 8).
 */
public class CalculPI_Parallel_StaticGuidedSelf {

    final static int N         = 2_000_000_000;
    final static int numThreads = 4;     // ← changer pour tester : 2, 4, 6, 8
    final static int minSize    = 1;     // taille minimale d'un bloc (par défaut 1)

    // Création du scheduler guidé :
    //   - démarre à 1, finit à N
    //   - blocs décroissants : N/P au début, minSize au minimum
    static guidedSelfSchedule guidedSched =
            new guidedSelfSchedule(1, N, numThreads, minSize);

    // ------------------------------------------------------------------
    //  Tâche : chaque thread demande un bloc (de taille décroissante),
    //          calcule pi partiel, recommence jusqu'à épuisement.
    // ------------------------------------------------------------------
    static class Task implements Runnable {
        double pi = 0.0;

        @Override
        public void run() {
            guidedSelfSchedule.LoopRange range;
            // Tant qu'il reste des blocs, en prendre un
            while ((range = guidedSched.loopGetRange()) != null) {
                System.out.println(Thread.currentThread().getName()
                        + " Start: " + range.start
                        + " , End: " + range.end
                        + " --> [" + range.start + "," + range.end + "]"
                        + " (taille=" + (range.end - range.start + 1) + ")");
                // Calcul de la somme de Leibniz sur ce bloc
                for (int i = range.start; i <= range.end; i++) {
                    pi = pi + Math.pow(-1, i % 2) * 1.0 / (2.0 * ((double) i) + 1.0);
                }
            }
        }
    }

    // ------------------------------------------------------------------
    //  Thread principal : fork → join → réduction
    // ------------------------------------------------------------------
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        double pi = 1.0;

        // Créer les tâches et les threads
        Task[]   task_array   = new Task[numThreads];
        Thread[] thread_array = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            task_array[i]   = new Task();
            thread_array[i] = new Thread(task_array[i]);
        }

        // ---- FORK : lancer tous les threads ----
        for (int i = 0; i < numThreads; i++) {
            thread_array[i].start();
        }

        // ---- JOIN : attendre que tous aient terminé ----
        for (int i = 0; i < numThreads; i++) {
            try {
                thread_array[i].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        // ---- RÉDUCTION : additionner les résultats partiels ----
        for (int i = 0; i < numThreads; i++) {
            pi = pi + task_array[i].pi;
        }

        long stopTime    = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        System.out.println("==================================================");
        System.out.println("PI= " + 4 * pi
                + " | N=" + N
                + " | Threads=" + numThreads
                + " | minSize=" + minSize
                + " | Temps=" + elapsedTime / 1000 + " s");
        System.out.println("==================================================");
    }
}