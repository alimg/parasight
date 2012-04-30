/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.State;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
abstract public class FileReader {
	abstract public int readFile(String path, State state);
}
