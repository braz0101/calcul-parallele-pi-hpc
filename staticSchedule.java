/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

/**
 *
 * @author dell
 */

public class staticSchedule {
    public int startLoop, curLoop, endLoop, numThreads;
    public static class LoopRange {
        public int start, end;
    }
    
    ///for(int i=1;i<=100;i++) -->[1,100] à repartir entre deux threads
    //th1 aura [1,50] et th2 aura[51,100]
    // {1,2,3,4,5,6,7,8,...,49,50,....,90,91,92,93,94,95,96,97,98,99,100,101}
    public int step, modulo;
    public staticSchedule(int startLoop, int endLoop, int numThreads) {
        int numIterations=endLoop - startLoop + 1;
        curLoop = startLoop - 1;
        this.startLoop = startLoop;
        this.endLoop = endLoop;
        this.numThreads = numThreads;
        step = numIterations / numThreads;
        modulo = numIterations % numThreads;
    }
       
    public  synchronized LoopRange loopGetRange() {
        if (curLoop >= endLoop) {
            return null;
        }
        LoopRange range = new LoopRange();
        range.start = curLoop + 1;
        curLoop = curLoop + step;
        if (modulo > 0) {
            curLoop++;
            modulo--;
        }
        range.end = (curLoop < endLoop) ? curLoop : endLoop;
        return range;
    }
}
