**Статус:** draft  
**Версия:** 0.1.0  
**Дата создания:** 2025-11-10 00:40  
**Приоритет:** high  
**api-readiness:** in-review  
**api-readiness-check-date:** 2025-11-10 00:40  
**api-readiness-notes:** Черновик характеристик для shooter-ядра. Требуется валидация с `combat-shooter-core.md` и `analytics-service` до перевода в ready.

# Shooter Attributes — ядро характеристик 3D-шутера

## 1. Обзор
- **Цель:** заменить D&D кубики на непрерывные shooter-параметры.
- **Контекст:** используется всеми боевыми подсистемами (`combat-shooter-core.md`, `combat-abilities.md`, `combat-stealth.md`, `combat-freerun.md`).
- **Интеграции:** `analytics-service` (телеметрия), `session-service` (античит), `quest-engine` (события), `economy-service` (стоимость модификаций).

## 2. Категории характеристик
| Категория | Параметр | Диапазон | Описание | Источник |
| --- | --- | --- | --- | --- |
| Стрельба | `accuracy` | 0.30–0.90 | Базовая точность оружия + модификаторы от навыков и имплантов | `combat-shooter-core.md` |
| Стрельба | `stability` | 0.20–0.85 | Контроль отдачи; влияет на рассеивание при автоматическом огне | `combat-shooter-core.md` |
| Стрельба | `handling` | 0.25–0.80 | Скорость смены оружия/прицеливания, взаимодействия с укрытиями | `combat-shooting.md` |
| Стрельба | `suppression_score` | 0.00–1.00 | Давление на врага; определяет шанс подавления | `combat-shooter-core.md` |
| Мобильность | `mobility` | 0.35–0.90 | Базовая скорость, ускорение, урон от падений | `combat-freerun.md` |
| Мобильность | `momentum` | 0.00–1.00 | Накопленный импульс паркура, влияет на комбо | `combat-freerun.md` |
| Выживаемость | `resilience` | 0.30–0.85 | Сопротивление контролю, устойчивость к урону | `combat-abilities.md` |
| Выживаемость | `shield_capacity` | 250–900 | Генераторы щитов, пассивы поддержки | `combat-abilities.md` |
| Социалка | `presence` | 0.40–0.85 | Переговоры в условиях угрозы; заменяет skill checks | `quest-system.md` |
| Технические навыки | `tech_override` | 0.30–0.90 | Вскрытие панелей, взлом оборудования | `combat-hacking-networks.md` |
| Технические навыки | `analysis` | 0.35–0.95 | Расчёт траекторий, обнаружение слабых мест | `analytics-service` |

## 3. Базовые значения
| Роль | accuracy | stability | handling | suppression_score | mobility | resilience | presence | tech_override | analysis |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Assault | 0.56 | 0.52 | 0.48 | 0.32 | 0.50 | 0.48 | 0.40 | 0.30 | 0.40 |
| Support | 0.50 | 0.55 | 0.42 | 0.38 | 0.42 | 0.54 | 0.52 | 0.35 | 0.48 |
| Tech Specialist | 0.48 | 0.50 | 0.44 | 0.28 | 0.40 | 0.46 | 0.45 | 0.60 | 0.62 |
| Stealth/Recon | 0.54 | 0.48 | 0.46 | 0.20 | 0.58 | 0.44 | 0.58 | 0.42 | 0.50 |
| Hybrid (Starter) | 0.52 | 0.50 | 0.45 | 0.26 | 0.48 | 0.48 | 0.46 | 0.45 | 0.48 |

- Значения используют диапазоны из `combat-shooter-core.md`; корректируются перками и экипировкой.
- Analytics рекомендует поддерживать `accuracy` в диапазоне 0.50–0.60 для стартовых билдов, чтобы TTK оставался в целевых SLA.
- Security фиксирует отклонения >0.12 от базовых значений без активных бустов как потенциальное нарушение (событие `combat.anticheat.threshold`).

## 4. Пороги взаимодействий
- **Стандартный успех:** параметр ≥ порога → гарантированно активирует опцию.
- **Условный успех:** параметр в диапазоне порога ±0.03 → требует подтверждения (микро-ивент).
- **Провал:** параметр < порога −0.03 → запускает негативные последствия (дополнительный бой, алерт, потеря ресурсов).

### 4.1 Таблица порогов
| Сложность | Диапазон порога | Типичные сценарии |
| --- | --- | --- |
| Tier 1 (Easy) | 0.35–0.45 | Вход в дом, базовые технические панели |
| Tier 2 (Standard) | 0.46–0.55 | Бой с мародёрами, короткий ремонт |
| Tier 3 (Challenging) | 0.56–0.65 | Полевой ремонт под огнём, нейтрализация турелей |
| Tier 4 (Hard) | 0.66–0.75 | Сложные переговоры, перехват продвинутых дронов |
| Tier 5 (Elite) | 0.76–0.90 | Корпоративные операции, рейдовые механики |

## 5. Баланс и формулы
- **Accuracy vs Stability:** `effective_spread = base_spread * (1 - stability)`. Минимальный spread ограничен 30% от базового.
- **Suppression:** `suppression_apply = suppression_score * accuracy`. Значения ниже 0.25 не подавляют элитных врагов.
- **Mobility:** `mobility_speed = base_speed * (1 + mobility)`; momentum > 0.65 открывает advanced freerun-actions.
- **Presence:** `presence_check = presence - enemy_resolve`. Отрицательные значения вызывают агрессию.
- **Resilience:** уменьшает длительность негативных состояний: `duration_final = duration_base * (1 - resilience*0.4)`.

## 6. Телеметрия
| Поток | Потребитель | Payload |
| --- | --- | --- |
| `analytics.shooter.metrics` | analytics-service | `{ accuracy, stability, suppression, outcome }` |
| `analytics.movement.momentum` | analytics-service | `{ momentum, action, success, location }` |
| `analytics.social.interaction` | quest-engine | `{ presence, compliance, outcome, reputation_delta }` |
| `combat.anticheat.threshold` | security-service | `{ parameter, value, limit, violation }` |

## 7. Взаимодействие с прогрессией
- **Перки/уровни:** каждые 5 уровней игрок выбирает усиление двух параметров (макс +0.05).
- **Импланты:** дают постоянные бонусы (например, `optic_suite` +0.06 accuracy).
- **Снаряжение:** модификаторы завязаны на rarity (Common +0.02, Rare +0.05, Epic +0.08, Legendary +0.12).
- **Комбо:** связка способностей может временно повышать/понижать параметры.

## 8. Связанные документы
- `.BRAIN/02-gameplay/combat/combat-shooter-core.md`
- `.BRAIN/02-gameplay/combat/combat-shooting.md`
- `.BRAIN/02-gameplay/combat/combat-abilities.md`
- `.BRAIN/02-gameplay/combat/combat-stealth.md`
- `.BRAIN/02-gameplay/combat/combat-freerun.md`
- `.BRAIN/02-gameplay/combat/combat-hacking-networks.md`

## 9. Следующие шаги
1. Провести ревью с analytics/security для подтверждения формул.
2. Согласовать дефолтные значения для стартового персонажа и классов.
3. Добавить таблицы конвертации в `combat-shooter-core.md` (TTK, recoil, suppression).
4. Подготовить JSON-схему (`shooter-attributes.json`) для генерации SDK.
