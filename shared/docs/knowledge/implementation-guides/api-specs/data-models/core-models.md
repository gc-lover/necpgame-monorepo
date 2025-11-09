---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:30
**api-readiness-notes:** API Core Data Models. Player, Character, Account models. ~390 строк.
---

# API Core Data Models - Основные модели

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:30  
**Приоритет:** КРИТИЧЕСКИЙ  
**Автор:** AI Brain Manager

**Микрофича:** Core models  
**Размер:** ~390 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Account Model

```typescript
interface Account {
  id: string; // UUID
  email: string;
  username: string;
  passwordHash: string; // Не возвращается в API
  createdAt: Date;
  lastLoginAt?: Date;
  isActive: boolean;
}
```

---

## Player Model

```typescript
interface Player {
  id: string; // UUID
  accountId: string;
  username: string;
  eurodollars: number;
  reputation: Record<string, number>; // {arasaka: 45, militech: -10}
  createdAt: Date;
}
```

---

## Character Model

```typescript
interface Character {
  id: string;
  playerId: string;
  name: string;
  class: "solo" | "netrunner" | "techie" | "nomad" | "corpo";
  level: number;
  experience: number;
  
  attributes: {
    STR: number; // Strength
    DEX: number; // Dexterity
    INT: number; // Intelligence
    TECH: number; // Technical
    COOL: number; // Cool
  };
  
  location: {
    city: string;
    zone: string;
    coords: {x: number, y: number};
  };
  
  hp: {current: number, max: number};
  sp: {current: number, max: number}; // Stamina Points
  
  origin: "street" | "nomad" | "corpo";
  faction: string;
  
  createdAt: Date;
}
```

---

## Связанные документы

- `.BRAIN/05-technical/api-specs/data-models/gameplay-models.md` - Gameplay (микрофича 2/3)
- `.BRAIN/05-technical/api-specs/data-models/social-models.md` - Social (микрофича 3/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:30)** - Микрофича 1/3 (split from api-data-models.md)
