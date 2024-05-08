package com.example.demo.Cli;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public class DictionaryManagement {
    private static final String DATA_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\Data\\E_V.txt";
    private static final String SPLITTING_CHARACTERS = "<html>";

    public static void insertFromCommandline(Dictionary dictionary) {
//        Scanner scanner = new Scanner(System.in);
        Scanner getStringInput = new Scanner(System.in);
        Scanner getIntegerInput = new Scanner(System.in);
        System.out.print("Number of word to add to dictionary: ");
        int wordNum = getIntegerInput.nextInt();
        while (wordNum > 0) {
            System.out.print("Enter your word: ");
            String wordTarget = getStringInput.nextLine();
            System.out.print("Enter the meaning: ");
            String wordExplain = getStringInput.nextLine();
            Word newWord = new Word(wordTarget, wordExplain);
            //dictionary.add(newWord);
            dictionary.insert(newWord);
            wordNum--;
        }
    }

    public static void loadDataFromFile(Dictionary dictionary, String path) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(SPLITTING_CHARACTERS);
            String word = parts[0];
            String def = SPLITTING_CHARACTERS + parts[1];
            Word newWord = new Word(word, def);
            dictionary.insert(newWord);
        }
        br.close();
    }
//    public static void loadDataFromFileCommandline(Dictionary dictionary, String path) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] parts = line.split("\t"); // Sử dụng ký tự tab để phân tách
//            if (parts.length >= 2) { // Đảm bảo rằng dòng có ít nhất hai phần
//                String word = parts[0];
//                String def = parts[1];
//                Word newWord = new Word(word, def);
//                dictionary.insert(newWord);
//            }
//        }
//        br.close();
//    }
public static void insertFromFile( Dictionary dictionary,String fileName) {
    try (InputStream inputStream = DictionaryManagement.class.getClassLoader().getResourceAsStream(fileName)) {
        if (inputStream != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (parts.length >= 2) {
                        String wordTarget = parts[0].trim();
                        String wordExplain = parts[1].trim();
                        Word newWord = new Word(wordTarget, wordExplain);
                        dictionary.insert(newWord);
                    }
                }
            }
        } else {
            System.err.println("File not found: " + fileName);
        }
    } catch (IOException e) {
        System.err.println("Error reading file: " + e.getMessage());
    }
}


    public static void insertFromFile(Dictionary dictionary) {
        try {
            loadDataFromFile(dictionary, DATA_PATH);
            insertFromCommandline(dictionary);
        } catch (FileNotFoundException f) {
            System.out.println("Can not find the file" + f);
        } catch (IOException e) {
            System.out.println("IOException" + e);
        }
    }

    public static void dictionaryLookup(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the word you are looking for: ");
        String find = scanner.nextLine().trim().toLowerCase();
        if (dictionary.getWords().containsKey(find)) {
            System.out.println("Meaning: " +  dictionary.translate(find));
        } else {
            System.out.println("Could not find the word you entered");
        }

    }

    public static void dictionaryExportToFile(Dictionary dictionary) {
        String MOD_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\dictionary.txt";
        try {
            File file = new File(MOD_PATH);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
               file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<String, String> i : dictionary.getWords().entrySet()) {
                bw.write(i.getKey() + " \t " + i.getValue());
                bw.newLine();
//                fileWriter.write(i.getKey() + i.getValue() + "\n");
            }
            bw.close();
//            fileWriter.flush();
//            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public static void dictionaryEdit(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the word you want to edit:");
        String edit = scanner.nextLine().trim().toLowerCase();
        if (dictionary.getWords().containsKey(edit)) {
            System.out.println("Enter the meaning:");
            String meaning = scanner.nextLine();
            dictionary.getWords().replace(edit, meaning);
        } else {
            System.out.println("Could not find the word you entered");
        }
    }

    public static void dictionaryRemove(Dictionary dictionary) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the word you want to remove:");
        String remove = scanner.nextLine().trim().toLowerCase();
        if (dictionary.getWords().containsKey(remove)) {
            dictionary.removeWord(remove);
            System.out.println("Deleted");
        } else {
            System.out.println("Could not find the word you entered");
        }
    }


//    public ObservableList<String> searcher(Dictionary dictionary, String searchWord) {
//        searchWord = searchWord.trim().toLowerCase();
//        ObservableList<String> list = FXCollections.observableArrayList();
//        Set<String> entries = dictionary.getWords().keySet();
//        for (String entry : entries) {
//            if (entry.startsWith(searchWord)) {
//                list.add(entry);
//            }
//        }
//        return list;
//    }
public ObservableList<String> searcher(Dictionary dictionary, String searchWord) {
    searchWord = searchWord.trim().toLowerCase();
    ObservableList<String> list = FXCollections.observableArrayList();
    List<String> sortedWords = new ArrayList<>(dictionary.getWords().keySet());
    Collections.sort(sortedWords);

    int startIndex = binarySearchRecursive(sortedWords, searchWord, 0, sortedWords.size());
    if (startIndex >= 0) {
        // Tìm thấy chỉ số bắt đầu của các từ có tiền tố tương đồng
        for (int i = startIndex; i < sortedWords.size(); i++) {
            int length = searchWord.length();
               String word = sortedWords.get(i);
            int LengthWord = word.length();
            int LengthsearchWord = searchWord.length();
            if (word.startsWith(searchWord)) {
                list.add(word);
            }
            // neu khong co a a ma chi co a (chi so bat dau) va a a a thi khi tim kiem se khong ra neu khong co else if
            else if (searchWord.startsWith(word) || word.startsWith(" " + searchWord)) {
                continue;
            } else {
                break; // Dừng khi không còn từ nào có tiền tố tương đồng
            }
        }
        for (int i = startIndex -1 ; i >= 0; i--) {
            int length = searchWord.length();
            String word = sortedWords.get(i);
            if (word.startsWith(searchWord) || word.startsWith(" " + searchWord)) {
                list.add(word);
            } else {
                break; // Dừng khi không còn từ nào có tiền tố tương đồng
            }
        }
    } else {
        return list;
    }
    Collections.sort(list);
    return list;
}


    private int binarySearchRecursive(List<String> words, String searchWord, int left, int right) {
        int mid = 0;
        while (left <= right) {
            mid = left + (right - left) / 2;
            String midWord = words.get(mid);
            int LengthmidWord = midWord.length();
            int LengthsearchWord = searchWord.length();
            int res = 0;
                res = searchWord.compareTo(midWord);
            if (res == 0) {
                return mid; // Tìm thấy từ khóa
            } else if (res < 0) {
                right = mid -1 ; // Tìm kiếm bên trái
            } else {
                left = mid +1 ; // Tìm kiếm bên phải
            }
        }
        return mid; // Không tìm thấy từ khóa
    }






    /**rewrite the dictionary
     *
     */
    public static void updateWord(Dictionary dictionary, String path) throws IOException {
        try{
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file);
            for (Map.Entry<String, String> i : dictionary.getWords().entrySet()) {
                fileWriter.write(i.getKey() + i.getValue() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

