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
public class BEDVisualizer extends SingleChromosomeVisualizer{
    
    float vHeight=30;
    
    public BEDVisualizer(int w,int h,Chromosome chr){
        super(w,h,chr);
        resize();
    }
    
    @Override
    public int getRowCount()
    {
        if(cytoband==null)
            return 2;
        return 3;
    }
    
    @Override
    public float getRowHeight(int rowNumber) {
        if(cytoband==null)
        {
            if(rowNumber==1)
                return vHeight;
            return 20;
        }
        if(rowNumber==2)
            return vHeight;
        else if(rowNumber==1)
            return 10;
        return 20;
    }

    @Override
    public String getRowLabel(int rowNumber) {
        if(rowNumber==0)
            return (chromosome.getName() + " (" + chromosome.getStart() + ":" + chromosome.getEnd() + ")");
        return "";
    }
    
    
    
    @Override
    public void drawRow(int rowNumber) {
        if(rowNumber == 0 )
            return;
        if(rowNumber==1 && cytoband!=null)
        {
            drawCytoband(getRowHeight(rowNumber));
            return;
        }
        
        float h = vHeight/3;
        float w = getWidth();
        glDisable(GL_TEXTURE_2D);
        
        glColor4f(0, 0, 0, 1);
        glLineWidth(5.0f);
        glBegin(GL_LINE_STRIP);
        glVertex2f(0.0f,0);
        glVertex2f(0.0f,vHeight);
        glVertex2f(w,vHeight);
        glVertex2f(w,0);
        glEnd();
        
        glLineWidth(1.0f);
        boolean black = true;
        boolean up = true;
        synchronized(chromosome)
        {
            for(Feature i:chromosome.getFeatures()){
                glBegin(GL_POLYGON);
                float x1 = getPositionX(i.getPosition());
                float y1 = 4;
                float x2 = getPositionX(i.getPosition()+((NormalFeature)i).getLength());
                float y2 = y1+h;
                if(-1<= x1 - x2 && x1 - x2 <= 1) 
                    x2++;
                if(!up){
                    y1+=h;
                    y2+=h;
                }
                up = !up;
              
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
                        glVertex2f(x2+((x2-x1) < 10 ? (x2-x1) : 10),(y1+y2)/2);
                        glVertex2f(x2,y1);
                    }
                    else{
                        glVertex2f(x1,y2);
                        glVertex2f(x1-((x2-x1) < 10 ? (x2-x1) : 10),(y1+y2)/2);
                        glVertex2f(x1,y1);
                    }
                glEnd();
                black = !black;
            }
        }
    }
}
