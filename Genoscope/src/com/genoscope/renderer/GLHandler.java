/*
 * GLHandler.java
 * Shader initialization, viewport setup etc.
 */
package com.genoscope.renderer;
import java.awt.Canvas;
import java.awt.Font;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
/**
 *
 * @author alim
 */
public class GLHandler {
    private static GenoscopeRenderer renderer=null;
    private static Canvas glcanvas;
    
    static public int imgUniform;
    static public int sizeUniform;
    static public int shaderprogram;
    static private int width;
    static private int height;
    static public boolean FBOEnabled;
    
    public static TrueTypeFont font=null;


    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }
    
    public static void init() //shader initialization
    {  
        System.out.println(new File(".").getAbsoluteFile());
        try {
            
            int i=glGetError();
            if(i!=GL_NO_ERROR)
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
            shaderprogram = GL20.glCreateProgram();
            System.out.println(" s "+shaderprogram);
            GL20.glAttachShader(shaderprogram, v);
            GL20.glAttachShader(shaderprogram, f);
            GL20.glLinkProgram(shaderprogram);
            GL20.glValidateProgram(shaderprogram);
            
            GL20.glUseProgram(shaderprogram);
            i=glGetError();
            if(i!=GL_NO_ERROR)
                System.out.println(" error init "+i);
            imgUniform = GL20.glGetUniformLocation(shaderprogram, "baseImage");
            sizeUniform = GL20.glGetUniformLocation(shaderprogram, "baseSize");
            
            //System.out.println("uniform "+imgUniform);
            
        } catch (FileNotFoundException ex) {
           System.out.println("err:not found");
            Logger.getLogger(GLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
           System.out.println("err:io exception");
            Logger.getLogger(GLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           System.out.println("done loading shaders");
        }
        
        //frame buffer object
        FBOEnabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
        System.out.println("framebuffer enabled "+FBOEnabled);
        
        GL20.glUseProgram(0);
        font=new TrueTypeFont(new Font("serif", 0&Font.ITALIC | Font.BOLD, 12), true);
        
    }
    
    static void setup() {
        
        setup(width,height);
    }
    
  
    public static void setup( int w, int h ) {
        width=w;
        height=h;
        if(width==0 || height==0)
                return;
        //System.out.println("oldu" + width+"  "+height);
        glClearColor (1.0f, 1.0f, 1.0f, 1.0f);
       // glEnable(GL_LINE_SMOOTH);
        //glEnable(GL13.GL_MULTISAMPLE);
        glEnable(GL_POLYGON_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc (GL_SRC_ALPHA_SATURATE, GL_ONE);
        glEnable(GL_BLEND);
        //glEnable(GL_LINE_SMOOTH); 
        glDisable(GL_DEPTH_TEST);
        glHint(GL_LINE_SMOOTH_HINT,GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT,GL_NICEST);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT,GL_NICEST);
        glMatrixMode( GL_PROJECTION );
        glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        //GLU glu = new GLU();
        //glu.gluOrtho2D( 0.0f, width, 0.0f, height );
        GLU.gluOrtho2D( 0.0f, width, height,0.0f );

        glMatrixMode( GL_MODELVIEW );
        glLoadIdentity();

        glViewport( 0, 0, width, height );
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    protected static void render( ) {
		
        if(width==0 || height==0)
                return;
        //System.out.print("oldu\n");
        glClear( GL_COLOR_BUFFER_BIT );
        if(renderer!=null)
            renderer.draw();

        int i=glGetError();
        if(i!=GL_NO_ERROR)
            System.out.println(" error "+i);
    }

    public static void setRenderer(GenoscopeRenderer r) {
        renderer=r;
    }

    static void requestPaint() {
        glcanvas.repaint();
    }

    public static void setGLCanvas(Canvas c) {
        glcanvas=c;
    }

    
}
