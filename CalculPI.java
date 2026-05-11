
package sequentiel;

public class CalculPI {
    final static int N = 2_000_000_000;
    public static void main(String args[]) { //Cette methode est exécutée par lme Thread Principal
        long startTime = System.currentTimeMillis(); //Lancement du chrono        
        double pi = 1.0;
//////////////////////////////////////////
        for (int i = 1; i <= N; i++) {
            pi = pi + Math.pow(-1, i % 2) * 1.0 / (2.0 * ((double) i) + 1.0);
        }
//////////////////////////////////////
        long stopTime = System.currentTimeMillis();//Arret du chrono
        long elapsedTime = stopTime - startTime;
        System.out.println("PI= " + 4 * pi + " avec N=" + N + ""
                + " Temps d'exécution " + elapsedTime / 1000 + " s"); //Affichage du résultat et de la performance
    }
}
