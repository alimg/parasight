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
    protected String gieStain;

    public Cytoband(String name, String sourceFile,int chrNo, int position,int length,String gieStain) {
        super(name,sourceFile,chrNo,position);
        this.length = length;
        this.gieStain = gieStain;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    
}
