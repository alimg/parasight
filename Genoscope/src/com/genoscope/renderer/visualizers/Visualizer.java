/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.renderer.GLHandler;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author alim
 */
public class Visualizer {
    
    private int width;
    private int height;
    private ByteBuffer buffer;
    private int textId;
    private IntBuffer texts=null;
    //private int []matrix=new int[16]; //position&orientation
    
    boolean updateNeeded=true;
    
    private int posX;
    private int posY;
    private boolean higlighted;


    public Visualizer(int w,int h)
    {
        setSize(w, h);
    }
    public void setSize(int w,int h)
    {
        width=w;
        height=h;
        buffer = ByteBuffer.allocateDirect((width + 1)* (height) * 4 - 1); 
    }
    
    public int getWidht() {
        return width;
    }
    public int getHeight(){
        return height;
    }

    public void setPosition(int x, int y) {
        posX=x;
        posY=y;
    }
    public int getX()
    {
        return posX;
    }
    public int getY()
    {
        return posY;
    }

        
    public boolean isBufferUpToDate() { //should have default modifier
        //throw new UnsupportedOperationException("Not yet implemented");
        return !updateNeeded;
    }
    
    public final void initBuffer() {
        GL11.glMatrixMode( GL11.GL_PROJECTION );
        GL11.glLoadIdentity();
        GL11.glViewport( 0, 0, width, height );
        GLU.gluOrtho2D( 0.0f, width, height,0.0f );
        GL11.glMatrixMode( GL11.GL_MODELVIEW );
        GL11.glLoadIdentity();
    }
    

    
    public final void updateBuffer()
    {
        updateNeeded=false;//
        GL20.glUseProgram(0);
        draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1); 
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1); 
        GL11.glReadPixels(0,0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        
        if(texts!=null)
            GL11.glDeleteTextures(texts);
        else texts=createIntBuffer(1);
        GL11.glGenTextures(texts);
        textId=texts.get(0);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width,
                height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,buffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textId);
        System.out.println("textId "+textId);
        
    }
    /**
     * Called when update needed
     * @param gl 
     */
    public void draw()
    {
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        System.out.println("buffer update");
        GL11.glLoadIdentity();
        GL11.glBegin( GL11.GL_QUADS );
        GL11.glColor4f( 1, 0, 0,1 );
        GL11.glVertex2f( 0, 0 );
        GL11.glColor4f( 0, 1, 0,1 );
        GL11.glVertex2f( width/2, height/2 );
        GL11.glColor4f( 0, 1, 0,0.0f);
        GL11.glVertex2f( width, 0 );
        GL11.glColor4f( 0, 0, 1,1 );
        GL11.glVertex2f( width / 2, height );
        GL11.glEnd();
    }


    public final void drawBuffered() {
        //throw new UnsupportedOperationException("Not yet implemented");
         
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL20.glUseProgram(0);
        
        //GL11.glRasterPos3f(10,500,0); 
        //GL11.glDrawPixels(width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer); 
                
        GL20.glUseProgram(GLHandler.shaderprogram);
        GL20.glUniform1i(GLHandler.imgUniform, 0);
        GL20.glUniform2f(GLHandler.sizeUniform, width,height);
        
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textId);
        GL20.glUseProgram(GLHandler.shaderprogram);
        
        //gl.glColor4f( 0, 0, 0 ,0.5f);
        //gl.glLoadIdentity();
        GL11.glBegin( GL11.GL_QUADS );
        GL11.glTexCoord2f( 0, 0 );
        GL11.glVertex3f( 0, height,0 );
        GL11.glTexCoord2f( 0, -1 );
        GL11.glVertex3f( 0, 0,0 );
        GL11.glTexCoord2f( 1, -1 );
        GL11.glVertex3f( width, 0,0 );
        GL11.glTexCoord2f( 1, 0 );
        GL11.glVertex3f( width, height,0 );
        GL11.glEnd();
    }

    /**
     * is selected
     */
    public final void setHighlight(boolean intersect) {
        higlighted=intersect;
    }
    public final boolean isHiglighted() {
        return higlighted;
    }
    
    protected final IntBuffer createIntBuffer(int size) {
      ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
      temp.order(ByteOrder.nativeOrder());

      return temp.asIntBuffer();
    }

}
