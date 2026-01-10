#!/usr/bin/env python3
"""
Proof-of-Concept: Dynamic Quest System Demo
Demonstrates player choice-driven quest mechanics

Issue: #2244
"""

import json
import random
from datetime import datetime
from typing import Dict, List, Optional, Any
from dataclasses import dataclass, asdict

@dataclass
class PlayerChoice:
    choice_id: str
    node_id: str
    choice_text: str
    timestamp: str
    consequences: List[Dict[str, Any]]
    narrative_impact: Dict[str, Any]

@dataclass
class QuestNode:
    node_id: str
    node_type: str
    title: str
    description: str
    choices: List[Dict[str, Any]]
    metadata: Dict[str, Any]

@dataclass
class QuestInstance:
    quest_id: str
    player_id: str
    current_node_id: str
    status: str
    choice_history: List[PlayerChoice]
    player_characteristics: Dict[str, Any]

class DynamicQuestEngine:
    """Simplified Dynamic Quest Engine for demonstration"""

    def __init__(self):
        self.nodes: Dict[str, QuestNode] = {}
        self.active_quests: Dict[str, QuestInstance] = {}
        self.consequence_queue: List[Dict[str, Any]] = []

    def create_quest_template(self):
        """Create a sample dynamic quest template"""

        # Node 1: Initial encounter
        node1 = QuestNode(
            node_id="encounter_1",
            node_type="choice",
            title="Тени в Переулке",
            description="""
В темном переулке Малой Страны вы замечаете подозрительную фигуру.
Человек в потрепанном плаще нервно оглядывается по сторонам.

"Эй, ты выглядишь как человек, которому нужны деньги... или информация?"
- говорит он тихим голосом.
            """,
            choices=[
                {
                    "choice_id": "approach_help",
                    "text": "Подойти и предложить помощь",
                    "conditions": [],
                    "immediate_effects": [
                        {"type": "reputation_change", "target": "street_cred", "value": 5}
                    ],
                    "narrative_branches": ["help_dialogue"]
                },
                {
                    "choice_id": "approach_threaten",
                    "text": "Подойти и пригрозить рассказать о нем стражам",
                    "conditions": [{"type": "stat_check", "stat": "intimidation", "value": 3}],
                    "immediate_effects": [
                        {"type": "reputation_change", "target": "street_cred", "value": -10},
                        {"type": "relationship_change", "target": "informant", "value": -20}
                    ],
                    "narrative_branches": ["threaten_dialogue"]
                },
                {
                    "choice_id": "ignore_walk_away",
                    "text": "Проигнорировать и продолжить путь",
                    "conditions": [],
                    "immediate_effects": [
                        {"type": "quest_end", "reason": "player_ignored"}
                    ],
                    "narrative_branches": ["quest_end"]
                }
            ],
            metadata={
                "difficulty": 0.2,
                "emotional_impact": "medium",
                "world_impact": "local"
            }
        )

        # Node 2: Help dialogue
        node2 = QuestNode(
            node_id="help_dialogue",
            node_type="choice",
            title="Разговор с Информатором",
            description="""
Человек расслабляется, увидев ваше дружелюбное отношение.

"Спасибо, что не позвал стражу. Меня зовут Алекс. Я работаю на подпольную сеть.
У меня есть информация о коррупции в корпорации Arasaka. Но мне нужна помощь..."

Он показывает вам голографическую карту с отмеченными точками.
            """,
            choices=[
                {
                    "choice_id": "accept_mission",
                    "text": "Принять задание по сбору улик",
                    "conditions": [],
                    "immediate_effects": [
                        {"type": "quest_progress", "value": 25},
                        {"type": "item_grant", "item_id": "data_chip", "name": "Зашифрованный чип данных"}
                    ],
                    "narrative_branches": ["mission_accepted"]
                },
                {
                    "choice_id": "ask_for_payment",
                    "text": "Потребовать предоплату",
                    "conditions": [{"type": "stat_check", "stat": "negotiation", "value": 2}],
                    "immediate_effects": [
                        {"type": "currency_grant", "amount": 500, "currency": "eddies"},
                        {"type": "relationship_change", "target": "informant", "value": 10}
                    ],
                    "narrative_branches": ["payment_negotiated"]
                },
                {
                    "choice_id": "decline_politely",
                    "text": "Вежливо отказаться, но предложить встретиться позже",
                    "conditions": [],
                    "immediate_effects": [
                        {"type": "relationship_change", "target": "informant", "value": 5},
                        {"type": "quest_pause", "duration_days": 3}
                    ],
                    "narrative_branches": ["quest_paused"]
                }
            ],
            metadata={
                "difficulty": 0.3,
                "emotional_impact": "medium",
                "world_impact": "local"
            }
        )

        # Store nodes
        self.nodes = {
            "encounter_1": node1,
            "help_dialogue": node2,
            "threaten_dialogue": self._create_threaten_node(),
            "quest_end": self._create_end_node()
        }

    def _create_threaten_node(self) -> QuestNode:
        """Create threaten dialogue node"""
        return QuestNode(
            node_id="threaten_dialogue",
            node_type="narrative",
            title="Конфронтация",
            description="""
Информатор бледнеет и пятится назад.

"Эй, полегче! Я просто хотел поговорить. Ладно, забудь что я здесь был."

Он быстро исчезает в тенях переулка. Вы чувствуете, что упустили важную возможность.
            """,
            choices=[
                {
                    "choice_id": "search_area",
                    "text": "Обыскать место на предмет улик",
                    "conditions": [],
                    "immediate_effects": [
                        {"type": "skill_check", "skill": "perception", "difficulty": 0.6},
                        {"type": "item_chance", "item_id": "abandoned_data_chip", "chance": 0.3}
                    ],
                    "narrative_branches": ["quest_end"]
                },
                {
                    "choice_id": "continue_path",
                    "text": "Продолжить путь как ни в чем не бывало",
                    "conditions": [],
                    "immediate_effects": [
                        {"type": "quest_end", "reason": "opportunity_missed"}
                    ],
                    "narrative_branches": ["quest_end"]
                }
            ],
            metadata={
                "difficulty": 0.4,
                "emotional_impact": "low",
                "world_impact": "none"
            }
        )

    def _create_end_node(self) -> QuestNode:
        """Create quest end node"""
        return QuestNode(
            node_id="quest_end",
            node_type="end",
            title="Конец Квеста",
            description="""
Квест завершен. Ваши выборы повлияли на развитие событий в Night City.
Возвращайтесь позже - возможно, появятся новые возможности.
            """,
            choices=[],
            metadata={
                "difficulty": 0.0,
                "emotional_impact": "low",
                "world_impact": "none"
            }
        )

    def start_quest(self, player_id: str, player_characteristics: Dict[str, Any]) -> QuestInstance:
        """Start a new dynamic quest for player"""

        quest_id = f"quest_{player_id}_{int(datetime.now().timestamp())}"

        quest = QuestInstance(
            quest_id=quest_id,
            player_id=player_id,
            current_node_id="encounter_1",
            status="active",
            choice_history=[],
            player_characteristics=player_characteristics
        )

        self.active_quests[quest_id] = quest
        return quest

    def process_choice(self, quest_id: str, choice_id: str) -> Dict[str, Any]:
        """Process player choice and return results"""

        if quest_id not in self.active_quests:
            return {"error": "Quest not found"}

        quest = self.active_quests[quest_id]
        current_node = self.nodes.get(quest.current_node_id)

        if not current_node:
            return {"error": "Current node not found"}

        # Find the chosen option
        chosen_option = None
        for choice in current_node.choices:
            if choice["choice_id"] == choice_id:
                chosen_option = choice
                break

        if not chosen_option:
            return {"error": "Choice not found"}

        # Check conditions
        if not self._check_conditions(chosen_option.get("conditions", []), quest.player_characteristics):
            return {"error": "Choice conditions not met"}

        # Create choice record
        choice_record = PlayerChoice(
            choice_id=choice_id,
            node_id=quest.current_node_id,
            choice_text=chosen_option["text"],
            timestamp=datetime.now().isoformat(),
            consequences=chosen_option.get("immediate_effects", []),
            narrative_impact={"branch_taken": chosen_option.get("narrative_branches", [])[0] if chosen_option.get("narrative_branches") else "end"}
        )

        # Add to history
        quest.choice_history.append(choice_record)

        # Apply immediate effects
        effects_applied = self._apply_effects(chosen_option.get("immediate_effects", []), quest)

        # Determine next node
        next_branches = chosen_option.get("narrative_branches", [])
        if next_branches:
            quest.current_node_id = next_branches[0]
        else:
            quest.status = "completed"
            quest.current_node_id = "quest_end"

        # Check if quest should end
        if quest.current_node_id == "quest_end":
            quest.status = "completed"

        return {
            "success": True,
            "quest_update": {
                "current_node": quest.current_node_id,
                "status": quest.status,
                "progress": len(quest.choice_history) * 20  # Simple progress calculation
            },
            "effects_applied": effects_applied,
            "narrative_response": self._generate_narrative_response(choice_record, quest),
            "next_node": self.nodes.get(quest.current_node_id)
        }

    def _check_conditions(self, conditions: List[Dict[str, Any]], player_chars: Dict[str, Any]) -> bool:
        """Check if player meets choice conditions"""
        for condition in conditions:
            cond_type = condition.get("type")
            if cond_type == "stat_check":
                stat = condition.get("stat")
                required_value = condition.get("value", 0)
                player_value = player_chars.get("stats", {}).get(stat, 0)
                if player_value < required_value:
                    return False
        return True

    def _apply_effects(self, effects: List[Dict[str, Any]], quest: QuestInstance) -> List[str]:
        """Apply effects and return descriptions"""
        applied = []

        for effect in effects:
            effect_type = effect.get("type")

            if effect_type == "reputation_change":
                target = effect.get("target")
                value = effect.get("value", 0)
                applied.append(f"Репутация '{target}' изменена на {value}")

            elif effect_type == "currency_grant":
                amount = effect.get("amount", 0)
                currency = effect.get("currency", "eddies")
                applied.append(f"Получено {amount} {currency}")

            elif effect_type == "item_grant":
                item_name = effect.get("name", "предмет")
                applied.append(f"Получен предмет: {item_name}")

            elif effect_type == "quest_progress":
                progress = effect.get("value", 0)
                applied.append(f"Прогресс квеста: +{progress}%")

            elif effect_type == "quest_end":
                reason = effect.get("reason", "unknown")
                applied.append(f"Квест завершен: {reason}")

        return applied

    def _generate_narrative_response(self, choice: PlayerChoice, quest: QuestInstance) -> str:
        """Generate narrative response based on choice and player characteristics"""
        responses = {
            "approach_help": [
                "Информатор оценивающе смотрит на вас. 'Хорошо, что ты не из тех, кто сразу бежит за подмогой.'",
                "Он кивает, признавая вашу смелость в этом опасном районе."
            ],
            "approach_threaten": [
                "Человек в панике отступает. 'Ладно, ладно! Я ухожу!'",
                "Он исчезает в тенях, бормоча проклятия."
            ],
            "accept_mission": [
                "'Отлично! Вот чип с данными. Не попадайся страже.'",
                "Информатор быстро передает вам устройство и скрывается."
            ]
        }

        choice_key = choice.choice_id
        if choice_key in responses:
            return random.choice(responses[choice_key])

        return "Ваши действия находят отклик в окружающем мире."

def demo_dynamic_quest_system():
    """Demonstrate the dynamic quest system"""

    print("🎮 Dynamic Quest System Demo")
    print("=" * 50)

    # Initialize engine
    engine = DynamicQuestEngine()
    engine.create_quest_template()

    # Create player characteristics
    player_chars = {
        "stats": {
            "intimidation": 4,
            "negotiation": 3,
            "perception": 2
        },
        "personality": "diplomatic",
        "background": "street_samurai"
    }

    # Start quest
    quest = engine.start_quest("player_123", player_chars)
    print(f"📖 Started quest: {quest.quest_id}")
    print(f"🎯 Current node: {quest.current_node_id}")
    print()

    # Show initial node
    current_node = engine.nodes[quest.current_node_id]
    print(f"📄 {current_node.title}")
    print(f"📝 {current_node.description.strip()}")
    print()
    print("💭 Available choices:")
    for i, choice in enumerate(current_node.choices, 1):
        conditions_text = ""
        if choice.get("conditions"):
            conditions_text = " (требует специальных условий)"
        print(f"  {i}. {choice['text']}{conditions_text}")
    print()

    # Simulate player choices
    choices_sequence = ["approach_help", "accept_mission"]

    for choice_id in choices_sequence:
        print(f"🎮 Player chooses: {choice_id}")
        result = engine.process_choice(quest.quest_id, choice_id)

        if result.get("error"):
            print(f"❌ Error: {result['error']}")
            continue

        print("✅ Choice processed successfully!")

        # Show effects
        if result.get("effects_applied"):
            print("🎁 Effects applied:")
            for effect in result["effects_applied"]:
                print(f"  • {effect}")

        # Show narrative response
        if result.get("narrative_response"):
            print(f"💬 {result['narrative_response']}")

        # Show next node
        if result.get("next_node"):
            next_node = result["next_node"]
            print()
            print(f"📄 Next: {next_node.title}")
            if hasattr(next_node, 'description') and next_node.description:
                print(f"📝 {next_node.description.strip()[:100]}...")
            if next_node.choices:
                print("💭 Next choices:")
                for i, choice in enumerate(next_node.choices[:2], 1):  # Show first 2
                    print(f"  {i}. {choice['text']}")

        print("-" * 50)

    print("🏁 Demo completed!")
    print(f"📊 Quest status: {quest.status}")
    print(f"📈 Choices made: {len(quest.choice_history)}")
    print(f"🎯 Final node: {quest.current_node_id}")

if __name__ == "__main__":
    demo_dynamic_quest_system()