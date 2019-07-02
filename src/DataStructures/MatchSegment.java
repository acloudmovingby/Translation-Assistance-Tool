/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import State.UIState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * This class represents a match between a source Segment in the MainFile and a
 * target segment from the corpus.
 *
 * The source Segment is not stored in the Segment, but the target Segment and
 * its properties are.
 *
 *
 * @author Chris
 */
public class MatchSegment implements Comparable<MatchSegment> {

    /**
     * Represents the segment from the corpus with which a match was found (the
     * "target" segment in a match).
     */
    private final Segment seg;
    /**
     * Represents where in the seg's Thai property there are matches. Length is
     * equal to seg.getThai().length()
     */
    private boolean[] charsPartOfSubstring;
    /**
     * The name of the file this segment exists in.
     */
    private final StringProperty fileName;
    /**
     * The Thai text of the match (the target Segment).
     */
    private final StringProperty thaiProperty;
    /**
     * The English text of the match (the target Segment).
     */
    private final StringProperty englishProperty;
    /**
     * The committed status of the match (the target Segment)
     */
    private final BooleanProperty isCommittedProperty;
    /**
     * How many characters total form a match.
     */
    int matchSize;
    int longestMatchLength;
    // The Thai text represented as a TextFlow object
    TextFlow tFlow;

    public MatchSegment(Segment targetSeg) {
        this.seg = targetSeg;
        isCommittedProperty = new SimpleBooleanProperty(true);
        thaiProperty = new SimpleStringProperty(seg.getThai());
        englishProperty = new SimpleStringProperty(seg.getEnglish());
        fileName = new SimpleStringProperty(seg.getFileName());
        charsPartOfSubstring = new boolean[thaiProperty.get().length()];
    }

    public String getFileName() {
        return fileName.getValue();
    }

    public final void setFileName(String fileName) {
        if (fileName == null) {
            this.fileName.set("");
        } else {
            this.fileName.set(fileName);
        }
    }

    public String getThai() {
        return thaiProperty.getValue();
    }

    public void setThai(String thai) {
        thaiProperty.set(thai);
        charsPartOfSubstring = new boolean[getThai().length()];
        for (boolean b : charsPartOfSubstring) {
            b = false;
        }
    }

    public String getEnglish() {
        return englishProperty.getValue();
    }

    public void setEnglish(String english) {
        englishProperty.set(english);
    }

    public boolean[] getMatches() {
        return charsPartOfSubstring;
    }

    public void setMatches(boolean[] matches) {
        this.charsPartOfSubstring = matches;
    }

    public int getMatchSize() {
        matchSize = 0;
        for (boolean b : charsPartOfSubstring) {
            if (b == true) {
                matchSize++;
            }
        }
        return matchSize;
    }

    @Override
    public int compareTo(MatchSegment t) {
        if (t.getMatchSize() - getMatchSize() == 0) {
            return t.getThai().length() - getThai().length();
        }
        return t.getMatchSize() - getMatchSize();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MatchSegment)) {
            return false;
        }

        MatchSegment m = (MatchSegment) o;

        boolean cond1 = m.getMatchSize() == getMatchSize();
        boolean cond2 = m.getFileName().equals(getFileName());
        boolean cond3 = m.getThai().equals(getThai());
        boolean cond4 = m.getEnglish().equals(getEnglish());

        return cond1 && cond2 && cond3 && cond4;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + getMatchSize();
        hash = 59 * hash + Objects.hashCode(this.fileName);
        hash = 59 * hash + Objects.hashCode(getThai());
        hash = 59 * hash + Objects.hashCode(getEnglish());
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder bf = new StringBuilder();

        bf.append("[");
        bf.append(this.getThai());
        bf.append(", ");
        bf.append(this.getEnglish());
        bf.append(", ");
        bf.append(this.getFileName());
        bf.append(", ");
        bf.append(this.getMatchSize());
        bf.append("]");

        return bf.toString();

    }

    public StringProperty thaiProperty() {
        return thaiProperty;
    }

    public StringProperty englishProperty() {
        return englishProperty;
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public StringProperty matchSizeProperty() {
        return new SimpleStringProperty(String.valueOf(getMatchSize()));
    }

    public void setCommitted(boolean b) {
        isCommittedProperty.set(b);
    }

    public boolean isCommitted() {
        return isCommittedProperty.get();
    }

    public BooleanProperty isCommittedProperty() {
        return isCommittedProperty;
    }

    private int findLongestMatchLength() {
        int longestLength = 0;
        int currentLength = 0;
        for (boolean b : charsPartOfSubstring) {
            if (b) {
                currentLength++;
            } else {
                longestLength = currentLength > longestLength ? currentLength : longestLength;
                currentLength = 0;
            }
        }
        return (currentLength > longestLength ? currentLength : longestLength);
    }

    public IntegerProperty longestMatchLengthProperty() {
        return new SimpleIntegerProperty(findLongestMatchLength());
    }

    /**
     * Returns a TextFlow object for use in updateItem callback method
     *
     * @return
     */
    public TextFlow getTFlow() {
        return tFlow;
    }

    /**
     * Takes a boolean array of same length as text where true indicates a
     * matching character and false indicates a non-matching character.
     *
     * @param matchingTUText The Thai text with matches in it.
     * @param matches A boolean array of same length as text.
     * @return A TextFlow object with matching substrings colored differently.
     */
    public TextFlow matchesAsTextFlow(String matchingTUText, boolean[] matches) {
        // breaks text up into list of matching and non-matching substrings
        ArrayList<String> substrings = new ArrayList();
        TextFlow textFlow = new TextFlow();
        if (matchingTUText.length() == 0) {
            return textFlow;
        }

        boolean currentBoolean = matches[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matches.length; i++) {
            if (matches[i] != currentBoolean) {
                // adds substring to list
                substrings.add(sb.toString());
                // restarts stringbuilder and resets current boolean
                sb = new StringBuilder();
                sb.append(matchingTUText.charAt(i));
                currentBoolean = matches[i];
            } else {
                sb.append(matchingTUText.charAt(i));
            }
        }
        substrings.add(sb.toString());

        // Builds TextFlow object
        // Assigns specific colors for matching and non-matching substrings.
        Color matchColor = Color.GREEN;
        Color nonMatchColor = Color.BLACK;
        Color currentColor;

        // sets currentColor according to whether the text begins with a  match or not
        currentColor = matches[0] == false ? nonMatchColor : matchColor;

        Iterator<String> iter = substrings.iterator();

        // creates Text objects, assigns color, then adds to TextFlow
        while (iter.hasNext()) {
            Text text = new Text(iter.next());
            text.setFill(currentColor);
            text.setFont(UIState.getThaiFont());
            textFlow.getChildren().add(text);
            // switches current color
            currentColor = currentColor == matchColor ? nonMatchColor : matchColor;
        }
        return textFlow;
    }

    /**
     * Gets the segment that this MatchSegment represents (i.e. where in the
     * corpus the match was found).
     *
     * @return
     */
    public Segment getSegment() {
        return seg;
    }

}
