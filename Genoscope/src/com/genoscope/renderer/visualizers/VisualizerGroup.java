/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import java.util.Vector;

/**
 *
 * @author alim
 */
public class VisualizerGroup extends Visualizer
{
    
    Vector <Visualizer> subs;

    public VisualizerGroup(int w, int h) {
        super(w, h);
        
    }

    @Override
    public void setSize(int w, int h) {
        super.setSize(w, h);
        
    }

    @Override
    public void draw() {
        //super.draw();
        
    }
    
    
    public void addVisulizer(Visualizer v)
    {
        
    
    }
    
    public Vector<Visualizer> getVisualizers()
    {
        return null;
    }
    public Vector<Visualizer> release()
    {
        return null;
    }
    
    
}