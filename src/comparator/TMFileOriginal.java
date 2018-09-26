/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.Arrays;

/**
 *
 * @author Chris
 */
public class TMFileOriginal {

    /**
     * TMs has three components in each array: id number (int), thai string, and
     * english string
     */
    Object[][] TMs;
    int[] tmIndices;
    String thai;
    String eng;
    int numFields;

    
    public TMFileOriginal(String thai, String eng) {
        this.thai = thai;
        this.eng = eng;
        numFields = 3;
        makeSegments();
    }
    
    public TMFileOriginal() {
        thai = "";
        eng = "";
        numFields = 3;
        TMs = new Object[0][0];
    }
    
    public TMFileOriginal(Object[][] o) {
        TMs = o;
    }
    
    
    
    public void addTM(Object[][] o) {
       
        Object[][] newTMs = new Object[TMs.length+o.length][numFields];
        
        System.arraycopy(TMs, 0, newTMs, 0, TMs.length);
        System.arraycopy(o, 0, newTMs, TMs.length, o.length);
        
        for (int i=0; i<newTMs.length; i++) {
            newTMs[i][0] = i+1;
        }
        TMs = newTMs;
        
    }
    
    public Object[][] getTMArray() {
        return TMs;
    }

    public Object[] getSegment(int index) {

        for (int i = 0; i < tmIndices.length; i++) {
            if (index <= tmIndices[i]) {
                return TMs[i];
                // 0 --> tmIndices[0] = 0
                // 2,3 --> tmIndices[1] = 3
                // 5 --> tmIndices[2] = 5
                // 7,8,9 --> tmIndices[3] = 9
                // 11 --> tmIndices[4] = 11
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * 
     * @param indices indices of where in the file a TM segment
     * @return 
     */
    public Object[][] getSegmentArray(int[] indices) {
        Object[][] segs = new Object[indices.length][numFields];
        int counter = 0;
        for (int j = 0; j < tmIndices.length; j++) {
            if (indices[counter] <= tmIndices[j]) {
                segs[counter] = TMs[j];
                //return TMs[j];
                counter++;
                if (counter >= indices.length) {
                    break;
                }
                // 0 --> tmIndices[0] = 0
                // 2,3 --> tmIndices[1] = 3
                // 5 --> tmIndices[2] = 5
                // 7,8,9 --> tmIndices[3] = 9
                // 11 --> tmIndices[4] = 11
            }
        }

        return segs;

    }

    private void makeSegments() {

        String[] th = thai.split("\r?\n");
        String[] en = eng.split("\r?\n");

        if (th.length != en.length) {
            throw new IllegalArgumentException("Unequal number of line breaks in the two texts.");
        }

        TMs = new Object[th.length][numFields];
        tmIndices = new int[th.length];
        int foo = -2;
        for (int i = 0; i < th.length; i++) {
            TMs[i][0] = i;
            TMs[i][1] = th[i];
            TMs[i][2] = en[i];

            // saves the index of the return line, useful for getSegment() method
            foo = foo + th[i].length() + 1;
            tmIndices[i] = foo;
        }

    }
    
    @Override
    public String toString() {
        String foo = "";
        for (Object[] oa : TMs) {
            foo = foo.concat(Arrays.toString(oa));
        }
        return foo;
    }

    public String getThai() {
        return thai;
    }
    
    public String getEnglish() {
        return eng;
    }

    

}
