#!/usr/bin/env python3
"""
Create missing Chicago quest migrations.
"""

import yaml
from pathlib import Path
import json
from datetime import datetime

def create_chicago_migrations():
    """Create migration files for missing Chicago quests."""

    quests_data = [
        ('quest-002-deep-dish-pizza', 'Чикаго — Deep Dish Pizza', 'Игрок пробует аутентичную чикагскую пиццу, участвует в дегустации и получает бафф насыщения.'),
        ('quest-003-cloud-gate', 'Чикаго — Cloud Gate (The Bean)', 'Игрок посещает знаменитую скульптуру, фотографируется и узнаёт об искусстве Millennium Park.'),
        ('quest-004-lake-michigan', 'Чикаго — Озеро Мичиган (Third Coast)', 'Игрок исследует побережье озера, участвует в водных активностях и узнаёт историю города.'),
        ('quest-005-cubs-wrigley-field', 'Чикаго — Chicago Cubs на Wrigley Field', 'Игрок посещает бейсбольный матч, знакомится с традициями и получает сувениры команды.'),
        ('quest-007-architecture-tour', 'Чикаго — Архитектурный тур по реке', 'Игрок участвует в речном круизе Chicago Architecture Center, узнаёт историю небоскрёбов.'),
        ('quest-008-prohibition-speakeasy', 'Чикаго — Тайные бары эпохи Prohibition', 'Игрок находит скрытые speakeasy, общается с барменами и узнаёт историю подполья.'),
        ('quest-009-blues-jazz-scene', 'Чикаго — Blues & Jazz Scene', 'Игрок посещает джаз-клубы, слушает музыку и знакомится с легендами блюза.'),
        ('quest-011-windy-city-crypto-trading', 'Чикаго 2020-2029 — Крипто-торговля', 'Игрок изучает криптовалютный рынок, торгует и получает инвестиционные баффы.')
    ]

    timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
    output_dir = Path('infrastructure/liquibase/data/gameplay/quests')

    for quest_id, title, description in quests_data:
        file_path = Path(f'knowledge/canon/lore/timeline-author/quests/america/chicago/2020-2029/{quest_id}.yaml')

        # Generate unique ID
        unique_id = str(hash(quest_id + str(datetime.now())))[:16]

        # Create migration content
        migration = f'''databaseChangeLog:
- changeSet:
    id: quests-{quest_id}-migration
    author: content-migration-generator
    changes:
    - insert:
        tableName: gameplay.quest_definitions
        columns:
        - column:
            name: id
            value: '{unique_id}'
        - column:
            name: metadata
            value: '{{"id": "{quest_id}", "version": "1.0.0", "source_file": "knowledge\\\\canon\\\\lore\\\\timeline-author\\\\quests\\\\america\\\\chicago\\\\2020-2029\\\\{quest_id}.yaml"}}'
        - column:
            name: quest_id
            value: {quest_id}
        - column:
            name: title
            value: {title}
        - column:
            name: description
            value: {description}
        - column:
            name: status
            value: active
        - column:
            name: level_min
            value: 1
        - column:
            name: level_max
            value: null
        - column:
            name: rewards
            value: '{{"xp": 1500, "currency": {{"amount": 500, "type": "eddies"}}}}'
        - column:
            name: objectives
            value: '["Complete the quest objectives"]'
'''

        # Write migration file
        migration_file = output_dir / f'data_quests_{quest_id}_migration_{timestamp}.yaml'
        with open(migration_file, 'w', encoding='utf-8') as f:
            f.write(migration)

        print(f'Created: {migration_file.name}')

if __name__ == '__main__':
    create_chicago_migrations()

