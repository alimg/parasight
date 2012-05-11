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
    
    @Override
    public void updateState()
    {
        if(v1 == null || v2 == null)
            return;
        if((v1.isVisible()==false) || (v2.isVisible()==false))
            setVisible(false);
        else 
            setVisible(true);
            
        if(v1.getCoordinatesChanging() || v2.getCoordinatesChanging())
        {
            setVisible(false);
            return;
        }
        
        if(v1.getCoordinatesUpdated() || v2.getCoordinatesUpdated())
        {
            setVisible(true);
            int x=Math.min(v1.getX(), v2.getX());
            int y=Math.min(v1.getY(), v2.getY());
            int ex=Math.max(v1.getX()+v1.getWidth(), v2.getX()+v2.getWidth());
            int ey=Math.max(v1.getY()+v1.getHeight(), v2.getY()+v2.getHeight());
            
            setPosition(x,y);
            setSize(ex-x,ey-y);
            doneResizing();
            System.out.println("  "+x+" "+ y+" "+ex+" "+ey);
        }
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
            glColor4f(0, 0, 0, .8f);
            glVertex2f(v1x+v1.getPositionX(p.getFirst().getPosition()),v1y-v1.getHeight()/2.f);
            if(v1==v2)
               glVertex2f(v1x+(v1.getPositionX(p.getFirst().getPosition())+
                       v2.getPositionX(p.getSecond().getPosition()))/2.f,v1y);
            glVertex2f(v2x+v2.getPositionX(p.getSecond().getPosition()),v2y-v2.getHeight()/2.f);
            
            
            glVertex2f(v1x+v1.getPositionX(p.getFirst().getPosition()+p.getFirst().getLength()),v1y-v1.getHeight()/2.f);
            if(v1==v2)
               glVertex2f(v1x+(v1.getPositionX(p.getFirst().getPosition()+p.getFirst().getLength())+
                       v2.getPositionX(p.getSecond().getPosition()+p.getSecond().getLength()))/2.f,v1y);
            glVertex2f(v2x+v2.getPositionX(p.getSecond().getPosition()+p.getSecond().getLength()),v2y-v2.getHeight()/2.f);
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
