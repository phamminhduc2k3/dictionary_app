package com.example.demo.Cli;

import java.util.TreeMap;

public class Dictionary {
    private TreeMap<String, String> words = new TreeMap<String, String>();

    public TreeMap<String, String> getWords() {
        return words;
    }

    /**
     * Inserts a word into the dictionary.
     *
     * @param word the word to be inserted
     */
    public void insert(Word word) {
        words.put(word.getWordTarget(), word.getWordExplain());
    }

    /**
     * Removes a word from the dictionary.
     *
     * @param wordTarget
     */
    public void removeWord(String wordTarget) {
        words.remove(wordTarget.toLowerCase());
    }

    /**
     * Translates a word from the dictionary.
     *
     * @param wordTarget
     * @return the translated word
     */
    public String translate(String wordTarget) {
        return words.get(wordTarget.toLowerCase());
    }
}
