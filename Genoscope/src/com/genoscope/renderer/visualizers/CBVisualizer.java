/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.types.Chromosome;
import com.genoscope.types.Cytoband;
import com.genoscope.types.Feature;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Alper
 */
public class CBVisualizer extends ChromosomeVisualizer{
    public CBVisualizer(int w,int h,Chromosome chr){
        super(w,h,chr);
    }
    @Override
    public void draw(){
        float h = getHeight()/4.0f;
        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        glColor4f(0, 0, 0, 1);
        glLineWidth(5.0f);
        glBegin(GL_LINES);
        glVertex2f(0.0f,getHeight()/4.0f);
        glVertex2f(getWidth(),getHeight()/4.0f);
        glVertex2f(0.0f,3*getHeight()/4.0f);
        glVertex2f(getWidth(),3*getHeight()/4.0f);
        glVertex2f(0.0f,getHeight()/4.0f);
        glVertex2f(0.0f,3*getHeight()/4.0f);
        glVertex2f(getWidth(),getHeight()/4.0f);
        glVertex2f(getWidth(),3*getHeight()/4.0f);
        glEnd();
        glBegin(GL_QUADS);
        boolean black = true;
        for(Feature i:chromosome.getFeatures()){
            if(black)
                glColor4f(0,0,0,1);
            else
                glColor4f(0.5f,0.5f,0.5f,1);
            glVertex2f(getPosXY(i.getPosition()),h);
            glVertex2f(getPosXY(i.getPosition()+((Cytoband)i).getLength()),h);
            glVertex2f(getPosXY(i.getPosition()+((Cytoband)i).getLength()),3*h);
            glVertex2f(getPosXY(i.getPosition()),3*h);
            black = !black;
        }
        glLineWidth(2.0f);
    }
}
