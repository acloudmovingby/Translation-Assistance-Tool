/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single string that appears both in one segment (s1) and in a committed segment (s2) from the corpus.
 * A match of the string "zz" where s1 = "aaazz" and s2 = "zzzzz" would store:
 * "zz" as the string
 * 3 as the index in s1
 * 0, 1, 2, 3 as the indices in s2 (notice the overlap)
 * @author Chris
 */
public class MatchEntry3 {
    
    // The string 
    String match;
    // The index of where that string is in s1 (the segment we are running through MatchFinder)
    int index;
    // the list of indices where the string appears in s2 (segment from the corpus). 
    // As currently implemented, these indices may overlap. 
    ArrayList<Integer> indices;
    
    public MatchEntry3(String match, int index, ArrayList<Integer> indices) {
        this.match = match;
        this.index = index;
        this.indices = indices;
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
