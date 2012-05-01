/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import com.genoscope.renderer.RendererThread;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Ahmet Kerim ŞENOL
 */
public class ImageExporter extends Thread {

    String fileName;
    BufferedImage bi;

    public ImageExporter(String loc,int bpp) {
        fileName = loc;
        bi = RendererThread.getScreenShot(bpp);
    }
}
