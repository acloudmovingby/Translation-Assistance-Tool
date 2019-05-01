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
 * A list of Segments, some active, some hidden.
 *
 * Active Segments are what the user actually sees while translating. Hidden
 * Segments may be committed (and thus available for match search), but are not
 * seen by the user upon opening the file.
 *
 * @author Chris
 */
public class BasicFile {

    // Actual segs displayed to the user while translating this file.
    private final ObservableList<Segment> activeSegs;
    // Any segs that had been hidden. They still might appear in match queries if they had been commited
    private final ArrayList<Segment> hiddenSegs;
    /**
     * The name of the file.
     */
    private String fileName;
    /**
     * The id associated with the file in the SQLite database.
     */
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
     * Constructor creating a deep copy of another file.
     *
     * One use of this constructor is to transform a BasicFile into a MainFile.
     * All segment ids will be identical, but they are distinct objects in
     * distinct lists.
     *
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
     * Constructor for an empty file with the specified id and name.
     *
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
     * Adds the segment to the end of the activeSegs list in BasicFile. Throws
     * an IllegalArgumentException if: (1) fileID/fileName do not match this
     * file or (2) the segment is "hidden"
     *
     * Arguably a Segment doesn't need to know what file it is in, but it made
     * certain things easier in other parts of the program... (but if need be,
     * could be changed). The current program never moves Segment between files,
     * so it's not a bad assumption that every Segment is permanently associated
     * with just one file.
     *
     * @param seg
     */
    public void addSeg(Segment seg) {
        if (seg.getFileID() != getFileID()
                || !seg.getFileName().equals(getFileName())) {
            throw new IllegalArgumentException();
        } else {
            getActiveSegs().add(seg);
        }
    }

    /**
     * Removes the given Segment from the active lists (if it exists there or
     * not), and adds it to the hidden list.
     *
     * @param seg
     */
    public void hideSeg(Segment seg) {
        hiddenSegs.add(seg);
        activeSegs.remove(seg);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        DatabaseOperations.addOrUpdateFileName(fileID, fileName);
    }

    /**
     * NOTE: THIS REPLACES ALL SEGMENTS IN FILE (because they are immutable).
     *
     * Is expensive to do, so if you know at the time the file is being
     * parsed/constructed that all segments will be committed, then it's better
     * to build the file with committed segments from the get go.
     */
    public void commitAllSegs() {
        getActiveSegs().replaceAll((seg) -> {
            SegmentBuilder sb = new SegmentBuilder(seg);
            sb.setCommitted(true);
            return sb.createSegment();
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // add fileName 
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");

        // show active segments
        sb.append("ACTIVE\n\t");
        getActiveSegs().stream()
                .map((seg) -> {
                    sb.append(seg.toString());
                    return seg;
                })
                .forEachOrdered((segString) -> {
                    sb.append("\n\t");
                });

        // show hidden segments
        sb.append("HIDDEN\n\t");
        getHiddenSegs().stream().map((seg) -> {
            sb.append(seg.toString());
            return seg;
        }).forEachOrdered((segString) -> {
            sb.append("\n\t");
        });

        return sb.toString();
    }

    /**
     * Returns all "active" Segments in the file, i.e. those which can be seen
     * when the user opens the file to translate.
     *
     * @return
     */
    public ObservableList<Segment> getActiveSegs() {
        return activeSegs;
    }

    public int getFileID() {
        return fileID;
    }

    /**
     * Retursn all Segments associated with this file but which have been
     * "hidden," i.e. they can be searched for matches but they are not shown
     * when the file is opened.
     *
     * @return
     */
    public ArrayList<Segment> getHiddenSegs() {
        return hiddenSegs;
    }

    /**
     * Returns all Segments from the file, both hidden and active.
     *
     * @return
     */
    public List<Segment> getAllSegs() {
        Stream<Segment> streamA = getActiveSegs().stream();
        Stream<Segment> streamR = getHiddenSegs().stream();
        Stream<Segment> allSegsStream = Stream.concat(streamA, streamR);

        return allSegsStream.collect(Collectors.toList());
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

        // First checks that the hidden segs length is the same, and if it is, checks the equalit of every segment in that list. 
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

        // Checks that the ACTIVE SEGS length is the same, and if it is, checks the equalit of every segment in that list. 
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
        hash = 11 * hash + Objects.hashCode(getFileID());
        hash = 23 * hash + Objects.hashCode(getActiveSegs());
        hash = 17 * hash + Objects.hashCode(getHiddenSegs());
        hash = 51 * hash + Objects.hashCode(getFileName());
        return hash;
    }

}
