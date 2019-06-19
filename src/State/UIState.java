/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import DataStructures.TranslationFile;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;

/**
 * Provides all the data that will be displayed to the user. Is currently put in the State module, because all changes to UIState derive from changes in State.
 * 
 * All properties are final, but they can be mutated (because they are JavaFX properties). They are final because the JavaFX UI components need to bind to them and so they shouldn't change (although their contents will of course). 
 *
 * @author Chris
 */
public class UIState {

    public ObservableList<TranslationFile> getAllFiles() {
        return allFilesInCorpus;
    }

    private final ObservableList<Segment> mainFileSegs;
    private final StringProperty mainFileName;
    private final ObservableList<MatchSegment> matchList;
    private final IntegerProperty numMatches;
    private final ObservableList<TranslationFile> allFilesInCorpus;
    private int minMatchLength;
    private static final Font DEFAULT_THAI_FONT = Font.font("Arial");
    private static final Font DEFAULT_ENGLISH_FONT = Font.font("Arial");
    

    public UIState() {
        mainFileSegs = FXCollections.observableArrayList();
        matchList = FXCollections.observableArrayList();
        numMatches = new SimpleIntegerProperty(0);
        allFilesInCorpus = FXCollections.observableArrayList();
        mainFileName = new SimpleStringProperty("");
        minMatchLength = 0;
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
    
    public void setAllFilesInCorpus(List<TranslationFile> files) {
        allFilesInCorpus.setAll(files);
    }
    
    public StringProperty getMainFileName() {
        return mainFileName;
    }
    
    protected void setMainFileName(String mainFileName) {
        this.mainFileName.set(mainFileName);
    }

    public String getMinMatchLength() {
        return String.valueOf(minMatchLength);
    }

    void setMinMatchLength(int minMatchLength) {
        this.minMatchLength = minMatchLength;
    }
}
