package com.example.demo.Controllers;

import com.example.demo.Cli.Dictionary;
import com.example.demo.Cli.DictionaryManagement;
import com.example.demo.Cli.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.Set;

public abstract class MainController implements Initializable {
    private static final String DATA_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\Data\\E_V.txt";
    private static final String Bookmark_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\Data\\bookmark.txt";
    private static final String History_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\Data\\history.txt";
    protected final ObservableList<String> searchList = FXCollections.observableArrayList();
    protected final ObservableList<String> bookmarkList = FXCollections.observableArrayList();
    protected final Dictionary bookmarkDictionary = DictionaryContainer.getBookmarkDictionary();
    protected final Dictionary historyDictionary = DictionaryContainer.getHistoryDictionary();
    protected final Dictionary dictionary = DictionaryContainer.getDictionary();
    protected final DictionaryManagement dictionaryManagement = new DictionaryManagement();
    protected boolean isEdit = false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /** load the dictionary from the file
     *
     */
    public void setSearchList(Dictionary dictionary) {
        searchList.clear();
        Set<String> entries = dictionary.getWords().keySet();
        searchList.addAll(entries);
        resultListView.setItems(searchList);
    }

    /** show the alert when cannot find the word you need.
     *
     */
    public void notFoundWordAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Không tìm thấy từ bạn tìm kiếm!");
        alert.showAndWait();
    }


    /** show the alert when you not chose word before you want to edit, delete,etc.
     *
     */
    public void notChoseWordAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Không có từ nào được chọn!");
        alert.showAndWait();
    }

    public void loadDictionaryData() throws IOException {
       DictionaryManagement.loadDataFromFile(dictionary, DATA_PATH);
    }

    public void loadBookmarkData() throws IOException {
        DictionaryManagement.loadDataFromFile(bookmarkDictionary, Bookmark_PATH);
    }

    public void loadHistoryData() throws IOException {
        DictionaryManagement.loadDataFromFile(historyDictionary, History_PATH);
    }

    public void updateHistoryDictionary() throws IOException {
       DictionaryManagement.updateWord(historyDictionary, History_PATH);
    }

    /** show the match result when search for the word.
     *
     */
//    public abstract void searchTextFieldAction(KeyEvent keyEvent);
    public void setSearchTextFieldAction(KeyEvent keyEvent, Dictionary dictionary) {
        String searchWord =searchTextField.getText().trim().toLowerCase();
        if (!searchWord.isEmpty()) {
            if (dictionaryManagement.searcher(dictionary, searchWord).isEmpty()) {
                notFoundWordAlert();
                setSearchList(dictionary);
            } else {
                resultListView.setItems(dictionaryManagement.searcher(dictionary, searchWord));
            }
        } else {
            setSearchList(dictionary);
        }
    }


    @FXML
    public abstract void handleClickListView() throws IOException;

    @FXML
    public void handleClickUKSoundBtn() throws UnsupportedEncodingException {
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word == null) {
            notChoseWordAlert();
        } else {
            String URL = "https://translate.google.com.vn/translate_tts?ie=UTF-8&q=" + URLEncoder.encode(word, StandardCharsets.UTF_8) + "&tl=en-uk&client=tw-ob";
            Media sound = new Media(URL);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }

    @FXML
    public void handleClickUSSoundBtn() throws UnsupportedEncodingException{
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word == null) {
            notChoseWordAlert();
        } else {
            String URL = "https://translate.google.com.vn/translate_tts?ie=UTF-8&q=" + URLEncoder.encode(word, StandardCharsets.UTF_8) + "&tl=en-us&client=tw-ob";
            Media sound = new Media(URL);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }
    /** replace a word in a dictionary.
     *
     */
    public void updateWordToFile(Dictionary dictionary, String word, String newMeaning) throws IOException {
        dictionary.getWords().replace(word, newMeaning);
    }
    @FXML
    public void handleClickSaveBtn() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Sửa từ thành công!");
        alert.showAndWait();
        saveChangeBtn.setVisible(false);
        editDefinition.setVisible(false);
        isEdit = false;
        String newMeaning = editDefinition.getHtmlText().replace(" dir=\"ltr\"", "");
        String word = resultListView.getSelectionModel().getSelectedItem();
        updateWordToFile(dictionary, word, newMeaning);
        updateWordToFile(bookmarkDictionary, word, newMeaning);
        updateWordToFile(historyDictionary, word, newMeaning); // Chu y
        explainView.getEngine().loadContent(newMeaning, "text/html");
    }



    @FXML
    public void handleClickEditBtn() {
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word == null) {
            notChoseWordAlert();
            return;
        }
        if (isEdit) {
            isEdit = false;
            editDefinition.setVisible(false);
            saveChangeBtn.setVisible(false);
            return;
        }
        isEdit = true;
        saveChangeBtn.setVisible(true);
        editDefinition.setVisible(true);
        String meaning = dictionary.translate(word);
        editDefinition.setHtmlText(meaning);
    }

    @FXML
    public void handleClickRemoveBtn() throws IOException {
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word == null) {
            notChoseWordAlert();
        } else {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setContentText("Bạn có muốn xóa từ này không");
                alert.showAndWait();
                if (alert.getResult() == javafx.scene.control.ButtonType.OK) {
                    dictionary.removeWord(word);
                    bookmarkDictionary.removeWord(word);
                    historyDictionary.removeWord(word);
                    resultListView.getItems().remove(word);
//                    DictionaryManagement.updateWord(dictionary, DATA_PATH);
//                    DictionaryManagement.updateWord(bookmarkDictionary, Bookmark_PATH);
//                    DictionaryManagement.updateWord(historyDictionary, History_PATH);
                    explainView.getEngine().loadContent("");
                } else {
                    alert.close();
                }
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void handleClickRemoveBtnBookmark() throws IOException {
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word == null) {
            notChoseWordAlert();
        } else {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setContentText("Bạn có muốn xóa từ này không");
                alert.showAndWait();
                if (alert.getResult() == javafx.scene.control.ButtonType.OK) {
                    bookmarkDictionary.removeWord(word);
                    resultListView.getItems().remove(word);
//                    DictionaryManagement.updateWord(dictionary, DATA_PATH);
//                    DictionaryManagement.updateWord(bookmarkDictionary, Bookmark_PATH);
//                    DictionaryManagement.updateWord(historyDictionary, History_PATH);
                    explainView.getEngine().loadContent("");
                } else {
                    alert.close();
                }
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void handleClickRemoveBtnHistory() throws IOException {
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word == null) {
            notChoseWordAlert();
        } else {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setContentText("Bạn có muốn xóa từ này không");
                alert.showAndWait();
                if (alert.getResult() == javafx.scene.control.ButtonType.OK) {
                    historyDictionary.removeWord(word);
                    resultListView.getItems().remove(word);
//                    DictionaryManagement.updateWord(dictionary, DATA_PATH);
//                    DictionaryManagement.updateWord(bookmarkDictionary, Bookmark_PATH);
//                    DictionaryManagement.updateWord(historyDictionary, History_PATH);
                    explainView.getEngine().loadContent("");
                } else {
                    alert.close();
                }
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleClickBookmarkBtn() throws IOException {
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word == null) {
            notChoseWordAlert();
        } else {
            if (bookmarkDictionary.getWords().containsKey(word)) {
                removeBookmark(word);
                explainView.getEngine().loadContent("");
            } else {
                addBookmark(word);
            }
        }
    }
    public void handleClickBookmarkBtninsearch() throws IOException {
        String word = resultListView.getSelectionModel().getSelectedItem();
        if (word == null) {
            notChoseWordAlert();
        } else {
            if (bookmarkDictionary.getWords().containsKey(word)) {
                return;
            } else {
                addBookmark(word);
            }
        }
    }


    protected void addBookmark(String word) throws IOException {
        bookmarkBtn.setVisible(true);
        notBookmarkBtn.setVisible(false);
        String meaning = dictionary.translate(word);
        Word newWord = new Word(word, meaning);
        bookmarkDictionary.insert(newWord);
//        DictionaryManagement.updateWord(bookmarkDictionary, Bookmark_PATH);
    }

    protected void removeBookmark(String word) throws IOException {
        bookmarkBtn.setVisible(false);
        notBookmarkBtn.setVisible(true);
        bookmarkDictionary.removeWord(word);
        resultListView.getItems().remove(word);
//        resultListView.getItems().remove(word);
//        explainView.getEngine().loadContent("");
//        DictionaryManagement.updateWord(bookmarkDictionary, Bookmark_PATH);
    }


    @FXML
    public Button bookmarkBtn;
    @FXML
    public Button notBookmarkBtn;
    @FXML
    public Button UKSoundBtn;
    @FXML
    public Button USSoundBtn;
    @FXML
    public Button editBtn;
    @FXML
    public Button deleteBtn;
    @FXML
    public Button saveChangeBtn;
    @FXML
    public ListView<String> resultListView;
    @FXML
    public WebView explainView;
    @FXML
    public TextField searchTextField;
    @FXML
    public HTMLEditor editDefinition;
}
