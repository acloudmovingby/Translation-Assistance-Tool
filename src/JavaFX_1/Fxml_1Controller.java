/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import Files.BasicFile;
import Files.CompareFile;
import Files.FileFactory;
import Files.TUCompareEntry;
import Files.TUEntry;
import comparator.Comparator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class Fxml_1Controller implements Initializable {

    @FXML
    Label title;
    
    @FXML
    TableView<TUEntry_UI> tableView;

    @FXML
    TableColumn<TUEntry_UI, String> thaiCol;

    @FXML
    TableColumn<TUEntry_UI, String> englishCol;

    @FXML
    TableColumn<TUCompare_UI, String> thaiColComp;

    @FXML
    TableColumn<TUCompare_UI, String> englishColComp;

    @FXML
    TableView<TUCompare_UI> compareTable;

    BasicFile file1;
    /*
    Later this will be the corpus. Currently represents the file from which matches are found.
     */
    BasicFile compareFile;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Sets columns
        thaiCol = new TableColumn<>("Thai");
        PropertyValueFactory pvf = new PropertyValueFactory<>("thai");
        thaiCol.setCellValueFactory(new PropertyValueFactory<>("thai"));
        thaiCol.setMinWidth(120);
        thaiCol.setCellFactory(tc -> {
            TableCell<TUEntry_UI, String> cell = new TableCell<>();
            Text text = new Text();
            text.setFont(Font.font("Arial"));
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(thaiCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        /*@Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setWrapText(true);
                    System.out.println(getFont());
                    TableCell.
                }
            }*/
        // });
        englishCol = new TableColumn<>("English");
        englishCol.setCellValueFactory(new PropertyValueFactory<>("english"));
        englishCol.setMinWidth(180);

        // makes main file
        FileFactory ff = new FileFactory();
        String filePath = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/FanSafety.txt";
        file1 = ff.justThaiFilePath(filePath);
        file1.setFileName("Main File");
        compareFile = file1;
        
        title.setText(file1.getFileName());

        // binds main file to main table view
        ObservableList<TUEntry_UI> tuList = FXCollections.observableArrayList();
        file1.getTUs().forEach((t) -> {
            tuList.add(t.getUI());
        });
        tableView.setItems(tuList);
        tableView.getColumns().addAll(thaiCol, englishCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Sets compare table columns
        thaiColComp = new TableColumn<>("Thai");
        thaiColComp.setCellValueFactory(new PropertyValueFactory<>("thai"));
        thaiColComp.setMinWidth(120);
        thaiColComp.setCellFactory(tc -> {
            TableCell<TUCompare_UI, String> cell = new TableCell<>();
            Text text = new Text();
            text.setFont(Font.font("Arial"));
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(thaiColComp.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        englishColComp = new TableColumn<>("English");
        englishColComp.setCellValueFactory(new PropertyValueFactory<>("english"));
        englishColComp.setMinWidth(180);
        compareTable.getColumns().addAll(thaiColComp, englishColComp);
        compareTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // binds initial compare file 
        Comparator c = new Comparator(file1.getTUs().get(0).toString(), compareFile, 10);
        CompareFile cf = c.getCompareFile();
        compareTable.setItems(getCompareTableItems(cf));

        /*
            Makes it so when a row is selected in the main table, this renders compareTable with a new CompareFile made from the Thai String from the main table.
         */
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        Comparator newComparator = new Comparator(newSelection.getThai(), compareFile, 10);
                        CompareFile cfNew = newComparator.getCompareFile();
                        compareTable.setItems(getCompareTableItems(cfNew));
                        System.out.println("Selection");
                    }
                }
        );

        for (String s : Font.getFamilies()) {
            System.out.println(s);
        }
    }

    @FXML
    protected void addRow(ActionEvent event) {
        System.out.println("button clicked");
        TUEntry_UI tuNew = new TUEntry_UI("???", "???");
        tableView.getItems().add(tuNew);
    }

    public ObservableList<TUCompare_UI> getCompareTableItems(CompareFile c) {
        ObservableList<TUCompare_UI> tuList = FXCollections.observableArrayList();
        c.getTUs().forEach((t) -> {
            tuList.add((TUCompare_UI) t.getUI());
        });
        return tuList;
    }
}
