package com.nehavin.cookiecutter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CookieCutter {

    public static void main(String args[])
    {
        // scrambled words
        String words = args[0];
        // min length of word required, could start from 2 letter or 3 letter words
        int minLen = Integer.valueOf(args[1]);
        // max length of words required currently kept at the mav len of the scrambled word input
        int maxLen = words.length();

        System.out.println(words + ":::" + minLen + ":::" + maxLen);

        // create a hashmap with the count of the individual characters with the characters as the key
        HashMap<Character, Integer> wordsCount = charCount(words);

        // Array list to store the initial dictionary words
        List<String> listOfWords = new ArrayList<>(500000);

        //Read the text file from the file system into the array list
        try
        {
            BufferedReader in = new BufferedReader( new FileReader("words3.txt"));
            while(in.ready())
            {
                listOfWords.add(in.readLine());
            }
            System.out.println(listOfWords.get(0) + " and " + (listOfWords.get(listOfWords.size()-1)));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //sort the list of words loaded
        List<String> sublist = listOfWords.subList(1, listOfWords.size());
        Collections.sort(sublist);

        //create different lists for different length of words
        // so that we run through only those lists that are required.
        List<String> list2LetterWords = new ArrayList<>(1300);
        List<String> list3LetterWords = new ArrayList<>(6500);
        List<String> list4LetterWords = new ArrayList<>(13500);
        List<String> list5LetterWords = new ArrayList<>(25500);
        List<String> list6LetterWords = new ArrayList<>(42000);
        List<String> list7LetterWords = new ArrayList<>(339000);
        List<String> listNLetterWords = new ArrayList<>(480000);

        String temp = null;
        int len;

        // for loop to separate out the different length of words
        for (int i = 0; i < listOfWords.size(); i++)
        {
            temp = listOfWords.get(i);

            // regular expression to weed out unnecessary words with contain numbers and hypens
            // this regex can be skipped if the dictionary contains only words
            if (temp.matches("[a-zA-Z]*"))
            {
                len = temp.length();

                if (len >= minLen && len <= maxLen) {
                    switch (len)
                    {
                        case 1:
                            break;

                        case 2:
                            list2LetterWords.add(temp);
                            break;

                        case 3:
                            list3LetterWords.add(temp);
                            break;

                        case 4:
                            list4LetterWords.add(temp);
                            break;

                        case 5:
                            list5LetterWords.add(temp);
                            break;

                        case 6:
                            list6LetterWords.add(temp);
                            break;

                        case 7:
                            list7LetterWords.add(temp);
                            break;

                        default:
                            listNLetterWords.add(temp);
                            break;
                    }
                }
            }
        }

        // map containing hashmaps containing arraylist of words
        // there can be a better way of handling this and need to look into it.
        // also instead of these arraylists we can have the words in a db.
        Map<Integer, List> mapOfLists = new HashMap<>();

        mapOfLists.put(2, list2LetterWords);
        mapOfLists.put(3, list3LetterWords);
        mapOfLists.put(4, list4LetterWords);
        mapOfLists.put(5, list5LetterWords);
        mapOfLists.put(6, list6LetterWords);
        mapOfLists.put(7, list7LetterWords);
        mapOfLists.put(8, listNLetterWords);

        Set<Integer> setOfKeys = mapOfLists.keySet();
        Iterator<Integer> iter = setOfKeys.iterator();
        sublist.clear();

        // final list of words matching the input string, this will be output to the user
        List<String> finalListOfWords = new ArrayList<>(500);
        Integer keyVal = null;

        HashMap<Character, Integer> dbWordCnt = null;
        while(iter.hasNext())
        {
            keyVal = iter.next();

            // take only the arraylists that we require
            if(keyVal.intValue()>=minLen && keyVal.intValue()<=maxLen)
            {
                sublist = (List)mapOfLists.get(keyVal);
                // from individual arraylist fetch the dictionary words for comparison
                for (String aSublist : sublist) {
                    temp = aSublist;
                    // regex the dictionary word with the input word
                    if (temp.matches("[" + words + "]*"))
                    {
                        // if the dictionary word matches the input word then get individual char count for
                        // the dictionary word
                        dbWordCnt = charCount(temp);
                        //compare the char counts of the dictionary word and input word
                        if (mapsAreEqual(wordsCount, dbWordCnt))
                        {
                            finalListOfWords.add(temp);
                        }
                    }
                }
            }
        }

        for (String finalListOfWord : finalListOfWords) {
            System.out.println(finalListOfWord);
        }
    }

    // method to get return hashmap containing individual character counts
    // this method is run for both the dictionary word and input word
    // each dictionary word that matches the input word
    private static HashMap<Character, Integer> charCount(String words) {
        HashMap<Character, Integer> wordsCount = new HashMap<>();
        for (char ch : words.toCharArray())
        {
            if (wordsCount.containsKey(ch))
            {
                int val = wordsCount.get(ch);
                wordsCount.put(ch, val + 1);
            }
            else
            {
                wordsCount.put(ch, 1);
            }
        }
        return wordsCount;
    }


    // check if 2 character (dictionary & input) maps  are equal as far as values are concerned
    // The dictionary map needs to be checked against the input keyword
    private static boolean mapsAreEqual(Map<Character, Integer> inpWord, Map<Character, Integer> dbWord)
    {

        try{
            for (Character y : inpWord.keySet())
            {
                if(dbWord.containsKey(y))
                {
                    if (!(dbWord.get(y).intValue() <= inpWord.get(y).intValue()))
                    {
                        return false;
                    }
                }
            }
        } catch (NullPointerException np)
        {
            return false;
        }
        return true;
    }
}