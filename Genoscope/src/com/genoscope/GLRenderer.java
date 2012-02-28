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
    }

    protected static void render( GL2 gl2, int width, int height ) {
        //System.out.print("oldu\n");
        gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
        if(renderer!=null)
            renderer.draw(gl2);
        /*
        // draw a triangle filling the window
        gl2.glLoadIdentity();
        gl2.glBegin( GL.GL_TRIANGLES );
        gl2.glColor3f( 1, 0, 0 );
        gl2.glVertex2f( 0, 0 );
        gl2.glColor3f( 0, 1, 0 );
        gl2.glVertex2f( width, 0 );
        gl2.glColor3f( 0, 0, 1 );
        gl2.glVertex2f( width / 2, height );
        gl2.glEnd();
        
        gl2.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1); 
        gl2.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1); 
        int WIDTH=50;
        int HEIGHT=50;
        ByteBuffer fbpixels = ByteBuffer.allocateDirect((WIDTH + 1)* (HEIGHT) * 3 - 1); 
        gl2.glReadPixels(200,100, WIDTH, HEIGHT, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, fbpixels); 
        gl2.glRasterPos3f(0,0,0); 
        gl2.glDrawPixels(WIDTH, HEIGHT, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, fbpixels); 
        */

    }

    static void setRenderer(GenoscopeRenderer r) {
        renderer=r;
    }
    
}
