# Frontend очередь — статус `not_started`

- Лимит файла: ≤ 500 строк. При превышении создайте `not-started_0001.md`, `not-started_0002.md`.
- Формат записи:

```markdown
- **API:** api/v1/inventory/inventory-core.yaml (API-TASK-204)  
  **Записано:** 2025-11-09 00:50 — ФРОНТТАСК  
  **Комментарий:** backend.status=queued; подготовлен план `docs/modules/ECONOMY-INVENTORY-CORE.md`, ждём economy-service и генерацию клиента.
```

- После начала работы перенесите запись в `in-progress.md`.

```markdown
- **API:** api/v1/trade/trade-system.yaml (API-TASK-TBD)  
  **Записано:** 2025-11-09 16:50 — Frontend Agent  
  **Комментарий:** backend.status=not_started (trade-service отсутствует); подготовлен план `docs/modules/ECONOMY-TRADE-SYSTEM.md`, ждём постановки задачи и Orval генерации.
```

```markdown
- **API:** api/v1/social/npc-relationships/npc-relationships.yaml (API-TASK-096)  
  **Записано:** 2025-11-09 16:58 — Frontend Agent  
  **Комментарий:** backend.status=not_started; подготовлен план `docs/modules/SOCIAL-NPC-RELATIONSHIPS.md`, ожидаем реализацию social-service и генерацию клиента.
```

```markdown
- **API:** api/v1/narrative/hybrid-media-references/hybrid-media-references.yaml (API-TASK-095)  
  **Записано:** 2025-11-09 17:05 — Frontend Agent  
  **Комментарий:** backend.status=not_started (narrative-service); подготовлен план `docs/modules/NARRATIVE-HYBRID-MEDIA-REFERENCES.md`, ждём включение сервиса и генерацию клиента.
```

```markdown
- **API:** api/v1/characters/players/players.yaml (API-TASK-097)  
  **Записано:** 2025-11-09 17:12 — Frontend Agent  
  **Комментарий:** backend.status=queued (character-service не реализован); подготовлен план `docs/modules/CHARACTERS-PLAYER-LIFECYCLE.md`, ожидаем запуск сервиса и ген. клиента.
```

