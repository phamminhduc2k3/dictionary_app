package com.example.demo.Cli;

import java.io.*;
import java.util.*;

public class EnglishQuizGame {
    private List<Question> questions;
    private Scanner scanner;

    public EnglishQuizGame(String filePath) throws FileNotFoundException {
        questions = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadQuestions(filePath);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    private void loadQuestions(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner fileScanner = new Scanner(file);

        while (fileScanner.hasNextLine()) {
            String type = fileScanner.nextLine();
            String questionText = fileScanner.nextLine();
            if (type.equals("MC")) { // Multiple Choice
                String[] options = new String[4];
                for (int i = 0; i < 4; i++) {
                    options[i] = fileScanner.nextLine();
                }
                String correctAnswer = fileScanner.nextLine();
                questions.add(new Question(type, questionText, options, correctAnswer));
            }
        }

        fileScanner.close();
    }

}