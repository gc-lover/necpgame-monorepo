from pathlib import Path

INSERT_BYTES = (
    b\"\\n  - path: \\\".BRAIN/03-lore/_03-lore/timeline-author/quests/cis/yerevan/2020-2029/quest-003-armenian-cognac.md\\\"\\n\"
    b\"    version: \\\"0.1.0\\\"\\n\"
    b\"    status: \\\"needs-work\\\"\\n\"
    b\"    priority: \\\"medium\\\"\\n\"
    b\"    checked: \\\"2025-11-09 09:38\\\"\\n\"
    b\"    checker: \\\"Brain Manager\\\"\\n\"
    b\"    api_target:\\n\"
    b\"      microservice: null\\n\"
    b\"      directory: null\\n\"
    b\"      frontend_module: null\\n\"
    b\"    notes: \\\"Перепроверено 2025-11-09 09:38: требуется применить QUEST-TEMPLATE, описать ветвления дегустации, зависимости и целевые API каталоги, указать связанный фронтенд модуль.\\\"\\n\"
    b\"  - path: \\\".BRAIN/03-lore/_03-lore/timeline-author/quests/cis/yerevan/2020-2029/quest-004-khor-virap-monastery.md\\\"\\n\"
    b\"    version: \\\"0.1.0\\\"\\n\"
    b\"    status: \\\"needs-work\\\"\\n\"
    b\"    priority: \\\"medium\\\"\\n\"
    b\"    checked: \\\"2025-11-09 09:45\\\"\\n\"
    b\"    checker: \\\"Brain Manager\\\"\\n\"
    b\"    api_target:\\n\"
    b\"      microservice: null\\n\"
    b\"      directory: null\\n\"
    b\"      frontend_module: null\\n\"
    b\"    notes: \\\"Перепроверено 2025-11-09 09:45: требуется оформить паломнический сценарий по QUEST-TEMPLATE, указать зависимости quest-engine и narrative-service, целевые API каталоги и модуль фронтенда, добавить KPI наград.\\\"\\n\"
    b\"  - path: \\\".BRAIN/03-lore/_03-lore/timeline-author/quests/cis/yerevan/2020-2029/quest-005-khachkars-art.md\\\"\\n\"
    b\"    version: \\\"0.1.0\\\"\\n\"
    b\"    status: \\\"needs-work\\\"\\n\"
    b\"    priority: \\\"medium\\\"\\n\"
    b\"    checked: \\\"2025-11-09 09:56\\\"\\n\"
    b\"    checker: \\\"Brain Manager\\\"\\n\"
    b\"    api_target:\\n\"
    b\"      microservice: null\\n\"
    b\"      directory: null\\n\"
    b\"      frontend_module: null\\n\"
    b\"    notes: \\\"Перепроверено 2025-11-09 09:56: требуется оформить квест по QUEST-TEMPLATE, расписать образовательные ветки и мастер-классы, определить интеграции quest-engine и культурных систем, каталоги API и модуль фронтенда, согласовать KPI наград.\\\"\\n\"
)

MARKER = (
    b\"    notes: \\\"Добавить детализированные этапы турнира, NPC, модели ставок и связи с economy/combat системами; подготовить каталоги quest-engine и betting сервисов перед постановкой API задач.\\\"\\n\"
)

path = Path('.BRAIN/06-tasks/config/readiness-tracker.yaml')
data = path.read_bytes()

if INSERT_BYTES not in data:
    idx = data.find(MARKER)
    if idx == -1:
        raise SystemExit('Marker not found')
    insert_point = idx + len(MARKER)
    data = data[:insert_point] + INSERT_BYTES + data[insert_point:]
    path.write_bytes(data)
