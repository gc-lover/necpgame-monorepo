package server

import (
	"context"
	"encoding/json"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/sirupsen/logrus"
)

type ProgressionRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewProgressionRepository(db *pgxpool.Pool) *ProgressionRepository {
	return &ProgressionRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *ProgressionRepository) GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error) {
	var progression models.CharacterProgression
	var attributesJSON []byte

	query := `
		SELECT character_id, level, experience, experience_to_next,
		       attribute_points, skill_points, attributes, updated_at
		FROM progression.character_progression
		WHERE character_id = $1`

	err := r.db.QueryRow(ctx, query, characterID).Scan(
		&progression.CharacterID, &progression.Level, &progression.Experience,
		&progression.ExperienceToNext, &progression.AttributePoints,
		&progression.SkillPoints, &attributesJSON, &progression.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(attributesJSON) > 0 {
		json.Unmarshal(attributesJSON, &progression.Attributes)
	} else {
		progression.Attributes = make(map[string]int)
	}

	return &progression, nil
}

func (r *ProgressionRepository) CreateProgression(ctx context.Context, progression *models.CharacterProgression) error {
	attributesJSON, _ := json.Marshal(progression.Attributes)

	query := `
		INSERT INTO progression.character_progression (
			character_id, level, experience, experience_to_next,
			attribute_points, skill_points, attributes, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, NOW()
		)`

	_, err := r.db.Exec(ctx, query,
		progression.CharacterID, progression.Level, progression.Experience,
		progression.ExperienceToNext, progression.AttributePoints,
		progression.SkillPoints, attributesJSON,
	)

	return err
}

func (r *ProgressionRepository) UpdateProgression(ctx context.Context, progression *models.CharacterProgression) error {
	attributesJSON, _ := json.Marshal(progression.Attributes)

	query := `
		UPDATE progression.character_progression
		SET level = $1, experience = $2, experience_to_next = $3,
		    attribute_points = $4, skill_points = $5, attributes = $6, updated_at = NOW()
		WHERE character_id = $7`

	_, err := r.db.Exec(ctx, query,
		progression.Level, progression.Experience, progression.ExperienceToNext,
		progression.AttributePoints, progression.SkillPoints, attributesJSON,
		progression.CharacterID,
	)

	return err
}

func (r *ProgressionRepository) GetSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string) (*models.SkillExperience, error) {
	var skillExp models.SkillExperience

	query := `
		SELECT id, character_id, skill_id, level, experience, updated_at
		FROM progression.skill_experience
		WHERE character_id = $1 AND skill_id = $2`

	err := r.db.QueryRow(ctx, query, characterID, skillID).Scan(
		&skillExp.ID, &skillExp.CharacterID, &skillExp.SkillID,
		&skillExp.Level, &skillExp.Experience, &skillExp.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &skillExp, nil
}

func (r *ProgressionRepository) CreateSkillExperience(ctx context.Context, skillExp *models.SkillExperience) error {
	query := `
		INSERT INTO progression.skill_experience (
			id, character_id, skill_id, level, experience, updated_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, NOW()
		) RETURNING id`

	return r.db.QueryRow(ctx, query,
		skillExp.CharacterID, skillExp.SkillID, skillExp.Level, skillExp.Experience,
	).Scan(&skillExp.ID)
}

func (r *ProgressionRepository) UpdateSkillExperience(ctx context.Context, skillExp *models.SkillExperience) error {
	query := `
		UPDATE progression.skill_experience
		SET level = $1, experience = $2, updated_at = NOW()
		WHERE character_id = $3 AND skill_id = $4`

	_, err := r.db.Exec(ctx, query,
		skillExp.Level, skillExp.Experience, skillExp.CharacterID, skillExp.SkillID,
	)

	return err
}

func (r *ProgressionRepository) ListSkillExperience(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.SkillExperience, error) {
	query := `
		SELECT id, character_id, skill_id, level, experience, updated_at
		FROM progression.skill_experience
		WHERE character_id = $1
		ORDER BY updated_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, characterID, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var skills []models.SkillExperience
	for rows.Next() {
		var skillExp models.SkillExperience
		err := rows.Scan(
			&skillExp.ID, &skillExp.CharacterID, &skillExp.SkillID,
			&skillExp.Level, &skillExp.Experience, &skillExp.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		skills = append(skills, skillExp)
	}

	return skills, nil
}

func (r *ProgressionRepository) CountSkillExperience(ctx context.Context, characterID uuid.UUID) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM progression.skill_experience WHERE character_id = $1`
	err := r.db.QueryRow(ctx, query, characterID).Scan(&count)
	return count, err
}

