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
        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
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
        glColor4f(0, 0, 0, 0.8f);
        glBegin(GL_LINES);
        glVertex2f(0.0f,h);
        glVertex2f(w,h);
        glEnd();
        
        glEnable(GL_TEXTURE_2D);
        font.drawString(20, getHeight()-15, (chromosome.getName() + " (" + chromosome.getStart() + ":" + chromosome.getEnd() + ")") + chromosome.getLength(),1,1 );
        glDisable(GL_TEXTURE_2D);
       
        for(Feature i:chromosome.getFeatures()){
            ReadDepth f=(ReadDepth)i;
            float ratio = (float)(f.getScore()-min) / (max-min);
            
            glBegin(GL_QUADS);
            if(ratio <= 0.5)
                glColor4f(ratio*2,1,0,1);
            else
                glColor4f(1,(1-ratio)*2,0,1);
            
            glVertex2f(getPositionX(f.getPosition()),h);
            
            glVertex2f(getPositionX(f.getPosition()+f.getLength()), h);
            
            glVertex2f(getPositionX(f.getPosition()+f.getLength()), h + 2*h*ratio);
          
            glVertex2f(getPositionX(f.getPosition()), h + 2*h*ratio);
         
            glEnd();
                     
        }
       
    }
}
