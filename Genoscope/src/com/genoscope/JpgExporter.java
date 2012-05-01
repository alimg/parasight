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
public class JpgExporter extends ImageExporter{
    
    public JpgExporter(String loc){
        super(loc,3);
    }
    
    @Override
    public void run() {
        Graphics g = super.bi.createGraphics();
        g.dispose();        
        try {
            ImageIO.write(super.bi, "jpg", new File(fileName));
        } catch (Exception e) {
        }
    }
    
}
