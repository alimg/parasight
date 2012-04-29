/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.mouseactions;

/**
 *
 * @author alim
 */
public abstract class MouseActionHandler {
    
    abstract public void mouseDown();
    abstract public void mouseUp();
    abstract public void mouseMove(int x,int y,int buttons);
    abstract public void update();
    
}
