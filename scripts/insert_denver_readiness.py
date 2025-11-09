from pathlib import Path

tracker_path = Path(".BRAIN/06-tasks/config/readiness-tracker.yaml")
text = tracker_path.read_text()

needle = (
    '  - path: ".BRAIN/03-lore/_03-lore/locations/world-cities/delhi-detailed-2020-2093.md"\n'
    '    version: "1.0.0"\n'
    '    status: "needs-work"\n'
    '    priority: "high"\n'
    '    checked: "2025-11-09 09:47"\n'
    '    checker: "Brain Manager"\n'
    '    api_target:\n'
    '      microservice: "world-service"\n'
    '      directory: "api/v1/world/cities/delhi.yaml"\n'
    '      frontend_module: "modules/world/atlas"\n'
    '    notes: "Добавить модели данных, REST/Events контракты и интеграцию с фронтендом для подготовки API пакета world-service."\n'
)

block = (
    '  - path: ".BRAIN/03-lore/_03-lore/locations/world-cities/denver-detailed-2020-2093.md"\n'
    '    version: "1.0.0"\n'
    '    status: "needs-work"\n'
    '    priority: "high"\n'
    '    checked: "2025-11-09 09:58"\n'
    '    checker: "Brain Manager"\n'
    '    api_target:\n'
    '      microservice: "world-service"\n'
    '      directory: "api/v1/world/cities/denver.yaml"\n'
    '      frontend_module: "modules/world/atlas"\n'
    '    notes: "Добавить модели данных, REST/Events контракты и интеграцию с фронтендом для world-service перед постановкой API задач."\n'
)

if block not in text:
    idx = text.find(needle)
    if idx == -1:
        raise SystemExit("Не удалось найти блок Дели для вставки Денвера")
    insert_pos = idx + len(needle)
    tracker_path.write_text(text[:insert_pos] + block + text[insert_pos:])

