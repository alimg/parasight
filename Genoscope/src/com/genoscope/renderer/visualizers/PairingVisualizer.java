/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;


import com.genoscope.types.Pair;
import com.genoscope.types.PairBlock;
import static org.lwjgl.opengl.GL11.*;

/**
 * Visualizer for pairing data of two chromosomes.
 * @author alim
 */
public class PairingVisualizer extends InterChromosomeV{
    
    private ChromosomeVisualizer v1;
    private ChromosomeVisualizer v2;
    private PairBlock pairs;
    private boolean pairsVisible;

    public PairBlock getPairs() {
        return pairs;
    }

    public PairingVisualizer(int w, int h, ChromosomeVisualizer v1, ChromosomeVisualizer v2, PairBlock pairing) {
        super(50, 50);
        //setSize(1, 1);
        this.v1=v1;
        this.v2=v2;
        this.pairs=pairing;
    }

    public boolean arePairsVisible() {
        return pairsVisible;
    }
    
    @Override 
    public void setVisible(boolean x){
        super.setVisible(x);
        if(x){
            v1.setVisible(true);
            v2.setVisible(true);
        }
    }
    @Override
    public void updateState()
    {
        if(v1 == null || v2 == null)
            return;
        if((v1.isVisible()==false) || (v2.isVisible()==false))
        {
            
            pairsVisible = false;
            return;
        }
        //else
            //pairsVisible = true;
        //else 
        //    setVisible(true);
            
        if(v1.getCoordinatesChanging() || v2.getCoordinatesChanging())
        {
            pairsVisible = false;
            return;
        }
        
        if(v1.getCoordinatesUpdated() || v2.getCoordinatesUpdated())
        {
            int x=Math.min(v1.getX(), v2.getX());
            int y=Math.min(v1.getY(), v2.getY());
            int ex=Math.max(v1.getX()+v1.getWidth(), v2.getX()+v2.getWidth());
            int ey=Math.max(v1.getY()+v1.getHeight(), v2.getY()+v2.getHeight());
            
            if( (ex-x<2024) && (ey-y<2024))
            {
                pairsVisible = true;
                setPosition(x,y);
                setSize(ex-x,ey-y);
                doneResizing();
            }
            else 
                pairsVisible=false;
            //System.out.println("  "+x+" "+ y+" "+ex+" "+ey);
        }
    }

    @Override
    public boolean isVisible(){
        if(!super.isVisible())
            return false;
        else if(!pairsVisible)
            return false;
        return true;
    }
    @Override
    public void draw() {
        if(v1 == null || v2 == null)
            return;
        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        
        float v1x=v1.getSnapX()-getSnapX();
        float v1y=getHeight()-v1.getSnapY()+getY();
        float v2x=v2.getSnapX()-getX();
        float v2y=getHeight()-v2.getSnapY()+getY();
        
        glLoadIdentity();
        glLineWidth(2);
        
        glBegin(GL_LINES);
        for(Pair p:pairs.getPairings())
        {   
            if(v1 == v2)
                glColor4f(0.8f, 0.2f, 0.2f, .8f);
            else
                glColor4f(0.2f, 0.2f, 0.8f, .8f);
            glVertex2f(v1x+v1.getPositionX(p.getFirst().getPosition()),v1y-v1.getHeight()*3/4.f+15);
            if(v1==v2){
               glVertex2f(v1x+(v1.getPositionX(p.getFirst().getPosition())+
                       v2.getPositionX(p.getSecond().getPosition()))/2.f,v1y);
               glVertex2f(v1x+(v1.getPositionX(p.getFirst().getPosition())+
                       v2.getPositionX(p.getSecond().getPosition()))/2.f,v1y);
            }
            glVertex2f(v2x+v2.getPositionX(p.getSecond().getPosition()),v2y-v2.getHeight()*3/4.f+15);
            
            
            glVertex2f(v1x+v1.getPositionX(p.getFirst().getPosition()+p.getFirst().getLength()),v1y-v1.getHeight()*3/4.f+15);
            if(v1==v2){
               glVertex2f(v1x+(v1.getPositionX(p.getFirst().getPosition()+p.getFirst().getLength())+
                       v2.getPositionX(p.getSecond().getPosition()+p.getSecond().getLength()))/2.f,v1y);
               glVertex2f(v1x+(v1.getPositionX(p.getFirst().getPosition()+p.getFirst().getLength())+
                       v2.getPositionX(p.getSecond().getPosition()+p.getSecond().getLength()))/2.f,v1y);
            }
            glVertex2f(v2x+v2.getPositionX(p.getSecond().getPosition()+p.getSecond().getLength()),v2y-v2.getHeight()*3/4.f+15);
        }
        glEnd();
        
    }

    @Override
    public void setCoordinatesUpdateHandled() {
        if(v1 == null || v2 == null)
            return;
        super.setCoordinatesUpdateHandled();
        v1.setCoordinatesUpdateHandled();
        v2.setCoordinatesUpdateHandled(); 
    }
    
    

}
