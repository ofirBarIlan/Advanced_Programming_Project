package test;

import java.util.HashMap;

public class DictionaryManager {

    //Hash-map of strings to dictionaries
    private HashMap<String, Dictionary> dictionaries;

    //Singleton
    private static class DictionaryManagerHolder {
        private static DictionaryManager instance = new DictionaryManager();
    }

    //Constructor
    private DictionaryManager() {
        dictionaries = new HashMap<String, Dictionary>();
    }


    //query method - returns true if the word is in the dictionary
    public boolean query(String... args) {
        //get the last argument
        String word = args[args.length - 1];

        //get the book names
        String[] bookNames = new String[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            bookNames[i] = args[i];
            if (!dictionaries.containsKey(bookNames[i])) {
                dictionaries.put(bookNames[i], new Dictionary(bookNames[i]));
            }
        }

        //check in each dictionary if the word is in it
        boolean found = false;
        for (int i = 0; i < bookNames.length; i++) {
            if (dictionaries.get(bookNames[i]).query(word)) {
                found = true;
            }
        }

        return found;
    }

    //challenge method - returns true if the word is in the dictionary
    public boolean challenge(String... args) {
        //get the last argument
        String word = args[args.length - 1];

        //get the book names
        String[] bookNames = new String[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            bookNames[i] = args[i];
            if (!dictionaries.containsKey(bookNames[i])) {
                dictionaries.put(bookNames[i], new Dictionary(bookNames[i]));
            }
        }

        //check in each dictionary if the word is in it
        boolean found = false;
        for (int i = 0; i < bookNames.length; i++) {
            if (dictionaries.get(bookNames[i]).challenge(word)) {
                found = true;
            }
        }

        return found;
    }


    //getSize method - returns the number of entries in the dictionary
    public int getSize() {
        return dictionaries.size();//TODO: understand what this method should return
    }


    //get method - returns the instance of the dictionary manager
    public static DictionaryManager get() {
        return DictionaryManagerHolder.instance;
    }



}
