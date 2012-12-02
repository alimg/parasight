/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.AppState;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Furkan Mustafa Akdemir This class is the main file reading class
 * which decides to format reader and calls this format reader depending on the
 * extension with a static method
 */
public class Reader {

    /**
     * This method checks the extension of file and depending on the extension,
     * calls corresponding format reader
     *
     * @param path path of the genome annotation data file
     * @param state current state of genoscope
     */
    static public int readFile(String path, AppState state) {
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
                new BED_Reader(path,state).start();
                return 0;
            case "cb":
                System.out.println("reading: " + path);
                new CB_Reader(path, state).start();
                return 0;
            case "rd":
                System.out.println("reading: " + path);
                new RD_Reader(path, state).start();
                return 0;
            case "bedpe":
                System.out.println("reading: " + path);
                new BEDPE_Reader(path, state).start();
                return 0;
            case "psa":
                System.out.println("reading: " + path);
                new PSA_Reader(path, state).start();
                return 0;
            case "cn":
                System.out.println("reading: " + path);
                JOptionPane.showMessageDialog(panel, "This extension will be added: " + extension,
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return 0;
        }

        JOptionPane.showMessageDialog(panel, "Unsupported file extension: " + extension,
                "Warning", JOptionPane.WARNING_MESSAGE);
        return -1;
    }

    public static void onReadComplete(int result) {
    }
}
