// Issue: #142109955
package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/pkg/implantsmaintenanceapi"
	"github.com/sirupsen/logrus"
)

type ImplantsMaintenanceServiceInterface interface {
	RepairImplant(ctx context.Context, implantID uuid.UUID, repairType string) (*implantsmaintenanceapi.RepairResult, error)
	UpgradeImplant(ctx context.Context, implantID uuid.UUID, components []Component) (*implantsmaintenanceapi.UpgradeResult, error)
	ModifyImplant(ctx context.Context, implantID uuid.UUID, modificationID uuid.UUID) (*implantsmaintenanceapi.ModifyResult, error)
	GetVisuals(ctx context.Context) (*implantsmaintenanceapi.VisualsSettings, error)
	CustomizeVisuals(ctx context.Context, implantID uuid.UUID, visibilityMode *string, colorScheme *string) (*implantsmaintenanceapi.CustomizeVisualsResult, error)
}

type ImplantsMaintenanceService struct {
	repo   ImplantsMaintenanceRepositoryInterface
	logger *logrus.Logger
}

func NewImplantsMaintenanceService(db *pgxpool.Pool) *ImplantsMaintenanceService {
	return &ImplantsMaintenanceService{
		repo:   NewImplantsMaintenanceRepository(db),
		logger: GetLogger(),
	}
}

func (s *ImplantsMaintenanceService) RepairImplant(ctx context.Context, implantID uuid.UUID, repairType string) (*implantsmaintenanceapi.RepairResult, error) {
	result, err := s.repo.RepairImplant(ctx, implantID, repairType)
	if err != nil {
		return nil, err
	}

	return convertRepairResultToAPI(result), nil
}

func (s *ImplantsMaintenanceService) UpgradeImplant(ctx context.Context, implantID uuid.UUID, components []Component) (*implantsmaintenanceapi.UpgradeResult, error) {
	result, err := s.repo.UpgradeImplant(ctx, implantID, components)
	if err != nil {
		return nil, err
	}

	return convertUpgradeResultToAPI(result), nil
}

func (s *ImplantsMaintenanceService) ModifyImplant(ctx context.Context, implantID uuid.UUID, modificationID uuid.UUID) (*implantsmaintenanceapi.ModifyResult, error) {
	result, err := s.repo.ModifyImplant(ctx, implantID, modificationID)
	if err != nil {
		return nil, err
	}

	return convertModifyResultToAPI(result), nil
}

func (s *ImplantsMaintenanceService) GetVisuals(ctx context.Context) (*implantsmaintenanceapi.VisualsSettings, error) {
	settings, err := s.repo.GetVisuals(ctx)
	if err != nil {
		return nil, err
	}

	return convertVisualsSettingsToAPI(settings), nil
}

func (s *ImplantsMaintenanceService) CustomizeVisuals(ctx context.Context, implantID uuid.UUID, visibilityMode *string, colorScheme *string) (*implantsmaintenanceapi.CustomizeVisualsResult, error) {
	result, err := s.repo.CustomizeVisuals(ctx, implantID, visibilityMode, colorScheme)
	if err != nil {
		return nil, err
	}

	return convertCustomizeVisualsResultToAPI(result), nil
}

