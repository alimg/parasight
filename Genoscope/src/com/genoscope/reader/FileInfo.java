/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genoscope.reader;

/**
 *
 * @author alim
 */
public class FileInfo {
    public String path;
    public String name;//seperated file name from path.
    public String []header;
    public Class type ;
    
    public FileInfo(String path, Class type)
    {
        this.path=path;
        this.type=type;
        this.name=path.substring(Math.max(path.lastIndexOf('/') + 1, path.lastIndexOf('\\')) + 1);
    }

    @Override
    public boolean equals(Object obj) {
        return path.equals(((FileInfo)obj).path);
    }
    
}
