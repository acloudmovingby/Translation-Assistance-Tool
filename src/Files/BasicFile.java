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

    ObservableList<TUEntryBasic> observableList;
    //  private final int NUM_FIELDS;
    private String fileName;
    private final double fileID;

    /**
     * When creating a new BasicFile, the fileID is auto-generated and a default
     * fileName of "untitled" is assigned.
     */
    public BasicFile() {
        observableList = FXCollections.observableArrayList();
        fileName = "untitled";
        fileID = DatabaseOperations.createFileID(fileName);
    }

    /**
     * Used when rebuilding a file from the database.
     *
     * @param fileID
     * @param fileName
     */
    public BasicFile(double fileID, String fileName) {
        this.fileID = fileID;
        this.fileName = fileName;
        observableList = FXCollections.observableArrayList();
    }

    public TUEntryBasic newTU() {
        // makes id that matches this file and assigns the fileid
        TUEntryBasic newTU = new TUEntryBasic(makeTUID(), getFileID());
        // adds to end of list
        observableList.add(newTU);
        // returns the TU so it can be modified by the user (i.e. Thai/English can be added)
        return newTU;
    }

    /**
     * Used for reconstructing files from the database.
     *
     * @param tu
     */
    public void addTU(TUEntryBasic tu) {
        observableList.add(tu);
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
     * Inserts a TU at the specified position in this list. Shifts the element
     * currently at that position (if any) and any subsequent elements to the
     * right (adds one to their indices). Returns the TU that was created.
     *
     * @param index
     * @return The newly created TU
     */
    private TUEntryBasic insertTU(int index) {
        if (index == observableList.size()) {
            return newTU();
        } else {
            double newID;

            if (index == 0) {
                double nextID = observableList.get(index).getID();
                newID = (nextID + getFileID()) / 2;
            } else {
                double priorID = observableList.get(index - 1).getID();
                double nextID = observableList.get(index).getID();
                newID = (priorID + nextID) / 2;
            }
            System.out.println("newID = " + newID);
            System.out.println("does id exist? " + DatabaseOperations.containsID(newID));
            for (TUEntryBasic tu1 : observableList) {
                System.out.println("\t" + tu1.getID());
            }
            TUEntryBasic newTU = new TUEntryBasic(newID, getFileID());
            observableList.add(index, newTU);
            return newTU;
        }
    }

    /**
     * Removes the TU from the list. Sets the boolean isRemoved to true and
     * updates the TU in the database.
     *
     * @param tu
     */
    private TUEntryBasic removeTU(int index) {
        TUEntryBasic removedTU = observableList.remove(index);
        removedTU.setRemoved(true);
        return removedTU;
    }

    public void changeThai(TUEntryBasic tu, String newThaiText) {
        // retrieves index of old TU
        int index = observableList.indexOf(tu);

        // inserts new TU andd updates information
        TUEntryBasic insertedTU = insertTU(index);
        insertedTU.setThai(newThaiText);
        insertedTU.setEnglish(tu.getEnglish());
        insertedTU.setCommitted(tu.isCommitted());

        // removes the previous TU
        TUEntryBasic removedTU = removeTU(observableList.indexOf(tu));

        // DATABASE
        DatabaseOperations.addOrUpdateTU(removedTU);
        DatabaseOperations.addOrUpdateTU(insertedTU);
    }

    /**
     * Splits a TU into two pieces. The second TU begins with the character at
     * the specified index. So if this method was given "unhappy" and 2, the two
     * new TUs would be "un" and "happy".
     *
     * @param tu
     * @param splitIndex
     */
    public void splitTU(TUEntryBasic tu, int splitIndex) {
        String firstThai = tu.getThai().substring(0, splitIndex);
        String secondThai = tu.getThai().substring(splitIndex);

        // retrieves index of old TU
        int index = observableList.indexOf(tu);

        // inserts first TU andd updates information
        TUEntryBasic insertedTU1 = insertTU(index);
        insertedTU1.setThai(firstThai);
        insertedTU1.setEnglish(tu.getEnglish());
        insertedTU1.setCommitted(false);

        // inserts second TU and updates information
        TUEntryBasic insertedTU2 = insertTU(index + 1);
        insertedTU2.setThai(secondThai);
        insertedTU2.setEnglish("");
        insertedTU2.setCommitted(false);

        //removes old TU
        TUEntryBasic removedTU = removeTU(observableList.indexOf(tu));

        // DATABASE
        DatabaseOperations.addOrUpdateTU(removedTU);
        DatabaseOperations.addOrUpdateTU(insertedTU1);
        DatabaseOperations.addOrUpdateTU(insertedTU2);
    }

    public void mergeTU(List<TUEntryBasic> tuList) {
  System.out.println("Before: " + DatabaseOperations.numberOfTUs());
        StringBuilder thaiSB = new StringBuilder();
        StringBuilder engSB = new StringBuilder();
        int firstTUIndex = observableList.indexOf(tuList.get(0));

        for (TUEntryBasic tu : tuList) {
            thaiSB.append(tu.getThai());
            engSB.append(tu.getEnglish());
        }

        // removes each of the selected TUs from the file's observablelist
        int numberOfTUs = tuList.size();
        ArrayList<TUEntryBasic> removedTUs = new ArrayList();
        for (int i = 0; i < numberOfTUs; i++) {
            removedTUs.add(removeTU(firstTUIndex));
        }

        // inserts new TU into place and adds features
       System.out.println("Mid1: " + DatabaseOperations.numberOfTUs());
        TUEntryBasic newTU = insertTU(firstTUIndex);
        newTU.setThai(thaiSB.toString());
        newTU.setEnglish(engSB.toString());
        newTU.setCommitted(false);
 System.out.println("Mid2: " + DatabaseOperations.numberOfTUs());
        // DATABASE
        removedTUs.forEach((removedTU1) -> {
            DatabaseOperations.addOrUpdateTU(removedTU1);
        });
        
        System.out.println(DatabaseOperations.addOrUpdateTU(newTU));
        System.out.println("After: " + DatabaseOperations.numberOfTUs());
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        DatabaseOperations.addOrUpdateFileName(fileID, fileName);
    }

    public void commitAllTUs() {
        for (TUEntryBasic tu : getObservableList()) {
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
        if (m.getObservableList().size() != this.getObservableList().size()) {
            return false;
        } else {
            Iterator i1 = this.getObservableList().iterator();
            Iterator i2 = m.getObservableList().iterator();
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
        hash = 23 * hash + Objects.hashCode(getObservableList());
        // hash = 23 * hash + this.NUM_FIELDS;
        hash = 23 * hash + Objects.hashCode(getFileName());
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");

        for (TUEntryBasic tu : getObservableList()) {
            sb.append(tu.toString());
            sb.append("\n\t");
        }
        return sb.toString();
    }

    /*
    public ArrayList getTUs() {
        return new ArrayList(observableList);
    }
     */
    public ObservableList<TUEntryBasic> getObservableList() {
        return observableList;
    }

    public Object[][] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getFileID() {
        return fileID;
    }

    /**
     * Assigns an id value to the TU. The id value is equal to the fileID plus
     * increments of 0.0001 Each time a new TU is added to the end of the list
     * its id is assigned to be 0.0001 higher than the prior one.
     *
     * @return
     */
    private double makeTUID() {
        if (observableList.isEmpty()) {
            return fileID + 0.0001;
        } else {
            return ((TUEntryBasic) observableList.get(observableList.size() - 1)).getID() + 0.0001;
        }

    }

}
