/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import com.genoscope.renderer.RendererThread;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.image.RescaleOp;
import java.io.FileOutputStream;

/**
 *
 * @author Ahmet Kerim ŞENOL
 */
public class PdfExporter extends Thread{

    @Override
    public void run() {
        
        Document document = new Document();
        System.out.println("save menu opened");
        try {
            PdfWriter writer;
            writer = PdfWriter.getInstance(document,
                    new FileOutputStream("my_jtable_shapes.pdf"));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(500, 500);
            Graphics2D g2 = new PdfGraphics2D(cb,500,500);
            float[] scales = { 1f, 1f, 1f, 0.5f };
            float[] offsets = new float[4];
            RescaleOp rop = new RescaleOp(scales, offsets, null);
            
            g2.drawImage(RendererThread.getScreenShot(),rop,0,0);
            g2.dispose();
            cb.addTemplate(tp, 30, 300);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("save menu closed");
        document.close();
        
    }
    
    
    
    
}
