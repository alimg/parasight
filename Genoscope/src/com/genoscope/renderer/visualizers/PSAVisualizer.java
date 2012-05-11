/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.types.*;
import java.util.TreeMap;
import static org.lwjgl.opengl.GL11.*;


/**
 *
 * @author Ahmet
 */
public class PSAVisualizer extends ChromosomeVisualizer {

    private final int BIG = 2000000000;
    TreeMap<String,Integer> map = new <String,Integer>TreeMap();

    public PSAVisualizer(int w, int h, Chromosome chr) {
         super(w, h, chr);
         int height = 0;
         
         for (Feature i : chromosome.getFeatures()) {
             PSA f = (PSA) i;
     
             if( !map.containsKey(f.getType()) ){
                 if(font.getWidth(f.getType()) > paddingLeft)
                     paddingLeft = font.getWidth(f.getType());
                 
                 map.put(f.getType(), new Integer(height));
                 height+=f.getHeight();
  
             }
         }
        
         paddingLeft+=5;
         paddingLeft/=OVERSAMPLE;
         setSize((int)(getWidth()+paddingLeft),(int)((height+paddingTop*OVERSAMPLE)*5/4f));
         doneResizing();
         
    }

    @Override
    public void draw() {
        glDisable(GL_TEXTURE_2D);
        glClearColor(1, 1, 1, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        float h = getHeight() / 4.0f;
        float w = getWidth();




        glLineWidth(5.0f);
        glColor4f(0, 0, 0, 1);
        glBegin(GL_LINES);
        glVertex2f(0.0f, h);
        glVertex2f(w, h);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        font.drawString(20, getHeight() - 15, (chromosome.getName() + " (" + chromosome.getStart() + ":" + chromosome.getEnd() + ")"), 1, 1);
        for(String s: map.keySet()){
            int i = map.get(s);
            System.out.println(s);
            glLineWidth(1.0f);
            glColor4f(0, 0, 0, 1);
            font.drawString(1,h+i+5,s, 1, 1);
            
        }
        glDisable(GL_TEXTURE_2D);

        synchronized (chromosome) {
            for (Feature i : chromosome.getFeatures()) {

                PSA f = (PSA) i;
                Color c = f.getColor();

                float x1 = getPositionX(f.getPosition());
                float y1 = h + 5 + map.get(f.getType());
                //System.out.println(map.get(f.getName()));
                float x2 = getPositionX(f.getPosition()+f.getLength());
                float y2 = y1 + f.getHeight();  
                if(-1<= x1 - x2 && x1 - x2 <= 1) 
                    x2++;


                glBegin(GL_QUADS);

                glColor4f((float) c.getR() / 255, (float) c.getG() / 255, (float) c.getB() / 255, 0.8f);



                glVertex2f(x1, y1);

                glVertex2f(x2, y1);

                glVertex2f(x2, y2);

                glVertex2f(x1, y2);

                glEnd();

            }
        }

    }
}
