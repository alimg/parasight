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
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author alim
 */
public class RendererThread extends Thread {
    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
    
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
        while(!Display.isCloseRequested())
        {

            Dimension newDim;
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
            synchronized(this)
            {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Genoscope.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Display.destroy();
        System.exit(0);
    }
    
    
    
}
