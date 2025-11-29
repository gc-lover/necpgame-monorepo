# UI/UX Designer Agent - Документация

## Обзор

UI/UX Designer Agent создан для проработки дизайна интерфейсов и UX механик для игровых систем. Агент работает между Idea Writer (концепция) и UE5 Developer (реализация).

## Workflow

```
Idea Writer (концепция UI) 
    ↓
UI/UX Designer (дизайн интерфейса)
    ↓
UE5 Developer (реализация в UE5)
```

## Метки GitHub

Для работы с новым агентом необходимо создать следующие метки в GitHub:

- `agent:ui-ux-designer` - метка агента
- `stage:ui-design` - этап разработки
- `ui` - категория (уже существует)
- `ux` - категория (уже существует)
- `design` - категория (уже существует)

## Development Stage в Project

В GitHub Project необходимо добавить новый этап:
- **Development Stage:** `ui-design`

## Область ответственности

UI/UX Designer отвечает за:
- Создание дизайн-документов для UI интерфейсов
- Проработку UX механик и пользовательских сценариев
- Создание wireframes и макетов интерфейсов
- Визуальный дизайн в стиле Cyberpunk 2077
- Описание анимаций и переходов
- Интеграцию с игровыми механиками
- Адаптивность и доступность интерфейсов

## Входные данные

- Концепция UI от Idea Writer (Issue с меткой `stage:idea`)
- Требования к интерфейсу
- Существующие дизайн-документы
- Стиль игры (Cyberpunk 2077)

## Выходные данные

- Дизайн-документы в `knowledge/design/ui/` (YAML или Markdown)
- Описание визуального дизайна
- Wireframes и макеты
- UX механики и пользовательские сценарии
- Описание анимаций и переходов

## Интеграция с другими агентами

### Idea Writer → UI/UX Designer
- Idea Writer передает UI задачи напрямую UI/UX Designer
- Не требует прохождения через Architect

### Architect → UI/UX Designer
- Architect передает UI задачи UI/UX Designer
- Не создает техническую архитектуру для UI задач

### UI/UX Designer → UE5 Developer
- UI/UX Designer передает готовый дизайн UE5 Developer
- UE5 Developer реализует дизайн в UE5

## Примеры задач

- `UI: Hacking System - Интерфейс взлома и киберпространства`
- `UI: Arena System - Интерфейс арены`
- `UI: Progression System - Интерфейс навыков и атрибутов`
- `UI: Implants System - Интерфейс управления имплантами`

## Файлы правил

- `.cursor/rules/agent-ui-ux-designer.mdc` - основные правила агента
- Обновлены правила для:
  - `agent-idea-writer.mdc` - передача UI задач
  - `agent-architect.mdc` - передача UI задач
  - `agent-ue5.mdc` - прием задач от UI/UX Designer

## Следующие шаги

1. **Создать метки в GitHub:**
   - `agent:ui-ux-designer`
   - `stage:ui-design`

2. **Добавить Development Stage в Project:**
   - `ui-design`

3. **Обновить существующие UI Issues:**
   - Назначить метку `agent:ui-ux-designer`
   - Установить `Development Stage` = `ui-design`

4. **Начать работу с UI задачами:**
   - Использовать нового агента для проработки дизайна интерфейсов

