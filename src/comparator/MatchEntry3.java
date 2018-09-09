/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Chris
 */
public class MatchEntry3 {
    String match;
    int index;
    ArrayList<Integer> indices;
    
    public MatchEntry3(String match, int index, ArrayList<Integer> indices) {
        this.match = match;
        this.index = index;
        this.indices = indices;
    }
    
    public int[] getIntArray() {
        int[] foo = new int[indices.size()];
        for (int i=0; i<indices.size(); i++) {
            foo[i] = (indices.get(i));
            
        }
        return foo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
                return true;
            }

            if (!(o instanceof MatchEntry3)) {
                return false;
            }

            MatchEntry3 m = (MatchEntry3) o;

            return (m.match == null ? match == null : m.match.equals(match)) && m.index == index && m.indices.equals(indices);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.match);
        hash = 43 * hash + this.index;
        hash = 43 * hash + Objects.hashCode(this.indices);
        return hash;
    }

    
    
    @Override
    public String toString() {
        return "{" + match + "," + index + "," + indices + "}";
    }
}
