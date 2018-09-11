/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;


import Files.CompareFile;
import Files.TMCompareEntry;
import Files.TMCorpus;
import Files.TMEntry;
import Files.TMFile;
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
    private final int NGRAM_LENGTH;
    private final NGramWrapper text;
    private final NGramWrapper corpus;
    private Matches matches;
    private CompareFile cFile;

    public Comparator(String text, String corpus, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;

        this.text = new NGramWrapper(text, this.NGRAM_LENGTH);
        this.corpus = new NGramWrapper(corpus, this.NGRAM_LENGTH);

        matches = new Matches();
        findMatchesOriginal();
       // cFile = buildCompareFile();

    }
    
    /**
     * THis constructor finds the matches between the text and a file, allowing Comparator to return those matches by means of a CompareFile.
     * @param text The untranslated text.
     * @param file The file that should be searched for matches. 
     * @param minMatchLength Minimum match length
     
    
    public Comparator(String text, AbstractTMFile file, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;
        
        
        This takes the text and the string representation of the file and turns them into Ngram.
        Later, if an index is found in the file, the TM segment for that index can be returned using the method getSingleTM or getMultipleTMs.
        
        this.text = new NGramWrapper(text, this.NGRAM_LENGTH);
        this.corpus = new NGramWrapper(file.getThai(), this.NGRAM_LENGTH); 
        
        String[] thaiSegs = file.getThaiSegments();
        
        // this goes through every segment in the target file and finds matches for that segment, building the TM entry to be added to the CompareFile
        for (int i=0; i<thaiSegs.length; i++) {
            Matches m = findMatches(text, thaiSegs[i]);
            Object[] tmMatch = new Object[cFile.getNumFields()];
            
            tmMatch[0] = 0;
            tmMatch[1] = thaiSegs[i];
                tmMatch[2] = file.getTM(i)[file.getThaiColumn()];
                tmMatch[3] = new int[m.totalCorpusMatches()][2];
                tmMatch[4] = file.getFileName();
                
            // this finds all the index ranges of where in the target segment (corpus) those matches exist
            for (MatchEntry3 m1 : m.getMatchList()) {
                for (int ind : m1.indices) {
                    tmMatch[3] = new int[] {
                        ind, m1.match.length()+ind
                    };
                }
               
            }
            
           // cFile.addCompareMatch(thaiSegs[i], file.getTM(i)[file.getEnglishColumn()], int[][])
            
        }
        
        
        
        
        
    }*/
    
    public Comparator(String text, TMFile file, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;
        ArrayList<TMEntry> tms = file.getTMs();
        
        //not necessary
        this.text = new NGramWrapper(text, NGRAM_LENGTH);
        this.corpus = new NGramWrapper(text, NGRAM_LENGTH);
        
        cFile = new CompareFile();
        
        for (TMEntry tm : tms) {
            Matches m = findMatches(text, tm.getThai());
            TMCompareEntry ce = new TMCompareEntry();
            ce.setThai(tm.getThai());
            ce.setEnglish(tm.getEnglish());
            ce.setFileName(file.getFileName());
            
            // WRONG
            for (MatchEntry3 me : m.getMatchList()) {
                ArrayList<Integer> indices = me.indices;
                int[] ia = new int[indices.size()];
                for (int i = 0; i<indices.size(); i++) {
                    ia[i] = indices.get(i);
                }
                ce.addMatchIntervals(ia);
            }
            cFile.addEntry(ce);
        }
        
    }
    
    public Comparator(String text, TMCorpus corpus, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;
        
        this.text = new NGramWrapper(text, this.NGRAM_LENGTH);
        this.corpus = new NGramWrapper(corpus.toString(), this.NGRAM_LENGTH);
        
        throw new UnsupportedOperationException();
    }

    public final Matches findMatches(String t1, String t2) {
        NGramWrapper n1 = new NGramWrapper(t1, NGRAM_LENGTH);
        NGramWrapper n2 = new NGramWrapper(t2, NGRAM_LENGTH);
        
        Matches ret = new Matches();
        
        for (int i = 0; i < n1.getList().size(); i++) {
            String s = n1.getList().get(i);

            // finds indices of all matching ngrams in corpus
            ArrayList<Integer> indices = n2.contains(s);

            if (indices != null) {
                for (int j : indices) {
                    
                    if (!checkPrior(i, j)) {

                        String segment = s;
                        
                        String foo1 = remaining(i, n1);
                        String foo2 = remaining(j, n2);
                        
                        segment = segment.concat(afters(foo1, foo2));
                        
                        ret.addMatch(segment, i, j);
                        
                            
                    }
                }
            }
        }
        return ret;
    }
    
    public void findMatchesOriginal() {
        
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

    private CompareFile buildCompareFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public CompareFile getCompareFile() {
        return cFile;
    }

    public NGramWrapper getText() {
        return text;
    }

    public NGramWrapper getCorpus() {
        return corpus;
    }
}
