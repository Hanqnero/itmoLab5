# Music Band Collection Management System

A Java-based client-server application for managing music band collections with support for both CSV and PostgreSQL persistence.

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

## Quick Start

### Prerequisites
- Java 11 or higher
- PostgreSQL 12+ (optional, for database persistence)
- Maven 3.6+ (for building)

### Environment Setup

1. **Choose your persistence mode:**
   ```bash
   # For development with database + CSV backup
   source setup-environment.sh development
   
   # For production with PostgreSQL only
   source setup-environment.sh production
   
   # For traditional CSV-only mode
   source setup-environment.sh csv-only
   ```

2. **Set up PostgreSQL (if using database modes):**
   ```sql
   -- Create database and user
   CREATE DATABASE musicband_db;
   CREATE USER musicband_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE musicband_db TO musicband_user;
   ```

3. **Configure environment variables:**
   ```bash
   export DB_URL="jdbc:postgresql://localhost:5432/musicband_db"
   export DB_USERNAME="musicband_user"
   export DB_PASSWORD="your_password"
   export MUSIC_BANDS_FILE="./data/music_bands.csv"
   export PERSISTENCE_MODE="POSTGRES_WITH_CSV_BACKUP"
   ```

### Running the Application

1. **Build the project:**
   ```bash
   mvn clean package
   ```

2. **Start the server:**
   ```bash
   java -jar server/target/server.jar
   ```

3. **Start the client:**
   ```bash
   java -jar client/target/client.jar
   ```

## Persistence Modes

### Available Modes

| Mode | Description | Use Case |
|------|-------------|----------|
| `CSV_ONLY` | Traditional CSV file storage | Legacy systems, simple deployments |
| `POSTGRES_ONLY` | PostgreSQL database only | Production environments |
| `BOTH` | Synchronize CSV and PostgreSQL | Development, testing |
| `POSTGRES_WITH_CSV_BACKUP` | PostgreSQL primary, CSV backup | Recommended for most use cases |

### Configuration

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

### Constraints and Indexes

- Primary keys and foreign key relationships
- Check constraints for positive values
- Indexes on frequently queried columns (name, creation_date, genre)
- Unique constraints where appropriate

## Architecture

### Core Components

- **CollectionManager**: Central collection management with hybrid persistence
- **HybridPersistenceManager**: Unified persistence layer supporting multiple backends
- **PostgreSQLMusicBandORM**: Database operations and queries  
- **DatabaseConfig**: Connection management and configuration
- **CSVManager**: File-based persistence (legacy support)

### Database Infrastructure

- **Connection Pooling**: HikariCP for efficient connection management
- **Transaction Management**: Automatic transaction handling
- **Schema Management**: Automatic table creation and validation
- **Migration Support**: Tools for data migration between storage types

## API Reference

### Environment Variables

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `DB_URL` | For DB modes | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/musicband_db` |
| `DB_USERNAME` | For DB modes | Database username | `musicband_user` |
| `DB_PASSWORD` | For DB modes | Database password | `secure_password` |
| `MUSIC_BANDS_FILE` | Always | CSV file path | `./data/music_bands.csv` |
| `PERSISTENCE_MODE` | Optional | Storage mode | `POSTGRES_WITH_CSV_BACKUP` |

### Command Examples

```bash
# Development setup
export DB_URL="jdbc:postgresql://localhost:5432/musicband_dev"
export DB_USERNAME="dev_user"
export DB_PASSWORD="dev_pass"
export MUSIC_BANDS_FILE="./data/bands.csv"
export PERSISTENCE_MODE="POSTGRES_WITH_CSV_BACKUP"

# Production setup  
export PERSISTENCE_MODE="POSTGRES_ONLY"
export DB_URL="jdbc:postgresql://prod-server:5432/musicband_prod"
# ... other production settings
```

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

## Migration Guide

### From CSV to PostgreSQL

1. **Set up PostgreSQL database**
2. **Configure environment variables**
3. **Set mode to `BOTH` for initial sync:**
   ```bash
   export PERSISTENCE_MODE="BOTH"
   ```
4. **Start application** - Data will be automatically migrated
5. **Switch to `POSTGRES_ONLY` mode** once migration is verified

### Backup and Recovery

- **CSV Backup**: Automatic in `POSTGRES_WITH_CSV_BACKUP` mode
- **Database Backup**: Use standard PostgreSQL backup tools
- **Point-in-time Recovery**: Supported via PostgreSQL PITR

## Troubleshooting

### Common Issues

**Connection refused:**
- Check PostgreSQL is running
- Verify host/port in DB_URL
- Check firewall settings

**Authentication failed:**
- Verify username/password
- Check PostgreSQL authentication config

**Schema errors:**
- Ensure user has CREATE privileges
- Check for existing tables with different schema

For detailed troubleshooting, see [DATABASE_SETUP.md](DATABASE_SETUP.md).

## Development

### Building
```bash
mvn clean compile test package
```

### Testing
```bash
# Unit tests
mvn test

# Integration tests with database
mvn verify -P integration-test
```

### Code Structure
```
server/src/main/java/ru/hanqnero/uni/lab5/server/
├── CollectionManager.java           # Main collection management
├── database/
│   ├── DatabaseConfig.java          # Connection configuration  
│   ├── DatabaseSchemaManager.java   # Schema management
│   ├── HybridPersistenceManager.java # Unified persistence
│   └── PostgreSQLMusicBandORM.java  # Database operations
└── CSVManager.java                   # Legacy CSV support
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality  
4. Ensure all tests pass
5. Submit a pull request
