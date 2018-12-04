/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import Database.DatabaseOperations;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Contains only two fields: a Thai string and an English string.
 * @author Chris
 */
public class TUEntryBasic implements TUEntry {

    private final double id;
    private double fileID;
    private String fileName;
    
    private StringProperty thaiProperty;
    private StringProperty englishProperty;
    
    private boolean isCommitted;
    BooleanProperty isCommittedProperty;

    /*
    public TUEntryBasic() {
        boolean isCommitted = false;
        thaiProperty = new SimpleStringProperty();
        englishProperty = new SimpleStringProperty();
        isCommittedProperty = new SimpleBooleanProperty(false);
        id = -100;
    }*/
    /*
    public TUEntryBasic(double id) {
        boolean isCommitted = false;
        thaiProperty = new SimpleStringProperty();
        englishProperty = new SimpleStringProperty();
        isCommittedProperty = new SimpleBooleanProperty(false);
        this.id = id;
    }
    */
    public TUEntryBasic(double id, double fileID, String fileName){
        boolean isCommitted = false;
        thaiProperty = new SimpleStringProperty();
        englishProperty = new SimpleStringProperty();
        isCommittedProperty = new SimpleBooleanProperty(false);
        this.id = id;
        this.fileID = fileID;
        this.fileName = fileName;
        //DatabaseOperations.addTUtoDatabase(this);
    }
    /*
    public TUEntryBasic(String thai, String english) {
        thaiProperty = new SimpleStringProperty(thai);
        englishProperty = new SimpleStringProperty(english);
    }*/


    @Override
    public String getThai() {
        return thaiProperty.getValue();
    }

    @Override
    public void setThai(String thai) {
       thaiProperty.set(thai);
    }

    @Override
    public String getEnglish() {
        return englishProperty.getValue();
    }

    @Override
    public void setEnglish(String english) {
        englishProperty.set(english);
    }

  

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof TUEntryBasic)) {
            return false;
        }

        TUEntryBasic m = (TUEntryBasic) o;
        
        return (this.getThai().equals(m.getThai())) && 
                (this.getEnglish().equals(m.getEnglish())) &&
                (this.getFileID() == m.getFileID()) &&
                (this.getFileName().equals(m.getFileName())) &&
                (this.getID() == m.getID()) &&
                (this.isCommitted() == m.isCommitted());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.getEnglish());
        hash = 41 * hash + Objects.hashCode(this.getThai());
        hash = 41 * hash + Objects.hashCode(this.getFileID());
        hash = 41 * hash + Objects.hashCode(this.getFileName());
        hash = 41 * hash + Objects.hashCode(this.getID());
        hash = 41 * hash + Objects.hashCode(this.isCommitted);
        return hash;
    }
    
    @Override
    public String toString() {
        return "[" + getID()  + ", " + getFileID() + ", " + getFileName()  + ", " + isCommitted()  + ", " + getThai() + ", " + getEnglish() + "]";
    }

    @Override
    public StringProperty thaiProperty() {
        return thaiProperty;
    }

    @Override
    public StringProperty englishProperty() {
        return englishProperty;
    }

    @Override
    public boolean isCommitted() {
        return isCommitted;
    }
    
     @Override
    public BooleanProperty isCommittedProperty() {
        return isCommittedProperty;
    }
    
    @Override
    public void setCommitted(boolean b) {
        isCommittedProperty.set(b);
        isCommitted = b;
        //DatabaseOperations.replaceTU(this);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
         this.fileName = fileName;
    }
    
    /*
    public void setID(double id) {
        this.id = id;
    } */
    
    public double getID() {
        return id;
    }
    /*
    public void setFileID(double fileID) {
        this.fileID = fileID;
    }*/
    
    public double getFileID() {
        return fileID;
    }
    

}
