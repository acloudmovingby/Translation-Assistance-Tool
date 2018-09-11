/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.Objects;

public class TMEntryBasic implements TMEntry {

    private String thai;
    private String english;
    private final int NUM_FIELDS;

    public TMEntryBasic() {
        NUM_FIELDS = 2;
    }

    public TMEntryBasic(String thai, String english) {
        this.thai = thai;
        this.english = english;
        NUM_FIELDS = 2;
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof TMEntryBasic)) {
            return false;
        }

        TMEntryBasic m = (TMEntryBasic) o;
        
        return (this.getThai().equals(m.getThai())) && (this.getEnglish().equals(m.getEnglish()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.getEnglish());
        hash = 41 * hash + Objects.hashCode(this.getThai());
        return hash;
    }

}
