package com.example.demo.Controllers;

import com.example.demo.Cli.DictionaryManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    protected final DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private static final String DATA_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\Data\\E_V.txt";
    private static final String Bookmark_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\Data\\bookmark.txt";
    private static final String History_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\Data\\history.txt";
    private void addPane(AnchorPane anchorPane) {
        container.getChildren().clear();
        container.getChildren().add(anchorPane);
    }

    private void setContainer(String path) {
        try {
            AnchorPane container = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            addPane(container);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadFiles() {
        try {
            DictionaryManagement.loadDataFromFile(MainController.getDictionary(),DATA_PATH);
            DictionaryManagement.loadDataFromFile(MainController.getBookmarkDictionary(),Bookmark_PATH);
            DictionaryManagement.loadDataFromFile(MainController.getHistoryDictionary(),History_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.setOnAction((ActionEvent event) -> {
            setContainer("/com/example/demo/search.fxml");
        });

        translateBtn.setOnAction((ActionEvent event) -> {
            setContainer("/com/example/demo/translate.fxml");
        });
        addBtn.setOnAction((ActionEvent event) -> {
            setContainer("/com/example/demo/addWord.fxml");
        });

        bookmarkBtn.setOnAction((ActionEvent event) -> {
            setContainer("/com/example/demo/bookmark.fxml");
        });

        historyBtn.setOnAction((ActionEvent event) -> {
            setContainer("/com/example/demo/history.fxml");
        });
        Game.setOnAction((ActionEvent event) -> {
            setContainer("/com/example/demo/Game.fxml");
        });

        exitBtn.setOnAction((ActionEvent event ) -> {
            try {
                DictionaryManagement.updateWord(MainController.getDictionary(), DATA_PATH);
                DictionaryManagement.updateWord(MainController.getHistoryDictionary(), History_PATH);
                DictionaryManagement.updateWord(MainController.getBookmarkDictionary(), Bookmark_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            javafx.application.Platform.exit();
        } );

//        setContainer("/com/example/demo/search.fxml");
    }

    @FXML
    public Button searchBtn;
    @FXML
    public Tooltip searchTooltip;
    @FXML
    public Button translateBtn;
    @FXML
    public Tooltip translateTooltip;
    @FXML
    public Button addBtn;
    @FXML
    public Tooltip addTooltip;
    @FXML
    public Button historyBtn;
    @FXML
    public Tooltip historyTooltip;
    @FXML
    public Button bookmarkBtn;
    @FXML
    public Tooltip bookmarkTooltip;
    @FXML
    public AnchorPane container;
    @FXML
    public Button exitBtn;
    @FXML
    public Button Game;
    @FXML
    public Tooltip GameTooltip;


    @FXML
    private AnchorPane searchPane;
    @FXML
    private AnchorPane translatePane;
    @FXML
    private AnchorPane addWordPane;

}
