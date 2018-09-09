/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class Comparator {

    /*
    Game Plan
        (1) form ngrams class for each text
        (2) find ngram (potential) matches, (make ngram matches class?)
        (3) for each match:
            check if the prior character matches. If so, don't check further
            if not, then continue after the ngram to detect matching characters and store
            take finished match and add to matches object
     */
    // shingle length
    final int NGRAM_LENGTH;
    final NGramWrapper text;
    final NGramWrapper corpus;
    Matches matches;

    public Comparator(String text, String corpus, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;

        this.text = new NGramWrapper(text, this.NGRAM_LENGTH);
        this.corpus = new NGramWrapper(corpus, this.NGRAM_LENGTH);

        matches = new Matches();
        findMatches();

    }

    public void findMatches() {
        
        // goes through each ngram in text
        for (int i = 0; i < text.getList().size(); i++) {
            String s = text.getList().get(i);

            // finds indices of all matching ngrams in corpus
            ArrayList<Integer> indices = corpus.contains(s);

            if (indices != null) {
                for (int j : indices) {
                    
                    if (!checkPrior(i, j)) {

                        String segment = s;
                        
                        String foo1 = remaining(i, text);
                        String foo2 = remaining(j, corpus);
                        
                        segment = segment.concat(afters(foo1, foo2));
                        
                        matches.addMatch(segment, i, j);
                        
                            
                    }
                }
            }
        }

    }

    public String priors(int textIndex, int corpusIndex) {
        String priorMatch = "";
        for (int i = 1; i <= textIndex || i <= corpusIndex; i++) {
            try {
                String foo1 = text.getText().substring(textIndex - i, textIndex - i + 1);
                String foo2 = corpus.getText().substring(corpusIndex - i, corpusIndex - i + 1);

                if (foo1.equals(foo2)) {
                    priorMatch = foo1 + priorMatch;
                } else {
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                return priorMatch;
            }
        }
        return priorMatch;
    }

    public static String afters(String t1, String t2) {

        StringBuilder afterMatch = new StringBuilder(14);
        boolean finished = false;
        
      

        for (int i = 0; i < t1.length() && i < t2.length() && finished == false; i++) {

            if (t1.charAt(i) == t2.charAt(i)) {
                afterMatch.append(t1.charAt(i));

            } else {
                finished = true;
            }
        }
        return afterMatch.toString();
    }

    /**
     *
     * @param i text index of ngram match
     * @param j corpus index of ngram match
     * @return true if the prior character at i-1, j-1 is identical. Helps
     * prevent redundant match checking
     */
    private boolean checkPrior(int i, int j) {
        
        if (i > 0 && j > 0) {
            return text.getText().charAt(i - 1) == corpus.getText().charAt(j - 1);
        } else {
            return false;
        }

    }
    
    public Matches getMatches() {
        return matches;
    }

    private String remaining(int i, NGramWrapper text) {
        
        String str = "";
        
        if (text.length()-i>NGRAM_LENGTH) {
            str = text.getText().substring(i+NGRAM_LENGTH);
        }
        return str;
    }
}
