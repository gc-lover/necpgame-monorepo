#!/bin/bash
# Скрипт автоматического коммита для агентов (Linux/Mac)
# Использование: ./autocommit.sh [сообщение коммита]

COMMIT_MESSAGE="${1:-Автоматический коммит: обновления от агента}"

# Получаем корневую директорию репозитория
REPO_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
if [ -z "$REPO_ROOT" ]; then
    echo "Ошибка: Не найден git репозиторий в текущей директории" >&2
    exit 1
fi

cd "$REPO_ROOT" || exit 1

# Проверяем, есть ли изменения для коммита
if [ -z "$(git status --porcelain)" ]; then
    echo "Нет изменений для коммита"
    exit 0
fi

# Добавляем все изменения
echo "Добавление изменений..."
git add -A

# Генерируем сообщение коммита, если не указано явно
if [ "$COMMIT_MESSAGE" = "Автоматический коммит: обновления от агента" ]; then
    CHANGED_FILES=$(git diff --cached --name-only)
    
    if [ -n "$CHANGED_FILES" ]; then
        # Подсчитываем количество файлов (исключаем пустые строки)
        FILE_COUNT=$(echo "$CHANGED_FILES" | grep -v '^$' | wc -l | tr -d ' \t' || echo "0")
        
        # Определяем тип изменений
        ACTION="Обновление"
        if echo "$CHANGED_FILES" | grep -q "\.md$"; then
            ACTION="Документация"
        elif echo "$CHANGED_FILES" | grep -q "\.\(yaml\|yml\)$"; then
            ACTION="API спецификация"
        elif echo "$CHANGED_FILES" | grep -q "\.\(go\|java\|js\|ts\|py\)$"; then
            ACTION="Реализация"
        elif echo "$CHANGED_FILES" | grep -q "rules\.mdc$"; then
            ACTION="Обновление правил"
        fi
        
        COMMIT_MESSAGE="$ACTION: изменения в файлах ($FILE_COUNT файлов)"
    fi
fi

# Делаем коммит
echo "Создание коммита: $COMMIT_MESSAGE"
if ! git commit -m "$COMMIT_MESSAGE"; then
    echo "Ошибка при создании коммита" >&2
    exit 1
fi

echo "Коммит создан успешно"

# Определяем текущую ветку
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD 2>/dev/null)
if [ -z "$CURRENT_BRANCH" ]; then
    echo "Предупреждение: Не удалось определить текущую ветку, используем 'main'" >&2
    CURRENT_BRANCH="main"
fi

# Отправляем изменения
echo "Отправка изменений в GitHub (ветка: $CURRENT_BRANCH)..."
if ! git push origin "$CURRENT_BRANCH"; then
    echo "Предупреждение: Не удалось отправить изменения" >&2
    echo "Изменения закоммичены локально, но не отправлены" >&2
    # Не выходим с ошибкой, т.к. коммит уже создан
    exit 0
fi

echo "Изменения успешно отправлены в GitHub"
exit 0


