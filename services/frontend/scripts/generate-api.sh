#!/bin/bash

# Скрипт для генерации TypeScript клиента из API Swagger спецификаций
# Использование: ./scripts/generate-api.sh <путь-к-api-swagger-file> <output-dir>

set -e

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Проверка аргументов
if [ $# -lt 2 ]; then
    echo -e "${RED}Ошибка: Недостаточно аргументов${NC}"
    echo "Использование: $0 <путь-к-api-swagger-file> <output-dir>"
    echo "Пример: $0 ../openapi/api/v1/gameplay/social/personal-npc-tool.yaml src/api/generated/personal-npc-tool"
    exit 1
fi

SWAGGER_FILE=$1
OUTPUT_DIR=$2

# Проверка существования файла
if [ ! -f "$SWAGGER_FILE" ]; then
    echo -e "${RED}Ошибка: Файл $SWAGGER_FILE не найден${NC}"
    exit 1
fi

# Проверка установки OpenAPI Generator
if ! command -v openapi-generator-cli &> /dev/null; then
    echo -e "${YELLOW}OpenAPI Generator не установлен. Устанавливаю...${NC}"
    npm install -g @openapitools/openapi-generator-cli
fi

echo -e "${GREEN}Генерация TypeScript клиента...${NC}"
echo "  Файл: $SWAGGER_FILE"
echo "  Выходная директория: $OUTPUT_DIR"
echo ""

# Создание директории, если не существует
mkdir -p "$OUTPUT_DIR"

# Генерация кода
openapi-generator-cli generate \
  -i "$SWAGGER_FILE" \
  -g typescript-axios \
  -o "$OUTPUT_DIR" \
  --additional-properties=supportsES6=true,withInterfaces=true,npmName=@necpgame/api-client,typescriptThreePlus=true

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Генерация завершена успешно!${NC}"
    echo ""
    echo "Следующие шаги:"
    echo "  1. Проверьте сгенерированный клиент в $OUTPUT_DIR"
    echo "  2. Настройте базовый URL API в configuration.ts"
    echo "  3. Создайте компоненты в src/components/"
    echo "  4. Создайте хуки в src/hooks/"
    echo "  5. Создайте сервисы в src/services/"
else
    echo -e "${RED}✗ Ошибка при генерации кода${NC}"
    exit 1
fi



























