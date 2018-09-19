/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * A basic file that stores a list of TMBasicEntry's, which each contain just a Thai and an English field.
 * @author Chris
 */
public class BasicFile implements TMFile {
    
    ArrayList<TMEntry> tmList;
    private final int NUM_FIELDS;
    private String fileName;
    
    
    public BasicFile() {
        tmList = new ArrayList();
        NUM_FIELDS = 2;
        fileName = "untitled";
    }
    
    public void addEntry(TMEntryBasic a) {
        TMEntryBasic a2 = a.getCopy();
        tmList.add(a2);
       
    }

    @Override
    public Object[][] toArray() {
        Object[][] oa = new Object[tmList.size()][];
        
        for (int i=0; i<tmList.size(); i++) {
            Object[] entryArray = tmList.get(i).toArray();
            Object[] entryArrayWithID = new Object[entryArray.length+1];
            // adds id number
            entryArrayWithID[0] = i;
            System.arraycopy(entryArray, 0, entryArrayWithID, 1, entryArray.length);
            oa[i] = entryArrayWithID;
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
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof BasicFile)) {
            return false;
        }

        BasicFile m = (BasicFile) o;
        
        // tests equality of all TMs within files
        boolean areTMsEqual = true;
        if (m.getTMs().size() != this.getTMs().size()) {
            return false;
        } else {
            Iterator i1 = this.getTMs().iterator();
            Iterator i2 = m.getTMs().iterator();
            while (i1.hasNext()) {
                if (!i1.next().equals(i2.next())) {
                    areTMsEqual = false;
                }
            }
        }
        
        return this.getNumFields() == m.getNumFields() &&
                this.getFileName().equals(m.getFileName()) &&
                areTMsEqual;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.tmList);
        hash = 23 * hash + this.NUM_FIELDS;
        hash = 23 * hash + Objects.hashCode(this.fileName);
        return hash;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");
        
        for (TMEntry tm : tmList) {
            sb.append(tm.toString());
            sb.append("\n\t");
        }
         return sb.toString();
    }

    
}
