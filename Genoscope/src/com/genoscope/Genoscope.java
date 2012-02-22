/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;

import java.awt.*;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
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
        final GLCanvas glcanvas = new GLCanvas( glcapabilities );

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
        });/*
    	//MyPanel canvas=new MyPanel();
        JFrame frame = new JFrame( "Hello World" );
        //frame.getContentPane().add( canvas);
        JPanel p=new com.genoscope.GLJPanel();
        p.setLayout(new FlowLayout());
        p.add(glcanvas);
        p.add(new TextField("deneme"));
        p.revalidate();
        frame.getContentPane().add( p);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });


        frame.setSize( frame.getContentPane().getPreferredSize() );
        frame.setVisible( true );
        //frame.getContentPane().add( glcanvas, BorderLayout.CENTER );
        
        frame.pack();
        frame.setSize( 640, 480 );
        frame.setVisible( true );
        * 
        */
        
        GenoscopeApp f=new GenoscopeApp();
        f.getContentPane().add(new TextField("deneme"));
        f.setVisible(true);
        
        f.jPanel1.removeAll();
        //f.jPanel1.setPreferredSize(new Dimension(0,0));
        f.jPanel1.layout();
        
        f.jPanel1.add(glcanvas);
        //f.jPanel1.add(new TextField("deneme"));
        //f.main(args);

        System.out.println("ends\n");
        
    }
}
