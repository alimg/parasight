/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

import java.util.Vector;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public class Chromosome {
    private int length;
    private int chrNo;
    private String name;
	private String sourceFile;
    private Vector<Feature> features;

    public Chromosome(int length, int chrNo, String name, String sourceFile) {
        this.length = length;
        this.chrNo = chrNo;
        this.name = name;
		this.sourceFile = sourceFile;
        this.features = new <Feature>Vector();
    }

    public Chromosome() {
            throw new UnsupportedOperationException("Not yet implemented");
    }

    public void addFeature(Feature feature){
        features.add(feature);
    }

    public Vector<Feature> getFeatures(){
        return features;
	}

    public int getChrNo() {
        return chrNo;
    }

    public void setChrNo(int chrNo) {
        this.chrNo = chrNo;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

	public String getSourceFile(){
		return sourceFile;
	}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
