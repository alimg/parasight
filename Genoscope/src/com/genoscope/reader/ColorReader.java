/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.ColorPicker;
import com.genoscope.types.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Furkan Mustafa Akdemir
 * Color reader for ColorPicker class which adds Colors with names
 */
public class ColorReader {
	public static void readFile(String path){
		File config = new File(path);
		String line;
		String[] val;
		Color cl;
		Scanner scan;
		try {
			scan = new Scanner(config);
			while(scan.hasNextLine()){
				line = scan.nextLine();
				if(line.replaceAll(" ","").replaceAll("\t","").length()==0)
					continue;
				val = line.split("\t");
				cl = new Color(val[0],val[1],Integer.parseInt(val[2])
						,Integer.parseInt(val[3]),Integer.parseInt(val[4]));
				ColorPicker.addColor(cl);
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(ColorReader.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
