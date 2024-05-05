package com.example.demo.Controllers;

import com.example.demo.Cli.Dictionary;

public class DictionaryContainer {
    private static final Dictionary dictionary = new Dictionary();
    private static final Dictionary historydictionary = new Dictionary();
    private static final Dictionary bookmarkdictionary = new Dictionary();
    public static Dictionary getBookmarkDictionary() {
        return bookmarkdictionary;
    }
    public static Dictionary getHistoryDictionary() {
        return historydictionary;
    }
    public static Dictionary getDictionary() {
        return dictionary;
    }

}
