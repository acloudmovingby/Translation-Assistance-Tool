package JavaFX_1;

import Files.CompareFile;
import Files.TUCompareEntry;
import Files.TUEntry;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class Fxml_1Controller implements Initializable {

    @FXML
    Label title;

    @FXML
    TableView<TUEntry> tableView;

    @FXML
    TableColumn<TUEntry, String> thaiCol;

    @FXML
    TableColumn<TUEntry, String> englishCol;

    @FXML
    TableColumn<TUEntry, Boolean> status;

    @FXML
    TableColumn<TUEntry, String> matchScore;

    @FXML
    TableColumn<TUCompareEntry, String> thaiColComp;

    @FXML
    TableColumn<TUCompareEntry, String> englishColComp;

    @FXML
    TableColumn<TUCompareEntry, String> fileColComp;

    @FXML
    TableColumn<TUCompareEntry, String> scoreColComp;

    @FXML
    TableView<TUCompareEntry> compareTable;

    @FXML
    TextField minMatchLengthField;

    @FXML
    Label numMatches;

    Font defaultThaiFont;
    Font defaultEnglishFont;
    String committedStatusColor;
    String unCommittedStatusColor;
    /**
     * Main logic of the program. Controller retrieves and sends information to
     * main.
     */
    MainLogic main;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        main = new MainLogic();
        defaultThaiFont = Font.font("Arial");
        defaultEnglishFont = Font.font("Arial");
        committedStatusColor = "rgb(183, 215, 255)";
        unCommittedStatusColor = "rgb(255, 255, 255)";

        // Default minimum length for matches
        // Sets prompt text for minMatchLengthField to equal default minimum match length
        minMatchLengthField.setPromptText(Integer.toString(main.getMinMatchLength()));

        // MAIN FILE VIEWER COLUMNS
        // Thai column:
        thaiCol.setCellValueFactory(new PropertyValueFactory<>("thai"));
        thaiCol.setMinWidth(80);
        thaiCol.setCellFactory(tc -> {
            TableCell<TUEntry, String> cell = new TableCell<>();
            Text text = new Text();
            text.setFont(defaultThaiFont);
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(thaiCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        // English column:
        englishCol.setCellFactory(new Callback<TableColumn<TUEntry, String>, TableCell<TUEntry, String>>() {
            @Override
            public TableCell<TUEntry, String> call(TableColumn<TUEntry, String> tc) {
                TextFieldTableCell<TUEntry, String> cell = new TextFieldTableCell(new StringConverter() {
                 
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }

                    @Override
                    public Object fromString(String string) {
                        return string;
                    }
                });
                return cell;
            }
        });

        englishCol.setOnEditCommit(new EventHandler<CellEditEvent<TUEntry, String>>() {
            @Override
            public void handle(CellEditEvent<TUEntry, String> t) {
                main.englishEdited(((TUEntry) t.getTableView().getItems().get(t.getTablePosition().getRow())),
                        t.getNewValue());
            }
        });
        englishCol.setCellValueFactory(new PropertyValueFactory<>("english"));

        // Status column:
        // makes it so this columns cellValueFactory is bound to the isCommitted() method of TUEntry
        status.setCellValueFactory(new PropertyValueFactory<>("isCommitted"));
        //if the value of isCommitted() changes, the background color of the cell changes
        status.setCellFactory(tc -> {
            TableCell<TUEntry, Boolean> cell = new TableCell<TUEntry, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    // assign item's toString value as text
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        TUEntry thisCellTU = tc.getTableView().getItems().get(this.getIndex());
                        boolean isCommitted = thisCellTU.isCommitted();
                        if (isCommitted == true) {
                            this.setStyle("-fx-background-color: " + committedStatusColor + ";");
                            setText(thisCellTU.getThai());
                        } else {
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
        title.setText(main.getMainFile().getFileName());

        // binds main file to main table viewer
        tableView.setItems(main.getMainFile().getObservableList());
        tableView.setEditable(true);
        // tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // COMPARE FILE VIEWER
        // Thai columns
        thaiColComp.setCellValueFactory(new PropertyValueFactory<>("thai"));
        thaiColComp.setMinWidth(120);
        thaiColComp.setCellFactory(tc -> {
            TableCell<TUCompareEntry, String> cell = new TableCell<TUCompareEntry, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {

                        setText(null);
                        setStyle("");

                    } else {

                        TUCompareEntry thisCellTU = tc.getTableView().getItems().get(this.getIndex());
                        boolean[] matches = thisCellTU.getMatches();
                        // all true will be one color, all false will be another color
                        final TextFlow textFlow = matchesAsTextFlow(thisCellTU.getThai(), matches);
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
        englishColComp.setCellFactory(tc -> {
            TableCell<TUCompareEntry, String> cell = new TableCell<>();
            Text text = new Text();
            text.setFont(defaultEnglishFont);
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(englishColComp.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        //compareTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //File column
        Callback<TableColumn<TUCompareEntry, String>, TableCell<TUCompareEntry, String>> myCallBack;
        myCallBack = new Callback<TableColumn<TUCompareEntry, String>, TableCell<TUCompareEntry, String>>() {
            @Override
            public TableCell<TUCompareEntry, String> call(TableColumn<TUCompareEntry, String> param) {
                TableCell<TUCompareEntry, String> cell = new TableCell<TUCompareEntry, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {

                            setText(null);
                            setStyle("");

                        } else {
                            TextFlow textFlow = new TextFlow();
                            String str1 = "";
                            for (int i = 0; i < item.length(); i++) {
                                if (item.charAt(i) == 't') {
                                    Text text1 = new Text(str1);
                                    str1 = "";
                                    text1.setFill(Color.BLUE);
                                    textFlow.getChildren().add(text1);
                                    Text text2 = new Text("t");
                                    text2.setFill(Color.RED);
                                    textFlow.getChildren().add(text2);
                                } else if (i + 1 == item.length()) {
                                    str1 = str1.concat(String.valueOf(item.charAt(i)));
                                    Text text1 = new Text(str1);
                                    str1 = "";
                                    text1.setFill(Color.BLUE);
                                    textFlow.getChildren().add(text1);
                                } else {
                                    str1 = str1.concat(String.valueOf(item.charAt(i)));
                                }
                            }
                            setGraphic(textFlow);
                        }
                    }
                };

                /*ObjectProperty<String> itemProperty = cell.itemProperty();
                System.out.println(itemProperty);
                String cellString = itemProperty.get();
                System.out.println(cellString);
                String str1 = cellString.substring(0, cellString.length()/2);
                String str2 = cellString.substring(cellString.length()/2, cellString.length());
                 */
                Text text1 = new Text();
                text1.setFill(Color.BLUE);
                text1.textProperty().bind(cell.itemProperty());
                Text text2 = new Text();
                text2.setFill(Color.RED);
                text2.textProperty().bind(cell.itemProperty());

                TextFlow textFlow = new TextFlow(text1, text2);
                cell.setGraphic(textFlow);
                return cell;
            }

        };
        //fileColComp.setCellFactory(myCallBack);
        fileColComp.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        // Sets "score" rating for match. Currently represents the number of matching characters.
        scoreColComp.setCellValueFactory(new PropertyValueFactory<>("longestMatchLength"));

        // sets initial compare file in compare table to first TU in main file viewer
        setCompareTable(0);

        /*
            Makes it so when a row is selected in the main table, this renders compareTable with a new CompareFile made from the Thai String from the main table.
         */
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setCompareTable(tableView.getSelectionModel().getSelectedIndex());
            }
        }
        );

        Font.getFamilies().forEach((s) -> {
            System.out.println(s);
        });

    }

    @FXML
    protected void minMatchLengthChanged(ActionEvent event) {
        // changes the minimum match length (retrieved from the minMatchLength field)
        main.setMinMatchLength(Integer.valueOf(minMatchLengthField.getText()));
        // redraws the compare table
        setCompareTable(main.getCurrentIndex());
    }

    @FXML
    private void commit(ActionEvent event) {
        main.commit(tableView.getSelectionModel().getSelectedItem());
    }

    private void setCompareTable(int index) {
        CompareFile cf = main.getCompareFile(index);
        setNumMatches(cf.getTUs().size());
        compareTable.setItems(cf.getObservableList());
    }

    private void setNumMatches(int num) {
        numMatches.setText(String.valueOf(num));
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
            text.setFont(defaultThaiFont);
            textFlow.getChildren().add(text);
            // switches current color
            currentColor = currentColor == matchColor ? nonMatchColor : matchColor;
        }
        return textFlow;
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
