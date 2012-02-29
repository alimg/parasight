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
    int textId;
    int []texts=null;
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
        gl.glUseProgram(0);
        draw(gl);
        
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1); 
        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1); 
        gl.glReadPixels(0,0, WIDTH, HEIGHT, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer); 
        if(texts!=null)
            gl.glDeleteTextures(WIDTH, texts, textId);
        texts=new int [1];
        gl.glGenTextures(1,texts , 0);
        textId=texts[0];
        
        gl.glBindTexture(GL.GL_TEXTURE_2D,textId);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri (GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_RGBA, WIDTH,
                HEIGHT, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE,buffer);
        gl.glBindTexture(GL.GL_TEXTURE_2D,textId);
        System.out.println("textId"+textId);
        
    }
    /**
     * Called when update needed
     * @param gl 
     */
    public void draw(GL2 gl)
    {
        
        gl.glDisable(GL.GL_TEXTURE_2D);
        System.out.println("redraw");
        gl.glLoadIdentity();
        gl.glBegin( GL2.GL_QUADS );
        gl.glColor4f( 1, 0, 0,1 );
        gl.glVertex2f( 0, 0 );
        gl.glColor4f( 0, 1, 0,1 );
        gl.glVertex2f( WIDTH/2, HEIGHT/2 );
        gl.glColor4f( 0, 1, 0,0.8f);
        gl.glVertex2f( WIDTH, 0 );
        gl.glColor4f( 0, 0, 1,1 );
        gl.glVertex2f( WIDTH / 2, HEIGHT );
        gl.glEnd();
    }
    
    boolean isBufferUpToDate() { //should have default modifier
        //throw new UnsupportedOperationException("Not yet implemented");
        return !updateNeeded;
    }

    final void drawBuffered(GL2 gl) {
        //throw new UnsupportedOperationException("Not yet implemented");
         
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glUseProgram(0);
        
        gl.glRasterPos3f(10,300,0); 
        gl.glDrawPixels(WIDTH, HEIGHT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer); 
                
        gl.glUseProgram(GLRenderer.shaderprogram);
        gl.glUniform1i(GLRenderer.imgUniform, 0);
        gl.glUniform2f(GLRenderer.sizeUniform, WIDTH,HEIGHT);
        
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D,textId);
        gl.glUseProgram(GLRenderer.shaderprogram);
        
        //gl.glColor4f( 0, 0, 0 ,0.5f);
        gl.glLoadIdentity();
        gl.glBegin( GL2.GL_QUADS );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 0, HEIGHT,0 );
        gl.glTexCoord2f( 0, -1 );
        gl.glVertex3f( 0, 0,0 );
        gl.glTexCoord2f( 1, -1 );
        gl.glVertex3f( WIDTH, 0,0 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( WIDTH, HEIGHT,0 );
        gl.glEnd();
    }
    
}
