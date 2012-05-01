/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer;

import com.genoscope.Genoscope;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
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
    boolean running = true;
    RendererThread that = this;
    GenoscopeRenderer renderer;
    public Object initSync = new Object();
    public static Object exportSync = new Object();
    private static BufferedImage image;
    private static boolean screenshotReq = false;
    private static int bpp;

    public RendererThread(GenoscopeRenderer renderer) {
        this.renderer = renderer;
    }

    public static BufferedImage getScreenShot(int bpp) {
        synchronized (exportSync) {
            RendererThread.bpp=bpp;
            screenshotReq = true;
            try {
                exportSync.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(RendererThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return image;

    }

    public void setSize(Dimension s) {
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
        synchronized (initSync) {
            initSync.notify();
        }
        Dimension newDim;
        int mButtonCount = Mouse.getButtonCount(), mouseState = 0, mouseState_old = 0;
        boolean needUpdate=true;
        while (!Display.isCloseRequested()) {

            newDim = newCanvasSize.getAndSet(null);

            if (newDim != null) {
                GL11.glViewport(0, 0, newDim.width, newDim.height);
                GLHandler.setup(newDim.width, newDim.height);
            } else  if(needUpdate){
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                GLHandler.render();
            }
            Display.update();


            //System.out.println("mouse");

            while (Mouse.next()) {

                if (Mouse.isInsideWindow()) {
                    int x = Mouse.getX();
                    int y = Mouse.getY();
                    renderer.mouseMove(x, GLHandler.getHeight() - y, mouseState);
                    //System.out.println("mouse "+x+" "+y);
                    mouseState = 0;
                    for (int i = 0; i < mButtonCount; i++) {
                        if (Mouse.isButtonDown(i)) {
                            mouseState |= 1 << i;
                            //System.out.println("mouse " +i+" "+Mouse.getEventButtonState());
                        }
                        if ((((mouseState ^ mouseState_old) >> i) & 1) == 1) {
                            if (Mouse.isButtonDown(i)) {
                                renderer.mouseDown(i);
                            } else {
                                renderer.mouseUp(i);
                            }
                        }
                    }
                    renderer.mouseWheel(Mouse.getEventDWheel());
                    mouseState_old = mouseState;
                } else {
                    /*
                     * //release mouse buttons when exited from screen
                     * mouseState=0; for(int i=0;i<mButtonCount;i++) if(
                     * (((mouseState^mouseState_old)>>i)&1 )==1 )
                     * renderer.mouseUp(i);
                    mouseState_old=0;
                     */
                }
            }
            synchronized (this) {
                try {
                    this.wait(30);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Genoscope.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (screenshotReq) 
            {
                final int width=GLHandler.getWidth();
                final int height=GLHandler.getHeight();
                ByteBuffer buffer = ByteBuffer.allocateDirect((width + 1) * (height) * 4 - 1);
                
                GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                
                
                
                if(bpp == 4){
                    GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                }
                if(bpp == 3){
                    GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
                    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    
                }
                
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int i = (x + (width * y)) * bpp;
                        int a = 0xFF;
                        int r = buffer.get(i) & 0xFF;
                        int g = buffer.get(i + 1) & 0xFF;
                        int b = buffer.get(i + 2) & 0xFF;
                        if(bpp == 4)
                            a = buffer.get(i + 3) & 0xFF;
                        image.setRGB(x, height - (y + 1), (a << 24) | (r << 16) | (g << 8) | b);
                        
                    }
                }
                
                synchronized(exportSync){
                    exportSync.notify();
                }
                screenshotReq=false;
                
            }
            //</editor-fold>
        }
        running = false;
        Display.destroy();
        System.exit(0);
    }
}
