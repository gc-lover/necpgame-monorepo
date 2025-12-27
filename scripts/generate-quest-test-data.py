#!/usr/bin/env python3
"""
Quest Test Data Generator
Generates sample quest data for testing the dynamic quests service.

Usage:
    python scripts/generate-quest-test-data.py --output-dir knowledge/canon/narrative/quests/ --count 5

Author: NECPGAME Backend Agent
Issue: #146074975
"""

import argparse
import json
import os
import random
import yaml
from datetime import datetime
from pathlib import Path


class QuestTestDataGenerator:
    """Generates test quest data with branching logic."""

    def __init__(self, seed=None):
        self.random = random.Random(seed)
        self.locations = [
            "Tokyo", "Vancouver", "Singapore", "Shanghai", "Seoul",
            "Hong Kong", "Bangkok", "Taipei", "Manila", "Jakarta"
        ]
        self.time_periods = [
            "2020-2029", "2030-2039", "2040-2060", "2061-2077", "2078-2093"
        ]
        self.difficulties = ["easy", "medium", "hard", "extreme"]
        self.moral_alignments = ["good", "neutral", "evil"]
        self.factions = ["corporate", "street", "humanity"]

    def generate_quest_id(self, location, time_period):
        """Generate unique quest ID."""
        return f"test-{location.lower().replace(' ', '-')}-{time_period.replace('-', '')}-{self.random.randint(1000, 9999)}"

    def generate_choice_point(self, sequence, theme="corporate"):
        """Generate a choice point with options."""
        choice_point_id = f"choice_{sequence}"
        title = self.get_choice_title(theme, sequence)
        description = self.get_choice_description(theme, sequence)

        choices = []
        for i in range(self.random.randint(2, 4)):
            choice = {
                "id": f"option_{i+1}",
                "text": self.get_choice_text(theme, i),
                "description": self.get_choice_desc(theme, i),
                "consequences": self.generate_consequences(),
                "requirements": {},
                "unlocks": self.get_unlocks(),
                "risk_level": self.random.choice(["low", "medium", "high"]),
                "moral_alignment": self.random.choice(self.moral_alignments)
            }
            choices.append(choice)

        return {
            "id": choice_point_id,
            "sequence": sequence,
            "title": title,
            "description": description,
            "context": self.get_context(theme),
            "choices": choices,
            "time_limit": None,
            "critical": self.random.choice([True, False])
        }

    def generate_consequences(self):
        """Generate random consequences for a choice."""
        consequences = []
        consequence_types = ["reputation", "item", "experience"]

        for _ in range(self.random.randint(1, 3)):
            consequence_type = self.random.choice(consequence_types)
            if consequence_type == "reputation":
                target = self.random.choice(self.factions)
                value = self.random.randint(-50, 50)
                consequences.append({
                    "type": "reputation",
                    "target": target,
                    "value": value,
                    "probability": 1.0,
                    "description": f"Изменение репутации {target}: {value:+d}"
                })
            elif consequence_type == "item":
                consequences.append({
                    "type": "item",
                    "target": "inventory",
                    "value": f"test_item_{self.random.randint(1, 100)}",
                    "probability": 0.8,
                    "description": "Получен тестовый предмет"
                })
            elif consequence_type == "experience":
                value = self.random.randint(100, 1000)
                consequences.append({
                    "type": "experience",
                    "target": "player",
                    "value": value,
                    "probability": 1.0,
                    "description": f"Получено {value} опыта"
                })

        return consequences

    def get_choice_title(self, theme, sequence):
        """Get choice point title based on theme."""
        titles = {
            "corporate": ["Корпоративное решение", "Бизнес выбор", "Экономическая дилемма"],
            "street": ["Уличный выбор", "Гангстерская дилемма", "Территориальный конфликт"],
            "personal": ["Личный выбор", "Моральная дилемма", "Жизненное решение"]
        }
        return self.random.choice(titles.get(theme, ["Выбор"]))

    def get_choice_description(self, theme, sequence):
        """Get choice point description."""
        return f"Критический момент в квесте, требующий принятия решения."

    def get_choice_text(self, theme, index):
        """Get choice option text."""
        options = [
            "Выбрать безопасный путь",
            "Рискнуть ради большой награды",
            "Помочь другим",
            "Приоритизировать личную выгоду",
            "Следовать этическим принципам",
            "Игнорировать правила"
        ]
        return options[index % len(options)]

    def get_choice_desc(self, theme, index):
        """Get choice option description."""
        descs = [
            "Консервативный подход с минимальным риском",
            "Агрессивная стратегия с потенциально высокой наградой",
            "Альтруистический выбор, помогающий окружающим",
            "Эгоистичный выбор, фокусирующийся на личной выгоде",
            "Моральный выбор, следующий этическим нормам",
            "Прагматичный выбор, игнорирующий ограничения"
        ]
        return descs[index % len(descs)]

    def get_context(self, theme):
        """Get narrative context."""
        return "Ваше решение повлияет на развитие сюжета и отношения с фракциями."

    def get_unlocks(self):
        """Get potential unlocks."""
        unlocks = []
        if self.random.random() < 0.3:
            unlocks.append(f"quest_branch_{self.random.randint(1, 10)}")
        if self.random.random() < 0.2:
            unlocks.append(f"achievement_{self.random.randint(1, 50)}")
        return unlocks

    def generate_ending_variation(self, ending_id, title, description):
        """Generate quest ending variation."""
        return {
            "id": ending_id,
            "title": title,
            "description": description,
            "requirements": [f"choice_{self.random.randint(1, 5)}_option_{self.random.randint(1, 3)}"],
            "rewards": [
                {
                    "type": "experience",
                    "value": self.random.randint(500, 2000)
                },
                {
                    "type": "currency",
                    "value": self.random.randint(1000, 5000)
                }
            ],
            "narrative": f"Квест завершается {description.lower()}. Ваши решения привели к этому результату."
        }

    def generate_quest(self):
        """Generate a complete quest with branching logic."""
        location = self.random.choice(self.locations)
        time_period = self.random.choice(self.time_periods)
        quest_id = self.generate_quest_id(location, time_period)
        theme = self.random.choice(["corporate", "street", "personal"])

        # Generate choice points
        choice_points = []
        num_choices = self.random.randint(3, 6)
        for i in range(num_choices):
            choice_points.append(self.generate_choice_point(i + 1, theme))

        # Generate ending variations
        ending_variations = [
            self.generate_ending_variation("good_ending", "Положительная концовка",
                                         "История завершается успехом и гармонией"),
            self.generate_ending_variation("bad_ending", "Отрицательная концовка",
                                         "История завершается неудачей и конфликтом"),
            self.generate_ending_variation("neutral_ending", "Нейтральная концовка",
                                         "История завершается компромиссом")
        ]

        # Generate reputation impacts
        reputation_impacts = []
        for faction in self.factions:
            reputation_impacts.append({
                "faction": faction,
                "change": self.random.randint(-30, 30),
                "description": f"Влияние на репутацию {faction}"
            })

        quest = {
            "metadata": {
                "id": quest_id,
                "title": self.generate_title(location, theme),
                "english_title": self.generate_english_title(location, theme),
                "type": f"{theme}_{'investigation' if theme == 'corporate' else 'combat' if theme == 'street' else 'drama'}_branching",
                "location": location,
                "time_period": time_period,
                "difficulty": self.random.choice(self.difficulties),
                "estimated_duration": f"{self.random.randint(60, 180)} минут",
                "player_level": f"{self.random.randint(10, 30)}-{self.random.randint(35, 60)}",
                "tags": self.generate_tags(location, theme)
            },
            "quest_definition": {
                "status": "active",
                "level_min": self.random.randint(10, 30),
                "level_max": self.random.randint(35, 60),
                "rewards": {
                    "xp": self.random.randint(1000, 3000),
                    "currency": self.random.randint(2000, 6000),
                    "reputation": self.generate_reputation_rewards(),
                    "unlocks": {
                        "achievements": [f"branching_quest_{self.random.randint(1, 10)}"],
                        "flags": [f"quest_completed_{quest_id}"],
                        "items": [f"quest_reward_{self.random.randint(1, 20)}"]
                    }
                },
                "choice_points": choice_points,
                "ending_variations": ending_variations,
                "reputation_impacts": reputation_impacts
            }
        }

        return quest

    def generate_title(self, location, theme):
        """Generate Russian quest title."""
        location_names = {
            "Tokyo": "Токио",
            "Vancouver": "Ванкувер",
            "Singapore": "Сингапур",
            "Shanghai": "Шанхай"
        }

        theme_words = {
            "corporate": ["Корпорация", "Бизнес", "Деньги", "Власть"],
            "street": ["Улицы", "Банды", "Территория", "Конфликт"],
            "personal": ["Личное", "Судьба", "Выбор", "Жизнь"]
        }

        location_ru = location_names.get(location, location)
        theme_word = self.random.choice(theme_words.get(theme, ["Приключение"]))

        return f"{theme_word} {location_ru}"

    def generate_english_title(self, location, theme):
        """Generate English quest title."""
        theme_words = {
            "corporate": ["Corporate", "Business", "Money", "Power"],
            "street": ["Street", "Gang", "Territory", "Conflict"],
            "personal": ["Personal", "Fate", "Choice", "Life"]
        }

        theme_word = self.random.choice(theme_words.get(theme, ["Adventure"]))
        return f"{theme_word} {location}"

    def generate_tags(self, location, theme):
        """Generate quest tags."""
        base_tags = [location.lower(), theme]
        additional_tags = ["branching", "choices", "consequences", "narrative"]

        tags = base_tags + self.random.sample(additional_tags, self.random.randint(1, 3))
        return tags

    def generate_reputation_rewards(self):
        """Generate reputation rewards."""
        rewards = {}
        for faction in self.random.sample(self.factions, self.random.randint(1, 3)):
            rewards[faction] = self.random.randint(100, 500)
        return rewards


def main():
    parser = argparse.ArgumentParser(description="Generate test quest data")
    parser.add_argument("--output-dir", required=True, help="Output directory for YAML files")
    parser.add_argument("--count", type=int, default=5, help="Number of quests to generate")
    parser.add_argument("--seed", type=int, help="Random seed for reproducible results")

    args = parser.parse_args()

    # Create output directory if it doesn't exist
    output_dir = Path(args.output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)

    # Initialize generator
    generator = QuestTestDataGenerator(seed=args.seed)

    print(f"Generating {args.count} test quests...")

    for i in range(args.count):
        quest = generator.generate_quest()
        filename = f"{quest['metadata']['id']}.yaml"
        filepath = output_dir / filename

        with open(filepath, 'w', encoding='utf-8') as f:
            yaml.dump(quest, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

        print(f"Generated: {filename}")

    print(f"\nCompleted! Generated {args.count} test quests in {args.output_dir}")


if __name__ == "__main__":
    main()
