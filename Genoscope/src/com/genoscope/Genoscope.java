/*
 * 
 */
package com.genoscope;

import com.genoscope.renderer.GenoscopeRenderer;
import com.genoscope.renderer.RendererThread;
import com.genoscope.renderer.GLHandler;
import com.genoscope.renderer.visualizers.Visualizer;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

/**
 *
 * @author alim
 */
public class Genoscope {

    private static Object renderLocker=new Object();
    private static RendererThread renderThread;
    public static Canvas canvas=new Canvas() {
            @Override
            public Dimension getSize() {
                return super.getSize();
            }
            @Override
            public void repaint() {
                synchronized(renderThread)
                {
                    renderThread.notifyAll();
                }
            }
            @Override
            public void paint(Graphics g) {
		if(renderThread==null)
                    return;
                synchronized(renderThread)
                {
                    renderThread.notifyAll();
                }
            }
            @Override
            public void paintAll(Graphics g) {
            }
        };
        
    /**
     * @param args the command line arguments
     */
    static boolean ok=false;
    public static void main(String[] args) {
        //System.setProperty("org.lwjgl.opengl.Display.noinput","true");
        final GenoscopeRenderer renderer=new GenoscopeRenderer();
        
        GLHandler.setRenderer(renderer);
        GenoscopeApp app=new GenoscopeApp();
        app.setVisible(true);
        app.getAppState().setRenderer(renderer);
        //System.out.println("Trying LWJGL");
        
       
        
       
        app.OpenGLPanel.setMinimumSize(new Dimension(0,0));
        //f.OpenGLPanel.setLayout(new BorderLayout());
        app.OpenGLPanel.add(canvas);
        GLHandler.setGLCanvas( canvas );
        //c.setFocusable(false);
        try {
            Display.setParent(canvas);
            
            Display.setVSyncEnabled(true);

            app.pack();

            canvas.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e)
                {
                    //System.out.println(" resize "+c.getSize());
                    renderThread.setSize(canvas.getSize());
                    synchronized(renderThread)
                    {
                        renderThread.notifyAll();
                    }
                }
            });

            
            renderThread=new RendererThread(renderer);
            renderThread.setSize(canvas.getSize());
            renderThread.start();
            
        } catch (LWJGLException e1) {
            e1.printStackTrace();
        }
        synchronized(renderThread.initSync)
        {
            try {
                renderThread.initSync.wait();
            } catch (InterruptedException ex) {
                //Logger.getLogger(Genoscope.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        System.out.println("main returns");
    }
}
