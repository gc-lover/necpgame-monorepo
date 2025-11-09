---
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-08 12:20  
**Приоритет:** high  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 12:20
**api-readiness-notes:** Социальные и романтические линии лидеров фракций: мотивация, репутационные условия, диалоги. WebSocket через gateway `wss://api.necp.game/v1/social/dialogues/{leaderId}`.
---

# Faction Social Lines — NPC лидеры и отношения

**target-domain:** narrative/dialogues  
**target-microservice:** social-service (8084)  
**target-frontend-module:** modules/social/interactions  
**интеграции:** reputation, romance, contract outcomes

---

## 1. Обзор
- Для ключевых фракций описаны лидеры, их мотивация, условия доступа и ветви общения.
- Каждая линия привязана к репутации, веткам контрактов и историческим событиям.
- Поддерживаются романтические, платонические и прагматические сценарии.

## 2. Таблица лидеров
| Фракция | Лидер | Тип связи | Условие доступа | Ключевые темы |
| --- | --- | --- | --- | --- |
| Aeon Dynasty | Liang  Celestial Wen | Прагматичный союз | 
ep.corp.aeon >= 25, успех escort ветки | Корпорат стратегия, будущее орбиталей |
| Crescent Energy | Amira Al-Faris | Романтическая/деловая | Завершён Nomad альянс, 
ep.corp.crescent >= 20 | Пустынные реформы, баланс Nomad |
| Mnemosyne Archives | Dr. Sofia Arvidsson | Платонический/научный | legacy_rep.hist-urban-scribes >= 20 | Этика памяти, выбор идентичности |
| Ember Saints | Mother Pyra | Интенсивная приверженность | 
ep.street.ember >= 15, освобождение района | Искупление, огненные ритуалы |
| Void Sirens | Captain Nyla Kalu | Приключенческий роман | Космический корабль, спасение колонистов | Свобода в нулевой гравитации |
| Basilisk Sons | Marshal Vega | Боевое братство | Победа в Mech Rampart, 
ep.nomad >= 20 | Защита караванов, кодекс мехов |
| Quantum Fable | Lyra Voss | Творческая связь | Успешный story-heist-legacies | Нарративные войны, правда vs коммерция |
| Echo Dominion | Echo Arbiter Z3N | Философский диалог | Выбранный ИИ в Tribunal | Сосуществование ИИ и людей |

## 3. Структура взаимодействия (пример Amira Al-Faris)
1. **Initiation:** после Desert Grid, POST /social/dialogues/crescent/init становится доступным.
2. **Branches:** выбор между романтическим (личные разговоры о будущем аркологий) и деловым (контракты по энергии).
3. **Reputation Gates:** романтика требует ffinity.crescent >= 40, деловой союз требует доставка ресурсов.
4. **Outcome:** финал ветки меняет цены на энергию, открывает эксклюзивный транспорт.

## 4. Диалоговые узлы
- 	rust_test: проверка репутации и выбранных решений в контрактной цепочке.
- confession_branch: романтическая ветка с эмоциональным выбором.
- pact_branch: деловой союз, влияет на economy-service.
- history_reflection: узлы, ссылающиеся на historical timeline.

## 5. REST/WS Контуры
| Endpoint | Метод | Назначение |
| --- | --- | --- |
| /social/dialogues | GET | Доступные линии по фракциям |
| /social/dialogues/{leaderId} | GET | Узлы диалога, условия, репутационные пороги |
| /social/dialogues/{leaderId}/branch | POST | Выбор ветки (romance, pact, mentorship) |
| /social/dialogues/{leaderId}/progress | POST | Сохранение прогресса, выдача наград |

**WebSocket:** wss://api.necp.game/v1/social/dialogues/{leaderId} — NodeAvailable, AffinityChange, OutcomeApplied.

## 6. Схемы данных
`sql
CREATE TABLE faction_dialogues (
    leader_id VARCHAR(64) PRIMARY KEY,
    faction_id VARCHAR(64) NOT NULL,
    connection_type VARCHAR(32) NOT NULL,
    prerequisites JSONB NOT NULL,
    dialogue_nodes JSONB NOT NULL,
    rewards JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE faction_dialogue_progress (
    leader_id VARCHAR(64) REFERENCES faction_dialogues(leader_id) ON DELETE CASCADE,
    player_id UUID NOT NULL,
    current_node VARCHAR(64) NOT NULL,
    affinity_score INTEGER NOT NULL,
    branch_selected VARCHAR(32),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (leader_id, player_id)
);
`

## 7. Готовность
- NPC-линии связаны с контрактами и рейдами, интеграция с social-service описана.
- Документ готов для API Task Creator и сценаристов.
