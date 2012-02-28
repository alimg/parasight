/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.genoscope.types;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public class ReadDepth extends Feature {
    protected int length;
    protected int score;

    public ReadDepth(int length, int score) {
        this.length = length;
        this.score = score;
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
    
}
