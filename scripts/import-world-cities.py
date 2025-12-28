#!/usr/bin/env python3
"""
World Cities Data Import Script
Imports world cities data from YAML files to database

Usage:
    python scripts/import-world-cities.py [--dry-run] [--limit N] [--continent CONTINENT]

Arguments:
    --dry-run: Validate data without importing
    --limit: Limit number of cities to import
    --continent: Import only cities from specific continent
"""

import os
import sys
import yaml
import psycopg2
import psycopg2.extras
import json
import argparse
from pathlib import Path
from typing import Dict, List, Optional, Any
from dataclasses import dataclass
import logging

# Add project root to path
sys.path.append(str(Path(__file__).parent.parent))

from scripts.core.config import DatabaseConfig
import logging

# Convert DatabaseConfig to class for compatibility
@dataclass
class DatabaseConfig:
    host: str = "localhost"
    port: int = 5432
    database: str = "necpgame"
    user: str = "postgres"
    password: str = "postgres"

    def __init__(self):
        self.host = os.getenv("DB_HOST", self.host)
        self.port = int(os.getenv("DB_PORT", self.port))
        self.database = os.getenv("DB_NAME", self.database)
        self.user = os.getenv("DB_USER", self.user)
        self.password = os.getenv("DB_PASSWORD", self.password)

    @property
    def DB_HOST(self):
        return self.host

    @property
    def DB_PORT(self):
        return self.port

    @property
    def DB_NAME(self):
        return self.database

    @property
    def DB_USER(self):
        return self.user

    @property
    def DB_PASSWORD(self):
        return self.password

# Setup basic logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(name)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)

class WorldCitiesImporter:
    def __init__(self, config: DatabaseConfig, dry_run: bool = False):
        self.config = config
        self.dry_run = dry_run
        self.stats = {
            'processed': 0,
            'imported': 0,
            'failed': 0,
            'skipped': 0
        }

    def connect_db(self):
        """Connect to database"""
        try:
            conn = psycopg2.connect(
                host=self.config.DB_HOST,
                port=self.config.DB_PORT,
                database=self.config.DB_NAME,
                user=self.config.DB_USER,
                password=self.config.DB_PASSWORD
            )
            return conn
        except Exception as e:
            logger.error(f"Failed to connect to database: {e}")
            raise

    def parse_city_data(self, yaml_file: Path) -> Optional[Dict[str, Any]]:
        """Parse city data from YAML file"""
        try:
            with open(yaml_file, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)

            # Extract metadata
            metadata = data.get('metadata', {})
            summary = data.get('summary', {})
            content = data.get('content', {})
            city = data.get('city', {})

            # Build city data structure
            # Debug coordinate extraction
            lat = self._extract_coordinate(data, 'latitude')
            lon = self._extract_coordinate(data, 'longitude')
            logger.info(f"City {metadata.get('title', '')}: lat={lat}, lon={lon}")

            city_data = {
                'city_id': metadata.get('id', ''),
                'name': metadata.get('title', '').split(':')[0].strip(),
                'continent': self._extract_continent(data),
                'country': self._extract_country(data),
                'latitude': lat,
                'longitude': lon,
                'population_2020': self._extract_population(content, 2020),
                'population_2050': self._extract_population(content, 2050),
                'population_2093': self._extract_population(content, 2093),
                'area_km2': self._extract_area(content),
                'elevation_m': self._extract_elevation(content),
                'cyberpunk_level': self._extract_cyberpunk_level(content),
                'corruption_index': self._extract_corruption_index(content),
                'technology_index': self._extract_technology_index(content),
                'zones': self._extract_zones(content),
                'districts': self._extract_districts(content),
                'landmarks': self._extract_landmarks(content),
                'economy_data': self._extract_economy_data(content),
                'corporation_presence': self._extract_corporations(content),
                'faction_influence': self._extract_factions(content),
                'timeline_events': self._extract_timeline_events(content),
                'future_evolution': self._extract_future_evolution(content),
                'is_capital': self._extract_is_capital(content),
                'is_megacity': self._extract_is_megacity(content),
                'available_in_game': True,
                'game_regions': self._extract_game_regions(content),
                'source_file': str(yaml_file),
                'version': metadata.get('version', '1.0.0'),
                'status': 'active'
            }

            return city_data

        except Exception as e:
            logger.error(f"Failed to parse {yaml_file}: {e}")
            return None

    def _extract_continent(self, content: Dict) -> str:
        """Extract continent from content"""
        # Try different possible locations
        if 'city' in content:
            city_data = content['city']
            if 'continent' in city_data:
                return city_data['continent']
        if 'geography' in content:
            geo = content['geography']
            if 'continent' in geo:
                return geo['continent']
        if 'location' in content:
            loc = content['location']
            if 'continent' in loc:
                return loc['continent']
        # Default based on filename patterns
        return 'Unknown'

    def _extract_country(self, content: Dict) -> str:
        """Extract country from content"""
        if 'city' in content:
            city_data = content['city']
            if 'country' in city_data:
                return city_data['country']
        if 'geography' in content:
            geo = content['geography']
            if 'country' in geo:
                return geo['country']
        if 'location' in content:
            loc = content['location']
            if 'country' in loc:
                return loc['country']
        return 'Unknown'

    def _extract_coordinate(self, content: Dict, coord_type: str) -> Optional[float]:
        """Extract latitude/longitude"""
        # Try different possible locations for coordinates
        if 'city' in content:
            city_data = content['city']
            coords = city_data.get('coordinates', {})
            if coord_type in coords:
                return float(coords[coord_type])
        if 'geography' in content:
            geo = content['geography']
            coords = geo.get('coordinates', {})
            if coord_type in coords:
                return float(coords[coord_type])
        return None

    def _extract_population(self, content: Dict, year: int) -> Optional[int]:
        """Extract population for specific year"""
        if 'demographics' in content:
            demo = content['demographics']
            pop_data = demo.get('population', {})
            if isinstance(pop_data, dict):
                year_str = str(year)
                if year_str in pop_data:
                    return int(pop_data[year_str])
        return None

    def _extract_area(self, content: Dict) -> Optional[float]:
        """Extract area in km²"""
        if 'geography' in content:
            geo = content['geography']
            if 'area_km2' in geo:
                return float(geo['area_km2'])
        return None

    def _extract_elevation(self, content: Dict) -> Optional[int]:
        """Extract elevation in meters"""
        if 'geography' in content:
            geo = content['geography']
            if 'elevation_m' in geo:
                return int(geo['elevation_m'])
        return None

    def _extract_cyberpunk_level(self, content: Dict) -> Optional[int]:
        """Extract cyberpunk level (1-10)"""
        if 'cyberpunk' in content:
            cp = content['cyberpunk']
            if 'level' in cp:
                return int(cp['level'])
        return None

    def _extract_corruption_index(self, content: Dict) -> Optional[float]:
        """Extract corruption index"""
        if 'politics' in content:
            pol = content['politics']
            if 'corruption_index' in pol:
                return float(pol['corruption_index'])
        return None

    def _extract_technology_index(self, content: Dict) -> Optional[float]:
        """Extract technology index"""
        if 'technology' in content:
            tech = content['technology']
            if 'technology_index' in tech:
                return float(tech['technology_index'])
        return None

    def _extract_zones(self, content: Dict) -> Optional[str]:
        """Extract zones as JSON"""
        if 'zones' in content:
            return json.dumps(content['zones'])
        return None

    def _extract_districts(self, content: Dict) -> Optional[str]:
        """Extract districts as JSON"""
        if 'districts' in content:
            return json.dumps(content['districts'])
        return None

    def _extract_landmarks(self, content: Dict) -> Optional[str]:
        """Extract landmarks as JSON"""
        if 'landmarks' in content:
            return json.dumps(content['landmarks'])
        return None

    def _extract_economy_data(self, content: Dict) -> Optional[str]:
        """Extract economy data as JSON"""
        if 'economy' in content:
            return json.dumps(content['economy'])
        return None

    def _extract_corporations(self, content: Dict) -> Optional[str]:
        """Extract corporation presence as JSON"""
        if 'corporations' in content:
            return json.dumps(content['corporations'])
        return None

    def _extract_factions(self, content: Dict) -> Optional[str]:
        """Extract faction influence as JSON"""
        if 'factions' in content:
            return json.dumps(content['factions'])
        return None

    def _extract_timeline_events(self, content: Dict) -> Optional[str]:
        """Extract timeline events as JSON"""
        if 'timeline' in content:
            return json.dumps(content['timeline'])
        return None

    def _extract_future_evolution(self, content: Dict) -> Optional[str]:
        """Extract future evolution as JSON"""
        if 'future_evolution' in content:
            return json.dumps(content['future_evolution'])
        return None

    def _extract_is_capital(self, content: Dict) -> bool:
        """Extract if city is capital"""
        if 'politics' in content:
            pol = content['politics']
            if 'is_capital' in pol:
                return bool(pol['is_capital'])
        return False

    def _extract_is_megacity(self, content: Dict) -> bool:
        """Extract if city is megacity"""
        if 'demographics' in content:
            demo = content['demographics']
            if 'is_megacity' in demo:
                return bool(demo['is_megacity'])
        return False

    def _extract_game_regions(self, content: Dict) -> Optional[str]:
        """Extract game regions as JSON"""
        if 'gameplay' in content:
            gp = content['gameplay']
            if 'regions' in gp:
                return json.dumps(gp['regions'])
        return None

    def import_city(self, conn, city_data: Dict[str, Any]) -> bool:
        """Import single city to database"""
        try:
            with conn.cursor() as cur:
                # Check if city already exists
                cur.execute("SELECT id FROM cities WHERE city_id = %s", (city_data['city_id'],))
                existing = cur.fetchone()

                if existing:
                    logger.info(f"City {city_data['name']} already exists, skipping")
                    self.stats['skipped'] += 1
                    return True

                # Insert new city
                query = """
                INSERT INTO cities (
                    id, city_id, name, name_local, country, continent, latitude, longitude,
                    population_2020, population_2050, population_2093, area_km2, elevation_m,
                    cyberpunk_level, corruption_index, technology_index, zones, districts,
                    landmarks, economy_data, corporation_presence, faction_influence,
                    timeline_events, future_evolution, status, is_capital, is_megacity,
                    available_in_game, game_regions, source_file, version, created_at, updated_at
                ) VALUES (
                    gen_random_uuid(), %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s,
                    %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, NOW(), NOW()
                )
                """

                cur.execute(query, (
                    city_data['city_id'], city_data['name'], city_data.get('name_local'),
                    city_data['country'], city_data['continent'], city_data['latitude'],
                    city_data['longitude'], city_data['population_2020'], city_data['population_2050'],
                    city_data['population_2093'], city_data['area_km2'], city_data['elevation_m'],
                    city_data['cyberpunk_level'], city_data['corruption_index'], city_data['technology_index'],
                    city_data['zones'], city_data['districts'], city_data['landmarks'],
                    city_data['economy_data'], city_data['corporation_presence'], city_data['faction_influence'],
                    city_data['timeline_events'], city_data['future_evolution'], city_data['status'],
                    city_data['is_capital'], city_data['is_megacity'], city_data['available_in_game'],
                    city_data['game_regions'], city_data['source_file'], city_data['version']
                ))

                if not self.dry_run:
                    conn.commit()

                logger.info(f"Imported city: {city_data['name']}")
                self.stats['imported'] += 1
                return True

        except Exception as e:
            logger.error(f"Failed to import city {city_data.get('name', 'Unknown')}: {e}")
            self.stats['failed'] += 1
            return False

    def run_import(self, limit: Optional[int] = None, continent: Optional[str] = None):
        """Run the import process"""
        cities_dir = Path("knowledge/data/world-cities")
        if not cities_dir.exists():
            logger.error(f"Cities directory not found: {cities_dir}")
            return

        # Collect YAML files
        yaml_files = list(cities_dir.glob("*.yaml"))
        logger.info(f"Found {len(yaml_files)} YAML files")

        # Filter by continent if specified
        if continent:
            filtered_files = []
            for yaml_file in yaml_files:
                city_data = self.parse_city_data(yaml_file)
                if city_data and city_data['continent'].lower() == continent.lower():
                    filtered_files.append(yaml_file)
            yaml_files = filtered_files
            logger.info(f"Filtered to {len(yaml_files)} files for continent: {continent}")

        # Apply limit
        if limit:
            yaml_files = yaml_files[:limit]
            logger.info(f"Limited to {len(yaml_files)} files")

        if not yaml_files:
            logger.warning("No files to process")
            return

        # Connect to database
        conn = None
        try:
            conn = self.connect_db()

            # Process each file
            for yaml_file in yaml_files:
                self.stats['processed'] += 1

                logger.info(f"Processing {yaml_file.name}")

                # Parse city data
                city_data = self.parse_city_data(yaml_file)
                if not city_data:
                    self.stats['failed'] += 1
                    continue

                # Import city
                if not self.import_city(conn, city_data):
                    continue

                # Progress logging
                if self.stats['processed'] % 10 == 0:
                    logger.info(f"Progress: {self.stats['processed']}/{len(yaml_files)} processed")

        except Exception as e:
            logger.error(f"Import failed: {e}")
        finally:
            if conn:
                conn.close()

        # Print summary
        self.print_summary()

    def print_summary(self):
        """Print import summary"""
        logger.info("=== Import Summary ===")
        logger.info(f"Processed: {self.stats['processed']}")
        logger.info(f"Imported: {self.stats['imported']}")
        logger.info(f"Failed: {self.stats['failed']}")
        logger.info(f"Skipped: {self.stats['skipped']}")

        if self.dry_run:
            logger.info("DRY RUN - No actual data was imported")

def main():
    parser = argparse.ArgumentParser(description="Import world cities data")
    parser.add_argument("--dry-run", action="store_true", help="Validate without importing")
    parser.add_argument("--limit", type=int, help="Limit number of cities to import")
    parser.add_argument("--continent", help="Import only cities from specific continent")

    args = parser.parse_args()

    # Load configuration
    config = DatabaseConfig()

    # Create importer
    importer = WorldCitiesImporter(config, dry_run=args.dry_run)

    # Run import
    importer.run_import(limit=args.limit, continent=args.continent)

if __name__ == "__main__":
    main()
