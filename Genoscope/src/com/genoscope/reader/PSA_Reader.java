/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.ColorPicker;
import com.genoscope.AppState;
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

    PSA_Reader(String path, AppState state) {
        super(path, state);
    }

	@Override
	public int readFile() {
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
        
        FileInfo fInfo = new FileInfo(mpPath, PSA_Reader.class);

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
						chr = state.getChromosome( chrName, fInfo);
						if (chr == null) {
							System.out.println("Adding Chromosome to State: '" + chrName + "'");
							chr = new Chromosome(0, chrName, fInfo);
							state.addChromosome(chr);
						}
					}

					length = Integer.parseInt(val[7]);
					cl = ColorPicker.getColor(val[3]);
					feature = new PSA(chrName, Integer.parseInt(val[1]), length,
							Integer.parseInt(val[5]), cl , val[6]);

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
