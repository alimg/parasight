package com.genoscope.renderer;

import com.genoscope.renderer.mouseactions.MouseActionHandler;
import com.genoscope.renderer.mouseactions.MoveAction;
import com.genoscope.renderer.mouseactions.ScrollAction;
import com.genoscope.renderer.visualizers.Visualizer;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Vector;
import javax.swing.JScrollBar;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;


/**
 * This class handles OpenGL and input events for Visualizer classes.
 * @author alim
 */
public class GenoscopeRenderer {
    public final static int EDIT_MODE=1;
    public final static int NAVIGATE_MODE=0;
    private final static float WHEEL_SENS=1;
    public static boolean drawAll = false;

    private int horizonalGap=15;//pixels
    private int verticalGap=15;//pixels
    private Vector<Visualizer> clients= new <Visualizer>Vector();
    private int mMode=0;
    
    private int MPX=0,MPY=0;

    public Vector<Visualizer> getVisualizerList() {
        return clients;
    }

    
    
    
    public class ViewConfig{
        public float pos[]={0,0,0};
        private JScrollBar hScroll;
        private JScrollBar vScroll;
        
        private AdjustmentListener hAdjustmenListener=new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                pos[0]=-e.getValue();
            }
        };
        private AdjustmentListener vAdjustmenListener=new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                pos[1]=-e.getValue();
            }
        };
        
        public int boundW;
        public int boundH;
        
        public void updateScrollbars()
        {
            hScroll.setMinimum(0);
            hScroll.setMaximum(boundW);
            hScroll.setVisibleAmount(GLHandler.getWidth());
            hScroll.setValue((int)-pos[0]);
            
            vScroll.setMinimum(0);
            vScroll.setMaximum(boundH);
            vScroll.setVisibleAmount(GLHandler.getHeight());
            vScroll.setValue((int)-pos[1]);
        }
        
        public void setViewBound(int w,int h)
        {
            boundW=w;
            boundH=h;
            updateScrollbars();
        }

        public void setPos(float x,float y)
        {
            pos[0]=x;
            pos[1]=y;
            if(GLHandler.getWidth()-pos[0]>boundW)
                pos[0]=GLHandler.getWidth()-boundW;
            if(GLHandler.getHeight()-pos[1]>boundH)
                pos[1]=GLHandler.getHeight()-boundH;
            if(pos[0]>0)pos[0]=0;
            if(pos[1]>0)pos[1]=0;
            updateScrollbars();
        }
        private void translate() {
            glMatrixMode(GL_PROJECTION);
            
            glTranslatef(pos[0], pos[1], pos[2]);
            glMatrixMode(GL_MODELVIEW);
        }
    }
    private ViewConfig mViewConfig=new ViewConfig();
    private MouseActionHandler mouseHandlers[]=new MouseActionHandler[5];
    private MoveAction mMoveAction;
    private ScrollAction mScrollAction;
    public GenoscopeRenderer()
    {
        mouseHandlers[0]=mMoveAction=new MoveAction(clients,mViewConfig);
        mouseHandlers[1]=mScrollAction=new ScrollAction(mViewConfig);
    }
    /**
     * Arranges position of all clients 
     * @see Visualizer
     */
    public void resetLayout()
    {
        int x=horizonalGap;
        int y=0;
        int lineMax=0;
        for(Visualizer v: clients)
        {
            if(x+v.getWidth()>GLHandler.getWidth())
            {
                y+=lineMax+verticalGap;
                lineMax=0;
                x=horizonalGap;
            }
            v.setPosition(x,y);
            v.setSnapX(x);
            v.setSnapY(y);
            x+=v.getWidth()+horizonalGap;
            if(lineMax<v.getHeight())
                lineMax=v.getHeight();
                
        }
        mouseHandlers[0].update();
    }
    
    public void addVisualizer(Visualizer v)
    {
        synchronized(clients){
            clients.add(v);
            resetLayout();
        }
        //GLHandler.requestPaint();
    }
    
    void mouseDown(int i)
    {
        synchronized(clients)
        {
            //System.out.println("down "+i);
            if(mouseHandlers[i]!=null)
                mouseHandlers[i].mouseDown();
        }
    }
    
    void mouseUp(int i)
    {
        synchronized(clients)
        {
        if(mouseHandlers[i]!=null)
            mouseHandlers[i].mouseUp();
        //System.out.println("up "+i);
        }
    }
    void mouseWheel(int eventDWheel) {
        mViewConfig.setPos(mViewConfig.pos[0],mViewConfig.pos[1]+WHEEL_SENS*eventDWheel);
    }
    
    void mouseMove(int x, int y, int buttons) {
        synchronized(clients)
        {
        MPX=(int) (-mViewConfig.pos[0]+x);
        MPY=(int) (-mViewConfig.pos[1]+y);
        
        mMoveAction.mouseMove(MPX, MPY, buttons);
        mScrollAction.mouseMove(x, y, buttons);
        Visualizer r=null;
        for(Visualizer v: clients)
        {
            if(intersect(v,MPX,MPY))
                r=v;
            v.setHighlight(false);
        }
        if(r!=null)
            r.setHighlight(true);
        GLHandler.requestPaint();
        //System.out.println("x,y "+MPX+" "+MPY);
        }
    }

    

    private boolean updated=false;
    /**
     * @see GLHandler
     */
    void draw() {
       
        if(!updated)
        {
            resetLayout();
            updated=true;
        }
        synchronized(clients)
        {
            for(Visualizer v: clients)
            {
                if(! v.isBufferUpToDate() || drawAll == true)
                {
                    glPushMatrix();
                    v.initBufferMode();
                    v.updateBuffer();
                    glPopMatrix();
                }
            }
            GLHandler.setup();
            glClear( GL_COLOR_BUFFER_BIT );
            
            mViewConfig.translate();
            
            
            for(Visualizer v: clients)
            {
                if(v.isBufferUpToDate() && v.isVisible())
                {
                    //translate then draw;
                    glPushMatrix();
                    glTranslatef(v.getSnapX(), v.getSnapY(), 0);
                    //glTranslatef(v.getX(), v.getY(), 0);
                    v.drawBuffered();
                    if(v.isHiglighted())
                    {//<editor-fold defaultstate="collapsed" desc="draw some rectangle around">
                        GL20.glUseProgram(0);
                        glColor4f(0,0,0,1);
                        glDisable(GL_TEXTURE_2D);
                        glLineWidth(1);
                        glBegin(GL_LINE_LOOP);
                        glVertex2f(0, 0);
                        glVertex2f(0,v.getHeight());
                        glVertex2f(v.getWidth(),v.getHeight());
                        glVertex2f(v.getWidth(), 0);
                        glEnd();
                        //</editor-fold>
                    }
                    glPopMatrix();

                }
        }
        }
        drawAll = false;
    }

    private boolean intersect(Visualizer v, int x, int y) {
        if( x>v.getX() && x<v.getX()+v.getWidth() &&
                y>v.getY() && y<v.getY()+v.getHeight() )
            return true;
        return false;
    }



    public void setScrollbars(JScrollBar horizontalScroll, JScrollBar verticalScroll) {
        mViewConfig.hScroll=horizontalScroll;
        mViewConfig.vScroll=verticalScroll;
        horizontalScroll.addAdjustmentListener(mViewConfig.hAdjustmenListener);
        verticalScroll.addAdjustmentListener(mViewConfig.vAdjustmenListener);
    }   
}
