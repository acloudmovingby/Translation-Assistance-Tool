/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import Database.DatabaseOperations;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    // Any segs that had been hidden. They still might appear in match queries if they had been commited
    private ArrayList<Segment> hiddenSegs;
    private String fileName;
    private final int fileID;

    /**
     * When creating a new BasicFile, the fileID is auto-generated and a default
     * fileName of "untitled" is assigned.
     */
    public BasicFile() {
        activeSegs = FXCollections.observableArrayList();
        hiddenSegs = new ArrayList();
        fileName = "untitled";
        fileID = DatabaseOperations.createFileID(fileName);
    }
    
    /**
     * This constructor is used when transforming a BasicFile into a MainFile. All seg ids will be identical, but they are distinct objects in distinct lists (this is a deep copy of bf).
     * @param file 
     */
    public BasicFile(BasicFile file) {
        this.fileID = file.getFileID();
        this.fileName = file.getFileName();
        activeSegs = FXCollections.observableArrayList();
        hiddenSegs = new ArrayList();
        
        
        for (Segment s : file.getActiveSegs()) {
            SegmentBuilder sb = new SegmentBuilder(s);
            Segment segCopy = sb.createSegment();
            activeSegs.add(segCopy);
        }
        for (Segment s : file.getHiddenSegs()) {
            SegmentBuilder sb = new SegmentBuilder(s);
            Segment segCopy = sb.createSegment();
            hiddenSegs.add(segCopy);
        } 
        
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
        hiddenSegs = new ArrayList();
    }
    
    

    /**
     * Adds the segment to the end of the activeSegs list in BasicFile. Throws an IllegalArgumentException if: (1) fileID/fileName do not match this file or (2) the seg is "hidden"
     * @param seg 
     */
    public void addSeg(Segment seg) {
        // these exceptions are bad programming practice, but will be replaced in time!
        // this just makes sure the Segment was constructed correctly.
        // in the future, BasicFile/Segment will be designed in a way that an illegal argument can't be made
        if (seg.getFileID() != getFileID() ||
                !seg.getFileName().equals(getFileName())) {
            throw new IllegalArgumentException();
        } else {
            getActiveSegs().add(seg);
        }
    }
    
    
    

    /**
     * ************************************************************************************
     *
     *
     * METHODS FOR CHANGING EXISTING SEGS - removing, inserting, merging, and
     * splitting TUs
     *
     *
     *********************************************************************************
     */

    public void mergeSegs(List<Segment> tusToMerge) {

        if (tusToMerge.size() < 2) {
            return;
        }
        int firstIndex = this.getActiveSegs().indexOf(tusToMerge.get(0));

        // Add to hidden list
        for (Segment tu : tusToMerge) {
            hideSeg(tu);
        }

        // Build new TU 
        StringBuilder thaiSB = new StringBuilder();
        StringBuilder engSB = new StringBuilder();
        for (Segment tu : tusToMerge) {
            thaiSB.append(tu.getThai());
            engSB.append(tu.getEnglish());
        }
        
        SegmentBuilder sb = new SegmentBuilder(this);
        sb.setThai(thaiSB.toString());
        sb.setEnglish(engSB.toString());
        Segment newSeg = sb.createSegment();

        
        int size = tusToMerge.size();

        // Insert new TU
        insertSeg(firstIndex, newSeg);
    }

    public void hideSeg(Segment seg) {
        hiddenSegs.add(seg);
        activeSegs.remove(seg);
    }
    
    /**
     * Adds the segment at the proper index in activeSegs and sets its rank accordingly
     * @param index
     * @param seg 
     */
    public void insertSeg(int index, Segment seg) {
        getActiveSegs().add(index, seg);
    }

 

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        DatabaseOperations.addOrUpdateFileName(fileID, fileName);
    }

    /**
     * Currently mutates the segment instead of replacing it. 
     */
    public void commitAllSegs() {
        for (Segment seg : getActiveSegs()) {
            seg.setCommitted(true);
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

        if (this.getFileID() != m.getFileID()) {
            return false;
        }
        
        if (!this.getFileName().equals(m.getFileName())) {
            return false;
        }
        
        // First checks that the hidden segs length is the same, and if it is, checks the equalit of every seg in that list. 
        // if the hidden segs list length is unequal, returns false.
        if (this.getHiddenSegs().size() == m.getHiddenSegs().size()) {
            Iterator i1 = this.getHiddenSegs().iterator();
            Iterator i2 = m.getHiddenSegs().iterator();
            
            while (i1.hasNext() && i2.hasNext()) {
                if (!i1.next().equals(i2.next())) {
                    return false;
                }
            }
        } else {
            return false;
        }
        
         // Checks that the ACTIVE SEGS length is the same, and if it is, checks the equalit of every seg in that list. 
         // if the ACTIVE SEGS list length is unequal, returns false.
        if (this.getActiveSegs().size() == m.getActiveSegs().size()) {
            Iterator i1 = this.getActiveSegs().iterator();
            Iterator i2 = m.getActiveSegs().iterator();
            
            while (i1.hasNext() && i2.hasNext()) {
                if (!i1.next().equals(i2.next())) {
                    return false;
                }
            }
        } else {
            return false;
        }
        
        return true;
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
        sb.append("ACTIVE\n\t");

        for (Segment tu : getActiveSegs()) {
            sb.append(tu.toString());
            sb.append("\n\t");
        }
        
        sb.append("HIDDEN\n\t");
        for (Segment tu : getHiddenSegs()) {
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
    
    public ArrayList<Segment> getHiddenSegs() {
        return hiddenSegs;
    }
    
    /**
     * Returns all segs from the file, both hidden and active.
     * @return 
     */
    public List<Segment> getAllSegs() {
        
        ArrayList<Segment> ret = new ArrayList();
        Stream<Segment> streamA = getActiveSegs().stream();
        Stream<Segment> streamR = getHiddenSegs().stream();
        Stream<Segment> allSegsStream = Stream.concat(streamA, streamR);
        
        return allSegsStream.collect(Collectors.toList());
    }
  
    
    protected List<Segment> addSegments(List<Segment> origList, List<Segment> toAdd, int index){
        origList.addAll(index, toAdd);
        return origList;
    }
    
    protected void split2(Segment seg, int splitIndex) {
        
        int segIndex = this.getActiveSegs().indexOf(seg);
        
        // Checking for invalid input
        if (segIndex == -1) {
            return;
        } 
        
        if (seg == null 
                || splitIndex <= 0 
                || splitIndex >= seg.getThai().length()) {
            return;
        }
        
        // Make new segs that will replace old seg
        String firstThai = seg.getThai().substring(0, splitIndex);
        String secondThai = seg.getThai().substring(splitIndex);
        
        
        
        
    }
}
