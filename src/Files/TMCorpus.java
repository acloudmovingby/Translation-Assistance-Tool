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
public class TMCorpus {
    
    ArrayList<TMFile> files;
    
    public TMCorpus() {
        files = new ArrayList();
    }
    
    public void addFile(TMFile file) {
        files.add(file);
    }
    
    public ArrayList<TMFile> getFiles() {
        return files;
    }
}
