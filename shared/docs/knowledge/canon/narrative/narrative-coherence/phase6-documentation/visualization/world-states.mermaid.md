# Диаграмма состояний мира

**Версия:** 1.0.0  
**Дата:** 2025-11-06 23:59

---

## Описание

Визуализация возможных состояний мира и переходов между ними.

---

## Основные состояния мира

```mermaid
stateDiagram-v2
    [*] --> PostWar2023: 2023 Nuclear explosion
    
    PostWar2023 --> CorpControl: Corporations win recovery
    PostWar2023 --> Anarchy: Failed recovery
    
    CorpControl --> CorpDominance: 2030-2045 Stabilization
    Anarchy --> Revolution: People organize
    
    CorpDominance --> AIPeace: 2045-2060 AI integration
    CorpDominance --> AIWar: 2045-2060 AI conflict
    
    AIPeace --> Transcendence2093: AI-Human merge
    AIWar --> CorpVictory2093: Corporations control AI
    
    Revolution --> PeopleVictory2093: People overthrow corps
    Revolution --> Apocalypse2093: Total chaos
    
    CorpVictory2093 --> [*]: ENDING: Corporatocracy
    Transcendence2093 --> [*]: ENDING: Transcendence
    PeopleVictory2093 --> [*]: ENDING: Revolution
    Apocalypse2093 --> [*]: ENDING: Apocalypse
```

---

## Ключевые переходы

**PostWar2023 → CorpControl:**
- Условие: Игроки поддерживают корпорации в восстановлении
- Квесты: MQ-2030-006 (выбор корпораций)

**CorpDominance → AIPeace:**
- Условие: Игроки доверяют ИИ
- Квесты: MQ-2045-005 (доверять ИИ), MQ-2045-006 (поддержать культы)

**AIPeace → Transcendence2093:**
- Условие: Контакт с ИИ за Blackwall
- Квесты: SQ-2078-004 (Blackwall expedition)

---

## Гибридная система влияния

```mermaid
graph TD
    World[World State]
    
    World --> Personal[Personal State]
    World --> Server[Server State]
    World --> Faction[Faction State]
    
    Personal --> P1[Player sees personal quests]
    Server --> S1[All players see server events]
    Faction --> F1[Faction members see faction quests]
    
    P1 --> Combined[Combined World View]
    S1 --> Combined
    F1 --> Combined
    
    Combined --> Priority{Conflict?}
    Priority -->|Server event| UseServer[Server > Faction > Personal]
    Priority -->|Personal quest| UsePersonal[Personal > Server]
    Priority -->|Faction war| UseFaction[Faction > Personal]
    
    style World fill:#4ecdc4
    style Combined fill:#ffe66d
    style Priority fill:#ff6b6b
```

---

## Territory Control States

```mermaid
stateDiagram-v2
    [*] --> Neutral: Territory unclaimed
    
    Neutral --> Faction1: Faction A claims
    Neutral --> Faction2: Faction B claims
    
    Faction1 --> Contested: Faction B attacks
    Faction2 --> Contested: Faction A attacks
    
    Contested --> Faction1: Faction A wins
    Contested --> Faction2: Faction B wins
    Contested --> Neutral: Both retreat
    
    Faction1 --> Fortress: Full control 90%+
    Faction2 --> Fortress: Full control 90%+
    
    Fortress --> Contested: Major attack
```

---

## История изменений

- v1.0.0 (2025-11-06 23:59) - Диаграммы состояний мира

