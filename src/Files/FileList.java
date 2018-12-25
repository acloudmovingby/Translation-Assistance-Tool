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
public class FileList {

    ArrayList<BasicFile> files;

    public FileList() {
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
            aList.addAll(f.getTUsToDisplay());
            aList.addAll(f.getRemovedTUs());
        });
        aList.removeIf(tu -> !tu.isCommitted());

        return aList;
    }
}
