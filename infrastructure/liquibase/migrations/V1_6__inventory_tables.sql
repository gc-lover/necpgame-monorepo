CREATE TABLE IF NOT EXISTS mvp_core.character_inventory (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  capacity INT NOT NULL DEFAULT 50,
  used_slots INT NOT NULL DEFAULT 0,
  weight DECIMAL(10, 2) NOT NULL DEFAULT 0,
  max_weight DECIMAL(10, 2) NOT NULL DEFAULT 100.0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_character_inventory_character 
  ON mvp_core.character_inventory(character_id) WHERE deleted_at IS NULL;

CREATE TABLE IF NOT EXISTS mvp_core.character_items (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  inventory_id UUID NOT NULL REFERENCES mvp_core.character_inventory(id) ON DELETE CASCADE,
  item_id VARCHAR(100) NOT NULL,
  slot_index INT NOT NULL,
  stack_count INT NOT NULL DEFAULT 1,
  max_stack_size INT NOT NULL DEFAULT 1,
  is_equipped BOOLEAN NOT NULL DEFAULT false,
  equip_slot VARCHAR(50),
  metadata JSONB,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_character_items_inventory_slot 
  ON mvp_core.character_items(inventory_id, slot_index) WHERE deleted_at IS NULL AND is_equipped = false;

CREATE TABLE IF NOT EXISTS mvp_core.item_templates (
  id VARCHAR(100) PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  type VARCHAR(50) NOT NULL,
  rarity VARCHAR(50) NOT NULL DEFAULT 'common',
  max_stack_size INT NOT NULL DEFAULT 1,
  weight DECIMAL(10, 2) NOT NULL DEFAULT 0,
  can_equip BOOLEAN NOT NULL DEFAULT false,
  equip_slot VARCHAR(50),
  requirements JSONB,
  stats JSONB,
  metadata JSONB,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_character_inventory_character_id ON mvp_core.character_inventory(character_id) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_character_items_inventory_id ON mvp_core.character_items(inventory_id) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_character_items_item_id ON mvp_core.character_items(item_id) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_character_items_is_equipped ON mvp_core.character_items(is_equipped) WHERE deleted_at IS NULL AND is_equipped = true;
