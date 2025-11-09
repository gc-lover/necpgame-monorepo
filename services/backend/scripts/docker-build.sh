#!/bin/bash
# Скрипт для сборки Docker образа конкретного микросервиса
# Использование: ./scripts/docker-build.sh <service> [tag] [--no-cache]

set -e  # Выход при ошибке

if [ $# -lt 1 ]; then
    echo "Usage: ./scripts/docker-build.sh <service> [tag] [--no-cache]"
    exit 1
fi

SERVICE="$1"
shift

SERVICE_NORMALIZED=$(echo "$SERVICE" | tr '[:upper:]' '[:lower:]')
TAG="latest"
NO_CACHE=""

for ARG in "$@"; do
    case "$ARG" in
        --no-cache)
            NO_CACHE="--no-cache"
            ;;
        *)
            if [ "$TAG" = "latest" ]; then
                TAG="$ARG"
            else
                echo "Неизвестный аргумент: $ARG"
                exit 1
            fi
            ;;
    esac
done

echo "🐳 Сборка Docker образа для микросервиса '$SERVICE_NORMALIZED'..."

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
cd "$PROJECT_ROOT"

CANDIDATES=(
    "microservices/${SERVICE_NORMALIZED}"
    "infrastructure/${SERVICE_NORMALIZED}"
)

CONTEXT_DIR=""
for DIR in "${CANDIDATES[@]}"; do
    if [ -d "$DIR" ]; then
        CONTEXT_DIR="$DIR"
        break
    fi
done

if [ -z "$CONTEXT_DIR" ]; then
    echo "❌ Директория микросервиса '${SERVICE_NORMALIZED}' не найдена. Ожидается microservices/${SERVICE_NORMALIZED} или infrastructure/${SERVICE_NORMALIZED}."
    exit 1
fi

if [ -f "${CONTEXT_DIR}/Dockerfile" ]; then
    DOCKERFILE="${CONTEXT_DIR}/Dockerfile"
elif [ -f "${CONTEXT_DIR}/docker/Dockerfile" ]; then
    DOCKERFILE="${CONTEXT_DIR}/docker/Dockerfile"
else
    echo "❌ Dockerfile для микросервиса '${SERVICE_NORMALIZED}' не найден. Создайте файл ${CONTEXT_DIR}/Dockerfile."
    exit 1
fi

if [ ! -d "../API-SWAGGER" ]; then
    echo "⚠️  Директория API-SWAGGER не найдена. Убедитесь, что спецификации доступны перед сборкой."
fi

# Параметры сборки
IMAGE_NAME="necpgame-${SERVICE_NORMALIZED}"
FULL_TAG="${IMAGE_NAME}:${TAG}"

echo "📦 Сборка образа: $FULL_TAG"
echo "📄 Dockerfile: $DOCKERFILE"
echo "📂 Контекст: $CONTEXT_DIR"

# Выполнение сборки
echo "🔨 Начало сборки..."
docker build $NO_CACHE -t "$FULL_TAG" -f "$DOCKERFILE" "$CONTEXT_DIR"

if [ $? -eq 0 ]; then
    echo "✅ Образ успешно собран: $FULL_TAG"
    
    # Показываем информацию об образе
    echo "📊 Информация об образе:"
    docker images "$IMAGE_NAME" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"
    
    echo "💡 Для запуска используйте:"
    case "$SERVICE_NORMALIZED" in
        auth-service) PORT=8081 ;;
        character-service) PORT=8082 ;;
        gameplay-service) PORT=8083 ;;
        social-service) PORT=8084 ;;
        economy-service) PORT=8085 ;;
        world-service) PORT=8086 ;;
        api-gateway) PORT=8080 ;;
        *) PORT="" ;;
    esac
    if [ -n "$PORT" ]; then
        echo "   docker run -p ${PORT}:${PORT} $FULL_TAG"
    else
        echo "   docker run $FULL_TAG"
    fi
else
    echo ""
    echo "❌ Ошибка при сборке образа!"
    exit 1
fi










