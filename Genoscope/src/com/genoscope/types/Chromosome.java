/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

import com.genoscope.reader.FileInfo;
import java.util.Vector;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public class Chromosome {

    private int length;
    private String name;
    private FileInfo sourceFile;
    private Vector<Feature> features;
    private int start;
    private int end;

    public Chromosome(int length, String name, FileInfo sourceFile) {
        this.length = length;
        this.name = name;
        this.sourceFile = sourceFile;
        this.features = new <Feature>Vector();
        start = 1000000000;
        end = 0;
    }

    public Chromosome() {
    }

    public void addFeature(Feature feature) {
        synchronized (this) {
            if (feature.getPosition() < start) {
                start = feature.getPosition();
            }
            if (feature.getClass().equals(Cytoband.class)) {
                int featureLength = ((Cytoband) feature).getLength();
                if (feature.getPosition() + featureLength > end) {
                    end = feature.getPosition() + featureLength;
                }
            }
            if (feature.getClass().equals(NormalFeature.class)) {
                int featureLength = ((NormalFeature) feature).getLength();
                if (feature.getPosition() + featureLength > end) {
                    end = feature.getPosition() + featureLength;
                }
            }
            if (feature.getClass().equals(ReadDepth.class)) {
                int featureLength = ((ReadDepth) feature).getLength();
                if (feature.getPosition() + featureLength > end) {
                    end = feature.getPosition() + featureLength;
                }
            }
            if (feature.getClass().equals(PSA.class)) {
                int featureLength = ((PSA) feature).getLength();
                if (feature.getPosition() + featureLength > end) {
                    end = feature.getPosition() + featureLength;
                }
            }
            length = end - start;
            features.add(feature);
        }
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Vector<Feature> getFeatures() {
        return features;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public FileInfo getSourceFile() {
        return sourceFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
