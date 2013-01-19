/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public class Cytoband extends Feature {
    public static final int GNEG = 0;
    public static final int GPOS25 = 1;
    public static final int GPOS50 = 2;
    public static final int GPOS75 = 3;
    public static final int GPOS100 = 4;
    public static final int ACEN = 5;
    public static final int GVAR = 6;
    public static final int STALK = 7;
    
	protected int length;
	protected int gieStain;
    
	public Cytoband(String name, int position, int length, String gieStain) {
		super(name, position);
		this.length = length;
        switch(gieStain)
        {
        case "gneg":
            this.gieStain = GNEG;
            break;
        case "gpos50":
            this.gieStain = GPOS50;
            break;
        case "gpos75":
            this.gieStain = GPOS75;
            break;
        case "gpos100":
            this.gieStain = GPOS100;
            break;
        case "gpos25":
            this.gieStain = GPOS25;
            break;
        case "acen":
            this.gieStain = ACEN;
            break;
        case "gvar":
            this.gieStain = GVAR;
            break;
        case "stalk":
            this.gieStain = STALK;
            break;
        }
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

    public int getGieStain() {
        return gieStain;
    }
}
