/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public class Cytoband extends Feature{
    int length;

    public Cytoband(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    
}
