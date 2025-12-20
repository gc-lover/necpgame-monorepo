// Issue: #2203 - Station repository implementation
package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

// StationRepository handles station database operations
type StationRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

// NewStationRepository creates new station repository
func NewStationRepository(db *pgxpool.Pool) *StationRepository {
	return &StationRepository{
		db:     db,
		logger: GetLogger(),
	}
}

// GetByID retrieves station by ID
func (r *StationRepository) GetByID(ctx context.Context, id uuid.UUID) (*Station, error) {
	query := `
		SELECT id, name, description, type, efficiency, zone_id, owner_id,
			   current_order_id, is_available, maintenance_cost, last_maintenance,
			   created_at, updated_at
		FROM crafting_stations
		WHERE id = $1
	`

	var station Station
	err := r.db.QueryRow(ctx, query, id).Scan(
		&station.ID, &station.Name, &station.Description, &station.Type,
		&station.Efficiency, &station.ZoneID, &station.OwnerID,
		&station.CurrentOrderID, &station.IsAvailable, &station.MaintenanceCost,
		&station.LastMaintenance, &station.CreatedAt, &station.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get station: %w", err)
	}

	return &station, nil
}

// List retrieves stations with pagination and filtering
func (r *StationRepository) List(ctx context.Context, zoneID *uuid.UUID, stationType *string, available *bool, limit, offset int) ([]Station, int, error) {
	baseQuery := `
		SELECT id, name, description, type, efficiency, zone_id, owner_id,
			   current_order_id, is_available, maintenance_cost, last_maintenance,
			   created_at, updated_at
		FROM crafting_stations
		WHERE 1=1
	`
	args := []interface{}{}

	if zoneID != nil {
		baseQuery += fmt.Sprintf(" AND zone_id = $%d", len(args)+1)
		args = append(args, *zoneID)
	}

	if stationType != nil {
		baseQuery += fmt.Sprintf(" AND type = $%d", len(args)+1)
		args = append(args, *stationType)
	}

	if available != nil {
		baseQuery += fmt.Sprintf(" AND is_available = $%d", len(args)+1)
		args = append(args, *available)
	}

	// Safe parameterized query construction
	query := baseQuery + fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list stations: %w", err)
	}
	defer rows.Close()

	var stations []Station
	for rows.Next() {
		var station Station
		err := rows.Scan(
			&station.ID, &station.Name, &station.Description, &station.Type,
			&station.Efficiency, &station.ZoneID, &station.OwnerID,
			&station.CurrentOrderID, &station.IsAvailable, &station.MaintenanceCost,
			&station.LastMaintenance, &station.CreatedAt, &station.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan station: %w", err)
		}
		stations = append(stations, station)
	}

	// Get total count
	countQuery := "SELECT COUNT(*) FROM crafting_stations WHERE 1=1"
	countArgs := []interface{}{}

	if zoneID != nil {
		countQuery += " AND zone_id = $1"
		countArgs = append(countArgs, *zoneID)
	}

	if stationType != nil {
		countQuery += " AND type = $2"
		countArgs = append(countArgs, *stationType)
	}

	if available != nil {
		countQuery += " AND is_available = $3"
		countArgs = append(countArgs, *available)
	}

	var total int
	err = r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	return stations, total, nil
}

// Update modifies existing station
func (r *StationRepository) Update(ctx context.Context, station *Station) error {
	query := `
		UPDATE crafting_stations SET
			name = $2, description = $3, type = $4, efficiency = $5,
			zone_id = $6, owner_id = $7, current_order_id = $8,
			is_available = $9, maintenance_cost = $10, last_maintenance = $11,
			updated_at = $12
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		station.ID, station.Name, station.Description, station.Type,
		station.Efficiency, station.ZoneID, station.OwnerID,
		station.CurrentOrderID, station.IsAvailable, station.MaintenanceCost,
		station.LastMaintenance, station.UpdatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to update station: %w", err)
	}

	return nil
}

// BookStation books station for player
func (r *StationRepository) BookStation(ctx context.Context, booking *StationBooking) error {
	query := `
		INSERT INTO station_bookings (
			station_id, player_id, booked_until, priority, created_at
		) VALUES ($1, $2, $3, $4, $5)
	`

	_, err := r.db.Exec(ctx, query,
		booking.StationID, booking.PlayerID, booking.BookedUntil,
		booking.Priority, booking.CreatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to book station: %w", err)
	}

	return nil
}

// GetActiveBooking gets active booking for station
func (r *StationRepository) GetActiveBooking(ctx context.Context, stationID uuid.UUID) (*StationBooking, error) {
	query := `
		SELECT station_id, player_id, booked_until, priority, created_at
		FROM station_bookings
		WHERE station_id = $1 AND booked_until > NOW()
		ORDER BY priority DESC, created_at ASC
		LIMIT 1
	`

	var booking StationBooking
	err := r.db.QueryRow(ctx, query, stationID).Scan(
		&booking.StationID, &booking.PlayerID, &booking.BookedUntil,
		&booking.Priority, &booking.CreatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get active booking: %w", err)
	}

	return &booking, nil
}
