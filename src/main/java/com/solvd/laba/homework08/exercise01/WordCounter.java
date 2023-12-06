package com.solvd.laba.homework08.exercise01;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class WordCounter {
    public static void main(String[] args) {
        String inputFilename = "src/main/resources/random_words.txt";
        String outputFilename = "src/main/resources/uniqueWordsCount.txt";

        // count unique words
        try {
            int uniqueWordCount = countWords(inputFilename);
            String resultToWrite = "file \"%s\" have %d unique words".formatted(
                    inputFilename, uniqueWordCount);
            FileUtils.writeStringToFile(new File(outputFilename), resultToWrite, "UTF-8");

        } catch (IOException e) {
            System.out.println("File error: " + e.toString());
        }
    }

    /**
     * count number of unique words in filename
     *
     * @param filename
     * @return
     * @throws IOException
     */
    static int countWords(String filename) throws IOException {
        final String CHARS_TO_IGNORE = ",.-";
        Set<String> uniqueWords = new HashSet<>();

        // load files and separate into list of words
        String text = FileUtils.readFileToString(new File(filename), "UTF-8");
        List<String> words = List.of(StringUtils.split(text));

        // clean up words to avoid duplication, and count then
        // (remove irrelevant chars and convert to lowercase)
        words.stream()
                .map(word -> StringUtils.lowerCase(
                        StringUtils.strip(word, CHARS_TO_IGNORE),
                        Locale.ENGLISH))
                .forEach(uniqueWords::add);
        return uniqueWords.size();
    }
}
