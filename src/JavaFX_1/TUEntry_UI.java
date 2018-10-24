/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Chris
 */
public class TUEntry_UI {
    
    private final StringProperty thaiField = new SimpleStringProperty();
    private final StringProperty englishField = new SimpleStringProperty();
    
    public TUEntry_UI(String thai, String english) {
        setThai(thai);
        setEnglish(english);
    }
    
    public StringProperty thaiProperty() { return thaiField; }
    public final String getThai() { return thaiField.toString();}
    public final void setThai(String thai) { thaiField.set(thai); }
   
    
    public StringProperty englishProperty() { return englishField; }
    public final String getEnglish() { return englishField.toString();}
    public final void setEnglish(String thai) { englishField.set(thai); }
   
    
}
