---
**api-readiness:** needs-work  
**api-readiness-check-date:** 2025-11-09 09:34
**api-readiness-notes:** Перепроверено 2025-11-09 09:34. По-прежнему отсутствуют структурированные API контракты для витрин, SQL/ETL схемы и согласованная матрица KPI; требуется детализация перед постановкой задач.
---

# Player Market Analytics - Аналитика и метрики

**Статус:** in-review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Дата обновления:** 2025-11-09 09:34  
**Приоритет:** высокий  
**Автор:** AI Brain Manager

**Микрофича:** Analytics & monitoring  
**Размер:** ~180 строк ✅

---

- **Status:** in-review
- **Last Updated:** 2025-11-09 09:34
---

## Метрики

**Volume Metrics:**
```
market.listings.active - активные объявления
market.listings.created.daily - создано за день
market.trades.completed.daily - сделок за день
market.volume.eurodollars.daily - оборот в деньгах
```

**Performance Metrics:**
```
market.search.time - время поиска
market.purchase.time - время покупки
market.listing.creation.time - время создания
```

**User Metrics:**
```
market.sellers.active - активные продавцы
market.buyers.active - активные покупатели
market.sellers.rating.average - средний рейтинг
market.listings.conversion.rate - % проданных
```

---

## Roadmap

**MVP (Phase 1):**
- ✅ Создание объявлений
- ✅ Поиск и фильтры
- ✅ Покупка
- ✅ Отмена объявлений
- ✅ Базовая репутация

**Phase 2:**
- 🔜 Reviews (отзывы)
- 🔜 Favorite sellers
- 🔜 Price history
- 🔜 Watch list

**Phase 3:**
- 🔜 Buy orders (обратные заявки)
- 🔜 Bulk operations
- 🔜 API для сторонних приложений
- 🔜 Mobile app

---

## Интеграция с геймплеем

**Crafting → Market:**
- Crafted items → можно продать
- Rare recipes → эксклюзивные товары

**Quests → Market:**
- Quest rewards → можно продать
- Rare items → высокая цена

**Extract Shooter → Market:**
- Extracted loot → продажа
- High-risk → high-reward items

---

## Продвинутые стратегии

**Для игроков:**
- Фарминг редких предметов → продажа
- Market speculation (buy low, sell high)
- Monopoly на редкие ресурсы
- Crafting economy (создание → продажа)

**Для разработчиков:**
- Dynamic pricing (автоподстройка цен)
- Item sink mechanics (удаление из экономики)
- Seasonal events (ограниченные предметы)
- Tax system (балансировка экономики)

---

## Связанные документы

- `.BRAIN/02-gameplay/economy/player-market/player-market-core.md` - Core (микрофича 1/4)
- `.BRAIN/02-gameplay/economy/player-market/player-market-database.md` - Database (микрофича 2/4)
- `.BRAIN/02-gameplay/economy/player-market/player-market-api.md` - API (микрофича 3/4)
- `.BRAIN/02-gameplay/economy/economy-auction-house.md` - Auction House

---

## История изменений

- **v1.0.0 (2025-11-07 06:10)** - Микрофича 4/4: Player Market Analytics (split from economy-player-market.md)
