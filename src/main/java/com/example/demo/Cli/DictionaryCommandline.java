package com.example.demo.Cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DictionaryCommandline {

    private static final String DATA_PATH = new File("").getAbsolutePath() + "dic_modified.txt";

    public static void showAllWords(Dictionary dictionary) {
        System.out.printf("%-8s| %-20s\t| %s\n", "No", "English", "Vietnamese");
        int x = 0;
        for (Map.Entry<String, String> e : dictionary.getWords().entrySet()) {
            x++;
            System.out.printf("%-8d| %-20s\t| %s\n", x, e.getKey(), e.getValue());
        }
    }

    public static void dictionaryBasic(Dictionary dictionary) {
        DictionaryManagement.insertFromCommandline(dictionary);
        showAllWords(dictionary);
    }

    public static void dictionaryAdvanced(Dictionary dictionary) throws IOException {
        boolean quit = false;
        while (!quit) {
            System.out.println("\nWelcome to My Application");
            System.out.println("[0] Exit");
            System.out.println("[1] Add");
            System.out.println("[2] Remove");
            System.out.println("[3] Update");
            System.out.println("[4] Display");
            System.out.println("[5] Lookup");
            System.out.println("[6] Search");
            System.out.println("[7] Import from file");
            System.out.println("[8] Export to file");
            System.out.println("Choose option: ");
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
            switch (option) {
                case 0: {
                    quit = true;
                    break;
                }
                case 1: {
                    DictionaryManagement.insertFromCommandline(dictionary);
                    System.out.println("Sync successfully!");
                    break;
                }
                case 2: {
                    DictionaryManagement.dictionaryRemove(dictionary);
                    break;
                }
                case 3: {
                    DictionaryManagement.dictionaryEdit(dictionary);
                    break;
                }
                case 4: {
                    DictionaryCommandline.showAllWords(dictionary);
                    break;
                }
                case 5: {
                    DictionaryManagement.dictionaryLookup(dictionary);
                    break;
                }
                case 6: {
                    DictionaryCommandline.dictionarySearcher(dictionary);
                    break;
                }
                case 7: {
//                    DictionaryManagement.loadDataFromFileCommandline(dictionary, "dic_modified.txt");
                    DictionaryManagement.insertFromFile(dictionary, "dictionary.txt");
                    System.out.println("Imported Successfully");
                    break;
                }
                case 8: {
                    DictionaryManagement.dictionaryExportToFile(dictionary);
                    break;
                }
                default: {
                    System.out.println("“Action not supported");
                    break;
                }
            }
        }
        System.out.println("See you next time!");
    }

    public static void dictionarySearcher(Dictionary dictionary) {
        System.out.print("Enter the prefix: ");
        Scanner scanner = new Scanner(System.in);
        String searchWord = scanner.nextLine().trim().toLowerCase();
        List<String> listWords = new ArrayList<String>();
        System.out.println("List words starts with \"" + searchWord + "\" is: ");
        for (Map.Entry<String, String> e : dictionary.getWords().entrySet()) {
//            if (e.getKey().length() >= searchWord.length()) {
//                if (searchWord.equals(e.getKey().substring(0, searchWord.length()))) {
//                    listWords.add(e.getKey());
//                }
//            }
            if (e.getKey().startsWith(searchWord)) {
                listWords.add(e.getKey());
            }
        }
        if (!listWords.isEmpty()) {
            System.out.print(listWords.get(0));
            for (int i = 1; i < listWords.size(); i++) {
                System.out.print(", " + listWords.get(i));
            }
        } else {
            System.out.print("There are no words that start with \"" + searchWord + "\" in dictionary!");
        }
    }

    public static void main(String[] args) throws IOException {
        Dictionary dictionary = new Dictionary();
        dictionaryAdvanced(dictionary);
    }
}
