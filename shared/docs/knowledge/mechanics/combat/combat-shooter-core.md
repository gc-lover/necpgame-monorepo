# Combat Shooter Core — Боевая петля 3D-шутера

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-09  
**Последнее обновление:** 2025-11-09 23:55  
**Приоритет:** критический

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 23:55  
**api-readiness-notes:** Данные по оружию, баллистике, хитбоксам, сетевым SLA и античиту верифицированы с `combat-shooting.md` и требованиями analytics-service. Готово к постановке API задач.

**target-domain:** gameplay-combat  
**target-microservice:** gameplay-service (8083)  
**target-frontend-module:** modules/combat/mechanics

---

## 1. Цель
Перевести боевые взаимодействия NECPGAME на детерминированную 3D-шутерную модель (web-клиент → UE5), заменив все D&D-проверки на статические параметры оружия, имплантов и сетевые SLA. Документ служит базой для API задач, телеметрии и античита.

## 2. Ключевые компоненты
- **Классы оружия**: пистолеты, SMG, штурмовые, снайперские, дробовики, тяжелые, энергетические, экзотические.
- **Баллистика**: hitscan, projectile, drop tables, penetration, ricochet.
- **Хитбоксы и броня**: head, neck, torso, arms, legs, cyber-узлы; модификаторы и устойчивость по tier.
- **Отдача и перегрев**: паттерны, bloom, управление имплантами, охлаждение.
- **Импланты/способности**: модификация конуса, recoil, энергобюджета, боеприпасов.
- **Сетевой слой**: authoritative сервер, client prediction, shot validation, lag compensation.
- **Telemetry & Anti-cheat**: события `combat.shooter.*`, макрос-контроль, latency SLA.
- **API**: REST, WebSocket, Kafka для реестра оружия, событий стрельбы, suppression, аналитики.

---

## 2.1 Базовые атрибуты игроков
| Роль | accuracy | stability | handling | suppression_score | mobility | resilience | presence | tech_override | analysis |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Assault | 0.56 | 0.52 | 0.48 | 0.32 | 0.50 | 0.48 | 0.40 | 0.30 | 0.40 |
| Support | 0.50 | 0.55 | 0.42 | 0.38 | 0.42 | 0.54 | 0.52 | 0.35 | 0.48 |
| Tech Specialist | 0.48 | 0.50 | 0.44 | 0.28 | 0.40 | 0.46 | 0.45 | 0.60 | 0.62 |
| Stealth/Recon | 0.54 | 0.48 | 0.46 | 0.20 | 0.58 | 0.44 | 0.58 | 0.42 | 0.50 |
| Hybrid Starter | 0.52 | 0.50 | 0.45 | 0.26 | 0.48 | 0.48 | 0.46 | 0.45 | 0.48 |

- Значения синхронизированы с `05-technical/shooter-attributes.md` и используются как отправная точка для балансировки TTK.
- Analytics-service следит, чтобы `accuracy`/`stability` оставались в диапазоне базовых ±0.10 без активных бафов.
- Security-service использует эти значения для первичной фильтрации макро-нарушений.

---

## 3. Категории оружия
| Класс | Примеры | Баллистика | Базовый TTK (PvP/PvE) | Особенности |
| --- | --- | --- | --- | --- |
| Пистолеты | Кинетические, лазерные | Hitscan | 620 мс / 780 мс | Низкий recoil, +15% скорость бега |
| SMG | Плазменные, электромагнитные | Hitscan + лёгкий drop после 25 м | 480 мс / 650 мс | Высокий DPS вблизи, штраф точности на дистанции |
| Штурмовые винтовки | Баллистические, гибридные | Projectile, drop −0.8 м на 100 м | 540 мс / 700 мс | Универсальный класс, моды под роли |
| Дробовики | Магнитные, термобарические | Hitscan cone | 320 мс / 520 мс | Рассеивание, стихийные эффекты, stagger |
| Снайперские | Рельсовые, оптические | Hitscan/Projectile, drop −0.4 м на 100 м | 280 мс / 420 мс | Высокий урон по head/torso, требует устойчивости |
| Тяжелое оружие | Миниганы, рейлган | Projectile, зарядка | 360 мс / 520 мс | Режим разогрева, энергопотребление, suppression |
| Энергетическое | Лазеры, лучевые | Hitscan, перегрев | 400 мс / 600 мс | Проникает средние укрытия, управляется охлаждением |
| Экзотические | Нанодроны, smart | Projectile, автокоррекция | 520 мс / 680 мс | DOT, само-наведение, высокий энергобюджет |

---

## 4. Параметры оружия по классам
| Класс | Урон Head / Torso / Limbs | RPM | Скорость снаряда | Паттерн отдачи | Overheat (°C) | Эффективная дальность |
| --- | --- | --- | --- | --- | --- | --- |
| Пистолеты | 120 / 60 / 45 | 420 | 600 м/с | Линейный | 65 | 0–25 м |
| SMG | 105 / 55 / 42 | 720 | 520 м/с | Z-пила | 75 | 0–30 м |
| Штурмовые | 135 / 70 / 50 | 650 | 720 м/с | Спираль | 80 | 0–70 м |
| Дробовики (×8 пеллет) | 45 / 35 / 25 | 120 | 400 м/с | Конический | 60 | 0–15 м |
| Снайперские | 260 / 140 / 90 | 72 | 950 м/с | Линейный | 70 | 40–150 м |
| Тяжелое оружие | 80 / 65 / 50 | 900 | 680 м/с | Уступчатый | 95 | 15–60 м |
| Энергетическое | 140 / 75 / 55 | 540 | 1 000 м/с | Стабильный | 110 | 20–80 м |
| Экзотические | 90 / 55 / 45 + DOT 30 | 420 | 480 м/с | Адаптивный | 105 | 10–50 м |

- RPM = rounds per minute.  
- Overheat — температура выключения; охлаждение 2 °С/сек без имплантов, 4 °С/сек с `cooling_matrix`.

---

## 5. Хитбоксы и броня
| Хитбокс | Модификатор (без брони) | Броня Tier 1 | Tier 2 | Tier 3 | Особенности |
| --- | --- | --- | --- | --- | --- |
| Head/Neck | ×2.0 | ×1.7 | ×1.5 | ×1.3 | Шлемы добавляют шанс рикошета 12/18/25% |
| Torso | ×1.0 | ×0.85 | ×0.75 | ×0.65 | Уменьшение stagger по мере tier |
| Arms | ×0.75 | ×0.65 | ×0.55 | ×0.45 | Снижение точности при попадании <60% |
| Legs | ×0.7 | ×0.6 | ×0.5 | ×0.4 | Вводит debuff `slow` 10% на 1 сек |
| Cyber-core (implants) | ×1.2 | ×1.0 | ×0.9 | ×0.8 | Уязвимость к EMP ×1.5, immunity к bleed |

- Armor tiers потребляют `armor_integrity` (0–100%). При снижении <25% модификаторы растут на +0.1.
- EMP и кибер-урон игнорируют органическую броню; физический урон отражается по таблице.

---

## 6. Влияние имплантов и способностей
| Модуль | Эффект | Энергобюджет | Синергии |
| --- | --- | --- | --- |
| `optic_suite MK.IV` | −15% конус, +12% точность в движении | 8 энергии | `combat-stealth` (уменьшает шум выстрела) |
| `neural_reflex accelerator` | +10% RPM (пистолеты/SMG), сокращение отката recoil на 0.1 сек | 12 | `combat-abilities` (ultimates boosting fire mode) |
| `gyro_stabilizer` | −20% вертикальная отдача, +8% устойчивость при ADS | 10 | `combat-freerun` (снижение штрафа при прыжках) |
| `cooling_matrix` | +3 °С/сек охлаждение, −10% перегрев энергетики | 9 | `combat-hacking` (перенаправление тепла в скафандр) |
| `ammo_fabricator` | Перегенерация боеприпасов 5%/сек, переключение типов | 14 | `economy/equipment-matrix` (редкие материалы) |
| `suppression_field` ability | Создает зону подавления: −25% точность врагов, ↑ telem `combat.shooter.suppress` | 0 (ability cooldown) | `combat-stealth` (маскировка шума) |

- Энергобюджет суммируется; базовый лимит 40, увеличивается имплантом ядра до 60.
- Способности/импланты отражаются в API `/combat/shooter/ability-modifiers`.

---

## 7. Баллистика и попадания
- Hitscan latency windows: 16 мс (PvP), 32 мс (PvE) — сервер валидирует по последнему acknowledged кадру.
- Projectile drop: `drop = gravity_coeff × (distance² / 1000)`; коэффициенты 0.35 (штурмовое), 0.18 (снайперское), 0.6 (экзотическое).
- Penetration: `light_cover` 30% урона проходит, `medium_cover` 60% снижение, `heavy_cover` блокирует физический урон, но пропускает энергетический 40%.
- Ricochet: угол >35° с `hard_surface` даёт шанс 25% рикошета, уменьшение урона ×0.4.
- Suppression radius: 4 м для тяжелого оружия, 2 м для штурмовых; при попаданиях создаёт debuff `suppressed` (−20% точность, +15% разброс).

---

## 8. Контроль отдачи и перегрев
- Recoil pattern задаётся массивом точек на 12 выстрелов, затем повторяется с увеличением ×1.05.
- Bloom: старт 0.6°, максимум 4.2°; ADS снижает на 40%, crouch — ещё на 20%.
- Перегрев энергетики: предупреждение на 85°С, отключение на пороге из таблицы; охлаждение ускоряют импланты и `support` способности.
- Дробовики используют подход pellets spread: σ = 1.6° (без модов), минимально 0.9° при стабилизаторах.
- Тяжелое оружие вводит `spin-up` 0.5 сек и `spin-down` 0.3 сек; отмена стоит 15 энергии `servo_implant`.

---

## 9. Сетевой слой и SLA
| Метрика | PvP таргет | PvE таргет | Комментарий |
| --- | --- | --- | --- |
| Tickrate сервера | 128 Hz | 64 Hz | Уровень ядра gameplay-service |
| Round-trip latency | ≤80 мс | ≤120 мс | При превышении включается интерполяция 1.5× |
| Частота событий `fire` | ≤1 каждые 20 мс | ≤1 каждые 15 мс | Throttle на сервере, очередь до 10 событий |
| Частота событий `hit` | ≤1 каждые 15 мс | ≤1 каждые 12 мс | Консолидируются при burst |
| Rewind buffer | 280 мс | 320 мс | История позиций для лаг-компенсации |
| Максимальный desync | 12 см | 20 см | Если выше — сервер шлёт корректировку позиции |
| Packet loss tolerance | до 2% | до 5% | При превышении сервер включает `degraded mode` |

- Shot validation: сравнение паттерна отдачи и временных интервалов; >5% отклонение маркируется событием `combat.shooter.macro-flagged`.
- WebSocket `/ws/gameplay/combat/shooter/{sessionId}` отправляет батчи раз в 33 мс (PvP) и 50 мс (PvE).

---

## 10. Телеметрия и античит
- События:
  - `combat.shooter.fire`: weaponId, fireMode, latency, clientTimestamp, predictionFrame.
  - `combat.shooter.hit`: weaponId, targetId, hitbox, damageApplied, penetration, distance.
  - `combat.shooter.kill`: targetType, overkill, assistors, streakId.
  - `combat.shooter.suppress`: radius, suppressionScore, affectedTargets.
  - `combat.shooter.reload`: reloadType, duration, canceled.
  - `combat.shooter.overheat`: temperature, cooldownTime, shutdownReason.
- Античит-порог: максимум 9 триггеров `fire` за 100 мс; >12 маркируется `macro_flagged`.  
  Отдача сверяется по кривой; если 3 подряд попадания отклоняются <5% от идеального паттерна без корректировок — отправляется предупреждение.
- Telemetry агрегируется в `shooter_accuracy_rate`, `shooter_ttk_pvp`, `shooter_ttk_pve`, `shooter_macro_flags`, `shooter_latency_p95`.

---

## 11. API черновик
| Endpoint | Ключевые поля запроса | Основные ответы / события |
| --- | --- | --- |
| `POST /combat/shooter/fire` | weaponId, shotId, position, direction, latency, predictionFrame | 202 + event `combat.shooter.fire` |
| `POST /combat/shooter/hit` | shotId, targetId, hitbox, rawDamage, modifiers | 200 + recalculated damage, stagger flag |
| `POST /combat/shooter/reload` | weaponId, mode (start/cancel), magazineState | 200 + `reloadId`, websocket broadcast |
| `POST /combat/shooter/suppress` | sourceId, radius, intensity | 200; triggers Kafka `combat.shooter.suppress` |
| `POST /combat/shooter/projectile` | projectileId, path nodes, detonation | 202 + server authoritative trajectory |
| `GET /combat/shooter/weapon-stats` | filters: class, rarity, manufacturer | 200 JSON с таблицей параметров, ссылки на моды |
| `POST /combat/shooter/ability-modifiers` | loadoutId, modifiers[], duration | 200 с валидированным энергобюджетом |
| `GET /combat/shooter/telemetry` | timeframe, aggregation (p50/p95/max) | 200 с KPI, refs на analytics-service |

- WebSocket `/ws/gameplay/combat/shooter/{sessionId}`: пакет `stateFrame` содержит массив событий `[fire, hit, suppression, overheat]` за тик.
- Kafka topics: `combat.shooter.fire`, `combat.shooter.hit`, `combat.shooter.kill`, `combat.shooter.suppress`, `combat.shooter.macro-flagged`.

---

## 12. Интеграции
- **combat-session-backend**: lifecycle, синхронизация sessionId, отчёт о фейлах latency.
- **combat-abilities.md**: способности, которые временно меняют RPM, recoil, энергетическое потребление.
- **combat-stealth.md**: шум выстрелов, вспышки, suppression → модификаторы угрозы.
- **combat-freerun.md**: штрафы к точности при движении, wall-run бонусы.
- **combat-hacking-combat-integration.md**: удалённое отключение оружия, overrides охлаждения.
- **quest-engine-backend.md**: `skill-test` опирается на показатели `accuracy`, `resilience`, `handling`, исключая рандом.
- **analytics-service**: агрегация телеметрии, авто-балансировка TTK, отчёты для live-ops.

---

## 13. Статус и следующие шаги
1. Подготовить JSON-примеры для `weapon-stats`, `fire`, `hit` и WebSocket `stateFrame` для API-SWAGGER.
2. Вынести параметры TTK и anti-cheat thresholds в shared reference (`combat-balancing-reference.md`).
3. После генерации API задач синхронизировать реализации с economy/progression (боеприпасы, награды).

---

## История изменений
- v1.0.0 (2025-11-09) — финализированы параметры, подтверждена готовность к API задачам.
- v0.2.0 (2025-11-09) — добавлены таблицы оружия, хитбоксов, сетевых SLA и античита.
- v0.1.0 (2025-11-09) — создан базовый каркас, перенёс фокус с D&D на реалтайм шутер.