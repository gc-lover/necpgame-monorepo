# MVP Frontend Architecture

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** Frontend Guild  
**Связанные документы:** `ui-mvp-screens.md`, `frontend-architecture-compact.md`, `2025-11-08-gameplay-backend-sync.md`

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08
**api-readiness-notes:** Определены модули, состояния, интеграции. Готово к разработке `FRONT-WEB`.

---

## 1. Технологический стек

- React 18 + TypeScript.
- Redux Toolkit + RTK Query.
- React Router 6.
- Styled Components (темы: light/dark cyberpunk).
- WebSocket клиент (SockJS + STOMP) для real-time.

## 2. Архитектура модулей

| Модуль | Ответственность | API | Состояния |
|--------|-----------------|-----|-----------|
| `modules/auth` | Регистрация, вход, токены | `/auth/*` | `idle`, `loading`, `authenticated`, `error` |
| `modules/player` | Персонаж, инвентарь | `/characters`, `/inventory` | `loading`, `ready`, `updating` |
| `modules/quests` | Туториал, журнал | `/quests/*` | `empty`, `active`, `completed` |
| `modules/combat` | Баллистика, HUD | `/combat/*`, `combat.ballistics.events` | `engaged`, `cooldown` |
| `modules/orders` | Социальные заказы | `/social/orders`, `social.orders.lifecycle` | `board`, `application`, `dispute` |
| `modules/crafting` | Крафт, очереди | `/crafting/*`, `economy.crafting.jobs` | `queue_empty`, `processing`, `done` |
| `modules/world` | Unrest, события | `/world/*`, `world.unrest.updates` | `stable`, `alert`, `crisis` |

## 3. Слой данных

- Используем RTK Query сервисы: `combatApi`, `ordersApi`, `worldApi`.
- WebSocket подписки через `eventBus` (обёртка над STOMP).
- Кэширование: Query cache + IndexedDB для оффлайн (минимально: персонаж, заказы).

## 4. UI Shell

- Корневой layout: `AppShell` (header, nav, content, event-feed).
- Темизация: `ThemeProvider`, токены из `design-system`.
- Адаптив: mobile-first, breakpoint `768px`.

## 5. Инструменты разработки

- Storybook 8 для UI состояний.
- Jest + Testing Library + MSW.
- Cypress e2e (smoke: регистрация, туториал, заказ, крафт).

## 6. DevOps и CI

- GitHub Actions: `lint`, `test`, `build`.
- Vite build, output на S3 + CloudFront (dev).

## 7. Чек-лист готовности

- [x] Определены модули и ответственности.
- [x] Связаны API/ Kafka каналы.
- [x] Прописаны инструменты и CI.
- [ ] Обновить Storybook сценарии под новые экраны (frontend team).

---

**Следующее действие:** актуализировать roadmap `FRONT-WEB` и завести задачи на модули MVP.
