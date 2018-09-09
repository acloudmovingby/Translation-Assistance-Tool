/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Chris
 */
public class Matches implements Iterable {

    /**
     * ArrayList of MatchEntry3 (string, text index, corpus indices)
     */
    private ArrayList<MatchEntry3> matchList;
    /**
     * HashMap where key is a MatchEntry2 (string and index in text) and the value is an ArrayList of corpus indices
     */
    private HashMap<MatchEntry2, ArrayList<Integer>> matchHashT;

    public Matches() {
        //matches = new ArrayList();
        matchList = new ArrayList();
        matchHashT = new HashMap();
    }

    /**
     * Adds the given string as a match. If that string and textIndex already
     * exist, t adds the corpusIndex to the already existing list of corpus
     * indices for that MatchEntry
     *
     * @param s
     * @param textIndex
     * @param corpusIndex
     */
    public void addMatch(String s, int textIndex, int corpusIndex) {
        
        if (!contains(s, textIndex, corpusIndex)) {
            MatchEntry2 match = new MatchEntry2(s, textIndex);
            ArrayList<Integer> indices = matchHashT.get(match);
            if (indices == null) {
                indices = new ArrayList();
            } else {
                matchList.remove(new MatchEntry3(s, textIndex, indices));
            }
            
            
            
            indices.add(corpusIndex);
            matchHashT.put(match, indices);
            
            MatchEntry3 match3 = new MatchEntry3(s, textIndex, indices);
            matchList.add(match3);
            System.out.println(match3);
            System.out.println("MatchList is: " + matchList);
        }

        
    }

    /**
     * Adds the given string as a match. If that string and textIndex already
     * exist as a MatchEntry, t adds the corpusIndices to the already existing
     * list of corpus indices for that MatchEntry
     *
     * @param s
     * @param textIndex
     * @param corpusIndices
     */
    public void addMatch(String s, int textIndex, ArrayList<Integer> corpusIndices) {

        MatchEntry2 match = new MatchEntry2(s, textIndex);
        ArrayList<Integer> indices = matchHashT.get(match);
        if (indices == null) {
            indices = new ArrayList();
        }
        indices.addAll(corpusIndices);

        matchHashT.put(match, indices);
    }

    public boolean contains(String s, int textIndex, int corpusIndex) {

        boolean foo = false;

        MatchEntry2 match = new MatchEntry2(s, textIndex);

        if (matchHashT.containsKey(match)) {
            ArrayList<Integer> indices = matchHashT.get(match);
            foo = indices.contains(corpusIndex);
        }
        return foo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Matches)) {
            return false;
        }

        Matches s = (Matches) o;

        return matchHashT.equals(s.matchHashT);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.matchHashT);
        return hash;
    }

    public boolean isEmpty() {
        return matchHashT.isEmpty();
    }

    @Override
    public String toString() {
        return matchList.toString();
    }

    @Override
    public Iterator iterator() {
        return matchList.iterator();
    }

    @Override
    public void forEach(Consumer action) {
        Iterable.super.forEach(action); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ArrayList<MatchEntry3> getMatchList() {
        return matchList;
    }

    
    
 
    
}
