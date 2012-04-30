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
 */
public class CBReader extends FileReader{

	@Override
	public Chromosome readFile(String path, State state) {
		try {
			if(state.checkChromosome(path))
			{
				final JPanel panel = new JPanel();
				JOptionPane.showMessageDialog(panel, "File already added",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return null;
			}
			
			File file = new File(path);

			Scanner scanner;
			String line, chrName="";
			String[] val;
			Chromosome chr = null;
			Cytoband cytoband;
			int length = 0;
 
			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {

				line = scanner.nextLine();
				val=line.split("\t");

				if(chr == null || !chrName.equals(val[0]))
				{
					chrName = val[0];
					System.out.println("Adding Chromosome to State: '" + chrName + "'");
					chr = new Chromosome(0, chrName, path);
					state.addChromosome(chr);
				}

				length = Integer.parseInt(val[2])-Integer.parseInt(val[1]);
				cytoband = new Cytoband(val[3], Integer.parseInt(val[1]),length, val[4]);

				chr.addFeature(cytoband);
			}
			return null;
		} catch (FileNotFoundException ex) {
			Logger.getLogger(CBReader.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
}
