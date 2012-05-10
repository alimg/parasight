 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.types.Chromosome;
import com.genoscope.types.Cytoband;
import com.genoscope.types.Feature;
import com.genoscope.types.NormalFeature;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Alper
 */
public class BEDVisualizer extends ChromosomeVisualizer{
    
    public BEDVisualizer(int w,int h,Chromosome chr){
        super(w,h,chr);
    }
    @Override
    public void draw(){
        float h = getHeight()/4.0f;
        float w = getWidth();
        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        
        glColor4f(0, 0, 0, 1);
        glLineWidth(5.0f);
        glBegin(GL_LINES);
        glVertex2f(0.0f,h);
        glVertex2f(0.0f,3*h);
        glVertex2f(0.0f,h);
        glVertex2f(w,h);
        glVertex2f(w,h);
        glVertex2f(w,3*h);
        glEnd();
        boolean black = true;
        boolean up = true;
        synchronized(chromosome)
        {
            for(Feature i:chromosome.getFeatures()){
                glBegin(GL_POLYGON);
                float x1 = getPosX(i.getPosition());
                float y1 = h+5;
                float x2 = getPosX(i.getPosition()+((NormalFeature)i).getLength());
                float y2 = 2*h-5;
                if(up){
                    y1+=10;
                    y2+=10;
                }
                up = !up;
                if(((NormalFeature)i).getStrand())
                    x2-=10;
                else
                    x1+=10;
                if(black)
                    glColor4f(0,0,0,1);
                else
                    glColor4f(0.8f,0.8f,0.8f,1);
                glVertex2f(x1,y1);
                glVertex2f(x2,y1);
                glVertex2f(x2,y2);
                glVertex2f(x1,y2);
                glEnd();
                glBegin(GL_TRIANGLES);
                if(((NormalFeature)i).getStrand()){
                    glVertex2f(x2,y2);
                    glVertex2f(x2+10,(y1+y2)/2);
                    glVertex2f(x2,y1);
                }
                else{
                    glVertex2f(x1,y2);
                    glVertex2f(x1-10,(y1+y2)/2);
                    glVertex2f(x1,y1);
                }
                glEnd();
                black = !black;
            }
        }
        glColor4f(0, 0, 1, 1);
        glEnable(GL_TEXTURE_2D);
        font.drawString(20, getHeight()-20, "Deneme  e\n234 \\n\n\\\nn",1,1 );
        glDisable(GL_TEXTURE_2D);
        
        glLineWidth(2.0f);
    }
}
