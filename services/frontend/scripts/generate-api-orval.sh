#!/bin/bash
# Скрипт для генерации TypeScript клиента и React Query хуков из OpenAPI спецификаций
# Использует Orval для автоматической генерации type-safe API клиента
# Использование: ./scripts/generate-api-orval.sh

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
GRAY='\033[0;90m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

echo -e "${CYAN}╔═══════════════════════════════════════════════════════════╗${NC}"
echo -e "${CYAN}║       NECPGAME - API Code Generation (Orval)             ║${NC}"
echo -e "${CYAN}╚═══════════════════════════════════════════════════════════╝${NC}"
echo ""

# Проверка наличия node_modules
if [ ! -d "node_modules" ]; then
    echo -e "${YELLOW}WARNING  node_modules не найден. Устанавливаю зависимости...${NC}"
    npm install
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Ошибка при установке зависимостей${NC}"
        exit 1
    fi
    echo ""
fi

# Проверка наличия OpenAPI файлов
API_SWAGGER_PATH="../openapi/api/v1"
if [ ! -d "$API_SWAGGER_PATH" ]; then
    echo -e "${RED}❌ Директория services/openapi/api/v1 не найдена: $API_SWAGGER_PATH${NC}"
    echo -e "${YELLOW}   Убедитесь, что вы запускаете скрипт из директории services/frontend${NC}"
    exit 1
fi

echo -e "${GREEN}📋 Найденные OpenAPI спецификации:${NC}"
find "$API_SWAGGER_PATH" -name "*.yaml" -type f | while read file; do
    echo -e "${GRAY}   • ${file#../}${NC}"
done
echo ""

# Очистка старых сгенерированных файлов
echo -e "${YELLOW}🧹 Очистка старых сгенерированных файлов...${NC}"
GENERATED_PATH="src/api/generated"
if [ -d "$GENERATED_PATH" ]; then
    rm -rf "$GENERATED_PATH"
    echo -e "${GRAY}   ✓ Удалено: $GENERATED_PATH${NC}"
fi
echo ""

# Генерация кода с помощью Orval
echo -e "${GREEN}🚀 Запуск генерации кода...${NC}"
echo ""

npm run generate:api

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}╔═══════════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║           ✓ Генерация завершена успешно!                ║${NC}"
    echo -e "${GREEN}╚═══════════════════════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${CYAN}📦 Сгенерированные файлы находятся в:${NC}"
    echo -e "${WHITE}   • src/api/generated/${NC}"
    echo ""
    echo -e "${CYAN}🎯 Что дальше?${NC}"
    echo -e "${WHITE}   1. Проверьте сгенерированный код в src/api/generated/${NC}"
    echo -e "${WHITE}   2. Импортируйте хуки в компоненты:${NC}"
    echo -e "${GRAY}      import { useLogin, useRegister } from '@/api/generated/auth/auth'${NC}"
    echo -e "${WHITE}   3. Используйте в компонентах React Query хуки:${NC}"
    echo -e "${GRAY}      const { mutate: login } = useLogin()${NC}"
    echo -e "${WHITE}   4. Настройте QueryClientProvider в main.tsx${NC}"
    echo ""
    echo -e "${CYAN}📚 Документация:${NC}"
    echo -e "${WHITE}   • Orval: https://orval.dev/${NC}"
    echo -e "${WHITE}   • React Query: https://tanstack.com/query/latest${NC}"
    echo ""
else
    echo ""
    echo -e "${RED}╔═══════════════════════════════════════════════════════════╗${NC}"
    echo -e "${RED}║             ✗ Ошибка при генерации кода                 ║${NC}"
    echo -e "${RED}╚═══════════════════════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${YELLOW}🔍 Возможные причины:${NC}"
    echo -e "${WHITE}   1. Проверьте синтаксис OpenAPI спецификаций${NC}"
    echo -e "${WHITE}   2. Убедитесь, что все \$ref ссылки корректны${NC}"
    echo -e "${WHITE}   3. Проверьте путь к файлам в orval.config.ts${NC}"
    echo -e "${WHITE}   4. Проверьте логи выше для деталей ошибки${NC}"
    echo ""
    exit 1
fi
























