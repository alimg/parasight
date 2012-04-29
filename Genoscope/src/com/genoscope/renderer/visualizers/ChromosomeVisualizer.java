/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.types.Chromosome;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author PC
 */
public class ChromosomeVisualizer extends Visualizer{
    Chromosome chromosome;
    public ChromosomeVisualizer(int w,int h,Chromosome chr){
        super(w,h);
        this.chromosome = chr;
    }
    @Override
    public void draw(){
        glLineWidth(5.0f);
        glBegin(GL_LINES);
        glVertex2f(0.0f,getHeight()/2.0f);
        glVertex2f(getWidth(),getHeight()/2.0f);
        glVertex2f(0.0f,0.0f);
        glVertex2f(0.0f,getHeight());
        glVertex2f(getWidth(),getHeight());
        glVertex2f(getWidth(),0);
        glEnd();
        glLineWidth(2.0f);
    }
}