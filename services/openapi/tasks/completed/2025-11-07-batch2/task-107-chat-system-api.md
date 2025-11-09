# Task ID: API-TASK-107
**Тип:** API Generation
**Приоритет:** КРИТИЧЕСКИЙ (BACKEND)
**Статус:** queued
**Создано:** 2025-11-07 05:25
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-106 (session-management.yaml)

---

## 📋 Краткое описание

Создать API для системы внутриигрового чата.

**Что нужно сделать:** Создать API для Chat System (каналы, сообщения, модерация, mentions, commands, rich formatting, voice chat).

---

## 🎯 Цель задания

Создать API для Chat System (КРИТИЧЕСКИЙ):
- **Каналы:** Global, Local, Party, Guild, Whisper, Trade, Combat
- **Сообщения:**
  - Отправка/получение
  - Message persistence (история)
  - Timestamps, sender info
- **Модерация:**
  - Фильтры, бан слов
  - Spam protection (cooldowns)
  - Mute/ban функции
- **Функции:**
  - Mentions (@player)
  - Emojis
  - Slash commands (/help, /invite, /trade)
  - Rich formatting (bold, italic, links)
- **Voice chat:** WebRTC integration
- **Translation:** Автоперевод между языками

**КРИТИЧЕСКИ ВАЖНО:** Коммуникация между игроками в MMORPG! (1000+ строк документа)

---

## 📚 Источники информации

**Путь:** `.BRAIN/05-technical/backend/chat-system.md`
**Версия:** v1.0.0
**Статус:** approved (ready)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/technical/chat-system.yaml`

**ВАЖНО:** Большая система. ОБЯЗАТЕЛЬНО разбить:
- chat-system-core.yaml - основные endpoints
- chat-system-channels.yaml - управление каналами
- chat-system-moderation.yaml - модерация
- chat-system-ws.yaml - WebSocket для real-time

---

## ✅ Endpoints

1. **POST `/api/v1/technical/chat/send`** - Отправить сообщение
2. **GET `/api/v1/technical/chat/messages/{channel}`** - Получить сообщения
3. **POST `/api/v1/technical/chat/join-channel`** - Присоединиться к каналу
4. **WebSocket `/ws/chat/{channel}`** - Real-time чат

---

**История:** 2025-11-07 05:25 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

