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
    private String name;
    private String sourceFile;
    private Vector<Feature> features;

    public Chromosome(int length, String name, String sourceFile) {
        this.length = length;
        this.name = name;
	this.sourceFile = sourceFile;
        this.features = new <Feature>Vector();
    }

    public Chromosome() {
        
    }

    public void addFeature(Feature feature){
        if(feature.getPosition() > length)
            length = feature.getPosition();
        if(feature.getClass().equals(Cytoband.class)){
            int featureLength = ((Cytoband)feature).getLength();
            if(feature.getPosition() + featureLength > length)
                length = feature.getPosition()+featureLength;
        }
        features.add(feature);
    }

    public Vector<Feature> getFeatures(){
        return features;
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
