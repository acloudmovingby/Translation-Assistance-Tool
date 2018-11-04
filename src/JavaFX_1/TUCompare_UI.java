/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Chris
 */
public class TUCompare_UI extends TUEntry_UI {
    private final StringProperty thaiField = new SimpleStringProperty();
    private final StringProperty englishField = new SimpleStringProperty();
    private ArrayList<int[]> matchIntervals = new ArrayList();
    private final StringProperty fileName = new SimpleStringProperty();

    public TUCompare_UI(String thai, String english, ArrayList<int[]> matchIntervals, String fileName) {
        super(thai, english);
        setMatchIntervals(matchIntervals);
        this.fileName.set(fileName);
    }
    
    public ArrayList<int[]> getMatchIntervals() { return matchIntervals; }
    public void setMatchIntervals(ArrayList<int[]> matchIntervals) { this.matchIntervals = matchIntervals; }
    
   
}
