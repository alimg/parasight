/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.mouseactions;

import com.genoscope.renderer.GenoscopeRenderer.ViewConfig;

/**
 *
 * @author alim
 */
public class ScrollAction extends MouseActionHandler{
    ViewConfig view;
    private int x;
    private int y;

    public ScrollAction(ViewConfig conf) {
        view=conf;
    }

    int state=0;
    
    @Override
    public void mouseDown() {
        state=1;
    }

    @Override
    public void mouseUp() {
        state=0;
    }

    @Override
    public void mouseMove(int x, int y, int buttons) {
        final int dx=x-this.x;
        final int dy=y-this.y;
        this.x=x;
        this.y=y;
        if(state==1)
        {
            view.setPos(view.pos[0]+dx,view.pos[1]+dy );
            
        }
    }

    @Override
    public void update() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
