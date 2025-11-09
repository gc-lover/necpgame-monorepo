from pathlib import Path

tracker_path = Path(".BRAIN/06-tasks/config/readiness-tracker.yaml")
lines = tracker_path.read_text(encoding="cp1251").splitlines()

denver_line = '  - path: ".BRAIN/03-lore/_03-lore/locations/world-cities/denver-detailed-2020-2093.md"'

if denver_line not in lines:
    delhi_line = '  - path: ".BRAIN/03-lore/_03-lore/locations/world-cities/delhi-detailed-2020-2093.md"'
    try:
        idx = lines.index(delhi_line)
    except ValueError:
        raise SystemExit("Не найден блок Дели")

    block = [
        denver_line,
        '    version: "1.0.0"',
        '    status: "needs-work"',
        '    priority: "high"',
        '    checked: "2025-11-09 09:58"',
        '    checker: "Brain Manager"',
        '    api_target:',
        '      microservice: "world-service"',
        '      directory: "api/v1/world/cities/denver.yaml"',
        '      frontend_module: "modules/world/atlas"',
        '    notes: "Добавить модели данных, REST/Events контракты и интеграцию с фронтендом для world-service перед постановкой API задач."',
    ]

    lines[idx + 1:idx + 1] = block
    tracker_path.write_text("\r\n".join(lines) + "\r\n", encoding="cp1251")
from pathlib import Path

tracker_path = Path(".BRAIN/06-tasks/config/readiness-tracker.yaml")
text = tracker_path.read_text(encoding="utf-8")

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
    tracker_path.write_text(text[:insert_pos] + block + text[insert_pos:], encoding="utf-8")

