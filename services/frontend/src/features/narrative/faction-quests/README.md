## Faction Quests

SPA-страница работы с API `api/v1/narrative/faction-quests.yaml`:

- список квестов с фильтрами по фракции/уровню, быстрый поиск;
- детальная карточка с ветвлениями и концовками (`/branches`, `/endings`);
- модуль доступности: `GET /character/{id}/available` и `GET /character/{id}/progress`.

Данные получают React Query хуки, сгенерированные Orval (`narrative/faction-quests/*`). Компоненты используют MUI, лимит 400 строк соблюдён.






