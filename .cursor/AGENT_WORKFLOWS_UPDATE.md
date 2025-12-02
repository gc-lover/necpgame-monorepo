# Сводка обновлений Workflow для всех агентов

## ✅ Обновлено:
1. **Backend** - Done
2. **Idea Writer** - Done
3. **Architect** - Done

## 📝 Нужно обновить:

### Database
- In Progress: `91d49623`
- Передача: `API Designer - Todo` (`3eddfee3`)

### API Designer
- In Progress: `ff20e8f2`
- Передача: `Backend - Todo` (`72d37d44`)

### Content Writer
- In Progress: `cf5cf6bb`
- Передача: `Backend - Todo` (`72d37d44`) для импорта в БД

### Network
- In Progress: `88b75a08`
- Передача: `Security - Todo` (`3212ee50`)

### Security
- In Progress: `187ede76`
- Передача: `DevOps - Todo` (`ea62d00f`)

### DevOps
- In Progress: `f5a718a4`
- Передача: `UE5 - Todo` (`fa5905fb`)

### UE5
- In Progress: `9396f45a`
- Передача: `QA - Todo` (`86ca422e`)

### UI/UX Designer
- In Progress: `dae97d56`
- Передача: `UE5 - Todo` (`fa5905fb`)

### QA
- In Progress: `251c89a6`
- Передача: `Game Balance - Todo` (`d48c0835`) или `Release - Todo` (`ef037f05`)

### Game Balance
- In Progress: `a67748e9`
- Передача: `Release - Todo` (`ef037f05`)

### Release
- In Progress: `67671b7e`
- Передача: `Done` (`98236657`)

### Performance
- In Progress: `1674ad2c`
- Возврат: `Backend - Todo` (`72d37d44`) или `UE5 - Todo` (`fa5905fb`)

### Stats
- In Progress: `a67748e9`
- Передача: `Done` (`98236657`)

## Шаблон для обновления:

```markdown
## 🚀 Быстрый старт

**Новичок?** Читай `.cursor/AGENT_SIMPLE_GUIDE.md` - там простой алгоритм работы в 4 шага!

---

## Workflow with Issues

### 📋 Понимание статуса

**`{Agent} - Todo`** = Задача ДЛЯ ТЕБЯ ({Agent} агента). Ты должен её взять!

### 🔄 Простой алгоритм

1. **НАЙТИ задачу:** `Status:"{Agent} - Todo"` (это задачи для тебя)
2. **ВЗЯТЬ задачу:** СРАЗУ обнови статус на `{Agent} - In Progress`
3. **РАБОТАТЬ:** {Специфичная работа агента}
4. **ПЕРЕДАТЬ:** Обнови статус согласно карте передачи ниже

### 📍 ID статусов

**Все ID в `.cursor/GITHUB_PROJECT_CONFIG.md`:**
- `{Agent} - In Progress`: `{id}`

**Карта передачи задач:**
- **{Условие}:** `{Next Agent} - Todo` (`{id}`)

**Пример обновления статуса:** См. `.cursor/AGENT_SIMPLE_GUIDE.md`
```

