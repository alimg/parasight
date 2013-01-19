package com.genoscope.renderer.visualizers;

import com.genoscope.renderer.GLHandler;
import com.genoscope.renderer.GenoscopeRenderer;
import com.genoscope.renderer.TrueTypeFont;
import com.genoscope.renderer.mouseactions.MoveAction;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

/**
 * This is base Visualizer class that handles buffering and input operations. 
 * 
 * @see GLHandler
 * 
 * @author alim
 */
public class Visualizer {

    private int width;
    private int height;
    private int absoluteHeight;
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
    private boolean needRecreateBuffers = true;
    protected int OVERSAMPLE=2;
    private int snapX=-100067;
    private int snapY=-100067;
    private boolean visible = true;
    
    static TrueTypeFont font=GLHandler.font;
    /**
     * if visualizer moved
     */
    private boolean coordsChanged=false;
    /**
     * is moving
     */
    private boolean moving=false;
    protected int minWidth=150;
    protected int minHeight=80;
    private String mName="";
    
    public Visualizer(int w, int h) {
        useFBO = GLHandler.FBOEnabled;
        setSize(w, h);
        
    }

    /**
     * Changes only size of appearance. 
     * <b>NOTE: doneResizing() must be called to request actual rendering</b>
     * @param w width
     * @param h height
     * @see MoveAction
     */
    public void setSize(int w, int h) {
        if(w<minWidth)
            w = 150;
        if(h<minHeight)
            h = 80;
        width = w;
        height = h;
        
        if(absoluteHeight>0)
            height=absoluteHeight;
            
        if(!useFBO)
            buffer = ByteBuffer.allocateDirect((width + 1) * (height) * 4 - 1);
    }
    /**
     * Called after resizing to request actual redrawing of this visualizer 
     * (ie buffer update).
     * @see MoveAction
     */
    public void doneResizing()
    {
        updateNeeded=true;
        needRecreateBuffers = true;
    }


    /**
    * @return vertical distance to the top left corner of screen
    */
    public int getX() {
        return posX;
    }
    public void setX(int a) {
        posX=a;
    }
    /**
    * @return horizonal distance to the top corner of screen
    */
    public int getY() {
        return posY;
    }
    public void setY(int a) {
        posY=a;
    }
    
    /**
     * Set position to appear
     * @param x
     * @param y 
     */
    public void setPosition(int x, int y) {
        posX = x;
        posY = y;
    }
    
    public void setAbsoluteHeight(int h){
        absoluteHeight=h;
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * 
     * @return x 
     * @see MoveAction
     */
    public int getSnapX() {
        if(snapX==-100067)return posX;
        return snapX;
    }

    /**
     * 
     * @return y 
     * @see MoveAction
     */
    public int getSnapY() {
        if(snapY==-100067)return posY;
        return snapY;
    }

    /**
     * 
     * @return true if this visualizer needs redrawing
     */
    public boolean isBufferUpToDate() {
        return !updateNeeded;
    }
    /**
     * Called by GenoscopeRenderer when update is needed
     * @see GenoscopeRenderer
     */
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

    /**
     * Called by GenoscopeRenderer when update is needed
     * @see GenoscopeRenderer
     */
    public final void updateBuffer() {
        updateNeeded = false;//

        if (useFBO) {

            glBindTexture(GL_TEXTURE_2D, 0);
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FBOid);
        }
        GL20.glUseProgram(0);
        glTranslated(0, getHeight(), 0);
        glScalef(1, -1, 1);
        draw();
 
        if (useFBO) {
                glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        } else { // buffer screen if FBO is not supported
            //<editor-fold defaultstate="collapsed" desc="buffer screen if FBO is not supported">
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
     * Implement this class to do visualization of genomic data. This class is
     * called when buffers are being update for this visualizer.
     * 
     * @see Visualizer
     */
    public void draw() {

        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
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

    /**
     * Draws the Visualizer's last state from buffers.
     * @see GenoscopeRenderer
     */
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
     * Set highlighting value to let GenoscopeRenderer draw a frame around 
     * visualizer.
     * 
     * @param h set if this visualizer is highlighted
     */
    public final void setSelected(boolean h) {
        higlighted = h;
    }

    /**
     *
     * @see GenoscopeRenderer
     */
    public final boolean isSelected() {
        return higlighted;
    }

    protected final IntBuffer createIntBuffer(int size) {
        ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
        temp.order(ByteOrder.nativeOrder());

        return temp.asIntBuffer();
    }

    /**
     * 
     * @param x 
     * @see MoveAction
     */
    final public void setSnapX(int x) {
        snapX=x;
    }
    
    /**
     * 
     * @param y 
     * @see MoveAction
     */
    final public void setSnapY(int y) {
        snapY=y;
    }

    final public void destroy()
    {
        //TODO free fbo's and buffers 
    }

/* **** These are states needed for pairwise data;    */
    public boolean getCoordinatesUpdated() {
        return coordsChanged;
    }
    
    public boolean getCoordinatesChanging() {
        return moving;
    }

    public void setCoordinatesChanging() {
        moving=true;
    }
    public void setCoordinatesUpdateDone() {
        moving=false;
        coordsChanged=true;
        
    }
    public void setCoordinatesUpdateHandled() {
        coordsChanged=false;
        
    }   
 /* **********************************************    */

    
    public boolean hasChromosome() {
        return false;
    }

    public void setName(String name) {
        mName=name;
    }

    @Override
    public String toString() {
        return mName;
    }
    
}
