/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

/**
 *
 * @author Ahmet Kerim ŞENOL
 */
public class PSA extends Feature{
    protected int length; // bulungdugu chromosome uzerindeki uzunlugu
  
    protected int height;
    protected Color color;
    protected String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
  
    public PSA(String name, int position,int length,int height, Color color,String type) {
        super(name,position);
        this.length = length;
        this.height = height;
        this.color = color;
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getLength() {
        return length;
    }
    
    public void setLength(int length) {
        this.length = length;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
        
}
