# Inventory System - Навигация

**Версия:** 1.0.2  
**Дата:** 2025-11-07  
**Статус:** approved  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 23:39  
**api-readiness-notes:** Навигация по подсистемам инвентаря, указаны сервисы и зависимости.

---

## Микросервисная архитектура

**Ответственный микросервис:** economy-service  
**Порт:** 8085  
**API Gateway маршрут:** `/api/v1/economy/inventory/*`  
**Статус:** 📋 В планах (Фаза 3)

**Взаимодействие с другими сервисами:**
- character-service: получение character data для инвентаря
- loot-service (economy): добавление лута в инвентарь
- trade-service (economy): проверка наличия items при трейде

---

## 📋 Описание

Система инвентаря: Storage, Equipment, Stacking, Trading, Weight management.

---

## 📑 Структура

### Part 1: Core System
**Файл:** [part1-core-system.md](./part1-core-system.md)  
**Содержание:** Database, Storage management, Equipment

### Part 2: Advanced Features
**Файл:** [part2-advanced-features.md](./part2-advanced-features.md)  
**Содержание:** Trading, Quality tiers, Durability, Optimization

---

## История изменений

- v1.0.1 (2025-11-07 02:20) - Разбит на 2 части
- v1.0.0 (2025-11-06) - Создан (896 строк)

