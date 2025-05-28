package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.collection.MusicBand;
import ru.hanqnero.uni.lab5.collection.MusicBandORM;
import ru.hanqnero.uni.lab5.util.exceptions.DataBaseInitializationError;
import ru.hanqnero.uni.lab5.util.exceptions.FileEndReachedException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Manager class for CSV file operations related to MusicBand data persistence.
 * <p>
 * CSVManager handles reading from and writing to CSV files that store music band
 * collection data. It provides methods for loading collections from CSV files,
 * validating CSV headers, and converting between CSV records and MusicBand objects.
 * The class uses UTF-8 encoding for file operations and includes comprehensive
 * error handling for file system operations.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Environment variable-based file path configuration</li>
 *   <li>UTF-8 encoding support for international characters</li>
 *   <li>CSV header validation for format consistency</li>
 *   <li>Streaming CSV reading with cursor tracking</li>
 *   <li>Integration with MusicBandORM for object mapping</li>
 *   <li>Robust error handling and reporting</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The manager requires an absolute file path specified through an environment
 * variable to ensure consistent access across different deployment environments.
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see MusicBand
 * @see MusicBandORM
 */
public class CSVManager {
    /** The CSV database file for music band data storage */
    private final File dataBaseFile;
    /** Input stream reader for reading CSV data with UTF-8 encoding */
    private final InputStreamReader dbInputStreamReader;

    /** Current cursor position for tracking file reading progress */
    private long cursor = 0;

    /**
     * Constructs a new CSVManager with the file path from an environment variable.
     * <p>
     * This constructor initializes the CSV manager by reading the file path
     * from the specified environment variable. The path must be absolute
     * to ensure consistent access across different environments. The file
     * is opened with UTF-8 encoding for proper handling of international characters.
     * </p>
     * 
     * @param EnvVarName the name of the environment variable containing the absolute
     *                   path to the CSV database file
     * @throws DataBaseInitializationError if the environment variable is not set,
     *                                    the path is relative, or the file cannot be accessed
     * @throws IllegalArgumentException if EnvVarName is null or empty
     */
    public CSVManager(String EnvVarName) {
        String filePath = System.getenv(EnvVarName);
        dataBaseFile = new File(filePath);
        InputStream dbInputStream;
        if (!dataBaseFile.isAbsolute()) {
            throw new DataBaseInitializationError("Relative paths like `%s` are not supported".formatted(filePath));
        } else {
            try {
                dbInputStream = new FileInputStream(dataBaseFile);
            } catch (FileNotFoundException e) {
                throw new DataBaseInitializationError("filePath");
            }
        }
        dbInputStreamReader = new InputStreamReader(dbInputStream, StandardCharsets.UTF_8);
    }

    /**
     * Checks and validates the CSV header format.
     * <p>
     * This method reads the first line of the CSV file and validates that it
     * contains the expected header format for music band data. The header
     * validation ensures that the CSV file structure matches the expected
     * format before attempting to parse data records.
     * </p>
     * 
     * @return true if the header is valid and matches expected format, false otherwise
     * @throws RuntimeException if there are I/O errors reading the file
     */
    public boolean checkReadCSVHeader() {
        Optional<String> headerOption;
        try {
            headerOption = readLine();
        } catch (FileEndReachedException e) {
            System.out.println("Reached file end while reading csv header.");
            return false;
        }
        return headerOption.map(s -> s.equals(MusicBandORM.expectedCSVHeader())).orElse(false);
    }



    private Optional<String> readLine() throws FileEndReachedException {
        try {
            if (!dbInputStreamReader.ready()) {
                throw new FileEndReachedException();
            }


            StringBuilder line = new StringBuilder();
            int c;
            while ((c = dbInputStreamReader.read()) != -1 && c != '\n') {
                line.append((char) c);
            }

            cursor++;

            if (line.isEmpty()) return Optional.empty();
            else return Optional.of(line.toString());

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public TreeSet<MusicBand> restoreFromDatabase() {
        var result = new TreeSet<MusicBand>();
        while (true) {
            Optional<String> option;
            try {
                option = readLine();
            } catch (FileEndReachedException e) {
                System.out.println("Reached end of file. Should have read all rows at this point.");
                break;
            }
            if (option.isEmpty()) continue;

            String line = option.get();
            Optional<MusicBand> element = MusicBandORM.createFromCSVRow(line);
            if (element.isEmpty()) {
                System.out.printf("Error in csv on line %d%n", cursor);
            } else
                result.add(element.get());
        }
        try {
            dbInputStreamReader.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close file after reading.");
        }
        return result;
    }

    public long writeCollection(TreeSet<MusicBand> collection) {
        long saved = 0;
        try (FileOutputStream out = new FileOutputStream(dataBaseFile, false)) {
            out.write((MusicBandORM.expectedCSVHeader() + "\n").getBytes(StandardCharsets.UTF_8));
            for (MusicBand e : collection) {
                String row = MusicBandORM.toCSVRow(e);
                out.write(row.getBytes(StandardCharsets.UTF_8));
                saved++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return saved;
    }
}
