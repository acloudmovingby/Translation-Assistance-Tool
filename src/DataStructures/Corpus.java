/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.ArrayList;

/**
 * 
 * A collection of BasicFiles. 
 * 
 * @author Chris
 */
public class Corpus {

    ArrayList<BasicFile> files;

    public Corpus() {
        files = new ArrayList();
    }

    public void addFile(BasicFile file) {
        files.add(file);
    }

    public ArrayList<BasicFile> getFiles() {
        return files;
    }

    public ArrayList<Segment> getAllCommittedSegs() {
        ArrayList<Segment> aList = new ArrayList();

        files.stream().forEach(f -> {
            aList.addAll(f.getActiveSegs());
            aList.addAll(f.getHiddenSegs());
        });
        aList.removeIf(seg -> !seg.isCommitted());

        return aList;
    }
    
    /**
     * Removes the file from the corpus. 
     * @param bf
     * @return
     */
    public boolean removeFile(BasicFile bf) {
        return getFiles().remove(bf);
    }

    public boolean contains(BasicFile file) {
        for (BasicFile f : getFiles()) {
            if (file.equals(f)) {
                return true;
            }
        }
        return false;
    }

    public BasicFile getFile(String fileName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        files.forEach((bf) -> {
            sb.append(bf.toString());
        });
        return sb.toString();
    }
    
    /**
     * Returns the total number of segs (both hidden and active) in all files of the corpus.
     * @return 
     */
    public int numTotalSegs() {
        int numSegs = 0;
        for (BasicFile bf : getFiles()) {
            numSegs = numSegs + bf.getActiveSegs().size();
            numSegs = numSegs + bf.getHiddenSegs().size();
        }
        return numSegs;
    }
    
    /**
     * Commits segment of every file in corpus. 
     */
    public void commitAllFiles() {
        for (BasicFile f : getFiles()) {
            f.commitAllSegs();
        }
    }
    
}
