/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Chris
 */
public class TMCompareEntry implements TMEntry, Comparable<TMCompareEntry> {

    private ArrayList<int[]> matchIntervals;
    private int matchSize;
    private String fileName;
    private String thai;
    private String english;
    private final int NUM_FIELDS = 3;

    /**
     * Entry has 4 fields: thai, english, match intervals, and filename
     */
    public TMCompareEntry() {
        this.setMatchIntervals(new ArrayList<>());
    }
    
    public TMCompareEntry(String thai, String english, ArrayList<int[]> matchIntervals, String filename) {
        
        setMatchIntervals(matchIntervals);
        this.thai = thai;
        this.english = english;
        setFileName(filename);
    }

    
    public void addMatchInterval(int[] interval) {
        if (interval.length != 2 || interval[0] > interval[1]) {
            throw new IllegalArgumentException();
        }
        matchIntervals.add(interval);
        matchSize = matchSize + (interval[1] - interval[0]);

    }

    public ArrayList<int[]> getMatchIntervals() {
        return matchIntervals;
    }
    
    public int[][] getMatchIntervalsArray() {
        return ((int[][]) matchIntervals.toArray());
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
        return matchSize;
    }

    @Override
    public int compareTo(TMCompareEntry t) {
        return t.getMatchSize() - this.getMatchSize();
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS;
    }
    
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

        if (!(o instanceof TMCompareEntry)) {
            return false;
        }

        TMCompareEntry m = (TMCompareEntry) o;

        return (m.getEnglish().equals(this.getEnglish())
                && m.getThai().equals(this.getThai())
                && deepCompare(m.getMatchIntervals(), this.getMatchIntervals())
                && m.getFileName().equals(this.getFileName()));
    }

    private boolean deepCompare(ArrayList<int[]> list1, ArrayList<int[]> list2) {
        
        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
System.out.println("WHATSUPPPPPPPPPPPP");
            if (!Arrays.equals(list1.get(i), list2.get(i))) {
                
                return false;
            }

        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.matchIntervals);
        hash = 59 * hash + this.matchSize;
        hash = 59 * hash + Objects.hashCode(this.fileName);
        return hash;
    }
}
