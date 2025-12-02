// Issue: #1560

package server

import (
	"context"
	"database/sql"
	"encoding/json"

	"github.com/gc-lover/necpgame-monorepo/services/projectile-core-service-go/pkg/api"
)

// ProjectileRepository handles database operations
type ProjectileRepository struct {
	db *sql.DB
}

// NewProjectileRepository creates a new repository
func NewProjectileRepository(db *sql.DB) *ProjectileRepository {
	return &ProjectileRepository{
		db: db,
	}
}

// GetForms returns list of projectile forms
func (r *ProjectileRepository) GetForms(ctx context.Context, params api.GetProjectileFormsParams) ([]*api.ProjectileForm, error) {
	query := `SELECT id, name, type, description, parameters, visual_effect FROM projectile_forms WHERE 1=1`
	args := []interface{}{}

	// Add filters
	if params.Type != nil {
		query += " AND type = $" + string(rune(len(args)+1))
		args = append(args, string(*params.Type))
	}

	// Add pagination
	if params.Limit != nil {
		query += " LIMIT $" + string(rune(len(args)+1))
		args = append(args, *params.Limit)
	}
	if params.Offset != nil {
		query += " OFFSET $" + string(rune(len(args)+1))
		args = append(args, *params.Offset)
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var forms []*api.ProjectileForm
	for rows.Next() {
		var form api.ProjectileForm
		var paramsJSON []byte
		var visualEffect *string

		err := rows.Scan(&form.Id, &form.Name, &form.Type, &form.Description, &paramsJSON, &visualEffect)
		if err != nil {
			return nil, err
		}

		// Parse parameters JSON
		if err := json.Unmarshal(paramsJSON, &form.Parameters); err != nil {
			return nil, err
		}

		if visualEffect != nil {
			form.VisualEffect = visualEffect
		}

		forms = append(forms, &form)
	}

	return forms, nil
}

// GetFormByID returns a single form by ID
func (r *ProjectileRepository) GetFormByID(ctx context.Context, formID string) (*api.ProjectileForm, error) {
	query := `SELECT id, name, type, description, parameters, visual_effect FROM projectile_forms WHERE id = $1`

	var form api.ProjectileForm
	var paramsJSON []byte
	var visualEffect *string

	err := r.db.QueryRowContext(ctx, query, formID).Scan(
		&form.Id, &form.Name, &form.Type, &form.Description, &paramsJSON, &visualEffect,
	)
	if err != nil {
		return nil, err
	}

	// Parse parameters JSON
	if err := json.Unmarshal(paramsJSON, &form.Parameters); err != nil {
		return nil, err
	}

	if visualEffect != nil {
		form.VisualEffect = visualEffect
	}

	return &form, nil
}

// CheckCompatibility checks if form is compatible with weapon
func (r *ProjectileRepository) CheckCompatibility(ctx context.Context, weaponID, formID string) (bool, error) {
	// TODO: Get weapon type from weapon_id
	weaponType := "pistols" // Placeholder

	return r.CheckCompatibilityByType(ctx, weaponType, formID)
}

// CheckCompatibilityByType checks if form is compatible with weapon type
func (r *ProjectileRepository) CheckCompatibilityByType(ctx context.Context, weaponType, formID string) (bool, error) {
	query := `
		SELECT is_compatible 
		FROM projectile_compatibility 
		WHERE weapon_type = $1 AND projectile_form_id = $2
	`

	var compatible bool
	err := r.db.QueryRowContext(ctx, query, weaponType, formID).Scan(&compatible)
	if err == sql.ErrNoRows {
		return false, nil
	}
	if err != nil {
		return false, err
	}

	return compatible, nil
}

// GetCompatibilityMatrix returns full compatibility matrix
func (r *ProjectileRepository) GetCompatibilityMatrix(ctx context.Context) (map[string]interface{}, error) {
	query := `
		SELECT weapon_type, projectile_form_id 
		FROM projectile_compatibility 
		WHERE is_compatible = true
		ORDER BY weapon_type, projectile_form_id
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	matrix := make(map[string][]string)

	for rows.Next() {
		var weaponType, formID string
		if err := rows.Scan(&weaponType, &formID); err != nil {
			return nil, err
		}

		if _, exists := matrix[weaponType]; !exists {
			matrix[weaponType] = []string{}
		}
		matrix[weaponType] = append(matrix[weaponType], formID)
	}

	result := make(map[string]interface{})
	for k, v := range matrix {
		result[k] = v
	}

	return result, nil
}

