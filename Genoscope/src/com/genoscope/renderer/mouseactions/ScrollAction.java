/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.mouseactions;

import com.genoscope.Genoscope;
import com.genoscope.renderer.GenoscopeRenderer.ViewConfig;
import java.awt.Cursor;

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
        Genoscope.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseUp() {
        state=0;
        Genoscope.canvas.setCursor(Cursor.getDefaultCursor());
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
