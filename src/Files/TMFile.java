/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public interface TMFile {
    
    /**
     * 
     * @return The file in object array form as it is expected to be displayed. 
     */
    public Object[][] toArray();
    
    /**
     * 
     * @return The numbef of fields to be actually displayed. (not the total number of fields the file's TM entries store).
     */
    public int getNumFields();
    
    /**
     * 
     * @return A list of the TM entries (TUEntry objects).
     */
    public ArrayList<TUEntry> getTUs();
    
    public String getFileName();
    
}
