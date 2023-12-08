package com.solvd.laba.homework08.exercise01;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class WordCounter {
    public static void main(String[] args) {
        String inputFilename = "src/main/resources/random_words.txt";
        String outputFilename = "src/main/resources/uniqueWordsCount.txt";

        // count unique words and write to file
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
     * @param filename file for which to count number of unique words
     * @return number of unique words in specified file
     * @throws IOException
     */
    static int countWords(String filename) throws IOException {
        // load files and separate into list of words
        String fileText = FileUtils.readFileToString(new File(filename), "UTF-8");
        List<String> words = List.of(StringUtils.split(fileText));

        // clean up words to avoid duplication
        // (remove irrelevant chars and convert to lowercase)
        Set<String> uniqueWords = words.stream()
                .map(word -> StringUtils.lowerCase(word, Locale.ENGLISH))
                .map(WordCounter::removeNonAlphanumericChars)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toUnmodifiableSet());

        return uniqueWords.size();
    }

    static String removeNonAlphanumericChars(String input) {
        return input.codePoints()
                // convert code point to string to check if it's alphanumeric
                .filter(codePoint -> StringUtils.isAlphanumeric(Character.toString(codePoint)))
                // this solution for collecting code points to string is from stackoverflow
                // by user "shmosel" https://stackoverflow.com/questions/57318362/make-a-string-from-an-intstream-of-code-point-numbers
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
