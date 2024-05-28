package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.collection.MusicBand;
import ru.hanqnero.uni.lab5.util.exceptions.DataBaseInitializationError;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TreeSet;
import java.util.UUID;

public class CollectionManager {
    private TreeSet<MusicBand> collection;
    private LocalDateTime creationDate;
    private CSVManager fileManager;

    public void initialize(String EnvVar) {
        creationDate = LocalDateTime.now();
        try {
            fileManager = new CSVManager(EnvVar);
            if (fileManager.checkReadCSVHeader())
                collection = fileManager.restoreFromDatabase();
            else {
                System.out.println("""
                        Warning: File contains some data and not valid csv header.
                        Check before running save.
                        """);
                collection = new TreeSet<>();
            }
        } catch (DataBaseInitializationError e) {
            System.out.printf("""
                    Could not restore collection from file:
                    Error: %s
                    Staring with empty collection""".formatted(e.getMessage()));
            collection = new TreeSet<>();
        }
    }

    public long saveToFile() {
        return fileManager.writeCollection(collection);
    }

    private long generateId() {
        return UUID.randomUUID().getMostSignificantBits();
    }

    public Optional<Long> add(MusicBand band) {
        long id;
        id = generateId();

        if (idInCollection(id))
            return Optional.empty();

        band.setId(id);
        band.setCreationDate(LocalDateTime.now());

        if (!collection.add(band))
            return Optional.empty();
        return Optional.of(id);
    }

    public boolean idInCollection(long id) {
        return collection.stream().anyMatch(e -> e.getId() == id);
    }

    public long size() {
        return collection.size();
    }
    public LocalDateTime getCreationTime() {
        return creationDate;
    }

    public TreeSet<MusicBand> getCollectionCopy() {
        return new TreeSet<>(collection);
    }

    public boolean isMax(MusicBand band) {
        var maxElement = collection.last();
        return band.compareTo(maxElement) > 0;
    }

    public boolean isMin(MusicBand band) {
        var minElement = collection.first();
        return band.compareTo(minElement) < 0;
    }

}
