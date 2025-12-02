# ⏱️ SLA Guidelines для конвейера

**Service Level Agreement для каждого этапа разработки**

## 🎯 Цели SLA

- **Предсказуемость:** Знать сколько займет каждый этап
- **Контроль:** Обнаруживать застрявшие задачи
- **Оптимизация:** Находить узкие места конвейера

## 📊 SLA по этапам

### Обычный конвейер

| Этап | SLA | Описание | При превышении |
|------|-----|----------|----------------|
| **Idea Writer** | 1 день | Описать идею, лор, концепцию | Alert → Blocked? |
| **Architect** | 2 дня | Спроектировать архитектуру | Alert → Escalate to lead |
| **Database** | 1 день | Схемы БД, миграции | Alert → Check workload |
| **API Designer** | 1 день | OpenAPI спецификация | Alert → Check dependencies |
| **Backend** | 3 дня | Реализация Go сервиса | Alert → Check complexity |
| **Network** | 1 день | Envoy, протокол, тикрейт | Alert → Check dependencies |
| **Security** | 1 день | Аудит безопасности | Alert → Check findings |
| **DevOps** | 1 день | Docker, K8s, CI/CD | Alert → Check infrastructure |
| **UE5** | 3 дня | Клиент, UI, механика | Alert → Check complexity |
| **QA** | 1 день | Тестирование, баги | Alert → Check quality |
| **Release** | 1 день | Релизные заметки, деплой | Alert → Check readiness |

### **Итого:** ~16 дней для обычной задачи

### Fast Track конвейер

| Этап | SLA | При превышении |
|------|-----|----------------|
| **Backend/UE5** | 4 часа | Alert → НЕ Fast Track? |
| **QA** | 2 часа | Alert → Проблемы? |
| **Release** | 1 час | Alert → Escalate |

### **Итого:** <1 день для Fast Track задачи

### Контент-квесты

| Этап | SLA | При превышении |
|------|-----|----------------|
| **Idea Writer** | 1 день | Alert |
| **Content Writer** | 2 дня | Alert → Check quest complexity |
| **Backend** (import) | 2 часа | Alert → Check DB issues |
| **QA** | 4 часа | Alert |
| **Release** | 1 час | - |

### **Итого:** ~4 дня для контент-квеста

## 🚨 Алерты и эскалация

### Уровни алертов

**🟢 Normal (в пределах SLA)**
- Задача в работе
- Прогресс есть
- Никаких действий не требуется

**🟡 Warning (80% SLA использовано)**
- Задача близка к превышению SLA
- Проверить: есть ли прогресс?
- Возможна помощь агенту?

**🔴 Critical (SLA превышен)**
- Задача превысила SLA
- **Действие:** Alert → Check status
- Возможна блокировка?
- Нужна эскалация?

### Автоматические действия

**При превышении SLA:**

1. **Отправить уведомление** (GitHub comment или external notification)
2. **Проверить статус:**
   - `In Progress` → Возможно задача сложнее чем ожидалось
   - `Blocked` → Нужно разблокировать
   - `Review` → Ускорить review
3. **Эскалировать если нужно:**
   - Привлечь tech lead
   - Обсудить проблемы
   - Пересмотреть задачу

## 📈 Метрики SLA

### Отслеживать:

```markdown
## SLA Compliance

| Агент | Задач | В SLA | Превысили SLA | Compliance % |
|-------|-------|-------|---------------|--------------|
| Backend | 20 | 17 | 3 | 85% |
| QA | 15 | 14 | 1 | 93% |
| Architect | 10 | 8 | 2 | 80% |
```

**Цель:** >90% compliance для каждого агента

### Среднее время на этапах:

```markdown
## Avg Time per Stage

| Этап | SLA | Среднее | P50 | P95 |
|------|-----|---------|-----|-----|
| Backend | 3 дня | 2.5 дня | 2 дня | 5 дней |
| QA | 1 день | 0.8 дня | 0.5 дня | 2 дня |
```

## 🔧 Настройка мониторинга SLA

### GitHub Action для проверки SLA

**Создать:** `.github/workflows/sla-monitor.yml`

```yaml
name: SLA Monitor

on:
  schedule:
    - cron: '0 */4 * * *'  # Каждые 4 часа
  workflow_dispatch:

jobs:
  check-sla:
    runs-on: ubuntu-latest
    steps:
      - name: Check SLA violations
        uses: actions/github-script@v7
        with:
          script: |
            const SLA_HOURS = {
              'Idea Writer': 24,
              'Architect': 48,
              'Database': 24,
              'API Designer': 24,
              'Backend': 72,
              'Network': 24,
              'Security': 24,
              'DevOps': 24,
              'UE5': 72,
              'QA': 24,
              'Release': 24
            };
            
            // Получить все In Progress задачи
            const issues = await github.rest.issues.listForRepo({
              owner: context.repo.owner,
              repo: context.repo.repo,
              state: 'open',
              labels: 'in-progress'  // Приблизительно
            });
            
            for (const issue of issues.data) {
              // Проверить время в текущем статусе
              // Сравнить с SLA
              // Если превышено → создать alert comment
            }
```

### Manual проверка

**Использовать Stats Agent:**

```javascript
// Запрос задач с превышением SLA
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Status:"Backend - In Progress" updated:<@today-3d'
  // Задачи в Backend > 3 дней
});
```

## 🎯 Исключения из SLA

**SLA может быть превышен в случаях:**

### 1. Блокировка

- Задача в статусе `Blocked`
- Ожидание внешних зависимостей
- SLA временно приостановлен

### 2. Review

- Задача в статусе `Review`
- Проходит внутренняя проверка
- SLA продлевается на +50%

### 3. Returned

- Задача возвращена предыдущему агенту
- SLA сбрасывается при возврате
- Новый SLA начинается с момента возврата

### 4. Сложность

**Добавить label `complex`:**
- SLA увеличивается x1.5
- Для Backend: 3 дня → 4.5 дня
- Для Architect: 2 дня → 3 дня

## 📋 SLA Checklist для агентов

### При взятии задачи:

- [ ] Проверил SLA для моего этапа
- [ ] Оценил сложность (нужен `complex` label?)
- [ ] Есть ли блокирующие зависимости? (нужен `Blocked`?)

### Во время работы:

- [ ] Проверяю прогресс ежедневно
- [ ] Если приближаюсь к 80% SLA → ускоряюсь или прошу помощи
- [ ] Если блокировка → сразу статус `Blocked` + комментарий

### При передаче:

- [ ] Прошел валидацию (`/agent-validate-result`)
- [ ] Передал в пределах SLA
- [ ] Добавил комментарий с summary

## 🚫 Что делать при превышении SLA

### Шаг 1: Проверить причину

```markdown
**Почему превышено:**
- Задача сложнее чем ожидалось?
- Были блокировки?
- Недостаточно информации?
- Технические проблемы?
```

### Шаг 2: Обновить статус

```javascript
// Если блокировка
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '{agent_blocked_id}'  // Например: '504999e1' для Backend - Blocked
  }
});

// Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: '⚠️ **SLA exceeded: Task blocked**\n\n' +
        '**Reason:**\n' +
        '- Waiting for OpenAPI spec update\n' +
        '- Cannot proceed without architecture clarification\n\n' +
        '**Action needed:**\n' +
        '- API Designer: update spec\n' +
        '- Architect: clarify component interaction\n\n' +
        '**Status updated:** `Backend - Blocked`\n\n' +
        'Issue: #' + issue_number
});
```

### Шаг 3: Эскалировать если нужно

**Если блокировка >2 дней:**
- Привлечь tech lead
- Создать встречу для обсуждения
- Возможно нужен другой подход

## 📊 Dashboard для SLA

**Создать dashboard с метриками:**

```markdown
# 📊 SLA Dashboard (обновляется каждые 4 часа)

## 🎯 Общий SLA Compliance: 87%

## По агентам:

### 🟢 В пределах SLA:
- QA: 14/15 задач (93%)
- Release: 10/10 задач (100%)
- API Designer: 8/9 задач (89%)

### 🟡 Близко к превышению:
- Backend: 5 задач >80% SLA
- UE5: 3 задачи >80% SLA

### 🔴 SLA превышен:
- Backend: 3 задачи (Issue #123, #145, #167)
- Architect: 2 задачи (Issue #89, #102)

## 📈 Тренды:
- Среднее время Backend: 2.5 дня (↓ от 3 дней)
- Среднее время QA: 0.8 дня (↓ от 1 дня)
- % Fast Track: 25% (↑ от 20%)
```

## ✅ Итого

**SLA помогает:**
- ✅ Контролировать скорость конвейера
- ✅ Находить узкие места
- ✅ Предсказывать время выполнения
- ✅ Обнаруживать проблемы рано

**Используй SLA как ориентир, не как жесткое ограничение!**

Качество важнее скорости. Если задача сложная и требует больше времени → это нормально, просто обновляй статус и коммуницируй.

