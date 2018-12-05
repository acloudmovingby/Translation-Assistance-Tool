/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import Database.DatabaseOperations;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A basic file that stores a list of TMBasicEntry's, which each contain just a Thai and an English field.
 * @author Chris
 */
public class BasicFile {
    
    ObservableList<TUEntryBasic> observableList;
  //  private final int NUM_FIELDS;
    private String fileName;
    private final double fileID;
    
    
    public BasicFile() {
        observableList = FXCollections.observableArrayList();
      //  NUM_FIELDS = 2;
        fileName = "untitled";
        fileID = DatabaseOperations.createFileID();
    }
    
    public TUEntryBasic newTU() {
       TUEntryBasic newTU = new TUEntryBasic(makeTUID(), getFileID(), getFileName());
      // newTU.setFileID(getFileID());
      // newTU.setFileName(getFileName());
       
       //DatabaseOperations.replaceTU(newTU);
       observableList.add(newTU);
       return newTU;
    }
    
    
    /**
     * Adds the TU to the BasicFile. Because TUEntryBasic id field is final, it makes a new TUEntryBasic object copying the fields from the input and assigning it a new id linked to the file. TUEntryBasics made outside of this context are assigned a default (non-unique) id value;
     * @param a The TU to be added to the file.
     
    public TUEntryBasic addEntry(TUEntryBasic a) {
       // sets id's and filenames
       TUEntryBasic newTU = new TUEntryBasic(makeTUID());
       newTU.setFileID(getFileID());
       newTU.setFileName(getFileName());
       
       // assigns other fields based on input TUEntryBasic object
       newTU.setThai(a.getThai());
       newTU.setEnglish(a.getEnglish());
       newTU.setCommitted(a.isCommitted());
       
       DatabaseOperations.addTUtoDatabase(newTU);
       observableList.add(newTU);
       return newTU;
    }*/
   
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        for (TUEntryBasic tu : observableList) {
            tu.setFileName(fileName);
        }
    }
    
    public void commitAllTUs() {
        for (TUEntryBasic tu : getObservableList()) {
            tu.setCommitted(true);
        }
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
        
        for (TUEntryBasic tu : getObservableList()) {
            sb.append(tu.toString());
            sb.append("\n\t");
        }
         return sb.toString();
    }

    public ArrayList getTUs() {
        return new ArrayList(observableList);
    }

    public ObservableList<TUEntryBasic> getObservableList() {
        return observableList;
    }

    public Object[][] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getFileID() {
        return fileID;
    }
    
    public void setFileID(double fileID) {
        
    }

    /**
     * Assigns an id value to the TU. The id value is equal to the fileID plus increments of 0.0001
     * Each time a new TU is added to the end of the list its id is assigned to be 0.0001 higher than the prior one. 
     * 
     * @return 
     */
    private double makeTUID() {
        if (observableList.isEmpty()) {
            return fileID + 0.0001;
        } else {
            return ((TUEntryBasic) observableList.get(observableList.size()-1)).getID() + 0.0001;
        }
        
    }
    
    
    
}
