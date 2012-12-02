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
 * @author Furkan Mustafa Akdemir This abstract class implies a reader method
 * for extending class
 */
abstract public class FileReader extends Thread {
    public static final int READ_SUCCESS=0;
    public static final int READ_ERROR_EXCEPTION=-1;
    public static final int READ_ERROR_ALREADY_EXISTS=-2;
    /**
    * File reading method for a format which changes state
    *
    * @param path shows the path of file
    * @param state current state of Genoscope
    */
    protected AppState mpState;
    protected String mpPath;
    
    
    public FileReader(String path, AppState state)
    {
        this.mpState=state;
        this.mpPath=path;
    }

    @Override
    public void run() {
        System.out.println("------------ Read Start --------------\n");
        int result = readFile();
        System.out.println("------------ Read Complete --------------\n");
        
        Reader.onReadComplete(result);
    }

    
    abstract protected int readFile();
}
