# Music Band Collection Management System

A Java-based client-server application for managing music band collections with support for both CSV and PostgreSQL persistence.

## Prerequisites
- Java 17 or higher
- PostgreSQL
- Maven

## Features

### Core Functionality
- **Music Band Management**: Add, update, delete, and query music bands
- **Collection Operations**: Sort, filter, and analyze band collections
- **TCP Client-Server Architecture**: Networked communication between client and server
- **Command-Line Interface**: Interactive console for collection management

### Persistence Options
- **Hybrid Persistence**: Support for CSV files, PostgreSQL database, or both
- **Multiple Storage Modes**: Choose between CSV-only, PostgreSQL-only, synchronized, or backup modes
- **Data Migration**: Automatic migration from CSV to PostgreSQL
- **Fallback Support**: Graceful degradation if database is unavailable

### Database Features
- **PostgreSQL Integration**: Full CRUD operations with PostgreSQL database
- **Connection Pooling**: HikariCP for efficient database connections
- **Schema Management**: Automatic database schema creation and validation
- **Transaction Support**: ACID compliance for data consistency
- **Performance Optimization**: Indexes and batch operations


### Persistence Modes

| Mode | Description | Use Case |
|------|-------------|----------|
| `CSV_ONLY` | Traditional CSV file storage | Legacy systems, simple deployments |
| `POSTGRES_ONLY` | PostgreSQL database only | Production environments |
| `BOTH` | Synchronize CSV and PostgreSQL | Development, testing |
| `POSTGRES_WITH_CSV_BACKUP` | PostgreSQL primary, CSV backup | Recommended for most use cases |

Set the `PERSISTENCE_MODE` environment variable to your desired mode:

```bash
export PERSISTENCE_MODE="POSTGRES_WITH_CSV_BACKUP"
```

## Database Schema

### Tables

**music_bands**
- `id` (BIGINT, PRIMARY KEY) - Unique band identifier
- `creation_date` (TIMESTAMP) - Record creation timestamp
- `name` (VARCHAR(255)) - Band name
- `coordinate_x` (REAL) - X coordinate
- `coordinate_y` (BIGINT) - Y coordinate  
- `number_of_participants` (BIGINT) - Number of band members
- `singles_count` (INTEGER) - Number of singles released
- `establishment_date` (DATE) - Band establishment date
- `genre` (VARCHAR(50)) - Music genre (enum)
- `studio_id` (INTEGER) - Foreign key to studios table

**studios**
- `id` (SERIAL, PRIMARY KEY) - Studio identifier
- `name` (VARCHAR(255)) - Studio name

## Architecture

### Core Components

- **CollectionManager**: Central collection management with hybrid persistence
- **HybridPersistenceManager**: Unified persistence layer supporting multiple backends
- **PostgreSQLMusicBandORM**: Database operations and queries  
- **DatabaseConfig**: Connection management and configuration
- **CSVManager**: File-based persistence

### Database Infrastructure

- **Connection Pooling**: HikariCP for efficient connection management
- **Transaction Management**: Automatic transaction handling
- **Schema Management**: Automatic table creation and validation
- **Migration Support**: Tools for data migration between storage types


## Environment Variables

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `DB_URL` | For DB modes | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/musicband_db` |
| `DB_USERNAME` | For DB modes | Database username | `musicband_user` |
| `DB_PASSWORD` | For DB modes | Database password | `secure_password` |
| `MUSIC_BANDS_FILE` | Always | CSV file path | `./data/music_bands.csv` |
| `PERSISTENCE_MODE` | Optional | Storage mode | `POSTGRES_WITH_CSV_BACKUP` |

## Performance Considerations

### Database Optimization
- Connection pooling (max 10 connections)
- Batch operations for bulk inserts/updates
- Proper indexing on query columns
- Transaction optimization

### Memory Management
- TreeSet for automatic sorting
- Lazy loading for large datasets
- Connection cleanup and resource management

## Error Handling

### Automatic Fallbacks
- Database unavailable → Falls back to CSV mode
- Invalid data → Logs error and continues
- Connection timeout → Automatic retry with exponential backoff

### Monitoring
- Connection health checks
- Query performance logging  
- Error tracking and reporting
