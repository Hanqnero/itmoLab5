package ru.hanqnero.uni.secondsemester.server;

import ru.hanqnero.uni.secondsemester.commons.collection.MusicBand;
import ru.hanqnero.uni.secondsemester.commons.collection.MusicBandORM;
import ru.hanqnero.uni.secondsemester.commons.exceptions.DataBaseInitializationError;
import ru.hanqnero.uni.secondsemester.server.exceptions.FileEndReachedException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.TreeSet;

public class CSVManager {
    private final File dataBaseFile;
    private final InputStreamReader dbInputStreamReader;

    private long cursor = 0;

    /**
     * @param EnvVarName Name of environment variable storing absolute path to csv database
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
