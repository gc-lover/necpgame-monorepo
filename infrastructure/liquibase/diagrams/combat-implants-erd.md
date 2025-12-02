<!-- Issue: #58 -->
# Combat Implants System - Database ERD

## Entity Relationship Diagram

```mermaid
erDiagram
    IMPLANTS_CATALOG ||--o{ CHARACTER_IMPLANTS : "has"
    IMPLANTS_CATALOG ||--o{ IMPLANT_ACQUISITIONS : "acquired"
    CHARACTER ||--o{ CHARACTER_IMPLANTS : "owns"
    CHARACTER ||--|| IMPLANT_LIMITS_STATE : "has"
    CHARACTER ||--|| CYBERPSYCHOSIS_STATE : "tracks"
    CHARACTER ||--o{ IMPLANT_SYNERGIES : "activates"
    CHARACTER ||--o{ IMPLANT_ACQUISITIONS : "acquires"

    IMPLANTS_CATALOG {
        uuid id PK
        varchar name UK
        enum type "combat, movement, os, visual"
        varchar category
        enum rarity "common to legendary"
        jsonb effects
        int energy_cost
        int humanity_cost
        varchar slot_type
        jsonb compatibility
        text description
        timestamp created_at
        timestamp updated_at
    }

    CHARACTER_IMPLANTS {
        uuid id PK
        uuid character_id FK
        uuid implant_id FK
        timestamp installed_at
        int upgrade_level "min 1"
        varchar slot
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    IMPLANT_ACQUISITIONS {
        uuid id PK
        uuid character_id FK
        uuid implant_id FK
        enum acquisition_type "purchase, loot, quest, crafting"
        jsonb cost
        timestamp acquired_at
    }

    IMPLANT_LIMITS_STATE {
        uuid id PK
        uuid character_id FK UK
        int total_energy_used
        int max_energy
        int total_humanity_lost
        int max_humanity
        jsonb slots_used
        timestamp last_update
        timestamp created_at
    }

    CYBERPSYCHOSIS_STATE {
        uuid id PK
        uuid character_id FK UK
        int current_level "0-100"
        int threshold_level "0-100"
        jsonb effects_active
        timestamp last_update
        timestamp created_at
    }

    IMPLANT_SYNERGIES {
        uuid id PK
        uuid character_id FK
        uuid synergy_id
        jsonb active_implants
        jsonb bonus_effects
        timestamp activated_at
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }
```

## Schema: `implant`

### Tables Overview

| Table | Description | Key Columns |
|-------|-------------|-------------|
| `implants_catalog` | Каталог имплантов | id, name, type, rarity |
| `character_implants` | Установленные импланты | character_id, implant_id, slot |
| `implant_acquisitions` | История приобретений | character_id, acquisition_type |
| `implant_limits_state` | Лимиты персонажа | character_id, energy, humanity |
| `cyberpsychosis_state` | Киберпсихоз | character_id, current_level |
| `implant_synergies` | Активные синергии | character_id, synergy_id |

## Indexes

### Performance Indexes

- **implants_catalog**: type+category, rarity, slot_type, energy_cost, humanity_cost
- **character_implants**: character_id+is_active, implant_id, slot, upgrade_level
- **implant_acquisitions**: character_id+acquired_at DESC, implant_id, acquisition_type
- **implant_limits_state**: character_id, energy, humanity
- **cyberpsychosis_state**: character_id, current_level+threshold_level, threshold breach
- **implant_synergies**: character_id+is_active, synergy_id, activated_at DESC

## ENUM Types

```sql
CREATE TYPE implant_type AS ENUM ('combat', 'movement', 'os', 'visual');
CREATE TYPE implant_rarity AS ENUM ('common', 'uncommon', 'rare', 'epic', 'legendary');
CREATE TYPE implant_acquisition_type AS ENUM ('purchase', 'loot', 'quest', 'crafting');
```

## Constraints

### Check Constraints

- `energy_cost >= 0`
- `humanity_cost >= 0`
- `upgrade_level >= 1`
- `total_energy_used >= 0`
- `max_energy > 0`
- `total_humanity_lost >= 0`
- `max_humanity > 0`
- `current_level BETWEEN 0 AND 100`
- `threshold_level BETWEEN 0 AND 100`

### Unique Constraints

- `implants_catalog.name` - unique implant names
- `character_implants(character_id, slot)` - one implant per slot
- `implant_limits_state.character_id` - one limits state per character
- `cyberpsychosis_state.character_id` - one cyberpsychosis state per character

## Foreign Keys

- `character_implants.character_id` → `character(id)` ON DELETE CASCADE
- `character_implants.implant_id` → `implants_catalog(id)` ON DELETE CASCADE
- `implant_acquisitions.character_id` → `character(id)` ON DELETE CASCADE
- `implant_acquisitions.implant_id` → `implants_catalog(id)` ON DELETE CASCADE
- `implant_limits_state.character_id` → `character(id)` ON DELETE CASCADE
- `cyberpsychosis_state.character_id` → `character(id)` ON DELETE CASCADE
- `implant_synergies.character_id` → `character(id)` ON DELETE CASCADE

## Migration File

`infrastructure/liquibase/migrations/V1_57__combat_implants_system_tables.sql`

---

**Database Engineer - Combat Implants System**
**Issue: #58**

