/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.visualizers;

import com.genoscope.types.*;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Ahmet
 */
public class PSAVisualizer extends ChromosomeVisualizer {

	private final int BIG = 2000000000;

	public PSAVisualizer(int w, int h, Chromosome chr) {
		super(w, h, chr);
	}

	@Override
	public void draw() {
		glDisable(GL_TEXTURE_2D);
		glClearColor(1, 1, 1, 0);
		glClear(GL_COLOR_BUFFER_BIT);
		float h = getHeight() / 4.0f;
		float w = getWidth();


		glLineWidth(5.0f);
		glColor4f(0, 0, 0, 0.8f);
		glBegin(GL_LINES);
		glVertex2f(0.0f, h);
		glVertex2f(w, h);
		glEnd();

		glEnable(GL_TEXTURE_2D);
		font.drawString(20, getHeight() - 15, (chromosome.getName() + " (" + chromosome.getStart() + ":" + chromosome.getEnd() + ")"), 1, 1);
		glDisable(GL_TEXTURE_2D);

		synchronized (chromosome) {
			for (Feature i : chromosome.getFeatures()) {
				PSA f = (PSA) i;
				Color c = f.getColor();

				glBegin(GL_QUADS);

				glColor4f((float) c.getR() / 255, (float) c.getG() / 255, (float) c.getB() / 255, 0.8f);



				glVertex2f(getPositionX(f.getPosition()), h);

				glVertex2f(getPositionX(f.getPosition() + f.getLength()), h);

				glVertex2f(getPositionX(f.getPosition() + f.getLength()), h + f.getHeight());

				glVertex2f(getPositionX(f.getPosition()), h + f.getHeight());

				glEnd();

			}
		}

	}
}
