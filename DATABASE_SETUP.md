# Database Setup Guide

This guide explains how to set up PostgreSQL database integration for the Music Band Collection Management System.

## Prerequisites

1. **PostgreSQL Database**: Make sure you have access to a PostgreSQL database (version 12 or higher recommended)
2. **Java Dependencies**: The required dependencies (PostgreSQL JDBC driver and HikariCP) are already included in the Maven configuration

## Environment Variables

The system requires the following environment variables to be set:

### Required Database Configuration

```bash
# Database connection URL
export DB_URL="jdbc:postgresql://localhost:5432/musicband_db"

# Database credentials
export DB_USERNAME="your_username"
export DB_PASSWORD="your_password"

# CSV file path (for hybrid persistence)
export MUSIC_BANDS_FILE="/path/to/your/data.csv"
```

### Optional Configuration

```bash
# Persistence mode (default: POSTGRES_WITH_CSV_BACKUP)
export PERSISTENCE_MODE="POSTGRES_WITH_CSV_BACKUP"
```

## Persistence Modes

The system supports four different persistence modes:

1. **CSV_ONLY**: Traditional CSV file storage only
   ```bash
   export PERSISTENCE_MODE="CSV_ONLY"
   ```

2. **POSTGRES_ONLY**: PostgreSQL database only
   ```bash
   export PERSISTENCE_MODE="POSTGRES_ONLY"
   ```

3. **BOTH**: Synchronize between CSV and PostgreSQL
   ```bash
   export PERSISTENCE_MODE="BOTH"
   ```

4. **POSTGRES_WITH_CSV_BACKUP**: PostgreSQL primary with CSV backup (default)
   ```bash
   export PERSISTENCE_MODE="POSTGRES_WITH_CSV_BACKUP"
   ```

## Database Setup

### Option 1: Automatic Schema Creation

The application will automatically create the required database schema when it starts. Just ensure:

1. The database specified in `DB_URL` exists
2. The user has CREATE TABLE permissions
3. The connection parameters are correct

### Option 2: Manual Schema Creation

If you prefer to create the schema manually, execute the following SQL:

```sql
-- Create the database (if it doesn't exist)
CREATE DATABASE musicband_db;

-- Connect to the database and create tables
\c musicband_db;

-- Studios table
CREATE TABLE IF NOT EXISTS studios (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Music bands table
CREATE TABLE IF NOT EXISTS music_bands (
    id BIGINT PRIMARY KEY,
    creation_date TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL,
    coordinate_x REAL NOT NULL,
    coordinate_y BIGINT NOT NULL,
    number_of_participants BIGINT NOT NULL CHECK (number_of_participants > 0),
    singles_count INTEGER CHECK (singles_count > 0),
    establishment_date DATE,
    genre VARCHAR(50) CHECK (genre IN ('PSYCHEDELIC_CLOUD_RAP', 'BLUES', 'BRIT_POP')),
    studio_id INTEGER REFERENCES studios(id)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_music_bands_name ON music_bands(name);
CREATE INDEX IF NOT EXISTS idx_music_bands_creation_date ON music_bands(creation_date);
CREATE INDEX IF NOT EXISTS idx_music_bands_genre ON music_bands(genre);
CREATE INDEX IF NOT EXISTS idx_music_bands_studio_id ON music_bands(studio_id);
CREATE INDEX IF NOT EXISTS idx_studios_name ON studios(name);
```

## Example Environment Setup

### For Development (Local PostgreSQL)

```bash
# .env file or shell export commands
export DB_URL="jdbc:postgresql://localhost:5432/musicband_db"
export DB_USERNAME="musicband_user"
export DB_PASSWORD="secure_password"
export MUSIC_BANDS_FILE="./data/music_bands.csv"
export PERSISTENCE_MODE="POSTGRES_WITH_CSV_BACKUP"
```

### For Production

```bash
# Production environment variables
export DB_URL="jdbc:postgresql://prod-db-host:5432/musicband_production"
export DB_USERNAME="prod_user"
export DB_PASSWORD="very_secure_production_password"
export MUSIC_BANDS_FILE="/var/data/music_bands_backup.csv"
export PERSISTENCE_MODE="POSTGRES_ONLY"
```

## Connection Pool Settings

The system uses HikariCP for connection pooling with the following default settings:

- **Maximum Pool Size**: 10 connections
- **Minimum Idle**: 2 connections  
- **Connection Timeout**: 30 seconds
- **Idle Timeout**: 10 minutes
- **Max Lifetime**: 30 minutes

These settings are optimized for typical usage but can be adjusted in the `DatabaseConfig` class if needed.

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Check if PostgreSQL is running
   - Verify the host and port in DB_URL
   - Ensure firewall allows connections

2. **Authentication Failed**
   - Verify username and password
   - Check PostgreSQL authentication configuration (pg_hba.conf)

3. **Database Does Not Exist**
   - Create the database manually: `CREATE DATABASE musicband_db;`
   - Or update DB_URL to point to an existing database

4. **Permission Denied**
   - Ensure the user has CREATE, INSERT, UPDATE, DELETE permissions
   - Grant necessary permissions: `GRANT ALL PRIVILEGES ON DATABASE musicband_db TO your_username;`

### Fallback Behavior

If database connection fails:
- The system will attempt to fall back to CSV-only mode
- A warning message will be displayed
- All operations will continue to work with CSV storage

## Migration from CSV to PostgreSQL

To migrate existing CSV data to PostgreSQL:

1. Set `PERSISTENCE_MODE="BOTH"`
2. Start the application with your existing CSV file
3. The system will automatically load CSV data and sync it to PostgreSQL
4. Once confirmed working, switch to `PERSISTENCE_MODE="POSTGRES_ONLY"`

## Performance Considerations

- **Indexes**: The system creates indexes on frequently queried columns
- **Connection Pooling**: HikariCP provides efficient connection management
- **Batch Operations**: Large operations use batch processing for better performance
- **Transactions**: All multi-step operations are wrapped in transactions

## Security Notes

- Never commit database credentials to version control
- Use environment variables or secure configuration management
- Consider using connection SSL in production environments
- Regularly update PostgreSQL and JDBC driver versions
