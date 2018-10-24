/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import JavaFX_1.TUCompare_UI;
import JavaFX_1.TUEntry_UI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Chris
 */
public class TUCompareEntry implements TUEntry, Comparable<TUCompareEntry> {

    private ArrayList<int[]> matchIntervals;
    private int[] matches;
    private String fileName;
    private String thai;
    private String english;
    private final int NUM_FIELDS = 3;

    /**
     * Entry has 4 fields: thai, english, match intervals, and filename
     */
    public TUCompareEntry() {
        this.setMatchIntervals(new ArrayList<>());
        matches = new int[0];
    }
    
    /*
    public TUCompareEntry(String thai, String english, ArrayList<int[]> matchIntervals, String filename) {
        
        setMatchIntervals(matchIntervals);
        this.thai = thai;
        this.english = english;
        setFileName(filename);
        matches = new int[thai.length()];
        for (int i=0; i<matches.length; i++) {
            matches[i] = 0;
        }
    }*/

    
    TUCompareEntry getCopy() {
        TUCompareEntry ret = new TUCompareEntry();
        
        ret.setThai(this.getThai());
        ret.setEnglish(this.getEnglish());
        ret.setFileName(this.getFileName());
        for (int i = 0; i<matchIntervals.size(); i++) {
            int[] ia1 = matchIntervals.get(i);
            int[] ia2 = new int[ia1.length];
            System.arraycopy(ia1, 0, ia2, 0, ia1.length);
            ret.addMatchInterval(ia1[0], ia1[1]);
        }
        return ret;
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
        
        if (startIndex<0 || startIndex>=thai.length() || endIndex<1 || endIndex>=thai.length()) {
            throw new IllegalArgumentException("Impossible match interval");
        }
        
        //records over which characters there are matches
        matchIntervals.add(new int[] {startIndex, endIndex});
        for (int i=startIndex; i<=endIndex; i++) {
            matches[i] = 1;
            
        }

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

    public final void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
    
    @Override
    public String getThai() {
        return thai;
    }
    
    @Override
    public void setThai(String thai) {
        this.thai = thai;
        matches = new int[thai.length()];
    }
    
    @Override
    public String getEnglish() {
        return english;
    }
    
    @Override
    public void setEnglish(String english) {
        this.english = english;
    }

    public int getMatchSize() {
        int matchSize = 0;
        for (int i1 : matches) {
            if (i1 == 1) {
                matchSize++;
            }
        }
        return matchSize;
    }
    
    @Override
    public TUCompare_UI getUI() {
        return new TUCompare_UI(getThai(), getEnglish(), matchIntervals);
    }

    @Override
    public int compareTo(TUCompareEntry t) {
        if (t.getMatchSize() - getMatchSize() == 0) {
            return t.getThai().length() - getThai().length();
        }
        return t.getMatchSize() - getMatchSize();
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS;
    }
    
    /**
     * 
     * @return An object array containing three strings: Thai, English, and Filename.
     */
    @Override
    public Object[] toArray() {
        Object[] oa = new Object[NUM_FIELDS];
        oa[0] = getThai();
        oa[1] = getEnglish();
        oa[2] = getFileName();
        return oa;
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

}
