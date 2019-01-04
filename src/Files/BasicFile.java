/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import Database.DatabaseOperations;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A basic file that stores a list of TMBasicEntry's, which each contain just a
 * Thai and an English field.
 *
 * @author Chris
 */
public class BasicFile {

    // Actual segs displayed to the user while translating this file.
    private ObservableList<Segment> activeSegs;
    // Any segs that had been removed. They still might appear in match queries if they had been commited
    private ArrayList<Segment> removedSegs;
    private String fileName;
    private final int fileID;

    /**
     * When creating a new BasicFile, the fileID is auto-generated and a default
     * fileName of "untitled" is assigned.
     */
    public BasicFile() {
        activeSegs = FXCollections.observableArrayList();
        removedSegs = new ArrayList();
        fileName = "untitled";
        fileID = DatabaseOperations.createFileID(fileName);
    }

    /**
     * Used when rebuilding a file from the database.
     *
     * @param fileID
     * @param fileName
     */
    public BasicFile(int fileID, String fileName) {
        this.fileID = fileID;
        this.fileName = fileName;
        activeSegs = FXCollections.observableArrayList();
        removedSegs = new ArrayList();
    }

    public Segment newSeg() {
        // makes id that matches this file and assigns the fileid
        Segment newSeg = new Segment(0, getFileID(), fileName);
        // adds to end of lists
        addTUAtEnd(newSeg);
        // returns the Seg so it can be modified by the user (i.e. Thai/English can be added)
      
        return newSeg;
    }

    /**
     * Helper method to add new seg to the various lists.
     *
     * @param seg
     */
    private void addTUAtEnd(Segment seg) {
        if (seg.isRemoved()) {
            removedSegs.add(seg);
        } else {
            ObservableList<Segment> dispTUs = getActiveSegs();
            int newRank;
            if (dispTUs.isEmpty()) {
                newRank = Integer.MIN_VALUE + 8192;
            } else {
                newRank = 8192 + dispTUs.get(dispTUs.size()-1).getRank();
            }
            dispTUs.add(seg);
            seg.setRank(newRank);
        }
    }
    

    /**
     * ************************************************************************************
     *
     *
     * METHODS FOR CHANGING EXISTING TUS - removing, inserting, merging, and
     * splitting TUs
     *
     *
     *********************************************************************************
     */
    /**
     * Splits a segment into two pieces. The second segment begins with the character at
     * the specified index. So if this method was given "unhappy" and 2, the two
     * new TUs would be "un" and "happy".
     *
     * @param seg
     * @param splitIndex
     */
    public void splitTU(Segment seg, int splitIndex) {
        if (seg == null || splitIndex == 0 || splitIndex == seg.getThai().length()) {
            return;
        }
        String firstThai = seg.getThai().substring(0, splitIndex);
        String secondThai = seg.getThai().substring(splitIndex);

        // retrieves index of old TU
        int index = activeSegs.indexOf(seg);

        // creates first new TU and inserts
        Segment newTU1 = new Segment(getFileID());
        newTU1.setThai(firstThai);
        newTU1.setEnglish(seg.getEnglish());
        newTU1.setCommitted(false);
        getActiveSegs().add(index, newTU1);

        // creates second new TU and inserts
        Segment newTU2 = new Segment(getFileID());
        newTU2.setThai(secondThai);
        newTU2.setEnglish("");
        newTU2.setCommitted(false);
        getActiveSegs().add(index + 1, newTU2);

        //removes old TU
        removeTU(seg);

        // DATABASE
        realignRanks();
    }

    public void mergeTUs(List<Segment> tusToMerge) {

        if (tusToMerge.size() < 2) {
            return;
        }

        // Add to removed list
        for (Segment tu : tusToMerge) {
            tu.setRemoved(true);
            System.out.println("?" + tu.isRemoved());
            this.getRemovedSegs().add(tu);
        }

        // Build new TU 
        StringBuilder thaiSB = new StringBuilder();
        StringBuilder engSB = new StringBuilder();
        for (Segment tu : tusToMerge) {
            System.out.println("?" + tu.isRemoved());
            thaiSB.append(tu.getThai());
            engSB.append(tu.getEnglish());
        }
        Segment newTU = new Segment(DatabaseOperations.makeTUID(), getFileID(), getFileName());
        newTU.setThai(thaiSB.toString());
        newTU.setEnglish(engSB.toString());

        // Remove old TUs from display list
        int firstIndex = this.getActiveSegs().indexOf(tusToMerge.get(0));
        int size = tusToMerge.size();
        for (int i = 0; i < size; i++) {
            Segment removedTU = getActiveSegs().remove(firstIndex);
        }

        // Insert new TU
        insertSeg(firstIndex, newTU);
        //getTUsToDisplay().add(firstIndex, newSeg);
        //realignRanks();

    }

    private void removeTU(Segment tu) {
        tu.setRemoved(true);
        removedSegs.add(tu);
        activeSegs.remove(tu);
    }
    
    /**
     * Adds the segment at the proper index in activeSegs and sets its rank accordingly
     * @param index
     * @param seg 
     */
    private void insertSeg(int index, Segment seg) {
        int priorRank;
        int nextRank;
        
        if (index == getActiveSegs().size()-1 || getActiveSegs().isEmpty()) {
            addTUAtEnd(seg);
            return;
        } else if (index == 0) {
            priorRank = Integer.MIN_VALUE;
        } else {
            priorRank = getActiveSegs().get(index-1).getRank();
        }
        
        nextRank = getActiveSegs().get(index).getRank();
        int newRank = (nextRank+priorRank)/2;
        seg.setRank(newRank);
        getActiveSegs().add(index, seg);
        
        if (nextRank-priorRank < 2) {
            System.out.println("Ranks realigned");
            realignRanks();
        }

    }

    private void realignRanks() {
        // updates tusToDisplay in DB
        if (!getActiveSegs().isEmpty()) {
            getActiveSegs().get(0).setRank(Integer.MIN_VALUE + 8192);
        }
        for (int i = 1; i < getActiveSegs().size(); i++) {
            Segment tu = getActiveSegs().get(i);
            tu.setRank(getActiveSegs().get(i-1).getRank());
            DatabaseOperations.addOrUpdateSegment(tu);
        }

        // updates removedSegs in DB
        for (int i = 0; i < getRemovedSegs().size(); i++) {
            Segment tu = getRemovedSegs().get(i);
            DatabaseOperations.addOrUpdateSegment(tu);

        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        DatabaseOperations.addOrUpdateFileName(fileID, fileName);
    }

    public void commitAllTUs() {
        for (Segment tu : getActiveSegs()) {
            tu.setCommitted(true);
            // DATABASE
            //DatabaseOperations.addOrUpdateSegment(tu);
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
        if (this.getFileName().equals(m.getFileName()) 
                && m.getActiveSegs().size() != this.getActiveSegs().size()) {
            return false;
        } else {
            Iterator i1 = this.getActiveSegs().iterator();
            Iterator i2 = m.getActiveSegs().iterator();
            while (i1.hasNext()) {
                if (!i1.next().equals(i2.next())) {
                    areTMsEqual = false;
                }
            }
        }

        return areTMsEqual;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(getActiveSegs());
        // hash = 23 * hash + this.NUM_FIELDS;
        hash = 23 * hash + Objects.hashCode(getFileName());
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");

        for (Segment tu : getActiveSegs()) {
            sb.append(tu.toString());
            sb.append("\n\t");
        }
        return sb.toString();
    }

    public ObservableList<Segment> getActiveSegs() {
        return activeSegs;
    }

    public int getFileID() {
        return fileID;
    }
    
    public ArrayList<Segment> getRemovedSegs() {
        return removedSegs;
    }
}
