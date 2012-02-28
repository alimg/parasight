/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;


/**
 * This class handles OpenGL and input events for Visualizer classes.
 * @author alim
 */
public class GenoscopeRenderer implements MouseListener {
    ArrayList<Visualizer> clients= new ArrayList<Visualizer>();
    
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
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    void draw(GL2 gl) {
        for(Visualizer v: clients)
        {
            //TODO init buffer
            if(! v.isBufferUpToDate())
                v.updateBuffer(gl);
        }
        
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT );
        for(Visualizer v: clients)
        {
            if(v.isBufferUpToDate())
                v.drawBuffered(gl);
        }

    }

    
}
