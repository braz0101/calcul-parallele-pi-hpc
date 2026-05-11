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
public class main {
    public static void main(String args[]){
        final int startLoop=0, endLoop=101, numThreads=4, groupSize=20, minSize=6;
        staticSchedule staticSched=new  staticSchedule(startLoop,endLoop,numThreads);
        staticSchedule.LoopRange range;
        System.out.println("A l'issue du partitionnement de la partition ["+startLoop+","+endLoop+"]"+" en "+numThreads+" partitions, par l'algorithme du static scheduling, nous obtenons les partitions suivantes: ");
        while((range=staticSched.loopGetRange())!=null){
            System.out.println("Start: "+range.start+" , "+"End: "+range.end+"-->"+"["+range.start+","+range.end+"]");
        }
        
        System.out.println("\n***********************************************************\n");
        
        selfSchedule selfSched=new  selfSchedule(startLoop,endLoop,groupSize);
        selfSchedule.LoopRange range1;
        System.out.println("A l'issue du partitionnement de la partition ["+startLoop+","+endLoop+"]"+" en des partitions de taille "+groupSize+" , par l'algorithme du self scheduling, nous obtenons les partitions suivantes: ");
        while((range1=selfSched.loopGetRange())!=null){
            System.out.println("Start: "+range1.start+" , "+"End: "+range1.end+"-->"+"["+range1.start+","+range1.end+"]");
        }  
         System.out.println("\n***********************************************************\n");
        guidedSelfSchedule guidedSelfSched=new  guidedSelfSchedule(startLoop,endLoop,numThreads,minSize);
        guidedSelfSchedule.LoopRange range2;
        System.out.println("A l'issue du partitionnement de la partition ["+startLoop+","+endLoop+"]"+" en des partitions taille variable variable et n'excédant pas "+minSize+" , par l'algorithme du guided self scheduling, nous obtenons les partitions suivantes: ");
        while((range2=guidedSelfSched.loopGetRange())!=null){
            System.out.println("Start: "+range2.start+" , "+"End: "+range2.end+"-->"+"["+range2.start+","+range2.end+"]");
        } 
    }
}
