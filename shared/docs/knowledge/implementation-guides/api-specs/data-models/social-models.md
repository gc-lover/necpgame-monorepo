---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:30
**api-readiness-notes:** API Social Models. Party, Guild, Friends, NPC models. ~200 строк.
---

# API Social Models - Социальные модели

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:30  
**Приоритет:** КРИТИЧЕСКИЙ  
**Автор:** AI Brain Manager

**Микрофича:** Social models  
**Размер:** ~200 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Party Model

```typescript
interface Party {
  id: string;
  leaderId: string;
  members: string[]; // Player IDs
  maxMembers: number; // 5
  lootMode: "freeForAll" | "roundRobin" | "needGreed";
  createdAt: Date;
}
```

---

## Guild Model

```typescript
interface Guild {
  id: string;
  name: string;
  tag: string; // [TAG]
  leaderId: string;
  memberCount: number;
  maxMembers: number;
  level: number;
  createdAt: Date;
}

interface GuildMember {
  guildId: string;
  playerId: string;
  rank: "leader" | "officer" | "veteran" | "member" | "recruit";
  joinedAt: Date;
}
```

---

## Friend Model

```typescript
interface Friend {
  id: string;
  playerId: string;
  friendId: string;
  status: "pending" | "accepted" | "blocked";
  createdAt: Date;
}
```

---

## NPC Model

```typescript
interface NPC {
  id: string;
  name: string;
  type: "vendor" | "quest_giver" | "hireable" | "romance";
  location: {city: string, zone: string};
  relationship: number; // -100 to 100
  fate?: "alive" | "dead" | "missing" | "hero" | "villain";
}
```

---

## Связанные документы

- `.BRAIN/05-technical/api-specs/data-models/core-models.md` - Core (микрофича 1/3)
- `.BRAIN/05-technical/api-specs/data-models/gameplay-models.md` - Gameplay (микрофича 2/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:30)** - Микрофича 3/3 (split from api-data-models.md)
