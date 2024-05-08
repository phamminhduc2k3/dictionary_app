package com.example.demo;

import com.example.demo.Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    public MainController mainController = new MainController();
    @Override
    public void start(Stage stage) throws IOException {
        mainController.loaddata();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainView.fxml")));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Dictionary");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
