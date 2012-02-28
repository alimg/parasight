/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.GLBuffers;
import java.nio.ByteBuffer;
import javax.media.opengl.GL;

/**
 *
 * @author alim
 */
class Visualizer {
    
    int WIDTH;
    int HEIGHT;
    ByteBuffer buffer;
    
    //private int []matrix=new int[16]; //position&orientation
    
    boolean updateNeeded=true;
    public Visualizer(int w,int h)
    {
        setSize(w, h);
    }
    public void setSize(int w,int h)
    {
        WIDTH=w;
        HEIGHT=h;
        buffer = ByteBuffer.allocateDirect((WIDTH + 1)* (HEIGHT) * 4 - 1); 
    }
    
    final void updateBuffer(GL2 gl)
    {
        updateNeeded=false;//
        
        draw(gl);
        
        //debug
        gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1); 
        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1); 
        gl.glReadPixels(0,0, WIDTH, HEIGHT, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer); 
    }
    /**
     * Called when update needed
     * @param gl 
     */
    public void draw(GL2 gl)
    {
        System.out.println("redraw");
        gl.glLoadIdentity();
        gl.glBegin( GL2.GL_LINE_LOOP );
        gl.glColor3f( 1, 0, 0 );
        gl.glVertex2f( 0, 0 );
        gl.glColor3f( 0, 1, 0 );
        gl.glVertex2f( WIDTH/2, HEIGHT/2 );
        gl.glVertex2f( WIDTH, 0 );
        gl.glColor3f( 0, 0, 1 );
        gl.glVertex2f( WIDTH / 2, HEIGHT );
        gl.glEnd();
    }
    
    boolean isBufferUpToDate() { //should have default modifier
        //throw new UnsupportedOperationException("Not yet implemented");
        return !updateNeeded;
    }

    final void drawBuffered(GL2 gl) {
        //throw new UnsupportedOperationException("Not yet implemented");
         
        gl.glRasterPos3f(300,300,0); 
        gl.glDrawPixels(WIDTH, HEIGHT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer); 
    }
    
}
