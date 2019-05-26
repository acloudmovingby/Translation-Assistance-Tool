package DataStructures;

import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Stores the Thai and English translation (if it exists yet) for a given Thai
 * segment. This entry is tied to the fileID of the file with which it is
 * associated.
 *
 * Immutable. 
 *
 * @author Chris
 */
public final class Segment {

    /**
     * The id associated with the Segment (is equivalent to the unique id
     * representing the segment in the SQLite database).
     */
    private final int id;
    /**
     * The id of the file with which this Segment is associated.
     */
    private final int fileID;

    /**
     * The name of the file with which this is associated.
     */
    private final String fileName;

    /**
     * The source text.
     */
    private final String thai;
    
    /**
     * The target text (the translation).
     */
    private final String english;
    
    /**
     * If a Segment is committed, it means that the user has wanted to store the
     * translation represented by the Segment (and make it available for
     * searches).
     */
    private final boolean isCommitted;
  
    public Segment(int id, int fileID, String fileName, String thai, String english, boolean isCommitted) {
        this.id = id;
        this.fileID = fileID;
        this.fileName = fileName;
        this.thai = thai;
        this.english = english;
        this.isCommitted = isCommitted;
    }

    /**
     * Convenience method to return a deep copy of a Segment. Equivalent to
     * using createSegment() in SegmentBuilder
     *
     * @param s
     * @return
     */
    public static Segment getDeepCopy(Segment s) {
        SegmentBuilder sb = new SegmentBuilder(s);
        return sb.createSegment();
    }

    public String getThai() {
        return thai;
        //return thaiProperty.getValue();
    }

    public String getEnglish() {
        return english;
        //return englishProperty.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Segment)) {
            return false;
        }

        Segment s = (Segment) o;

        return (this.getThai().equals(s.getThai()))
                && (this.getEnglish().equals(s.getEnglish()))
                && (this.getFileID() == s.getFileID())
                && (this.getFileName().equals(s.getFileName()))
                && (this.getID() == s.getID())
                && (this.isCommitted() == s.isCommitted());
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
        return "[id=" + getID() + ", fid="
                + getFileID() + ", fn="
                + getFileName() + ", th="
                + getThai() + ", en="
                + getEnglish() + ", c?="
                + isCommitted() + "]";
    }

    /**
     * Same as getThai except it returns an observable StringProperty (necessary
     * for JavaFX bindings).
     *
     * @return
     */
    public StringProperty thaiProperty() {
        return new SimpleStringProperty(thai);
    }

    /**
     * Same as getEnglish except it returns an observable StringProperty
     * (necessary for JavaFX bindings).
     *
     * @return
     */
    public StringProperty englishProperty() {
        return new SimpleStringProperty(english);
    }

    /**
     * If a Segment is committed, it means that the user has wanted to store the
     * translation for the Thai text represented by the Segment.
     *
     * If a Segment is committed, then it can be searched for matches.
     *
     * @return True if the Segment is committed and false otherwise.
     */
    public boolean isCommitted() {
        return isCommitted;
    }

    /**
     * Same as isCommitted() except it returns an observable Boolean Property
     * (necessary for JavaFX stuff).
     *
     * @return True if the Segment is committed and false otherwise.
     */
    public BooleanProperty isCommittedProperty() {
        return new SimpleBooleanProperty(isCommitted);
    }

    public String getFileName() {
        return fileName;
    }

    public int getID() {
        return id;
    }

    public int getFileID() {
        return fileID;
    }

}
