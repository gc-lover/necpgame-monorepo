-- Issue: #1575
-- liquibase formatted sql

-- changeset database-engineer:089-create-weapon-modifiers-tables
-- comment: Создание таблиц для системы модификаторов оружия

CREATE TABLE IF NOT EXISTS weapon_modifiers (
  id VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(20) NOT NULL CHECK (type IN ('attachment', 'chip', 'firmware')),
  category VARCHAR(50) NOT NULL,
  slot_type VARCHAR(50) NOT NULL,
  rarity VARCHAR(20) NOT NULL CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),
  period_available VARCHAR(20),
  effects JSONB NOT NULL DEFAULT '[]',
  compatible_weapons JSONB DEFAULT '[]',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_weapon_modifiers_type ON weapon_modifiers(type);
CREATE INDEX idx_weapon_modifiers_category ON weapon_modifiers(category);
CREATE INDEX idx_weapon_modifiers_slot ON weapon_modifiers(slot_type);

CREATE TABLE IF NOT EXISTS weapon_modifier_slots (
    id SERIAL PRIMARY KEY,
    weapon_id VARCHAR(50) NOT NULL,
    owner_id VARCHAR(50) NOT NULL,
    attachment_slot_1 VARCHAR(50),
    attachment_slot_2 VARCHAR(50),
    attachment_slot_3 VARCHAR(50),
    attachment_slot_4 VARCHAR(50),
    chip_slot_1 VARCHAR(50),
    chip_slot_2 VARCHAR(50),
    chip_slot_3 VARCHAR(50),
    firmware_slot VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(weapon_id)
);

CREATE INDEX idx_weapon_modifier_slots_weapon ON weapon_modifier_slots(weapon_id);
CREATE INDEX idx_weapon_modifier_slots_owner ON weapon_modifier_slots(owner_id);

-- rollback DROP TABLE IF EXISTS weapon_modifier_slots;
-- rollback DROP TABLE IF EXISTS weapon_modifiers;









