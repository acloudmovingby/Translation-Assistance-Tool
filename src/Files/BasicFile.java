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
public class BasicFile implements TMFile {
    
    ArrayList<TMEntry> tmList;
    private final int NUM_FIELDS;
    private String fileName;
    
    
    public BasicFile() {
        tmList = new ArrayList();
        NUM_FIELDS = 2;
    }
    
    public void addEntry(TMEntryBasic a) {
        tmList.add(a);
    }

    @Override
    public Object[][] toArray() {
        Object[][] oa = new Object[tmList.size()][];
        
        for (int i=0; i<tmList.size(); i++) {
            oa[i] = tmList.get(i).toArray();
        }
        return oa;
    }
    
    @Override
    public ArrayList<TMEntry> getTMs() {
        return tmList;
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
