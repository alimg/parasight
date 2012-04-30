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
 * @author Alper
 */
public class ChromosomeVisualizer extends Visualizer{
    Chromosome chromosome;
    public ChromosomeVisualizer(int w,int h,Chromosome chr){
        super(w,h);
        this.chromosome = chr;
    }
    public float getPosXY(int position){
        return (float)getWidth()*(position/chromosome.getLength());
    }
    @Override
    public void draw(){
        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        glColor4f(0, 0, 0, 1);
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
