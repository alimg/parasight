/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.GenoscopeApp;
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
        
        int cenDir = 1;
        for(Feature i:chromosome.getFeatures()){
            glBegin(GL_POLYGON);
            float x1 = getPositionX(i.getPosition());
            float y1 = h;
            float x2 = getPositionX(i.getPosition()+((Cytoband)i).getLength());
            float y2 = 3*h;
            
            int gieStain=((Cytoband)i).getGieStain();
            switch(gieStain)
            {
            case Cytoband.GPOS50:
                glColor4f(0.90f,0.9f,0.9f,1);
                break;
            case Cytoband.GPOS25:
                glColor4f(0.7f,0.7f,0.7f,1);
                break;
            case Cytoband.GPOS75:
                glColor4f(0.5f,0.5f,0.5f,1);
                break;
            default:
                glColor4f(0,0,0,1);
                break;
            }
            if(gieStain==Cytoband.ACEN && cenDir==1){
                glColor4f(0.8f,0.1f,0.1f,1);
                glVertex2f(x1,y1);
                glVertex2f(x2,(y1+y2)/2);
                glVertex2f(x1,y2);
                cenDir=0;
            }
            else if(gieStain==Cytoband.ACEN && cenDir==0){
                glColor4f(0.8f,0.1f,0.1f,1);
                glVertex2f(x1,(y1+y2)/2);
                glVertex2f(x2,y1);
                glVertex2f(x2,y2);
            }
            else {   
                glVertex2f(x1,y1);
                glVertex2f(x2,y1);
                glVertex2f(x2,y2);
                glVertex2f(x1,y2);
            }
            glEnd();
        }
        glColor4f(0, 0, 1, 1);
        glEnable(GL_TEXTURE_2D);
        font.drawString(20, 5, (chromosome.getName() + " (" + chromosome.getStart() + ":" + chromosome.getEnd() + ")"),1,1 );
        glDisable(GL_TEXTURE_2D);
        glLineWidth(2.0f);
        System.out.println("draw");
    }
}
