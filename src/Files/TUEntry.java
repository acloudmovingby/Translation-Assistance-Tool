/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import javafx.beans.property.StringProperty;

/**
 *
 * @author Chris
 */
public interface TUEntry {
    
    String getThai();
    void setThai(String thai);
    public StringProperty thaiProperty();
    
    String getEnglish();
    void setEnglish(String english);
    public StringProperty englishProperty();
    
    
    
}
