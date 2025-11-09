---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:35
**api-readiness-notes:** Romance Dialogue Flow. Диалоговые деревья романтики, выборы. ~180 строк.
---

# Romance Dialogue - Диалоговая система

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:35  
**Приоритет:** средний  
**Автор:** AI Brain Manager

**Микрофича:** Dialogue flow  
**Размер:** ~180 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Dialogue Node

```typescript
interface RomanceDialogueNode {
  id: number;
  npcId: string;
  text: string;
  
  choices: Array<{
    id: string;
    text: string;
    relationshipChange: number;
    nextNode: number;
    flags?: string[];
  }>;
}
```

---

## API Endpoints

**GET /api/v1/romance/{npcId}/events** - available events  
**POST /api/v1/romance/{npcId}/start** - start event  
**POST /api/v1/romance/{npcId}/choice** - make choice

---

## Связанные документы

- `.BRAIN/05-technical/algorithms/romance/romance-triggers.md` - Triggers (микрофича 1/3)
- `.BRAIN/05-technical/algorithms/romance/romance-relationship.md` - Relationship (микрофича 2/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:35)** - Микрофича 3/3 (split from romance-event-engine.md)
