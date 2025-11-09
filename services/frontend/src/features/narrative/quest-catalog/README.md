## Quest Catalog

Обзорная страница для `api/v1/narrative/quest-catalog.yaml`:

- фильтрация каталога (`type`, `period`, `difficulty`, уровни, романс, бой);
- контекстный поиск (`GET /search`);
- подробности квеста: цели, требования, диалоговое дерево, лут;
- рекомендации для персонажа (`GET /recommendations/{characterId}`).

Компоненты используют хуки Orval (`narrative/quest-catalog/*`), рендерятся на основе MUI. Код укладывается в лимит 400 строк.






