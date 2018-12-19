/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import Files.BasicFile;
import Files.CompareFile;
import Files.TUCompareEntry;
import Files.FileList;
import Files.TUEntryBasic;
import java.util.List;

/**
 *
 * @author Chris
 */
public final class OrigComparator {

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
    private NGramWrapper text;
    private NGramWrapper corpus;
    private Matches matches;
    private CompareFile cFile;

    
    public OrigComparator(String text, String corpus, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;

        this.text = new NGramWrapper(text, this.NGRAM_LENGTH);
        this.corpus = new NGramWrapper(corpus, this.NGRAM_LENGTH);

        matches = new Matches();
        findMatchesOriginal();
        // cFile = buildCompareFile();

    }

    /**
     * THis constructor finds the matches between the text and a file, allowing
     * Comparator to return those matches by means of a CompareFile.
     *
     * @param text The untranslated text.
     * @param file The file that should be searched for matches.
     * @param minMatchLength Minimum match length
     *
     *
     * public Comparator(String text, AbstractTMFile file, int minMatchLength) {
     * NGRAM_LENGTH = minMatchLength;
     *
     *
     * This takes the text and the string representation of the file and turns
     * them into Ngram. Later, if an index is found in the file, the TM segment
     * for that index can be returned using the method getSingleTM or
     * getMultipleTMs.
     *
     * this.text = new NGramWrapper(text, this.NGRAM_LENGTH); this.corpus = new
     * NGramWrapper(file.getThai(), this.NGRAM_LENGTH);      *
     * String[] thaiSegs = file.getThaiSegments();
     *
     * // this goes through every segment in the target file and finds matches
     * for that segment, building the TM entry to be added to the CompareFile
     * for (int i=0; i<thaiSegs.length; i++) { Matches m = findStringMatches(text,
 thaiSegs[i]); Object[] tmMatch = new Object[cFile.getNumFields()];

 tmMatch[0] = 0; tmMatch[1] = thaiSegs[i]; tmMatch[2] =
 file.getTM(i)[file.getThaiColumn()]; tmMatch[3] = new
 int[m.totalCorpusMatches()][2]; tmMatch[4] = file.getFileName();

 // this finds all the index ranges of where in the target segment
 (corpus) those matches exist for (MatchEntry3 m1 : m.getMatchList()) {
 for (int ind : m1.indices) { tmMatch[3] = new int[] { ind,
 m1.match.length()+ind }; }

 }

 // cFile.addCompareMatch(thaiSegs[i],
 file.getTM(i)[file.getEnglishColumn()], int[][])

 }





 }
     */
    public OrigComparator(String text, BasicFile file, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;
        cFile = new CompareFile();
        findMatches(text, file);
    }

    public OrigComparator(String text, FileList fileList, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;
        cFile = new CompareFile();
        
        for (BasicFile file : fileList.getFiles()) {
           findMatches(text, file); 
        }
    }
    
    public OrigComparator(String text, TUEntryBasic tu, int minMatchLength) {
        NGRAM_LENGTH = minMatchLength;
        cFile = new CompareFile();
        
        findMatches(text, tu);
    }
    
    /**
     * Finds the matches between the text and the file and adds them to cFile (the compare file for this comparator object).
     * @param text The text
     * @param file The file to compare to.
     */
    private void findMatches(String text, BasicFile file) {
        
        List<TUEntryBasic> tms = file.getTUsToDisplay();

        for (TUEntryBasic tu : tms) {
            findMatches(text, tu);
        }
    }

    public void findMatches(String text, TUEntryBasic tu) {
        if (tu.isCommitted()) {
        Matches m = findStringMatches(text, tu.getThai());
            if (!m.isEmpty()) {
                TUCompareEntry ce = new TUCompareEntry();
                ce.setThai(tu.getThai());
                ce.setEnglish(tu.getEnglish());
                ce.setFileName(tu.getFileName());

                for (MatchEntry3 me : m.getMatchList()) {
                    int startIndex = -1;
                    int endIndex = -1;
                    for (int i = 0; i < me.indices.size(); i++) {
                        startIndex = me.indices.get(i);
                        endIndex = startIndex + me.match.length() - 1;
                    }
                    ce.addMatchInterval(startIndex, endIndex);
                }
                cFile.addEntry(ce);
            }
        }
    }
    
    Matches findStringMatches(String t1, String t2) {

        NGramWrapper n1 = new NGramWrapper(t1, NGRAM_LENGTH);
        NGramWrapper n2 = new NGramWrapper(t2, NGRAM_LENGTH);

        Matches ret = new Matches();

        for (int i = 0; i < n1.getList().size(); i++) {
            String s = n1.getList().get(i);

            // finds indices of all matching ngrams in corpus
            List<Integer> indices = n2.contains(s);

            if (indices != null) {
                for (int j : indices) {

                    if (!checkPrior(i, j, t1, t2)) {

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

    private void findMatchesOriginal() {

        // goes through each ngram in text
        for (int i = 0; i < text.getList().size(); i++) {
            String s = text.getList().get(i);

            // finds indices of all matching ngrams in corpus
            List<Integer> indices = corpus.contains(s);

            if (indices != null) {
                for (int j : indices) {

                    if (!checkPriorOriginal(i, j)) {

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

    private String priors(int textIndex, int corpusIndex) {
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

    private String afters(String t1, String t2) {

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
    private boolean checkPrior(int i, int j, String fileText, String corpusText) {

        if (i > 0 && j > 0) {
            return fileText.charAt(i - 1) == corpusText.charAt(j - 1);
        } else {
            return false;
        }

    }
    
    /**
     *
     * @param i text index of ngram match
     * @param j corpus index of ngram match
     * @return true if the prior character at i-1, j-1 is identical. Helps
     * prevent redundant match checking
     */
    private boolean checkPriorOriginal(int i, int j) {

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

        if (text.length() - i > NGRAM_LENGTH) {
            str = text.getText().substring(i + NGRAM_LENGTH);
        }
        return str;
    }

    private CompareFile buildCompareFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CompareFile getCompareFile() {
        return cFile;
    }

    private NGramWrapper getText() {
        return text;
    }

    private NGramWrapper getCorpus() {
        return corpus;
    }
}
