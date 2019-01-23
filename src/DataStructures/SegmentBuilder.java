/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import Database.DatabaseOperations;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

/**
 * Helps build Segments (which are immutable). The Segment properties can be set optionally, but the default values are as follows: id=0; fileID=0; fileName = "", Thai = "", English = "", isCommitted=false, isRemoved=false, rank=0
 * @author Chris
 */
public class SegmentBuilder {
    
    private int id;
    private int fileID;
    private String fileName;
    private String thai;
    private String english;
    private boolean isCommitted;
    private boolean isRemoved;
    private int rank;
    
    public SegmentBuilder() {
        this.id = DatabaseOperations.makeSegID();
        this.fileID = 0;
        this.fileName = "";
        this.thai = "";
        this.english = "";
        this.isCommitted = false;
        this.isRemoved = false;
        this.rank = 0;
    }
    
    public SegmentBuilder(Segment s) {
        this.id = s.getID();
        this.fileID = s.getFileID();
        this.fileName = s.getFileName();
        this.thai = s.getThai();
        this.english = s.getEnglish();
        this.isCommitted = s.isCommitted();
        this.isRemoved = s.isRemoved();
        this.rank = s.getRank();
    }
    
    public Segment createSegment() {
        return new Segment(id, fileID, fileName, thai, english, isCommitted, isRemoved, rank);
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public void setFileID(int fileID) {
        this.fileID = fileID;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void setThai(String thai) {
        this.thai = thai;
    }
    
    public void setEnglish(String english) {
        this.english = english;
    }
    
    public void setCommitted(boolean isCommitted) {
        this.isCommitted = isCommitted;
    }
    
    public void setRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }
}
