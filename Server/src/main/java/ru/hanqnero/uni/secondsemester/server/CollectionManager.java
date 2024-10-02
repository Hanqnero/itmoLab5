package ru.hanqnero.uni.secondsemester.server;

import ru.hanqnero.uni.secondsemester.commons.collection.MusicBand;
import ru.hanqnero.uni.secondsemester.commons.collection.Studio;
import ru.hanqnero.uni.secondsemester.commons.exceptions.DataBaseInitializationError;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

        if (idInCollection(id).isPresent())
            return Optional.empty();

        band.setId(id);
        band.setCreationDate(LocalDateTime.now());

        if (!collection.add(band))
            return Optional.empty();
        return Optional.of(id);
    }

    /**
     * Try to update existing element in collection.
     * @param id id of element to be updated.
     * @param nev new element.
     * @return Empty optional if update failed, Optional of false if id is not present inside collection, true Optional otherwise.
     */
    public Optional<Boolean> update(long id, MusicBand nev) {
        var old = idInCollection(id);
        if (old.isEmpty()) return Optional.of(false);

        nev.setId(id);
        nev.setCreationDate(old.get().getCreationDate());

        collection.remove(old.get());
        var b = collection.add(nev);
        if (!b) {
            return Optional.empty();
        }
        return Optional.of(true);
    }

    public Optional<MusicBand> idInCollection(long id) {
        return collection.stream().filter(e -> e.getId() == id).findFirst();
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

    public void clear() {
        collection.clear();
    }

    public long remove(long id) {
        return collection.removeIf(e -> e.getId().equals(id)) ? 1L : 0L;
    }
    public long remove(Studio s) {
        Collection<MusicBand> match;
        match = collection.stream().filter(e -> Objects.equals(s, e.getStudio())).collect(Collectors.toUnmodifiableSet());
        return collection.removeAll(match) ? match.size() : 0L;
    }

    public long removeIf(Predicate<MusicBand> predicate) {
        Collection<MusicBand> match;
        match = collection.stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
        return collection.removeAll(match) ? match.size() : 0L;
    }

    public Optional<MusicBand> min(Comparator<MusicBand> comparator) {
        return collection.stream().min(comparator);
    }

}
