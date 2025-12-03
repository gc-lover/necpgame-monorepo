// Issue: #141887950
package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type EngramChipTier struct {
	Tier                int     `json:"tier"`
	TierName            string  `json:"tier_name"`
	StabilityLevel      string  `json:"stability_level"`
	LifespanYearsMin    int     `json:"lifespan_years_min"`
	LifespanYearsMax    int     `json:"lifespan_years_max"`
	CorruptionRisk      string  `json:"corruption_risk"`
	CorruptionRiskPercent float64 `json:"corruption_risk_percent"`
	ProtectionLevel     string  `json:"protection_level"`
	CreationCostMin     float64 `json:"creation_cost_min"`
	CreationCostMax     float64 `json:"creation_cost_max"`
	AvailableFromYear   int     `json:"available_from_year"`
}

type EngramChipDecay struct {
	ID                      uuid.UUID              `json:"id"`
	ChipID                  uuid.UUID              `json:"chip_id"`
	Tier                    int                    `json:"tier"`
	DecayPercent            float64                `json:"decay_percent"`
	DecayRisk               string                 `json:"decay_risk"`
	StorageTemperature      string                 `json:"storage_temperature"`
	StorageHumidity         string                 `json:"storage_humidity"`
	ElectromagneticShield   bool                   `json:"electromagnetic_shield"`
	StorageTimeOutsideHours int                    `json:"storage_time_outside_hours"`
	TimeUntilCriticalHours  *int                   `json:"time_until_critical_hours,omitempty"`
	DecayEffects            []string               `json:"decay_effects"`
	LastCheckedAt           time.Time              `json:"last_checked_at"`
	CreatedAt               time.Time              `json:"created_at"`
	UpdatedAt               time.Time              `json:"updated_at"`
}

type EngramChipsRepositoryInterface interface {
	GetChipTiers(ctx context.Context, leagueYear *int) ([]*EngramChipTier, error)
	GetChipTierByTier(ctx context.Context, tier int) (*EngramChipTier, error)
	GetChipTierByChipID(ctx context.Context, chipID uuid.UUID) (*EngramChipTier, error)
	GetChipDecay(ctx context.Context, chipID uuid.UUID) (*EngramChipDecay, error)
	CreateChipDecay(ctx context.Context, chipID uuid.UUID, tier int) (*EngramChipDecay, error)
	UpdateChipDecay(ctx context.Context, decay *EngramChipDecay) error
}

type EngramChipsRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewEngramChipsRepository(db *pgxpool.Pool) *EngramChipsRepository {
	return &EngramChipsRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *EngramChipsRepository) GetChipTiers(ctx context.Context, leagueYear *int) ([]*EngramChipTier, error) {
	query := `SELECT tier, tier_name, stability_level, lifespan_years_min, lifespan_years_max,
	         corruption_risk, corruption_risk_percent, protection_level,
	         creation_cost_min, creation_cost_max, available_from_year
	         FROM inventory.engram_chip_tiers
	         ORDER BY tier ASC`

	var rows pgx.Rows
	var err error

	if leagueYear != nil {
		query += ` WHERE available_from_year <= $1`
		rows, err = r.db.Query(ctx, query, *leagueYear)
	} else {
		rows, err = r.db.Query(ctx, query)
	}

	if err != nil {
		r.logger.WithError(err).Error("Failed to get chip tiers")
		return nil, err
	}
	defer rows.Close()

	var tiers []*EngramChipTier
	for rows.Next() {
		tier := &EngramChipTier{}
		err := rows.Scan(
			&tier.Tier, &tier.TierName, &tier.StabilityLevel,
			&tier.LifespanYearsMin, &tier.LifespanYearsMax,
			&tier.CorruptionRisk, &tier.CorruptionRiskPercent,
			&tier.ProtectionLevel, &tier.CreationCostMin, &tier.CreationCostMax,
			&tier.AvailableFromYear,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan chip tier")
			continue
		}

		if leagueYear != nil {
			isAvailable := tier.AvailableFromYear <= *leagueYear
			if !isAvailable {
				continue
			}
		}

		tiers = append(tiers, tier)
	}

	return tiers, nil
}

func (r *EngramChipsRepository) GetChipTierByTier(ctx context.Context, tier int) (*EngramChipTier, error) {
	chipTier := &EngramChipTier{}

	err := r.db.QueryRow(ctx,
		`SELECT tier, tier_name, stability_level, lifespan_years_min, lifespan_years_max,
		 corruption_risk, corruption_risk_percent, protection_level,
		 creation_cost_min, creation_cost_max, available_from_year
		 FROM inventory.engram_chip_tiers
		 WHERE tier = $1`,
		tier,
	).Scan(
		&chipTier.Tier, &chipTier.TierName, &chipTier.StabilityLevel,
		&chipTier.LifespanYearsMin, &chipTier.LifespanYearsMax,
		&chipTier.CorruptionRisk, &chipTier.CorruptionRiskPercent,
		&chipTier.ProtectionLevel, &chipTier.CreationCostMin, &chipTier.CreationCostMax,
		&chipTier.AvailableFromYear,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get chip tier")
		return nil, err
	}

	return chipTier, nil
}

func (r *EngramChipsRepository) GetChipTierByChipID(ctx context.Context, chipID uuid.UUID) (*EngramChipTier, error) {
	var tier int

	err := r.db.QueryRow(ctx,
		`SELECT tier FROM inventory.engram_chip_decay WHERE chip_id = $1`,
		chipID,
	).Scan(&tier)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get chip tier from decay")
		return nil, err
	}

	return r.GetChipTierByTier(ctx, tier)
}

func (r *EngramChipsRepository) GetChipDecay(ctx context.Context, chipID uuid.UUID) (*EngramChipDecay, error) {
	decay := &EngramChipDecay{}
	var decayEffectsJSON []byte
	var timeUntilCritical *int

	err := r.db.QueryRow(ctx,
		`SELECT id, chip_id, tier, decay_percent, decay_risk,
		 storage_temperature, storage_humidity, electromagnetic_shield,
		 storage_time_outside_hours, time_until_critical_hours,
		 decay_effects, last_checked_at, created_at, updated_at
		 FROM inventory.engram_chip_decay
		 WHERE chip_id = $1`,
		chipID,
	).Scan(
		&decay.ID, &decay.ChipID, &decay.Tier, &decay.DecayPercent, &decay.DecayRisk,
		&decay.StorageTemperature, &decay.StorageHumidity, &decay.ElectromagneticShield,
		&decay.StorageTimeOutsideHours, &timeUntilCritical,
		&decayEffectsJSON, &decay.LastCheckedAt, &decay.CreatedAt, &decay.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get chip decay")
		return nil, err
	}

	if decayEffectsJSON != nil {
		if err := json.Unmarshal(decayEffectsJSON, &decay.DecayEffects); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal decay effects JSON")
			decay.DecayEffects = []string{}
		}
	}

	decay.TimeUntilCriticalHours = timeUntilCritical

	return decay, nil
}

func (r *EngramChipsRepository) CreateChipDecay(ctx context.Context, chipID uuid.UUID, tier int) (*EngramChipDecay, error) {
	decay := &EngramChipDecay{
		ID:                    uuid.New(),
		ChipID:                chipID,
		Tier:                  tier,
		DecayPercent:          0.0,
		DecayRisk:             "none",
		StorageTemperature:    "optimal",
		StorageHumidity:       "optimal",
		ElectromagneticShield: true,
		StorageTimeOutsideHours: 0,
		DecayEffects:          []string{},
		LastCheckedAt:         time.Now(),
		CreatedAt:             time.Now(),
		UpdatedAt:             time.Now(),
	}

	decayEffectsJSON, _ := json.Marshal(decay.DecayEffects)

	_, err := r.db.Exec(ctx,
		`INSERT INTO inventory.engram_chip_decay 
		 (id, chip_id, tier, decay_percent, decay_risk,
		  storage_temperature, storage_humidity, electromagnetic_shield,
		  storage_time_outside_hours, decay_effects, last_checked_at, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)`,
		decay.ID, decay.ChipID, decay.Tier, decay.DecayPercent, decay.DecayRisk,
		decay.StorageTemperature, decay.StorageHumidity, decay.ElectromagneticShield,
		decay.StorageTimeOutsideHours, decayEffectsJSON, decay.LastCheckedAt,
		decay.CreatedAt, decay.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create chip decay")
		return nil, err
	}

	return decay, nil
}

func (r *EngramChipsRepository) UpdateChipDecay(ctx context.Context, decay *EngramChipDecay) error {
	decayEffectsJSON, _ := json.Marshal(decay.DecayEffects)
	decay.UpdatedAt = time.Now()
	decay.LastCheckedAt = time.Now()

	_, err := r.db.Exec(ctx,
		`UPDATE inventory.engram_chip_decay
		 SET decay_percent = $1, decay_risk = $2,
		     storage_temperature = $3, storage_humidity = $4,
		     electromagnetic_shield = $5, storage_time_outside_hours = $6,
		     time_until_critical_hours = $7, decay_effects = $8,
		     last_checked_at = $9, updated_at = $10
		 WHERE chip_id = $11`,
		decay.DecayPercent, decay.DecayRisk,
		decay.StorageTemperature, decay.StorageHumidity,
		decay.ElectromagneticShield, decay.StorageTimeOutsideHours,
		decay.TimeUntilCriticalHours, decayEffectsJSON,
		decay.LastCheckedAt, decay.UpdatedAt, decay.ChipID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update chip decay")
		return err
	}

	return nil
}



