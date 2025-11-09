---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:20
**api-readiness-notes:** UI Main Game Features. Навигация, карта, квесты, бой, социальные. ~300 строк.
---

# UI Main Game Features - Игровые функции

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:20  
**Приоритет:** КРИТИЧЕСКИЙ (MVP)  
**Автор:** AI Brain Manager

**Микрофича:** Navigation, Quests, Combat, Social  
**Размер:** ~300 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Навигация и карта

```
┌──────────────────────────────────────────────────────┐
│ MAP: NIGHT CITY                         [🔍 Zoom]    │
├──────────────────────────────────────────────────────┤
│                                                       │
│    [Watson]     [Westbrook]    [City Center]         │
│                                                       │
│    [Pacifica]   [Heywood]      [Santo Domingo]       │
│                                                       │
│ YOUR LOCATION: Watson - Kabuki Market 📍             │
│                                                       │
│ AVAILABLE LOCATIONS:                                 │
│ ┌────────────────────────────────────────────────┐  │
│ │ 🏪 Kabuki Market (Current)                     │  │
│ │ 🏥 Trauma Team Clinic         [Travel 500 ed]  │  │
│ │ 🎯 NCPD HQ (Quest available)  [Travel Free]    │  │
│ └────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘
```

---

## Квесты

```
┌──────────────────────────────────────────────────────┐
│ ACTIVE QUESTS                        [Available: 12] │
├──────────────────────────────────────────────────────┤
│ ⭐ MAIN: The Blackwall Breach                        │
│    Objective: Investigate NCPD HQ                    │
│    Location: Watson - NCPD HQ                        │
│    [Continue] [Track]                                │
│                                                       │
│ 📋 SIDE: Missing Cop Morgana                         │
│    Objective: Talk to Officer Chen                   │
│    Location: Watson - Kabuki Market                  │
│    [Continue]                                        │
│                                                       │
│ 💀 EXTRACT: Tech Vault Raid                          │
│    Danger: HIGH | Reward: 5,000 ed                   │
│    Location: Westbrook - Corp Plaza                  │
│    [Start Mission]                                   │
└──────────────────────────────────────────────────────┘
```

---

## Бой (Text-based)

```
┌──────────────────────────────────────────────────────┐
│ COMBAT: Gang Member                                  │
├──────────────────────────────────────────────────────┤
│ YOU:                   ENEMY:                        │
│ ❤️  92/100 HP           ❤️  35/50 HP                 │
│ ⚡ 25/30 SP            ⚡ 10/15 SP                   │
│                                                       │
│ TURN 5                                               │
│ > You dealt 15 damage! (Mantis Blades)              │
│ > Enemy attacked! You took 8 damage.                │
│                                                       │
│ YOUR ACTIONS:                                        │
│ [⚔️ Attack] [🛡️ Defend] [⚡ Special] [🏃 Flee]       │
│                                                       │
│ ABILITIES:                                           │
│ [🔪 Slash] [💥 Power Attack] [🌀 Spin Attack]       │
└──────────────────────────────────────────────────────┘
```

---

## Социальные функции

```
┌──────────────────────────────────────────────────────┐
│ SOCIAL                                               │
├──────────────────────────────────────────────────────┤
│ [Friends] [Guild] [Party] [Chat]                    │
│                                                       │
│ ONLINE FRIENDS (3/15):                               │
│ 🟢 CyberSamurai - Level 18 - Watson                 │
│ 🟢 NetRunner99 - Level 22 - Tokyo                   │
│ 🟢 StreetKid - Level 14 - Night City                │
│                                                       │
│ GUILD: Night Runners (45 members)                    │
│ 💬 Guild Chat: "Raid tonight at 20:00!"             │
│                                                       │
│ [Invite to Party] [Send Message] [Guild Panel]      │
└──────────────────────────────────────────────────────┘
```

---

## API Calls

**GET /api/v1/locations/{city}** - карта  
**GET /api/v1/quests/available** - квесты  
**POST /api/v1/combat/start** - начать бой  
**GET /api/v1/social/friends** - друзья  
**GET /api/v1/guilds/my** - моя гильдия

---

## Связанные документы

- `.BRAIN/05-technical/ui/main-game/ui-hud-core.md` - HUD (микрофича 1/3)
- `.BRAIN/05-technical/ui/main-game/ui-system.md` - System (микрофича 3/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:20)** - Микрофича 2/3 (split from ui-main-game.md)
