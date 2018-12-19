/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import Database.DatabaseOperations;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A basic file that stores a list of TMBasicEntry's, which each contain just a
 * Thai and an English field.
 *
 * @author Chris
 */
public class BasicFile {

    // the TUs to be displayed to the user
    private ObservableList<TUEntryBasic> tusToDisplay;
    private ArrayList<TUEntryBasic> removedTUs;
    //  private final int NUM_FIELDS;
    private String fileName;
    private final int fileID;

    /**
     * When creating a new BasicFile, the fileID is auto-generated and a default
     * fileName of "untitled" is assigned.
     */
    public BasicFile() {
        tusToDisplay = FXCollections.observableArrayList();
        removedTUs = new ArrayList();
        fileName = "untitled";
        fileID = DatabaseOperations.createFileID(fileName);
    }

    /**
     * Used when rebuilding a file from the database.
     *
     * @param fileID
     * @param fileName
     */
    public BasicFile(int fileID, String fileName) {
        this.fileID = fileID;
        this.fileName = fileName;
        tusToDisplay = FXCollections.observableArrayList();
        removedTUs = new ArrayList();
    }

    public TUEntryBasic newTU() {
        // makes id that matches this file and assigns the fileid
        TUEntryBasic newTU = new TUEntryBasic(getFileID());
        // adds to end of lists
        addTUAtEnd(newTU);
        // returns the TU so it can be modified by the user (i.e. Thai/English can be added)
      
        return newTU;
    }

    /**
     * Helper method to add new TUs to the various lists.
     *
     * @param tu
     */
    private void addTUAtEnd(TUEntryBasic tu) {
        if (tu.isRemoved()) {
            removedTUs.add(tu);
        } else {
            ObservableList<TUEntryBasic> dispTUs = getTUsToDisplay();
            int newRank;
            if (dispTUs.isEmpty()) {
                newRank = Integer.MIN_VALUE + 8192;
            } else {
                newRank = 8192 + dispTUs.get(dispTUs.size()-1).getRank();
            }
            dispTUs.add(tu);
            tu.setRank(newRank);
        }
    }
    

    /**
     * ************************************************************************************
     *
     *
     * METHODS FOR CHANGING EXISTING TUS - removing, inserting, merging, and
     * splitting TUs
     *
     *
     *********************************************************************************
     */
    /**
     * Splits a TU into two pieces. The second TU begins with the character at
     * the specified index. So if this method was given "unhappy" and 2, the two
     * new TUs would be "un" and "happy".
     *
     * @param tu
     * @param splitIndex
     */
    public void splitTU(TUEntryBasic tu, int splitIndex) {
        if (tu == null || splitIndex == 0 || splitIndex == tu.getThai().length()) {
            return;
        }
        String firstThai = tu.getThai().substring(0, splitIndex);
        String secondThai = tu.getThai().substring(splitIndex);

        // retrieves index of old TU
        int index = tusToDisplay.indexOf(tu);

        // creates first new TU and inserts
        TUEntryBasic newTU1 = new TUEntryBasic(getFileID());
        newTU1.setThai(firstThai);
        newTU1.setEnglish(tu.getEnglish());
        newTU1.setCommitted(false);
        getTUsToDisplay().add(index, newTU1);

        // creates second new TU and inserts
        TUEntryBasic newTU2 = new TUEntryBasic(getFileID());
        newTU2.setThai(secondThai);
        newTU2.setEnglish("");
        newTU2.setCommitted(false);
        getTUsToDisplay().add(index + 1, newTU2);

        //removes old TU
        removeTU(tu);

        // DATABASE
        realignRanks();
    }

    public void mergeTUs(List<TUEntryBasic> tusToMerge) {

        if (tusToMerge.size() < 2) {
            return;
        }

        // Add to removed list
        for (TUEntryBasic tu : tusToMerge) {
            tu.setRemoved(true);
            System.out.println("?" + tu.isRemoved());
            this.getRemovedTUs().add(tu);
        }

        // Build new TU 
        StringBuilder thaiSB = new StringBuilder();
        StringBuilder engSB = new StringBuilder();
        for (TUEntryBasic tu : tusToMerge) {
            System.out.println("?" + tu.isRemoved());
            thaiSB.append(tu.getThai());
            engSB.append(tu.getEnglish());
        }
        TUEntryBasic newTU = new TUEntryBasic(DatabaseOperations.makeTUID(), getFileID());
        newTU.setThai(thaiSB.toString());
        newTU.setEnglish(engSB.toString());

        // Remove old TUs from display list
        int firstIndex = this.getTUsToDisplay().indexOf(tusToMerge.get(0));
        int size = tusToMerge.size();
        for (int i = 0; i < size; i++) {
            TUEntryBasic removedTU = getTUsToDisplay().remove(firstIndex);
        }

        // Insert new TU
        insertTU(firstIndex, newTU);
        //getTUsToDisplay().add(firstIndex, newTU);
        //realignRanks();

    }

    private void removeTU(TUEntryBasic tu) {
        tu.setRemoved(true);
        removedTUs.add(tu);
        tusToDisplay.remove(tu);
    }
    
    /**
     * Adds the TU at the proper index in TUsToDisplay and sets it's rank accordingly
     * @param index
     * @param tu 
     */
    private void insertTU(int index, TUEntryBasic tu) {
        int priorRank;
        int nextRank;
        
        if (index == getTUsToDisplay().size()-1 || getTUsToDisplay().isEmpty()) {
            addTUAtEnd(tu);
            return;
        } else if (index == 0) {
            priorRank = Integer.MIN_VALUE;
        } else {
            priorRank = getTUsToDisplay().get(index-1).getRank();
        }
        
        nextRank = getTUsToDisplay().get(index).getRank();
        int newRank = (nextRank+priorRank)/2;
        tu.setRank(newRank);
        getTUsToDisplay().add(index, tu);
        
        if (nextRank-priorRank < 2) {
            System.out.println("Ranks realigned");
            realignRanks();
        }

    }

    private void realignRanks() {
        // updates tusToDisplay in DB
        if (!getTUsToDisplay().isEmpty()) {
            getTUsToDisplay().get(0).setRank(Integer.MIN_VALUE + 8192);
        }
        for (int i = 1; i < getTUsToDisplay().size(); i++) {
            TUEntryBasic tu = getTUsToDisplay().get(i);
            tu.setRank(getTUsToDisplay().get(i-1).getRank());
            DatabaseOperations.addOrUpdateTU(tu);
        }

        // updates removedTUs in DB
        for (int i = 0; i < getRemovedTUs().size(); i++) {
            TUEntryBasic tu = getRemovedTUs().get(i);
            DatabaseOperations.addOrUpdateTU(tu);

        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        DatabaseOperations.addOrUpdateFileName(fileID, fileName);
    }

    public void commitAllTUs() {
        for (TUEntryBasic tu : getTUsToDisplay()) {
            tu.setCommitted(true);
            // DATABASE
            DatabaseOperations.addOrUpdateTU(tu);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof BasicFile)) {
            return false;
        }

        BasicFile m = (BasicFile) o;

        // tests equality of all TMs within files
        boolean areTMsEqual = true;
        if (m.getTUsToDisplay().size() != this.getTUsToDisplay().size()) {
            return false;
        } else {
            Iterator i1 = this.getTUsToDisplay().iterator();
            Iterator i2 = m.getTUsToDisplay().iterator();
            while (i1.hasNext()) {
                if (!i1.next().equals(i2.next())) {
                    areTMsEqual = false;
                }
            }
        }

        return this.getFileName().equals(m.getFileName())
                && areTMsEqual;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(getTUsToDisplay());
        // hash = 23 * hash + this.NUM_FIELDS;
        hash = 23 * hash + Objects.hashCode(getFileName());
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");

        for (TUEntryBasic tu : getTUsToDisplay()) {
            sb.append(tu.toString());
            sb.append("\n\t");
        }
        return sb.toString();
    }

    public ObservableList<TUEntryBasic> getTUsToDisplay() {
        return tusToDisplay;
    }

    public int getFileID() {
        return fileID;
    }

    /**
     * Assigns an id value to the TU. Only for when the TU is being added to the
     * end of the list. The id increments 256 higher than the prior one.
     *
     * @return
     */
    private int makeTUID() {
        // FIX
        if (tusToDisplay.isEmpty()) {
            return fileID + 256;
        } else {
            return ((TUEntryBasic) tusToDisplay.get(tusToDisplay.size() - 1)).getID() + 256;
        }

    }

    public ArrayList<TUEntryBasic> getRemovedTUs() {
        return removedTUs;
    }

}
