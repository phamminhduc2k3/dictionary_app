package com.example.demo.Controllers;

import javafx.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import com.example.demo.Cli.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class GameController implements Initializable {
    private static final String MULTIPLE_CHOICE = "MC";
//    private static final String FILL_IN_THE_BLANK = "FIB";
//    private static final String SHORT_ANSWER = "SA";
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questions;
    private EnglishQuizGame Egame;
    private String gameFilePath = "src/main/resources/Data/questions.txt";

    public GameController() throws FileNotFoundException {
        Egame = new EnglishQuizGame(gameFilePath);
        questions = Egame.getQuestions();
        Collections.shuffle(questions);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayNextQuestion();
    }
    @FXML
    private void handleReplayAction(ActionEvent event) {
        try {
            // Tạo một trò chơi mới
            Egame = new EnglishQuizGame(gameFilePath);
            questions = Egame.getQuestions();
            Collections.shuffle(questions);

            // Đặt lại trạng thái ban đầu của trò chơi
            currentQuestionIndex = 0;
            score = 0;

            // Hiển thị câu hỏi đầu tiên
            displayNextQuestion();
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Xử lý lỗi nếu không tìm thấy tệp tin
        }
    }


    @FXML
    private void handleOptionAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        checkAnswer(clickedButton);
    }

    private void checkAnswer(Button selectedButton) {
        Question currentQuestion = questions.get(currentQuestionIndex);

        if (currentQuestion.getType().equals(MULTIPLE_CHOICE)) {
            String selectedAnswer = selectedButton.getText().substring(0,1);
            Button correctButton = null;

            // Xác định nút nào chứa đáp án đúng
            if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionA.getText().substring(0,1)))
                correctButton = optionA;
            else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionB.getText().substring(0,1)))
                correctButton = optionB;
            else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionC.getText().substring(0,1)))
                correctButton = optionC;
            else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionD.getText().substring(0,1)))
                correctButton = optionD;
            if (selectedAnswer.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
                currentQuestionIndex ++;
                score++;
                scoreLabel.setText("Score: " + score);
                correctButton.setStyle("-fx-background-color: #21e833;"); // Đáp án đúng
            } else {
                correctButton.setStyle("-fx-background-color: #21e833;"); // Hiển thị đáp án đúng
                selectedButton.setStyle("-fx-background-color: #ff4747 ;"); // Hiển thị đáp án sai
                new Thread(() -> {
                    try {
                        Thread.sleep(2); // Đợi 2 giây
                        Platform.runLater(this::endGame);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
                return;
            }
        }
        prepareNextQuestion();
    }

    private void prepareNextQuestion() {
        NextButton.setVisible(true);
        NextButton.setDisable(false);
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        optionD.setDisable(true);
        NextButton.setOnAction(event -> {
            if (currentQuestionIndex < questions.size() - 1) { // Kiểm tra xem còn câu hỏi tiếp theo không
                displayNextQuestion(); // Hiển thị câu hỏi mới
            } else {
                endGame(); // Nếu không còn câu hỏi, kết thúc trò chơi
            }
        });

    }

    private void resetButtonColors() {
        optionA.setStyle("-fx-background-color: #1dfee8;");
        optionB.setStyle("-fx-background-color: #1dfee8;");
        optionC.setStyle("-fx-background-color: #1dfee8;");
        optionD.setStyle("-fx-background-color: #1dfee8;");
    }

    private void displayNextQuestion() {
        Replay.setVisible(false);
        Replay.setDisable(true);
        if (currentQuestionIndex < questions.size()) {
            resetButtonColors();
            Question question = questions.get(currentQuestionIndex);
            questionLabel.setText("Question " + (currentQuestionIndex + 1) + ": " + question.getQuestionText());
            // Cập nhật giao diện dựa trên loại câu hỏi
            if (question.getType().equals(MULTIPLE_CHOICE)) {
                // Tương tự như đã thiết lập trong `initialize()`
                NextButton.setVisible(false);

                optionA.setVisible(true);
                optionB.setVisible(true);
                optionC.setVisible(true);
                optionD.setVisible(true);
                optionA.setDisable(false);
                optionB.setDisable(false);
                optionC.setDisable(false);
                optionD.setDisable(false);

                optionA.setText(question.getOptions()[0]);
                optionB.setText(question.getOptions()[1]);
                optionC.setText(question.getOptions()[2]);
                optionD.setText(question.getOptions()[3]);
            }
            questionLabel.setMinHeight(72.0);

            // Thêm một ChangeListener để theo dõi khi chiều cao của label thay đổi
            questionLabel.heightProperty().addListener((obs, oldHeight, newHeight) -> {
                if (newHeight.doubleValue() > questionLabel.getMinHeight()) {
                    questionLabel.setMinHeight(newHeight.doubleValue());
                }
            });

            scoreLabel.setText("Score: " + score);
            NextButton.setDisable(true);
        } else {
            endGame();
        }
    }
    private void endGame() {
        questionLabel.setText("Trò chơi kết thúc! Điểm của bạn là: " + score);
        Replay.setVisible(true);
        Replay.setDisable(false);
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        optionD.setDisable(true);
        NextButton.setVisible(false);
        NextButton.setDisable(true);
    }
    @FXML
    private Label questionLabel, scoreLabel;
    @FXML
    private Button optionA, optionB, optionC, optionD, NextButton, Replay;
}
