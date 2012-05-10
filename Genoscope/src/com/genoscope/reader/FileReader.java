/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.State;

/**
 *
 * @author Furkan Mustafa Akdemir This abstract class implies a reader method
 * for extending class
 */
abstract public class FileReader {

	/**
	 * File reading method for a format which changes state
	 *
	 * @param path shows the path of file
	 * @param state current state of Genoscope
	 */
	abstract public int readFile(String path, State state);
}
