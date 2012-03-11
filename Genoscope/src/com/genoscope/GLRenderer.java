/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;
import java.awt.Canvas;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
/**
 *
 * @author alim
 */
public class GLRenderer {
    private static GenoscopeRenderer renderer=null;
    private static Canvas glcanvas;
    
    static int imgUniform;
    static int sizeUniform;
    static int shaderprogram;
    static private int width;
    static private int height;

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }
    
    protected static void init() //shader initialization
    {  
        System.out.println(new File(".").getAbsoluteFile());
        try {
            
            int i=GL11.glGetError();
            if(i!=GL11.GL_NO_ERROR)
                System.out.println(" error "+i);
            
            int v = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
            BufferedReader brv = new BufferedReader(new FileReader("resources/vertexshader.glsl"));
            String vsrc = "";
            String line;
            while ((line=brv.readLine()) != null) {
            vsrc += line + "\n";
            }
            GL20.glShaderSource(v, vsrc );
            GL20.glCompileShader(v);
            
            int f = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
            BufferedReader brf = new BufferedReader(new FileReader("resources/fragmentshader.glsl"));
            String fsrc = "";
            while ((line=brf.readLine()) != null) {
            fsrc += line + "\n";
            }
            GL20.glShaderSource(f, fsrc);
            GL20.glCompileShader(f);
            int shaderprogram = GL20.glCreateProgram();
            System.out.println(" s "+shaderprogram);
            GL20.glAttachShader(shaderprogram, v);
            GL20.glAttachShader(shaderprogram, f);
            GL20.glLinkProgram(shaderprogram);
            GL20.glValidateProgram(shaderprogram);
            
            GL20.glUseProgram(shaderprogram);
            i=GL11.glGetError();
            if(i!=GL11.GL_NO_ERROR)
                System.out.println(" error "+i);
            imgUniform = GL20.glGetUniformLocation(shaderprogram, "baseImage");
            sizeUniform = GL20.glGetUniformLocation(shaderprogram, "baseSize");
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
    
    protected static void setup( int w, int h ) {
        width=w;
        height=h;
        //System.out.println("oldu" + width+"  "+height);
        GL11.glClearColor (1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        //GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glBlendFunc (GL11.GL_SRC_ALPHA_SATURATE, GL11.GL_ONE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH); 

        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT,GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT,GL11.GL_NICEST);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT,GL11.GL_NICEST);
        GL11.glMatrixMode( GL11.GL_PROJECTION );
        GL11.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        //GLU glu = new GLU();
        //glu.gluOrtho2D( 0.0f, width, 0.0f, height );
        GLU.gluOrtho2D( 0.0f, width, height,0.0f );

        GL11.glMatrixMode( GL11.GL_MODELVIEW );
        GL11.glLoadIdentity();

        GL11.glViewport( 0, 0, width, height );
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        
        GL11.glClear( GL11.GL_COLOR_BUFFER_BIT );
        
        
        if(renderer!=null)
            renderer.draw();
    }

    protected static void render( ) {
		
		if(width==0 || height==0)
			return;
        //System.out.print("oldu\n");
        GL11.glClear( GL11.GL_COLOR_BUFFER_BIT );
        if(renderer!=null)
            renderer.draw();
    }

    static void setRenderer(GenoscopeRenderer r) {
        renderer=r;
    }

    static void requestPaint() {
        glcanvas.repaint();
    }

    static void setGLCanvas(Canvas c) {
        glcanvas=c;
    }
    
}
