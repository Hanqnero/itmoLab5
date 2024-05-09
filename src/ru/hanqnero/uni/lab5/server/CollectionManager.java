package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.collection.MusicBand;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TreeSet;
import java.util.UUID;

public class CollectionManager {
    private final TreeSet<MusicBand> collection;
    private final LocalDateTime creationDate;
    public CollectionManager() {
        collection = new TreeSet<>();
        creationDate = LocalDateTime.now();
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

        collection.add(band);
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

}
