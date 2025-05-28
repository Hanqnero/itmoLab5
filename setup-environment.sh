#!/bin/bash

# Example configuration script for the Music Band Collection Management System
# This script demonstrates how to set up environment variables for different deployment scenarios

echo "Music Band Collection Management System - Database Configuration"
echo "=================================================================="

# Function to set up development environment
setup_development() {
    echo "Setting up development environment..."
    
    # Database configuration for local PostgreSQL
    export DB_URL="jdbc:postgresql://localhost:5432/musicband_dev"
    export DB_USERNAME="musicband_dev"
    export DB_PASSWORD="dev_password"
    
    # CSV file for hybrid persistence
    export MUSIC_BANDS_FILE="./data/music_bands_dev.csv"
    
    # Use PostgreSQL with CSV backup for development
    export PERSISTENCE_MODE="POSTGRES_WITH_CSV_BACKUP"
    
    echo "Development environment configured:"
    echo "  Database: $DB_URL"
    echo "  CSV File: $MUSIC_BANDS_FILE"
    echo "  Mode: $PERSISTENCE_MODE"
}

# Function to set up production environment
setup_production() {
    echo "Setting up production environment..."
    
    # Database configuration for production PostgreSQL
    export DB_URL="jdbc:postgresql://prod-db-server:5432/musicband_production"
    export DB_USERNAME="musicband_prod"
    export DB_PASSWORD="${PROD_DB_PASSWORD:-secure_production_password}"
    
    # CSV file for backup purposes only
    export MUSIC_BANDS_FILE="/var/data/music_bands_backup.csv"
    
    # Use PostgreSQL only for production
    export PERSISTENCE_MODE="POSTGRES_ONLY"
    
    echo "Production environment configured:"
    echo "  Database: $DB_URL"
    echo "  CSV File: $MUSIC_BANDS_FILE"
    echo "  Mode: $PERSISTENCE_MODE"
}

# Function to set up CSV-only mode (for backward compatibility)
setup_csv_only() {
    echo "Setting up CSV-only mode..."
    
    # No database configuration needed
    unset DB_URL DB_USERNAME DB_PASSWORD
    
    # CSV file is the primary storage
    export MUSIC_BANDS_FILE="./data/music_bands.csv"
    
    # Use CSV only
    export PERSISTENCE_MODE="CSV_ONLY"
    
    echo "CSV-only mode configured:"
    echo "  CSV File: $MUSIC_BANDS_FILE"
    echo "  Mode: $PERSISTENCE_MODE"
}

# Function to set up testing environment
setup_testing() {
    echo "Setting up testing environment..."
    
    # Use in-memory H2 database for testing (if implemented)
    # For now, use a test PostgreSQL database
    export DB_URL="jdbc:postgresql://localhost:5432/musicband_test"
    export DB_USERNAME="musicband_test"
    export DB_PASSWORD="test_password"
    
    # Temporary CSV file for testing
    export MUSIC_BANDS_FILE="/tmp/music_bands_test.csv"
    
    # Use both storages for comprehensive testing
    export PERSISTENCE_MODE="BOTH"
    
    echo "Testing environment configured:"
    echo "  Database: $DB_URL"
    echo "  CSV File: $MUSIC_BANDS_FILE"
    echo "  Mode: $PERSISTENCE_MODE"
}

# Main script
if [ $# -eq 0 ]; then
    echo "Usage: $0 [development|production|csv-only|testing]"
    echo ""
    echo "Available modes:"
    echo "  development  - Local development with PostgreSQL + CSV backup"
    echo "  production   - Production PostgreSQL only"
    echo "  csv-only     - Traditional CSV file storage"
    echo "  testing      - Testing environment with both storages"
    echo ""
    echo "Example:"
    echo "  source $0 development"
    echo "  java -jar server/target/server.jar"
    exit 1
fi

case "$1" in
    development)
        setup_development
        ;;
    production)
        setup_production
        ;;
    csv-only)
        setup_csv_only
        ;;
    testing)
        setup_testing
        ;;
    *)
        echo "Unknown mode: $1"
        echo "Use: development, production, csv-only, or testing"
        exit 1
        ;;
esac

echo ""
echo "Environment configured successfully!"
echo "You can now start the server with these settings."
echo ""
echo "Next steps:"
echo "1. Make sure PostgreSQL is running (if using database modes)"
echo "2. Create the database and user if they don't exist"
echo "3. Run the server application"
echo ""
echo "Example commands:"
echo "  # Create database (if needed)"
echo "  createdb \$DB_URL"
echo "  # Start the server"
echo "  java -jar server/target/server.jar"
