package models

import (
	"time"

	"github.com/google/uuid"
)

type CharacterProgression struct {
	CharacterID      uuid.UUID              `json:"character_id" db:"character_id"`
	Level            int                    `json:"level" db:"level"`
	Experience       int64                  `json:"experience" db:"experience"`
	ExperienceToNext int64                  `json:"experience_to_next" db:"experience_to_next"`
	AttributePoints  int                    `json:"attribute_points" db:"attribute_points"`
	SkillPoints      int                    `json:"skill_points" db:"skill_points"`
	Attributes       map[string]int        `json:"attributes" db:"attributes"`
	UpdatedAt        time.Time              `json:"updated_at" db:"updated_at"`
}

type SkillExperience struct {
	ID          uuid.UUID `json:"id" db:"id"`
	CharacterID uuid.UUID `json:"character_id" db:"character_id"`
	SkillID     string    `json:"skill_id" db:"skill_id"`
	Level       int       `json:"level" db:"level"`
	Experience  int64     `json:"experience" db:"experience"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

type AddExperienceRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	Amount      int64     `json:"amount"`
	Source      string    `json:"source"`
}

type AddSkillExperienceRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	SkillID     string    `json:"skill_id"`
	Amount      int64     `json:"amount"`
}

type AllocateAttributePointRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	Attribute   string    `json:"attribute"`
}

type AllocateSkillPointRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	SkillID     string    `json:"skill_id"`
}

type ProgressionResponse struct {
	Progression *CharacterProgression `json:"progression"`
}

type SkillProgressionResponse struct {
	Skills []SkillExperience `json:"skills"`
	Total  int              `json:"total"`
}

