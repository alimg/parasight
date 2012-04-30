/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.types;

/**
 *
 * @author Ahmet Kerim SENOL
 */
public abstract class Feature {

	protected String name;
	protected String source; //formatlardan birinde hangi programdan geldigini belirtmek icin kullaniliyor.
	protected int position; //position on chromosome

	public Feature(String name, int position) {
		this.name = name;
		this.source = "";
		this.position = position;
	}

	public Feature() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
