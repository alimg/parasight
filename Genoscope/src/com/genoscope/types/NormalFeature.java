/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

import java.util.ArrayList;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public class NormalFeature extends Feature{
    protected int length; // bulungdugu chromosome uzerindeki uzunlugu
    protected int score;  // verilen ozellige gore bir skor
    protected boolean strand;// verilen feature in hangi DNA strandinde oldugu
    
    // bazen bir Feature in blocklari olacak icinde bunlar da NormalFeature olarak dusunulebilir
    ArrayList<Feature> blocks;

    public NormalFeature(int length, int score, boolean strand, ArrayList<Feature> blocks) {
        this.length = length;
        this.score = score;
        this.strand = strand;
        this.blocks = blocks;
    }
    
    

    public ArrayList<Feature> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Feature> blocks) {
        this.blocks = blocks;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getStrand() {
        return strand;
    }

    public void setStrand(boolean strand) {
        this.strand = strand;
    }
 
    
}
