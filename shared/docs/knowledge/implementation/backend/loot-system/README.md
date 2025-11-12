# Loot System - Навигация

**Версия:** 1.0.2  
**Дата:** 2025-11-07  
**Статус:** approved  
**api-readiness:** ready

---

## Микросервисная архитектура

**Ответственный микросервис:** economy-service  
**Порт:** 8085  
**API Gateway маршрут:** `/api/v1/economy/loot/*`  
**Статус:** 📋 В планах (Фаза 3)

**Взаимодействие с другими сервисами:**
- gameplay-service: событие `enemy-killed` → генерация лута
- character-service: проверка уровня персонажа для loot scaling
- inventory-service (economy): добавление лута в инвентарь

**Event Bus события:**
- Подписывается: `combat:enemy-killed`, `raid:boss-defeated`
- Публикует: `loot:generated`, `loot:picked-up`, `legendary:dropped`

---

## 📋 Описание

Система добычи: Loot tables, Drop rates, Rarity, Boss loot, Smart loot.

---

## 📑 Структура

### Part 1: Loot Generation
**Файл:** [part1-loot-generation.md](./part1-loot-generation.md)  
**Содержание:** Loot tables, Drop algorithm, Rarity system

### Part 2: Advanced Loot
**Файл:** [part2-advanced-loot.md](./part2-advanced-loot.md)  
**Содержание:** Smart loot, Boss loot, Event loot, Anti-duplicate

---

## История изменений

- v1.0.1 (2025-11-07 02:20) - Разбит на 2 части
- v1.0.0 (2025-11-06) - Создан (888 строк)

