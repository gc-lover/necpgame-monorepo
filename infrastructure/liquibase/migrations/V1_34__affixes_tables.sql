CREATE TABLE IF NOT EXISTS gameplay.affixes (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  category VARCHAR(50) NOT NULL,
  description TEXT NOT NULL,
  mechanics JSONB,
  visual_effects JSONB,
  reward_modifier DECIMAL(5,2) NOT NULL DEFAULT 1.0 CHECK (reward_modifier >= 1.0),
  difficulty_modifier DECIMAL(5,2) NOT NULL DEFAULT 1.0 CHECK (difficulty_modifier >= 1.0),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CHECK (category IN ('combat', 'environmental', 'debuff', 'defensive'))
);

CREATE TABLE IF NOT EXISTS gameplay.affix_rotations (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  week_start TIMESTAMP NOT NULL,
  week_end TIMESTAMP NOT NULL,
  seasonal_affix_id UUID,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(week_start, week_end),
  FOREIGN KEY (seasonal_affix_id) REFERENCES gameplay.affixes(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS gameplay.affix_rotation_affixes (
  rotation_id UUID NOT NULL,
  affix_id UUID NOT NULL,
  PRIMARY KEY (rotation_id, affix_id),
  FOREIGN KEY (rotation_id) REFERENCES gameplay.affix_rotations(id) ON DELETE CASCADE,
  FOREIGN KEY (affix_id) REFERENCES gameplay.affixes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS gameplay.instance_affixes (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  instance_id UUID NOT NULL,
  affix_id UUID NOT NULL,
  applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(instance_id, affix_id),
  FOREIGN KEY (affix_id) REFERENCES gameplay.affixes(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_affixes_category ON gameplay.affixes(category);
CREATE INDEX IF NOT EXISTS idx_affix_rotations_week_start ON gameplay.affix_rotations(week_start);
CREATE INDEX IF NOT EXISTS idx_affix_rotations_week_end ON gameplay.affix_rotations(week_end);
CREATE INDEX IF NOT EXISTS idx_affix_rotation_affixes_rotation ON gameplay.affix_rotation_affixes(rotation_id);
CREATE INDEX IF NOT EXISTS idx_affix_rotation_affixes_affix ON gameplay.affix_rotation_affixes(affix_id);
CREATE INDEX IF NOT EXISTS idx_instance_affixes_instance ON gameplay.instance_affixes(instance_id);
CREATE INDEX IF NOT EXISTS idx_instance_affixes_affix ON gameplay.instance_affixes(affix_id);

