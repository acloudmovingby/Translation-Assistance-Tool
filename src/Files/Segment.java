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
 * Stores the Thai and English translation (if it exists yet) for a given Thai segment. This entry is tied to the fileID of the file with which it is associated.
 * @author Chris
 */
public class Segment {

    private int id;
    private int fileID;
    
    private StringProperty thaiProperty;
    private StringProperty englishProperty;
    
    private boolean isCommitted;
    BooleanProperty isCommittedProperty;
    
    private boolean isRemoved;
    private int rank;

    public Segment(int fileID){
        boolean isCommitted = false;
        thaiProperty = new SimpleStringProperty("");
        englishProperty = new SimpleStringProperty("");
        isCommittedProperty = new SimpleBooleanProperty(false);
        isRemoved = false;
        this.id = 0;
        this.fileID = fileID;
    }
    
    public Segment(int id, int fileID){
        boolean isCommitted = false;
        thaiProperty = new SimpleStringProperty("");
        englishProperty = new SimpleStringProperty("");
        isCommittedProperty = new SimpleBooleanProperty(false);
        isRemoved = false;
        this.id = id;
        this.fileID = fileID;
    }



    public String getThai() {
        return thaiProperty.getValue();
    }


    public void setThai(String thai) {
       thaiProperty.set(thai);
    }


    public String getEnglish() {
        return englishProperty.getValue();
    }


    public void setEnglish(String english) {
        englishProperty.set(english);
    }

  

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Segment)) {
            return false;
        }

        Segment m = (Segment) o;
        
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
        return "[" + getID()  + ", " 
                + getFileID() + ", " 
                + getFileName()  + ", " 
                + getRank() + ", "
                + getThai() + ", " 
                + getEnglish() + ", "
                + isCommitted()  + ", " 
                + isRemoved()  + "]";
    }


    public StringProperty thaiProperty() {
        return thaiProperty;
    }


    public StringProperty englishProperty() {
        return englishProperty;
    }


    public boolean isCommitted() {
        return isCommitted;
    }

    public BooleanProperty isCommittedProperty() {
        return isCommittedProperty;
    }
    

    public void setCommitted(boolean b) {
        isCommittedProperty.set(b);
        isCommitted = b;
    }

    public String getFileName() {
        return DatabaseOperations.getFileName(fileID);
    }
    
    public int getID() {
        return id;
    }
    
    public int getFileID() {
        return fileID;
    }

    public void setRemoved(boolean b) {
        isRemoved = b;
    }
    
    public boolean isRemoved() {
        return isRemoved;
    }

    public int getRank() {
        return rank;
    }
    
    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setID(int id) {
        this.id = id;
    }
    
    

}
