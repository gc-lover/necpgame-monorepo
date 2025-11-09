# NPC Readiness — Prioritization Matrix

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** Lore Keeper  
**Связанные документы:** `important-npcs-list.md`, `NPC-TEMPLATE.md`, `../quests/main/main-quests-template-plan.md`

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-08
**api-readiness-notes:** Управленческий документ для нарратива.

---

## 1. Цель

- Завершить портирование ключевых NPC в формат `NPC-TEMPLATE.md`.
- Согласовать приоритет подготовки NPC для основных квестов.
- Обозначить дедлайны и владельцев.

## 2. Матрица NPC

| NPC | Квесты | Шаблон | Приоритет | Владелец | Дедлайн | Статус |
|-----|--------|--------|-----------|----------|---------|--------|
| Виктор Вектор | MQ-001, MQ-003 | `important/viktor-vektor.md` | P0 | MedTech Writer | 2025-11-09 | in_review |
| Padre Ibarra | MQ-002, MQ-201 | `important/padre-ibarra.md` | P0 | Street Writer | 2025-11-10 | draft |
| Hanako Arasaka | MQ-002, MQ-101 | `important/hanako-arasaka.md` | P0 | Corpo Writer | 2025-11-10 | draft |
| James "Iron" Reed | MQ-102 | `important/militech-james-reed.md` | P1 | Corpo Writer | 2025-11-12 | missing |
| Royce | MQ-201 | `important/royce.md` | P1 | Street Writer | 2025-11-11 | missing |
| Marko "Fix" Sanchez | MQ-001 | `important/marko-sanchez.md` | P1 | Fixer Writer | 2025-11-11 | draft |
| Anders Hellman | MQ-101 | `important/anders-hellman.md` | P1 | Corpo Writer | 2025-11-12 | draft |
| Susie Q | MQ-202 | `important/susie-q.md` | P2 | Nightlife Writer | 2025-11-14 | ready |

## 3. Требования к шаблону

- Использовать блоки `Motivation`, `Arcs`, `Quest Hooks`, `Dialogue Beats`.
- Добавить поле `readiness` (draft, in_review, ready).
- После обновления синхронизировать с `npc-status-tracker`.

## 4. Чек-лист

- [x] Определены приоритеты и владельцы.
- [x] Согласован список с `main-quests-template-plan.md`.
- [ ] Обновлены файлы NPC до статуса `ready`.

---

**Следующее действие:** Завершить Padre/Hanako до 2025-11-10 и провести ревью 2025-11-11.