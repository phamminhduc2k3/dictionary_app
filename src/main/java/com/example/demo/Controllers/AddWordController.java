package com.example.demo.Controllers;

import com.example.demo.Cli.DictionaryManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.example.demo.Cli.Dictionary;
import com.example.demo.Cli.Word;


public class AddWordController implements Initializable {
    private final Dictionary dictionary = MainController.getDictionary();
    private static final String DATA_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\Data\\E_V.txt";
    private final DictionaryManagement dictionaryManagement = new DictionaryManagement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addWordEditor.setVisible(false);
        try {
            DictionaryManagement.loadDataFromFile(dictionary, DATA_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void notChoseWordAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Không có từ nào được chọn!");
        alert.showAndWait();
    }

    /** create a template for the word meaning.
     *
     */
    public void addTemplate(ActionEvent actionEvent) {
        addWordEditor.setVisible(true);
        addWordEditor.setHtmlText("<html>" + addTextField.getText()
                + "<ul><li><b><i> Từ loại: </i></b><ul><li><font color='#cc0000'><b> Nghĩa thứ nhất: </b></font><ul></li></ul></ul></li></ul><ul><li><b><i> Từ loại khác: </i></b><ul><li><font color='#cc0000'><b> Nghĩa thứ hai: </b></font></li></ul></li></ul></html>");
    }

    public void addWord(ActionEvent actionEvent) throws IOException {
        String wordExplain = addWordEditor.getHtmlText().replace(" dir=\"ltr\"", "");
        String wordTarget = addTextField.getText().toLowerCase().trim();
        if (addTextField.getText().isEmpty() || addTextField.getText().isBlank()) {
            notChoseWordAlert();
            return;
        }
        Alert confirmAlert = confirmAlert("Thông báo", "Bạn có chắc chắn muốn thêm từ này không");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (dictionary.getWords().containsKey(wordTarget)) {
                showWarningAlert("Thông báo", "Từ bạn muốn thêm đã có trong từ điển!\nXin vui lòng chọn từ khác!");
            } else {
                Word word = new Word(wordTarget, wordExplain);
                dictionary.insert(word);
//                DictionaryManagement.updateWord(dictionary, DATA_PATH);
                showInfoAlert("Thông báo", "Đã thêm từ thành công");
            }
        } else if (result.get() == ButtonType.CANCEL) {
            showInfoAlert("Thông báo", "Đã hủy thêm từ mới");
        }

    }

    public void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showWarningAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Alert confirmAlert(String title, String content) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle(title);
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText(content);
        return confirmAlert;
    }


    @FXML
    public TextField addTextField;
    @FXML
    public Button addWordBtn;
    @FXML
    public WebView addWordView;
    @FXML
    public HTMLEditor addWordEditor;
    @FXML
    public Button addTemplateBtn;
}
