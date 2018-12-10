/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Chris
 */
public class TUCompareEntry implements Comparable<TUCompareEntry> {

    private ArrayList<int[]> matchIntervals;
    private boolean[] matches;
    private StringProperty fileName;
    private StringProperty thaiProperty;
    private StringProperty englishProperty;
    boolean isCommitted;
    private BooleanProperty isCommittedProperty;
    int matchSize;
    int longestMatchLength;

    public TUCompareEntry() {
        isCommitted = true;
        isCommittedProperty = new SimpleBooleanProperty(true);
        thaiProperty = new SimpleStringProperty();
        englishProperty = new SimpleStringProperty();
        fileName = new SimpleStringProperty();
        this.setMatchIntervals(new ArrayList<>());
        
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
    
    public boolean[] whereAreMatches() {
        boolean[] matchArray = new boolean[getThai().length()];
        for (int i=0; i<getThai().length(); i++) {
            matchArray[i] = false;
        }
        for (int[] match : matchIntervals) {
            
        }
        
        return matchArray;
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
    
    private void setMatchSize() {
        matchSize = 0;
        for (boolean b : matches) {
            if (b == true) {
                matchSize++;
            }
        }
        
    }
    
    public int getMatchSize() {
        return matchSize;
    }

    @Override
    public int compareTo(TUCompareEntry t) {
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

        if (!(o instanceof TUCompareEntry)) {
            return false;
        }

        TUCompareEntry m = (TUCompareEntry) o;
      
        boolean cond1 = m.getThai().equals(getThai());
        boolean cond2 = m.getEnglish().equals(getEnglish());
        boolean cond3 = deepCompare(m.getMatchIntervals(), getMatchIntervals());
        boolean cond4 = m.getFileName().equals(getFileName());
        boolean cond5 = m.getMatchSize() == getMatchSize();
        
        return cond1 && cond2 && cond3 && cond4 && cond5;
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
        for (int i=0; i<matchIntervals.size(); i++) {
            hash = 59 * hash + Arrays.hashCode(matchIntervals.get(i));
        }
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
        bf.append(" || ");
        bf.append(Arrays.deepToString(this.getMatchIntervalsArray()));
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
        return new SimpleStringProperty(String.valueOf(longestMatchLength));
    }

}
