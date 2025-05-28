package ru.hanqnero.uni.lab5.server.database;

import ru.hanqnero.uni.lab5.collection.MusicBand;
import ru.hanqnero.uni.lab5.server.CSVManager;
import ru.hanqnero.uni.lab5.util.exceptions.DataBaseInitializationError;

import java.sql.SQLException;
import java.util.TreeSet;

/**
 * Hybrid persistence manager that handles both CSV and PostgreSQL operations.
 * <p>
 * HybridPersistenceManager provides a unified interface for data persistence
 * that supports both CSV file-based storage (for backward compatibility)
 * and PostgreSQL database storage (for enhanced functionality). It can
 * operate in different modes and synchronize data between both storage
 * mechanisms.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Dual persistence support (CSV + PostgreSQL)</li>
 *   <li>Data synchronization between storage types</li>
 *   <li>Fallback mechanisms for reliability</li>
 *   <li>Migration support from CSV to PostgreSQL</li>
 *   <li>Configurable persistence strategy</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Persistence modes:
 * <ul>
 *   <li>CSV_ONLY - Uses only CSV file storage</li>
 *   <li>POSTGRES_ONLY - Uses only PostgreSQL storage</li>
 *   <li>BOTH - Uses both storages with synchronization</li>
 *   <li>POSTGRES_WITH_CSV_BACKUP - Primary PostgreSQL with CSV backup</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 */
public class HybridPersistenceManager {
    
    public enum PersistenceMode {
        CSV_ONLY,
        POSTGRES_ONLY,
        BOTH,
        POSTGRES_WITH_CSV_BACKUP
    }
    
    private final CSVManager csvManager;
    private final PostgreSQLMusicBandORM postgresORM;
    private final DatabaseConfig databaseConfig;
    private final PersistenceMode mode;
    private final boolean postgresAvailable;
    
    /**
     * Constructs a HybridPersistenceManager with the specified configuration.
     * 
     * @param csvEnvVar environment variable for CSV file path
     * @param mode the persistence mode to use
     */
    public HybridPersistenceManager(String csvEnvVar, PersistenceMode mode) {
        this.mode = mode;
        
        // Initialize CSV manager
        this.csvManager = new CSVManager(csvEnvVar);
        
        // Initialize PostgreSQL components
        DatabaseConfig dbConfig = null;
        PostgreSQLMusicBandORM postgres = null;
        boolean pgAvailable = false;
        
        if (mode != PersistenceMode.CSV_ONLY) {
            try {
                dbConfig = DatabaseConfig.getInstance();
                postgres = new PostgreSQLMusicBandORM(dbConfig);
                
                // Initialize database schema
                DatabaseSchemaManager schemaManager = new DatabaseSchemaManager(dbConfig);
                schemaManager.initializeSchema();
                
                pgAvailable = true;
                System.out.println("PostgreSQL connection established successfully");
            } catch (Exception e) {
                System.err.println("Failed to initialize PostgreSQL: " + e.getMessage());
                if (mode == PersistenceMode.POSTGRES_ONLY) {
                    throw new DataBaseInitializationError("PostgreSQL required but not available: " + e.getMessage());
                }
                System.out.println("Falling back to CSV-only mode");
            }
        }
        
        this.databaseConfig = dbConfig;
        this.postgresORM = postgres;
        this.postgresAvailable = pgAvailable;
    }
    
    /**
     * Restores the music band collection from the configured storage.
     * 
     * @return TreeSet containing all music bands
     */
    public TreeSet<MusicBand> restoreFromDatabase() {
        switch (mode) {
            case CSV_ONLY:
                return restoreFromCSV();
                
            case POSTGRES_ONLY:
                return restoreFromPostgreSQL();
                
            case BOTH:
            case POSTGRES_WITH_CSV_BACKUP:
                return restoreFromBoth();
                
            default:
                throw new IllegalStateException("Unknown persistence mode: " + mode);
        }
    }
    
    /**
     * Saves the music band collection to the configured storage.
     * 
     * @param collection the collection to save
     * @return the number of records saved
     */
    public long writeCollection(TreeSet<MusicBand> collection) {
        switch (mode) {
            case CSV_ONLY:
                return csvManager.writeCollection(collection);
                
            case POSTGRES_ONLY:
                return writeToPostgreSQL(collection);
                
            case BOTH:
                return writeToBoth(collection);
                
            case POSTGRES_WITH_CSV_BACKUP:
                return writeToPostgreSQLWithCSVBackup(collection);
                
            default:
                throw new IllegalStateException("Unknown persistence mode: " + mode);
        }
    }
    
    /**
     * Adds a single music band to the storage.
     * 
     * @param band the music band to add
     * @return true if successfully added, false otherwise
     */
    public boolean addMusicBand(MusicBand band) {
        boolean success = true;
        
        if (shouldUsePostgreSQL()) {
            try {
                success &= postgresORM.insertMusicBand(band);
            } catch (SQLException e) {
                System.err.println("Failed to add band to PostgreSQL: " + e.getMessage());
                success = false;
            }
        }
        
        // Note: For CSV, we typically rebuild the entire file, so individual adds
        // are not supported in the current CSV implementation
        
        return success;
    }
    
    /**
     * Updates a music band in the storage.
     * 
     * @param band the music band with updated data
     * @return true if successfully updated, false otherwise
     */
    public boolean updateMusicBand(MusicBand band) {
        boolean success = true;
        
        if (shouldUsePostgreSQL()) {
            try {
                success &= postgresORM.updateMusicBand(band);
            } catch (SQLException e) {
                System.err.println("Failed to update band in PostgreSQL: " + e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Deletes a music band from the storage.
     * 
     * @param id the ID of the music band to delete
     * @return true if successfully deleted, false otherwise
     */
    public boolean deleteMusicBand(long id) {
        boolean success = true;
        
        if (shouldUsePostgreSQL()) {
            try {
                success &= postgresORM.deleteMusicBand(id);
            } catch (SQLException e) {
                System.err.println("Failed to delete band from PostgreSQL: " + e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Gets the count of music bands in the storage.
     * 
     * @return the number of music bands
     */
    public long getCount() {
        if (shouldUsePostgreSQL()) {
            try {
                return postgresORM.count();
            } catch (SQLException e) {
                System.err.println("Failed to get count from PostgreSQL: " + e.getMessage());
            }
        }
        
        // Fallback to CSV or return 0
        return 0;
    }
    
    /**
     * Migrates data from CSV to PostgreSQL.
     * 
     * @return the number of records migrated
     */
    public long migrateCSVToPostgreSQL() {
        if (!postgresAvailable) {
            throw new IllegalStateException("PostgreSQL not available for migration");
        }
        
        try {
            System.out.println("Starting migration from CSV to PostgreSQL...");
            
            // Load data from CSV
            TreeSet<MusicBand> csvData = csvManager.restoreFromDatabase();
            System.out.println("Loaded " + csvData.size() + " records from CSV");
            
            // Clear PostgreSQL tables
            postgresORM.deleteAll();
            System.out.println("Cleared existing PostgreSQL data");
            
            // Batch insert to PostgreSQL
            long migrated = postgresORM.batchInsert(csvData);
            System.out.println("Migrated " + migrated + " records to PostgreSQL");
            
            return migrated;
        } catch (SQLException e) {
            throw new RuntimeException("Migration failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Synchronizes data between CSV and PostgreSQL storages.
     */
    public void synchronizeStorages() {
        if (!postgresAvailable) {
            System.out.println("PostgreSQL not available for synchronization");
            return;
        }
        
        try {
            System.out.println("Synchronizing storages...");
            
            // Get data from PostgreSQL (considered the source of truth)
            TreeSet<MusicBand> postgresData = postgresORM.findAll();
            
            // Write to CSV as backup
            long csvWritten = csvManager.writeCollection(postgresData);
            
            System.out.println("Synchronized " + csvWritten + " records to CSV backup");
        } catch (SQLException e) {
            System.err.println("Failed to synchronize storages: " + e.getMessage());
        }
    }
    
    private TreeSet<MusicBand> restoreFromCSV() {
        System.out.println("Restoring data from CSV...");
        return csvManager.restoreFromDatabase();
    }
    
    private TreeSet<MusicBand> restoreFromPostgreSQL() {
        if (!postgresAvailable) {
            throw new DataBaseInitializationError("PostgreSQL not available");
        }
        
        try {
            System.out.println("Restoring data from PostgreSQL...");
            return postgresORM.findAll();
        } catch (SQLException e) {
            throw new DataBaseInitializationError("Failed to restore from PostgreSQL: " + e.getMessage());
        }
    }
    
    private TreeSet<MusicBand> restoreFromBoth() {
        TreeSet<MusicBand> result = new TreeSet<>();
        
        if (postgresAvailable) {
            try {
                result = postgresORM.findAll();
                System.out.println("Primary data loaded from PostgreSQL: " + result.size() + " records");
            } catch (SQLException e) {
                System.err.println("Failed to load from PostgreSQL, trying CSV: " + e.getMessage());
                result = csvManager.restoreFromDatabase();
                System.out.println("Fallback data loaded from CSV: " + result.size() + " records");
            }
        } else {
            result = csvManager.restoreFromDatabase();
            System.out.println("Data loaded from CSV: " + result.size() + " records");
        }
        
        return result;
    }
    
    private long writeToPostgreSQL(TreeSet<MusicBand> collection) {
        if (!postgresAvailable) {
            throw new DataBaseInitializationError("PostgreSQL not available");
        }
        
        try {
            // Clear existing data and insert new data
            postgresORM.deleteAll();
            return postgresORM.batchInsert(collection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to write to PostgreSQL: " + e.getMessage(), e);
        }
    }
    
    private long writeToBoth(TreeSet<MusicBand> collection) {
        long csvWritten = csvManager.writeCollection(collection);
        
        if (postgresAvailable) {
            try {
                long pgWritten = writeToPostgreSQL(collection);
                System.out.println("Data written to both storages - CSV: " + csvWritten + ", PostgreSQL: " + pgWritten);
                return Math.max(csvWritten, pgWritten);
            } catch (Exception e) {
                System.err.println("Failed to write to PostgreSQL, CSV written successfully: " + e.getMessage());
            }
        }
        
        return csvWritten;
    }
    
    private long writeToPostgreSQLWithCSVBackup(TreeSet<MusicBand> collection) {
        long written = 0;
        
        if (postgresAvailable) {
            try {
                written = writeToPostgreSQL(collection);
                System.out.println("Data written to PostgreSQL: " + written + " records");
                
                // Create CSV backup
                long csvBackup = csvManager.writeCollection(collection);
                System.out.println("CSV backup created: " + csvBackup + " records");
            } catch (Exception e) {
                System.err.println("PostgreSQL write failed, falling back to CSV: " + e.getMessage());
                written = csvManager.writeCollection(collection);
            }
        } else {
            written = csvManager.writeCollection(collection);
        }
        
        return written;
    }
    
    private boolean shouldUsePostgreSQL() {
        return postgresAvailable && (mode == PersistenceMode.POSTGRES_ONLY || 
                                   mode == PersistenceMode.BOTH || 
                                   mode == PersistenceMode.POSTGRES_WITH_CSV_BACKUP);
    }
    
    /**
     * Gets the PostgreSQL ORM instance if available.
     * 
     * @return the PostgreSQL ORM instance or null if not available
     */
    public PostgreSQLMusicBandORM getPostgresORM() {
        return postgresORM;
    }
    
    /**
     * Checks if PostgreSQL is available and operational.
     * 
     * @return true if PostgreSQL is available, false otherwise
     */
    public boolean isPostgreSQLAvailable() {
        return postgresAvailable;
    }
    
    /**
     * Gets the current persistence mode.
     * 
     * @return the current persistence mode
     */
    public PersistenceMode getMode() {
        return mode;
    }
    
    // Convenience methods for CollectionManager compatibility
    
    /**
     * Saves a single music band to storage. 
     * Alias for addMusicBand for compatibility with CollectionManager.
     * 
     * @param band the music band to save
     * @throws SQLException if database operation fails
     */
    public void saveBand(MusicBand band) throws SQLException {
        if (!addMusicBand(band)) {
            throw new SQLException("Failed to save music band to storage");
        }
    }
    
    /**
     * Updates a single music band in storage.
     * Alias for updateMusicBand for compatibility with CollectionManager.
     * 
     * @param band the music band to update
     * @throws SQLException if database operation fails
     */
    public void updateBand(MusicBand band) throws SQLException {
        if (!updateMusicBand(band)) {
            throw new SQLException("Failed to update music band in storage");
        }
    }
    
    /**
     * Deletes a single music band from storage.
     * Alias for deleteMusicBand for compatibility with CollectionManager.
     * 
     * @param id the ID of the music band to delete
     * @throws SQLException if database operation fails
     */
    public void deleteBand(long id) throws SQLException {
        if (!deleteMusicBand(id)) {
            throw new SQLException("Failed to delete music band from storage");
        }
    }
    
    /**
     * Clears all music bands from storage.
     * 
     * @throws SQLException if database operation fails
     */
    public void clearAllBands() throws SQLException {
        if (shouldUsePostgreSQL()) {
            try {
                postgresORM.deleteAll();
            } catch (SQLException e) {
                throw new SQLException("Failed to clear all bands from PostgreSQL: " + e.getMessage(), e);
            }
        }
        
        // For CSV modes, we write an empty collection
        if (mode == PersistenceMode.CSV_ONLY || mode == PersistenceMode.BOTH || 
            mode == PersistenceMode.POSTGRES_WITH_CSV_BACKUP) {
            csvManager.writeCollection(new TreeSet<>());
        }
    }
    
    /**
     * Saves the entire collection to storage.
     * Alias for writeCollection for compatibility with CollectionManager.
     * 
     * @param collection the collection to save
     * @return the number of records saved
     */
    public long saveCollection(TreeSet<MusicBand> collection) {
        return writeCollection(collection);
    }
    
    /**
     * Closes all database connections and resources.
     */
    public void close() {
        if (databaseConfig != null) {
            databaseConfig.close();
        }
    }
}
