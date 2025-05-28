package ru.hanqnero.uni.lab5.server.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database schema initialization and management utility.
 * <p>
 * DatabaseSchemaManager handles the creation and management of database
 * tables required for the music band collection system. It provides
 * methods to initialize the database schema, create tables with proper
 * constraints, and ensure database structure consistency.
 * </p>
 * 
 * <p>
 * The schema includes:
 * <ul>
 *   <li>studios table - for recording studio information</li>
 *   <li>music_bands table - for music band data with foreign key to studios</li>
 *   <li>Proper indexes for performance optimization</li>
 *   <li>Check constraints for data validation</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 */
public class DatabaseSchemaManager {
    
    private final DatabaseConfig databaseConfig;
    
    public DatabaseSchemaManager(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }
    
    /**
     * Initializes the complete database schema.
     * <p>
     * This method creates all necessary tables, indexes, and constraints
     * required for the music band collection system. It's safe to call
     * multiple times as it uses CREATE TABLE IF NOT EXISTS.
     * </p>
     * 
     * @throws SQLException if schema creation fails
     */
    public void initializeSchema() throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create studios table first (referenced by music_bands)
            createStudiosTable(stmt);
            
            // Create music_bands table with foreign key to studios
            createMusicBandsTable(stmt);
            
            // Create indexes for performance
            createIndexes(stmt);
            
            System.out.println("Database schema initialized successfully");
        }
    }
    
    /**
     * Creates the studios table for recording studio information.
     */
    private void createStudiosTable(Statement stmt) throws SQLException {
        String createStudiosSQL = """
            CREATE TABLE IF NOT EXISTS studios (
                id BIGSERIAL PRIMARY KEY,
                name VARCHAR(255),
                address VARCHAR(500),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                
                -- Ensure at least one of name or address is provided
                CONSTRAINT studios_not_empty CHECK (
                    name IS NOT NULL OR address IS NOT NULL
                )
            )
            """;
        
        stmt.executeUpdate(createStudiosSQL);
        System.out.println("Studios table created/verified");
    }
    
    /**
     * Creates the music_bands table for music band data.
     */
    private void createMusicBandsTable(Statement stmt) throws SQLException {
        String createMusicBandsSQL = """
            CREATE TABLE IF NOT EXISTS music_bands (
                id BIGINT PRIMARY KEY,
                creation_date TIMESTAMP NOT NULL,
                name VARCHAR(255) NOT NULL,
                number_of_participants BIGINT NOT NULL,
                singles_count INTEGER NOT NULL,
                coordinate_x BIGINT NOT NULL,
                coordinate_y INTEGER NOT NULL,
                establishment_date TIMESTAMP WITH TIME ZONE NOT NULL,
                genre VARCHAR(50),
                studio_id BIGINT,
                
                -- Foreign key constraint
                CONSTRAINT fk_studio FOREIGN KEY (studio_id) REFERENCES studios(id)
                    ON DELETE SET NULL ON UPDATE CASCADE,
                
                -- Check constraints for data validation
                CONSTRAINT check_participants_positive CHECK (number_of_participants > 0),
                CONSTRAINT check_singles_positive CHECK (singles_count > 0),
                CONSTRAINT check_name_not_empty CHECK (name IS NOT NULL AND LENGTH(TRIM(name)) > 0),
                
                -- Genre validation (enum-like constraint)
                CONSTRAINT check_genre_valid CHECK (
                    genre IS NULL OR 
                    genre IN ('PROGRESSIVE_ROCK', 'PSYCHEDELIC_ROCK', 'POST_ROCK', 'PUNK_ROCK')
                )
            )
            """;
        
        stmt.executeUpdate(createMusicBandsSQL);
        System.out.println("Music bands table created/verified");
    }
    
    /**
     * Creates indexes for query performance optimization.
     */
    private void createIndexes(Statement stmt) throws SQLException {
        String[] indexStatements = {
            "CREATE INDEX IF NOT EXISTS idx_music_bands_name ON music_bands(name)",
            "CREATE INDEX IF NOT EXISTS idx_music_bands_creation_date ON music_bands(creation_date)",
            "CREATE INDEX IF NOT EXISTS idx_music_bands_establishment_date ON music_bands(establishment_date)",
            "CREATE INDEX IF NOT EXISTS idx_music_bands_participants ON music_bands(number_of_participants)",
            "CREATE INDEX IF NOT EXISTS idx_music_bands_genre ON music_bands(genre)",
            "CREATE INDEX IF NOT EXISTS idx_studios_name ON studios(name)"
        };
        
        for (String indexSQL : indexStatements) {
            stmt.executeUpdate(indexSQL);
        }
        
        System.out.println("Database indexes created/verified");
    }
    
    /**
     * Drops all tables (use with caution - for testing/cleanup only).
     * 
     * @throws SQLException if table dropping fails
     */
    public void dropAllTables() throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("DROP TABLE IF EXISTS music_bands CASCADE");
            stmt.executeUpdate("DROP TABLE IF EXISTS studios CASCADE");
            
            System.out.println("All tables dropped");
        }
    }
}
