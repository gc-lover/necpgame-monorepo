#!/usr/bin/env python3
"""
Example Python migration script for Liquibase.

Issue: #142718723

This script demonstrates how to use Python scripts in Liquibase migrations.
The script receives database connection parameters via command-line arguments
and performs data transformations or complex migrations that are easier to
implement in Python than in SQL.

Usage:
    python example_migration.py --database-url <url> --username <user> --password <pass>

Exit codes:
    0 - Success
    1 - Error
"""

import argparse
import sys
import psycopg2
from psycopg2 import sql


def parse_args():
    """Parse command-line arguments."""
    parser = argparse.ArgumentParser(description='Example Python migration script')
    parser.add_argument('--database-url', required=True, help='Database connection URL')
    parser.add_argument('--username', required=True, help='Database username')
    parser.add_argument('--password', required=True, help='Database password')
    return parser.parse_args()


def main():
    """Main migration logic."""
    args = parse_args()
    
    try:
        # Connect to database
        # Parse JDBC URL: jdbc:postgresql://host:port/dbname?params
        url = args.database_url
        
        # Remove jdbc: prefix if present
        if url.startswith('jdbc:'):
            url = url[5:]
        
        # Extract protocol and connection string
        if '://' in url:
            protocol, conn_str = url.split('://', 1)
            if protocol != 'postgresql':
                raise ValueError(f"Unsupported database protocol: {protocol}")
        else:
            conn_str = url
        
        # Extract database name and connection params
        if '/' in conn_str:
            host_port, dbname_and_params = conn_str.split('/', 1)
            # Remove query parameters if present
            if '?' in dbname_and_params:
                dbname = dbname_and_params.split('?')[0]
            else:
                dbname = dbname_and_params
        else:
            host_port = conn_str
            dbname = 'necpgame'
        
        # Extract host and port
        if ':' in host_port:
            host, port_str = host_port.rsplit(':', 1)
            try:
                port = int(port_str)
            except ValueError:
                host = host_port
                port = 5432
        else:
            host = host_port
            port = 5432
        
        # Connect to PostgreSQL
        conn = psycopg2.connect(
            host=host,
            port=port,
            database=dbname,
            user=args.username,
            password=args.password
        )
        
        cursor = conn.cursor()
        
        # Example: Complex data transformation
        # This is just an example - replace with actual migration logic
        print("Executing Python migration script...")
        
        # Example: Update data using Python logic
        cursor.execute("""
            SELECT COUNT(*) FROM information_schema.tables 
            WHERE table_schema = 'mvp_core'
        """)
        table_count = cursor.fetchone()[0]
        print(f"Found {table_count} tables in mvp_core schema")
        
        # Commit changes
        conn.commit()
        cursor.close()
        conn.close()
        
        print("Migration completed successfully")
        return 0
        
    except Exception as e:
        print(f"Error executing migration: {e}", file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())

