/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.mouseactions;

import com.genoscope.renderer.visualizers.Visualizer;
import java.util.*;

/**
 * Move and snap
 * @author alim
 */
public  class MoveAction extends MouseActionHandler {
    private Vector<Visualizer> clients;
    private int x,y;
    Visualizer selected=null;
    
  
    public MoveAction(Vector<Visualizer> c)
    {
        System.out.println(" moveaction ");
        setVisualizers(c);
    }

    class MTreeMap extends TreeMap<Object, Object> {

        public Object put(Integer i) {
            if(containsKey(i))
                return super.put(i, ((Integer)get(i))+1);
            return super.put(i, 0);
        }

        @Override
        public Object remove(Object key) {
            if(containsKey(key))
                if(((Integer)get(key) )>0 )
                    return super.put(key, ((Integer)get(key))-1);
            return super.remove(key);
        }
        
    }
    MTreeMap arX=new <Integer,Integer>MTreeMap();
    MTreeMap arY=new <Integer,Integer>MTreeMap();
    
    /**
     * Set new visualizer list and update handler.
     * @param c list of visualizers
     */
    public void setVisualizers(Vector<Visualizer> c)
    {
        clients=c;
        update();
    }
    /**
     * update
     */
    public void update()
    {
        System.out.println(" update "+clients.size());
        arX.clear();
        arY.clear();
        
        for(Visualizer v:clients)
            if(v!=null)
            {
                System.out.println(" add ");
                arX.put(v.getX(),0);
                arX.put(v.getX()+v.getWidth(),0);
                arY.put(v.getY(),0);
                arY.put(v.getY()+v.getHeight(),0);
            }
    }
    @Override
    public void mouseDown() {
        selected=null;
        for(Visualizer c:clients)
            if(intersect(c, x, y))
                selected=c;
        if(selected!=null)
        {
            arX.remove(selected.getX());
            arX.remove(selected.getX()+selected.getWidth());
            arY.remove(selected.getY());
            arY.remove(selected.getY()+selected.getHeight());
            clients.remove(selected);
            clients.add(selected);
        }
        
    }

    @Override
    public void mouseUp() {
        if(selected!=null)
        {
            selected.setPosition(selected.getSnapX(), selected.getSnapY());
            arX.put(selected.getX());
            arX.put(selected.getX()+selected.getWidth());
            arY.put(selected.getY());
            arY.put(selected.getY()+selected.getHeight());
        }
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
            snap(selected,x,y,15);
        }
    }
    
    
    /**
     * 
     * @param v selected visualizer
     * @param x mouse x
     * @param y mouse y
     * @param r snapping distance
     */
    private void snap(Visualizer selected,int x,int y,int r)
    {
        if(arX.size()<1)return;
        
        final int sy=selected.getY();
        final int sye=sy+selected.getHeight();
        int a,b; 
        int aa,bb;
        Integer d,u;
        final int sx=selected.getX();
        final int sxe=sx+selected.getWidth();
        d=(Integer) arX.floorKey(sx);
        u=(Integer) arX.higherKey(sx);
        
        if(d==null)a=u;
        else if(u==null)a=d;
        else if(sx-d>u-sx)a=u;
        else a=d;
        
        d=(Integer) arX.floorKey(sxe);
        u=(Integer) arX.higherKey(sxe);
        if(d==null)b=u;
        else if(u==null)b=d;
        else if(sx-d>u-sx)b=u;
        else b=d;
        aa=Math.abs(a-sx);
        bb=Math.abs(b-sxe);
        if( aa<bb && aa<r)
            selected.setSnapX(a);
        else selected.setSnapX(sx);
        //snap Y
        
        d=(Integer) arY.floorKey(sy);
        u=(Integer) arY.higherKey(sy);
        
        if(d==null)a=u;
        else if(u==null)a=d;
        else if(sx-d>u-sx)a=u;
        else a=d;
        
        d=(Integer) arY.floorKey(sye);
        u=(Integer) arY.higherKey(sye);
        if(d==null)b=u;
        else if(u==null)b=d;
        else if(sx-d>u-sx)b=u;
        else b=d;
        aa=Math.abs(a-sy);
        bb=Math.abs(b-sye);
        if( aa<bb && aa<r)
            selected.setSnapY( a);
        else selected.setSnapY(sy);
    }
    
    
    private boolean intersect(Visualizer v, int x, int y) {
        if( x>v.getX() && x<v.getX()+v.getWidth() &&
                y>v.getY() && y<v.getY()+v.getHeight() )
            return true;
        return false;
    }

}
