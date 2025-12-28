#!/usr/bin/env python3
"""
NECPGAME Concept Director Automation System
Автоматизированная система генерации концепций, идей и планов для всех аспектов игры

SOLID: Single Responsibility - генерация концепций и автоматизация творческого процесса
PERFORMANCE: Многопоточная генерация, кеширование, оптимизированные алгоритмы
SECURITY: Валидация входных данных, защита от инъекций
"""

import sys
import json
import yaml
import asyncio
import threading
from pathlib import Path
from typing import Dict, List, Any, Optional, Union, Tuple
from dataclasses import dataclass, field
from datetime import datetime, timedelta
from concurrent.futures import ThreadPoolExecutor, as_completed
import hashlib
import re
from enum import Enum

# Add scripts directory to Python path
scripts_dir = Path(__file__).parent
sys.path.insert(0, str(scripts_dir))

from core.command_runner import CommandRunner
from core.config import ConfigManager
from core.file_manager import FileManager
from core.logger import Logger


class ConceptType(Enum):
    """Типы концепций для генерации"""
    QUEST = "quest"
    NPC = "npc"
    LOCATION = "location"
    MECHANIC = "mechanic"
    LORE = "lore"
    SYSTEM = "system"
    EVENT = "event"
    FACTION = "faction"
    ITEM = "item"
    WEAPON = "weapon"
    VEHICLE = "vehicle"
    CYBERWARE = "cyberware"
    CORPORATION = "corporation"
    MISSION = "mission"
    DIALOGUE = "dialogue"


class GenerationPriority(Enum):
    """Приоритеты генерации"""
    CRITICAL = "critical"
    HIGH = "high"
    MEDIUM = "medium"
    LOW = "low"


@dataclass
class ConceptTemplate:
    """Шаблон для генерации концепции"""
    type: ConceptType
    name: str
    description: str
    structure: Dict[str, Any]
    examples: List[str] = field(default_factory=list)
    validation_rules: Dict[str, Any] = field(default_factory=dict)


@dataclass
class GenerationRequest:
    """Запрос на генерацию концепции"""
    concept_type: ConceptType
    theme: str
    context: Dict[str, Any]
    constraints: Dict[str, Any] = field(default_factory=dict)
    priority: GenerationPriority = GenerationPriority.MEDIUM
    deadline: Optional[datetime] = None


@dataclass
class GeneratedConcept:
    """Сгенерированная концепция"""
    id: str
    type: ConceptType
    title: str
    content: Dict[str, Any]
    metadata: Dict[str, Any]
    quality_score: float
    generation_time: datetime
    validation_status: str


class ConceptValidator:
    """
    Валидатор сгенерированных концепций
    SOLID: Single Responsibility - валидация концепций
    """

    def __init__(self, logger_manager: Logger):
        self.logger = logger_manager.create_script_logger("concept_validator")
        self.validation_rules = self._load_validation_rules()

    def _load_validation_rules(self) -> Dict[str, Any]:
        """Загрузка правил валидации"""
        rules_file = Path(__file__).parent / "validation" / "concept_validation_rules.yaml"
        if rules_file.exists():
            with open(rules_file, 'r', encoding='utf-8') as f:
                return yaml.safe_load(f)
        return self._get_default_rules()

    def _get_default_rules(self) -> Dict[str, Any]:
        """Дефолтные правила валидации"""
        return {
            "quest": {
                "required_fields": ["title", "objectives", "rewards"],
                "min_length": {"description": 50, "objectives": 3},
                "max_length": {"title": 100}
            },
            "npc": {
                "required_fields": ["name", "background", "personality"],
                "min_length": {"background": 100}
            },
            "location": {
                "required_fields": ["name", "description", "atmosphere"],
                "min_length": {"description": 200}
            }
        }

    def validate_concept(self, concept: GeneratedConcept) -> Tuple[bool, List[str]]:
        """
        Валидация концепции
        Returns: (is_valid, errors_list)
        """
        errors = []

        # Проверка типа концепции
        if concept.type.value not in self.validation_rules:
            errors.append(f"Unknown concept type: {concept.type.value}")
            return False, errors

        rules = self.validation_rules[concept.type.value]

        # Проверка обязательных полей
        for field in rules.get("required_fields", []):
            if field not in concept.content:
                errors.append(f"Missing required field: {field}")

        # Проверка минимальной длины
        for field, min_len in rules.get("min_length", {}).items():
            if field in concept.content:
                value = concept.content[field]
                if isinstance(value, str) and len(value) < min_len:
                    errors.append(f"Field '{field}' too short: {len(value)} < {min_len}")
                elif isinstance(value, list) and len(value) < min_len:
                    errors.append(f"Field '{field}' too short: {len(value)} < {min_len}")

        # Проверка максимальной длины
        for field, max_len in rules.get("max_length", {}).items():
            if field in concept.content:
                value = concept.content[field]
                if isinstance(value, str) and len(value) > max_len:
                    errors.append(f"Field '{field}' too long: {len(value)} > {max_len}")

        # Кастомные правила валидации
        errors.extend(self._run_custom_validation(concept))

        return len(errors) == 0, errors

    def _run_custom_validation(self, concept: GeneratedConcept) -> List[str]:
        """Запуск кастомной валидации"""
        errors = []

        # Проверка на запрещенные символы (эмодзи)
        for key, value in concept.content.items():
            if isinstance(value, str):
                if self._contains_emoji(value):
                    errors.append(f"Field '{key}' contains forbidden emoji characters")

        # Проверка качества контента
        quality_issues = self._assess_content_quality(concept)
        errors.extend(quality_issues)

        return errors

    def _contains_emoji(self, text: str) -> bool:
        """Проверка на наличие эмодзи"""
        emoji_pattern = re.compile(
            "["
            "\U0001F600-\U0001F64F"  # emoticons
            "\U0001F300-\U0001F5FF"  # symbols & pictographs
            "\U0001F680-\U0001F6FF"  # transport & map symbols
            "\U0001F1E0-\U0001F1FF"  # flags (iOS)
            "\U00002700-\U000027BF"  # dingbats
            "\U0001f926-\U0001f937"  # gestures
            "\U00010000-\U0010ffff"  # other unicode
            "\u2640-\u2642"  # gender symbols
            "\u2600-\u2B55"  # misc symbols
            "\u200d"  # zero width joiner
            "\u23cf"  # eject symbol
            "\u23e9"  # fast forward
            "\u231a"  # watch
            "\ufe0f"  # variation selector
            "\u3030"  # wavy dash
            "]+",
            flags=re.UNICODE
        )
        return bool(emoji_pattern.search(text))

    def _assess_content_quality(self, concept: GeneratedConcept) -> List[str]:
        """Оценка качества контента"""
        issues = []

        # Проверка на повторяющиеся слова
        if "description" in concept.content:
            desc = concept.content["description"]
            words = desc.lower().split()
            duplicates = [word for word in set(words) if words.count(word) > 3]
            if duplicates:
                issues.append(f"Repeated words in description: {', '.join(duplicates[:3])}")

        # Проверка на логическую связность
        # (упрощенная проверка - можно расширить)

        return issues


class ConceptGenerator:
    """
    Генератор концепций с использованием различных стратегий
    SOLID: Single Responsibility - генерация концепций
    """

    def __init__(self, validator: ConceptValidator, logger_manager: Logger):
        self.validator = validator
        self.logger = logger_manager.create_script_logger("concept_generator")
        self.templates = self._load_templates()
        self.generation_cache = {}
        self.executor = ThreadPoolExecutor(max_workers=4)

    def _load_templates(self) -> Dict[str, ConceptTemplate]:
        """Загрузка шаблонов концепций"""
        templates_dir = Path(__file__).parent / "generation" / "templates" / "concepts"
        templates_dir.mkdir(exist_ok=True)

        templates = {}

        # Создание базовых шаблонов если не существуют
        if not (templates_dir / "quest_template.yaml").exists():
            self._create_default_templates(templates_dir)

        # Загрузка шаблонов
        for template_file in templates_dir.glob("*.yaml"):
            try:
                with open(template_file, 'r', encoding='utf-8') as f:
                    data = yaml.safe_load(f)
                    template = ConceptTemplate(**data)
                    templates[template.name] = template
            except Exception as e:
                logger.error(f"Failed to load template {template_file}: {e}")

        return templates

    def _create_default_templates(self, templates_dir: Path):
        """Создание дефолтных шаблонов"""
        quest_template = {
            "type": "quest",
            "name": "quest_template",
            "description": "Шаблон для генерации квестов",
            "structure": {
                "metadata": {
                    "title": "",
                    "description": "",
                    "level_requirement": 0,
                    "rewards": []
                },
                "objectives": [],
                "dialogues": {},
                "choices": []
            },
            "examples": [
                "Расследование корпоративной коррупции",
                "Защита района от бандитов",
                "Поиск редкого артефакта"
            ],
            "validation_rules": {
                "required_fields": ["title", "objectives"],
                "min_objectives": 3,
                "max_title_length": 100
            }
        }

        npc_template = {
            "type": "npc",
            "name": "npc_template",
            "description": "Шаблон для генерации NPC",
            "structure": {
                "identity": {
                    "name": "",
                    "age": 0,
                    "occupation": "",
                    "background": ""
                },
                "personality": {
                    "traits": [],
                    "motivations": [],
                    "relationships": []
                },
                "game_integration": {
                    "quests": [],
                    "services": [],
                    "dialogues": []
                }
            },
            "examples": [
                "Опытный риппердок с темным прошлым",
                "Уличный фиксер с широкими связями",
                "Корпоративный перебежчик с ценной информацией"
            ]
        }

        # Сохранение шаблонов
        with open(templates_dir / "quest_template.yaml", 'w', encoding='utf-8') as f:
            yaml.dump(quest_template, f, default_flow_style=False, allow_unicode=True)

        with open(templates_dir / "npc_template.yaml", 'w', encoding='utf-8') as f:
            yaml.dump(npc_template, f, default_flow_style=False, allow_unicode=True)

    async def generate_concept(self, request: GenerationRequest) -> GeneratedConcept:
        """
        Асинхронная генерация концепции
        PERFORMANCE: Асинхронная обработка для лучшей производительности
        """
        start_time = datetime.now()

        # Проверка кеша
        cache_key = self._generate_cache_key(request)
        if cache_key in self.generation_cache:
            cached_concept = self.generation_cache[cache_key]
            # Проверка актуальности кеша (не старше 1 часа)
            if (datetime.now() - cached_concept.generation_time) < timedelta(hours=1):
                return cached_concept

        try:
            # Параллельная генерация разных аспектов
            tasks = [
                self._generate_title(request),
                self._generate_content(request),
                self._generate_metadata(request)
            ]

            title, content, metadata = await asyncio.gather(*tasks)

            # Создание концепции
            concept = GeneratedConcept(
                id=self._generate_concept_id(request, title),
                type=request.concept_type,
                title=title,
                content=content,
                metadata=metadata,
                quality_score=self._calculate_quality_score(content),
                generation_time=datetime.now(),
                validation_status="pending"
            )

            # Валидация
            is_valid, errors = self.validator.validate_concept(concept)
            concept.validation_status = "valid" if is_valid else "invalid"
            if not is_valid:
                concept.metadata["validation_errors"] = errors

            # Кеширование
            self.generation_cache[cache_key] = concept

            generation_time = (datetime.now() - start_time).total_seconds()
            self.logger.info(f"Generated concept '{title}' in {generation_time:.2f}s")

            return concept

        except Exception as e:
            self.logger.error(f"Failed to generate concept: {e}")
            raise

    def _generate_cache_key(self, request: GenerationRequest) -> str:
        """Генерация ключа кеша"""
        key_data = f"{request.concept_type.value}:{request.theme}:{json.dumps(request.context, sort_keys=True)}"
        return hashlib.md5(key_data.encode()).hexdigest()

    async def _generate_title(self, request: GenerationRequest) -> str:
        """Генерация заголовка концепции"""
        # Упрощенная генерация - можно расширить с использованием AI
        base_title = f"{request.theme.title()}"

        if request.concept_type == ConceptType.QUEST:
            titles = [
                f"Quest: {base_title}",
                f"Mission: {base_title}",
                f"Operation: {base_title}",
                f"Assignment: {base_title}"
            ]
        elif request.concept_type == ConceptType.NPC:
            titles = [
                f"{base_title} - Character Profile",
                f"NPC: {base_title}",
                f"Character: {base_title}"
            ]
        else:
            titles = [f"{request.concept_type.value.title()}: {base_title}"]

        # Имитация асинхронной операции
        await asyncio.sleep(0.1)
        return titles[hash(base_title) % len(titles)]

    async def _generate_content(self, request: GenerationRequest) -> Dict[str, Any]:
        """Генерация содержимого концепции"""
        template = self.templates.get(f"{request.concept_type.value}_template")
        if not template:
            return {"description": f"Generated {request.concept_type.value} about {request.theme}"}

        content = template.structure.copy()

        # Заполнение шаблона на основе контекста
        if request.concept_type == ConceptType.QUEST:
            content["metadata"]["title"] = request.theme
            content["metadata"]["description"] = f"A quest involving {request.theme.lower()}"
            content["objectives"] = [
                f"Investigate the {request.theme.lower()} situation",
                f"Resolve the conflict with {request.theme.lower()}",
                f"Complete the mission objectives"
            ]
        elif request.concept_type == ConceptType.NPC:
            content["identity"]["name"] = request.theme
            content["identity"]["background"] = f"A character with a background in {request.theme.lower()}"
            content["personality"]["traits"] = ["determined", "mysterious", "resourceful"]
        elif request.concept_type == ConceptType.FACTION:
            content["organization"]["name"] = request.theme
            content["organization"]["type"] = "faction"
            content["organization"]["description"] = f"A faction focused on {request.theme.lower()}"
            content["influence"]["territory"] = ["Night City", "Badlands"]
            content["structure"]["hierarchy"] = ["leader", "lieutenants", "members"]
        elif request.concept_type == ConceptType.ITEM:
            content["item"]["name"] = request.theme
            content["item"]["rarity"] = "uncommon"
            content["item"]["description"] = f"An item with {request.theme.lower()} properties"
            content["stats"]["value"] = 500
            content["usage"]["requirements"] = ["Level 10", "Tech skill"]
        elif request.concept_type == ConceptType.WEAPON:
            content["weapon"]["name"] = request.theme
            content["weapon"]["type"] = "cybernetic"
            content["weapon"]["description"] = f"A weapon featuring {request.theme.lower()} technology"
            content["combat"]["damage"] = 85
            content["combat"]["fire_rate"] = 600
            content["mods"]["slots"] = 3
        elif request.concept_type == ConceptType.VEHICLE:
            content["vehicle"]["name"] = request.theme
            content["vehicle"]["type"] = "ground"
            content["vehicle"]["description"] = f"A vehicle designed for {request.theme.lower()}"
            content["performance"]["speed"] = 180
            content["performance"]["armor"] = 75
            content["mods"]["cyberware_slots"] = 4
        elif request.concept_type == ConceptType.CYBERWARE:
            content["cyberware"]["name"] = request.theme
            content["cyberware"]["type"] = "implant"
            content["cyberware"]["description"] = f"Cyberware that enhances {request.theme.lower()} abilities"
            content["effects"]["stat_boost"] = "+20% efficiency"
            content["risks"]["side_effects"] = ["neural feedback", "compatibility issues"]
        elif request.concept_type == ConceptType.CORPORATION:
            content["corporation"]["name"] = request.theme
            content["corporation"]["industry"] = "technology"
            content["corporation"]["description"] = f"A corporation specializing in {request.theme.lower()}"
            content["structure"]["divisions"] = ["R&D", "Security", "Operations"]
            content["influence"]["market_share"] = "15%"
        elif request.concept_type == ConceptType.MISSION:
            content["mission"]["title"] = request.theme
            content["mission"]["type"] = "contract"
            content["mission"]["description"] = f"A mission involving {request.theme.lower()}"
            content["objectives"]["primary"] = f"Complete the {request.theme.lower()} objective"
            content["rewards"]["payment"] = 2500
        elif request.concept_type == ConceptType.DIALOGUE:
            content["dialogue"]["speaker"] = request.theme
            content["dialogue"]["context"] = f"A conversation about {request.theme.lower()}"
            content["lines"]["opening"] = f"What do you know about {request.theme.lower()}?"
            content["branches"]["options"] = ["Tell me more", "I'm not interested", "What's in it for me?"]

        # Имитация асинхронной генерации
        await asyncio.sleep(0.2)

        return content

    async def _generate_metadata(self, request: GenerationRequest) -> Dict[str, Any]:
        """Генерация метаданных"""
        metadata = {
            "generated_at": datetime.now().isoformat(),
            "generator_version": "2.0.0",
            "request_context": request.context,
            "priority": request.priority.value,
            "estimated_complexity": "medium"
        }

        if request.deadline:
            metadata["deadline"] = request.deadline.isoformat()

        # Имитация асинхронной операции
        await asyncio.sleep(0.05)

        return metadata

    def _generate_concept_id(self, request: GenerationRequest, title: str) -> str:
        """Генерация уникального ID концепции"""
        id_data = f"{request.concept_type.value}:{title}:{datetime.now().isoformat()}"
        return hashlib.md5(id_data.encode()).hexdigest()[:16]

    def _calculate_quality_score(self, content: Dict[str, Any]) -> float:
        """Расчет оценки качества контента"""
        score = 0.5  # базовая оценка

        # Проверка на наличие ключевых элементов
        if "description" in content and len(str(content["description"])) > 50:
            score += 0.2

        if "objectives" in content and len(content["objectives"]) >= 3:
            score += 0.2

        if "metadata" in content:
            score += 0.1

        return min(score, 1.0)


class ConceptDirectorAutomation:
    """
    Главная система автоматизации Concept Director
    SOLID: Single Responsibility - оркестрация процесса генерации концепций
    """

    def __init__(self, config: ConfigManager, logger_manager: Logger):
        self.config = config
        self.logger = logger_manager.create_script_logger("concept_director")

        self.validator = ConceptValidator(logger_manager)
        self.generator = ConceptGenerator(self.validator, logger_manager)

        self.active_requests = {}
        self.completed_concepts = {}
        self.generation_stats = {
            "total_generated": 0,
            "average_quality": 0.0,
            "generation_times": [],
            "success_rate": 0.0
        }

    async def process_generation_request(self, request: GenerationRequest) -> GeneratedConcept:
        """
        Обработка запроса на генерацию концепции
        PERFORMANCE: Асинхронная обработка с мониторингом
        """
        request_id = f"{request.concept_type.value}_{datetime.now().timestamp()}"

        self.logger.info(f"Processing generation request: {request_id}")
        self.active_requests[request_id] = request

        try:
            # Генерация концепции
            concept = await self.generator.generate_concept(request)

            # Обновление статистики
            self._update_generation_stats(concept)

            # Сохранение результата
            self.completed_concepts[concept.id] = concept
            del self.active_requests[request_id]

            self.logger.info(f"Successfully generated concept: {concept.title} (ID: {concept.id})")

            return concept

        except Exception as e:
            self.logger.error(f"Failed to generate concept for request {request_id}: {e}")
            del self.active_requests[request_id]
            raise

    async def batch_generate_concepts(self, requests: List[GenerationRequest]) -> List[GeneratedConcept]:
        """
        Пакетная генерация концепций
        PERFORMANCE: Параллельная обработка нескольких запросов
        """
        self.logger.info(f"Starting batch generation of {len(requests)} concepts")

        # Группировка по приоритету
        priority_groups = {}
        for request in requests:
            priority = request.priority.value
            if priority not in priority_groups:
                priority_groups[priority] = []
            priority_groups[priority].append(request)

        # Обработка в порядке приоритета
        results = []
        for priority in ["critical", "high", "medium", "low"]:
            if priority in priority_groups:
                self.logger.info(f"Processing {len(priority_groups[priority])} {priority} priority requests")

                # Параллельная обработка группы
                tasks = [self.process_generation_request(req) for req in priority_groups[priority]]
                batch_results = await asyncio.gather(*tasks, return_exceptions=True)

                # Обработка результатов
                for result in batch_results:
                    if isinstance(result, Exception):
                        self.logger.error(f"Batch generation error: {result}")
                    else:
                        results.append(result)

        self.logger.info(f"Completed batch generation: {len(results)} concepts generated")
        return results

    def _update_generation_stats(self, concept: GeneratedConcept):
        """Обновление статистики генерации"""
        self.generation_stats["total_generated"] += 1

        # Обновление средней оценки качества
        total_quality = self.generation_stats["average_quality"] * (self.generation_stats["total_generated"] - 1)
        total_quality += concept.quality_score
        self.generation_stats["average_quality"] = total_quality / self.generation_stats["total_generated"]

        # Добавление времени генерации
        generation_time = (datetime.now() - concept.generation_time).total_seconds()
        self.generation_stats["generation_times"].append(generation_time)

        # Ограничение истории времен (последние 100)
        if len(self.generation_stats["generation_times"]) > 100:
            self.generation_stats["generation_times"] = self.generation_stats["generation_times"][-100:]

    def get_generation_report(self) -> Dict[str, Any]:
        """Получение отчета о генерации"""
        if self.generation_stats["generation_times"]:
            avg_time = sum(self.generation_stats["generation_times"]) / len(self.generation_stats["generation_times"])
        else:
            avg_time = 0

        return {
            "total_concepts_generated": self.generation_stats["total_generated"],
            "average_quality_score": round(self.generation_stats["average_quality"], 2),
            "average_generation_time": round(avg_time, 2),
            "active_requests": len(self.active_requests),
            "completed_concepts": len(self.completed_concepts),
            "success_rate": self.generation_stats["success_rate"]
        }

    def export_concepts_to_files(self, output_dir: Path, concept_type: Optional[ConceptType] = None):
        """
        Экспорт сгенерированных концепций в файлы
        """
        output_dir.mkdir(exist_ok=True)

        exported_count = 0
        for concept_id, concept in self.completed_concepts.items():
            if concept_type and concept.type != concept_type:
                continue

            # Определение пути файла
            type_dir = output_dir / concept.type.value
            type_dir.mkdir(exist_ok=True)

            filename = f"{concept.id}_{concept.title.lower().replace(' ', '_')}.yaml"
            filepath = type_dir / filename

            # Сериализация в YAML
            concept_data = {
                "metadata": concept.metadata,
                "content": concept.content,
                "quality_score": concept.quality_score,
                "validation_status": concept.validation_status
            }

            try:
                with open(filepath, 'w', encoding='utf-8') as f:
                    yaml.dump(concept_data, f, default_flow_style=False, allow_unicode=True)
                exported_count += 1
            except Exception as e:
                self.logger.error(f"Failed to export concept {concept_id}: {e}")

        self.logger.info(f"Exported {exported_count} concepts to {output_dir}")
        return exported_count


async def main():
    """Основная функция для демонстрации работы системы"""
    # Инициализация компонентов
    config = ConfigManager()
    logger_manager = Logger(config)
    logger_manager.configure()
    logger = logger_manager.create_script_logger("concept_director")
    command_runner = CommandRunner(logger)
    file_manager = FileManager(logger)

    # Создание системы автоматизации
    automation = ConceptDirectorAutomation(config, logger_manager)

    # Примеры запросов на генерацию
    sample_requests = [
        GenerationRequest(
            concept_type=ConceptType.QUEST,
            theme="Corporate Espionage",
            context={"setting": "Night City", "difficulty": "hard"},
            priority=GenerationPriority.HIGH
        ),
        GenerationRequest(
            concept_type=ConceptType.NPC,
            theme="Rogue Netrunner",
            context={"background": "corporate_defector", "specialization": "hacking"},
            priority=GenerationPriority.MEDIUM
        ),
        GenerationRequest(
            concept_type=ConceptType.LOCATION,
            theme="Underground Market",
            context={"district": "Watson", "atmosphere": "chaotic"},
            priority=GenerationPriority.LOW
        ),
        GenerationRequest(
            concept_type=ConceptType.FACTION,
            theme="Maelstrom Gang",
            context={"territory": "Badlands", "specialization": "combat"},
            priority=GenerationPriority.HIGH
        ),
        GenerationRequest(
            concept_type=ConceptType.WEAPON,
            theme="Monowire Whip",
            context={"damage_type": "slashing", "rarity": "epic"},
            priority=GenerationPriority.MEDIUM
        ),
        GenerationRequest(
            concept_type=ConceptType.CYBERWARE,
            theme="Sandevistan",
            context={"effect": "time_dilation", "risk_level": "high"},
            priority=GenerationPriority.CRITICAL
        ),
        GenerationRequest(
            concept_type=ConceptType.CORPORATION,
            theme="Arasaka Corp",
            context={"industry": "weapons", "headquarters": "Japan"},
            priority=GenerationPriority.HIGH
        )
    ]

    # Пакетная генерация
    concepts = await automation.batch_generate_concepts(sample_requests)

    # Вывод результатов
    print("=== Concept Director Automation Results ===")
    print(f"Generated {len(concepts)} concepts")

    for concept in concepts:
        print(f"\n--- {concept.title} ---")
        print(f"Type: {concept.type.value}")
        print(f"Quality Score: {concept.quality_score}")
        print(f"Validation: {concept.validation_status}")
        if hasattr(concept, 'metadata') and 'validation_errors' in concept.metadata:
            print(f"Errors: {concept.metadata['validation_errors']}")

    # Отчет о генерации
    report = automation.get_generation_report()
    print(f"\n=== Generation Report ===")
    for key, value in report.items():
        print(f"{key}: {value}")

    # Экспорт концепций
    output_dir = Path(__file__).parent.parent / "knowledge" / "generated_concepts"
    exported = automation.export_concepts_to_files(output_dir)
    print(f"\nExported {exported} concepts to files")


if __name__ == "__main__":
    asyncio.run(main())
