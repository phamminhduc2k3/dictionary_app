package com.example.demo.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.example.demo.Cli.TranslateAPI;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class TranslateController implements Initializable {
    private final HashMap<String, String> Lang = new HashMap<String, String>() {
        {
            put("Tiếng Việt", "vi");
            put("Tiếng Anh", "en");
            put("Tiếng Pháp", "fr");
            put("Tiếng Đức", "de");
            put("Tiếng Nhật", "ja");
            put("Tiếng Hàn", "ko");
            put("Tiếng Trung (Giản thể)", "zh-CN");
            put("Tiếng Trung (Phồn thể", "zh-TW");
            put("Tiếng Nga", "ru");
            put("Tiếng Tây Ba Nha", "es");
        }
    };

    private String langFrom = "";
    private String langTo = "";

    public void getLangFrom(ActionEvent actionEvent) {
        this.langFrom = Lang.get(sourceLangBox.getValue());
    }

    public void getLangTo(ActionEvent actionEvent) {
        this.langTo = Lang.get(targetLangBox.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sourceLangBox.getItems().addAll(Lang.keySet());
        targetLangBox.getItems().addAll(Lang.keySet());
        sourceLangBox.getSelectionModel().select(6);
        targetLangBox.getSelectionModel().select(0);
        textArea1.setWrapText(true);
        langFrom = "en";
        langTo = "vi";
    }

    public void handleClickSound1(ActionEvent actionEvent) {
        String searchWord = textArea1.getText();
        if (searchWord == null) {
            noInputWordAlert();
        } else {
            String URL = "https://translate.google.com.vn/translate_tts?ie=UTF-8&q=" + URLEncoder.encode(searchWord, StandardCharsets.UTF_8) + "&tl=" + langFrom + "&client=tw-ob";
            Media sound = new Media(URL);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }

    public void handleClickSound2(ActionEvent actionEvent) {
        String searchWord = textArea2.getText();
        if (searchWord == null) {
            noInputWordAlert();
        } else {
            String URL = "https://translate.google.com.vn/translate_tts?ie=UTF-8&q=" + URLEncoder.encode(searchWord, StandardCharsets.UTF_8) + "&tl=" + langTo + "&client=tw-ob";
            Media sound = new Media(URL);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }

    public void searchTextAreaAction(KeyEvent keyEvent) throws IOException {
        String searchWord = textArea1.getText();
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (searchWord.isBlank()) {
                noInputWordAlert();
            } else if (langFrom.equals(langTo)) {
                sameLanguageAlert();
            } else {
                textArea2.setText(TranslateAPI.googleTranslate(langFrom, langTo, searchWord));
            }
        }
    }

    public void noInputWordAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Xin hãy nhập từ bạn cần tìm kiếm!");
        alert.showAndWait();
    }

    public void sameLanguageAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã lựa chọn cùng một ngôn ngữ. Xin hãy chọn lại!");
        alert.showAndWait();
    }

    @FXML
    public Button soundBtn1;
    @FXML
    public Button soundBtn2;
    @FXML
    public TextArea textArea1;
    @FXML
    public TextField textArea2;
    @FXML
    public ComboBox<String> sourceLangBox;
    @FXML
    public ComboBox<String> targetLangBox;

}
