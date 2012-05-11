/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.ColorPicker;
import com.genoscope.State;
import com.genoscope.types.Chromosome;
import com.genoscope.types.Color;
import com.genoscope.types.PSA;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
public class PSA_Reader extends FileReader {

	@Override
	public int readFile(String path, State state_) {
		if (state_.checkChromosome(path)) {
			final JPanel panel = new JPanel();
			JOptionPane.showMessageDialog(panel, "File already added",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return -2;
		}
		State state = new State() {

			@Override
			public void addChromosome(Chromosome chr) {
				this.getChromosomeList().add(chr);
			}
		};
		File file = new File(path);

		try {
			Scanner scanner;
			String line;
			String[] val;
			boolean header = true;
			Chromosome chr = null;
			PSA feature;
			String chrName = "";
			Color cl;
			int length = 0;

			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {

				line = scanner.nextLine();
				if (line.replaceAll("\t", "").replaceAll(" ", "").length() == 0) {
					continue;
				}
				if (header == true) {
					header = false;
				} else {
					val = line.split("\t");

					if (chr == null || !chrName.equals(val[0])) {
						chrName = val[0];
						chr = state.getChromosome(path, chrName);
						if (chr == null) {
							System.out.println("Adding Chromosome to State: '" + chrName + "'");
							chr = new Chromosome(0, chrName, path);
							state.addChromosome(chr);
						}
					}

					length = Integer.parseInt(val[7]);
					cl = ColorPicker.getColor(val[3]);
					feature = new PSA(chrName, Integer.parseInt(val[1]), length,
							Integer.parseInt(val[5]), cl);

					chr.addFeature(feature);
				}
			}
			scanner.close();
			state.clone(state_);
			return 0;
		} catch (FileNotFoundException e) {
			System.out.println("File not found:" + path);
			return -1;
		}

	}
}