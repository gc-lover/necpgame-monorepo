# GitHub Issues Automation Scripts

## Обзор

Набор скриптов для автоматического создания GitHub Issues из документа `github-issues-pending.yaml`.

## Созданные Issues

### Категории (13 Issues всего):
1. **Квесты America (_03-lore)** - 4 issues
   - Seattle 2020-2029 (5 квестов)
   - Los Angeles 2020-2029 (5 квестов)
   - Las Vegas 2020-2029 (8 квестов)
   - Miami, Detroit, Mexico City 2020-2029 (6 квестов)

2. **Квесты Europe (_03-lore)** - 3 issues
   - Berlin 2020-2039 (6 квестов)
   - Amsterdam 2020-2029 (10 квестов)
   - Brussels 2020-2029 (10 квестов)

3. **Side Quests 2078** - 1 issue
   - Side Quests 2078 период (5 квестов)

4. **Регионы (_03-lore)** - 3 issues
   - Европейские города 2020-2093 (11 городов)
   - Азиатские и Ближневосточные города (10 городов)
   - Океания и континентальные регионы (9 документов)

5. **Implementation документы** - 1 issue
   - Global State система (5 документов)

6. **Analysis документы** - 1 issue
   - Cursor Agents Syntax документация

## Скрипты

### `create-github-issues-from-yaml.py`
Основной скрипт для создания GitHub Issues.

**Использование:**
```bash
# Создать все issues
python scripts/create-github-issues-from-yaml.py

# Создать только определенную категорию
python scripts/create-github-issues-from-yaml.py --category america_quests
```

**Особенности:**
- Парсит `github-issues-pending.yaml`
- Создает структурированные Issues с checklist
- Добавляет правильные метки и приоритеты
- Сохраняет результат в `github-issues-created.json`

### `create-github-issues-simple.py`
Простой анализатор для проверки структуры документа.

**Использование:**
```bash
python scripts/create-github-issues-simple.py
```

## Структура Issue

Каждый созданный Issue содержит:
- **Заголовок**: Название группы задач
- **Категория**: Тип контента (квесты, регионы, etc.)
- **Список файлов**: Checkbox список для обработки
- **Требования**: Стандартные критерии качества
- **Workflow**: Следующие шаги по агентам
- **Метки**: Приоритет, тип, автоматизация

## Следующие шаги

1. **Получить доступ к GitHub API**
2. **Запустить скрипт** для создания реальных Issues
3. **Назначить ответственных агентов** на созданные Issues
4. **Мониторить прогресс** выполнения задач

## Формат меток

- `P1/P2/P3` - приоритет
- `game-design/content/lore` - тип контента
- `backend/infrastructure` - технические системы
- `automation/task-creation` - метки автоматизации

## Файлы результатов

- `github-issues-created.json` - JSON со всеми созданными Issues
- Содержит полные описания, метки и структуру для GitHub API

---

*Создано автоматически для проекта NECPGAME*