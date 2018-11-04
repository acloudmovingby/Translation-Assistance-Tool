/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import JavaFX_1.TUEntry_UI;
import java.util.Objects;
import javafx.beans.property.StringProperty;

/**
 * Contains only two fields: a Thai string and an English string.
 * @author Chris
 */
public class TUEntryBasic implements TUEntry {

    private String thai;
    private String english;
    private StringProperty thaiProperty;
    private StringProperty englishProperty;

    public TUEntryBasic() {
    }

    public TUEntryBasic(String thai, String english) {
        this.thai = thai;
        this.english = english;
        thaiProperty.set(thai);
        englishProperty.set(english);
    }


    @Override
    public String getThai() {
        return thai;
    }

    @Override
    public void setThai(String thai) {
        this.thai = thai;
        thaiProperty.set(thai);
    }

    @Override
    public String getEnglish() {
        return english;
    }

    @Override
    public void setEnglish(String english) {
        this.english = english;
        englishProperty.set(english);
    }

    @Override
    public TUEntry_UI getUI() {
        return new TUEntry_UI(getThai(), getEnglish());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof TUEntryBasic)) {
            return false;
        }

        TUEntryBasic m = (TUEntryBasic) o;
        
        return (this.getThai().equals(m.getThai())) && (this.getEnglish().equals(m.getEnglish()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.getEnglish());
        hash = 41 * hash + Objects.hashCode(this.getThai());
        return hash;
    }
    
    @Override
    public String toString() {
        return "[" + getThai() + ", " + getEnglish() + "]";
    }

    @Override
    public StringProperty thaiProperty() {
        return thaiProperty;
    }

    @Override
    public StringProperty englishProperty() {
        return englishProperty;
    }
    

}
