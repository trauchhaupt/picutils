package de.vonrauchhaupt.picutils;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class DuplicateImagesView extends Application {


    private TextField textFieldFolder = new TextField();
    private Button buttonSearch = new Button("Find Duplicates");
    private HBox topBox = new HBox();
    private HBox centerBox = new HBox();
    private BorderPane borderPane = new BorderPane();
    private HBox bottomBox = new HBox();
    private Button buttonPrevious = new Button("Previous");
    private Button buttonNext = new Button("Next");

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox.setHgrow(textFieldFolder, Priority.ALWAYS);
        HBox.setHgrow(buttonSearch, Priority.SOMETIMES);
        topBox.getChildren().addAll(textFieldFolder, buttonSearch);
        borderPane.setTop(topBox);
        borderPane.setCenter(centerBox);

        bottomBox.getChildren().addAll(buttonPrevious, buttonNext);
        buttonPrevious.setDisable(true);
        buttonNext.setDisable(true);

        buttonSearch.setOnAction(this::buttonSearchClicked);

        primaryStage.setTitle("Duplicate Images");
        primaryStage.setScene(new Scene(borderPane));
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(x -> System.exit(0));
    }

    private void buttonSearchClicked(ActionEvent actionEvent) {

    }


}
