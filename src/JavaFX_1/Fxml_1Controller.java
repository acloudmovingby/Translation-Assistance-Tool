package JavaFX_1;

import State.StateBuilder;
import DataStructures.BasicFile;
import DataStructures.FileBuilder;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import DataStructures.SegmentBuilder;
import Database.DatabaseOperations;
import State.TopLevelBackEnd;
import State.UIState;
import UserActions.Commit;
import UserActions.EditEnglish;
import UserActions.EditThai;
import UserActions.Merge;
import UserActions.Split;
import UserActions.Uncommit;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

/**
 * FXML Controller class does: (1) registers user input to pass on to back end and (2) controls UI with any components that can't be done in CSS/FXML (such as dynamic content that I found hard to do in the SceneBuilder).
 *
 * JavaFX, the GUI used in this application, relies on working together with an
 * FXML document and a CSS stylesheet to properly render the application UI. The
 *
 * @FXML labels indicate that a given variable is linked to an id in the FXML
 * document (and therefore its name cannot be changed unless also changed in the
 * FXML doc).
 *
 * One of the least well organized classes in the program...so beware.
 *
 * @author Chris
 */
public class Fxml_1Controller implements Initializable {

    /**
     * *****************
     *
     * TAB FXML OBJECTS (the actual tabs you click on in the TabPane)
     *
     ******************
     */
    @FXML
    TabPane mainTabPane;

    @FXML
    Tab homeTab;

    @FXML
    Tab translationTab;

    @FXML
    Tab analysisTab;

    /**
     * *****************
     *
     * HOME TAB FXML OBJECTS
     *
     *******************
     */

    /*
    Represents the area where the new file and the import tmx icons are. 
     */
    @FXML
    HBox homeTopIcons;
    
    /*
    Represents the area of the home page where you click on icons to load prior projects. 
     */
    @FXML
    FlowPane loadFileFlowPane;

    

    /**
     * *****************
     *
     * TRANSLATION TAB FXML OBJECTS
     *
     ****************
     */
    @FXML
    Label title;

    @FXML
    TableView<Segment> tableView;

    @FXML
    TableColumn<Segment, Integer> idCol;

    @FXML
    TableColumn<Segment, String> thaiCol;

    @FXML
    TableColumn<Segment, String> englishCol;

    @FXML
    TableColumn<Segment, Boolean> status;

    @FXML
    TableColumn<Segment, String> matchScore;

    @FXML
    TableColumn<MatchSegment, String> thaiColComp;

    @FXML
    TableColumn<MatchSegment, String> englishColComp;

    @FXML
    TableColumn<MatchSegment, String> fileColComp;

    @FXML
    TableColumn<MatchSegment, String> scoreColComp;

    @FXML
    TableView<MatchSegment> compareTable;

    @FXML
    TextField minMatchLengthField;

    @FXML
    TextField searchField;

    @FXML
    Label numMatches;

    @FXML
    Button splitButton;

    @FXML
    Button mergeButton;

    @FXML
    Button commitButton;

    @FXML
    Button uncommitButton;

    @FXML
    Button undoButton;

    @FXML
    Button redoButton;

    @FXML
    Button exportButton;
    
    /**
     * ****************
     * 
     * Back End Objects
     * 
     * ***************
     */

    String committedStatusColor;
    String unCommittedStatusColor;

    TopLevelBackEnd backEnd;
    UIState uiState;

    /**
     * Initializes the controller class. I don't know what url/rb are used for (something to do with JavaFX)
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // builds a main file from the specified Thai document
        FileBuilder fileBuilder = new FileBuilder();
        //String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/ABCTestSimple.txt";
        //String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/ABCTest.txt";
        String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/FanSafety.txt";
        BasicFile mainFile = fileBuilder.justThaiFilePath(filePath);

        // actually builds all the important objects for the program and sets the items for the tables to display
        setMainFile(mainFile);

        /*
        *****************
        *
        SIDE BAR BUTTONS
        *
        ***************
         */
        // image for HOME TAB
        ImageView homeButtonLightBlueIV = new ImageView(getClass().getResource("/JavaFX_1/HomeButtonLightBlue.png").toExternalForm());
        homeButtonLightBlueIV.setFitWidth(50);
        homeButtonLightBlueIV.setPreserveRatio(true);
        homeButtonLightBlueIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        homeButtonLightBlueIV.setCache(true); // perhaps not necessary
        Label homeTabLabel = new Label("Home", homeButtonLightBlueIV);
        homeTabLabel.setMaxWidth(40);
        homeTabLabel.getStyleClass().add("homeTabLabel");
        homeTab.setGraphic(homeTabLabel);
        homeTab.setText("");

        // image for TRANSLATION TAB
        ImageView translationButtonWhiteIV = new ImageView(getClass().getResource("/JavaFX_1/TranslationButtonWhite.png").toExternalForm());
        translationButtonWhiteIV.setFitWidth(50);
        translationButtonWhiteIV.setPreserveRatio(true);
        translationButtonWhiteIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        translationButtonWhiteIV.setCache(true); // perhaps not necessary
        Label translationTabLabel = new Label("", translationButtonWhiteIV);
        translationTabLabel.setText("Translation");
        translationTabLabel.setMaxWidth(40);
        translationTab.setGraphic(translationTabLabel);
        translationTab.setText("");

        // image for ANALYSIS TAB 
        ImageView analysisIV = new ImageView(getClass().getResource("/JavaFX_1/PieChartButton.png").toExternalForm());
        analysisIV.setFitWidth(50);
        analysisIV.setPreserveRatio(true);
        analysisIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        analysisIV.setCache(true); // perhaps not necessary
        Label analysisTabLabel = new Label("", analysisIV);
        analysisTabLabel.setText("Analysis");
        analysisTabLabel.setMaxWidth(40);
        analysisTab.setGraphic(analysisTabLabel);
        analysisTab.setText("");

        /**
         * *****************
         *
         * HOME TAB UI INITIALIZATION
         *
         *******************
         */
        // NEW FILE ICON + IMPORT TMX ICON
        Image newFileIcon = new Image(getClass().getResource("/JavaFX_1/NewFileIcon.png").toExternalForm());
        ImageView newFileIconIV = new ImageView(newFileIcon);
        newFileIconIV.setFitWidth(110);
        newFileIconIV.setPreserveRatio(true);
        Button newFileLabel = new Button("New", newFileIconIV);
        newFileLabel.setMinWidth(130);
        newFileLabel.setMaxWidth(130);
        newFileLabel.setGraphicTextGap(8);
        newFileLabel.setContentDisplay(ContentDisplay.TOP);
        newFileLabel.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(JavaFX_1.stage);
            if (file != null) {
                this.setMainFile(FileBuilder.justThaiFilePath(file.getAbsolutePath()));
            }
        }
        );
        homeTopIcons.getChildren().add(newFileLabel);

        // LOAD FILE ICONS
        Image loadFileIconImage = new Image(getClass().getResource("/JavaFX_1/BlankFileIcon.png").toExternalForm());

        // Creates the actual icons (which are buttons to open a file for translation) and include the standard blank file icon image, the name of the file, and any listeners (?)
        for (BasicFile file : uiState.getAllFiles()) {
            ImageView loadFileIconIV = new ImageView(loadFileIconImage);
            loadFileIconIV.setFitWidth(110);
            loadFileIconIV.setPreserveRatio(true);
            Button loadFileIcon = new Button(file.getFileName(), loadFileIconIV);
            loadFileIcon.setMinWidth(130);
            loadFileIcon.setMaxWidth(130);
            loadFileIcon.setPrefWidth(130);
            loadFileIcon.setGraphicTextGap(8);
            loadFileIcon.setContentDisplay(ContentDisplay.TOP);
            loadFileFlowPane.getChildren().add(loadFileIcon);
            // the following handles the events when the user, in the home tab, clicks on a file to open 
            loadFileIcon.setOnAction(e -> {
                setMainFile(file); // resets the main file
                mainTabPane.getSelectionModel().select(translationTab); // switches the view from the home tab to the translation tab
            });
            loadFileIcon.getStyleClass().add("loadFileButton");
        }

        // BELOW IS ONLY UI INITIALIZIATION THAT DOESN'T DEPEND ON MAIN FILE
        // Because the main file can be changed mid program operation, all UI elements that depend on state/UIstate/TopLevelBackEnd need to be in the method setMainFile so they are reset when the main file is changed
        committedStatusColor = "rgb(183, 215, 255)";
        unCommittedStatusColor = "rgb(255, 255, 255)";

        // Set prompt text for search field
        searchField.setPromptText("search...");

        // MAIN FILE VIEWER COLUMNS
        // id column:
        idCol.setCellValueFactory((CellDataFeatures<Segment, Integer> p) -> new ReadOnlyObjectWrapper(tableView.getItems().indexOf(p.getValue())));
        idCol.setSortable(false);

        // Thai column:
        thaiCol.setCellValueFactory(new PropertyValueFactory<>("thai"));
        thaiCol.setMinWidth(80);
        thaiCol.setEditable(true);
        /*
        // The following just simply displays the Thai text (it is not editable or selectable)
        thaiCol.setCellFactory(tc -> {
            TableCell<Segment, String> cell = new TableCell<>();
            Text text = new Text();
            text.setFont(UIState.getThaiFont());
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(thaiCol.widthProperty()); // ensures that the text wraps at the column width
            text.textProperty().bind(cell.itemProperty());
            cell.setEditable(true);
            return cell;
        });*/

        EditableCellFactory cf = new EditableCellFactory();
        cf.setFont(UIState.getEnglishFont()); // should actually do in CSS

        thaiCol.setCellFactory(cf);
        thaiCol.setOnEditCommit(e -> {
            int row = e.getTablePosition().getRow();
            Segment editedSeg = tableView.getItems().get(row);
            if (editedSeg != null) {
                SegmentBuilder sb = new SegmentBuilder(editedSeg);
                editedSeg = sb.createSegment();
                backEnd.acceptAction(new EditThai(editedSeg, e.getNewValue()));
            }
        });

        // English column:
        englishCol.setCellFactory(cf);
        englishCol.setOnEditCommit(e -> {
            int row = e.getTablePosition().getRow();
            Segment editedSeg = tableView.getItems().get(row);
            if (editedSeg != null) {
                SegmentBuilder sb = new SegmentBuilder(editedSeg);
                editedSeg = sb.createSegment();
                backEnd.acceptAction(new EditEnglish(editedSeg, e.getNewValue()));
            }
        }
        );
        englishCol.setCellValueFactory(new PropertyValueFactory<>("english"));

        // Status column:
        // makes it so this columns cellValueFactory is bound to the isCommitted() method of Segment
        status.setCellValueFactory(cellData -> cellData.getValue().isCommittedProperty());
        // if the value of isCommitted() changes, the background color of the cell changes
        status.setCellFactory(tc -> {
            TableCell<Segment, Boolean> cell = new TableCell<Segment, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (item == true) {
                            setText(null);
                            // here is where the committed color is set 
                            setStyle("-fx-background-color: " + committedStatusColor + ";");
                        } else {
                            setStyle("");
                            setText(null);
                            setGraphic(null);
                        }
                    }
                }
            };
            return cell;
        });

        // MatchScore:
        matchScore.setCellValueFactory(new PropertyValueFactory<>("matchScore"));

        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // MATCH TABLE
        // MATCHES: THAI COLUMN
        thaiColComp.setCellValueFactory(new PropertyValueFactory<>("thai"));
        thaiColComp.setMinWidth(120);
        thaiColComp.setCellFactory(tc -> {
            TableCell<MatchSegment, String> cell = new TableCell<MatchSegment, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setGraphic(null);
                        setText(null);
                        setStyle("");
                    } else {
                        setText(null);
                        setStyle("");
                        MatchSegment thisCellSegment = tc.getTableView().getItems().get(this.getIndex());
                        boolean[] matches = thisCellSegment.getMatches();
                        // matching characters are marked as one color, non-matching characters are another column
                        final TextFlow textFlow = matchesAsTextFlow(thisCellSegment.getThai(), matches);
                        this.setGraphic(textFlow);

                        setPrefHeight(textFlow.prefHeight(thaiColComp.getWidth()) + 4);
                        thaiColComp.widthProperty().addListener((v, o, n)
                                -> setPrefHeight(textFlow.prefHeight(n.doubleValue()) + 4));
                    }
                }
            };
            Text text = new Text();
            text.setFont(Font.font("Arial"));
            TextFlow textFlow = new TextFlow(text);
            cell.setGraphic(textFlow);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(thaiColComp.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        // MATCHES: ENGLISH COLUMN
        englishColComp.setCellValueFactory(new PropertyValueFactory<>("english"));
        BasicCellFactory cf1 = new BasicCellFactory();
        cf1.setFont(UIState.getEnglishFont());
        englishColComp.setCellFactory(cf1);
        //fileColComp.setCellFactory(myCallBack);
        fileColComp.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        // Sets "score" rating for match. Currently represents the number of matching characters.
        scoreColComp.setCellValueFactory(new PropertyValueFactory<>("longestMatchLength"));


        /*
            Makes it so when a row is selected in the state table, this renders compareTable with a new MatchList made from the Thai String from the state table.
         */
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                backEnd.newSelection(newSelection);
            }
        }
        );

        /* FONTS
        Font.getFamilies().forEach((s) -> {
            System.out.println(s);
        }); */
        // Makes it so merge is disabled except when multiple cells are selected
        tableView.getSelectionModel().getSelectedItems().addListener((Change<? extends Segment> c) -> {
            if (c.getList().size() <= 1) {
                mergeButton.setDisable(true);
            } else {
                mergeButton.setDisable(false);
            }
        });

        /*
        BUTTON IMAGES
         */
        // Apply image to COMMIT button
        ImageView commitIV = new ImageView(getClass().getResource("/JavaFX_1/CommitButton.png").toExternalForm());
        commitIV.setFitWidth(40);
        commitIV.setPreserveRatio(true);
        commitIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        commitIV.setCache(true); // perhaps not necessary
        commitButton.setGraphic(commitIV);

        // apply image to UNCOMMIT button
        ImageView uncommitIV = new ImageView(getClass().getResource("/JavaFX_1/UncommitButton.png").toExternalForm());
        uncommitIV.setFitWidth(40);
        uncommitIV.setPreserveRatio(true);
        uncommitIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        uncommitIV.setCache(true); // perhaps not necessary
        uncommitButton.setGraphic(uncommitIV);

        // apply image to SPLIT button
        ImageView splitIV = new ImageView(getClass().getResource("/JavaFX_1/SplitButton.png").toExternalForm());
        splitIV.setFitWidth(40);
        splitIV.setPreserveRatio(true);
        splitIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        splitIV.setCache(true); // perhaps not necessary
        splitButton.setGraphic(splitIV);

        // apply image to MERGE button
        ImageView mergeIV = new ImageView(getClass().getResource("/JavaFX_1/MergeButton.png").toExternalForm());
        mergeIV.setFitWidth(40);
        mergeIV.setPreserveRatio(true);
        mergeIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        mergeIV.setCache(true); // perhaps not necessary
        mergeButton.setGraphic(mergeIV);

        // apply image to EXPORT button
        ImageView exportIV = new ImageView(getClass().getResource("/JavaFX_1/ExportButton.png").toExternalForm());
        exportIV.setFitWidth(40);
        exportIV.setPreserveRatio(true);
        exportIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        exportIV.setCache(true); // perhaps not necessary
        exportButton.setGraphic(exportIV);

        // apply image to UNDO button
        ImageView undoIV = new ImageView(getClass().getResource("/JavaFX_1/UndoButton.png").toExternalForm());
        undoIV.setFitWidth(30);
        undoIV.setPreserveRatio(true);
        undoIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        undoIV.setCache(true); // perhaps not necessary
        undoButton.setGraphic(undoIV);

        // apply image to REDO button
        ImageView redoIV = new ImageView(getClass().getResource("/JavaFX_1/RedoButton.png").toExternalForm());
        redoIV.setFitWidth(30);
        redoIV.setPreserveRatio(true);
        redoIV.setSmooth(true); // perhaps not necessary (makes it smoother when resized)
        redoIV.setCache(true); // perhaps not necessary
        redoButton.setGraphic(redoIV);

    }

    @FXML
    protected void minMatchLengthChanged(ActionEvent event) {
        backEnd.setMinLength(Integer.valueOf(minMatchLengthField.getText()));
    }

    @FXML
    protected void search(ActionEvent event) {
        // not yet implemented
        // is for when the user searches for an exact match in the search bar (much like typing "" around a search in google)
    }

    @FXML
    private void merge(ActionEvent event) {
        ObservableList<Segment> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems != null) {
            backEnd.acceptAction(new Merge(selectedItems));
        }
    }

    @FXML
    private void commit(ActionEvent event) {
        ObservableList<Segment> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems != null) {

            backEnd.acceptAction(new Commit(selectedItems));
        }
    }

    @FXML
    private void uncommit(ActionEvent event) {
        ObservableList<Segment> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems != null) {
            backEnd.acceptAction(new Uncommit(selectedItems));
        }
    }

    @FXML
    private void split(ActionEvent event) {
        ObservableList<Segment> selectedItems = tableView.getSelectionModel().getSelectedItems();

        if (selectedItems != null && !selectedItems.isEmpty()) {

            int charIndexToSplitOn = 0;
            // get caret position in currently editing Thai cell
            TablePosition<Segment, ?> cellBeingEdited = tableView.getEditingCell();
            if (cellBeingEdited != null) {
                TableColumn<Segment, ?> columnBeingEdited = tableView.getColumns().get(cellBeingEdited.getColumn());

            }

            // only splits if one row is selected
            if (selectedItems.size() == 1) {
                // sends that row to back end
                Segment seg = selectedItems.get(0);
                backEnd.acceptAction(new Split(selectedItems.get(0), 10));
            }
        }
    }

    @FXML
    private void export(ActionEvent event) {
        backEnd.exportCommittedSegs();
    }

    /**
     * Takes a boolean array of same length as text where true indicates a
     * matching character and false indicates a non-matching character.
     *
     * @param matchingSegText The Thai text with matches in it.
     * @param matches A boolean array of same length as text.
     * @return A TextFlow object with matching substrings colored differently.
     */
    public TextFlow matchesAsTextFlow(String matchingSegText, boolean[] matches) {
        // breaks text up into list of matching and non-matching substrings
        ArrayList<String> substrings = new ArrayList();
        TextFlow textFlow = new TextFlow();
        if (matchingSegText.length() == 0) {
            return textFlow;
        }

        boolean currentBoolean = matches[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matches.length; i++) {
            if (matches[i] != currentBoolean) {
                // adds substring to list
                substrings.add(sb.toString());
                // restarts stringbuilder and resets current boolean
                sb = new StringBuilder();
                sb.append(matchingSegText.charAt(i));
                currentBoolean = matches[i];
            } else {
                sb.append(matchingSegText.charAt(i));
            }
        }
        substrings.add(sb.toString());

        // Builds TextFlow object
        // Assigns specific colors for matching and non-matching substrings.
        Color matchColor = Color.GREEN;
        Color nonMatchColor = Color.BLACK;
        Color currentColor;

        // sets currentColor according to whether the text begins with a  match or not
        currentColor = matches[0] == false ? nonMatchColor : matchColor;

        Iterator<String> iter = substrings.iterator();

        // creates Text objects, assigns color, then adds to TextFlow
        while (iter.hasNext()) {
            Text text = new Text(iter.next());
            text.setFill(currentColor);
            text.setFont(UIState.getThaiFont());
            textFlow.getChildren().add(text);
            // switches current color
            currentColor = currentColor == matchColor ? nonMatchColor : matchColor;
        }
        return textFlow;
    }

    @FXML
    private void undo(ActionEvent event) {
        backEnd.undo();
    }

    @FXML
    private void redo(ActionEvent event) {
        backEnd.redo();
    }

    /**
     * Key method for initializing the state for a given main file. Creates a
     * new StateBuilder which in turn creates the various key components of the
     * program (TopLevelBackEnd, State, UIState) that are intrinsically linked to the
     * main file being translated. If a new file is being made the main file
     * (such as when opening an old translation project or starting a new one),
     * all of these should be reset.
     *
     * This is also where all segments are retrieved from the database (for
     * searching matches with the main file). Currently this just re-retrieves
     * all of these and recreates all the Postings Lists, which may be
     * inefficient, but the user isn't expected to change files that often. If
     * this needs to be changed (like the user is switching between files a lot)
     * it would not be too hard to reconfigure this so that it doesn't redo
     * these expensive operations (because all that's really changing is the
     * MainFile). But currently, for simplicity sake and to avoid unforeseen
     * errors, the whole program is essentially rebooted by doing this.
     *
     * @param bf The new main file to be translated
     */
    private void setMainFile(BasicFile newMainFile) {
        StateBuilder stateBuilder = new StateBuilder(newMainFile, DatabaseOperations.getAllFiles());
        uiState = stateBuilder.getUIState();
        backEnd = stateBuilder.getTopLevelBackEnd();

        // UI THINGS THAT DEPEND ON MAINFILE
        // sets title at top to the name of the file
        title.setText(uiState.getMainFileName());

        // puts the list of Segments from the main file into the main table view
        tableView.setItems(uiState.getMainFileSegs());

        compareTable.setItems(uiState.getMatchList());

        uiState.getNumMatchesProperty().addListener((ChangeListener) (arg, oldVal, newVal) -> {
            numMatches.setText(String.valueOf(newVal));
        });

    }

}

/*
Callback: 
    Callback<P, R> 
        P = the type of the function's argument
        R = the type of the thing returned

setCellFactory:
    takes callback that takes a TableColumn<S,T> and returns a TableCell<S,T>
    bind node text to cell value by doing this:  (the node in the cell).textProperty().bind(cell.itemProperty());
    but why do you call the cell and not param????

setCellValueFactory:
    takes callback that takes a CellDataFeatures<S,T> and returns an ObservableValue<T>

updateItem:
    is a protected method in Cell class
    is called when the item is changed.
 */
