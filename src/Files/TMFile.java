/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.util.ArrayList;

import javafx.collections.ObservableList;

/**
 *
 * @author Chris
 * @param <T> Type of TU Entry
 */
public interface TMFile<T> {
 
    
    /**
     * 
     * @return A list of the TU entries (TUEntry objects).
     */
    public ArrayList<T> getTUs();
    
    /**
     *
     * @return An observable list of the TUs
     */
    public ObservableList<T> getObservableList();
    
    public String getFileName();
    
}
