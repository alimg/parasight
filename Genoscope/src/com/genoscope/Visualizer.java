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
        buffer = ByteBuffer.allocateDirect((WIDTH + 1)* (HEIGHT) * 3 - 1); 
    }
    
    void updateBuffer(GL2 gl)
    {
        updateNeeded=false;//
        
        draw(gl);
        
        
        gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1); 
        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1); 
        gl.glReadPixels(0,0, WIDTH, HEIGHT, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, buffer); 
    }
    /**
     * Called when update needed
     * @param gl 
     */
    public void draw(GL2 gl)
    {
        System.out.println("redraw");
        gl.glLoadIdentity();
        gl.glBegin( GL2.GL_TRIANGLES );
        gl.glColor3f( 1, 0, 0 );
        gl.glVertex2f( 0, 0 );
        gl.glColor3f( 0, 1, 0 );
        gl.glVertex2f( WIDTH, 0 );
        gl.glColor3f( 0, 0, 1 );
        gl.glVertex2f( WIDTH / 2, HEIGHT );
        gl.glEnd();
        
        
    }
    
    boolean isBufferUpToDate() { //should have default modifier
        //throw new UnsupportedOperationException("Not yet implemented");
        return !updateNeeded;
    }

    void drawBuffered(GL2 gl) {
        //throw new UnsupportedOperationException("Not yet implemented");
         
        gl.glRasterPos3f(30,300,0); 
        gl.glDrawPixels(WIDTH, HEIGHT, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buffer); 
    }
    
}
