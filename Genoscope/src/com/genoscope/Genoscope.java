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
    /**
     * @param args the command line arguments
     */
    static boolean ok=false;
    public static void main(String[] args) {
        //System.setProperty("org.lwjgl.opengl.Display.noinput","true");
        final GenoscopeRenderer a=new GenoscopeRenderer();
        
        GLHandler.setRenderer(a);
        GenoscopeApp f=new GenoscopeApp();
        f.setVisible(true);
        //System.out.println("Trying LWJGL");
        final Canvas c=new Canvas() {
            @Override
            public Dimension getSize() {
                //return new Dimension(0,0);
                return super.getSize();
            }

            @Override
            public void repaint() {
                //super.repaint();
                synchronized(renderThread)
                {
                    renderThread.notifyAll();
                }
            }

            @Override
            public void paint(Graphics g) {
                //super.paint(g);
		if(renderThread==null)
                    return;
                synchronized(renderThread)
                {
                    renderThread.notifyAll();
                }
            }

            @Override
            public void paintAll(Graphics g) {
                //super.paintAll(g);
            }

            
            
        };
        
        f.OpenGLPanel.setMinimumSize(new Dimension(0,0));
        //f.OpenGLPanel.setLayout(new BorderLayout());
        f.OpenGLPanel.add(c);
        GLHandler.setGLCanvas( c );
        //c.setFocusable(false);
        try {
            Display.setParent(c);
            
            Display.setVSyncEnabled(true);

            f.pack();

            c.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e)
                {
                    //System.out.println(" resize "+c.getSize());
                    renderThread.setSize(c.getSize());
                    synchronized(renderThread)
                    {
                        renderThread.notifyAll();
                    }
                }
            });

            
            renderThread=new RendererThread(a);
            renderThread.setSize(c.getSize());
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
        
            a.addVisualizer(new Visualizer(800, 80));
            a.addVisualizer(new Visualizer(200, 300));
            a.addVisualizer(new Visualizer(100, 300));
            a.addVisualizer(new Visualizer(64, 64));
            a.addVisualizer(new Visualizer(64, 64));
            a.addVisualizer(new Visualizer(64, 64));
            System.out.println("main returns");
    }
}
