#!/usr/bin/env python3
"""
Скрипт для обновления Workflow секции во всех правилах агентов
"""

# Карта передачи задач: агент -> следующие агенты
AGENT_HANDOFF_MAP = {
    "idea-writer": {
        "current_status_id": "d9960d37",  # Idea Writer - In Progress
        "handoff": [
            {"condition": "UI/UX задачи (labels ui, ux, client)", "status": "UI/UX - Todo", "status_id": "49689997"},
            {"condition": "Контент-квесты (labels canon, lore, quest)", "status": "Content Writer - Todo", "status_id": "c62b60d3"},
            {"condition": "Системные задачи (default)", "status": "Architect - Todo", "status_id": "799d8a69"},
        ]
    },
    "architect": {
        "current_status_id": "02b1119e",  # Architect - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "Database - Todo", "status_id": "58644d24"},
        ]
    },
    "database": {
        "current_status_id": "91d49623",  # Database - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "API Designer - Todo", "status_id": "3eddfee3"},
        ]
    },
    "api-designer": {
        "current_status_id": "ff20e8f2",  # API Designer - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "Backend - Todo", "status_id": "72d37d44"},
        ]
    },
    "backend": {
        "current_status_id": "7bc9d20f",  # Backend - In Progress
        "handoff": [
            {"condition": "Системные задачи", "status": "Network - Todo", "status_id": "944246f3"},
            {"condition": "Контент-квесты (после импорта в БД)", "status": "QA - Todo", "status_id": "86ca422e"},
        ]
    },
    "network": {
        "current_status_id": "88b75a08",  # Network - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "Security - Todo", "status_id": "3212ee50"},
        ]
    },
    "security": {
        "current_status_id": "187ede76",  # Security - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "DevOps - Todo", "status_id": "ea62d00f"},
        ]
    },
    "devops": {
        "current_status_id": "f5a718a4",  # DevOps - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "UE5 - Todo", "status_id": "fa5905fb"},
        ]
    },
    "ue5": {
        "current_status_id": "9396f45a",  # UE5 - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "QA - Todo", "status_id": "86ca422e"},
        ]
    },
    "ui-ux-designer": {
        "current_status_id": "dae97d56",  # UI/UX - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "UE5 - Todo", "status_id": "fa5905fb"},
        ]
    },
    "content-writer": {
        "current_status_id": "cf5cf6bb",  # Content Writer - In Progress
        "handoff": [
            {"condition": "После завершения (для импорта в БД)", "status": "Backend - Todo", "status_id": "72d37d44"},
        ]
    },
    "qa": {
        "current_status_id": "251c89a6",  # QA - In Progress
        "handoff": [
            {"condition": "Если нужна балансировка", "status": "Game Balance - Todo", "status_id": "d48c0835"},
            {"condition": "Если всё готово", "status": "Release - Todo", "status_id": "ef037f05"},
        ]
    },
    "game-balance": {
        "current_status_id": "a67748e9",  # Game Balance - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "Release - Todo", "status_id": "ef037f05"},
        ]
    },
    "release": {
        "current_status_id": "67671b7e",  # Release - In Progress
        "handoff": [
            {"condition": "После завершения", "status": "Done", "status_id": "98236657"},
        ]
    },
    "performance": {
        "current_status_id": "1674ad2c",  # Performance - In Progress
        "handoff": [
            {"condition": "Возврат разработчику Backend", "status": "Backend - Todo", "status_id": "72d37d44"},
            {"condition": "Возврат разработчику UE5", "status": "UE5 - Todo", "status_id": "fa5905fb"},
        ]
    },
    "stats": {
        "current_status_id": "a67748e9",  # Stats - In Progress (использует тот же ID что Game Balance)
        "handoff": [
            {"condition": "После завершения", "status": "Done", "status_id": "98236657"},
        ]
    },
}

def generate_workflow_section(agent_key, agent_display_name):
    """Генерирует Workflow секцию для агента"""
    
    config = AGENT_HANDOFF_MAP.get(agent_key)
    if not config:
        return None
    
    current_status_id = config["current_status_id"]
    handoff_list = config["handoff"]
    
    # Генерируем список передач
    handoff_lines = []
    for h in handoff_list:
        handoff_lines.append(f"- **{h['condition']}:** `{h['status']}` (`{h['status_id']}`)")
    
    handoff_text = "\n".join(handoff_lines)
    
    workflow_template = f"""## Workflow with Issues

### 📋 Понимание статуса

**`{agent_display_name} - Todo`** = Задача ДЛЯ ТЕБЯ ({agent_display_name} агента). Ты должен её взять!

### 🔄 Простой алгоритм

1. **НАЙТИ задачу:** `Status:"{agent_display_name} - Todo"` (это задачи для тебя)
2. **ВЗЯТЬ задачу:** СРАЗУ обнови статус на `{agent_display_name} - In Progress`
3. **РАБОТАТЬ:** Создавай файлы, документы, код
4. **ПЕРЕДАТЬ:** Обнови статус согласно карте передачи ниже

### 📍 ID статусов

**Все ID в `.cursor/GITHUB_PROJECT_CONFIG.md`:**
- `{agent_display_name} - In Progress`: `{current_status_id}`

**Карта передачи задач:**
{handoff_text}

**Пример обновления статуса:** См. `.cursor/AGENT_SIMPLE_GUIDE.md`"""
    
    return workflow_template


# Карта имен агентов для display
AGENT_DISPLAY_NAMES = {
    "idea-writer": "Idea Writer",
    "architect": "Architect",
    "database": "Database",
    "api-designer": "API Designer",
    "backend": "Backend",
    "network": "Network",
    "security": "Security",
    "devops": "DevOps",
    "ue5": "UE5",
    "ui-ux-designer": "UI/UX",
    "content-writer": "Content Writer",
    "qa": "QA",
    "game-balance": "Game Balance",
    "release": "Release",
    "performance": "Performance",
    "stats": "Stats",
}

if __name__ == "__main__":
    for agent_key, display_name in AGENT_DISPLAY_NAMES.items():
        workflow = generate_workflow_section(agent_key, display_name)
        if workflow:
            print(f"\n{'='*60}")
            print(f"Agent: {display_name}")
            print(f"{'='*60}")
            print(workflow)

