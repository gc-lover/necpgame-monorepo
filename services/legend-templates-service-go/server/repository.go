// Legend Templates Repository - Database layer
// Issue: #2241
// PERFORMANCE: Optimized queries for MMOFPS legend generation

package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/legend-templates-service-go/pkg/api"
)

// Repository handles database operations
type Repository struct {
	db *sql.DB
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB) *Repository {
	return &Repository{db: db}
}

// PERFORMANCE: Connection pool configuration for high-throughput legend generation
func SetupDatabaseConnection(dsn string) (*sql.DB, error) {
	db, err := sql.Open("postgres", dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// PERFORMANCE: Optimized connection pool settings for MMOFPS
	db.SetMaxOpenConns(25)                 // Moderate pool size
	db.SetMaxIdleConns(25)                 // Keep connections alive
	db.SetConnMaxLifetime(5 * time.Minute) // Recycle connections

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// Template operations

// CreateTemplate saves a new template to database
func (r *Repository) CreateTemplate(ctx context.Context, template *api.StoryTemplate) error {
	query := `
		INSERT INTO legend_templates.templates (
			id, type, category, base_template, created_at, updated_at,
			conditions, variables, variants, active
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	// TODO: Implement JSON marshaling for complex fields
	_, err := r.db.ExecContext(ctx, query,
		template.ID,
		template.Type,
		template.Category,
		template.BaseTemplate,
		template.CreatedAt,
		template.UpdatedAt,
		nil, // conditions (JSON)
		nil, // variables (JSON array)
		nil, // variants (JSON array)
		template.Active,
	)

	if err != nil {
		return fmt.Errorf("failed to create template: %w", err)
	}

	return nil
}

// GetTemplate retrieves template by ID
func (r *Repository) GetTemplate(ctx context.Context, id uuid.UUID) (*api.StoryTemplate, error) {
	query := `
		SELECT id, type, category, base_template, created_at, updated_at,
			   conditions, variables, variants, active
		FROM legend_templates.templates
		WHERE id = $1 AND active = true
	`

	var template api.StoryTemplate
	// TODO: Implement JSON unmarshaling
	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&template.ID,
		&template.Type,
		&template.Category,
		&template.BaseTemplate,
		&template.CreatedAt,
		&template.UpdatedAt,
		nil, // conditions
		nil, // variables
		nil, // variants
		&template.Active,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("template not found")
		}
		return nil, fmt.Errorf("failed to get template: %w", err)
	}

	return &template, nil
}

// UpdateTemplate updates existing template
func (r *Repository) UpdateTemplate(ctx context.Context, template *api.StoryTemplate) error {
	query := `
		UPDATE legend_templates.templates
		SET type = $2, category = $3, base_template = $4, updated_at = $5,
			conditions = $6, variables = $7, variants = $8, active = $9
		WHERE id = $1
	`

	// TODO: Implement JSON marshaling
	_, err := r.db.ExecContext(ctx, query,
		template.ID,
		template.Type,
		template.Category,
		template.BaseTemplate,
		time.Now(),
		nil, // conditions
		nil, // variables
		nil, // variants
		template.Active,
	)

	if err != nil {
		return fmt.Errorf("failed to update template: %w", err)
	}

	return nil
}

// DeleteTemplate soft deletes a template
func (r *Repository) DeleteTemplate(ctx context.Context, id uuid.UUID) error {
	query := `UPDATE legend_templates.templates SET active = false WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete template: %w", err)
	}

	return nil
}

// ListTemplates retrieves templates with filtering and pagination
func (r *Repository) ListTemplates(ctx context.Context, filter TemplateFilter) ([]api.StoryTemplate, int, error) {
	query := `
		SELECT id, type, category, base_template, created_at, updated_at,
			   conditions, variables, variants, active
		FROM legend_templates.templates
		WHERE active = true
	`
	args := []interface{}{}
	argCount := 0

	// Add filters
	if filter.Type != "" {
		argCount++
		query += fmt.Sprintf(" AND type = $%d", argCount)
		args = append(args, filter.Type)
	}

	if filter.Category != "" {
		argCount++
		query += fmt.Sprintf(" AND category = $%d", argCount)
		args = append(args, filter.Category)
	}

	// Add pagination
	argCount++
	query += fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d", argCount)
	args = append(args, filter.Limit)

	if filter.Offset > 0 {
		argCount++
		query += fmt.Sprintf(" OFFSET $%d", argCount)
		args = append(args, filter.Offset)
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list templates: %w", err)
	}
	defer rows.Close()

	var templates []api.StoryTemplate
	for rows.Next() {
		var template api.StoryTemplate
		// TODO: Implement JSON unmarshaling
		err := rows.Scan(
			&template.ID,
			&template.Type,
			&template.Category,
			&template.BaseTemplate,
			&template.CreatedAt,
			&template.UpdatedAt,
			nil, // conditions
			nil, // variables
			nil, // variants
			&template.Active,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan template: %w", err)
		}
		templates = append(templates, template)
	}

	// Get total count
	countQuery := `SELECT COUNT(*) FROM legend_templates.templates WHERE active = true`
	// TODO: Add same filters to count query

	var total int
	err = r.db.QueryRowContext(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	return templates, total, nil
}

// TemplateFilter defines filtering options for templates
type TemplateFilter struct {
	Type     string
	Category string
	Limit    int
	Offset   int
}

// Variable operations

// CreateVariable saves a new variable rule
func (r *Repository) CreateVariable(ctx context.Context, variable *api.VariableRule) error {
	query := `
		INSERT INTO legend_templates.variables (
			id, type, name, created_at, updated_at, rules, active
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	// TODO: Implement JSON marshaling for rules
	_, err := r.db.ExecContext(ctx, query,
		variable.ID,
		variable.Type,
		variable.Name,
		variable.CreatedAt,
		variable.UpdatedAt,
		nil, // rules (JSON)
		variable.Active,
	)

	if err != nil {
		return fmt.Errorf("failed to create variable: %w", err)
	}

	return nil
}

// GetVariable retrieves variable by ID
func (r *Repository) GetVariable(ctx context.Context, id uuid.UUID) (*api.VariableRule, error) {
	query := `
		SELECT id, type, name, created_at, updated_at, rules, active
		FROM legend_templates.variables
		WHERE id = $1 AND active = true
	`

	var variable api.VariableRule
	// TODO: Implement JSON unmarshaling
	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&variable.ID,
		&variable.Type,
		&variable.Name,
		&variable.CreatedAt,
		&variable.UpdatedAt,
		nil, // rules
		&variable.Active,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("variable not found")
		}
		return nil, fmt.Errorf("failed to get variable: %w", err)
	}

	return &variable, nil
}

// ListVariables retrieves variables with filtering and pagination
func (r *Repository) ListVariables(ctx context.Context, filter VariableFilter) ([]api.VariableRule, int, error) {
	query := `
		SELECT id, type, name, created_at, updated_at, rules, active
		FROM legend_templates.variables
		WHERE active = true
	`
	args := []interface{}{}
	argCount := 0

	// Add filters
	if filter.Type != "" {
		argCount++
		query += fmt.Sprintf(" AND type = $%d", argCount)
		args = append(args, filter.Type)
	}

	// Add pagination
	argCount++
	query += fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d", argCount)
	args = append(args, filter.Limit)

	if filter.Offset > 0 {
		argCount++
		query += fmt.Sprintf(" OFFSET $%d", argCount)
		args = append(args, filter.Offset)
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list variables: %w", err)
	}
	defer rows.Close()

	var variables []api.VariableRule
	for rows.Next() {
		var variable api.VariableRule
		// TODO: Implement JSON unmarshaling
		err := rows.Scan(
			&variable.ID,
			&variable.Type,
			&variable.Name,
			&variable.CreatedAt,
			&variable.UpdatedAt,
			nil, // rules
			&variable.Active,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan variable: %w", err)
		}
		variables = append(variables, variable)
	}

	// Get total count
	countQuery := `SELECT COUNT(*) FROM legend_templates.variables WHERE active = true`

	var total int
	err = r.db.QueryRowContext(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	return variables, total, nil
}

// VariableFilter defines filtering options for variables
type VariableFilter struct {
	Type   string
	Limit  int
	Offset int
}

// GetActiveTemplatesForGeneration retrieves cached active templates for fast generation
// PERFORMANCE: HOT PATH - cached query, <100μs target
func (r *Repository) GetActiveTemplatesForGeneration(ctx context.Context, templateType *string) ([]api.ActiveTemplate, error) {
	query := `
		SELECT id, type, category, base_template, variables, variants
		FROM legend_templates.templates
		WHERE active = true
	`
	args := []interface{}{}

	if templateType != nil {
		query += " AND type = $1"
		args = append(args, *templateType)
	}

	query += " ORDER BY created_at DESC"

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to get active templates: %w", err)
	}
	defer rows.Close()

	var templates []api.ActiveTemplate
	for rows.Next() {
		var template api.ActiveTemplate
		// TODO: Implement JSON unmarshaling for variables and variants
		err := rows.Scan(
			&template.ID,
			&template.Type,
			&template.Category,
			&template.BaseTemplate,
			nil, // variables
			nil, // variants
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan active template: %w", err)
		}
		templates = append(templates, template)
	}

	return templates, nil
}