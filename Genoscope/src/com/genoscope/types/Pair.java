/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

/**
 *
 * @author PC
 */
public class Pair {
	private NormalFeature firstFeature;
	private NormalFeature secondFeature;

	public Pair(NormalFeature first, NormalFeature second) {
		firstFeature = first;
		secondFeature = second;
	}

	public Pair() {
	}

	public NormalFeature getFirst() {
		return firstFeature;
	}

	public NormalFeature getSecond() {
		return secondFeature;
	}
}
