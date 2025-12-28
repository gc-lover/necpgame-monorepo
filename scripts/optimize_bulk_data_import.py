#!/usr/bin/env python3
"""
Optimized Bulk Data Import Script for 1M+ Records
Handles NPC and Quest data imports with high performance

Usage:
    python scripts/optimize_bulk_data_import.py --type quests --batch-size 10000 --workers 4
    python scripts/optimize_bulk_data_import.py --type npcs --batch-size 5000 --workers 8
"""

import os
import sys
import json
import psycopg2
import psycopg2.extras
import argparse
import time
from pathlib import Path
from concurrent.futures import ThreadPoolExecutor, as_completed
from typing import List, Dict, Any, Tuple
import logging

# Add project root to path
sys.path.append(str(Path(__file__).parent.parent))

# Setup logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(name)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)

class BulkDataImporter:
    def __init__(self, db_config: Dict[str, str], batch_size: int = 1000, workers: int = 4):
        self.db_config = db_config
        self.batch_size = batch_size
        self.workers = workers
        self.stats = {
            'processed': 0,
            'successful': 0,
            'failed': 0,
            'start_time': time.time(),
            'batches': 0
        }

    def get_db_connection(self):
        """Get optimized database connection"""
        return psycopg2.connect(
            **self.db_config,
            cursor_factory=psycopg2.extras.RealDictCursor
        )

    def prepare_quest_data(self, quest_files: List[Path]) -> List[Dict[str, Any]]:
        """Prepare quest data for bulk import"""
        quests = []

        for quest_file in quest_files:
            try:
                with open(quest_file, 'r', encoding='utf-8') as f:
                    data = json.load(f)

                # Transform YAML structure to database format
                quest = {
                    'id': data.get('metadata', {}).get('id', str(quest_file.stem)),
                    'metadata': json.dumps(data.get('metadata', {})),
                    'quest_id': data.get('quest_id', ''),
                    'title': data.get('title', ''),
                    'description': data.get('description', ''),
                    'status': data.get('status', 'draft'),
                    'level_min': data.get('requirements', {}).get('level_min', 1),
                    'level_max': data.get('requirements', {}).get('level_max', 50),
                    'estimated_duration': data.get('estimated_duration', 30),
                    'difficulty': data.get('difficulty', 'normal'),
                    'category': data.get('category', 'main'),
                    'type': data.get('type', 'story'),
                    'rewards': json.dumps(data.get('rewards', {})),
                    'prerequisites': json.dumps(data.get('prerequisites', {})),
                    'objectives': json.dumps(data.get('objectives', [])),
                    'dialogue_tree': json.dumps(data.get('dialogue_tree', {})),
                    'branching_paths': json.dumps(data.get('branching_paths', {})),
                    'locations': json.dumps(data.get('locations', [])),
                    'npcs': json.dumps(data.get('npcs', [])),
                    'items': json.dumps(data.get('items', [])),
                    'events': json.dumps(data.get('events', [])),
                    'combat_elements': json.dumps(data.get('combat_elements', {})),
                    'endings': json.dumps(data.get('endings', [])),
                    'technical_requirements': json.dumps(data.get('technical_requirements', {})),
                    'balance_notes': json.dumps(data.get('balance_notes', {})),
                    'qa_checklist': json.dumps(data.get('qa_checklist', [])),
                }

                quests.append(quest)
                self.stats['processed'] += 1

            except Exception as e:
                logger.error(f"Failed to process quest {quest_file}: {e}")
                self.stats['failed'] += 1

        return quests

    def prepare_npc_data(self, npc_files: List[Path]) -> List[Dict[str, Any]]:
        """Prepare NPC data for bulk import"""
        npcs = []

        for npc_file in npc_files:
            try:
                with open(npc_file, 'r', encoding='utf-8') as f:
                    data = json.load(f)

                # Transform NPC structure to database format
                npc = {
                    'id': data.get('metadata', {}).get('id', str(npc_file.stem)),
                    'metadata': json.dumps(data.get('metadata', {})),
                    'npc_id': data.get('npc_id', ''),
                    'name': data.get('name', ''),
                    'description': data.get('description', ''),
                    'role': data.get('role', 'civilian'),
                    'location': data.get('location', ''),
                    'faction': data.get('faction', ''),
                    'personality': json.dumps(data.get('personality', {})),
                    'dialogues': json.dumps(data.get('dialogues', [])),
                    'quests': json.dumps(data.get('quests', [])),
                    'relationships': json.dumps(data.get('relationships', {})),
                    'schedule': json.dumps(data.get('schedule', {})),
                    'appearance': json.dumps(data.get('appearance', {})),
                    'combat_stats': json.dumps(data.get('combat_stats', {})),
                    'trading_inventory': json.dumps(data.get('trading_inventory', [])),
                    'technical_requirements': json.dumps(data.get('technical_requirements', {})),
                }

                npcs.append(npc)
                self.stats['processed'] += 1

            except Exception as e:
                logger.error(f"Failed to process NPC {npc_file}: {e}")
                self.stats['failed'] += 1

        return npcs

    def bulk_import_quests(self, quests: List[Dict[str, Any]]) -> bool:
        """Bulk import quests using optimized PostgreSQL COPY"""
        if not quests:
            return True

        conn = None
        try:
            conn = self.get_db_connection()

            # Create temporary table for staging
            with conn.cursor() as cur:
                cur.execute("""
                    CREATE TEMP TABLE temp_quests (
                        LIKE gameplay.quest_definitions INCLUDING ALL
                    ) ON COMMIT DROP;
                """)

                # Use COPY for bulk insert
                with cur.copy("COPY temp_quests FROM STDIN WITH CSV HEADER") as copy:
                    for quest in quests:
                        # Convert to CSV row
                        row = [
                            quest['id'],
                            quest['metadata'],
                            quest['quest_id'],
                            quest['title'],
                            quest['description'],
                            quest['status'],
                            quest['level_min'],
                            quest['level_max'],
                            quest['estimated_duration'],
                            quest['difficulty'],
                            quest['category'],
                            quest['type'],
                            quest['rewards'],
                            quest['prerequisites'],
                            quest['objectives'],
                            quest['dialogue_tree'],
                            quest['branching_paths'],
                            quest['locations'],
                            quest['npcs'],
                            quest['items'],
                            quest['events'],
                            quest['combat_elements'],
                            quest['endings'],
                            quest['technical_requirements'],
                            quest['balance_notes'],
                            quest['qa_checklist'],
                        ]
                        copy.write_row(row)

                # Merge temp table into main table
                cur.execute("""
                    INSERT INTO gameplay.quest_definitions
                    SELECT * FROM temp_quests
                    ON CONFLICT (id) DO UPDATE SET
                        metadata = EXCLUDED.metadata,
                        title = EXCLUDED.title,
                        description = EXCLUDED.description,
                        status = EXCLUDED.status,
                        updated_at = NOW();
                """)

            conn.commit()
            self.stats['successful'] += len(quests)
            self.stats['batches'] += 1

            logger.info(f"Imported batch of {len(quests)} quests")
            return True

        except Exception as e:
            logger.error(f"Failed to import quests batch: {e}")
            if conn:
                conn.rollback()
            self.stats['failed'] += len(quests)
            return False

        finally:
            if conn:
                conn.close()

    def bulk_import_npcs(self, npcs: List[Dict[str, Any]]) -> bool:
        """Bulk import NPCs using optimized PostgreSQL COPY"""
        if not npcs:
            return True

        conn = None
        try:
            conn = self.get_db_connection()

            # Create temporary table for staging
            with conn.cursor() as cur:
                cur.execute("""
                    CREATE TEMP TABLE temp_npcs (
                        LIKE gameplay.npcs INCLUDING ALL
                    ) ON COMMIT DROP;
                """)

                # Use COPY for bulk insert
                with cur.copy("COPY temp_npcs FROM STDIN WITH CSV HEADER") as copy:
                    for npc in npcs:
                        row = [
                            npc['id'],
                            npc['metadata'],
                            npc['npc_id'],
                            npc['name'],
                            npc['description'],
                            npc['role'],
                            npc['location'],
                            npc['faction'],
                            npc['personality'],
                            npc['dialogues'],
                            npc['quests'],
                            npc['relationships'],
                            npc['schedule'],
                            npc['appearance'],
                            npc['combat_stats'],
                            npc['trading_inventory'],
                            npc['technical_requirements'],
                        ]
                        copy.write_row(row)

                # Merge temp table into main table
                cur.execute("""
                    INSERT INTO gameplay.npcs
                    SELECT * FROM temp_npcs
                    ON CONFLICT (id) DO UPDATE SET
                        metadata = EXCLUDED.metadata,
                        name = EXCLUDED.name,
                        description = EXCLUDED.description,
                        updated_at = NOW();
                """)

            conn.commit()
            self.stats['successful'] += len(npcs)
            self.stats['batches'] += 1

            logger.info(f"Imported batch of {len(npcs)} NPCs")
            return True

        except Exception as e:
            logger.error(f"Failed to import NPCs batch: {e}")
            if conn:
                conn.rollback()
            self.stats['failed'] += len(npcs)
            return False

        finally:
            if conn:
                conn.close()

    def import_data_parallel(self, data_type: str, data_files: List[Path]) -> bool:
        """Import data using parallel processing"""
        logger.info(f"Starting parallel import of {len(data_files)} {data_type} files")

        # Split files into batches
        batches = []
        for i in range(0, len(data_files), self.batch_size):
            batches.append(data_files[i:i + self.batch_size])

        logger.info(f"Created {len(batches)} batches of max {self.batch_size} files each")

        # Process batches in parallel
        success_count = 0

        with ThreadPoolExecutor(max_workers=self.workers) as executor:
            if data_type == 'quests':
                futures = [
                    executor.submit(self._process_quest_batch, batch)
                    for batch in batches
                ]
            else:  # npcs
                futures = [
                    executor.submit(self._process_npc_batch, batch)
                    for batch in batches
                ]

            for future in as_completed(futures):
                if future.result():
                    success_count += 1
                else:
                    logger.error("Batch import failed")

        logger.info(f"Completed import: {success_count}/{len(batches)} batches successful")

        # Print final statistics
        self._print_stats()

        return success_count == len(batches)

    def _process_quest_batch(self, quest_files: List[Path]) -> bool:
        """Process a batch of quest files"""
        quests = self.prepare_quest_data(quest_files)
        return self.bulk_import_quests(quests)

    def _process_npc_batch(self, npc_files: List[Path]) -> bool:
        """Process a batch of NPC files"""
        npcs = self.prepare_npc_data(npc_files)
        return self.bulk_import_npcs(npcs)

    def _print_stats(self):
        """Print import statistics"""
        duration = time.time() - self.stats['start_time']
        rps = self.stats['successful'] / duration if duration > 0 else 0

        print(f"\n{'='*60}")
        print("BULK IMPORT STATISTICS")
        print(f"{'='*60}")
        print(f"Total processed:     {self.stats['processed']}")
        print(f"Successfully imported: {self.stats['successful']}")
        print(f"Failed:              {self.stats['failed']}")
        print(f"Batches processed:   {self.stats['batches']}")
        print(".2f"        print(".2f"        print(f"{'='*60}")

def load_db_config() -> Dict[str, str]:
    """Load database configuration from environment"""
    return {
        'host': os.getenv('DB_HOST', 'localhost'),
        'port': os.getenv('DB_PORT', '5432'),
        'database': os.getenv('DB_NAME', 'necpgame'),
        'user': os.getenv('DB_USER', 'postgres'),
        'password': os.getenv('DB_PASSWORD', 'postgres'),
    }

def find_data_files(data_type: str) -> List[Path]:
    """Find data files for import"""
    base_path = Path('knowledge/canon')

    if data_type == 'quests':
        # Look for quest files in various locations
        quest_paths = [
            base_path / 'narrative' / 'quests',
            base_path / 'content' / 'quests',
            Path('infrastructure/liquibase/migrations/gameplay/quests')
        ]

        files = []
        for path in quest_paths:
            if path.exists():
                files.extend(path.glob('**/*.yaml'))

        return files

    elif data_type == 'npcs':
        # Look for NPC files
        npc_paths = [
            base_path / 'narrative' / 'npcs',
            base_path / 'lore' / 'characters'
        ]

        files = []
        for path in npc_paths:
            if path.exists():
                files.extend(path.glob('**/*.yaml'))

        return files

    return []

def main():
    parser = argparse.ArgumentParser(description='Bulk import optimization for 1M+ records')
    parser.add_argument('--type', choices=['quests', 'npcs'], required=True,
                       help='Type of data to import')
    parser.add_argument('--batch-size', type=int, default=1000,
                       help='Batch size for processing (default: 1000)')
    parser.add_argument('--workers', type=int, default=4,
                       help='Number of parallel workers (default: 4)')
    parser.add_argument('--dry-run', action='store_true',
                       help='Analyze files without importing')

    args = parser.parse_args()

    # Load configuration
    db_config = load_db_config()

    # Find data files
    data_files = find_data_files(args.type)
    if not data_files:
        logger.error(f"No {args.type} files found")
        sys.exit(1)

    logger.info(f"Found {len(data_files)} {args.type} files to process")

    if args.dry_run:
        logger.info("DRY RUN MODE - No actual import will be performed")
        logger.info(f"Would process {len(data_files)} files in batches of {args.batch_size}")
        return

    # Create importer
    importer = BulkDataImporter(db_config, args.batch_size, args.workers)

    # Start import
    success = importer.import_data_parallel(args.type, data_files)

    sys.exit(0 if success else 1)

if __name__ == '__main__':
    main()
