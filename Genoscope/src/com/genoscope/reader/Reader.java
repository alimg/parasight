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
 */
public class Reader {
	static public int readFile(String path,State state){
		 final JPanel panel = new JPanel();
		//variables for extension check
		String extension;
		int lastInd;
 
		//get extension
		lastInd=path.lastIndexOf('.');
		extension=path.substring(lastInd+1);
		System.out.println("File type is: "+extension);

		switch (extension) {
			case "bed":
				System.out.println("reading: "+path+"\n------------");
				new BEDReader().readFile(path, state);
				return 0;
			case "cb":
			JOptionPane.showMessageDialog(panel, "This extension will be added: "+extension,
					"Warning", JOptionPane.WARNING_MESSAGE);
				System.out.println("reading: "+path);
				return 0;
			case "cn":
				System.out.println("reading: "+path);
				return 0;
		}

		JOptionPane.showMessageDialog(panel, "Unsupported file extension: "+extension,
				"Warning", JOptionPane.WARNING_MESSAGE);
		return -1;
	}
}
