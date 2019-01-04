/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

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
}
