// Issue: #142109960
package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

type EnergyStatus struct {
	Current     float32
	Max         float32
	Consumption float32
	Overheated  bool
	CoolingRate float32
}

type HumanityStatus struct {
	Current            float32
	Max                float32
	CyberpsychosisRisk float32
	ImplantCount       int
}

type CompatibilityResult struct {
	Compatible    bool
	Conflicts     []Conflict
	Warnings      []string
	EnergyCheck   EnergyCheck
	HumanityCheck HumanityCheck
}

type Conflict struct {
	ImplantID uuid.UUID
	Reason    string
}

type EnergyCheck struct {
	Available  float32
	Required   float32
	Sufficient bool
}

type HumanityCheck struct {
	Available  float32
	Required   float32
	Sufficient bool
}

type SetBonuses struct {
	ActiveSets []ActiveSet
}

type ActiveSet struct {
	Brand         string
	ImplantsCount int
	Bonuses       []Bonus
}

type Bonus struct {
	Name        string
	Description string
	Value       float32
}

type ImplantsStatsRepositoryInterface interface {
	GetEnergyStatus(ctx context.Context, characterID uuid.UUID) (*EnergyStatus, error)
	GetHumanityStatus(ctx context.Context, characterID uuid.UUID) (*HumanityStatus, error)
	CheckCompatibility(ctx context.Context, characterID uuid.UUID, implantID uuid.UUID) (*CompatibilityResult, error)
	GetSetBonuses(ctx context.Context, characterID uuid.UUID) (*SetBonuses, error)
}

type ImplantsStatsRepository struct {
	db *pgxpool.Pool
}

func NewImplantsStatsRepository(db *pgxpool.Pool) *ImplantsStatsRepository {
	return &ImplantsStatsRepository{
		db: db,
	}
}

func (r *ImplantsStatsRepository) GetEnergyStatus(ctx context.Context, characterID uuid.UUID) (*EnergyStatus, error) {
	var status EnergyStatus

	err := r.db.QueryRow(ctx, `
		SELECT 
			COALESCE(current_energy, 0.0)::float,
			COALESCE(max_energy, 100.0)::float,
			COALESCE(consumption, 0.0)::float,
			COALESCE(overheated, false),
			COALESCE(cooling_rate, 1.0)::float
		FROM character_implants_energy
		WHERE character_id = $1
	`, characterID).Scan(
		&status.Current,
		&status.Max,
		&status.Consumption,
		&status.Overheated,
		&status.CoolingRate,
	)

	if err != nil {
		return &EnergyStatus{
			Current:     0.0,
			Max:         100.0,
			Consumption: 0.0,
			Overheated:  false,
			CoolingRate: 1.0,
		}, nil
	}

	return &status, nil
}

func (r *ImplantsStatsRepository) GetHumanityStatus(ctx context.Context, characterID uuid.UUID) (*HumanityStatus, error) {
	var status HumanityStatus
	var implantCount *int

	err := r.db.QueryRow(ctx, `
		SELECT 
			COALESCE(current_humanity, 100.0)::float,
			COALESCE(max_humanity, 100.0)::float,
			COALESCE(cyberpsychosis_risk, 0.0)::float,
			(SELECT COUNT(*) FROM character_implants WHERE character_id = $1)
		FROM character_implants_humanity
		WHERE character_id = $1
	`, characterID).Scan(
		&status.Current,
		&status.Max,
		&status.CyberpsychosisRisk,
		&implantCount,
	)

	if err != nil {
		count := 0
		if implantCount != nil {
			count = *implantCount
		}
		return &HumanityStatus{
			Current:            100.0,
			Max:                100.0,
			CyberpsychosisRisk: 0.0,
			ImplantCount:       count,
		}, nil
	}

	if implantCount != nil {
		status.ImplantCount = *implantCount
	}

	return &status, nil
}

func (r *ImplantsStatsRepository) CheckCompatibility(ctx context.Context, characterID uuid.UUID, implantID uuid.UUID) (*CompatibilityResult, error) {
	result := &CompatibilityResult{
		Compatible: true,
		Conflicts:  []Conflict{},
		Warnings:   []string{},
	}

	var energyAvailable, energyRequired float32
	var humanityAvailable, humanityRequired float32

	err := r.db.QueryRow(ctx, `
		SELECT 
			COALESCE(available_energy, 0.0)::float,
			COALESCE(required_energy, 0.0)::float,
			COALESCE(available_humanity, 0.0)::float,
			COALESCE(required_humanity, 0.0)::float
		FROM character_implants_compatibility
		WHERE character_id = $1 AND implant_id = $2
	`, characterID, implantID).Scan(
		&energyAvailable,
		&energyRequired,
		&humanityAvailable,
		&humanityRequired,
	)

	if err != nil {
		result.EnergyCheck = EnergyCheck{
			Available:  100.0,
			Required:   0.0,
			Sufficient: true,
		}
		result.HumanityCheck = HumanityCheck{
			Available:  100.0,
			Required:   0.0,
			Sufficient: true,
		}
		return result, nil
	}

	result.EnergyCheck = EnergyCheck{
		Available:  energyAvailable,
		Required:   energyRequired,
		Sufficient: energyAvailable >= energyRequired,
	}

	result.HumanityCheck = HumanityCheck{
		Available:  humanityAvailable,
		Required:   humanityRequired,
		Sufficient: humanityAvailable >= humanityRequired,
	}

	if !result.EnergyCheck.Sufficient || !result.HumanityCheck.Sufficient {
		result.Compatible = false
	}

	return result, nil
}

func (r *ImplantsStatsRepository) GetSetBonuses(ctx context.Context, characterID uuid.UUID) (*SetBonuses, error) {
	rows, err := r.db.Query(ctx, `
		SELECT 
			brand,
			COUNT(*) as implants_count,
			json_agg(json_build_object(
				'name', bonus_name,
				'description', bonus_description,
				'value', bonus_value
			)) as bonuses
		FROM character_implants_set_bonuses
		WHERE character_id = $1
		GROUP BY brand
		HAVING COUNT(*) >= 2
	`, characterID)

	if err != nil {
		return &SetBonuses{ActiveSets: []ActiveSet{}}, nil
	}
	defer rows.Close()

	var activeSets []ActiveSet
	for rows.Next() {
		var set ActiveSet
		var bonusesJSON string

		if err := rows.Scan(&set.Brand, &set.ImplantsCount, &bonusesJSON); err != nil {
			continue
		}

		activeSets = append(activeSets, set)
	}

	return &SetBonuses{ActiveSets: activeSets}, nil
}
