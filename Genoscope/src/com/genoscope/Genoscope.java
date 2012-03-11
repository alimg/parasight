/*
 * 
 */
package com.genoscope;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
        
        GLRenderer.setRenderer(a);
        a.addVisualizer(new Visualizer(300, 80));
        a.addVisualizer(new Visualizer(200, 300));
        a.addVisualizer(new Visualizer(100, 300));
        a.addVisualizer(new Visualizer(100, 50));
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
        GLRenderer.setGLCanvas( c );
        
        try {
            Display.setParent(c);
            
            Display.setVSyncEnabled(true);

            f.pack();

            c.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e)
                {
                    //System.out.println(" resize");
                    renderThread.setSize(c.getSize());
                    synchronized(renderThread)
                    {
                        renderThread.notifyAll();
                    }
                }
            });

            
            renderThread=new RendererThread(a);
            renderThread.start();
            
        } catch (LWJGLException e1) {
            e1.printStackTrace();
        }

        System.out.println("main returns");

    }
}
