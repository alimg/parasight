/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.State;
import com.genoscope.types.Chromosome;
import com.genoscope.types.NormalFeature;
import com.genoscope.types.Pair;
import com.genoscope.types.PairBlock;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
public class BEDPE_Reader extends FileReader {

	@Override
	public int readFile(String path, State state) {
		if (state.checkChromosome(path)) {
			final JPanel panel = new JPanel();
			JOptionPane.showMessageDialog(panel, "File already added",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return -2;
		}
		File file = new File(path);

		try {
			Scanner scanner;
			String line;
			String[] val;

			boolean chr1Added = false;
			boolean chr2Added = false;

			Chromosome chr1 = null;
			Chromosome chr2 = null;
			Chromosome tmpChr = null;

			String chr1Name = "";
			String chr2Name = "";

			NormalFeature feature1, feature2;
			PairBlock pairBlock = null;
			int length = 0;

			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {

				line = scanner.nextLine();
				if (line.replaceAll("\t", "").replaceAll(" ", "").length() == 0) {
					continue;
				} else {
					val = line.split("\t");

					if (!chr1Added || !chr1Name.equals(val[0])) {
						if (chr1Added && !chr1Name.equals(val[0])) {
							tmpChr = state.getChromosome(path, chr1Name);
							if (tmpChr == null) {
								System.out.println("Adding Chromosome to State: '" + chr1Name + "'");
								state.addChromosome(chr1);
							}
						}
						chr1Name = val[0];
						chr1 = state.getChromosome(path, chr1Name);
						if (chr1 == null) {
							chr1 = new Chromosome(0, chr1Name, path);
						}
						chr1Added = true;
					}

					if (!chr2Added || !chr2Name.equals(val[3])) {
						if (chr1Added && !chr1Name.equals(val[0])) {
							tmpChr = state.getChromosome(path, chr1Name);
							if (tmpChr == null) {
								System.out.println("Adding Chromosome to State: '" + chr2Name + "'");
								state.addChromosome(chr2);
							}
						}
						chr2Name = val[3];
						chr2 = state.getChromosome(path, chr2Name);
						if (chr2 == null) {
							chr2 = new Chromosome(0, chr2Name, path);
						}
						if(pairBlock != null) state.addBlockPair(pairBlock);
						pairBlock = new PairBlock(chr1, chr2);
						chr2Added = true;
					}

					length = Integer.parseInt(val[2]) - Integer.parseInt(val[1]);
					feature1 = new NormalFeature(length, Integer.parseInt(val[7]), val[8].equals("+"));
					feature1.setPosition(Integer.parseInt(val[1]));

					chr1.addFeature(feature1);

					length = Integer.parseInt(val[5]) - Integer.parseInt(val[4]);
					feature2 = new NormalFeature(length, Integer.parseInt(val[7]), val[9].equals("+"));
					feature2.setPosition(Integer.parseInt(val[4]));

					chr1.addFeature(feature2);

					pairBlock.addPair(new Pair(feature1, feature2));
				}
			}
			scanner.close();
			if (chr1 != null) {
				System.out.println("Adding Chromosome to State: '" + chr1Name + "'");
				state.addChromosome(chr1);
			}
			if (chr2 != null) {
				System.out.println("Adding Chromosome to State: '" + chr2Name + "'");
				state.addChromosome(chr2);
			}

			return 0;
		} catch (FileNotFoundException e) {
			System.out.println("File not found:" + path);
			return -1;
		}
	}
}
