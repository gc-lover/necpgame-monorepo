-- Issue: #50, #107
-- Narrative System - NPC Definitions and Dialogue Nodes Tables
-- Создание таблиц для системы нарратива:
-- - narrative.npc_definitions (NPC определения из YAML)
-- - narrative.dialogue_nodes (узлы диалогов из YAML)

-- Создание схемы narrative, если её нет
CREATE SCHEMA IF NOT EXISTS narrative;

-- Таблица определений NPC
CREATE TABLE IF NOT EXISTS narrative.npc_definitions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  npc_id VARCHAR(255) NOT NULL UNIQUE,
  title VARCHAR(500) NOT NULL,
  content_data JSONB NOT NULL DEFAULT '{}',
  version INTEGER NOT NULL DEFAULT 1,
  is_active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для npc_definitions
CREATE INDEX IF NOT EXISTS idx_npc_definitions_npc_id ON narrative.npc_definitions(npc_id);
CREATE INDEX IF NOT EXISTS idx_npc_definitions_active ON narrative.npc_definitions(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_npc_definitions_version ON narrative.npc_definitions(version);

-- Таблица узлов диалогов
CREATE TABLE IF NOT EXISTS narrative.dialogue_nodes (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  dialogue_id VARCHAR(255) NOT NULL UNIQUE,
  title VARCHAR(500) NOT NULL,
  content_data JSONB NOT NULL DEFAULT '{}',
  version INTEGER NOT NULL DEFAULT 1,
  is_active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для dialogue_nodes
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_dialogue_id ON narrative.dialogue_nodes(dialogue_id);
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_active ON narrative.dialogue_nodes(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_version ON narrative.dialogue_nodes(version);

-- Комментарии к таблицам
COMMENT ON TABLE narrative.npc_definitions IS 'Определения NPC из YAML файлов (knowledge/canon/narrative/npc-lore/)';
COMMENT ON TABLE narrative.dialogue_nodes IS 'Узлы диалогов из YAML файлов (knowledge/canon/narrative/dialogues/)';
COMMENT ON COLUMN narrative.npc_definitions.content_data IS 'Полный YAML контент NPC в формате JSONB';
COMMENT ON COLUMN narrative.dialogue_nodes.content_data IS 'Полный YAML контент диалога в формате JSONB';

