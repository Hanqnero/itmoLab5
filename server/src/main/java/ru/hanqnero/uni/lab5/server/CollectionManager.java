package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.collection.MusicBand;
import ru.hanqnero.uni.lab5.collection.Studio;
import ru.hanqnero.uni.lab5.server.database.HybridPersistenceManager;
import ru.hanqnero.uni.lab5.util.exceptions.DataBaseInitializationError;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Manages the music band collection with hybrid persistence and operations.
 * 
 * <p>This class provides comprehensive management of a TreeSet-based collection
 * of MusicBand objects, including initialization from persistent storage,
 * CRUD operations, querying, and data persistence via both CSV files and
 * PostgreSQL database for enhanced reliability and performance.</p>
 * 
 * <p>The collection is automatically sorted using the natural ordering of
 * MusicBand objects. The class supports various operations like adding,
 * removing, updating, and querying bands based on different criteria.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Hybrid persistence (CSV + PostgreSQL)</li>
 *   <li>Collection initialization from environment variables</li>
 *   <li>CRUD operations with validation and database synchronization</li>
 *   <li>Query operations (min/max by various criteria)</li>
 *   <li>Batch operations and filtering</li>
 *   <li>Real-time database operations for individual changes</li>
 * </ul>
 * 
 * @author hanqnero
 * @version 1.0
 */
public class CollectionManager {
    private TreeSet<MusicBand> collection;
    private LocalDateTime creationDate;
    private HybridPersistenceManager persistenceManager;

    /**
     * Initializes the collection from persistent storage with hybrid support.
     * 
     * <p>Attempts to restore the collection from configured storage (CSV, PostgreSQL, or both)
     * specified by the given environment variable. The persistence mode is determined by
     * additional environment variables or falls back to CSV-only mode.</p>
     * 
     * <p>Supported persistence modes:</p>
     * <ul>
     *   <li>CSV_ONLY - Traditional CSV file storage</li>
     *   <li>POSTGRES_ONLY - PostgreSQL database only</li>
     *   <li>BOTH - Synchronize between CSV and PostgreSQL</li>
     *   <li>POSTGRES_WITH_CSV_BACKUP - PostgreSQL primary with CSV backup</li>
     * </ul>
     * 
     * <p>The creation date is set to the current time when this method
     * is called, regardless of whether restoration succeeds.</p>
     * 
     * @param EnvVar name of the environment variable containing the CSV file path
     */
    public void initialize(String EnvVar) {
        creationDate = LocalDateTime.now();
        
        // Determine persistence mode from environment or use default
        String modeStr = System.getenv("PERSISTENCE_MODE");
        HybridPersistenceManager.PersistenceMode mode = HybridPersistenceManager.PersistenceMode.POSTGRES_WITH_CSV_BACKUP;
        
        if (modeStr != null) {
            try {
                mode = HybridPersistenceManager.PersistenceMode.valueOf(modeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid persistence mode '" + modeStr + "', using default: " + mode);
            }
        }
        
        System.out.println("Initializing persistence with mode: " + mode);
        
        try {
            persistenceManager = new HybridPersistenceManager(EnvVar, mode);
            collection = persistenceManager.restoreFromDatabase();
            
            if (collection.isEmpty()) {
                System.out.println("Collection initialized as empty");
            } else {
                System.out.println("Collection initialized with " + collection.size() + " bands");
            }
            
        } catch (DataBaseInitializationError e) {
            System.out.printf("""
                    Could not restore collection from storage:
                    Error: %s
                    Starting with empty collection%n""", e.getMessage());
            collection = new TreeSet<>();
            
            // Try to create a minimal persistence manager for CSV-only fallback
            try {
                persistenceManager = new HybridPersistenceManager(EnvVar, HybridPersistenceManager.PersistenceMode.CSV_ONLY);
            } catch (Exception fallbackError) {
                System.err.println("Even CSV fallback failed: " + fallbackError.getMessage());
                persistenceManager = null;
            }
        }
    }

    /**
     * Saves the current collection to persistent storage.
     * 
     * @return the number of elements written to storage
     */
    public long saveToFile() {
        if (persistenceManager == null) {
            System.err.println("No persistence manager available");
            return 0;
        }
        return persistenceManager.saveCollection(collection);
    }

    /**
     * Generates a unique ID for a new music band.
     * 
     * @return a unique long identifier
     */
    private long generateId() {
        return UUID.randomUUID().getMostSignificantBits();
    }

    /**
     * Adds a new music band to the collection and persistent storage.
     * 
     * @param band the music band to add
     * @return Optional containing the generated ID if successful, empty if failed
     */
    public Optional<Long> add(MusicBand band) {
        long id = generateId();

        if (idInCollection(id).isPresent())
            return Optional.empty();

        band.setId(id);
        band.setCreationDate(LocalDateTime.now());

        if (!collection.add(band))
            return Optional.empty();
            
        // Save to persistent storage
        if (persistenceManager != null) {
            try {
                persistenceManager.saveBand(band);
            } catch (Exception e) {
                // Rollback in-memory change if database save fails
                collection.remove(band);
                System.err.println("Failed to save band to persistent storage: " + e.getMessage());
                return Optional.empty();
            }
        }
        
        return Optional.of(id);
    }

    /**
     * Updates an existing element in the collection and persistent storage.
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
            // Rollback - add the old element back
            collection.add(old.get());
            return Optional.empty();
        }
        
        // Update in persistent storage
        if (persistenceManager != null) {
            try {
                persistenceManager.updateBand(nev);
            } catch (Exception e) {
                // Rollback in-memory changes
                collection.remove(nev);
                collection.add(old.get());
                System.err.println("Failed to update band in persistent storage: " + e.getMessage());
                return Optional.empty();
            }
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

    /**
     * Clears the entire collection from memory and persistent storage.
     */
    public void clear() {
        collection.clear();
        
        // Clear from persistent storage
        if (persistenceManager != null) {
            try {
                persistenceManager.clearAllBands();
            } catch (Exception e) {
                System.err.println("Failed to clear persistent storage: " + e.getMessage());
            }
        }
    }

    /**
     * Removes a music band by ID from both collection and persistent storage.
     * @param id the ID of the band to remove
     * @return 1 if removed, 0 if not found
     */
    public long removeById(long id) {
        Optional<MusicBand> bandToRemove = idInCollection(id);
        if (bandToRemove.isEmpty()) {
            return 0L;
        }
        
        boolean removed = collection.removeIf(e -> e.getId().equals(id));
        if (!removed) {
            return 0L;
        }
        
        // Remove from persistent storage
        if (persistenceManager != null) {
            try {
                persistenceManager.deleteBand(id);
            } catch (Exception e) {
                // Rollback - add the band back to collection
                collection.add(bandToRemove.get());
                System.err.println("Failed to remove band from persistent storage: " + e.getMessage());
                return 0L;
            }
        }
        
        return 1L;
    }
    
    /**
     * Removes all music bands with the specified studio from both collection and persistent storage.
     * @param s the studio to match
     * @return the number of bands removed
     */
    public long removeByStudio(Studio s) {
        return removeIf(band -> band.getStudio().equals(s));

//        Collection<MusicBand> match = collection.stream()
//            .filter(e -> Objects.equals(s, e.getStudio()))
//            .collect(Collectors.toUnmodifiableSet());
//
//        if (match.isEmpty()) {
//            return 0L;
//        }
//
//        // Remove from collection
//        boolean removed = collection.removeAll(match);
//        if (!removed) {
//            return 0L;
//        }
//
//        // Remove from persistent storage
//        if (persistenceManager != null) {
//            try {
//                for (MusicBand band : match) {
//                    persistenceManager.deleteBand(band.getId());
//                }
//            } catch (Exception e) {
//                // Rollback - add all bands back to collection
//                collection.addAll(match);
//                System.err.println("Failed to remove bands from persistent storage: " + e.getMessage());
//                return 0L;
//            }
//        }
//
//        return match.size();
    }

    /**
     * Removes all music bands matching the given predicate from both collection and persistent storage.
     * @param predicate the condition to match bands for removal
     * @return the number of bands removed
     */
    public long removeIf(Predicate<MusicBand> predicate) {
        Collection<MusicBand> match = collection.stream()
            .filter(predicate)
            .collect(Collectors.toUnmodifiableSet());
            
        if (match.isEmpty()) {
            return 0L;
        }
        
        // Remove from collection
        boolean removed = collection.removeAll(match);
        if (!removed) {
            return 0L;
        }
        
        // Remove from persistent storage
        if (persistenceManager != null) {
            try {
                for (MusicBand band : match) {
                    persistenceManager.deleteBand(band.getId());
                }
            } catch (Exception e) {
                // Rollback - add all bands back to collection
                collection.addAll(match);
                System.err.println("Failed to remove bands from persistent storage: " + e.getMessage());
                return 0L;
            }
        }
        
        return match.size();
    }

    public Optional<MusicBand> min(Comparator<MusicBand> comparator) {
        return collection.stream().min(comparator);
    }

}
