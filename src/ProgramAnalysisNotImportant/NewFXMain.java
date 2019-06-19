/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProgramAnalysisNotImportant;

import DataStructures.TranslationFile;
import Database.DatabaseOperations;
import comparator.PostingsList;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Chris
 */
public class NewFXMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        
        ArrayList<TranslationFile> allFiles = DatabaseOperations.getAllFiles();
        int numSegs = TranslationFile.getAllCommittedSegsInFileList(allFiles).size();
        int totalSourceLength = TranslationFile.getAllCommittedSegsInFileList(allFiles).stream()
                .mapToInt(a -> a.getThai().length())
                .sum();
        System.out.println("numSegs = " + numSegs);
        System.out.println("totalSourceLength = " + totalSourceLength);
        
        PostingsList pl1 = new PostingsList(1);
        pl1.addMultipleSegments(TranslationFile.getAllCommittedSegsInFileList(allFiles));
        System.out.println("pl1 total ngrams = " + pl1.getMap().keySet().size());
        System.out.println("pl1 total segment pointers = " + pl1.getMap().values().stream().mapToInt(a -> a.size()).sum());   
         
        
        
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println(allFiles + " " + pl1);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
