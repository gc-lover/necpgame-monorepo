# Check Workflow Status

Проверка статуса GitHub Actions workflow runs и jobs через GitHub REST API.

## Использование

Спросить агента: "Проверь статус последних workflow runs" или "Какие CI jobs сейчас выполняются?"

## Реализация через GitHub REST API

Для получения информации о workflow runs нужен GitHub Personal Access Token (PAT) с правами `actions:read`.

### 1. Получить список последних workflow runs

```bash
curl -H "Authorization: token $GITHUB_TOKEN" \
     -H "Accept: application/vnd.github+json" \
     https://api.github.com/repos/gc-lover/necpgame-monorepo/actions/runs?per_page=10
```

### 2. Получить детали конкретного run

```bash
curl -H "Authorization: token $GITHUB_TOKEN" \
     -H "Accept: application/vnd.github+json" \
     https://api.github.com/repos/gc-lover/necpgame-monorepo/actions/runs/{run_id}
```

### 3. Получить jobs для run

```bash
curl -H "Authorization: token $GITHUB_TOKEN" \
     -H "Accept: application/vnd.github+json" \
     https://api.github.com/repos/gc-lover/necpgame-monorepo/actions/runs/{run_id}/jobs
```

### 4. Получить workflow run для конкретного коммита

```bash
curl -H "Authorization: token $GITHUB_TOKEN" \
     -H "Accept: application/vnd.github+json" \
     https://api.github.com/repos/gc-lover/necpgame-monorepo/actions/runs?head_sha={commit_sha}
```

## Использование скрипта

Использовать Python скрипт `scripts/check_workflow_status.py`:

```bash
python scripts/check_workflow_status.py --commit 6001e3813
python scripts/check_workflow_status.py --latest
python scripts/check_workflow_status.py --workflow ci-backend.yml
```

## Альтернатива: GitHub CLI

Если установлен `gh` CLI:

```bash
gh run list --limit 10
gh run view {run_id}
gh run view {run_id} --log
gh run list --workflow=ci-backend.yml
gh run watch {run_id}
```

## Через коммиты

Для конкретного коммита можно получить связанные workflow runs через SHA коммита.

## Интеграция с Project
- Issues с отчетами по workflow добавляй в Project с полями: Status `Todo`, Agent `DevOps`.

