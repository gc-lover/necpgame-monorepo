# Скрипт для создания GitHub Issues из pending документов

Этот документ описывает процесс создания Issues из файла `knowledge/analysis/tasks/github-issues-pending.yaml`.

## Использование

1. Прочитать файл `knowledge/analysis/tasks/github-issues-pending.yaml`
2. Для каждой секции создать Issue в GitHub
3. После создания Issue обновить соответствующие файлы:
   - Изменить `needs_task: true` на `needs_task: false`
   - Добавить `github_issue: <номер>`

## Список Issues для создания

См. `knowledge/analysis/tasks/github-issues-pending.yaml` для полного списка.

## Автоматизация

Можно использовать GitHub CLI или API для автоматического создания Issues:

```bash
gh issue create --title "[Quests] Seattle 2020-2029" --body-file issue-template.md --label "game-design,priority-low,content,lore"
```

