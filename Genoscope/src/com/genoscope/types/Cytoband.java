/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public class Cytoband extends Feature{
    protected int length;

    public Cytoband(String name, String source, int chrNo, int position,int length) {
        super(name,source,chrNo,position);
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    
}
