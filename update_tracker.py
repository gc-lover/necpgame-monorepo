from pathlib import Path

path = Path(r'.BRAIN/06-tasks/config/readiness-tracker.yaml')
text = path.read_text(encoding='utf-8')

start_marker = '  - path: ".BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-005-day-of-the-dead.md"\n'
next_marker = '\n  - path: ".BRAIN/03-lore/_03-lore/characters/activity-npc-roster.md"\n'

if start_marker not in text:
    raise SystemExit('start marker not found')
if next_marker not in text:
    raise SystemExit('next marker not found')

start_index = text.index(start_marker)
end_index = text.index(next_marker, start_index)

replacement = (
"  - path: \".BRAIN/03-lore/_03-lore/timeline-author/quests/america/mexico-city/2020-2029/quest-005-day-of-the-dead.md\"\n"
"    version: \"0.1.0\"\n"
"    status: \"needs-work\"\n"
"    priority: \"medium\"\n"
"    checked: \"2025-11-09 10:22\"\n"
"    checker: \"Brain Manager\"\n"
"    api_target:\n"
"      microservice: null\n"
"      directory: null\n"
"      frontend_module: null\n"
"    notes: \"Расширить ветки шествий и ритуалов, связать с системами социального рейтинга, экономики и нарратива, определить целевые каталоги OpenAPI и фронтенд-модуль.\"\n\n"
"  - path: \".BRAIN/03-lore/_03-lore/timeline-author/quests/asia/tokyo/2061-2077/quest-038-kendo-championship.md\"\n"
"    version: \"0.1.0\"\n"
"    status: \"needs-work\"\n"
"    priority: \"medium\"\n"
"    checked: \"2025-11-09 09:45\"\n"
"    checker: \"Brain Readiness Checker\"\n"
"    api_target:\n"
"      microservice: null\n"
"      directory: null\n"
"      frontend_module: null\n"
"    notes: \"Нет версии/статуса/приоритета, отсутствуют ветвления, интеграции с progression/combat и целевые API; требуется оформить квест по QUEST-TEMPLATE с турнирной сеткой и зависимостями.\"\n\n"
"  - path: \".BRAIN/03-lore/_03-lore/timeline-author/quests/asia/tokyo/2061-2077/quest-039-neon-district.md\"\n"
"    version: \"0.1.0\"\n"
"    status: \"needs-work\"\n"
"    priority: \"medium\"\n"
"    checked: \"2025-11-09 10:13\"\n"
"    checker: \"Brain Readiness Checker\"\n"
"    api_target:\n"
"      microservice: null\n"
"      directory: null\n"
"      frontend_module: null\n"
"    notes: \"Добавить статус/версию/приоритет, расписать ветвления конфликтов с якудза, определить системные зависимости и целевые API/фронтенд модули перед постановкой задач.\"\n\n"
)

new_text = text[:start_index] + replacement + text[end_index:]
path.write_text(new_text, encoding='utf-8')
