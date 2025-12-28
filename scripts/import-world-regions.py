#!/usr/bin/env python3
"""
World Regions Data Import Script
Imports world regions data from YAML files to database

Usage:
    python scripts/import-world-regions.py [--dry-run] [--limit N] [--type TYPE]

Arguments:
    --dry-run: Validate data without importing
    --limit: Limit number of regions to import
    --type: Import only regions of specific type (continent, subregion, etc.)
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

class WorldRegionsImporter:
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
            return None

    def run_import(self, limit: Optional[int] = None, region_type: Optional[str] = None):
        """Run the import process"""
        cities_dir = Path("knowledge/data/world-regions")
        if not cities_dir.exists():
            logger.error(f"Regions directory not found: {cities_dir}")
            return

        yaml_files = list(cities_dir.glob("*.yaml"))
        logger.info(f"Found {len(yaml_files)} YAML files")

        conn = self.connect_db()
        if not conn:
            return

        try:
            for yaml_file in yaml_files[:limit]:
                if region_type:
                    # Check if this region matches the requested type
                    try:
                        with open(yaml_file, 'r', encoding='utf-8') as f:
                            data = yaml.safe_load(f)
                        region_data = data.get('region', {})
                        if region_data.get('type') != region_type:
                            continue
                    except Exception:
                        pass

                self._import_region_file(yaml_file, conn)
        finally:
            conn.close()

        self._print_summary()

    def _import_region_file(self, yaml_file: Path, conn):
        """Import a single region file"""
        try:
            with open(yaml_file, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)

            # Extract metadata
            metadata = data.get('metadata', {})
            region = data.get('region', {})

            # Build region data structure
            region_data = {
                'region_id': metadata.get('id', ''),
                'name': region.get('name', ''),
                'type': region.get('type', 'continent'),
                'area_km2': region.get('area_km2'),
                'population_2020': region.get('population_2020'),
                'population_2050': region.get('population_2050'),
                'population_2093': region.get('population_2093'),
                'sovereign_states': self._extract_political_data(data).get('sovereign_states'),
                'major_powers': json.dumps(self._extract_political_data(data).get('major_powers')) if self._extract_political_data(data).get('major_powers') else None,
                'conflict_zones': json.dumps(self._extract_political_data(data).get('conflict_zones')) if self._extract_political_data(data).get('conflict_zones') else None,
                'stability_index': self._extract_political_data(data).get('stability_index'),
                'gdp_total': self._extract_economic_data(data).get('gdp_total'),
                'dominant_sectors': json.dumps(self._extract_economic_data(data).get('dominant_sectors')) if self._extract_economic_data(data).get('dominant_sectors') else None,
                'trade_hubs': json.dumps(self._extract_economic_data(data).get('trade_hubs')) if self._extract_economic_data(data).get('trade_hubs') else None,
                'currency_zones': json.dumps(self._extract_economic_data(data).get('currency_zones')) if self._extract_economic_data(data).get('currency_zones') else None,
                'class_structure': json.dumps(self._extract_social_data(data).get('class_structure')) if self._extract_social_data(data).get('class_structure') else None,
                'cultural_diversity': self._extract_social_data(data).get('cultural_diversity'),
                'education_level': self._extract_social_data(data).get('education_level'),
                'healthcare_access': self._extract_social_data(data).get('healthcare_access'),
                'migration_patterns': json.dumps(self._extract_social_data(data).get('migration_patterns')) if self._extract_social_data(data).get('migration_patterns') else None,
                'cybernetics_adoption': self._extract_technology_data(data).get('cybernetics_adoption'),
                'ai_integration': self._extract_technology_data(data).get('ai_integration'),
                'network_infrastructure': self._extract_technology_data(data).get('network_infrastructure'),
                'megacities': json.dumps(self._extract_technology_data(data).get('megacities')) if self._extract_technology_data(data).get('megacities') else None,
                'research_centers': json.dumps(self._extract_technology_data(data).get('research_centers')) if self._extract_technology_data(data).get('research_centers') else None,
                'climate_zones': json.dumps(self._extract_environmental_data(data).get('climate_zones')) if self._extract_environmental_data(data).get('climate_zones') else None,
                'natural_resources': json.dumps(self._extract_environmental_data(data).get('natural_resources')) if self._extract_environmental_data(data).get('natural_resources') else None,
                'environmental_issues': json.dumps(self._extract_environmental_data(data).get('environmental_issues')) if self._extract_environmental_data(data).get('environmental_issues') else None,
                'protected_areas': json.dumps(self._extract_environmental_data(data).get('protected_areas')) if self._extract_environmental_data(data).get('protected_areas') else None,
                'major_factions': json.dumps(self._extract_military_data(data).get('major_factions')) if self._extract_military_data(data).get('major_factions') else None,
                'conflict_types': json.dumps(self._extract_military_data(data).get('conflict_types')) if self._extract_military_data(data).get('conflict_types') else None,
                'strategic_resources': json.dumps(self._extract_military_data(data).get('strategic_resources')) if self._extract_military_data(data).get('strategic_resources') else None,
                'cities': json.dumps(data.get('cities')) if data.get('cities') else None,
                'timeline_events': json.dumps(data.get('timeline_events')) if data.get('timeline_events') else None,
                'subregions': json.dumps(data.get('subregions')) if data.get('subregions') else None,
                'game_regions': json.dumps(data.get('game_regions')) if data.get('game_regions') else None,
                'source_file': str(yaml_file),
                'version': metadata.get('version', '1.0.0'),
                'status': 'active'
            }

            if self.dry_run:
                logger.info(f"DRY RUN: Would import region: {region_data['name']}")
                self.stats['processed'] += 1
                self.stats['imported'] += 1
                return

            # Insert region data
            self._insert_region(conn, region_data)
            logger.info(f"Imported region: {region_data['name']}")
            self.stats['processed'] += 1
            self.stats['imported'] += 1

        except Exception as e:
            logger.error(f"Failed to import {yaml_file}: {e}")
            self.stats['processed'] += 1
            self.stats['failed'] += 1

    def _extract_political_data(self, data: Dict) -> Dict:
        """Extract political data"""
        political = data.get('political', {})
        return {
            'sovereign_states': political.get('sovereign_states'),
            'major_powers': political.get('major_powers'),
            'conflict_zones': political.get('conflict_zones'),
            'stability_index': political.get('stability_index')
        }

    def _extract_economic_data(self, data: Dict) -> Dict:
        """Extract economic data"""
        economic = data.get('economic', {})
        return {
            'gdp_total': economic.get('gdp_total'),
            'dominant_sectors': economic.get('dominant_sectors'),
            'trade_hubs': economic.get('trade_hubs'),
            'currency_zones': economic.get('currency_zones')
        }

    def _extract_social_data(self, data: Dict) -> Dict:
        """Extract social data"""
        social = data.get('social', {})
        return {
            'class_structure': social.get('class_structure'),
            'cultural_diversity': social.get('cultural_diversity'),
            'education_level': social.get('education_level'),
            'healthcare_access': social.get('healthcare_access'),
            'migration_patterns': social.get('migration_patterns')
        }

    def _extract_technology_data(self, data: Dict) -> Dict:
        """Extract technology data"""
        technology = data.get('technology', {})
        return {
            'cybernetics_adoption': technology.get('cybernetics_adoption'),
            'ai_integration': technology.get('ai_integration'),
            'network_infrastructure': technology.get('network_infrastructure'),
            'megacities': technology.get('megacities'),
            'research_centers': technology.get('research_centers')
        }

    def _extract_environmental_data(self, data: Dict) -> Dict:
        """Extract environmental data"""
        environment = data.get('environment', {})
        return {
            'climate_zones': environment.get('climate_zones'),
            'natural_resources': environment.get('natural_resources'),
            'environmental_issues': environment.get('environmental_issues'),
            'protected_areas': environment.get('protected_areas')
        }

    def _extract_military_data(self, data: Dict) -> Dict:
        """Extract military data"""
        military = data.get('military', {})
        return {
            'major_factions': military.get('major_factions'),
            'conflict_types': military.get('conflict_types'),
            'strategic_resources': military.get('strategic_resources')
        }

    def _insert_region(self, conn, region_data: Dict):
        """Insert region data into database"""
        query = """
        INSERT INTO regions (
            region_id, name, type, area_km2, population_2020, population_2050, population_2093,
            sovereign_states, major_powers, conflict_zones, stability_index,
            gdp_total, dominant_sectors, trade_hubs, currency_zones,
            class_structure, cultural_diversity, education_level, healthcare_access, migration_patterns,
            cybernetics_adoption, ai_integration, network_infrastructure, megacities, research_centers,
            climate_zones, natural_resources, environmental_issues, protected_areas,
            major_factions, conflict_types, strategic_resources,
            cities, timeline_events, subregions, game_regions,
            source_file, version, status
        ) VALUES (
            %(region_id)s, %(name)s, %(type)s, %(area_km2)s, %(population_2020)s, %(population_2050)s, %(population_2093)s,
            %(sovereign_states)s, %(major_powers)s, %(conflict_zones)s, %(stability_index)s,
            %(gdp_total)s, %(dominant_sectors)s, %(trade_hubs)s, %(currency_zones)s,
            %(class_structure)s, %(cultural_diversity)s, %(education_level)s, %(healthcare_access)s, %(migration_patterns)s,
            %(cybernetics_adoption)s, %(ai_integration)s, %(network_infrastructure)s, %(megacities)s, %(research_centers)s,
            %(climate_zones)s, %(natural_resources)s, %(environmental_issues)s, %(protected_areas)s,
            %(major_factions)s, %(conflict_types)s, %(strategic_resources)s,
            %(cities)s, %(timeline_events)s, %(subregions)s, %(game_regions)s,
            %(source_file)s, %(version)s, %(status)s
        )
        ON CONFLICT (region_id) DO UPDATE SET
            name = EXCLUDED.name,
            type = EXCLUDED.type,
            area_km2 = EXCLUDED.area_km2,
            population_2020 = EXCLUDED.population_2020,
            population_2050 = EXCLUDED.population_2050,
            population_2093 = EXCLUDED.population_2093,
            sovereign_states = EXCLUDED.sovereign_states,
            major_powers = EXCLUDED.major_powers,
            conflict_zones = EXCLUDED.conflict_zones,
            stability_index = EXCLUDED.stability_index,
            gdp_total = EXCLUDED.gdp_total,
            dominant_sectors = EXCLUDED.dominant_sectors,
            trade_hubs = EXCLUDED.trade_hubs,
            currency_zones = EXCLUDED.currency_zones,
            class_structure = EXCLUDED.class_structure,
            cultural_diversity = EXCLUDED.cultural_diversity,
            education_level = EXCLUDED.education_level,
            healthcare_access = EXCLUDED.healthcare_access,
            migration_patterns = EXCLUDED.migration_patterns,
            cybernetics_adoption = EXCLUDED.cybernetics_adoption,
            ai_integration = EXCLUDED.ai_integration,
            network_infrastructure = EXCLUDED.network_infrastructure,
            megacities = EXCLUDED.megacities,
            research_centers = EXCLUDED.research_centers,
            climate_zones = EXCLUDED.climate_zones,
            natural_resources = EXCLUDED.natural_resources,
            environmental_issues = EXCLUDED.environmental_issues,
            protected_areas = EXCLUDED.protected_areas,
            major_factions = EXCLUDED.major_factions,
            conflict_types = EXCLUDED.conflict_types,
            strategic_resources = EXCLUDED.strategic_resources,
            cities = EXCLUDED.cities,
            timeline_events = EXCLUDED.timeline_events,
            subregions = EXCLUDED.subregions,
            game_regions = EXCLUDED.game_regions,
            source_file = EXCLUDED.source_file,
            version = EXCLUDED.version,
            updated_at = NOW()
        """

        with conn.cursor() as cursor:
            cursor.execute(query, region_data)

    def _print_summary(self):
        """Print import summary"""
        logger.info("=== Import Summary ===")
        logger.info(f"Processed: {self.stats['processed']}")
        logger.info(f"Imported: {self.stats['imported']}")
        logger.info(f"Failed: {self.stats['failed']}")
        logger.info(f"Skipped: {self.stats['skipped']}")
        if self.dry_run:
            logger.info("DRY RUN - No actual data was imported")


def main():
    parser = argparse.ArgumentParser(description='Import world regions data')
    parser.add_argument('--dry-run', action='store_true', help='Validate data without importing')
    parser.add_argument('--limit', type=int, help='Limit number of regions to import')
    parser.add_argument('--type', help='Import only regions of specific type')

    args = parser.parse_args()

    # Load configuration
    config = DatabaseConfig()

    # Create importer
    importer = WorldRegionsImporter(config, dry_run=args.dry_run)

    # Run import
    importer.run_import(limit=args.limit, region_type=args.type)


if __name__ == '__main__':
    main()
