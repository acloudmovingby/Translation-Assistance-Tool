/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javafx.collections.ObservableList;


public class CompareFile implements TMFile {
    
    ArrayList<TUCompareEntry> tmList;
    //private final int NUM_FIELDS;
    private String fileName;
    
    public CompareFile() {
        tmList = new ArrayList();
       // NUM_FIELDS = (new TUCompareEntry()).getNumFields();
        fileName = "default";
    }
    
    public void addEntry(TUCompareEntry t) {
       //TUCompareEntry foo = t.getCopy();
       tmList.add(t);
    }
    
    /*
    public void addEntry(String thai, String english, String fileName, ArrayList<int[]> matchIntervals) {
        TUCompareEntry a = new TUCompareEntry();
        
        a.setThai(thai);
        a.setEnglish(english);
        for (int[] ia : matchIntervals) {
            a.addMatchInterval(ia[0], ia[1]);
        }
        a.setFileName("POLITICS");
        ts.add(a.getCopy());
    }*/
    
    @Override
    public ArrayList<TUEntry> getTUs() {
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
    /*
    @Override
    public Object[][] toArray() {
        sort();
        Iterator<TUCompareEntry> iter = tmList.iterator();
        Object[][] oa = new Object[tmList.size()][];
        
        int i = 0;
        while (iter.hasNext()) {
            TUCompareEntry nextEntry = iter.next();
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
    */

   
    @Override
    public String toString() {
        sort();
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");
        
        for (TUEntry tm : tmList) {
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
        if (m.getTUs().size() != this.getTUs().size()) {
            return false;
        } else {
            sort();
            m.sort();
            Iterator i1 = this.getTUs().iterator();
            Iterator i2 = m.getTUs().iterator();
            while (i1.hasNext()) {
                if (!i1.next().equals(i2.next())) {
                    areTMsEqual = false;
                }
            }
        }
        
        return 
                this.getFileName().equals(m.getFileName()) &&
                areTMsEqual;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.tmList);
        hash = 67 * hash + Objects.hashCode(this.fileName);
        return hash;
    }

    private void sort() {
        tmList.sort(null);
    }

    @Override
    public ObservableList getObservableList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object[][] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
   
}
