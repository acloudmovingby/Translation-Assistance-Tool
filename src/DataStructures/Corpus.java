/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;

/**
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

    public ArrayList<Segment> getAllCommittedTUs() {
        ArrayList<Segment> aList = new ArrayList();

        files.stream().forEach(f -> {
            aList.addAll(f.getActiveSegs());
            aList.addAll(f.getRemovedSegs());
        });
        aList.removeIf(tu -> !tu.isCommitted());

        return aList;
    }

    public boolean contains(String fileName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}