/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.State;
import com.genoscope.types.Chromosome;
import com.genoscope.types.Cytoband;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir
 * This class reads a BED type genome annotation data and adds this data to state
 * as a Chromosome object
 */
public class CBReader extends FileReader {

	/**
	 * File reading method for Cytoband format which generates a chromosome and adds to state
	 * @param path shows the path of Cytoband file
	 * @param state current state of Genoscope
	 */
	@Override
	public int readFile(String path, State state) {
		try {
			if (state.checkChromosome(path)) {
				final JPanel panel = new JPanel();
				JOptionPane.showMessageDialog(panel, "File already added",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return -2;
			}

			File file = new File(path);

			Scanner scanner;
			String line, chrName = "";
			String[] val;
			Chromosome chr = null;
			Cytoband cytoband;
			int length = 0;

			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {

				line = scanner.nextLine();
				val = line.split("\t");
				if (line.replaceAll("\t", "").replaceAll(" ", "").length() == 0
						|| val.length < 5) {
					continue;
				}

				if (chr == null || !chrName.equals(val[0])) {
					chrName = val[0];
					System.out.println("Adding Chromosome to State: '" + chrName + "'");

					if(chr != null)
						state.addChromosome(chr);
					chr = new Chromosome(0, chrName, path);
				}

				length = Integer.parseInt(val[2]) - Integer.parseInt(val[1]);
				cytoband = new Cytoband(val[3], Integer.parseInt(val[1]), length, val[4]);

				chr.addFeature(cytoband);
			}
			scanner.close();
			if(chr != null)
				state.addChromosome(chr);

			return 0;
		} catch (FileNotFoundException ex) {
			Logger.getLogger(CBReader.class.getName()).log(Level.SEVERE, null, ex);
			return -1;
		}
	}
}
