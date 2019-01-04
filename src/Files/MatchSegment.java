/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import State.StateWithDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Chris
 */
public class MatchSegment implements Comparable<MatchSegment> {

    private ArrayList<int[]> matchIntervals;
    private boolean[] matches;
    private StringProperty fileName;
    private StringProperty thaiProperty;
    private StringProperty englishProperty;
    boolean isCommitted;
    private BooleanProperty isCommittedProperty;
    int matchSize;
    int longestMatchLength;
    // The Thai text represented as a TextFlow object
    TextFlow tFlow;

    public MatchSegment() {
        isCommitted = true;
        isCommittedProperty = new SimpleBooleanProperty(true);
        thaiProperty = new SimpleStringProperty();
        englishProperty = new SimpleStringProperty();
        fileName = new SimpleStringProperty();
        
    }
    
    
    /**
     * Inclusive match interval. For example, if the string is "abcd" and the match is "bc" then startIndex=1, endIndex=2.
     * @param startIndex Beginning of match.
     * @param endIndex End of match (inclusive).
     */
    public void addMatchInterval(int startIndex, int endIndex) {
        if (startIndex > endIndex) {
            throw new IllegalArgumentException("first number must be less than or equal to second");
        }
        
        if (startIndex<0 || startIndex>=getThai().length() || endIndex<1 || endIndex>=getThai().length()) {
            throw new IllegalArgumentException("Impossible match interval");
        }
        
        //records over which characters there are matches
        matchIntervals.add(new int[] {startIndex, endIndex});
        for (int i=startIndex; i<=endIndex; i++) {
            matches[i] = true;
        }
        
        setMatchSize();
        setLongestMatchLength();

    }

    public ArrayList<int[]> getMatchIntervals() {
        return matchIntervals;
    }
    
    /**
     * The intervals representing matches in string Thai are represented here.
     * @return 
     */
    public int[][] getMatchIntervalsArray() {
        int[][] ret = new int[matchIntervals.size()][];
        for (int i=0; i<matchIntervals.size(); i++) {
            ret[i] = new int[matchIntervals.get(i).length];
            System.arraycopy(matchIntervals.get(i), 0, ret[i], 0, matchIntervals.get(i).length);
        }
        return ret;
    }
    
    final void setMatchIntervals(ArrayList<int[]> matchIntervals) {
        this.matchIntervals = matchIntervals;
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
        matches = new boolean[getThai().length()];
        for (boolean b : matches) {
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
        return matches;
    }
    
    public void setMatches(boolean[] matches) {
        this.matches = matches;
    }
    
    // not necessary
    private void setMatchSize() {
        matchSize = 0;
        for (boolean b : matches) {
            if (b == true) {
                matchSize++;
            }
        }
        
    }
    
    public int getMatchSize() {
        matchSize = 0;
        for (boolean b : matches) {
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
      
        boolean cond1 = m.getThai().equals(getThai());
        boolean cond2 = m.getEnglish().equals(getEnglish());
        boolean cond3 = m.getFileName().equals(getFileName());
        boolean cond4 = m.getMatchSize() == getMatchSize();
        
        return cond1 && cond2 && cond3 && cond4;
    }

    private boolean deepCompare(ArrayList<int[]> list1, ArrayList<int[]> list2) {
        
        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            if (!Arrays.equals(list1.get(i), list2.get(i))) {
                
                return false;
            }

        }
        return true;
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
         isCommitted = b;
         
    }

    public boolean isCommitted() {
        return isCommitted;
    }
    
    public BooleanProperty isCommittedProperty() {
        return isCommittedProperty;
    }
    
    private void setLongestMatchLength() {
        int longestLength = 0;
        int currentLength = 0;
        for (boolean b : matches) {
            if (b==false) {
                longestLength = currentLength>longestLength ? currentLength : longestLength;
                currentLength = 0;
            }
            if (b==true) {
                currentLength++;
            }
        }
        longestMatchLength = (currentLength>longestLength ? currentLength : longestLength);
    }
    
    public StringProperty longestMatchLengthProperty() {
        setLongestMatchLength();
        return new SimpleStringProperty(String.valueOf(longestMatchLength));
    }
    
    /**
     * Returns a TextFlow object for use in updateItem callback method 
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
            text.setFont(StateWithDatabase.getThaiFont());
            textFlow.getChildren().add(text);
            // switches current color
            currentColor = currentColor == matchColor ? nonMatchColor : matchColor;
        }
        return textFlow;
    }

}
