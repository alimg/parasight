/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author alim
 */
public class RendererThread extends Thread {
    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
    boolean running=true;
    RendererThread that=this;
    GenoscopeRenderer renderer;
    public RendererThread(GenoscopeRenderer renderer) {
        this.renderer=renderer;
    }
    
    
    
    public void setSize(Dimension s)
    {
        newCanvasSize.set(s);
    }
    @Override
    public void run() {
        try {
            Display.create();

        } catch (LWJGLException ex) {
            Logger.getLogger(Genoscope.class.getName()).log(Level.SEVERE, null, ex);
        }
        GLRenderer.init();
        Dimension newDim;
        while(!Display.isCloseRequested())
        {

            newDim = newCanvasSize.getAndSet(null);

            if (newDim != null)
            {
                GL11.glViewport(0, 0, newDim.width, newDim.height);
                GLRenderer.setup(newDim.width,newDim.height);
            }
            else{
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                GLRenderer.render();
            }
            Display.update();

            
            //System.out.println("mouse");
            
            while(Mouse.next())
            {
                if(Mouse.isInsideWindow())
                {
                    int x=Mouse.getX();
                    int y=Mouse.getY();
                    //System.out.println("mouse "+x+" "+y);
                    for(int i=0;i<Mouse.getButtonCount();i++)
                        System.out.println("mouse "+Mouse.isButtonDown(i));
                    
                    renderer.mouseMove(x,GLRenderer.getHeight()-y);
                }
            }
            synchronized(this)
            {
                try {
                    this.wait(30);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Genoscope.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        running=false;
        Display.destroy();
        System.exit(0);
    }
    
    
    
}
