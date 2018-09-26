/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Chris
 */
public class NGramWrapper implements Iterable {

    String text;
    int ngramLength;    
    HashMap<String, ArrayList<Integer>> hash;
    List<String> list;

    /**
     *
     * @param text Text to be divided into ngramLength-grams
     * @param n Length of ngramLength-gram.
     * @throws IllegalArgumentException if ngramLength<1
     */
    public NGramWrapper(String text, int n) throws IllegalArgumentException {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        this.text = text;
        this.ngramLength = n;
        hash = new HashMap();
        list = new ArrayList();
        makeNGrams();
    }
    
    private void makeNGrams() {
        
        if (text.length() < ngramLength) {
            addToHash(text, 0);
            list.add(text);
        } else {
            for (int i = 0; i <= text.length() - ngramLength; i++) {
                String str;
                
                str = text.substring(i, i + ngramLength);
                addToHash(str, i);
                list.add(str);
            }
        }
    }

    /**
     *
     * @param str The ngramLength-gram to add to the hashtable
     * @param i The index at which that ngram exists in the text
     */
    private void addToHash(String str, int i) {
        
        ArrayList<Integer> indices = hash.get(str);
        if (indices == null) {
            indices = new ArrayList();
            indices.add(i);
            hash.put(str, indices);
        } else {
            indices.add(i);
            hash.replace(str, indices);
        }
    }

    /**
     *
     * @param ngram
     * @return list of indices of where that ngram exists, if it doesn't exist,
     * it returns null
     */
    public ArrayList<Integer> contains(String ngram) {
        return hash.get(ngram);
    }

    /**
     *
     * @return String of text stored
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @return the length of ngram for this NGramWrapper object
     */
    public int getK() {
        return ngramLength;
    }
    
    public HashMap<String, ArrayList<Integer>> getHashMap() {
        return hash;
    }
    
    public List<String> getList() {
        return list;
    }
    
    @Override
    public String toString() {
        return list.toString();
    }

    /**
     *
     * @return The total number of ngrams stored
     */
    public int size() {
        return list.size();
    }

    /**
     *
     * @return The number of unique ngrams in the text.
     */
    public int uniqueSize() {
        return hash.size();
    }

    /**
     *
     * @return The length of the original string
     */
    public int length() {
        return text.length();
    }
    
    @Override
    public Iterator iterator() {
        return list.listIterator();
    }
}
