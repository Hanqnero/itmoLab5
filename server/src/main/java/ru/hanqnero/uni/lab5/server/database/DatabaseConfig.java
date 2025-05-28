package ru.hanqnero.uni.lab5.server.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.hanqnero.uni.lab5.util.exceptions.DataBaseInitializationError;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Configuration and management class for PostgreSQL database connections.
 * <p>
 * DatabaseConfig provides centralized configuration for PostgreSQL database
 * connectivity using HikariCP connection pooling. It manages database
 * connection parameters through environment variables and provides
 * a singleton DataSource for the application.
 * </p>
 * 
 * <p>
 * Required environment variables:
 * <ul>
 *   <li>DB_URL - PostgreSQL JDBC URL (e.g., jdbc:postgresql://localhost:5432/musicbands)</li>
 *   <li>DB_USERNAME - Database username</li>
 *   <li>DB_PASSWORD - Database password</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>HikariCP connection pooling for performance</li>
 *   <li>Environment variable-based configuration</li>
 *   <li>Singleton pattern for DataSource management</li>
 *   <li>Comprehensive error handling and validation</li>
 *   <li>Connection testing and validation</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 */
public class DatabaseConfig {
    
    private static DatabaseConfig instance;
    private final HikariDataSource dataSource;
    
    private DatabaseConfig() {
        try {
            HikariConfig config = new HikariConfig();
            
            // Get database configuration from environment variables
            String dbUrl = System.getenv("DB_URL");
            String dbUsername = System.getenv("DB_USERNAME");
            String dbPassword = System.getenv("DB_PASSWORD");
            
            if (dbUrl == null || dbUsername == null || dbPassword == null) {
                throw new DataBaseInitializationError(
                    "Missing required environment variables: DB_URL, DB_USERNAME, DB_PASSWORD");
            }
            
            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUsername);
            config.setPassword(dbPassword);
            
            // Connection pool configuration
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            
            // Connection validation
            config.setConnectionTestQuery("SELECT 1");
            config.setValidationTimeout(5000);
            
            dataSource = new HikariDataSource(config);
            
            // Test the connection
            try (Connection conn = dataSource.getConnection()) {
                if (!conn.isValid(5)) {
                    throw new DataBaseInitializationError("Database connection validation failed");
                }
            }
            
        } catch (SQLException e) {
            throw new DataBaseInitializationError("Failed to initialize database connection: " + e.getMessage());
        }
    }
    
    /**
     * Gets the singleton instance of DatabaseConfig.
     * 
     * @return the DatabaseConfig instance
     * @throws DataBaseInitializationError if initialization fails
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
    
    /**
     * Gets the configured DataSource for database connections.
     * 
     * @return the HikariDataSource instance
     */
    public DataSource getDataSource() {
        return dataSource;
    }
    
    /**
     * Gets a database connection from the connection pool.
     * 
     * @return a database Connection
     * @throws SQLException if connection cannot be obtained
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    /**
     * Closes the connection pool and releases all resources.
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
