# Main Quest Template Rollout — Readiness Plan

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** Narrative Guild  
**Связанные документы:** `../main-quests-outline.md`, `../QUEST-TEMPLATE-DND.md`, `../../npc-lore/NPC-TEMPLATE.md`

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-08
**api-readiness-notes:** План подготовки нарратива. Используется для менеджмента готовности.

---

## 1. Цель

- Упаковать основные квесты MVP в единый формат `QUEST-TEMPLATE.md`.
- Связать квесты с ключевыми NPC и определить приоритеты проработки.
- Обозначить дедлайны и ответственных для передачи в Narrative/Quest агентам.

## 2. Матрица квестов

| ID | Название | Шаблон | Приоритет | Ответственный | Дедлайн | Статус |
|----|----------|--------|-----------|---------------|---------|--------|
| MQ-001 | Первые шаги | `main/mq-001-first-steps.md` | P0 | Lead Writer | 2025-11-10 | in_progress |
| MQ-002 | Выбор пути | `main/mq-002-choose-path.md` | P0 | Faction Writer | 2025-11-11 | planned |
| MQ-003 | Развитие навыков | `main/mq-003-skill-growth.md` | P1 | Class Writer | 2025-11-12 | planned |
| MQ-101 | Корпоративные войны: выбор стороны | `main/mq-101-corp-side.md` | P1 | Corpo Writer | 2025-11-14 | planned |
| MQ-102 | Корпоративная операция | `main/mq-102-corp-op.md` | P1 | Corpo Writer | 2025-11-16 | planned |
| MQ-201 | Уличные войны: выбор банды | `main/mq-201-street-choice.md` | P1 | Street Writer | 2025-11-12 | planned |
| MQ-202 | Бандитская операция | `main/mq-202-street-op.md` | P1 | Street Writer | 2025-11-15 | planned |

## 3. Связанные NPC

| Квест | NPC | Файл | Статус |
|-------|-----|------|--------|
| MQ-001 | Виктор Вектор | `npc-lore/important/viktor-vektor.md` | draft |
| MQ-002 | Hanako Arasaka, Padre Ibarra | `npc-lore/important/hanako-arasaka.md`, `npc-lore/important/padre-ibarra.md` | needs-update |
| MQ-003 | Rogue, Виктор Вектор | `npc-lore/important/rogue.md`, `npc-lore/important/viktor-vektor.md` | draft |
| MQ-101 | Anders Hellman | `npc-lore/important/anders-hellman.md` | draft |
| MQ-102 | Yorinobu Arasaka, Джеймс Рид | `npc-lore/important/yorinobu-arasaka.md`, `npc-lore/important/militech-james-reed.md` | missing |
| MQ-201 | Хосе Рамирес, Ройс | `npc-lore/important/hoseramirez.md`, `npc-lore/important/royce.md` | missing |
| MQ-202 | Sasquatch, Susie Q | `npc-lore/important/sasquatch.md`, `npc-lore/important/susie-q.md` | ready |

## 4. План работ

1. Создать файлы `mq-*.md` на основе `QUEST-TEMPLATE.md`.  
2. Привязать квесты к NPC и обновить `important-npcs-list.md`.  
3. Обновить `npc-status-tracker` — завести карточки NPC.  
4. Провести ревью (Narrative Lead, Lore Keeper).

## 5. Статус готовности

- [x] Сформированы ID и ссылки на шаблоны.
- [x] Определены приоритеты и дедлайны.
- [ ] Созданы файлы `mq-001`, `mq-002`, `mq-003` по шаблону.
- [ ] Обновлены файлы NPC (Hanako, Padre, James Reed, Royce).

## 6. Рекомендации по шаблону

- Использовать `03-lore/timeline-author/quests/QUEST-TEMPLATE.md`.
- Минимум: синопсис, ветвления, цели, награды, связанные системы.
- Добавить блок `api-hooks` для связи с `quest-engine-backend.md`.

---

**Следующее действие:** подготовить `mq-001-first-steps.md` до 2025-11-10 и отправить на ревью Narrative Lead.