/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.mouseactions;

import com.genoscope.Genoscope;
import com.genoscope.renderer.visualizers.Visualizer;
import java.awt.Cursor;
import java.util.*;

/**
 * Move, resize and snap
 * @author alim
 */
public  class MoveAction extends MouseActionHandler {
    public final static int NONE=0;
    public final static int MOVING=1;
    public final static int RESIZING=2;
    public final static int RESIZE_DIST=10;
    public final static int SNAP_DIST=15;
    public final static int NW=Cursor.NW_RESIZE_CURSOR;
    public final static int NE=Cursor.NE_RESIZE_CURSOR;
    public final static int SE=Cursor.SE_RESIZE_CURSOR;
    public final static int SW=Cursor.SW_RESIZE_CURSOR;
    public final static int N=Cursor.N_RESIZE_CURSOR;
    public final static int E=Cursor.E_RESIZE_CURSOR;
    public final static int S=Cursor.S_RESIZE_CURSOR;
    public final static int W=Cursor.W_RESIZE_CURSOR;
    public final static int MIDDLE=0;
    public final static int AAR[][]={   {NW,N,NE},
                                        {W,MIDDLE,E},
                                        {SW,S,SE}};
    
    private Vector<Visualizer> clients;
    private int x,y;
    private int action=0;
    private int resizeW;
    private int resizeDirX,resizeDirY;
    
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
            int a=getArea(selected,x,y);
            
            arX.remove(selected.getX());
            arX.remove(selected.getX()+selected.getWidth());
            arY.remove(selected.getY());
            arY.remove(selected.getY()+selected.getHeight());
            synchronized(clients)
            {
                clients.remove(selected);
                clients.add(selected);
            }
            if(a==MIDDLE)
            {
                action=MOVING;
                Genoscope.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
            else {
                action=RESIZING;
            }
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
            if(action==MOVING)
            {
                Genoscope.canvas.setCursor(Cursor.getDefaultCursor());
                action=NONE;
            }
            else if(action==RESIZING) {
                action=NONE;
                selected.doneResizing();
            }
        }
        selected=null;
    }

    @Override
    public void mouseMove(int x, int y, int buttons) {
        final int dx=x-this.x;
        final int dy=y-this.y;
        int t,f;
        this.x=x;
        this.y=y;
        switch (action)
        {
            case NONE:
                
                f=0;
                for(Visualizer c:clients)
                {
                    t=getArea(c, x, y);
                    if(t>0)
                    {
                        Genoscope.canvas.setCursor(Cursor.getPredefinedCursor(t));
                        resizeW=t;
                        f=1;
                    }
                }
                if(f==0)
                {
                    resizeW=0;
                    Genoscope.canvas.setCursor(Cursor.getDefaultCursor());
                }
                break;
            case MOVING:
                if(selected!=null)
                {
                    selected.setPosition(selected.getX()+dx, selected.getY()+dy);
                    snap(selected);
                }
                break;
            case RESIZING:
                if(selected!=null)
                {
                    switch(resizeW)
                    {
                    case N:
                        selected.setPosition(selected.getX(), selected.getY()+dy);
                        selected.setSize(selected.getWidth(), selected.getHeight()-dy);
                        snap(selected);
                        break;
                    case S:
                        selected.setSize(selected.getWidth(), selected.getHeight()+dy);
                        break;
                    case W:
                        selected.setPosition(selected.getX()+dx, selected.getY());
                        selected.setSize(selected.getWidth()-dx, selected.getHeight());
                        snap(selected);
                        break;
                    case E:
                        selected.setSize(selected.getWidth()+dx, selected.getHeight());
                        break;
                    case NE:
                        selected.setPosition(selected.getX(), selected.getY()+dy);
                        selected.setSize(selected.getWidth()+dx, selected.getHeight()-dy);
                        snap(selected);
                        break;
                    case NW:
                        selected.setPosition(selected.getX()+dx, selected.getY()+dy);
                        selected.setSize(selected.getWidth()-dx, selected.getHeight()-dy);
                        break;
                    case SE:
                        selected.setSize(selected.getWidth()+dx, selected.getHeight()+dy);
                        snap(selected);
                        break;
                    case SW:
                        selected.setPosition(selected.getX()+dx, selected.getY());
                        selected.setSize(selected.getWidth()-dx, selected.getHeight()+dy);
                        break;
                    }
                }
                break;
        }
    }
    
    
    /**
     * 
     * @param v selected visualizer
     * @param x mouse x
     * @param y mouse y
     * @param r snapping distance
     */
    private void snap(Visualizer selected)
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
        if( aa<bb && aa<SNAP_DIST)
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
        if( aa<bb && aa<SNAP_DIST)
            selected.setSnapY( a);
        else selected.setSnapY(sy);
    }
    
    private int getArea(Visualizer v,int x,int y)
    {
        int a=1,b=1;
        x-=v.getX();
        y-=v.getY();
        if(x<0||x>v.getWidth()||y<0||y>v.getHeight())
            return -1;
        if(x<=RESIZE_DIST)
            a=0;
        else if(v.getWidth()-x<=RESIZE_DIST)
            a=2;
        if(y<=RESIZE_DIST)
            b=0;
        else if(v.getHeight()-y<=RESIZE_DIST)
            b=2;
        
        return AAR[b][a];
    }
    
    private boolean intersect(Visualizer v, int x, int y) {
        if( x>v.getX() && x<v.getX()+v.getWidth() &&
                y>v.getY() && y<v.getY()+v.getHeight() )
            return true;
        return false;
    }

}
