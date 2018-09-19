/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;


public class CompareFile implements TMFile {
    
    ArrayList<TMCompareEntry> tmList;
    private final int NUM_FIELDS;
    private String fileName;
    
    public CompareFile() {
        tmList = new ArrayList();
        NUM_FIELDS = (new TMCompareEntry()).getNumFields();
        fileName = "default";
    }
    
    public void addEntry(TMCompareEntry t) {
       TMCompareEntry foo = t.getCopy();
       tmList.add(foo);
    }
    
    /*
    public void addEntry(String thai, String english, String fileName, ArrayList<int[]> matchIntervals) {
        TMCompareEntry a = new TMCompareEntry();
        
        a.setThai(thai);
        a.setEnglish(english);
        for (int[] ia : matchIntervals) {
            a.addMatchInterval(ia[0], ia[1]);
        }
        a.setFileName("POLITICS");
        ts.add(a.getCopy());
    }*/
    
    @Override
    public ArrayList<TMEntry> getTMs() {
        sort();
        return (new ArrayList(tmList));
    }
    
  
    @Override
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public Object[][] toArray() {
        sort();
        Iterator<TMCompareEntry> iter = tmList.iterator();
        Object[][] oa = new Object[tmList.size()][];
        
        int i = 0;
        while (iter.hasNext()) {
            TMCompareEntry nextEntry = iter.next();
            Object[] entryArray = nextEntry.toArray();
            Object[] entryArrayWithID = new Object[entryArray.length + 1];
            entryArrayWithID[0] = i;
            System.arraycopy(entryArray, 0, entryArrayWithID, 1, entryArray.length);
            oa[i] = entryArrayWithID;
            i++;
        }
        return oa;
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS;
    }
    

   
    @Override
    public String toString() {
        sort();
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");
        
        for (TMEntry tm : tmList) {
            sb.append(tm.toString());
            sb.append("\n\t");
        }
         return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof CompareFile)) {
            return false;
        }

        CompareFile m = (CompareFile) o;
        
         // tests equality of all TMs within files
        boolean areTMsEqual = true;
        if (m.getTMs().size() != this.getTMs().size()) {
            return false;
        } else {
            sort();
            m.sort();
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
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.tmList);
        hash = 67 * hash + this.NUM_FIELDS;
        hash = 67 * hash + Objects.hashCode(this.fileName);
        return hash;
    }

    private void sort() {
        tmList.sort(null);
    }
    
    
   
}
