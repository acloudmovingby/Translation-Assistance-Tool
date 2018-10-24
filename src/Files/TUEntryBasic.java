/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import JavaFX_1.TUEntry_UI;
import java.util.Objects;

/**
 * Contains only two fields: a Thai string and an English string.
 * @author Chris
 */
public class TUEntryBasic implements TUEntry {

    private String thai;
    private String english;
    private final int NUM_FIELDS;

    public TUEntryBasic() {
        NUM_FIELDS = 2;
    }

    public TUEntryBasic(String thai, String english) {
        this.thai = thai;
        this.english = english;
        NUM_FIELDS = 2;
    }
    
    /**
     * Creates a copy of this TUEntryBasic object.
     * @return 
     */
    public TUEntryBasic getCopy() {
        TUEntryBasic foo = new TUEntryBasic();
        foo.setThai(this.getThai());
        foo.setEnglish(this.getEnglish());
        return foo;
    }

    @Override
    public String getThai() {
        return thai;
    }

    @Override
    public void setThai(String thai) {
        this.thai = thai;
    }

    @Override
    public String getEnglish() {
        return english;
    }

    @Override
    public void setEnglish(String english) {
        this.english = english;
    }

    @Override
    public int getNumFields() {
        return NUM_FIELDS;
    }

    @Override
    public Object[] toArray() {
        Object[] oa = new Object[NUM_FIELDS];
        oa[0] = thai;
        oa[1] = english;
        return oa;
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
    

}
