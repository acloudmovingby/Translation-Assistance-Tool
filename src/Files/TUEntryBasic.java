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
    
    BooleanProperty isCommittedProperty;

    
    /*
    protected TUEntryBasic(double id) {
        thaiProperty = new SimpleStringProperty();
        englishProperty = new SimpleStringProperty();
        isCommittedProperty = new SimpleBooleanProperty(false);
        this.id = id;
        DatabaseOperations.addTUtoDatabase(this);
    }*/
    
    /**
     * Constructor to create a TUEntryBasic when the associated file id is already known.
     * Only used in context where there is no file object (i.e. when the TU is being retrieved from the database).
     * When a totally new TU is being created it must be created through the BasicFile class, method newTU() 
     * @param id
     * @param fileID
     * @param fileName 
     */
    public TUEntryBasic(double id, double fileID, String fileName) {
        this.id = id;
        this.fileID = fileID;
        this.fileName = fileName;
        thaiProperty = new SimpleStringProperty();
        englishProperty = new SimpleStringProperty();
        isCommittedProperty = new SimpleBooleanProperty(false);
        DatabaseOperations.addTUtoDatabase(this);
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
       DatabaseOperations.replaceTU(this);
    }

    @Override
    public String getEnglish() {
        return englishProperty.getValue();
    }

    @Override
    public void setEnglish(String english) {
        englishProperty.set(english);
        DatabaseOperations.replaceTU(this);
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
        hash = 41 * hash + Objects.hashCode(this.isCommitted());
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
        return isCommittedProperty.get();
    }
    
     @Override
    public BooleanProperty isCommittedProperty() {
        return isCommittedProperty;
    }
    
    @Override
    public void setCommitted(boolean b) {
        isCommittedProperty.set(b);
        DatabaseOperations.replaceTU(this);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
         this.fileName = fileName;
         DatabaseOperations.replaceTU(this);
    }
    
    /*
    public void setID(double id) {
        this.id = id;
    } */
    
    public double getID() {
        return id;
    }
    
    public void setFileID(double fileID) {
        this.fileID = fileID;
        DatabaseOperations.replaceTU(this);
    }
    
    public double getFileID() {
        return fileID;
    }
    

}
