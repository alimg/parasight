/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
public class Reader {
	static public int readFile(String path){
		 final JPanel panel = new JPanel();
		//variables for extension check
		String extension;
		int lastInd;
		
		//get extension
		lastInd=path.lastIndexOf('.');
		extension=path.substring(lastInd+1);
		System.out.println(extension);
		switch (extension) {
			case "bed":
				return 0;//new BED().readFile(path);//return chromosome object generated by BED
			case "bla":
				return 0;//null;
		}
		JOptionPane.showMessageDialog(panel, "Unsupported file extension:"+path,
				"Warning", JOptionPane.WARNING_MESSAGE);
		return 0;//null;
	}
}
