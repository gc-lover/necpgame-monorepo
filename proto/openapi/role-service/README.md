# Role Service - Enterprise-Grade Role-Based Access Control

## 📋 **Назначение**

Role Service предоставляет комплексную систему управления ролями и разрешениями для платформы NECPGAME с enterprise-grade RBAC (Role-Based Access Control) и fine-grained permissions.

## 🎯 **Функциональность**

### **Управление Ролями**
- **Role Creation**: Создание и настройка ролей
- **Role Assignment**: Назначение ролей пользователям
- **Role Hierarchy**: Иерархическая структура ролей
- **Role Templates**: Предопределенные шаблоны ролей

### **Управление Разрешениями**
- **Permission Definition**: Определение granular разрешений
- **Permission Groups**: Группировка связанных разрешений
- **Dynamic Permissions**: Runtime проверка разрешений
- **Context-Aware Access**: Контекстные разрешения

### **Безопасность и Аудит**
- **Permission Auditing**: Полный аудит изменений разрешений
- **Role Conflicts**: Обнаружение конфликтов ролей
- **Security Policies**: Принудительные политики безопасности
- **Compliance Tracking**: Отслеживание соответствия требованиям

## 📁 **Структура**

```
role-service/
├── main.yaml              # Enterprise-grade спецификация RBAC
└── README.md              # Эта документация
```

## 🔗 **Domain Inheritance**

Наследует от `infrastructure-entities.yaml` с добавлением:
- Role hierarchy management
- Permission matrix operations
- Security audit trails
- Policy enforcement mechanisms

## 📊 **Performance**

- **P99 Latency**: <25ms для permission checks
- **Memory**: <30KB per instance
- **Concurrent Checks**: 100,000+ permission validations/second
- **Caching**: Redis-based permission caching

## 🚀 **API Endpoints**

- `POST /roles` - Создание роли
- `GET /roles/{id}` - Получение роли
- `PUT /roles/{id}` - Обновление роли
- `DELETE /roles/{id}` - Удаление роли
- `POST /roles/{id}/assign` - Назначение роли пользователю
- `POST /permissions/check` - Проверка разрешений
- `GET /users/{id}/permissions` - Разрешения пользователя

*Полная спецификация в main.yaml*

---

*Role Service обеспечивает enterprise-grade RBAC для NECPGAME*