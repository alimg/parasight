/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope;
import com.genoscope.types.Color;
import java.util.ArrayList;

/**
 *
 * @author Ahmet Kerim ŞENOL
 */

public class ColorPicker {
    private static ArrayList<Color> colors = new <Color>ArrayList();

    public static void addColor(Color c){
        colors.add(c);
    }

    public static Color getColor(String name){
        Color ret=null;
        for(Color c: colors){
            if(c.getName().equals(name.toLowerCase()))
                ret=c;
        }
        return ret;
    }

}
