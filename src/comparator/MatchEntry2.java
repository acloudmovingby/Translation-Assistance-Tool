/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.Objects;

/**
 *
 * @author Chris
 */
public class MatchEntry2 {
    String match;
    int index;
    
    public MatchEntry2(String match, int index) {
        this.match = match;
        this.index = index;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
                return true;
            }

            if (!(o instanceof MatchEntry2)) {
                return false;
            }

            MatchEntry2 m = (MatchEntry2) o;

            return (m.match == null ? match == null : m.match.equals(match)) && m.index == index;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.match);
        hash = 11 * hash + this.index;
        return hash;
    }
    
    @Override
    public String toString() {
        return "{" + match + "," + index + "}";
    }
}
