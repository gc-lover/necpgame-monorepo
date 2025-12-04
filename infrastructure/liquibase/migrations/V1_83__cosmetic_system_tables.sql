-- Issue: #315
-- Cosmetic System Database Schema
-- Создание таблиц для системы косметики:
-- - cosmetic_items (каталог косметических предметов)
-- - player_cosmetics (владение косметикой игроками)
-- - player_equipped_cosmetics (экипированная косметика игроков)
-- - cosmetic_shop_rotations (ротации магазина косметики)
-- - cosmetic_telemetry (телеметрия косметики)

-- Создание схемы content, если её нет (уже создана в V1_82, но для безопасности)
CREATE SCHEMA IF NOT EXISTS content;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'cosmetic_category') THEN
        CREATE TYPE cosmetic_category AS ENUM ('character_skin', 'weapon_skin', 'emote', 'title', 'name_plate');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'cosmetic_rarity') THEN
        CREATE TYPE cosmetic_rarity AS ENUM ('common', 'rare', 'epic', 'legendary');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'cosmetic_rotation_type') THEN
        CREATE TYPE cosmetic_rotation_type AS ENUM ('daily', 'weekly');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'cosmetic_telemetry_event') THEN
        CREATE TYPE cosmetic_telemetry_event AS ENUM ('acquired', 'equipped', 'unequipped', 'purchased', 'viewed');
    END IF;
END $$;

-- Таблица каталога косметических предметов
CREATE TABLE IF NOT EXISTS content.cosmetic_items (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  description TEXT,
  code VARCHAR(50) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  cost JSONB DEFAULT '{}'::jsonb,
  assets JSONB DEFAULT '{}'::jsonb,
  available_from TIMESTAMP,
  available_until TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  level_requirement INTEGER DEFAULT 0 CHECK (level_requirement >= 0),
  is_exclusive BOOLEAN NOT NULL DEFAULT false,
  is_time_limited BOOLEAN NOT NULL DEFAULT false,
  category cosmetic_category NOT NULL,
  rarity cosmetic_rarity NOT NULL DEFAULT 'common'
);

-- Индексы для cosmetic_items
CREATE INDEX IF NOT EXISTS idx_cosmetic_items_code ON content.cosmetic_items(code);
CREATE INDEX IF NOT EXISTS idx_cosmetic_items_category ON content.cosmetic_items(category, rarity);
CREATE INDEX IF NOT EXISTS idx_cosmetic_items_rarity ON content.cosmetic_items(rarity);
CREATE INDEX IF NOT EXISTS idx_cosmetic_items_is_exclusive ON content.cosmetic_items(is_exclusive) WHERE is_exclusive = true;
CREATE INDEX IF NOT EXISTS idx_cosmetic_items_is_time_limited ON content.cosmetic_items(is_time_limited, available_from, available_until) WHERE is_time_limited = true;
CREATE INDEX IF NOT EXISTS idx_cosmetic_items_level_requirement ON content.cosmetic_items(level_requirement);

-- Таблица владения косметикой игроками
CREATE TABLE IF NOT EXISTS content.player_cosmetics (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  cosmetic_id UUID NOT NULL,
  source VARCHAR(50) NOT NULL,
  acquired_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_used_at TIMESTAMP,
  usage_count INTEGER DEFAULT 0 CHECK (usage_count >= 0),
  CONSTRAINT fk_player_cosmetics_character FOREIGN KEY (character_id) REFERENCES mvp_core.characters(id) ON DELETE CASCADE,
  CONSTRAINT fk_player_cosmetics_cosmetic FOREIGN KEY (cosmetic_id) REFERENCES content.cosmetic_items(id) ON DELETE CASCADE,
  CONSTRAINT uq_player_cosmetics UNIQUE (character_id, cosmetic_id)
);

-- Индексы для player_cosmetics
CREATE INDEX IF NOT EXISTS idx_player_cosmetics_character_id ON content.player_cosmetics(character_id, acquired_at DESC);
CREATE INDEX IF NOT EXISTS idx_player_cosmetics_cosmetic_id ON content.player_cosmetics(cosmetic_id);
CREATE INDEX IF NOT EXISTS idx_player_cosmetics_source ON content.player_cosmetics(source);
CREATE INDEX IF NOT EXISTS idx_player_cosmetics_last_used_at ON content.player_cosmetics(last_used_at DESC) WHERE last_used_at IS NOT NULL;

-- Таблица экипированной косметики игроков
CREATE TABLE IF NOT EXISTS content.player_equipped_cosmetics (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  cosmetic_id UUID,
  slot_name VARCHAR(50) NOT NULL,
  equipped_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  slot_type cosmetic_category NOT NULL,
  CONSTRAINT fk_player_equipped_cosmetics_character FOREIGN KEY (character_id) REFERENCES mvp_core.characters(id) ON DELETE CASCADE,
  CONSTRAINT fk_player_equipped_cosmetics_cosmetic FOREIGN KEY (cosmetic_id) REFERENCES content.cosmetic_items(id) ON DELETE SET NULL,
  CONSTRAINT uq_player_equipped_cosmetics UNIQUE (character_id, slot_type, slot_name)
);

-- Индексы для player_equipped_cosmetics
CREATE INDEX IF NOT EXISTS idx_player_equipped_cosmetics_character_id ON content.player_equipped_cosmetics(character_id, slot_type);
CREATE INDEX IF NOT EXISTS idx_player_equipped_cosmetics_cosmetic_id ON content.player_equipped_cosmetics(cosmetic_id) WHERE cosmetic_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_player_equipped_cosmetics_slot_type ON content.player_equipped_cosmetics(slot_type, slot_name);

-- Таблица ротаций магазина косметики
CREATE TABLE IF NOT EXISTS content.cosmetic_shop_rotations (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  items JSONB DEFAULT '[]'::jsonb,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rotation_type cosmetic_rotation_type NOT NULL,
  CONSTRAINT chk_cosmetic_shop_rotations_dates CHECK (end_date > start_date)
);

-- Индексы для cosmetic_shop_rotations
CREATE INDEX IF NOT EXISTS idx_cosmetic_shop_rotations_type ON content.cosmetic_shop_rotations(rotation_type, start_date DESC);
CREATE INDEX IF NOT EXISTS idx_cosmetic_shop_rotations_dates ON content.cosmetic_shop_rotations(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_cosmetic_shop_rotations_active ON content.cosmetic_shop_rotations(start_date, end_date) WHERE start_date <= CURRENT_TIMESTAMP AND end_date >= CURRENT_TIMESTAMP;

-- Таблица телеметрии косметики
CREATE TABLE IF NOT EXISTS content.cosmetic_telemetry (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  cosmetic_id UUID NOT NULL,
  event_data JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  event_type cosmetic_telemetry_event NOT NULL,
  CONSTRAINT fk_cosmetic_telemetry_character FOREIGN KEY (character_id) REFERENCES mvp_core.characters(id) ON DELETE CASCADE,
  CONSTRAINT fk_cosmetic_telemetry_cosmetic FOREIGN KEY (cosmetic_id) REFERENCES content.cosmetic_items(id) ON DELETE CASCADE
);

-- Индексы для cosmetic_telemetry
CREATE INDEX IF NOT EXISTS idx_cosmetic_telemetry_event_type ON content.cosmetic_telemetry(event_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_cosmetic_telemetry_character_id ON content.cosmetic_telemetry(character_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_cosmetic_telemetry_cosmetic_id ON content.cosmetic_telemetry(cosmetic_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_cosmetic_telemetry_created_at ON content.cosmetic_telemetry(created_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE content.cosmetic_items IS 'Каталог косметических предметов';
COMMENT ON TABLE content.player_cosmetics IS 'Владение косметикой игроками';
COMMENT ON TABLE content.player_equipped_cosmetics IS 'Экипированная косметика игроков';
COMMENT ON TABLE content.cosmetic_shop_rotations IS 'Ротации магазина косметики';
COMMENT ON TABLE content.cosmetic_telemetry IS 'Телеметрия косметики';

-- Комментарии к колонкам
COMMENT ON COLUMN content.cosmetic_items.category IS 'Категория косметики: character_skin, weapon_skin, emote, title, name_plate';
COMMENT ON COLUMN content.cosmetic_items.rarity IS 'Редкость: common, rare, epic, legendary';
COMMENT ON COLUMN content.cosmetic_items.cost IS 'Стоимость (JSONB): валюта, количество';
COMMENT ON COLUMN content.cosmetic_items.assets IS 'Ассеты (JSONB): пути к файлам, ссылки';
COMMENT ON COLUMN content.cosmetic_items.is_exclusive IS 'Эксклюзивная косметика';
COMMENT ON COLUMN content.cosmetic_items.is_time_limited IS 'Ограниченная по времени косметика';
COMMENT ON COLUMN content.player_cosmetics.source IS 'Источник получения: shop, event, achievement, battle_pass и т.д.';
COMMENT ON COLUMN content.player_cosmetics.usage_count IS 'Количество использований';
COMMENT ON COLUMN content.player_equipped_cosmetics.slot_type IS 'Тип слота: character_skin, weapon_skin, emote, title, name_plate';
COMMENT ON COLUMN content.player_equipped_cosmetics.slot_name IS 'Имя слота (например, primary_weapon, secondary_weapon)';
COMMENT ON COLUMN content.cosmetic_shop_rotations.rotation_type IS 'Тип ротации: daily, weekly';
COMMENT ON COLUMN content.cosmetic_shop_rotations.items IS 'Список предметов в ротации (JSONB)';

