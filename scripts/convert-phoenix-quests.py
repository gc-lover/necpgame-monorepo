#!/usr/bin/env python3
"""
Скрипт для автоматического добавления quest_definition секций
к квестам Phoenix, у которых их нет.
"""

import os
import yaml
from pathlib import Path

def extract_quest_info_from_summary(summary):
    """Извлекает информацию о квесте из секции summary"""
    problem = summary.get('problem', '')
    goal = summary.get('goal', '')
    essence = summary.get('essence', '')
    key_points = summary.get('key_points', [])

    # Определяем тип квеста по содержимому
    quest_type = 'side'  # по умолчанию
    level_min = 1

    # Анализируем key_points для определения параметров
    for point in key_points:
        point_str = str(point).lower()
        if 'main' in point_str or 'faction' in point_str:
            quest_type = 'faction' if 'faction' in point_str else 'main'
        if 'extreme' in point_str:
            level_min = 25
        elif 'hard' in point_str:
            level_min = 15
        elif 'medium' in point_str:
            level_min = 5
        elif 'easy' in point_str:
            level_min = 1

    # Определяем награды на основе описания
    experience = 300
    money = {'min': 50, 'max': 200}

    if 'extreme' in str(key_points):
        experience = 1000
        money = {'min': 1000, 'max': 5000}
    elif 'hard' in str(key_points):
        experience = 800
        money = {'min': 500, 'max': 2000}
    elif 'medium' in str(key_points):
        experience = 500
        money = {'min': 200, 'max': 800}

    return {
        'quest_type': quest_type,
        'level_min': level_min,
        'level_max': None,
        'experience': experience,
        'money': money
    }

def add_quest_definition_to_file(filepath):
    """Добавляет quest_definition секцию к YAML файлу"""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        # Проверяем, есть ли уже quest_definition
        if 'quest_definition:' in content:
            print(f"Файл {filepath} уже имеет quest_definition, пропускаем")
            return False

        # Загружаем YAML
        data = yaml.safe_load(content)

        if 'summary' not in data:
            print(f"Файл {filepath} не имеет секции summary, пропускаем")
            return False

        # Извлекаем информацию из summary
        quest_info = extract_quest_info_from_summary(data['summary'])

        # Создаем quest_definition секцию
        quest_definition = {
            'quest_type': quest_info['quest_type'],
            'level_min': quest_info['level_min'],
            'level_max': quest_info['level_max'],
            'requirements': {
                'required_quests': [],
                'required_flags': [],
                'required_reputation': {},
                'required_items': []
            },
            'objectives': [
                {
                    'id': 'main_objective',
                    'text': f"Выполнить основную цель квеста: {data['summary'].get('goal', 'Неизвестно')[:100]}..."
                }
            ],
            'rewards': {
                'experience': quest_info['experience'],
                'money': quest_info['money'],
                'items': []
            }
        }

        # Добавляем quest_definition в data
        data['quest_definition'] = quest_definition

        # Сохраняем файл
        with open(filepath, 'w', encoding='utf-8') as f:
            yaml.safe_dump(data, f, default_flow_style=False, allow_unicode=True, sort_keys=False)

        print(f"Добавлена quest_definition к файлу {filepath}")
        return True

    except Exception as e:
        print(f"Ошибка при обработке файла {filepath}: {e}")
        return False

def main():
    """Основная функция"""
    phoenix_dir = Path("knowledge/canon/lore/timeline-author/quests/america/phoenix/2020-2029")

    if not phoenix_dir.exists():
        print(f"Директория {phoenix_dir} не найдена")
        return

    processed_count = 0

    # Обрабатываем все YAML файлы в директории
    for yaml_file in phoenix_dir.glob("*.yaml"):
        if add_quest_definition_to_file(yaml_file):
            processed_count += 1

    print(f"Обработано файлов: {processed_count}")

if __name__ == "__main__":
    main()
