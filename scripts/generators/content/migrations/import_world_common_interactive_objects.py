#!/usr/bin/env python3
"""
Import world common interactive objects to database
Generates and imports world common interactive objects based on content definitions
"""

import os
import sys
import psycopg2
import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
from typing import Dict, Any, List
import random

def get_random_position_in_world() -> Dict[str, float]:
    """Generate random position across the world"""
    # World-wide coordinates for global distribution
    # Longitude: -180 to 180
    # Latitude: -90 to 90
    lon = random.uniform(-180, 180)
    lat = random.uniform(-90, 90)

    return {
        'longitude': round(lon, 6),
        'latitude': round(lat, 6)
    }

def generate_world_common_interactive_objects(content_data: Dict[str, Any]) -> List[Dict[str, Any]]:
    """Generate specific interactive objects from content definitions"""
    objects = []

    # World zone ID (placeholder - should be real zone from database)
    world_zone_id = "660e8400-e29b-41d4-a716-446655440001"

    for interactive in content_data.get('interactives', []):
        name = interactive['name']
        display_name = interactive.get('display_name', name)
        category = interactive['category']

        # Map content categories to database object types
        object_type_mapping = {
            'faction_control': 'security_terminal',
            'communication': 'security_terminal',
            'medical': 'security_terminal',
            'logistics': 'cargo_container',
            'infrastructure': 'security_terminal',
            'security': 'security_terminal'
        }

        object_type = object_type_mapping.get(category, 'security_terminal')

        # Generate 3-8 instances of each type (fewer than urban since these are world-wide)
        instances_count = random.randint(3, 8)

        for i in range(instances_count):
            position = get_random_position_in_world()

            # Generate zone-specific data based on object type
            zone_specific_data = {}
            state = 'active'
            health = 100
            interaction_type = 'single_use'
            access_level = 'public'

            if object_type == 'security_terminal' and 'blockpost' in name.lower():
                # Faction blockpost
                zone_specific_data = {
                    'faction_type': random.choice(['corporate', 'gang', 'government']),
                    'control_radius': random.randint(200, 1000),
                    'price_modifier': random.randint(5, 30),
                    'security_level': random.randint(1, 5)
                }
                interaction_type = 'reusable'
                access_level = random.choice(['restricted', 'corporate'])

            elif object_type == 'security_terminal' and 'relay' in name.lower():
                # Communication relay
                zone_specific_data = {
                    'relay_type': random.choice(['satellite', 'ground', 'orbital']),
                    'coverage_range': random.randint(1000, 50000),
                    'signal_strength': random.randint(50, 100),
                    'encryption_level': random.randint(1, 5)
                }
                interaction_type = 'reusable'
                access_level = 'restricted'

            elif object_type == 'security_terminal' and 'medical' in name.lower():
                # Medical station
                zone_specific_data = {
                    'station_type': random.choice(['clinic', 'emergency', 'trauma_center']),
                    'capacity': random.randint(5, 50),
                    'equipment_level': random.randint(1, 5),
                    'power_status': random.choice(['nominal', 'backup', 'critical'])
                }
                interaction_type = 'reusable'
                access_level = 'public'

            elif object_type == 'cargo_container':
                # Logistics container
                zone_specific_data = {
                    'container_type': random.choice(['standard', 'secure', 'hazardous']),
                    'cargo_value': random.randint(100, 10000),
                    'security_measures': random.randint(1, 5),
                    'ownership': random.choice(['corporate', 'faction', 'private'])
                }
                access_level = random.choice(['public', 'restricted', 'corporate'])

            # Randomize some properties
            if random.random() < 0.15:  # 15% chance for world objects
                state = random.choice(['damaged', 'hacked', 'inactive', 'overloaded'])
                health = random.randint(20, 95)

            # Generate interaction effects and consequences
            interaction_effects = {}
            failure_consequences = {}

            if object_type == 'security_terminal':
                interaction_effects = {
                    'data_access': True,
                    'control_gain': random.choice([True, False]),
                    'temporary_buff': random.choice(['none', 'stealth_boost', 'hack_bonus', 'health_regen'])
                }
                failure_consequences = {
                    'alarm_triggered': random.choice([True, False]),
                    'faction_alert': random.choice([True, False]),
                    'combat_spawn': random.choice([True, False])
                }
            elif object_type == 'cargo_container':
                interaction_effects = {
                    'loot_acquired': True,
                    'cargo_value': random.randint(50, 5000),
                    'faction_reputation': random.choice([-5, -2, 0, 2, 5])
                }
                failure_consequences = {
                    'trap_triggered': random.choice([True, False]),
                    'cargo_destroyed': random.choice([True, False]),
                    'owner_alerted': random.choice([True, False])
                }

            obj = {
                'id': str(uuid.uuid4()),
                'object_type': object_type,
                'object_subtype': f"{name}_{i+1}",
                'zone_id': world_zone_id,
                'position': f"POINT({position['longitude']} {position['latitude']})",
                'rotation': round(random.uniform(0, 360), 1),
                'zone_name': 'World - Global Network',
                'state': state,
                'health': health,
                'max_health': 100,
                'interaction_type': interaction_type,
                'cooldown_seconds': random.randint(0, 600),  # Longer cooldowns for world objects
                'access_level': access_level,
                'zone_specific_data': json.dumps(zone_specific_data),
                'interaction_effects': json.dumps(interaction_effects),
                'failure_consequences': json.dumps(failure_consequences),
                'created_at': datetime.now().isoformat(),
                'updated_at': datetime.now().isoformat()
            }

            objects.append(obj)

    return objects

def import_objects_to_database(objects: List[Dict[str, Any]], conn_params: Dict[str, str]):
    """Import generated objects to database"""
    try:
        conn = psycopg2.connect(**conn_params)
        cursor = conn.cursor()

        # Create schema if not exists
        cursor.execute("CREATE SCHEMA IF NOT EXISTS interactive")

        # Insert objects
        insert_query = """
        INSERT INTO interactive.interactive_objects (
            id, object_type, object_subtype, zone_id, position, rotation, zone_name,
            state, health, max_health, interaction_type, cooldown_seconds,
            access_level, zone_specific_data, interaction_effects, failure_consequences,
            created_at, updated_at
        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ON CONFLICT (id) DO NOTHING
        """

        inserted_count = 0
        for obj in objects:
            values = (
                obj['id'], obj['object_type'], obj['object_subtype'], obj['zone_id'],
                obj['position'], obj['rotation'], obj['zone_name'], obj['state'],
                obj['health'], obj['max_health'], obj['interaction_type'],
                obj['cooldown_seconds'], obj['access_level'], obj['zone_specific_data'],
                obj['interaction_effects'], obj['failure_consequences'],
                obj['created_at'], obj['updated_at']
            )

            cursor.execute(insert_query, values)
            inserted_count += 1

            if inserted_count % 25 == 0:
                print(f"[PROGRESS] Imported {inserted_count} objects...")

        conn.commit()
        print(f"[SUCCESS] Imported {inserted_count} world common interactive objects to database")

        cursor.close()
        conn.close()

        return True

    except Exception as e:
        print(f"[ERROR] Failed to import objects: {e}")
        if 'conn' in locals():
            conn.rollback()
            conn.close()
        return False

def main():
    print("[START] Importing world common interactive objects to database")

    # Load world common interactives content
    content_file = Path("knowledge/content/interactives/world-common-interactives.yaml")
    if not content_file.exists():
        print(f"[ERROR] Content file not found: {content_file}")
        return False

    try:
        with open(content_file, 'r', encoding='utf-8') as f:
            content_data = yaml.safe_load(f)

        content_section = content_data.get('content', {})
        print(f"[INFO] Loaded content data with {len(content_section.get('interactives', []))} interactive types")

        # Generate objects
        objects = generate_world_common_interactive_objects(content_section)
        print(f"[INFO] Generated {len(objects)} interactive objects")

        # For testing - output generated objects
        if os.getenv('DRY_RUN', 'false').lower() == 'true':
            print("[DRY RUN] Generated objects:")
            print(json.dumps(objects[:3], indent=2, ensure_ascii=False))  # Show first 3
            print(f"[DRY RUN] Total objects generated: {len(objects)}")
            return True

        # Database connection
        conn_params = {
            'host': os.getenv('DB_HOST', 'localhost'),
            'port': os.getenv('DB_PORT', '5432'),
            'database': os.getenv('DB_NAME', 'necpgame'),
            'user': os.getenv('DB_USER', 'postgres'),
            'password': os.getenv('DB_PASSWORD', 'password')
        }

        # Import to database
        success = import_objects_to_database(objects, conn_params)

        if success:
            print("[SUCCESS] World common interactive objects import completed successfully")
            return True
        else:
            print("[ERROR] World common interactive objects import failed")
            return False

    except Exception as e:
        print(f"[ERROR] Import process failed: {e}")
        return False

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1)