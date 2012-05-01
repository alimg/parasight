/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.State;
import com.genoscope.types.Chromosome;
import com.genoscope.types.NormalFeature;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
public class BEDReader extends FileReader {
	
	@Override
	public Chromosome readFile(String path, State state) {
		if (state.checkChromosome(path)) {
			final JPanel panel = new JPanel();
			JOptionPane.showMessageDialog(panel, "File already added",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		File file = new File(path);
		
		try {
			Scanner scanner;
			String line;
			String[] val;
			boolean header = true;
			boolean chromosomeAdded = false;
			Chromosome chr = null;
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
					val = line.split(" ");
					if (val[0].equals("track")) {
						header = false;
					}
				} else {
					val = line.split("\t");
					
					if (!chromosomeAdded) {
						chrName = val[0];
						System.out.println("Adding Chromosome to State: '" + chrName + "'");
						
						chr = new Chromosome(0, chrName, path);
						chromosomeAdded = true;
					}
					
					length = Integer.parseInt(val[2]) - Integer.parseInt(val[1]);
					feature = new NormalFeature(length, -1, val[5].equals("+"));
					feature.setPosition(Integer.parseInt(val[1]));
					
					chr.addFeature(feature);
				}
			}
			scanner.close();
			if(chr != null)
				state.addChromosome(chr);

			return null;
		} catch (FileNotFoundException e) {
			System.out.println("File not found:" + path);
			return null;
		}
	}
}
