/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.mouseactions;

import com.genoscope.renderer.visualizers.Visualizer;
import java.util.Vector;

/**
 * Replace the visualizers. 
 * @author alim
 */
public  class MoveAction implements MouseActionHandler {
    Vector<Visualizer> clients;
    private int x,y;
    Visualizer selected=null;
    public MoveAction(Vector<Visualizer> c)
    {
        clients=c;
    }

    @Override
    public void mouseDown() {
        selected=null;
        for(Visualizer c:clients)
            if(intersect(c, x, y))
                selected=c;
        if(selected!=null)
        {
            clients.remove(selected);
            clients.add(selected);
        }
        
    }

    @Override
    public void mouseUp() {
        selected=null;
    }

    @Override
    public void mouseMove(int x, int y, int buttons) {
        int dx=x-this.x;
        int dy=y-this.y;
        this.x=x;
        this.y=y;
        if(selected!=null)
        {
            selected.setPosition(selected.getX()+dx, selected.getY()+dy);
        }
            
    }
    
    private boolean intersect(Visualizer v, int x, int y) {
        if( x>v.getX() && x<v.getX()+v.getWidht() &&
                y>v.getY() && y<v.getY()+v.getHeight() )
            return true;
        return false;
    }

}
