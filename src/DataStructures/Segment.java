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
 * Immutable. (though the StringProperty objects stored could in theory be
 * mutated elsewhere).
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
     * The source Thai text.
     */
    private final StringProperty thaiProperty;
    /**
     * The English translation of the Thai text (or "" if no translation has
     * been given yet).
     */
    private final StringProperty englishProperty;

    /**
     * If a Segment is committed, it means that the user has wanted to store the
     * translation represented by the Segment (and make it available for
     * searches later).
     */
    private final BooleanProperty isCommittedProperty;

    public Segment(int id, int fileID, String fileName, String thai, String english, boolean isCommitted) {
        this.id = id;
        this.fileID = fileID;
        this.fileName = fileName;
        thaiProperty = new SimpleStringProperty(thai);
        englishProperty = new SimpleStringProperty(english);
        isCommittedProperty = new SimpleBooleanProperty(isCommitted);
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
        return thaiProperty.getValue();
    }

    public String getEnglish() {
        return englishProperty.getValue();
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
     * for JavaFX stuff).
     *
     * @return
     */
    public StringProperty thaiProperty() {
        return thaiProperty;
    }

    /**
     * Same as getEnglish except it returns an observable StringProperty
     * (necessary for JavaFX stuff).
     *
     * @return
     */
    public StringProperty englishProperty() {
        return englishProperty;
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
        return isCommittedProperty.get();
    }

    /**
     * Same as isCommitted() except it returns an observable Boolean Property
     * (necessary for JavaFX stuff).
     *
     * @return True if the Segment is committed and false otherwise.
     */
    public BooleanProperty isCommittedProperty() {
        return isCommittedProperty;
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
