/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import com.genoscope.renderer.visualizers.Visualizer;
import com.genoscope.types.Chromosome;
import com.genoscope.types.Pair;
import java.util.Vector;

public class State {
    private Vector<Chromosome> chromosomeList;
    private Vector<Pair> pairList;
    private Vector<Visualizer> visualizerList;
    public State(){
        chromosomeList = new Vector<Chromosome>();
        pairList = new Vector<Pair>();
        
    }
    public int importData(String fileName){
        return 0;
    }
    public void addChromosome(Chromosome chr){
        chromosomeList.add(chr);
    }
    public void addPair(Pair pair){
        pairList.add(pair);
    }
}
