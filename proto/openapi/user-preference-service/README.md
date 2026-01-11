# User Preference Service - Enterprise-Grade User Settings & Personalization

## 📋 **Назначение**

User Preference Service предоставляет комплексное управление настройками пользователей и персонализацией для платформы NECPGAME с enterprise-grade масштабируемостью.

## 🎯 **Функциональность**

### **Персонализация Интерфейса**
- **Themes & Appearance**: Темы, цвета, шрифты
- **Layout Preferences**: Настройки расположения элементов
- **Language Settings**: Выбор языка и локализации
- **Accessibility Options**: Настройки доступности

### **Уведомления и Коммуникации**
- **Notification Preferences**: Настройки уведомлений по типам
- **Communication Channels**: Предпочтения каналов связи
- **Quiet Hours**: Время отключения уведомлений
- **Frequency Settings**: Частота различных типов уведомлений

### **Игровые Настройки**
- **Game Preferences**: Настройки игрового опыта
- **Control Schemes**: Схемы управления
- **Difficulty Settings**: Предпочтения сложности
- **Performance Options**: Настройки производительности

### **Приватность и Безопасность**
- **Privacy Controls**: Настройки приватности
- **Data Sharing**: Управление обменом данными
- **Security Preferences**: Настройки безопасности
- **Cookie & Tracking**: Управление отслеживанием

## 📁 **Структура**

```
user-preference-service/
├── main.yaml              # Enterprise-grade спецификация настроек
└── README.md              # Эта документация
```

## 🔗 **Domain Inheritance**

Наследует от `infrastructure-entities.yaml` с добавлением:
- User preference categorization
- Validation and constraints
- Cross-platform synchronization
- Privacy-aware data handling

## 📊 **Performance**

- **P99 Latency**: <20ms для preference access
- **Memory**: <25KB per instance
- **Concurrent Access**: 50,000+ preference operations/second
- **Cache Efficiency**: 99%+ cache hit rate

## 🚀 **API Endpoints**

- `GET /users/{id}/preferences` - Получение настроек
- `PUT /users/{id}/preferences` - Обновление настроек
- `PATCH /users/{id}/preferences/{category}` - Частичное обновление
- `POST /users/{id}/preferences/reset` - Сброс к defaults
- `GET /preferences/categories` - Категории настроек

*Полная спецификация в main.yaml*

---

*User Preference Service обеспечивает персонализированный опыт для пользователей NECPGAME*