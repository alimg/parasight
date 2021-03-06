package com.genoscope.renderer;

import com.genoscope.renderer.mouseactions.MouseActionHandler;
import com.genoscope.renderer.mouseactions.MoveAction;
import com.genoscope.renderer.mouseactions.ScrollAction;
import com.genoscope.renderer.visualizers.*;
import com.genoscope.types.Chromosome;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JScrollBar;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;


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
    private Vector<InterChromosomeV> interVisualizers= new <InterChromosomeV>Vector();
    private int mMode=0;
    
    private int MPX=0,MPY=0;

    public Vector<Visualizer> getVisualizerList() {
        return clients;
    }

    public Vector<InterChromosomeV> getInterVisualizers() {
        return interVisualizers;
    }

    public void resetZoom() {
        mViewConfig.zoomFactor = 1.0f;
    }

    public Vector<InterChromosomeV> getPairingVisualizerList() {
        return interVisualizers;
    }
    
    public class ViewConfig{
        private float pos[]={0,0,0};
        private float zoomFactor=1;
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
            hScroll.setVisibleAmount((int)(GLHandler.getWidth()/zoomFactor));
            hScroll.setValue((int)-pos[0]);
            
            vScroll.setMinimum(0);
            vScroll.setMaximum(boundH);
            vScroll.setVisibleAmount((int)(GLHandler.getHeight()/zoomFactor));
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
            if(GLHandler.getWidth()/zoomFactor-pos[0]>boundW)
                pos[0]=GLHandler.getWidth()/zoomFactor-boundW;
            if(GLHandler.getHeight()/zoomFactor-pos[1]>boundH)
                pos[1]=GLHandler.getHeight()/zoomFactor-boundH;
            if(pos[0]>0)pos[0]=0;
            if(pos[1]>0)pos[1]=0;
            updateScrollbars();
        }
        private void translate() {
            glMatrixMode(GL_PROJECTION);
            
            glLoadIdentity();

            // coordinate system origin at lower left with width and height same as the window
            //GLU glu = new GLU();
            //glu.gluOrtho2D( 0.0f, width, 0.0f, height );
            GLU.gluOrtho2D( 0.0f, GLHandler.getWidth()/zoomFactor, GLHandler.getHeight()/zoomFactor,0.0f );
            glTranslatef(pos[0], pos[1], pos[2]);
            glMatrixMode(GL_MODELVIEW);
        }

        private int getExactX(int x) {
            return (int) (-mViewConfig.pos[0]+x/zoomFactor);
        }
        private int getExactY(int y) {
            return (int) (-mViewConfig.pos[1]+y/zoomFactor);
        }

        public void setZoomFactor(float zoomFactor) {
            this.zoomFactor = zoomFactor;
            updateScrollbars();
        }

        public float getX() {
            return pos[0];
        }
        public float getY() {
            return pos[1];
        }

        private float getZoomFactor() {
            return zoomFactor;
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
        float maxLength = 0;
        for(Visualizer v: clients)
            maxLength = Math.max(maxLength,((ChromosomeVisualizer)v).getChromosomeLength());
        if(maxLength == 0)
            maxLength = 1;
        for(Visualizer v: clients)
        {
            if(x+v.getWidth()>GLHandler.getWidth())
            {
                y+=lineMax+verticalGap;
                lineMax=0;
                x=horizonalGap;
            }
            v.setSize((int)((((ChromosomeVisualizer)v).getChromosomeLength()/maxLength)*750+
                    ((ChromosomeVisualizer)v).getPaddingLeft()),v.getHeight());
            v.doneResizing();
            if(v.isVisible()){
                v.setPosition(x,y);
                v.setSnapX(x);
                v.setSnapY(y);
                x+=v.getWidth()+horizonalGap;
                if(lineMax<v.getHeight())
                    lineMax=v.getHeight();
            }   
            v.setCoordinatesUpdateDone();
        }
        
            
        mouseHandlers[0].update();
    }
    
    public void addVisualizer(Visualizer v)
    {
        if(v instanceof InterChromosomeV ||v instanceof PairingVisualizer)
        {
            synchronized(interVisualizers){
                interVisualizers.add((InterChromosomeV)v);
                v.setVisible(false);
            }
        }
        else{
            synchronized(clients){
                if(v instanceof CBVisualizer)
                {
                    Iterator<Visualizer> it = clients.iterator();
                    for(;it.hasNext();)
                    {
                        Visualizer c = it.next();
                        Chromosome chr1 = ((ChromosomeVisualizer)v).getChromosome();
                        Chromosome chr2 = ((ChromosomeVisualizer)c).getChromosome();
                        if(c instanceof SingleChromosomeVisualizer && chr1.getName().equals(chr2.getName()))
                        {
                            ((SingleChromosomeVisualizer)c).setCytoband(chr1);
                            resetLayout();
                            return;
                        }
                    }
                }
                clients.add(v);
                resetLayout();
            }
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
        MPX=mViewConfig.getExactX(x);
        MPY=mViewConfig.getExactY(y);
        
        mMoveAction.mouseMove(MPX, MPY, buttons);
        mScrollAction.mouseMove(x, y, buttons);

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
        synchronized(interVisualizers){
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
                for(InterChromosomeV v: interVisualizers)
                {
                    v.updateState();
                    if( (!v.isBufferUpToDate() && v.isVisible()) || drawAll == true)
                    {
                        glPushMatrix();
                        v.initBufferMode();
                        v.updateBuffer();
                        glPopMatrix();
                    }
                }
                for(InterChromosomeV v: interVisualizers)
                    v.setCoordinatesUpdateHandled();
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
                        if(v.isSelected())
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
                for(Visualizer v: interVisualizers)
                {
                    if(v.isBufferUpToDate() && v.isVisible())
                    {
                        //translate then draw;
                        glPushMatrix();
                        glTranslatef(v.getSnapX(), v.getSnapY(), 0);
                        //glTranslatef(v.getX(), v.getY(), 0);
                        v.drawBuffered();

                        glPopMatrix();

                    }
                }
            }
        }
        drawAll = false;
    }

    /**
     * Control zooming factor of renderer
     * @param multiply value to be multiplied with current zooming factor.
     * @return new zoom factor
     */
    public float zoomView(float multiply)
    {
        mViewConfig.setZoomFactor(multiply*mViewConfig.getZoomFactor());
        return mViewConfig.getZoomFactor();
    }

    public void setScrollbars(JScrollBar horizontalScroll, JScrollBar verticalScroll) {
        mViewConfig.hScroll=horizontalScroll;
        mViewConfig.vScroll=verticalScroll;
        horizontalScroll.addAdjustmentListener(mViewConfig.hAdjustmenListener);
        verticalScroll.addAdjustmentListener(mViewConfig.vAdjustmenListener);
    }   
}
