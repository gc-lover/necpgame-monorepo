# Настройка Git веток для проекта

## Текущая ситуация

- **Default branch в GitHub:** `main`
- **Локальная ветка:** `develop` (настроена для отслеживания `origin/develop`)
- **Статус:** Ветка `develop` настроена и отслеживает `origin/develop`

## Решение

### Вариант 1: Создать ветку `develop` на remote (рекомендуется)

Если вы хотите использовать Git Flow со веткой `develop`:

```bash
# Убедитесь, что вы на ветке develop
git checkout develop

# Отправьте ветку на remote
git push -u origin develop

# Установите develop как default branch (опционально)
# Через GitHub UI: Settings → Branches → Default branch → develop
```

### Вариант 2: Использовать только `main` (упрощенный вариант)

Если вы хотите использовать только `main`:

1. Обновите `.github/workflows/auto-create-branch.yml`:
   ```yaml
   git checkout -b ${{ steps.branch_name.outputs.branch_name }} main
   ```

2. Удалите локальную ветку `develop`:
   ```bash
   git checkout main
   git branch -d develop
   ```

## Рекомендуемая структура (Git Flow)

```
main (production)
  └─ защищена, только через PR
  └─ автоматический деплой

develop (integration)
  └─ все фичи объединяются сюда
  └─ тестирование перед релизом
  └─ создание feature веток отсюда

feature/issue-{number}-{description}
  └─ ветки для работы агентов
```

## Настройка после создания develop

После создания ветки `develop` на remote:

1. **Обновите default branch в GitHub:**
   - Settings → Branches → Default branch
   - Выберите `develop` (опционально)

2. **Проверьте настройку upstream:**
   ```bash
   git branch --set-upstream-to=origin/develop develop
   ```

3. **Проверьте синхронизацию:**
   ```bash
   git fetch origin
   git status
   ```

## Исправление ошибки "couldn't find remote ref develop"

Эта ошибка возникает, когда:
- Локальная ветка `develop` настроена отслеживать `origin/develop`
- Но ветка `origin/develop` не существует на remote

**Решение:**
1. Создайте ветку на remote: `git push -u origin develop`
2. Или удалите отслеживание: `git branch --unset-upstream develop`

## Текущий статус

После выполнения `git branch --set-upstream-to=origin/develop develop`:
- OK Локальная ветка `develop` настроена отслеживать `origin/develop`
- OK Ветка `origin/develop` существует на remote
- OK Git синхронизация должна работать корректно

## Следующие шаги

1. **Создайте ветку develop на remote:**
   ```bash
   git push -u origin develop
   ```

2. **Или переключитесь на main:**
   ```bash
   git checkout main
   git branch --set-upstream-to=origin/main main
   ```

3. **Перезапустите Cursor** после изменений

