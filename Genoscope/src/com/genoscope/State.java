/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import com.genoscope.reader.Reader;
import com.genoscope.renderer.GenoscopeRenderer;
import com.genoscope.renderer.visualizers.ChromosomeVisualizer;
import com.genoscope.renderer.visualizers.Visualizer;
import com.genoscope.types.Chromosome;
import com.genoscope.types.Pair;
import java.util.Vector;

public class State {
    static int ERROR = -1;
    private Vector<Chromosome> chromosomeList;
    private Vector<Pair> pairList;
    private Vector<Visualizer> visualizerList;
    private GenoscopeRenderer renderer;
    public State(){
        chromosomeList = new <Chromosome>Vector();
        pairList = new <Pair>Vector();
        visualizerList = new <Visualizer>Vector();
    }
    public void setRenderer(GenoscopeRenderer renderer){
        this.renderer = renderer;
    }
    public int importData(String fileName){
        int returnID = Reader.readFile(fileName,this);
        return returnID;
    }
    public boolean checkChromosome(String path){
        for(Chromosome i:chromosomeList)
            if(i.getSourceFile().equals(path))
                return true;
        return false;
    }
        
    public void addChromosome(Chromosome chr){
        chromosomeList.add(chr);
        renderer.addVisualizer(new ChromosomeVisualizer(500,100,chr));
    }
    public void addPair(Pair pair){
        pairList.add(pair);
    }
}
