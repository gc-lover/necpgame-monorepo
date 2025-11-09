# Stealth Feature
Система скрытности (Deus Ex / Dishonored / Hitman style).

**OpenAPI:** stealth.yaml | **Роут:** /game/stealth

## Функционал
- **4 уровня:** Hidden → Suspicious → Detected → Combat
- **Показатели:** Видимость, Шум, Освещение
- **Действия:**
  - Enter Stealth (crouch) - снижает скорость, но уменьшает видимость/шум
  - Takedown (lethal/non-lethal) - скрытная нейтрализация
  - Hide Body - прячет тело в контейнере/вентиляции
  - Create Distraction - отвлекает врагов (звук, взрыв, hologram, hack)
  - Optical Camo - активирует имплант невидимости

## Компоненты
- **StealthMeter** - статус скрытности с индикаторами (видимость, шум, свет, враги)
- **StealthPage** - управление скрытностью с действиями

## Механики
- Враги: зрение, слух, поиск, тревога
- Импланты: оптический камуфляж, глушители, радар
- Интеграция: D&D checks, netrunning, социальные навыки
- COOL проверки для takedown

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI компоненты.

