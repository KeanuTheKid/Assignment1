/*
 * the MapReduce functionality implemented in this program takes a single large text file to map i.e. split it into small chunks
 * Then, all words are assigned an initial count of one
 * Finally, it reduces by counting the unique words
 */

package io.grpc.filesystem.task2;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class MapReduce {

    private static final int CHUNK_SIZE = 500;

    /**
     * Splits the input file into smaller chunks and stores them in a temporary directory.
     *
     * @param inputFilePath The path to the input file to be split.
     * @return The path of the directory where chunks are stored.
     * @throws IOException If an error occurs during file I/O.
     */
    public static String makeChunks(String inputFilePath) throws IOException {
        int count = 1;
        File inputFile = new File(inputFilePath);
        File chunkDir = new File(inputFile.getParent() + "/temp");
        if (!chunkDir.exists()) {
            chunkDir.mkdirs();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line = br.readLine();

            while (line != null) {
                File chunkFile = new File(chunkDir, "chunk" + String.format("%03d", count++) + ".txt");
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(chunkFile))) {
                    int fileSize = 0;
                    while (line != null) {
                        byte[] bytes = (line + System.lineSeparator()).getBytes(Charset.defaultCharset());
                        if (bytes.length > CHUNK_SIZE) {
                            System.err.println("Skipping line exceeding chunk size: " + line);
                            line = br.readLine();
                            continue;
                        }
                        if (fileSize + bytes.length > CHUNK_SIZE)
                            break;
                        out.write(bytes);
                        fileSize += bytes.length;
                        line = br.readLine();
                    }
                }
            }
        }
        return chunkDir.getPath();
    }

    /**
     * Removes all punctuation from the given line, converts it to lowercase,
     * trims leading/trailing whitespace, and collapses multiple spaces to a single space.
     *
     * @param line The line of text to be filtered.
     * @return The filtered (cleaned) line of text.
     */
    public static String filterPunctuations(String line) {
        if (line == null) return "";
        //lowercase
        String cleaned = line.toLowerCase();
        //remove all punctuation characters
        cleaned = cleaned.replaceAll("\\p{Punct}", "");
        //collapse multiple spaces to single space +trim
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        return cleaned;
    }

    /**
     * Splits the given line of text into words.
     *
     * @param line The line of text to split.
     * @return An array of words from the input line.
     */
    public static String[] splitTextIntoWords(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }
        return line.trim().split("\\s+");
    }

    /**
     * Checks if a given word is valid (alphanumeric only).
     *
     * @param word The word to check.
     * @return True if the word is valid, false otherwise.
     */
    public static boolean isValidWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        // ret true if the word contains only letters or digits
        return word.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Maps the content of each file chunk by filtering, splitting, and counting words.
     *
     * @param inputFilePath The path of the file chunk to process.
     * @throws IOException If an error occurs during file I/O.
     */
    public static void map(String inputFilePath) throws IOException {
        // Map phase: Read the input chunk line by line, clean and split into words,
        // then write each valid word with a count of one to a map output file.
        File inputFile = new File(inputFilePath);
        File outputFile = new File(inputFile.getParent(), "map-" + inputFile.getName());

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String filteredLine = filterPunctuations(line);
                String[] words = splitTextIntoWords(filteredLine);
                for (String word : words) {
                    if (isValidWord(word)) {
                        bw.write(word + ":1");
                        bw.newLine();
                    }
                }
            }
        }
    }

    /**
     * Collects word-count pairs from map files.
     *
     * @param mapFiles An array of map file paths.
     * @return A map containing word counts.
     * @throws IOException If an error occurs during file I/O.
     */
    public static Map<String, Integer> collectWordCounts(String[] mapFiles) throws IOException {
        //accumulate the total counts for each word -> HashMap
        Map<String, Integer> wordCounts = new HashMap<>();

        if (mapFiles == null) {
            return wordCounts; //ret empty map if input is null
        }

        for (String mapFilePath : mapFiles) {
            if (mapFilePath == null) {
                continue; // Skip null paths
            }
            File mapFile = new File(mapFilePath);
            if (!mapFile.exists() || !mapFile.isFile()) {
                continue; // skip non-existent or non-file paths
            }

            try (BufferedReader br = new BufferedReader(new FileReader(mapFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue; // skip empty lines
                    }
                    int colonIndex = line.indexOf(':');
                    if (colonIndex == -1) {
                        continue; // skip lines without colon
                    }
                    String word = line.substring(0, colonIndex).trim();
                    String countStr = line.substring(colonIndex + 1).trim();

                    // parse count as integer, skip line if parsing fails
                    int count;
                    try {
                        count = Integer.parseInt(countStr);
                    } catch (NumberFormatException e) {
                        continue; // skip malformed count
                    }

                    // accumulate count for the word
                    wordCounts.put(word, wordCounts.getOrDefault(word, 0) + count);
                }
            }
        }

        return wordCounts;
    }

    /**
     * Reduces the mapped word counts into a final result file.
     *
     * @param mapDirPath     The path of the directory containing map files.
     * @param outputFilePath The path of the final output file.
     * @throws IOException If an error occurs during file I/O.
     */
    public static void reduce(String mapDirPath, String outputFilePath) throws IOException {
        // list map files starting with "map" prefix (case-insensitive)
        File mapDir = new File(mapDirPath);
        File[] mapFiles = mapDir.listFiles((dir, name) -> name.toLowerCase().startsWith("map") && new File(dir, name).isFile());

        //handle null listing gracefully
        if (mapFiles == null) {
            mapFiles = new File[0];
        }

        //build array of absolute paths for map files
        String[] mapFilePaths = new String[mapFiles.length];
        for (int i = 0; i < mapFiles.length; i++) {
            mapFilePaths[i] = mapFiles[i].getAbsolutePath();
        }

        Map<String, Integer> aggregatedCounts = collectWordCounts(mapFilePaths);

        // sort entries by count descending, then word ascending
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(aggregatedCounts.entrySet());
        sortedEntries.sort((e1, e2) -> {
            int cmp = Integer.compare(e2.getValue(), e1.getValue()); // descending count
            if (cmp != 0) {
                return cmp;
            }
            return e1.getKey().compareTo(e2.getKey()); // ascending word
        });

        // write sorted results to output file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }
        }
    }

    /**
     * Sorts the word counts and stores them in the final output file.
     *
     * @param wordCounts     The map of word counts to be sorted and stored.
     * @param outputFilePath The file to store the sorted word counts.
     * @throws IOException If an error occurs during file I/O.
     */
    public static void storeFinalCounts(java.util.Map<String, Integer> wordCounts, String outputFilePath) throws java.io.IOException {
        // handle null input by creating an empty output file
        if (wordCounts == null) {
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(outputFilePath))) {
                // nothing to write
            }
            return;
        }

        //create a list from the map entries
        java.util.List<java.util.Map.Entry<String, Integer>> entries =
                new java.util.ArrayList<>(wordCounts.entrySet());

        // sort by count descending, then word ascending
        entries.sort((e1, e2) -> {
            int cmp = Integer.compare(e2.getValue(), e1.getValue());
            if (cmp != 0) return cmp;
            return e1.getKey().compareTo(e2.getKey());
        });

        // Write sorted entries to output file
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(outputFilePath))) {
            for (java.util.Map.Entry<String, Integer> entry : entries) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException { // update the main function if required
        if (args.length < 2) {
            System.out.println("Usage: <inputFilePath> <outputFilePath>");
            return;
        }
        String inputFilePath = args[0];
        String outputFilePath = args[1];

        // split input file into chunks
        String chunkDirPath = makeChunks(inputFilePath);

        // Map phase: Process each chunk
        File chunkDir = new File(chunkDirPath);
        File[] chunkFiles = chunkDir.listFiles((dir, name) -> name.startsWith("chunk"));

        if (chunkFiles != null) {
            for (File chunkFile : chunkFiles) {
                map(chunkFile.getPath());
            }
        }

        // Reduce phase: Aggregate map results
        reduce(chunkDirPath, outputFilePath);
    }
}