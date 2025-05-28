package ru.hanqnero.uni.lab5.server.database;

import ru.hanqnero.uni.lab5.collection.Coordinates;
import ru.hanqnero.uni.lab5.collection.MusicBand;
import ru.hanqnero.uni.lab5.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.collection.MusicGenre;
import ru.hanqnero.uni.lab5.collection.Studio;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

/**
 * PostgreSQL-based Object-Relational Mapping for MusicBand entities.
 * <p>
 * PostgreSQLMusicBandORM provides database operations for music band data
 * including CRUD operations, complex queries, and data conversion between
 * Java objects and PostgreSQL records. It handles the mapping between
 * the MusicBand domain objects and the relational database structure.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Full CRUD operations for music bands</li>
 *   <li>Studio relationship management</li>
 *   <li>Batch operations for performance</li>
 *   <li>Transaction support</li>
 *   <li>Complex queries with filtering and sorting</li>
 *   <li>Data validation and constraint handling</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 */
public class PostgreSQLMusicBandORM {
    
    private final DatabaseConfig databaseConfig;
    
    public PostgreSQLMusicBandORM(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }
    
    /**
     * Inserts a new music band into the database.
     * 
     * @param band the music band to insert
     * @return true if insertion was successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean insertMusicBand(MusicBand band) throws SQLException {
        String insertSQL = """
            INSERT INTO music_bands (
                id, creation_date, name, number_of_participants, singles_count,
                coordinate_x, coordinate_y, establishment_date, genre, studio_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = databaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                Long studioId = null;
                if (band.getStudio() != null) {
                    studioId = insertOrGetStudio(conn, band.getStudio());
                }
                
                try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                    stmt.setLong(1, band.getId());
                    stmt.setTimestamp(2, Timestamp.valueOf(band.getCreationDate()));
                    stmt.setString(3, band.getName());
                    stmt.setLong(4, band.getNumberOfParticipants());
                    stmt.setInt(5, band.getSinglesCount());
                    stmt.setLong(6, band.getCoordinates().getX());
                    stmt.setInt(7, band.getCoordinates().getY());
                    stmt.setTimestamp(8, Timestamp.from(band.getEstablishmentDate().toInstant()));
                    stmt.setString(9, band.getGenre() != null ? band.getGenre().name() : null);
                    if (studioId != null) {
                        stmt.setLong(10, studioId);
                    } else {
                        stmt.setNull(10, Types.BIGINT);
                    }
                    
                    int rowsAffected = stmt.executeUpdate();
                    conn.commit();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    /**
     * Updates an existing music band in the database.
     * 
     * @param band the music band with updated data
     * @return true if update was successful, false if band not found
     * @throws SQLException if database operation fails
     */
    public boolean updateMusicBand(MusicBand band) throws SQLException {
        String updateSQL = """
            UPDATE music_bands SET
                creation_date = ?, name = ?, number_of_participants = ?, singles_count = ?,
                coordinate_x = ?, coordinate_y = ?, establishment_date = ?, genre = ?, studio_id = ?
            WHERE id = ?
            """;
        
        try (Connection conn = databaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                Long studioId = null;
                if (band.getStudio() != null) {
                    studioId = insertOrGetStudio(conn, band.getStudio());
                }
                
                try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                    stmt.setTimestamp(1, Timestamp.valueOf(band.getCreationDate()));
                    stmt.setString(2, band.getName());
                    stmt.setLong(3, band.getNumberOfParticipants());
                    stmt.setInt(4, band.getSinglesCount());
                    stmt.setLong(5, band.getCoordinates().getX());
                    stmt.setInt(6, band.getCoordinates().getY());
                    stmt.setTimestamp(7, Timestamp.from(band.getEstablishmentDate().toInstant()));
                    stmt.setString(8, band.getGenre() != null ? band.getGenre().name() : null);
                    if (studioId != null) {
                        stmt.setLong(9, studioId);
                    } else {
                        stmt.setNull(9, Types.BIGINT);
                    }
                    stmt.setLong(10, band.getId());
                    
                    int rowsAffected = stmt.executeUpdate();
                    conn.commit();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    /**
     * Deletes a music band from the database by ID.
     * 
     * @param id the ID of the music band to delete
     * @return true if deletion was successful, false if band not found
     * @throws SQLException if database operation fails
     */
    public boolean deleteMusicBand(long id) throws SQLException {
        String deleteSQL = "DELETE FROM music_bands WHERE id = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Finds a music band by ID.
     * 
     * @param id the ID of the music band to find
     * @return Optional containing the music band if found, empty otherwise
     * @throws SQLException if database operation fails
     */
    public Optional<MusicBand> findById(long id) throws SQLException {
        String selectSQL = """
            SELECT mb.id, mb.creation_date, mb.name, mb.number_of_participants, mb.singles_count,
                   mb.coordinate_x, mb.coordinate_y, mb.establishment_date, mb.genre,
                   s.id as studio_id, s.name as studio_name, s.address as studio_address
            FROM music_bands mb
            LEFT JOIN studios s ON mb.studio_id = s.id
            WHERE mb.id = ?
            """;
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createMusicBandFromResultSet(rs));
                }
                return Optional.empty();
            }
        }
    }
    
    /**
     * Retrieves all music bands from the database.
     * 
     * @return TreeSet containing all music bands
     * @throws SQLException if database operation fails
     */
    public TreeSet<MusicBand> findAll() throws SQLException {
        String selectSQL = """
            SELECT mb.id, mb.creation_date, mb.name, mb.number_of_participants, mb.singles_count,
                   mb.coordinate_x, mb.coordinate_y, mb.establishment_date, mb.genre,
                   s.id as studio_id, s.name as studio_name, s.address as studio_address
            FROM music_bands mb
            LEFT JOIN studios s ON mb.studio_id = s.id
            ORDER BY mb.name
            """;
        
        TreeSet<MusicBand> bands = new TreeSet<>();
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bands.add(createMusicBandFromResultSet(rs));
            }
        }
        
        return bands;
    }
    
    /**
     * Deletes all music bands from the database.
     * 
     * @return the number of bands deleted
     * @throws SQLException if database operation fails
     */
    public long deleteAll() throws SQLException {
        String deleteSQL = "DELETE FROM music_bands";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            
            return stmt.executeUpdate();
        }
    }
    
    /**
     * Counts the total number of music bands in the database.
     * 
     * @return the count of music bands
     * @throws SQLException if database operation fails
     */
    public long count() throws SQLException {
        String countSQL = "SELECT COUNT(*) FROM music_bands";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(countSQL);
             ResultSet rs = stmt.executeQuery()) {
            
            rs.next();
            return rs.getLong(1);
        }
    }
    
    /**
     * Batch inserts multiple music bands for better performance.
     * 
     * @param bands the collection of music bands to insert
     * @return the number of bands successfully inserted
     * @throws SQLException if database operation fails
     */
    public long batchInsert(TreeSet<MusicBand> bands) throws SQLException {
        String insertSQL = """
            INSERT INTO music_bands (
                id, creation_date, name, number_of_participants, singles_count,
                coordinate_x, coordinate_y, establishment_date, genre, studio_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = databaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                long insertedCount = 0;
                
                for (MusicBand band : bands) {
                    Long studioId = null;
                    if (band.getStudio() != null) {
                        studioId = insertOrGetStudio(conn, band.getStudio());
                    }
                    
                    stmt.setLong(1, band.getId());
                    stmt.setTimestamp(2, Timestamp.valueOf(band.getCreationDate()));
                    stmt.setString(3, band.getName());
                    stmt.setLong(4, band.getNumberOfParticipants());
                    stmt.setInt(5, band.getSinglesCount());
                    stmt.setLong(6, band.getCoordinates().getX());
                    stmt.setInt(7, band.getCoordinates().getY());
                    stmt.setTimestamp(8, Timestamp.from(band.getEstablishmentDate().toInstant()));
                    stmt.setString(9, band.getGenre() != null ? band.getGenre().name() : null);
                    if (studioId != null) {
                        stmt.setLong(10, studioId);
                    } else {
                        stmt.setNull(10, Types.BIGINT);
                    }
                    
                    stmt.addBatch();
                    insertedCount++;
                    
                    // Execute batch in chunks for better memory management
                    if (insertedCount % 100 == 0) {
                        stmt.executeBatch();
                    }
                }
                
                // Execute remaining batch
                stmt.executeBatch();
                conn.commit();
                return insertedCount;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    /**
     * Finds bands with more participants than the specified number.
     * 
     * @param participantsThreshold the minimum number of participants
     * @return list of music bands matching the criteria
     * @throws SQLException if database operation fails
     */
    public List<MusicBand> findByParticipantsGreaterThan(long participantsThreshold) throws SQLException {
        String selectSQL = """
            SELECT mb.id, mb.creation_date, mb.name, mb.number_of_participants, mb.singles_count,
                   mb.coordinate_x, mb.coordinate_y, mb.establishment_date, mb.genre,
                   s.id as studio_id, s.name as studio_name, s.address as studio_address
            FROM music_bands mb
            LEFT JOIN studios s ON mb.studio_id = s.id
            WHERE mb.number_of_participants > ?
            ORDER BY mb.number_of_participants DESC
            """;
        
        List<MusicBand> bands = new ArrayList<>();
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            
            stmt.setLong(1, participantsThreshold);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bands.add(createMusicBandFromResultSet(rs));
                }
            }
        }
        
        return bands;
    }
    
    /**
     * Deletes bands with more participants than the specified number.
     * 
     * @param participantsThreshold the minimum number of participants
     * @return the number of bands deleted
     * @throws SQLException if database operation fails
     */
    public long deleteByParticipantsGreaterThan(long participantsThreshold) throws SQLException {
        String deleteSQL = "DELETE FROM music_bands WHERE number_of_participants > ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            
            stmt.setLong(1, participantsThreshold);
            return stmt.executeUpdate();
        }
    }
    
    /**
     * Inserts a studio or returns the ID of an existing matching studio.
     */
    private Long insertOrGetStudio(Connection conn, Studio studio) throws SQLException {
        // First try to find existing studio
        String findSQL = """
            SELECT id FROM studios 
            WHERE (name = ? OR (name IS NULL AND ? IS NULL))
            AND (address = ? OR (address IS NULL AND ? IS NULL))
            """;
        
        try (PreparedStatement stmt = conn.prepareStatement(findSQL)) {
            stmt.setString(1, studio.getName());
            stmt.setString(2, studio.getName());
            stmt.setString(3, studio.getAddress());
            stmt.setString(4, studio.getAddress());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        
        // Insert new studio if not found
        String insertSQL = "INSERT INTO studios (name, address) VALUES (?, ?) RETURNING id";
        
        try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setString(1, studio.getName());
            stmt.setString(2, studio.getAddress());
            
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getLong("id");
            }
        }
    }
    
    /**
     * Creates a MusicBand object from a database result set.
     */
    private MusicBand createMusicBandFromResultSet(ResultSet rs) throws SQLException {
        // Extract basic fields
        long id = rs.getLong("id");
        LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        String name = rs.getString("name");
        long numberOfParticipants = rs.getLong("number_of_participants");
        int singlesCount = rs.getInt("singles_count");
        long coordinateX = rs.getLong("coordinate_x");
        int coordinateY = rs.getInt("coordinate_y");
        ZonedDateTime establishmentDate = rs.getTimestamp("establishment_date").toInstant()
                .atZone(java.time.ZoneId.systemDefault());
        
        // Handle optional genre
        String genreStr = rs.getString("genre");
        MusicGenre genre = genreStr != null ? MusicGenre.valueOf(genreStr) : null;
        
        // Handle optional studio
        Studio studio = null;
        long studioId = rs.getLong("studio_id");
        if (!rs.wasNull()) {
            String studioName = rs.getString("studio_name");
            String studioAddress = rs.getString("studio_address");
            studio = new Studio().setName(studioName).setAddress(studioAddress);
        }
        
        // Create the MusicBand object
        MusicBandBuilder builder = new MusicBandBuilder(
                name,
                new Coordinates(coordinateX, coordinateY),
                numberOfParticipants,
                singlesCount,
                establishmentDate
        ).setGenre(genre).setStudio(studio);
        
        MusicBand band = new MusicBand(builder);
        band.setId(id);
        band.setCreationDate(creationDate);
        
        return band;
    }
}
