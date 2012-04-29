/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import com.genoscope.reader.Reader;
import com.genoscope.renderer.visualizers.Visualizer;
import com.genoscope.types.Chromosome;
import com.genoscope.types.Pair;
import java.util.Vector;

public class State {
    static int ERROR = -1;
    private Vector<Chromosome> chromosomeList;
    private Vector<Pair> pairList;
    private Vector<Visualizer> visualizerList;
    public State(){
        chromosomeList = new <Chromosome>Vector();
        pairList = new <Pair>Vector();
        visualizerList = new <Visualizer>Vector();
    }
    public int importData(String fileName){
        int returnID = Reader.readFile(fileName,this);
        return returnID;
    }
    public void addChromosome(Chromosome chr){
        chromosomeList.add(chr);
    }
    public void addPair(Pair pair){
        pairList.add(pair);
    }
}
