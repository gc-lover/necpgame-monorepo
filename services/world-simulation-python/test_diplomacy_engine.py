#!/usr/bin/env python3
"""
Test script for Diplomacy Engine
Demonstrates FreeCiv-style Love/Fear diplomatic relations (#2279)
"""

import logging
from diplomacy_engine import DiplomacyEngine, DiplomaticState

# Configure logging
logging.basicConfig(level=logging.INFO)

def test_diplomacy_engine():
    """Test the diplomacy engine with various scenarios"""

    print("Testing Diplomacy Engine - FreeCiv Love/Fear Logic")
    print("=" * 60)

    # Initialize engine
    engine = DiplomacyEngine()

    # Add factions with different power levels
    print("Adding factions to diplomacy system:")
    factions = [
        ('arastov_family', 8.5, 'Arasaka Family'),
        ('nomad_nomads', 6.2, 'Nomad Clans'),
        ('valentino_gang', 7.1, 'Valentino Gang'),
        ('maelstrom_gang', 9.3, 'Maestro Gang'),
        ('tyger_claws', 7.8, 'Tyger Claws'),
    ]

    for faction_id, power, name in factions:
        engine.add_faction(faction_id, power, name)
        print(f"  - {name}: Military Power {power}")

    print()

    # Initial evaluation
    print("Initial diplomatic evaluation:")
    results = engine.evaluate_all_relations()
    for relation_key, state in results.items():
        print(f"  {relation_key}: {state.value}")
    print()

    # Simulate diplomatic events
    print("Simulating diplomatic events:")

    # 1. Maelstrom declares war on weaker Valentino
    print("  1. Maelstrom declares war on Valentino...")
    engine.trigger_diplomatic_event(
        'war_declaration',
        ['maelstrom_gang', 'valentino_gang'],
        {'aggressor_power': 9.3, 'defender_power': 7.1}
    )

    # 2. Arasaka forms alliance with Tyger Claws
    print("  2. Arasaka forms alliance with Tyger Claws...")
    engine.trigger_diplomatic_event(
        'alliance_formed',
        ['arastov_family', 'tyger_claws'],
        {'duration_days': 365, 'mutual_defense': True}
    )

    # 3. Nomads betray Arasaka
    print("  3. Nomads betray Arasaka...")
    engine.trigger_diplomatic_event(
        'betrayal',
        ['nomad_nomads', 'arastov_family'],
        {'broken_treaty': 'trade_agreement', 'severity': 'high'}
    )

    print()

    # Re-evaluate after events
    print("Diplomatic evaluation after events:")
    results = engine.evaluate_all_relations()
    for relation_key, state in results.items():
        print(f"  {relation_key}: {state.value}")
    print()

    # Show diplomacy summary
    print("Diplomacy Summary:")
    summary = engine.get_diplomacy_summary()
    print(f"  Total factions: {summary['total_factions']}")
    print(f"  Diplomatic relations: {summary['relations_count']}")
    print(f"  State distribution: {summary['state_distribution']}")
    print(f"  Active treaties: {summary['active_treaties']}")
    print()

    # Simulate time passage
    print("Simulating 30 days of diplomatic evolution...")
    engine.simulate_time_step(days=30)

    print("Final diplomatic evaluation:")
    results = engine.evaluate_all_relations()
    for relation_key, state in results.items():
        print(f"  {relation_key}: {state.value}")
    print()

    print("Diplomacy engine test completed!")
    print(f"Total diplomacy log entries: {len(engine.diplomacy_log)}")
    print(f"Total global events processed: {len(engine.global_events)}")

    # Show recent diplomacy log
    print("\nRecent Diplomacy Log:")
    for i, entry in enumerate(engine.diplomacy_log[-5:]):  # Last 5 entries
        print(f"  {i+1}. {entry['faction_a']} vs {entry['faction_b']}: {entry['old_state']} -> {entry['new_state']}")

if __name__ == "__main__":
    test_diplomacy_engine()