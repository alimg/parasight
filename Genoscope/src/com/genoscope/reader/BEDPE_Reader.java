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
 * @author Furkan Mustafa Akdemir This class reads a BEDPE type pairing genome
 * annotation data and adds this data to state as a PairBlock and two Chromosome
 * objects
 */
public class BEDPE_Reader extends FileReader {

	/**
	 * File reading method for BEDPE format which generates two chromosomes and
	 * their pairing data and adds to state
	 *
	 * @param path shows the path of BEDPE file
	 * @param state current state of Genoscope
	 */
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

			@Override
			public void addPairBlock(PairBlock pairBlock){
				this.getPairBlockList().add(pairBlock);
			}
		};
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

					if (chr1 == null || !chr1Name.equals(val[0])) {
						chr1Name = val[0];
						tmpChr = state.getChromosome(path, chr1Name);
						if (tmpChr == null) {
							System.out.println("\n1-Adding Chromosome to State: '" + chr1Name + "'");
							chr1 = new Chromosome(0, chr1Name, path);
							state.addChromosome(chr1);
						} else {
							chr1 = tmpChr;
						}
					}

					if (chr2 == null || !chr2Name.equals(val[3])) {
						if (chr2 != null && !chr2Name.equals(val[3])) {
							System.out.println("\nAdding Pair Block to State:\n\t"
									+ pairBlock.getFirst().getName() + " and " + pairBlock.getSecond().getName());
							if (pairBlock != null) {
								state.addPairBlock(pairBlock);
							}
						}

						chr2Name = val[3];
						tmpChr = state.getChromosome(path, chr2Name);
						if (tmpChr == null) {
							System.out.println("\n2-Adding Chromosome to State: '" + chr2Name + "'");
							chr2 = new Chromosome(0, chr2Name, path);
							state.addChromosome(chr2);
						} else {
							chr2 = tmpChr;
						}

						pairBlock = new PairBlock(chr1, chr2);
					}

					length = Integer.parseInt(val[2]) - Integer.parseInt(val[1]);
					feature1 = new NormalFeature(length, Integer.parseInt(val[7]), val[8].equals("+"));
					feature1.setPosition(Integer.parseInt(val[1]));

					chr1.addFeature(feature1);

					length = Integer.parseInt(val[5]) - Integer.parseInt(val[4]);
					feature2 = new NormalFeature(length, Integer.parseInt(val[7]), val[9].equals("+"));
					feature2.setPosition(Integer.parseInt(val[4]));

					chr2.addFeature(feature2);

					pairBlock.addPair(new Pair(feature1, feature2));
				}
			}

			scanner.close();
			if (pairBlock != null) {
				System.out.println("\nAdding Pair Block to State:\n\t"
						+ pairBlock.getFirst().getName() + " and " + pairBlock.getSecond().getName());
				state.addPairBlock(pairBlock);
			}
			state.clone(state_);
			return 0;
		} catch (FileNotFoundException e) {
			System.out.println("File not found:" + path);
			return -1;
		}
	}
}
