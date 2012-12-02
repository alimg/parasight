/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.AppState;
import com.genoscope.types.Chromosome;
import com.genoscope.types.ReadDepth;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir This class reads a Read Depth type genome
 * annotation data and adds this data to state as a Chromosome object
 */
public class RD_Reader extends FileReader {

    RD_Reader(String path, AppState state) {
        super(path, state);
    }

	/**
	 * File reading method for Read Depth format which generates a chromosome
	 * and adds to state
	 *
	 * @param path shows the path of Read Depth file
	 * @param state current state of Genoscope
	 */
	@Override
	public int readFile() {
		try {
			if (mpState.checkChromosome(mpPath)) {
				final JPanel panel = new JPanel();
				JOptionPane.showMessageDialog(panel, "File already added",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return READ_ERROR_ALREADY_EXISTS;
			}
			AppState state = new AppState() {

				@Override
				public void addChromosome(Chromosome chr) {
					this.getChromosomeList().add(chr);
				}
			};
			File file = new File(mpPath);

			Scanner scanner;
			String line, chrName = "";
			String[] val;
			Chromosome chr = null;
			ReadDepth readDepth;
			int length = 0;

			scanner = new Scanner(file);
            
            FileInfo fInfo = new FileInfo(mpPath, RD_Reader.class);

			while (scanner.hasNextLine()) {

				line = scanner.nextLine();
				val = line.split("\t");
				if (line.replaceAll("\t", "").replaceAll(" ", "").length() == 0
						|| val.length < 4) {
					continue;
				}

				if (chr == null || !chrName.equals(val[0])) {
					chrName = val[0];
					System.out.println("Adding Chromosome to State: '" + chrName + "'");

					if (chr != null) {
						state.addChromosome(chr);
					}

					chr = new Chromosome(0, chrName, fInfo);
				}

				length = Integer.parseInt(val[2]) - Integer.parseInt(val[1]);
				readDepth = new ReadDepth(length, Integer.parseInt(val[3]));
				readDepth.setPosition(Integer.parseInt(val[1]));

				chr.addFeature(readDepth);
			}
			scanner.close();
			if (chr != null) {
				state.addChromosome(chr);
			}
			state.inject(mpState);
			return 0;
		} catch (FileNotFoundException ex) {
			Logger.getLogger(RD_Reader.class.getName()).log(Level.SEVERE, null, ex);
		}
		return -1;
	}
}
