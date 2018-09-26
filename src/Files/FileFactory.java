/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class FileFactory {

    /**
     * Takes a thai and english text and splits into segments based on line
     * breaks. Both texts must have equal number of line breaks or it throws an
     * IllegalArgumentException.
     *
     * @param thai
     * @param english
     * @return BasicFile with each TM segment representing a line break in
     * original text.
     */
    public BasicFile buildBasicParse(String thai, String english) {
        String[] th = thai.split("\r?\n");
        String[] en = english.split("\r?\n");

        /*
        if (th.length != en.length) {
            throw new IllegalArgumentException("Unequal number of line breaks in the two texts.");
        }*/
        BasicFile bf = new BasicFile();
        
        for (int i = 0; i < th.length; i++) {
            TMEntryBasic e = new TMEntryBasic();
            e.setThai(th[i]);
            if (i < en.length) {
                e.setEnglish(en[i]);
            } else {
                e.setEnglish("");
            }
            bf.addEntry(e);
        }
        
        return bf;
    }
    
    public BasicFile justThaiFilePath(String filePath) {
        
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReaderThai
                    = new FileReader(filePath);
            
            BufferedReader buffReaderThai
                    = new BufferedReader(fileReaderThai);
            
            
            String line;
            
            StringBuilder sb = new StringBuilder();
            int counter = 0;
            while ((line = buffReaderThai.readLine()) != null) {
                if (line.trim().length() > 0) {
                    sb.append(line+"\n");
                    counter++;
                }                
            } 
            
            //makes "english" just be an index number, for testing purposes only
            StringBuilder sb2 = new StringBuilder(counter);
            for (int i=0; i<counter; i++) {
                sb2.append(i + "\n");
            }
           //System.out.println(sb.toString());
            BasicFile ret = this.buildBasicParse(sb.toString(), sb2.toString());
           
            buffReaderThai.close();
             return ret;
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '"
                    + filePath + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                    + filePath + "'");
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        
        return new BasicFile();
    }
    
    private static ArrayList<String> removeWhiteSpace(ArrayList<String> list) {
        ArrayList<String> ret = new ArrayList(list.size());
        for (String str : list) {
            if (str.trim().length() > 0) {
                str = str.trim();
                ret.add(str);
            }
        }
        return ret;
    }
    
}
