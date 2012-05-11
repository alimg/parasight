/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import java.io.File;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
public class ColorReader {
	public static void readFile(String path){
		File f=new File(".");
		System.out.println(f.getAbsolutePath());
		final JPanel panel = new JPanel();
			JOptionPane.showMessageDialog(panel, "'"+f.getAbsolutePath()+"'",
					"Warning", JOptionPane.WARNING_MESSAGE);
/*		Scanner scan = new Scanner(new File("resources/color.cl"));
		scan.nextLine();
*/	}
}
