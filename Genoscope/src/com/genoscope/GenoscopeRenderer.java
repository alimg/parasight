package com.genoscope;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;


/**
 * This class handles OpenGL and input events for Visualizer classes.
 * @author alim
 */
public class GenoscopeRenderer implements MouseListener,MouseMotionListener {
    public final static int EDIT_MODE=1;
    public final static int NAVIGATE_MODE=0;
    private int horizonalGap=15;//pixels
    private int verticalGap=15;//pixels
    private ArrayList<Visualizer> clients= new ArrayList<Visualizer>();
    private int mMode=0;
    
    private int MPX=0,MPY=0;
    
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
            if(x+v.getWidht()>GLRenderer.getWidth())
            {
                y+=lineMax+verticalGap;
                lineMax=0;
                x=horizonalGap;
            }
            v.setPosition(x,y);
            x+=v.getWidht();
            if(lineMax<v.getHeight())
                lineMax=v.getHeight();
                
        }
    }
    
    public void addVisualizer(Visualizer v)
    {
        clients.add(v);
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("x,y "+me.getX()+" "+me.getY());
        switch(mMode)
        {
            case NAVIGATE_MODE:
                
                break;
            case EDIT_MODE:
                break;
            default:
                break;
        }
    }
    @Override
    public void mouseDragged(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("x,y "+me.getX()+" "+me.getY());
    }
    
    @Override
    public void mouseMoved(MouseEvent me) {
        System.out.println("x,y "+me.getX()+" "+me.getY());
        MPX=me.getX();
        MPY=me.getY();
        
        
        for(Visualizer v: clients)
            v.setHighlight(intersect(v,MPX,MPY));
        GLRenderer.requestPaint();
        //System.out.println("x,y "+MPX+" "+MPY);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    

    private boolean updated=false;
    void draw() {
        //System.out.println("repaint");
        if(!updated)
        {
            resetLayout();
            updated=true;
        }
        for(Visualizer v: clients)
        {
            if(! v.isBufferUpToDate())
            {
                GL11.glClear( GL11.GL_COLOR_BUFFER_BIT );
                v.updateBuffer();
            }
        }
        GL11.glClear( GL11.GL_COLOR_BUFFER_BIT );
        for(Visualizer v: clients)
        {
            if(v.isBufferUpToDate())
            {
                //translate then draw;
                GL11.glPushMatrix();
                GL11.glTranslatef(v.getX(), v.getY(), 0);
                v.drawBuffered();
                if(v.isHiglighted())
                {
                    GL20.glUseProgram(0);
                    GL11.glColor4f(0,0,0,1);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glBegin(GL11.GL_LINE_LOOP);
                    GL11.glTranslatef(v.getX(), v.getY(),0);
                    GL11.glVertex2f(0, 0);
                    GL11.glVertex2f(0,v.getHeight());
                    GL11.glVertex2f(v.getWidht(),v.getHeight());
                    GL11.glVertex2f(v.getWidht(), 0);
                    GL11.glEnd();
                }
                GL11.glPopMatrix();
            }
        }
        /*
        gl.glUseProgram(0);
        gl.glPointSize(4);
        gl.glColor4f(0,0,0,1);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glPushMatrix();
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex2f(MPX, MPY);
        gl.glEnd();
        gl.glPopMatrix();
        */

    }

    private boolean intersect(Visualizer v, int x, int y) {
        if( x>v.getX() && x<v.getX()+v.getWidht() &&
                y>v.getY() && y<v.getY()+v.getHeight() )
            return true;
        return false;
    }


    
}
