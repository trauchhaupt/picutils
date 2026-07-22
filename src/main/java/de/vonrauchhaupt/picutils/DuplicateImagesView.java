package de.vonrauchhaupt.picutils;

import de.vonrauchhaupt.picutils.model.ImageInformationDto;
import de.vonrauchhaupt.picutils.model.ScannerContextDto;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DuplicateImagesView extends Application {

    private final TextField textFieldFolder = new TextField();
    private final Button buttonSearch = new Button("Find Duplicates");
    private final Button buttonIndex = new Button("Reindex");
    private final HBox topBox = new HBox();
    private final HBox centerBox = new HBox();
    private final BorderPane borderPane = new BorderPane();
    private final HBox bottomBox = new HBox();
    private final Button buttonPrevious = new Button("Previous");
    private final Button buttonNext = new Button("Next");
    private final Label labelCurrentImageIndex = new Label();

    private final ObservableList<SimilarImages> groupedSimilarImagesByHash = FXCollections.observableArrayList();
    private final ObjectProperty<Integer> currentDisplaydIndex = new SimpleObjectProperty<>();

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox.setHgrow(textFieldFolder, Priority.ALWAYS);
        HBox.setHgrow(buttonSearch, Priority.SOMETIMES);
        topBox.getChildren().addAll(textFieldFolder, buttonSearch, buttonIndex);
        topBox.setPadding(new javafx.geometry.Insets(10));
        borderPane.setTop(topBox);

        borderPane.setCenter(centerBox);
        centerBox.setSpacing(10);
        centerBox.setPadding(new javafx.geometry.Insets(10));

        borderPane.setBottom(bottomBox);
        bottomBox.getChildren().addAll(buttonPrevious, buttonNext, labelCurrentImageIndex);
        bottomBox.setSpacing(10);
        bottomBox.setPadding(new javafx.geometry.Insets(10));
        buttonPrevious.setOnAction(x -> {
            currentDisplaydIndex.set(currentDisplaydIndex.get() - 1);
            showCurrentSimilarImages();
            checkButtonStates();
        });
        buttonNext.setOnAction(x -> {
            currentDisplaydIndex.set(currentDisplaydIndex.get() + 1);
            showCurrentSimilarImages();
            checkButtonStates();
        });

        checkButtonStates();
        buttonSearch.setOnAction(this::buttonSearchClicked);
        buttonIndex.setOnAction(this::buttonIndexClicked);
        textFieldFolder.textProperty().addListener((oldValue, newValue, prop) -> checkFileState());

        List<String> rawArgs = getParameters().getRaw();
        if (!rawArgs.isEmpty()) {
            textFieldFolder.setText(rawArgs.get(0));
        }

        primaryStage.setTitle("Duplicate Images");
        primaryStage.setScene(new Scene(borderPane));
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(x -> System.exit(0));
    }

    private void buttonIndexClicked(ActionEvent actionEvent) {
        groupedSimilarImagesByHash.clear();
        currentDisplaydIndex.set(null);
        centerBox.getChildren().clear();
        checkButtonStates();
        Path pathToFileInfosXml = getPathToFileInfosXml();
        if (pathToFileInfosXml == null) {
            return;
        }
        ScannerContextDto scannerContextDto = new ScannerContextDto(pathToFileInfosXml.getParent(), true);
        try {
            scannerContextDto.scanRootDirectory();
        } catch (Exception e) {
            showError("Could not scan directory: " + e.getMessage());
        }
        showError("Done Indexing");
    }

    private void checkFileState() {
        if (textFieldFolder.getText().isBlank()) {
            buttonSearch.setDisable(true);
            return;
        }
        Path pathToFileInfosXml = Paths.get(textFieldFolder.getText());
        if (!pathToFileInfosXml.toFile().isDirectory()) {
        }
    }

    private void showError(String error) {
        centerBox.getChildren().clear();
        centerBox.getChildren().add(new Label(error));
    }

    private Path getPathToFileInfosXml() {
        if (textFieldFolder.getText().isBlank()) {
            return null;
        }
        Path pathToFileInfosXml = Paths.get(textFieldFolder.getText());
        if (!Files.exists(pathToFileInfosXml) ||
                !Files.isRegularFile(pathToFileInfosXml) ||
                !pathToFileInfosXml.endsWith("fileInfos.xml")) {
            showError("Please select a valid 'fileInfos.xml' file to start.");
            return null;
        }
        return pathToFileInfosXml;
    }

    private void checkButtonStates() {
        Integer currentDisplayIndex = currentDisplaydIndex.get();
        if (currentDisplayIndex == null ||
                groupedSimilarImagesByHash.isEmpty() ||
                groupedSimilarImagesByHash.size() == 1) {
            buttonNext.setDisable(true);
            buttonPrevious.setDisable(true);
        } else if (currentDisplayIndex <= 0) {
            buttonPrevious.setDisable(true);
            buttonNext.setDisable(false);
        } else if (currentDisplayIndex >= groupedSimilarImagesByHash.size() - 1) {
            buttonPrevious.setDisable(false);
            buttonNext.setDisable(true);
        }
    }

    private void buttonSearchClicked(ActionEvent actionEvent) {
        Path pathToFileInfosXml = getPathToFileInfosXml();
        if (pathToFileInfosXml == null) {
            return;
        }
        ScannerContextDto scannerContextDto = new ScannerContextDto(pathToFileInfosXml.getParent(), false);
        scannerContextDto.loadFromFile();

        Set<ImageInformationDto> imageInformationSet = scannerContextDto.getImageInformationSet();
        imageInformationSet.removeIf(x -> !x.exists());

        Map<Integer, List<ImageInformationDto>> groupedByHash = imageInformationSet.stream()
                .collect(Collectors.groupingBy(ImageInformationDto::hashCode));
        groupedSimilarImagesByHash.clear();
        groupedSimilarImagesByHash.setAll(groupedByHash.entrySet().stream()
                .filter(x -> x.getValue().size() >= 2)
                .map(x -> new SimilarImages(x.getKey(), x.getValue()))
                .toList());

        if (groupedSimilarImagesByHash.isEmpty()) {
            showError("No duplicate images found.");
            currentDisplaydIndex.set(null);
            groupedSimilarImagesByHash.clear();
            checkButtonStates();
            return;
        }
        currentDisplaydIndex.set(0);
        checkButtonStates();
        showCurrentSimilarImages();
    }

    private void showCurrentSimilarImages() {
        centerBox.getChildren().clear();
        Integer currentIndex = currentDisplaydIndex.get();
        if (currentIndex == null || groupedSimilarImagesByHash.isEmpty()) {
            labelCurrentImageIndex.setText("0/0");
            return;
        }
        labelCurrentImageIndex.setText((currentIndex + 1) + "/" + groupedSimilarImagesByHash.size());

        final List<ImageInformationDto> similarImages;
        if (currentIndex < 0)
            similarImages = groupedSimilarImagesByHash.getFirst().similarImages;
        else if (currentIndex >= groupedSimilarImagesByHash.size())
            similarImages = groupedSimilarImagesByHash.getLast().similarImages;
        else
            similarImages = groupedSimilarImagesByHash.get(currentIndex).similarImages;
        for (ImageInformationDto similarImage : similarImages) {
            ImageView imageView = new ImageView(similarImage.getPath().toUri().toString());
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(400);
            String fileName = similarImage.getPath().toString();
            String baseDirectory = getPathToFileInfosXml().getParent().toAbsolutePath().toString();
            if (fileName.startsWith(baseDirectory))
                fileName = fileName.substring(baseDirectory.length() + 1);
            Label labelFileName = new Label(fileName);
            Label labelImageSize = new Label(similarImage.getWidth() + "x" + similarImage.getHeight());
            Label labelFileSize = new Label();
            try {
                labelFileSize.setText(Files.size(similarImage.getPath()) + " bytes");
            } catch (IOException e) {
                labelFileSize.setText("Could not read file size");
            }
            Button buttonDelete = new Button("Delete");
            buttonDelete.setOnAction(x -> {
                try {
                    Files.delete(similarImage.getPath());
                    showCurrentSimilarImages();
                } catch (IOException e) {
                    showError("Could not delete file " + similarImage.getPath());
                }
            });

            VBox box = new VBox(imageView, labelFileName, labelImageSize, labelFileSize, buttonDelete);
            centerBox.getChildren().add(box);
        }
    }

    private static class SimilarImages {
        final int imageHash;
        final List<ImageInformationDto> similarImages;

        private SimilarImages(int imageHash, List<ImageInformationDto> similarImages) {
            this.imageHash = imageHash;
            this.similarImages = similarImages;
        }
    }
}
