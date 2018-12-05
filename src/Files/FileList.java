/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class FileList {
    
    ArrayList<BasicFile> files;
    
    public FileList() {
        files = new ArrayList();
    }
    
    public void addFile(BasicFile file) {
        files.add(file);
    }
    
    public ArrayList<BasicFile> getFiles() {
        return files;
    }
}
