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

/**
 *
 * @author Furkan Mustafa Akdemir
 */
public class BEDReader extends FileReader{

	@Override
	public Chromosome readFile(String path, State state) {
		File file = new File(path);

		try {
			Scanner scanner, lineScanner;
			String line, desc;
			String[] val;
			boolean header = true;
			boolean chromosomeAdded = false;
			Chromosome chr = null;
			NormalFeature feature;
			int chrNo = -1;

			scanner = new Scanner(file);
			desc = "";

			while (scanner.hasNextLine()) {

				line = scanner.nextLine();
				val=line.split(" ");
				if(header == true){
					if(val[0].equals("track"))
						header = false;
				}else{
					val = line.split("	");

					if(!chromosomeAdded)
					{
						System.out.println("Adding Chromosome to State: '"+val[0].substring(3)+"'");

						chrNo = Integer.parseInt(val[0].substring(3));
						chr = new Chromosome(1000000000, chrNo, val[0], path);
						state.addChromosome(chr);
						chromosomeAdded=true;
					}
					
					feature = new NormalFeature(1000000000,-1,val[5].equals("+"));
					feature.setPosition(Integer.parseInt(val[1]));
					feature.setLength(Integer.parseInt(val[2])-Integer.parseInt(val[1]));

					chr.addFeature(feature);
				}

			}
			return null;
		} catch (FileNotFoundException e) {
			System.out.println("File not found:"+path);
			return null;
		}
	}
}
