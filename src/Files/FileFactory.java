/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

/**
 *
 * @author Chris
 */
public class FileFactory {
    
    /**
     *  Takes a thai and english text and splits into segments based on line breaks. Both texts must have equal number of line breaks or it throws an IllegalArgumentException.
     * @param thai
     * @param english
     * @return BasicFile with each TM segment representing a line break in original text.
     */
    public BasicFile buildBasicParse(String thai, String english) {
        String[] th = thai.split("\r?\n");
        String[] en = english.split("\r?\n");

        if (th.length != en.length) {
            throw new IllegalArgumentException("Unequal number of line breaks in the two texts.");
        }

        BasicFile bf = new BasicFile();
        
        for (int i = 0; i < th.length; i++) {
            TMEntryBasic e = new TMEntryBasic();
            e.setThai(th[i]);
            e.setEnglish(en[i]);
            bf.addEntry(e);
        }
        
        return bf;
    }
    
    
}
