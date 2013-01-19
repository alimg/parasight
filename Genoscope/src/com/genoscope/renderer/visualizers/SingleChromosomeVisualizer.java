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
 * @author alim
 */
public abstract class SingleChromosomeVisualizer extends ChromosomeVisualizer {
    
    float paddingTop=1;
    float paddingLeft=1;
    float paddingBottom=0;
    float paddingRight=1;
    
    int startPos;//position in a chromosome;
    int endPos;
    Chromosome cytoband;
    
    
    
    public SingleChromosomeVisualizer(int w, int h,Chromosome chr) {
        super(w, h,chr);
    }
    
    public int getTotalRowHeight()
    {
        int r=0;
        for(int i=0;i<getRowCount();i++)
            r+=getRowHeight(i);
        return r;
    }
    public void resize()
    {
        setAbsoluteHeight((int)(paddingTop+getTotalRowHeight()));
    }
    
    public void setCytoband(Chromosome chr){
        cytoband=chr;
        resize();
    }

    public void drawCytoband(float height)
    {
        glDisable(GL_TEXTURE_2D);
        glColor4f(0, 0, 0, 1);
        glLineWidth(1.0f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(paddingLeft+0, 0);
        glVertex2f(paddingLeft+0, height);
        glVertex2f(getWidth(), height);
        glVertex2f(getWidth(), 0);
        glEnd();
        boolean black = true;
        int cenDir= 1;
        for(Feature i:cytoband.getFeatures()){
            glBegin(GL_POLYGON);
            float x1 = getPositionX(i.getPosition());
            float y1 = 0;
            float x2 = getPositionX(i.getPosition()+((Cytoband)i).getLength());
            float y2 = height;
            
            int gieStain=((Cytoband)i).getGieStain();
            switch(gieStain)
            {
            case Cytoband.GPOS25:
                glColor4f(0.7f,0.7f,0.7f,1);
                break;
            case Cytoband.GPOS50:
                glColor4f(0.5f,0.5f,0.5f,1);
                break;
            case Cytoband.GPOS75:
                glColor4f(0.3f,0.3f,0.3f,1);
                break;
            case Cytoband.GNEG:
                glColor4f(0.9f,0.9f,0.9f,1);
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
            black = !black;
        }
        
    }
    
    /**
     * Subclasses should implement this for drawing.
     * 
     * @param rowNumber 
     */
    abstract public void drawRow(int rowNumber);
    
    abstract public int getRowCount();
    
    abstract public float getRowHeight(int rowNumber);
    
    abstract public String getRowLabel(int rowNumber);
    
    
    @Override
    final public void draw(){
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        
        glTranslatef(0, paddingTop , 0);
        
        for(int i=0;i<getRowCount();i++){
            glPushMatrix();
            glEnable(GL_TEXTURE_2D);
            glColor4f(0, 0, 0, 1);
            font.drawString(0, 0, getRowLabel(i), 1,1);
            glDisable(GL_TEXTURE_2D);
            drawRow(i);
            glPopMatrix();
            glTranslatef(0, getRowHeight(i) , 0);
        }
    }
    
    
    
}
