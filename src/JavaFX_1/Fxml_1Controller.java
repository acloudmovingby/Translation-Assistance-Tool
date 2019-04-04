package JavaFX_1;

import DataStructures.BasicFile;
import DataStructures.Corpus;
import DataStructures.FileBuilder;
import State.State;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import Database.DatabaseOperations;
import State.Dispatcher;
import State.UIState;
import UserActions.Commit;
import UserActions.EditEnglish;
import UserActions.Merge;
import UserActions.Split;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class Fxml_1Controller implements Initializable {

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

    String committedStatusColor;
    String unCommittedStatusColor;
    
    Dispatcher dispatcher;
    UIState uiState;
    /**
     * Main logic of the program. Controller retrieves and sends information to
     * state.
     */
    State state;
    Scene scene;

    final BooleanProperty commandPressed;
    final BooleanProperty zPressed;
    final BooleanBinding commandZPressed;

    public Fxml_1Controller() {
        this.zPressed = new SimpleBooleanProperty(false);
        this.commandPressed = new SimpleBooleanProperty(false);
        this.commandZPressed = commandPressed.and(zPressed);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        JavaFX_1.myControllerHandle = this;
        
        // retrieves all files previously stored in database
        Corpus corpus = DatabaseOperations.getAllSegments();
        
        // builds a main file from some random Thai document
        FileBuilder fileBuilder = new FileBuilder();
        //String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/ABCTestSimple.txt";
        //String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/ABCTest.txt";
        String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/FanSafety.txt";
        BasicFile mainFile = fileBuilder.justThaiFilePath(filePath);
        
        Initializer init = new Initializer(mainFile, corpus);
        state = init.getState();
        uiState = init.getUIState();
        dispatcher = init.getDispatcher();

        committedStatusColor = "rgb(183, 215, 255)";
        unCommittedStatusColor = "rgb(255, 255, 255)";

        // Sets prompt text for minMatchLengthField to equal default minimum match length
        minMatchLengthField.setPromptText(Integer.toString(state.getMinMatchLength()));
        // Set prompt text for search field
        searchField.setPromptText("search...");

        // MAIN FILE VIEWER COLUMNS
        // id column:
        idCol.setCellValueFactory(new Callback<CellDataFeatures<Segment, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<Segment, Integer> p) {
                return new ReadOnlyObjectWrapper(tableView.getItems().indexOf(p.getValue()));
            }
        });
        idCol.setSortable(false);

        // Thai column:
        thaiCol.setCellValueFactory(new PropertyValueFactory<>("thai"));
        thaiCol.setMinWidth(80);
        thaiCol.setEditable(true);
        thaiCol.setCellFactory(tc -> {
            TableCell<Segment, String> cell = new TableCell<>();
            Text text = new Text();
            text.setFont(UIState.getThaiFont());
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(thaiCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            cell.setEditable(true);
            return cell;
        });

        // English column:
        EditableEnglishCellFactory cf2 = new EditableEnglishCellFactory();
        cf2.setFont(UIState.getEnglishFont());
        englishCol.setCellFactory(cf2);
        englishCol.setOnEditCommit(e -> {
            int row = e.getTablePosition().getRow();
            Segment editedTU = tableView.getItems().get(row);
            
            if (editedTU != null) {
                dispatcher.acceptAction(new EditEnglish(editedTU, e.getNewValue()));
            }
            
            /*
            editedTU.setEnglish(e.getNewValue());
            editedTU.setCommitted(true);
            tableView.getItems().set(row, editedTU); 
                */
        }
        );
        englishCol.setCellValueFactory(new PropertyValueFactory<>("english"));

        // Status column:
        // makes it so this columns cellValueFactory is bound to the isCommitted() method of TUEntry
        status.setCellValueFactory(cellData -> cellData.getValue().isCommittedProperty());
        //if the value of isCommitted() changes, the background color of the cell changes
        status.setCellFactory(tc -> {
            TableCell<Segment, Boolean> cell = new TableCell<Segment, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    // assign item's toString value as text
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (item == true) {
                            setText(null);
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

        // Tags column
        // sets title at top to the name of the file
        title.setText(state.getMainFile().getFileName());

        // binds state file to state table viewer
        //tableView.setItems(state.getMainFileSegs());
        tableView.setItems(uiState.getMainFileSegs());
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // COMPARE FILE VIEWER
        // Thai columns
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
                        // all true will be one color, all false will be another color
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
        // English column
        englishColComp.setCellValueFactory(new PropertyValueFactory<>("english"));
        BasicCellFactory cf1 = new BasicCellFactory();
        cf1.setFont(UIState.getEnglishFont());
        englishColComp.setCellFactory(cf1);
        //fileColComp.setCellFactory(myCallBack);
        fileColComp.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        // Sets "score" rating for match. Currently represents the number of matching characters.
        scoreColComp.setCellValueFactory(new PropertyValueFactory<>("longestMatchLength"));

        // sets initial compare file in compare table to first TU in state file viewer
        //setCompareTable(state.getMainFile().getActiveSegs().get(0).toString());

        /*
            Makes it so when a row is selected in the state table, this renders compareTable with a new MatchList made from the Thai String from the state table.
         */
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                state.newSelection(newSelection);
                if (scene == null && tableView.getScene() != null) {
                    setScene(tableView.getScene());
                }
            }
        }
        );

        /* FONTS
        Font.getFamilies().forEach((s) -> {
            System.out.println(s);
        }); */
        compareTable.setItems(uiState.getMatchList());

        uiState.getNumMatchesProperty().addListener((ChangeListener) (arg, oldVal, newVal) -> {
            numMatches.setText(String.valueOf(newVal));
        });

    }

    @FXML
    protected void minMatchLengthChanged(ActionEvent event) {
        state.setMinLength(Integer.valueOf(minMatchLengthField.getText()));
    }

    @FXML
    protected void search(ActionEvent event) {
        state.search(searchField.getText());
    }

    @FXML
    private void merge(ActionEvent event) {
        ObservableList<Segment> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems != null) {
            dispatcher.acceptAction(new Merge(selectedItems));
        }
    }

    @FXML
    private void commit(ActionEvent event) {
        ObservableList<Segment> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (selectedItems != null) {
            
            dispatcher.acceptAction(new Commit(selectedItems));
        }
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.Z) {
            if (!zPressed.getValue()) {
                
                zPressed.set(true);
                if (zPressed.getValue() && commandPressed.getValue()) {
                   // state.undo();
                }
                event.consume();
            }
        } else if (event.getCode() == KeyCode.COMMAND) {
            event.consume();
            commandPressed.set(true);
        }
    }

    @FXML
    private void keyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.COMMAND) {
            commandPressed.set(false);
            event.consume();
        } else if (event.getCode() == KeyCode.Z) {
            event.consume();
        }
    }

    @FXML
    private void split(ActionEvent event) {
        ObservableList<Segment> selectedItems = tableView.getSelectionModel().getSelectedItems();

        if (selectedItems != null && !selectedItems.isEmpty()) {
            // only splits if one row is selected
            if (selectedItems.size() == 1) {
                // sends that row to dispatcher
                Segment seg = selectedItems.get(0);
                dispatcher.acceptAction(new Split(selectedItems.get(0), 10));
            }
        }
    }

    @FXML
    private void export(ActionEvent event) {
        state.exportCommittedSegs();
    }

    /**
     * Takes a boolean array of same length as text where true indicates a
     * matching character and false indicates a non-matching character.
     *
     * @param matchingTUText The Thai text with matches in it.
     * @param matches A boolean array of same length as text.
     * @return A TextFlow object with matching substrings colored differently.
     */
    public TextFlow matchesAsTextFlow(String matchingTUText, boolean[] matches) {
        // breaks text up into list of matching and non-matching substrings
        ArrayList<String> substrings = new ArrayList();
        TextFlow textFlow = new TextFlow();
        if (matchingTUText.length() == 0) {
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
                sb.append(matchingTUText.charAt(i));
                currentBoolean = matches[i];
            } else {
                sb.append(matchingTUText.charAt(i));
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
        dispatcher.undo();
        
    }

    private void setScene(Scene scene) {
        this.scene = scene;
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
