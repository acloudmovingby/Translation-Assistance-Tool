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
    HashMap<Integer, TUEntryBasic> idHash;

    public FileList() {
        files = new ArrayList();
        idHash = new HashMap();
    }

    public void addFile(BasicFile file) {
        files.add(file);
        file.getTUsToDisplay().forEach(tu -> idHash.put(tu.getID(), tu));
        file.getRemovedTUs().forEach(tu -> idHash.put(tu.getID(), tu));
    }

    public ArrayList<BasicFile> getFiles() {
        return files;
    }

    public ArrayList<TUEntryBasic> getAllCommittedTUs() {
        ArrayList<TUEntryBasic> aList = new ArrayList();

        files.stream().forEach(f -> {
            aList.addAll(f.getTUsToDisplay());
            aList.addAll(f.getRemovedTUs());
        });
        aList.removeIf(tu -> !tu.isCommitted());

        return aList;
    }

    public TUEntryBasic getTU(Integer id) {
        return idHash.get(id);
    }
}
