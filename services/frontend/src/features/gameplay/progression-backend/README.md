## Progression Backend

Страница визуализирует ключевые эндпоинты `api/v1/progression/progression-backend.yaml`:

- `GET /experience`, `POST /experience/award` — текущее состояние опыта и выдача XP;
- `GET /attributes`, `POST /attributes/spend` — просмотр и распределение очков атрибутов;
- `GET /skills`, `POST /skills/{skillId}/experience` — список навыков и прокачка;
- `GET /milestones` — прогрессионные вехи персонажа.

Используются компоненты `ExperienceOverview`, `AttributesPanel`, `SkillsList`, `MilestonesList`. Данные подгружаются через хуки Orval (`progression/backend/*`), поддерживаются ошибки и состояния загрузки. UI построен на MUI, размеры файлов < 400 строк.






