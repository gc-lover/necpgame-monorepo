**Статус:** ready  
**Версия:** 2.0.0  
**Дата создания:** 2025-11-10 00:45  
**Последнее обновление:** 2025-11-10 00:45  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-10 00:45  
**api-readiness-notes:** Обновлён под shooter-логику: стартовые экраны, подключение к боевой петле, телеметрия.

# UI — Game Start & Shooter Intro

## 1. Цели
- Предоставить игроку быстрый вход в матчевые сессии (text → webGL → UE5 roadmap).
- Обучить базовым shooter-интеракциям без D&D проверок.
- Синхронизировать UI-потоки с `combat-shooter-core` и `session-service`.

## 2. Структура экранов
| Экран | Файл | Цель | Ключевые элементы |
| --- | --- | --- | --- |
| Login | `ui/game-start/login-screen.md` | Авторизация, выбор региона | Логин форма, MFA, latency indicator |
| Server Select | `ui/game-start/server-selection.md` | Подбор сервера, пинг, загруженность | Tiles серверов, фильтры, `GetServers` API |
| Character Select | `ui/game-start/character-select.md` | Выбор персонажа, просмотр loadout | Карточки персонажей, модуль loadout, readiness meter |
| Shooter Briefing | (новый экран) | Обучение боевой петле | Видео-превью, шкалы accuracy/stability, кнопка «Перейти в сессию» |
| Matchmaking Lobby | `ui/main-game/ui-hud-core.md` (секция «Pre-Match») | Подготовка к бою | Ready check, loadout swap, squad chat |

## 3. Shooter Briefing — ключевые блоки
- **Telemetry Panel:** отображает текущее значение `accuracy`, `stability`, `mobility`, `presence` из `shooter-attributes.md`.
- **Interactive Tutorial:** быстрые мини-сцены (стрельба, уклонение, suppression) с подсказками; события отправляются в `POST /api/v1/tutorial/events`.
- **Equipment Preview:** показывает влияние выбранного оружия и имплантов (TTK, recoil, suppression score).
- **Anti-cheat Notice:** информирование о серверном авторитете и политике макросов.

## 4. API и события
| Поток | Endpoint | Описание |
| --- | --- | --- |
| Auth | `POST /api/v1/auth/login` | Авторизация, выдача токена |
| Session | `GET /api/v1/session/regions` | Список серверов с latency |
| Characters | `GET /api/v1/characters/players` | Персонажи пользователя |
| Loadout | `GET /api/v1/combat/loadouts/current` | Текущий лодаут |
| Tutorial | `POST /api/v1/tutorial/events` | События обучения стрелке |
| Matchmaking | `POST /api/v1/matchmaking/queue` | Добавление в очередь |
| Shooter Intro | `POST /api/v1/combat/shooter/briefing` | Завершение обучения, фиксация параметров |

### WebSocket
- `wss://api.necp.game/v1/matchmaking/{sessionId}` — статус очереди, squad ready.
- `wss://api.necp.game/v1/tutorial/shooter/{playerId}` — прогресс обучения и подсказки.

### Kafka/Event Bus
- `ui.telemetry.login` — успешные авторизации.
- `ui.telemetry.shooter-briefing` — завершение обучения, запись параметров.
- `ui.telemetry.matchmaking` — тайминг ожидания.

## 5. UX-флоу
1. **Login → Server Select:** авторизация, выбор региона (подсветка рекомендуемого).
2. **Character Select:** отображение `loadout power`, `TTK preview`, `role tags`.
3. **Shooter Briefing:** интерактивное обучение (мин. 2 минуты), настройка чувствительности мыши.
4. **Matchmaking Lobby:** готовность сквада, быстрый чат, предупреждения по latency.
5. **Combat Session:** переход к `ui/main-game/ui-hud-core.md` после успешного ready.

## 6. Состояния UI
| Состояние | Триггер | Действие |
| --- | --- | --- |
| `maintenance` | `GET /maintenance/status` true | Показываем баннер, запрет входа |
| `queue-paused` | Matchmaking load high | Предлагаем PvE тренировку |
| `tutorial-required` | Новый аккаунт, shooter briefing not done | Автопросмотр обучающего экрана |

## 7. Телеметрия и аналитика
- Сбор данных о времени прохождения обучения, точности в мини-сценах, выборе loadout.
- Отправка `accuracy_before` / `accuracy_after` для оценки эффективности обучения.
- Отдельный трек `tutorial_skipped` (если у игрока достаточная статистика из UE5-клиента).

## 8. Ресурсы дизайна
- Figma: `https://figma.com/file/shooter-start-ui` (mvp-link).
- Видео-инструктаж: на стороне контент-команды.
- Голосовые подсказки: TBD, зависят от `audio-service`.
- JSON-конфиг обучения: `ui/game-start/shooter-briefing-config.json`

## 9. Следующие шаги
1. Синхронизировать макеты с frontend (`FRONT-WEB/modules/game-start`).
2. Развернуть `shooter-briefing-config.json` на dev-среде и интегрировать с `POST /api/v1/tutorial/events`.
3. Проверить доступность (screen reader, key-only navigation).
4. После WebGL демо — обновить раздел UE5 Launchpad.
