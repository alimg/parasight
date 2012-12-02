/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.AppState;
import com.genoscope.types.Chromosome;
import com.genoscope.types.NormalFeature;
import com.genoscope.types.PairBlock;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir This class reads a BED type genome annotation
 * data and adds this data to state as a Chromosome object
 */
public class BED_Reader extends FileReader {

    BED_Reader(String path, AppState state) {
        super(path, state);
    }

    /**
     * File reading method for BED format
     */
    @Override
    public int readFile() {
        if (mpState.checkChromosome(mpPath)) {
            return READ_ERROR_ALREADY_EXISTS;
        }
        
        AppState state=new AppState(){

            @Override
            public void addChromosome(Chromosome chr) {
                this.getChromosomeList().add(chr);
            }
            
        };

        File file = new File(mpPath);

        try {
            Scanner scanner;
            String line;
            String[] val;
            boolean header = true;
            Chromosome chr=null;
            FileInfo fInfo=new FileInfo(mpPath,BED_Reader.class);
            NormalFeature feature;
            String chrName;
            int length = 0;

            scanner = new Scanner(file);

            while (scanner.hasNextLine()) {

                line = scanner.nextLine();
                if (line.replaceAll("\t", "").replaceAll(" ", "").length() == 0) {
                    continue;
                }
                if (header == true) {
                    System.out.println("Header: "+line);
                    val = line.split(" ");
                    if (val[0].equals("track")) {
                        header = false;
                        fInfo.header=val;
                    }
                    val = line.split("\t");
                    if (val[0].equals("chrom")) {
                        header = false;
                        fInfo.header=val;
                    }
                    
                } else {
                    val = line.split("\t");
                    
                    
                    if (chr==null ) {
                        chrName = val[0];
                        System.out.println("Adding Chromosome to State: '" + chrName + "'");

                        chr = new Chromosome(0, chrName, fInfo);
                        state.addChromosome(chr);
                    }
                    
                    if(!chr.getName().equals(val[0]))
                    {
                        chrName = val[0];
                        chr = state.getChromosome(val[0], fInfo);
                        if(chr==null)
                        {
                            chr = new Chromosome(0, chrName, fInfo);
                            System.out.println("Adding Chromosome to State: '" + chrName + "'");
                            state.addChromosome(chr);
                        }
                            
                    }
                    
                    length = Integer.parseInt(val[2]) - Integer.parseInt(val[1]);
                    feature = new NormalFeature(length, -1, val[5].equals("+"));
                    feature.setPosition(Integer.parseInt(val[1]));

                    chr.addFeature(feature);
                }
            }
            scanner.close();
            
            state.inject(mpState);
            return 0;
        } catch (FileNotFoundException e) {
            System.out.println("File not found:" + mpPath);
            return READ_ERROR_EXCEPTION;
        }
    }
}
