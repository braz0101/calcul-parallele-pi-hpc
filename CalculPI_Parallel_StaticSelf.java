package parallel;

import schedule.selfSchedule;

/**
 * TP1 - Calcul parallèle de PI avec l'algorithme du Self Scheduling.
 *
 * Principe : chaque thread saisit un bloc de groupSize itérations,
 * l'exécute, puis en prend un autre, jusqu'à épuisement des itérations.
 *
 * Plus groupSize est petit  → meilleur équilibrage, plus de synchronisations.
 * Plus groupSize est grand  → moins de synchronisations, risque de déséquilibre.
 *
 * Pour le TP : tester avec 4 cœurs et les groupSize suivants :
 *   2_000_000_000 / 500_000_000 / 10_000_000 / 1_000_000
 */
public class CalculPI_Parallel_StaticSelf {

    final static int N         = 2_000_000_000;
    final static int numThreads = 4;              // ← changer pour tester
    final static int groupSize  = 1_000_000;      // ← changer pour tester

    // Création du scheduler avec le groupSize choisi
    static selfSchedule selfSched = new selfSchedule(1, N, groupSize);

    // ------------------------------------------------------------------
    //  Tâche : chaque thread appelle loopGetRange() pour obtenir
    //          un bloc d'itérations, calcule pi partiel, recommence.
    // ------------------------------------------------------------------
    static class Task implements Runnable {
        double pi = 0.0;

        @Override
        public void run() {
            selfSchedule.LoopRange range;
            // Tant qu'il reste des blocs disponibles, en prendre un
            while ((range = selfSched.loopGetRange()) != null) {
                System.out.println(Thread.currentThread().getName()
                        + " Start: " + range.start
                        + " , End: " + range.end
                        + " --> [" + range.start + "," + range.end + "]");
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
                + " | groupSize=" + groupSize
                + " | Temps=" + elapsedTime / 1000 + " s");
        System.out.println("==================================================");
    }
}