/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.types.*;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Ahmet
 */
public class ReadDepthVisualizer extends ChromosomeVisualizer{
    private final int BIG = 2000000000;
    public ReadDepthVisualizer(int w,int h,Chromosome chr){
        super(w,h,chr);
    }
    @Override
    public void draw(){
        float h = getHeight()/4.0f;
        float w = getWidth();
        int min=+BIG;
        int max=-BIG;
        
        for(Feature i:chromosome.getFeatures()){
            ReadDepth f=(ReadDepth)i;
            if(min > f.getScore())
                min = f.getScore();
            if(max < f.getScore())
                max = f.getScore();
        }
        glLineWidth(5.0f);
        glColor4f(0, 0, 0, 1);
        glBegin(GL_LINES);
        glVertex2f(0.0f,h);
        glVertex2f(w,h);
        glEnd();
        
        glEnable(GL_TEXTURE_2D);
        font.drawString(20, getHeight()-20, "RD Visualizer",1,1 );
        glDisable(GL_TEXTURE_2D);
       
        for(Feature i:chromosome.getFeatures()){
            ReadDepth f=(ReadDepth)i;
            float ratio = (float)(f.getScore()-min) / (max-min);
            System.out.println(ratio);
            glBegin(GL_QUADS);
            glColor4f(ratio,1-ratio,0,1);
            glVertex2f(getPosX(f.getPosition()),h);
            
            glVertex2f(getPosX(f.getPosition()+f.getLength()), h);
            
            glVertex2f(getPosX(f.getPosition()+f.getLength()), h + 2*h/ratio);
          
            glVertex2f(getPosX(f.getPosition()), h + 2*h/ratio);
         
            glEnd();
                     
        }

    }
}
