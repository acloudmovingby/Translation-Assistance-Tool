/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class CompareFile implements TMFile {
    
    ObservableList<TUCompareEntry> observableList;
    //private final int NUM_FIELDS;
    private String fileName;
    
    public CompareFile() {
        observableList = FXCollections.observableArrayList();
        fileName = "default";
    }
    
    public void addEntry(TUCompareEntry t) {
       observableList.add(t);
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
        return (new ArrayList(observableList));
    }
    
  
    @Override
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public String toString() {
        sort();
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");
        
        for (TUEntry tu : observableList) {
            sb.append(tu.toString());
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
        hash = 67 * hash + Objects.hashCode(this.observableList);
        hash = 67 * hash + Objects.hashCode(this.fileName);
        return hash;
    }

    private void sort() {
        observableList.sort(null);
    }

    @Override
    public ObservableList<TUCompareEntry> getObservableList() {
        return observableList;
    }

    public Object[][] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
   
}
