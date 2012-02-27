/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import java.util.ArrayList;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public class Chromosome {
    private int length;
    private int chrNo;
    private String name;
    ArrayList<Cytoband> cytobands;

    public Chromosome(int length, int chrNo, String name, ArrayList<Cytoband> cytobands) {
        this.length = length;
        this.chrNo = chrNo;
        this.name = name;
        this.cytobands = cytobands;
    }

    public int getChrNo() {
        return chrNo;
    }

    public void setChrNo(int chrNo) {
        this.chrNo = chrNo;
    }

    public ArrayList<Cytoband> getCytobands() {
        return cytobands;
    }

    public void setCytobands(ArrayList<Cytoband> cytobands) {
        this.cytobands = cytobands;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
  
}
