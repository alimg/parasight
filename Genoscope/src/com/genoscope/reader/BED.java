/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.types.Chromosome;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
public class BED extends BasicFormat{

	@Override
	public Chromosome readFile(String path) {
		File file = new File(path);

		try {
			Scanner scanner = new Scanner(file);
			for(int i=0;i<3;i++)
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
			}
			return null;
		} catch (FileNotFoundException e) {
			System.out.println("File not found:"+path);
			return null;
		}
	}
	
}
