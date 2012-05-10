/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.State;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir
 * This class is the main file reading class which decides to format reader and
 * calls this format reader depending on the extension with a static method
 */
public class Reader {

	/**
	 * This method checks the extension of file and depending on the extension,
	 * calls corresponding format reader
	 * @param path path of the genome annotation data file
	 * @param state current state of genoscope
	 */
	static public int readFile(String path, State state) {
		final JPanel panel = new JPanel();
		//variables for extension check
		String extension;
		int lastInd;

		//get extension
		lastInd = path.lastIndexOf('.');
		extension = path.substring(lastInd + 1);
		System.out.println("File type is: " + extension);

		switch (extension) {
			case "bed":
				System.out.println("reading: " + path);
				System.out.println("------------ Read Start --------------\n");
				new BED_Reader().readFile(path, state);
				System.out.println("------------ Read Complete -----------\n");
				return 0;
			case "cb":
				System.out.println("reading: " + path);
				System.out.println("------------ Read Start --------------\n");
				new CB_Reader().readFile(path, state);
				System.out.println("------------ Read Complete -----------\n");
				return 0;
			case "rd":
				System.out.println("reading: " + path);
				System.out.println("------------ Read Start --------------\n");
				new RD_Reader().readFile(path, state);
				System.out.println("------------ Read Complete -----------\n");
				return 0;
			case "cn":
				System.out.println("reading: " + path);
				System.out.println("------------ Read Start --------------\n");
				JOptionPane.showMessageDialog(panel, "This extension will be added: " + extension,
						"Warning", JOptionPane.WARNING_MESSAGE);
				System.out.println("------------ Read Complete -----------\n");
				return 0;
		}

		JOptionPane.showMessageDialog(panel, "Unsupported file extension: " + extension,
				"Warning", JOptionPane.WARNING_MESSAGE);
		return -1;
	}
}
