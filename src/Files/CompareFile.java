/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;


public class CompareFile implements TMFile {
    
    TreeSet<TMCompareEntry> ts;
    private final int NUM_FIELDS;
    private String fileName;
    
    public CompareFile() {
        ts = new TreeSet();
        NUM_FIELDS = (new TMCompareEntry()).getNumFields();
    }
    
    public void addEntry(TMCompareEntry t) {
        ts.add(t);
    }
    
    public void addEntry(String thai, String english, String fileName, ArrayList<int[]> matchIntervals) {
        TMCompareEntry a = new TMCompareEntry();
        
        a.setThai(thai);
        a.setEnglish(english);
        for (int[] ia : matchIntervals) {
            a.addMatchInterval(ia);
        }
        a.setFileName("POLITICS");
    }
    
     @Override
    public ArrayList<TMEntry> getTMs() {
        return (new ArrayList(ts));
    }
    
  
    @Override
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public Object[][] toArray() {
        Iterator<TMCompareEntry> iter = ts.iterator();
        Object[][] oa = new Object[ts.size()][getNumFields()];
        
        int i = 0;
        while (iter.hasNext()) {
            TMCompareEntry next = iter.next();
            Object[] oa1 = next.toArray();
            oa[i] = oa1;
            i++;
        }
        return oa;
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS;
    }
    
    public int[][][] getMatchIntervals() {
        
        
        Iterator<TMCompareEntry> iter = ts.iterator();
        int[][][] ia = new int[ts.size()][][];
        
        int i = 0;
        while (iter.hasNext()) {
            TMCompareEntry next = iter.next();
            ia[i] = next.getMatchIntervalsArray();
            i++;
        }
        return ia;
    }

   
    
    
    
   
}
