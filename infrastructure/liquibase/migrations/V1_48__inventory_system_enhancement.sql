-- Issue: #140887147
-- Inventory System Database Schema Enhancement
-- Дополнение схемы БД системы инвентаря согласно архитектуре:
-- - Добавление таблиц character_equipment, character_storage, storage_items
-- - Добавление недостающих полей в существующие таблицы
-- - Обновление индексов

-- Добавление недостающих полей в character_items
ALTER TABLE mvp_core.character_items
ADD COLUMN IF NOT EXISTS item_template_id UUID,
ADD COLUMN IF NOT EXISTS durability INTEGER,
ADD COLUMN IF NOT EXISTS bind_status VARCHAR(20) CHECK (bind_status IN ('unbound', 'bound', 'account_bound')),
ADD COLUMN IF NOT EXISTS modifiers JSONB;

-- Добавление недостающих полей в item_templates
ALTER TABLE mvp_core.item_templates
ADD COLUMN IF NOT EXISTS bind_on_pickup BOOLEAN NOT NULL DEFAULT false;

-- Обновление character_inventory: добавление max_slots (если capacity используется как max_slots, оставляем как есть)
-- Добавляем алиас current_weight для weight (weight уже существует, используем его как current_weight)

-- Таблица экипировки персонажа
CREATE TABLE IF NOT EXISTS mvp_core.character_equipment (
    character_id UUID NOT NULL,
    slot_type VARCHAR(50) NOT NULL CHECK (slot_type IN (
        'weapon_primary', 'weapon_secondary', 'weapon_melee',
        'armor_head', 'armor_body', 'armor_legs', 'armor_feet', 'armor_hands',
        'implant_1', 'implant_2', 'implant_3', 'implant_4', 'implant_5',
        'cyberdeck', 'operating_system', 'nervous_system'
    )),
    item_id UUID NOT NULL REFERENCES mvp_core.character_items(id) ON DELETE CASCADE,
    equipped_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (character_id, slot_type),
    FOREIGN KEY (character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE
);

-- Индексы для character_equipment
CREATE INDEX IF NOT EXISTS idx_character_equipment_character_id ON mvp_core.character_equipment(character_id);
CREATE INDEX IF NOT EXISTS idx_character_equipment_item_id ON mvp_core.character_equipment(item_id);
CREATE INDEX IF NOT EXISTS idx_character_equipment_slot_type ON mvp_core.character_equipment(slot_type);

-- Таблица хранилища персонажа (банк/стэш)
CREATE TABLE IF NOT EXISTS mvp_core.character_storage (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    storage_type VARCHAR(30) NOT NULL CHECK (storage_type IN ('personal_bank', 'guild_bank', 'stash')),
    max_slots INTEGER NOT NULL DEFAULT 50,
    current_weight DECIMAL(10, 2) NOT NULL DEFAULT 0,
    max_weight DECIMAL(10, 2) NOT NULL DEFAULT 500.0,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    UNIQUE(character_id, storage_type) WHERE deleted_at IS NULL
);

-- Индексы для character_storage
CREATE INDEX IF NOT EXISTS idx_character_storage_character_id ON mvp_core.character_storage(character_id) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_character_storage_storage_type ON mvp_core.character_storage(storage_type) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_character_storage_character_type ON mvp_core.character_storage(character_id, storage_type) WHERE deleted_at IS NULL;

-- Таблица предметов в хранилище
CREATE TABLE IF NOT EXISTS mvp_core.storage_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    storage_id UUID NOT NULL REFERENCES mvp_core.character_storage(id) ON DELETE CASCADE,
    item_template_id UUID,
    slot_index INTEGER NOT NULL,
    stack_size INTEGER NOT NULL DEFAULT 1,
    durability INTEGER,
    bind_status VARCHAR(20) CHECK (bind_status IN ('unbound', 'bound', 'account_bound')),
    modifiers JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    UNIQUE(storage_id, slot_index) WHERE deleted_at IS NULL
);

-- Индексы для storage_items
CREATE INDEX IF NOT EXISTS idx_storage_items_storage_id ON mvp_core.storage_items(storage_id) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_storage_items_slot_index ON mvp_core.storage_items(slot_index) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_storage_items_storage_slot ON mvp_core.storage_items(storage_id, slot_index) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_storage_items_item_template_id ON mvp_core.storage_items(item_template_id) WHERE deleted_at IS NULL AND item_template_id IS NOT NULL;

-- Обновление индексов для character_items
CREATE INDEX IF NOT EXISTS idx_character_items_character_id ON mvp_core.character_items(character_id) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_character_items_item_template_id ON mvp_core.character_items(item_template_id) WHERE deleted_at IS NULL AND item_template_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_character_items_character_slot ON mvp_core.character_items(character_id, slot_index) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_character_items_character_template ON mvp_core.character_items(character_id, item_template_id) WHERE deleted_at IS NULL AND item_template_id IS NOT NULL;

-- Обновление индексов для item_templates
CREATE INDEX IF NOT EXISTS idx_item_templates_type_rarity ON mvp_core.item_templates(type, rarity);

-- Комментарии к таблицам
COMMENT ON TABLE mvp_core.character_equipment IS 'Экипировка персонажа (оружие, броня, импланты)';
COMMENT ON TABLE mvp_core.character_storage IS 'Хранилище персонажа (банк/стэш)';
COMMENT ON TABLE mvp_core.storage_items IS 'Предметы в хранилище персонажа';

-- Комментарии к колонкам
COMMENT ON COLUMN mvp_core.character_items.item_template_id IS 'ID шаблона предмета из item_templates';
COMMENT ON COLUMN mvp_core.character_items.durability IS 'Прочность предмета (nullable для неразрушаемых предметов)';
COMMENT ON COLUMN mvp_core.character_items.bind_status IS 'Статус привязки: unbound, bound, account_bound';
COMMENT ON COLUMN mvp_core.character_items.modifiers IS 'JSONB модификаторы предмета (статы, аффиксы, улучшения)';
COMMENT ON COLUMN mvp_core.item_templates.bind_on_pickup IS 'Привязка предмета при подборе';
COMMENT ON COLUMN mvp_core.character_equipment.slot_type IS 'Тип слота экипировки: weapon_primary, armor_head, implant_1, etc.';
COMMENT ON COLUMN mvp_core.character_storage.storage_type IS 'Тип хранилища: personal_bank, guild_bank, stash';
COMMENT ON COLUMN mvp_core.storage_items.item_template_id IS 'ID шаблона предмета из item_templates';


