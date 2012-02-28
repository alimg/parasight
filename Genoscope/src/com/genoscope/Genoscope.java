/*
 * 
 */
package com.genoscope;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

/**
 *
 * @author alim
 */
public class Genoscope {

    static {
        // setting this true causes window events not to get sent on Linux if you run from inside Eclipse
        GLProfile.initSingleton( false );
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities( glprofile );
        final GLCanvas glcanvas = new GLCanvas( glcapabilities ) {

            @Override
            public Dimension getSize() {
                return new Dimension(0,0);
                //return super.getSize();
            } 
            
        };
        
        glcanvas.setPreferredSize(new Dimension(0,0));

        glcanvas.addGLEventListener( new GLEventListener() {
            
            @Override
            public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height ) {
                GLRenderer.setup( glautodrawable.getGL().getGL2(), width, height );
            }
            
            @Override
            public void init( GLAutoDrawable glautodrawable ) {
            }
            
            @Override
            public void dispose( GLAutoDrawable glautodrawable ) {
            }
            
            @Override
            public void display( GLAutoDrawable glautodrawable ) {
                GLRenderer.render( glautodrawable.getGL().getGL2(), glautodrawable.getWidth(), glautodrawable.getHeight() );
            }
        }); 
        
        GenoscopeApp f=new GenoscopeApp();
     
        f.setVisible(true);
        f.OpenGLPanel.add(glcanvas);
        
        System.out.println("ends\n");
        
    }
}
