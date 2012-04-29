/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

import com.genoscope.types.Chromosome;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
abstract public class BasicFormat {
	protected Chromosome Chr;
        
	abstract public Chromosome readFile(String path);
}
