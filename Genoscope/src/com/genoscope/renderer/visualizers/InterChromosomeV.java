/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

/**
 *
 * @author alim
 */
public abstract class InterChromosomeV extends Visualizer{

    public InterChromosomeV(int w, int h) {
        super(w, h);
    }
    
    public abstract void updateState();

    @Override
    public boolean hasChromosome() {
        return false;
    }
    
    
}
