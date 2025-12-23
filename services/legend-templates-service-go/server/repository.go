// Issue: #2241
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
)

// LegendRepository handles database operations for legend templates
type LegendRepository struct {
	db    *sql.DB
	redis *RedisClient
}

// NewLegendRepository creates a new legend repository
func NewLegendRepository(db *sql.DB, redis *RedisClient) *LegendRepository {
	return &LegendRepository{
		db:    db,
		redis: redis,
	}
}

// GetTemplates retrieves story templates with filtering and pagination
func (r *LegendRepository) GetTemplates(ctx context.Context, params api.GetTemplatesParams) (*api.TemplatesListResponse, error) {
	// BACKEND NOTE: Optimized query with proper indexing for admin operations
	baseQuery := `
		SELECT id, type, category, base_template, variables, conditions,
			   variants, active, created_at, updated_at
		FROM legend_templates
		WHERE 1=1`

	args := []interface{}{}
	argCount := 0

	// Add filters
	if params.Type != nil {
		argCount++
		baseQuery += fmt.Sprintf(" AND type = $%d", argCount)
		args = append(args, *params.Type)
	}

	if params.Category != nil {
		argCount++
		baseQuery += fmt.Sprintf(" AND category = $%d", argCount)
		args = append(args, *params.Category)
	}

	// Add ordering and pagination
	baseQuery += " ORDER BY created_at DESC"

	limit := int32(50) // default
	if params.Limit != nil && *params.Limit <= 100 {
		limit = *params.Limit
	}
	argCount++
	baseQuery += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, limit)

	offset := int32(0) // default
	if params.Offset != nil {
		offset = *params.Offset
	}
	argCount++
	baseQuery += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, offset)

	// Execute query
	rows, err := r.db.QueryContext(ctx, baseQuery, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to query templates: %w", err)
	}
	defer rows.Close()

	var templates []api.StoryTemplate
	for rows.Next() {
		var template api.StoryTemplate
		err := rows.Scan(
			&template.ID,
			&template.Type,
			&template.Category,
			&template.BaseTemplate,
			&template.Variables,
			&template.Conditions,
			&template.Variants,
			&template.Active,
			&template.CreatedAt,
			&template.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan template: %w", err)
		}
		templates = append(templates, template)
	}

	// Get total count
	countQuery := "SELECT COUNT(*) FROM legend_templates WHERE 1=1"
	if params.Type != nil {
		countQuery += " AND type = $1"
		args = []interface{}{*params.Type}
	} else {
		args = []interface{}{}
	}

	var totalCount int64
	err = r.db.QueryRowContext(ctx, countQuery, args...).Scan(&totalCount)
	if err != nil {
		return nil, fmt.Errorf("failed to get total count: %w", err)
	}

	response := &api.TemplatesListResponse{
		Templates: templates,
		Total:     totalCount,
		Offset:    offset,
		Limit:     limit,
	}

	return response, nil
}

// CreateTemplate creates a new story template
func (r *LegendRepository) CreateTemplate(ctx context.Context, req *api.CreateTemplateRequest) (*api.StoryTemplate, error) {
	query := `
		INSERT INTO legend_templates (
			id, type, category, base_template, variables, conditions, variants, active
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		RETURNING id, type, category, base_template, variables, conditions,
			variants, active, created_at, updated_at`

	templateID := uuid.New()
	now := time.Now()

	var template api.StoryTemplate
	err := r.db.QueryRowContext(ctx, query,
		templateID,
		req.Type,
		req.Category,
		req.BaseTemplate,
		req.Variables,
		req.Conditions,
		req.Variants,
		true, // active
	).Scan(
		&template.ID,
		&template.Type,
		&template.Category,
		&template.BaseTemplate,
		&template.Variables,
		&template.Conditions,
		&template.Variants,
		&template.Active,
		&template.CreatedAt,
		&template.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to create template: %w", err)
	}

	return &template, nil
}

// GetTemplateByID retrieves a template by ID
func (r *LegendRepository) GetTemplateByID(ctx context.Context, templateID string) (*api.StoryTemplate, error) {
	query := `
		SELECT id, type, category, base_template, variables, conditions,
			   variants, active, created_at, updated_at
		FROM legend_templates
		WHERE id = $1`

	var template api.StoryTemplate
	err := r.db.QueryRowContext(ctx, query, templateID).Scan(
		&template.ID,
		&template.Type,
		&template.Category,
		&template.BaseTemplate,
		&template.Variables,
		&template.Conditions,
		&template.Variants,
		&template.Active,
		&template.CreatedAt,
		&template.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get template: %w", err)
	}

	return &template, nil
}

// UpdateTemplate updates an existing template
func (r *LegendRepository) UpdateTemplate(ctx context.Context, templateID string, req *api.UpdateTemplateRequest) (*api.StoryTemplate, error) {
	query := `
		UPDATE legend_templates SET
			type = COALESCE($2, type),
			category = COALESCE($3, category),
			base_template = COALESCE($4, base_template),
			variables = COALESCE($5, variables),
			conditions = COALESCE($6, conditions),
			variants = COALESCE($7, variants),
			active = COALESCE($8, active),
			updated_at = $9
		WHERE id = $1
		RETURNING id, type, category, base_template, variables, conditions,
			variants, active, created_at, updated_at`

	var template api.StoryTemplate
	err := r.db.QueryRowContext(ctx, query,
		templateID,
		req.Type,
		req.Category,
		req.BaseTemplate,
		req.Variables,
		req.Conditions,
		req.Variants,
		req.Active,
		time.Now(),
	).Scan(
		&template.ID,
		&template.Type,
		&template.Category,
		&template.BaseTemplate,
		&template.Variables,
		&template.Conditions,
		&template.Variants,
		&template.Active,
		&template.CreatedAt,
		&template.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to update template: %w", err)
	}

	return &template, nil
}

// DeleteTemplate deletes a template
func (r *LegendRepository) DeleteTemplate(ctx context.Context, templateID string) error {
	query := "DELETE FROM legend_templates WHERE id = $1"
	result, err := r.db.ExecContext(ctx, query, templateID)
	if err != nil {
		return fmt.Errorf("failed to delete template: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("template not found")
	}

	return nil
}

// GetVariables retrieves variable rules
func (r *LegendRepository) GetVariables(ctx context.Context, params api.GetVariablesParams) (*api.VariablesListResponse, error) {
	baseQuery := `
		SELECT id, type, name, rules, active, created_at, updated_at
		FROM legend_variables
		WHERE 1=1`

	args := []interface{}{}
	argCount := 0

	// Add filters
	if params.Type != nil {
		argCount++
		baseQuery += fmt.Sprintf(" AND type = $%d", argCount)
		args = append(args, *params.Type)
	}

	// Add ordering and pagination
	baseQuery += " ORDER BY created_at DESC"

	limit := int32(50) // default
	if params.Limit != nil && *params.Limit <= 100 {
		limit = *params.Limit
	}
	argCount++
	baseQuery += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, limit)

	offset := int32(0) // default
	if params.Offset != nil {
		offset = *params.Offset
	}
	argCount++
	baseQuery += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, offset)

	// Execute query
	rows, err := r.db.QueryContext(ctx, baseQuery, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to query variables: %w", err)
	}
	defer rows.Close()

	var variables []api.VariableRule
	for rows.Next() {
		var variable api.VariableRule
		err := rows.Scan(
			&variable.ID,
			&variable.Type,
			&variable.Name,
			&variable.Rules,
			&variable.Active,
			&variable.CreatedAt,
			&variable.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan variable: %w", err)
		}
		variables = append(variables, variable)
	}

	// Get total count
	var totalCount int64
	err = r.db.QueryRowContext(ctx, "SELECT COUNT(*) FROM legend_variables").Scan(&totalCount)
	if err != nil {
		return nil, fmt.Errorf("failed to get total count: %w", err)
	}

	response := &api.VariablesListResponse{
		Variables: variables,
		Total:     totalCount,
		Offset:    offset,
		Limit:     limit,
	}

	return response, nil
}

// CreateVariable creates a new variable rule
func (r *LegendRepository) CreateVariable(ctx context.Context, req *api.CreateVariableRequest) (*api.VariableRule, error) {
	query := `
		INSERT INTO legend_variables (id, type, name, rules, active)
		VALUES ($1, $2, $3, $4, $5)
		RETURNING id, type, name, rules, active, created_at, updated_at`

	variableID := uuid.New()

	var variable api.VariableRule
	err := r.db.QueryRowContext(ctx, query,
		variableID,
		req.Type,
		req.Name,
		req.Rules,
		true, // active
	).Scan(
		&variable.ID,
		&variable.Type,
		&variable.Name,
		&variable.Rules,
		&variable.Active,
		&variable.CreatedAt,
		&variable.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to create variable: %w", err)
	}

	return &variable, nil
}

// GetActiveTemplatesForCache retrieves active templates for caching
func (r *LegendRepository) GetActiveTemplatesForCache(ctx context.Context, eventType string) ([]api.ActiveTemplate, error) {
	query := `
		SELECT id, type, category, base_template, variables, variants
		FROM legend_templates
		WHERE active = true AND type = $1`

	rows, err := r.db.QueryContext(ctx, query, eventType)
	if err != nil {
		return nil, fmt.Errorf("failed to query active templates: %w", err)
	}
	defer rows.Close()

	var templates []api.ActiveTemplate
	for rows.Next() {
		var template api.ActiveTemplate
		err := rows.Scan(
			&template.ID,
			&template.Type,
			&template.Category,
			&template.BaseTemplate,
			&template.Variables,
			&template.Variants,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan active template: %w", err)
		}
		templates = append(templates, template)
	}

	return templates, nil
}
