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

/**
 * A basic file that stores a list of TMBasicEntry's, which each contain just a Thai and an English field.
 * @author Chris
 */
public class BasicFile implements TMFile {
    
    ObservableList<TUEntry> observableList;
  //  private final int NUM_FIELDS;
    private String fileName;
    
    
    public BasicFile() {
        observableList = FXCollections.observableArrayList();
      //  NUM_FIELDS = 2;
        fileName = "untitled";
    }
    
    public void addEntry(TUEntryBasic a) {
        //TUEntryBasic a2 = a.getCopy();
        a.setFileName(getFileName());
       observableList.add(a);
    }
    /*

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
    public ArrayList<TUEntry> getTUs() {
        return tmList;
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS;
    }
*/
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
        if (m.getTUs().size() != this.getTUs().size()) {
            return false;
        } else {
            Iterator i1 = this.getTUs().iterator();
            Iterator i2 = m.getTUs().iterator();
            while (i1.hasNext()) {
                if (!i1.next().equals(i2.next())) {
                    areTMsEqual = false;
                }
            }
        }
        
        return this.getFileName().equals(m.getFileName()) &&
                areTMsEqual;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(getObservableList());
       // hash = 23 * hash + this.NUM_FIELDS;
        hash = 23 * hash + Objects.hashCode(getFileName());
        return hash;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");
        
        for (TUEntry tu : getObservableList()) {
            sb.append(tu.toString());
            sb.append("\n\t");
        }
         return sb.toString();
    }

    @Override
    public ArrayList getTUs() {
        return new ArrayList(observableList);
    }

    @Override
    public ObservableList<TUEntry> getObservableList() {
        return observableList;
    }

    public Object[][] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
}
