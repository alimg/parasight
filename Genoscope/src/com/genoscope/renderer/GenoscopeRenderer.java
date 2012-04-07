package com.genoscope.renderer;

import com.genoscope.renderer.mouseactions.MouseActionHandler;
import com.genoscope.renderer.GLHandler;
import com.genoscope.renderer.visualizers.Visualizer;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;


/**
 * This class handles OpenGL and input events for Visualizer classes.
 * @author alim
 */
public class GenoscopeRenderer {
    public final static int EDIT_MODE=1;
    public final static int NAVIGATE_MODE=0;

    private int horizonalGap=15;//pixels
    private int verticalGap=15;//pixels
    private ArrayList<Visualizer> clients= new ArrayList<Visualizer>();
    private int mMode=0;
    
    private int MPX=0,MPY=0;
    
    private MouseActionHandler mouseHandlers[]=new MouseActionHandler[5];
    public GenoscopeRenderer()
    {
        
    }
    /**
     * Arranges position of all clients 
     */
    public void resetLayout()
    {
        int x=horizonalGap;
        int y=verticalGap;
        int lineMax=0;
        for(Visualizer v: clients)
        {
            if(x+v.getWidht()>GLHandler.getWidth())
            {
                y+=lineMax+verticalGap;
                lineMax=0;
                x=horizonalGap;
            }
            v.setPosition(x,y);
            x+=v.getWidht()+horizonalGap;
            if(lineMax<v.getHeight())
                lineMax=v.getHeight();
                
        }
    }
    
    public void addVisualizer(Visualizer v)
    {
        clients.add(v);
    }
    
    void mouseDown(int i)
    {
        System.out.println("down "+i);
    }
    
    void mouseUp(int i)
    {
        System.out.println("up "+i);
        
    }
    void mouseMove(int x, int y, int buttons) {
        MPX=x;
        MPY=y;
        
        
        for(Visualizer v: clients)
            v.setHighlight(intersect(v,MPX,MPY));
        GLHandler.requestPaint();
        //System.out.println("x,y "+MPX+" "+MPY);
    }

    

    private boolean updated=false;
    void draw() {
       
        if(!updated)
        {
            resetLayout();
            updated=true;
        }
        for(Visualizer v: clients)
        {
            if(! v.isBufferUpToDate())
            {
                glPushMatrix();
                v.initBufferMode();
                v.updateBuffer();
                glPopMatrix();
            }
        }
        
        GLHandler.setup();
        glClear( GL_COLOR_BUFFER_BIT );
        for(Visualizer v: clients)
        {
            if(v.isBufferUpToDate())
            {
                //translate then draw;
                glPushMatrix();
                glTranslatef(v.getX(), v.getY(), 0);
                v.drawBuffered();
                if(v.isHiglighted())
                {//<editor-fold defaultstate="collapsed" desc="draw some rectangle around">
                    GL20.glUseProgram(0);
                    glColor4f(0,0,0,1);
                    glDisable(GL_TEXTURE_2D);
                    glBegin(GL_LINE_LOOP);
                    glVertex2f(0, 0);
                    glVertex2f(0,v.getHeight());
                    glVertex2f(v.getWidht(),v.getHeight());
                    glVertex2f(v.getWidht(), 0);
                    glEnd();
                    //</editor-fold>
                }
                glPopMatrix();
                
            }
        }
    }

    private boolean intersect(Visualizer v, int x, int y) {
        if( x>v.getX() && x<v.getX()+v.getWidht() &&
                y>v.getY() && y<v.getY()+v.getHeight() )
            return true;
        return false;
    }


    
}
