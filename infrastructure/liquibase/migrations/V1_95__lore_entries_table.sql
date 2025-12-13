-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Lore Entries Table for storing narrative documents
-- Создание таблицы для хранения lore документов (stories, scenarios, events, city lore)

-- Создание схемы narrative, если её нет
CREATE SCHEMA IF NOT EXISTS narrative;

-- Таблица записей lore (narrative документов)
CREATE TABLE IF NOT EXISTS narrative.lore_entries (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  lore_id VARCHAR(255) NOT NULL UNIQUE,
  title VARCHAR(500) NOT NULL,
  document_type VARCHAR(100) NOT NULL, -- 'story', 'scenario', 'event', 'city_lore', etc.
  category VARCHAR(255), -- 'timeline-author', 'narrative', etc.
  content_data JSONB NOT NULL DEFAULT '{}',
  version INTEGER NOT NULL DEFAULT 1,
  is_active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для lore_entries
CREATE INDEX IF NOT EXISTS idx_lore_entries_lore_id ON narrative.lore_entries(lore_id);
CREATE INDEX IF NOT EXISTS idx_lore_entries_document_type ON narrative.lore_entries(document_type);
CREATE INDEX IF NOT EXISTS idx_lore_entries_category ON narrative.lore_entries(category);
CREATE INDEX IF NOT EXISTS idx_lore_entries_active ON narrative.lore_entries(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_lore_entries_version ON narrative.lore_entries(version);

-- Комментарии к таблице
COMMENT ON TABLE narrative.lore_entries IS 'Записи lore из YAML файлов (knowledge/canon/lore/ и knowledge/canon/narrative/)';
COMMENT ON COLUMN narrative.lore_entries.lore_id IS 'Уникальный идентификатор lore записи (из metadata.id)';
COMMENT ON COLUMN narrative.lore_entries.document_type IS 'Тип документа: story, scenario, event, city_lore';
COMMENT ON COLUMN narrative.lore_entries.category IS 'Категория документа: timeline-author, narrative, etc.';
COMMENT ON COLUMN narrative.lore_entries.content_data IS 'Полный YAML контент lore в формате JSONB';