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
        TMCompareEntry foo = t.getCopy();
        System.out.println("\tThe foo is \t"+foo);
        System.out.println("\tThe t is \t" + t);
        System.out.println("\tThe ts is \t" + ts.toString());
        System.out.println("\tts contain foo? \t" + ts.contains(foo));
        System.out.println("\tts contain t? \t" + ts.contains(t));
        System.out.println("\tAdded the foo? \t" + ts.add(foo));
        System.out.println("\tThe ts is \t" + ts.toString());
    }
    
    /*
    public void addEntry(String thai, String english, String fileName, ArrayList<int[]> matchIntervals) {
        TMCompareEntry a = new TMCompareEntry();
        
        a.setThai(thai);
        a.setEnglish(english);
        for (int[] ia : matchIntervals) {
            a.addMatchInterval(ia[0], ia[1]);
        }
        a.setFileName("POLITICS");
        ts.add(a.getCopy());
    }*/
    
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
        Object[][] oa = new Object[ts.size()][];
        
        int i = 0;
        while (iter.hasNext()) {
            TMCompareEntry nextEntry = iter.next();
            Object[] entryArray = nextEntry.toArray();
            Object[] entryArrayWithID = new Object[entryArray.length + 1];
            entryArrayWithID[0] = i;
            System.arraycopy(entryArray, 0, entryArrayWithID, 1, entryArray.length);
            oa[i] = entryArrayWithID;
            i++;
        }
        return oa;
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS;
    }
    

   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");
        
        for (TMEntry tm : ts) {
            sb.append(tm.toString());
            sb.append("\n\t");
        }
         return sb.toString();
    }
    
    
   
}
