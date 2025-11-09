# Freerun (Parkour) Feature
Система паркура - мобильность и свобода перемещения (Assassin's Creed / Mirror's Edge / Dying Light).

**OpenAPI:** freerun.yaml | **Роут:** /game/freerun

## Функционал
- **Прыжки:** normal, double, roof-to-roof, ledge grab
- **Лазание:** wall, ledge, pipe, ladder
- **Скольжение:** уклонение в бою, бонус к evasion
- **Zцепление:** hook, gravibot, mantis blades
- **Aerial Attacks:** dive attack, aerial shoot, mantis strike (интеграция с боем)
- **Stamina:** выносливость (STA), регенерация, доступность действий

## Компоненты
- **StaminaMeter** - индикатор выносливости с доступными действиями
- **FreerunPage** - управление паркуром с кнопками действий

## Механики
- Тип: смешанный (автоматический + ручной контроль)
- Ограничения: выносливость (STA)
- Интеграция с боем: атаки с воздуха, стрельба в движении, комбо
- Импланты: ускорители ног, прыжковые усилители, крюки, gravibot

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI компоненты.

