/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

import java.util.Vector;

/**
 *
 * @author Furkan Mustafa Akdemir
 */
public class PairBlock {
	private Vector<Pair> pairings;
	private Chromosome chromosome1;
	private Chromosome chromosome2;

	public PairBlock(Chromosome chr1, Chromosome chr2){
		chromosome1 = chr1;
		chromosome2 = chr2;
		pairings = new <Pair>Vector();
	}

	public void addPair(Pair pair){
		pairings.add(pair);
	}
}
