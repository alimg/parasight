/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;


import com.genoscope.types.PairBlock;
import static org.lwjgl.opengl.GL11.*;

/**
 * Visualizer for pairing data of two chromosomes.
 * @author alim
 */
public class PairingVisualier extends InterChromosomeV{
    
    private ChromosomeVisualizer v1;
    private ChromosomeVisualizer v2;
    
    
    public PairingVisualier(int w, int h, ChromosomeVisualizer v1, ChromosomeVisualizer v2, PairBlock pairing) {
        super(1, 1);
        //setSize(1, 1);
        this.v1=v1;
        this.v2=v2;
    }
    
    @Override
    public void updateState()
    {
    
        if(v1.getCoordinatesChanging() || v2.getCoordinatesChanging())
        {
            setVisible(false);
        }
        
        if(v1.getCoordinatesUpdated() || v2.getCoordinatesUpdated())
        {
            setVisible(true);
            v1.setCoordinatesUpdateHandled();
            v2.setCoordinatesUpdateHandled();
            int x=Math.min(v1.getX(), v2.getX());
            int y=Math.min(v1.getY(), v2.getY());
            int ex=Math.max(v1.getX()+v1.getWidth(), v2.getX()+v2.getWidth());
            int ey=Math.min(v1.getY()+v1.getHeight(), v2.getY()+v2.getHeight());
            
            setPosition(x,y);
            setSize(ex-x,ey-y);
            System.out.println("  "+x+" "+ y+" "+ex+" "+ey);
        }
    }

    @Override
    public void draw() {
        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        System.out.println("buffer update");
        glLoadIdentity();
        glBegin(GL_LINES);
        glColor4f(0, 1, 0, 1);
        glVertex2f(getWidth(), getHeight() );
        glColor4f(1, 0, 0, 1);
        glVertex2f(0, 0);
        glEnd();
        
        
    }
    
    

}
