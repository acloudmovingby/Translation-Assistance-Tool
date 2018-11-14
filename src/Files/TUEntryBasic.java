/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Contains only two fields: a Thai string and an English string.
 * @author Chris
 */
public class TUEntryBasic implements TUEntry {

    private StringProperty thaiProperty;
    private StringProperty englishProperty;
    String fileName;
    boolean isCommitted;

    public TUEntryBasic() {
        boolean isCommitted = false;
        thaiProperty = new SimpleStringProperty();
        englishProperty = new SimpleStringProperty();
        fileName = "TU Filename not set";
    }

    public TUEntryBasic(String thai, String english) {
        thaiProperty = new SimpleStringProperty(thai);
        englishProperty = new SimpleStringProperty(english);
    }


    @Override
    public String getThai() {
        return thaiProperty.getValue();
    }

    @Override
    public void setThai(String thai) {
       thaiProperty.set(thai);
    }

    @Override
    public String getEnglish() {
        return englishProperty.getValue();
    }

    @Override
    public void setEnglish(String english) {
        englishProperty.set(english);
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

    @Override
    public boolean isCommitted() {
        return isCommitted;
    }
    
    @Override
    public void setCommitted(boolean b) {
        isCommitted = b;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
         this.fileName = fileName;
    }
    

}
