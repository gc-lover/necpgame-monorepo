# Visual & Simulation Guides — Readiness Roadmap

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** Art & Simulation Guild  
**Связанные документы:** `visual-style-locations-детально.md`, `visual-style-assets-детально.md`, `../content-generation/city-life-simulation-plan.md`, `../content-generation/city-life-population-algorithm.md`

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-08
**api-readiness-notes:** План по закрытию меток `needs-work`.

---

## 1. Контекст

- Визуальные гайды и симуляционные документы помечены `needs-work`, блокируют UI прототипы и контентные пайплайны.
- Требуется зафиксировать дедлайны, владельцев и зависимые артефакты.

## 2. Матрица документов

| Документ | Статус | Основные GAP | Ответственный | Дедлайн | Блокирует |
|----------|--------|--------------|---------------|---------|-----------|
| `visual-style-locations-детально.md` | needs-work | Нет финальных moodboard ссылок, не описано освещение по районам | Art Director | 2025-11-12 | UI локаций, world-building |
| `visual-style-assets-детально.md` | needs-work | Нет набора props для улиц/корп. зон, отсутствует palette для UI | Props Lead | 2025-11-13 | HUD, контент пайплайн |
| `content-generation/city-life-simulation-plan.md` | needs-work | Нет метрик density, не описаны события NPC | Simulation Lead | 2025-11-14 | Orders Board, world-service |
| `content-generation/city-life-population-algorithm.md` | needs-work | Нет Kafka выходов, отсутствует псевдокод алгоритма | Simulation Lead | 2025-11-15 | Gameplay-service telemetry |
| `visual-style-assets.md` | draft | Нет финальной палитры для фракций | Art Director | 2025-11-11 | Маркетинговые материалы |

## 3. План действий

1. Провести воркшоп `Art & Sim Alignment` 2025-11-09 (Art Director, Simulation Lead, UI Lead).  
2. Обновить moodboard ссылки в Figma и вложить их в документы.  
3. Прописать псевдокод алгоритма заселения и Kafka события.  
4. Провести ревью с Frontend/Content 2025-11-15.

## 4. Чек-лист готовности

- [x] Определены ответственные и дедлайны.
- [x] Зафиксированы блокеры и зависимости.
- [ ] Обновить документы и снять метки `needs-work`.

---

**Следующее действие:** 2025-11-09 провести синхронизацию и зафиксировать обновления в Figma/Notion, затем обновить документы до статуса `ready`.