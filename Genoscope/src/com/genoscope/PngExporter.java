/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import java.awt.Graphics;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Ahmet Kerim ŞENOL
 */
public class PngExporter extends ImageExporter{
    
    public PngExporter(String loc){
        super(loc,4);
    }
    
    @Override
    public void run() {
        Graphics g = super.bi.createGraphics();
        g.dispose();        
        try {
            ImageIO.write(super.bi, "png", new File(fileName));
        } catch (Exception e) {
        }
    }
    
}
