/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer;

import com.genoscope.Genoscope;
import com.genoscope.renderer.GLHandler;
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
    public Object initSync=new Object();
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
        GLHandler.init();
        synchronized(initSync)
        {
            initSync.notify();
        }
        Dimension newDim;
        int mButtonCount=Mouse.getButtonCount(),mouseState=0,mouseState_old=0;
        while(!Display.isCloseRequested())
        {

            newDim = newCanvasSize.getAndSet(null);

            if (newDim != null)
            {
                GL11.glViewport(0, 0, newDim.width, newDim.height);
                GLHandler.setup(newDim.width,newDim.height);
            }
            else{
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                GLHandler.render();
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
                    mouseState=0;
                    for(int i=0;i<mButtonCount;i++)
                    {
                        if(Mouse.isButtonDown(i))
                        {
                            mouseState|=1<<i;
                            //System.out.println("mouse " +i+" "+Mouse.getEventButtonState());
                        }
                        if( (((mouseState^mouseState_old)>>i)&1 )==1 )
                        {
                            if(Mouse.isButtonDown(i))
                                renderer.mouseDown(i);
                            else renderer.mouseUp(i);
                        }
                    }
                    mouseState_old=mouseState;
                    renderer.mouseMove(x,GLHandler.getHeight()-y, mouseState );
                }
                else {
                    //release mouse buttons when exited from screen
                    mouseState=0;
                    for(int i=0;i<mButtonCount;i++)
                        if( (((mouseState^mouseState_old)>>i)&1 )==1 )
                            renderer.mouseUp(i);
                    mouseState_old=0;
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
