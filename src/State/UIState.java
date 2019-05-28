/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.BasicFile;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;

/**
 * Provides all the data that will be displayed to the user.
 *
 * @author Chris
 */
public class UIState {

    public ObservableList<BasicFile> getAllFiles() {
        return allFilesInCorpus;
    }

    private ObservableList<Segment> mainFileSegs;
    private String mainFileName;
    private final ObservableList<MatchSegment> matchList;
    private final IntegerProperty numMatches;
    private final ObservableList<BasicFile> allFilesInCorpus;
    private static final Font DEFAULT_THAI_FONT = Font.font("Arial");
    private static final Font DEFAULT_ENGLISH_FONT = Font.font("Arial");
    

    public UIState() {
        mainFileSegs = FXCollections.observableArrayList();
        matchList = FXCollections.observableArrayList();
        numMatches = new SimpleIntegerProperty(0);
        allFilesInCorpus = FXCollections.observableArrayList();
        mainFileName = "";
    }

    /**
     * Returns the list of segments from the main file (the file currently being
     * translated).
     *
     * @return
     */
    public ObservableList<Segment> getMainFileSegs() {
        return mainFileSegs;
    }
    
    /**
     * Should be used when a new file is made the main file.
     * 
     * Because this is a link to a segment list contained with the file, as the file's segments are removed/added, this will reflect that automatically (no need to call this every time the user does some file-affecting action).
     *
     * @param segList 
     */
    protected void setMainFileSegs(ObservableList<Segment> segList) {
        this.mainFileSegs.setAll(segList);
    }
    
    

    /**
     * Returns the list of matching segments to be shown in the bottom part of
     * the application window.
     *
     * @return
     */
    public ObservableList<MatchSegment> getMatchList() {
        return matchList;
    }

    protected void setMatchList(List<MatchSegment> newMatchList) {
        matchList.setAll(newMatchList);
        setNumMatches(getMatchList().size());
    }

    public IntegerProperty getNumMatchesProperty() {
        return numMatches;
    }

    private void setNumMatches(int x) {
        numMatches.set(x);
    }

    public static Font getThaiFont() {
        return DEFAULT_THAI_FONT;
    }

    public static Font getEnglishFont() {
        return DEFAULT_ENGLISH_FONT;
    }
    
    public void setAllFilesInCorpus(List<BasicFile> files) {
        allFilesInCorpus.setAll(files);
    }
    
    public String getMainFileName() {
        return mainFileName;
    }
    
    protected void setMainFileName(String mainFileName) {
        this.mainFileName = mainFileName;
    }
}
