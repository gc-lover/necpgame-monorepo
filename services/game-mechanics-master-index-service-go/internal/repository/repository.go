// Package repository содержит репозиторий для работы с механиками
// Issue: #2176 - Game Mechanics Systems Master Index
// PERFORMANCE: Использует pgxpool для оптимальной работы с PostgreSQL в MMOFPS
package repository

import (
	"context"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/game-mechanics-master-index-service-go/internal/models"
)

// Repository интерфейс для работы с данными механик
type Repository interface {
	// Mechanics
	GetMechanic(ctx context.Context, id string) (*models.GameMechanic, error)
	GetMechanicsByType(ctx context.Context, mechanicType string) ([]*models.GameMechanic, error)
	GetActiveMechanics(ctx context.Context) ([]*models.GameMechanic, error)
	CreateMechanic(ctx context.Context, mechanic *models.GameMechanic) error
	UpdateMechanic(ctx context.Context, mechanic *models.GameMechanic) error
	DeleteMechanic(ctx context.Context, id string) error

	// Dependencies
	GetMechanicDependencies(ctx context.Context, mechanicID string) ([]*models.MechanicDependency, error)
	CreateDependency(ctx context.Context, dep *models.MechanicDependency) error
	DeleteDependency(ctx context.Context, id string) error

	// Configurations
	GetMechanicConfig(ctx context.Context, mechanicID string) (*models.MechanicConfig, error)
	SaveMechanicConfig(ctx context.Context, config *models.MechanicConfig) error

	// Health monitoring
	UpdateMechanicStatus(ctx context.Context, status *models.MechanicStatus) error
	GetSystemHealth(ctx context.Context) (*models.SystemHealth, error)
}

// PostgresRepository реализация репозитория для PostgreSQL
type PostgresRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewPostgresRepository создает новый репозиторий
func NewPostgresRepository(db *pgxpool.Pool, logger *zap.Logger) *PostgresRepository {
	return &PostgresRepository{
		db:     db,
		logger: logger,
	}
}

// GetMechanic получает механику по ID
func (r *PostgresRepository) GetMechanic(ctx context.Context, id string) (*models.GameMechanic, error) {
	query := `
		SELECT id, name, type, category, status, version, service_name, endpoint,
			   priority, is_required, created_at, updated_at
		FROM game_mechanics.mechanics
		WHERE id = $1
	`

	var mechanic models.GameMechanic
	err := r.db.QueryRow(ctx, query, id).Scan(
		&mechanic.ID, &mechanic.Name, &mechanic.Type, &mechanic.Category,
		&mechanic.Status, &mechanic.Version, &mechanic.ServiceName, &mechanic.Endpoint,
		&mechanic.Priority, &mechanic.IsRequired, &mechanic.CreatedAt, &mechanic.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get mechanic", zap.String("id", id), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get mechanic")
	}

	return &mechanic, nil
}

// GetMechanicsByType получает все механики определенного типа
func (r *PostgresRepository) GetMechanicsByType(ctx context.Context, mechanicType string) ([]*models.GameMechanic, error) {
	query := `
		SELECT id, name, type, category, status, version, service_name, endpoint,
			   priority, is_required, created_at, updated_at
		FROM game_mechanics.mechanics
		WHERE type = $1
		ORDER BY priority DESC, name
	`

	rows, err := r.db.Query(ctx, query, mechanicType)
	if err != nil {
		r.logger.Error("Failed to get mechanics by type", zap.String("type", mechanicType), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get mechanics by type")
	}
	defer rows.Close()

	var mechanics []*models.GameMechanic
	for rows.Next() {
		var mechanic models.GameMechanic
		err := rows.Scan(
			&mechanic.ID, &mechanic.Name, &mechanic.Type, &mechanic.Category,
			&mechanic.Status, &mechanic.Version, &mechanic.ServiceName, &mechanic.Endpoint,
			&mechanic.Priority, &mechanic.IsRequired, &mechanic.CreatedAt, &mechanic.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan mechanic", zap.Error(err))
			continue
		}
		mechanics = append(mechanics, &mechanic)
	}

	return mechanics, nil
}

// GetActiveMechanics получает все активные механики
func (r *PostgresRepository) GetActiveMechanics(ctx context.Context) ([]*models.GameMechanic, error) {
	query := `
		SELECT id, name, type, category, status, version, service_name, endpoint,
			   priority, is_required, created_at, updated_at
		FROM game_mechanics.mechanics
		WHERE status = 'active'
		ORDER BY priority DESC, name
	`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		r.logger.Error("Failed to get active mechanics", zap.Error(err))
		return nil, errors.Wrap(err, "failed to get active mechanics")
	}
	defer rows.Close()

	var mechanics []*models.GameMechanic
	for rows.Next() {
		var mechanic models.GameMechanic
		err := rows.Scan(
			&mechanic.ID, &mechanic.Name, &mechanic.Type, &mechanic.Category,
			&mechanic.Status, &mechanic.Version, &mechanic.ServiceName, &mechanic.Endpoint,
			&mechanic.Priority, &mechanic.IsRequired, &mechanic.CreatedAt, &mechanic.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan mechanic", zap.Error(err))
			continue
		}
		mechanics = append(mechanics, &mechanic)
	}

	return mechanics, nil
}

// CreateMechanic создает новую механику
func (r *PostgresRepository) CreateMechanic(ctx context.Context, mechanic *models.GameMechanic) error {
	query := `
		INSERT INTO game_mechanics.mechanics (
			id, name, type, category, status, version, service_name, endpoint,
			priority, is_required, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	now := time.Now()
	mechanic.CreatedAt = now
	mechanic.UpdatedAt = now

	_, err := r.db.Exec(ctx, query,
		mechanic.ID, mechanic.Name, mechanic.Type, mechanic.Category,
		mechanic.Status, mechanic.Version, mechanic.ServiceName, mechanic.Endpoint,
		mechanic.Priority, mechanic.IsRequired, mechanic.CreatedAt, mechanic.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create mechanic", zap.String("id", mechanic.ID), zap.Error(err))
		return errors.Wrap(err, "failed to create mechanic")
	}

	r.logger.Info("Mechanic created", zap.String("id", mechanic.ID), zap.String("name", mechanic.Name))
	return nil
}

// UpdateMechanic обновляет механику
func (r *PostgresRepository) UpdateMechanic(ctx context.Context, mechanic *models.GameMechanic) error {
	query := `
		UPDATE game_mechanics.mechanics SET
			name = $2, type = $3, category = $4, status = $5, version = $6,
			service_name = $7, endpoint = $8, priority = $9, is_required = $10,
			updated_at = $11
		WHERE id = $1
	`

	mechanic.UpdatedAt = time.Now()

	result, err := r.db.Exec(ctx, query,
		mechanic.ID, mechanic.Name, mechanic.Type, mechanic.Category,
		mechanic.Status, mechanic.Version, mechanic.ServiceName, mechanic.Endpoint,
		mechanic.Priority, mechanic.IsRequired, mechanic.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to update mechanic", zap.String("id", mechanic.ID), zap.Error(err))
		return errors.Wrap(err, "failed to update mechanic")
	}

	if result.RowsAffected() == 0 {
		r.logger.Warn("Mechanic not found for update", zap.String("id", mechanic.ID))
		return errors.New("mechanic not found")
	}

	return nil
}

// DeleteMechanic удаляет механику
func (r *PostgresRepository) DeleteMechanic(ctx context.Context, id string) error {
	query := `DELETE FROM game_mechanics.mechanics WHERE id = $1`

	result, err := r.db.Exec(ctx, query, id)
	if err != nil {
		r.logger.Error("Failed to delete mechanic", zap.String("id", id), zap.Error(err))
		return errors.Wrap(err, "failed to delete mechanic")
	}

	if result.RowsAffected() == 0 {
		r.logger.Warn("Mechanic not found for deletion", zap.String("id", id))
		return errors.New("mechanic not found")
	}

	r.logger.Info("Mechanic deleted", zap.String("id", id))
	return nil
}

// GetMechanicDependencies получает зависимости механики
func (r *PostgresRepository) GetMechanicDependencies(ctx context.Context, mechanicID string) ([]*models.MechanicDependency, error) {
	query := `
		SELECT id, mechanic_id, depends_on_id, dependency_type, is_hard_dependency
		FROM game_mechanics.dependencies
		WHERE mechanic_id = $1
		ORDER BY is_hard_dependency DESC, depends_on_id
	`

	rows, err := r.db.Query(ctx, query, mechanicID)
	if err != nil {
		r.logger.Error("Failed to get mechanic dependencies", zap.String("mechanic_id", mechanicID), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get mechanic dependencies")
	}
	defer rows.Close()

	var dependencies []*models.MechanicDependency
	for rows.Next() {
		var dep models.MechanicDependency
		err := rows.Scan(&dep.ID, &dep.MechanicID, &dep.DependsOnID, &dep.DependencyType, &dep.IsHardDependency)
		if err != nil {
			r.logger.Error("Failed to scan dependency", zap.Error(err))
			continue
		}
		dependencies = append(dependencies, &dep)
	}

	return dependencies, nil
}

// CreateDependency создает новую зависимость
func (r *PostgresRepository) CreateDependency(ctx context.Context, dep *models.MechanicDependency) error {
	query := `
		INSERT INTO game_mechanics.dependencies (
			id, mechanic_id, depends_on_id, dependency_type, is_hard_dependency
		) VALUES ($1, $2, $3, $4, $5)
	`

	_, err := r.db.Exec(ctx, query, dep.ID, dep.MechanicID, dep.DependsOnID, dep.DependencyType, dep.IsHardDependency)
	if err != nil {
		r.logger.Error("Failed to create dependency", zap.String("id", dep.ID), zap.Error(err))
		return errors.Wrap(err, "failed to create dependency")
	}

	return nil
}

// DeleteDependency удаляет зависимость
func (r *PostgresRepository) DeleteDependency(ctx context.Context, id string) error {
	query := `DELETE FROM game_mechanics.dependencies WHERE id = $1`

	result, err := r.db.Exec(ctx, query, id)
	if err != nil {
		r.logger.Error("Failed to delete dependency", zap.String("id", id), zap.Error(err))
		return errors.Wrap(err, "failed to delete dependency")
	}

	if result.RowsAffected() == 0 {
		return errors.New("dependency not found")
	}

	return nil
}

// GetMechanicConfig получает конфигурацию механики
func (r *PostgresRepository) GetMechanicConfig(ctx context.Context, mechanicID string) (*models.MechanicConfig, error) {
	query := `
		SELECT mechanic_id, config_version, settings, is_active, updated_at
		FROM game_mechanics.configurations
		WHERE mechanic_id = $1
		ORDER BY updated_at DESC
		LIMIT 1
	`

	var config models.MechanicConfig
	var settings []byte

	err := r.db.QueryRow(ctx, query, mechanicID).Scan(
		&config.MechanicID, &config.ConfigVersion, &settings,
		&config.IsActive, &config.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get mechanic config", zap.String("mechanic_id", mechanicID), zap.Error(err))
		return nil, errors.Wrap(err, "failed to get mechanic config")
	}

	// TODO: Parse JSON settings
	config.Settings = make(map[string]interface{})

	return &config, nil
}

// SaveMechanicConfig сохраняет конфигурацию механики
func (r *PostgresRepository) SaveMechanicConfig(ctx context.Context, config *models.MechanicConfig) error {
	query := `
		INSERT INTO game_mechanics.configurations (
			mechanic_id, config_version, settings, is_active, updated_at
		) VALUES ($1, $2, $3, $4, $5)
		ON CONFLICT (mechanic_id) DO UPDATE SET
			config_version = EXCLUDED.config_version,
			settings = EXCLUDED.settings,
			is_active = EXCLUDED.is_active,
			updated_at = EXCLUDED.updated_at
	`

	config.UpdatedAt = time.Now()
	settingsJSON := "{}" // TODO: Serialize settings to JSON

	_, err := r.db.Exec(ctx, query, config.MechanicID, config.ConfigVersion, settingsJSON, config.IsActive, config.UpdatedAt)
	if err != nil {
		r.logger.Error("Failed to save mechanic config", zap.String("mechanic_id", config.MechanicID), zap.Error(err))
		return errors.Wrap(err, "failed to save mechanic config")
	}

	return nil
}

// UpdateMechanicStatus обновляет статус механики
func (r *PostgresRepository) UpdateMechanicStatus(ctx context.Context, status *models.MechanicStatus) error {
	query := `
		INSERT INTO game_mechanics.status (
			mechanic_id, service_status, response_time, error_count, last_checked, is_healthy
		) VALUES ($1, $2, $3, $4, $5, $6)
		ON CONFLICT (mechanic_id) DO UPDATE SET
			service_status = EXCLUDED.service_status,
			response_time = EXCLUDED.response_time,
			error_count = EXCLUDED.error_count,
			last_checked = EXCLUDED.last_checked,
			is_healthy = EXCLUDED.is_healthy
	`

	_, err := r.db.Exec(ctx, query,
		status.MechanicID, status.ServiceStatus, status.ResponseTime,
		status.ErrorCount, status.LastChecked, status.IsHealthy,
	)

	if err != nil {
		r.logger.Error("Failed to update mechanic status", zap.String("mechanic_id", status.MechanicID), zap.Error(err))
		return errors.Wrap(err, "failed to update mechanic status")
	}

	return nil
}

// GetSystemHealth получает состояние здоровья системы
func (r *PostgresRepository) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	query := `
		SELECT
			COUNT(*) as total_mechanics,
			COUNT(CASE WHEN status = 'active' THEN 1 END) as active_mechanics,
			COUNT(CASE WHEN status != 'active' THEN 1 END) as inactive_mechanics
		FROM game_mechanics.mechanics
	`

	var health models.SystemHealth
	err := r.db.QueryRow(ctx, query).Scan(
		&health.TotalMechanics, &health.ActiveMechanics, &health.InactiveMechanics,
	)

	if err != nil {
		r.logger.Error("Failed to get system health", zap.Error(err))
		return nil, errors.Wrap(err, "failed to get system health")
	}

	// Calculate health score
	if health.TotalMechanics > 0 {
		health.HealthScore = float64(health.ActiveMechanics) / float64(health.TotalMechanics) * 100
	}

	health.LastHealthCheck = time.Now()

	return &health, nil
}