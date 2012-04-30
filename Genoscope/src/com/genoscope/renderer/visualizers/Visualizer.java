/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.renderer.GLFontRenderer;
import com.genoscope.renderer.GLHandler;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
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
    private IntBuffer texts = null;
    //private int []matrix=new int[16]; //position&orientation
    boolean updateNeeded = true;
    private int posX;
    private int posY;
    private boolean higlighted;
    private boolean useFBO = false;
    private int FBOid;
    private boolean needRecreateBuffers = false;
    private int OVERSAMPLE=4;
    private int snapX=-100067;
    private int snapY=-100067;
    
    GLFontRenderer font;
    
    public Visualizer(int w, int h) {
        useFBO = GLHandler.FBOEnabled;
        setSize(w, h);

    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
        needRecreateBuffers = true;
        if(!useFBO)
            buffer = ByteBuffer.allocateDirect((width + 1) * (height) * 4 - 1);

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(int x, int y) {
        posX = x;
        posY = y;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
    public int getSnapX() {
        if(snapX==-100067)return posX;
        return snapX;
    }

    public int getSnapY() {
        if(snapY==-100067)return posY;
        return snapY;
    }

    public boolean isBufferUpToDate() {
        return !updateNeeded;
    }

    public final void initBufferMode() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glViewport(0, 0, OVERSAMPLE*width, OVERSAMPLE*height);
        GLU.gluOrtho2D(0.0f, width, height, 0.0f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        if (needRecreateBuffers) {
            if (useFBO) {
                glEnable(GL_TEXTURE_2D);
                glDeleteFramebuffersEXT(FBOid);
                FBOid = glGenFramebuffersEXT();// create a new framebuffer
                glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FBOid);

                if (texts != null) {
                    glDeleteTextures(texts);
                } else {
                    texts = createIntBuffer(1);
                }
                glGenTextures(texts);
                textId = texts.get(0);
                
                glBindTexture(GL_TEXTURE_2D, textId);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);   
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, OVERSAMPLE*width, OVERSAMPLE*height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);
                glBindTexture(GL_TEXTURE_2D, textId);

                glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, textId, 0);

                int i = glGetError();
                if (i != GL_NO_ERROR) {
                    System.out.println(" error " + i);
                }
            }
        }
        needRecreateBuffers = false;
    }

    public final void updateBuffer() {
        updateNeeded = false;//

        if (useFBO) {

            glBindTexture(GL_TEXTURE_2D, 0);
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FBOid);
        }
        GL20.glUseProgram(0);
        draw();
 
        if (useFBO) {
                glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        } else { // buffer screen if FBO is not supported
            //<editor-fold defaultstate="collapsed" desc="comment">
            glEnable(GL_TEXTURE_2D);
            glPixelStorei(GL_PACK_ALIGNMENT, 1);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            
            if (texts != null) {
                glDeleteTextures(texts);
            } else {
                texts = createIntBuffer(1);
            }
            glGenTextures(texts);
            textId = texts.get(0);
            
            glBindTexture(GL_TEXTURE_2D, textId);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width,
                    height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glBindTexture(GL_TEXTURE_2D, textId);
            System.out.println("textId " + textId);
            //</editor-fold>
        }
        
    }

    /**
     * Called when update needed
     *
     * @param gl
     */
    public void draw() {

        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        System.out.println("buffer update");
        glLoadIdentity();
        glBegin(GL_POLYGON);
        glColor4f(0, 1, 0, 1);
        glVertex2f(width / 2, height / 2);
        glColor4f(1, 0, 0, 1);
        glVertex2f(0, 0);
        glColor4f(0, 0, 1, 1);
        glVertex2f(width / 2, height);
        glColor4f(0, 1, 0, 0.0f);
        glVertex2f(width, 0);
        glEnd();
    }

    public final void drawBuffered() {
        
        glEnable(GL_TEXTURE_2D);
        //GL20.glUseProgram(0);

        //glRasterPos3f(10,500,0); 
        //glDrawPixels(width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer); 

        glBindTexture(GL_TEXTURE_2D, textId);
        
        GL20.glUseProgram(GLHandler.shaderprogram);
        GL20.glUniform1i(GLHandler.imgUniform, 0);
        GL20.glUniform2f(GLHandler.sizeUniform, width, height);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        //gl.glColor4f( 0, 0, 0 ,0.5f);
        //gl.glLoadIdentity();
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(0, height, 0);
        glTexCoord2f(0, -1);
        glVertex3f(0, 0, 0);
        glTexCoord2f(1, -1);
        glVertex3f(width, 0, 0);
        glTexCoord2f(1, 0);
        glVertex3f(width, height, 0);
        glEnd();
    }

    /**
     * is selected
     */
    public final void setHighlight(boolean h) {
        higlighted = h;
    }

    public final boolean isHiglighted() {
        return higlighted;
    }

    protected final IntBuffer createIntBuffer(int size) {
        ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
        temp.order(ByteOrder.nativeOrder());

        return temp.asIntBuffer();
    }

    final public void setSnapX(int x) {
        snapX=x;
    }
    final public void setSnapY(int y) {
        snapY=y;
    }

}
