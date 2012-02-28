/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;
import java.nio.ByteBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
/**
 *
 * @author alim
 */
public class GLRenderer {
    static GenoscopeRenderer renderer=null;
    
    protected static void setup( GL2 gl2, int width, int height ) {
        //System.out.println("oldu" + width+"  "+height);
        gl2.glMatrixMode( GL2.GL_PROJECTION );
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, width, 0.0f, height );

        gl2.glMatrixMode( GL2.GL_MODELVIEW );
        gl2.glLoadIdentity();

        gl2.glViewport( 0, 0, width, height );
        gl2.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
    }

    protected static void render( GL2 gl2, int width, int height ) {
        //System.out.print("oldu\n");
        gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
        if(renderer!=null)
            renderer.draw(gl2);
    }

    static void setRenderer(GenoscopeRenderer r) {
        renderer=r;
    }
    
}
