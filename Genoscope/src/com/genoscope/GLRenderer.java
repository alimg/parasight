/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
/**
 *
 * @author alim
 */
public class GLRenderer {
    static GenoscopeRenderer renderer=null;
    
    static int imgUniform;
    static int sizeUniform;
    static int shaderprogram;
    
    protected static void init(GL2 gl) //shader initialization
    {  
        System.out.println(new File(".").getAbsoluteFile());
        try {
            
            int i=gl.glGetError();
            if(i!=GL2.GL_NO_ERROR)
                System.out.println(" error "+i);
            
            int v = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
            BufferedReader brv = new BufferedReader(new FileReader("resources/vertexshader.glsl"));
            String vsrc = "";
            String line;
            while ((line=brv.readLine()) != null) {
            vsrc += line + "\n";
            }
            gl.glShaderSource(v, 1, new String[] {vsrc}, (int[])null,0);
            gl.glCompileShader(v);
            
            int f = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
            BufferedReader brf = new BufferedReader(new FileReader("resources/fragmentshader.glsl"));
            String fsrc = "";
            while ((line=brf.readLine()) != null) {
            fsrc += line + "\n";
            }
            gl.glShaderSource(f, 1, new String[] {fsrc}, (int[])null,0);
            gl.glCompileShader(f);
            int shaderprogram = gl.glCreateProgram();
            System.out.println(" s "+shaderprogram);
            gl.glAttachShader(shaderprogram, v);
            gl.glAttachShader(shaderprogram, f);
            gl.glLinkProgram(shaderprogram);
            gl.glValidateProgram(shaderprogram);
            
            gl.glUseProgram(shaderprogram);
            i=gl.glGetError();
            if(i!=GL2.GL_NO_ERROR)
                System.out.println(" error "+i);
            imgUniform = gl.glGetUniformLocation(shaderprogram, "baseImage");
            sizeUniform = gl.glGetUniformLocation(shaderprogram, "baseSize");
            GLRenderer.shaderprogram=shaderprogram;
            System.out.println("uniform "+imgUniform);
            
        } catch (FileNotFoundException ex) {
           System.out.println("err:not found");
            Logger.getLogger(GLRenderer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
           System.out.println("err:io exception");
            Logger.getLogger(GLRenderer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           System.out.println("done loading shaders");
        }
    }
    
    protected static void setup( GL2 gl2, int width, int height ) {
        //System.out.println("oldu" + width+"  "+height);
        gl2.glClearColor (1.0f, 1.0f, 1.0f, 0.0f);
        gl2.glEnable(GL2.GL_LINE_SMOOTH);
        gl2.glEnable(GL2.GL_MULTISAMPLE);
        gl2.glEnable(GL2.GL_POLYGON_SMOOTH);
        gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        //gl2.glBlendFunc (GL2.GL_SRC_ALPHA_SATURATE, GL2.GL_ONE);
        gl2.glEnable(GL2.GL_BLEND);
        gl2.glEnable(GL2.GL_LINE_SMOOTH); 

        gl2.glHint(GL2.GL_LINE_SMOOTH_HINT,GL2.GL_NICEST);
        gl2.glHint(GL2.GL_POLYGON_SMOOTH_HINT,GL2.GL_NICEST);
        gl2.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT,GL2.GL_NICEST);
        gl2.glMatrixMode( GL2.GL_PROJECTION );
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, width, 0.0f, height );

        gl2.glMatrixMode( GL2.GL_MODELVIEW );
        gl2.glLoadIdentity();

        gl2.glViewport( 0, 0, width, height );
        gl2.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        
        gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
        if(renderer!=null)
            renderer.draw(gl2);
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
