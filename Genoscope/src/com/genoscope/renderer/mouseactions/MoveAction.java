/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.renderer.mouseactions;

import com.genoscope.Genoscope;
import com.genoscope.GenoscopeApp;
import com.genoscope.renderer.GenoscopeRenderer.ViewConfig;
import com.genoscope.renderer.visualizers.ChromosomeVisualizer;
import com.genoscope.renderer.visualizers.Visualizer;
import java.awt.Cursor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

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
    private final ViewConfig view;
    private HashSet<Visualizer> selectionSet=new <Visualizer>HashSet();
    
  
    public MoveAction(Vector<Visualizer> c, ViewConfig conf)
    {
        System.out.println(" moveaction ");
        setVisualizers(c);
        view=conf;
    }

    class MTreeMap extends TreeMap<Object, Object> {

        public Object put(Integer key) {
            if(containsKey(key))
                return super.put(key, ((Integer)get(key))+1);
            return super.put(key, 1);
        }

        @Override
        public Object remove(Object key) {
            if(containsKey(key))
                if(((Integer)get(key) )>1 )
                    return super.put(key, ((Integer)get(key))-1);
            return super.remove(key);
        }
        
    }
    MTreeMap arX=new <Integer,Integer>MTreeMap(); //snapping array
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
                //System.out.println(" add ");
                arX.put(v.getX());
                arX.put(v.getX()+v.getWidth());
                arY.put(v.getY());
                arY.put(v.getY()+v.getHeight());
            }
        if(arX.size()>0)
            view.setViewBound((int)arX.lastKey(),(int)arY.lastKey() );
    }
    @Override
    public void mouseDown() {
        selected=null;
        for(Visualizer c:clients)
            if(intersect(c, x, y) && c.isVisible())
                selected=c;
        
        //TODO: add support for multiple selection
        Iterator<Visualizer>i=selectionSet.iterator();
        while(i.hasNext())
            i.next().setSelected(false);
        
        selectionSet.clear();
        if(selected!=null)
        {
            selectionSet.add(selected);
            
            selected.setSelected(true);
            
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
            String name;
            if(selected.getClass() == ChromosomeVisualizer.class)
                name = ((ChromosomeVisualizer)selected).getChromosomeName();
        }
    }

    @Override
    public void mouseUp() {
        if(selected!=null)
        {
            selected.setCoordinatesUpdateDone();
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
        view.setViewBound((int)arX.lastKey(),(int)arY.lastKey() );
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
                if(c.isVisible()){
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
                if(buttons==0)
                    Genoscope.canvas.setCursor(Cursor.getDefaultCursor());
            }
            break;
        case MOVING:
            if(selected!=null)
            {
                selected.setCoordinatesChanging();
                selected.setPosition(selected.getX()+dx, selected.getY()+dy);
                snap(selected);
            }
            break;
        case RESIZING:
            if(selected!=null)
            {
                selected.setCoordinatesChanging();
                switch(resizeW)
                {
                case N:
                    selected.setPosition(selected.getX(), selected.getY()+dy);
                    snap(selected);
                    selected.setSize(selected.getWidth(), selected.getHeight()-dy);
                    break;
                case S:
                    selected.setSize(selected.getWidth(), selected.getHeight()+dy);
                    break;
                case W:
                    selected.setPosition(selected.getX()+dx, selected.getY());
                    snap(selected);
                    selected.setSize(selected.getWidth()-dx, selected.getHeight());
                    break;
                case E:
                    selected.setSize(selected.getWidth()+dx, selected.getHeight());
                    break;
                case NE:
                    selected.setPosition(selected.getX(), selected.getY()+dy);
                    snap(selected);
                    selected.setSize(selected.getWidth()+dx, selected.getHeight()-dy);
                    break;
                case NW:
                    selected.setPosition(selected.getX()+dx, selected.getY()+dy);
                    snap(selected);
                    selected.setSize(selected.getWidth()-dx, selected.getHeight()-dy);
                    break;
                case SE:
                    selected.setSize(selected.getWidth()+dx, selected.getHeight()+dy);
                    snap(selected);
                    break;
                case SW:
                    selected.setPosition(selected.getX()+dx, selected.getY());
                    snap(selected);
                    selected.setSize(selected.getWidth()-dx, selected.getHeight()+dy);
                    break;
                }
            }
            break;
        }
        if(selected!=null && arX.size()>0)
        {
            int a=view.boundW;
            if(a<selected.getSnapX()+selected.getWidth())
                a=selected.getSnapX()+selected.getWidth();
            int b=view.boundH;
            if(b<selected.getSnapY()+selected.getHeight())
                b=selected.getSnapY()+selected.getHeight();
            view.setViewBound(a,b);
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
        else if(sy-d>u-sy)a=u;
        else a=d;
        
        d=(Integer) arY.floorKey(sye);
        u=(Integer) arY.higherKey(sye);
        if(d==null)b=u;
        else if(u==null)b=d;
        else if(sy-d>u-sy)b=u;
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
