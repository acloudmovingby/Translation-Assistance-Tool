/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import JavaFX_1.TUEntry_UI;

/**
 *
 * @author Chris
 */
public interface TUEntry {
    
    String getThai();
    
    void setThai(String thai);
    
    String getEnglish();
    
    void setEnglish(String english);
    
    /**
     * 
     * @return The number of fields to be displayed in the GUI.
     */
    int getNumFields();
    
    /**
     * 
     * @return The TUEntry in array form as to be displayed in the GUI.
     */
    Object[] toArray(); 
    
    public TUEntry_UI getUI();
    
    
}
