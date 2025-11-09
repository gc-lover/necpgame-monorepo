# Personal NPC Tool API

## Структура файлов

- `personal-npc-tool.yaml` — базовые операции с персональными NPC (CRUD, роли, аудит, финансы)
- `personal-npc-tool-models.yaml` — общие модели NPC, статистики и аудита
- `personal-npc-scenarios.yaml` — управление блупринтами и выполнением сценариев персональных NPC
- `personal-npc-scenarios-models.yaml` — модели сценариев, шагов и KPI
- `personal-npc-contracts.yaml` — управление контрактами, лицензиями и сертификатами NPC
- `personal-npc-contracts-models.yaml` — модели контрактов, лицензий и сертификатов

## Источник данных

- `.BRAIN/02-gameplay/social/personal-npc-tool.md` v1.0.0

## Примечания

- Все спецификации используют общие компоненты из `api/v1/shared/common/`
- Лимит размера файла < 400 строк соблюден за счёт разбивки на отдельные спецификации и модели

