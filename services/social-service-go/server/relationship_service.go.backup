package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type RelationshipServiceInterface interface {
	GetRelationships(ctx context.Context, playerID uuid.UUID, relationshipType *models.RelationshipType, limit, offset int) (*models.RelationshipsResponse, error)
	SetRelationship(ctx context.Context, playerID uuid.UUID, req *models.SetRelationshipRequest) (*models.Relationship, error)
	GetRelationshipBetween(ctx context.Context, playerID1, playerID2 uuid.UUID) (*models.Relationship, error)
	GetTrustLevel(ctx context.Context, playerID, targetID uuid.UUID) (*models.TrustLevel, error)
	UpdateTrust(ctx context.Context, playerID uuid.UUID, req *models.UpdateTrustRequest) (*models.TrustLevel, error)
	CreateTrustContract(ctx context.Context, playerID uuid.UUID, req *models.CreateTrustContractRequest) (*models.TrustContract, error)
	GetTrustContract(ctx context.Context, contractID uuid.UUID) (*models.TrustContract, error)
	TerminateTrustContract(ctx context.Context, contractID uuid.UUID) error
	CreateAlliance(ctx context.Context, leaderID uuid.UUID, req *models.CreateAllianceRequest) (*models.Alliance, error)
	GetAlliances(ctx context.Context, limit, offset int) (*models.AllianceListResponse, error)
	GetAlliance(ctx context.Context, allianceID uuid.UUID) (*models.Alliance, error)
	TerminateAlliance(ctx context.Context, allianceID uuid.UUID) error
	InviteToAlliance(ctx context.Context, allianceID, inviterID uuid.UUID, req *models.AllianceInviteRequest) error
	JoinAlliance(ctx context.Context, allianceID, playerID uuid.UUID) error
	LeaveAlliance(ctx context.Context, allianceID, playerID uuid.UUID) error
	GetPlayerRatings(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.PlayerRatingsResponse, error)
	UpdateRating(ctx context.Context, playerID uuid.UUID, req *models.UpdateRatingRequest) (*models.PlayerRating, error)
	GetSocialCapital(ctx context.Context, playerID uuid.UUID) (*models.SocialCapital, error)
	GetInteractionHistory(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.InteractionHistoryResponse, error)
	RequestArbitration(ctx context.Context, requesterID uuid.UUID, req *models.RequestArbitrationRequest) (*models.ArbitrationCase, error)
	GetArbitrationCase(ctx context.Context, caseID uuid.UUID) (*models.ArbitrationCase, error)
}

type RelationshipService struct {
	repo        *RelationshipRepository
	allianceRepo *RelationshipAllianceRepository
	logger      *logrus.Logger
}

func NewRelationshipService(repo *RelationshipRepository, allianceRepo *RelationshipAllianceRepository) *RelationshipService {
	return &RelationshipService{
		repo:        repo,
		allianceRepo: allianceRepo,
		logger:      GetLogger(),
	}
}

func (s *RelationshipService) GetRelationships(ctx context.Context, playerID uuid.UUID, relationshipType *models.RelationshipType, limit, offset int) (*models.RelationshipsResponse, error) {
	relationships, total, err := s.repo.GetRelationships(ctx, playerID, relationshipType, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.RelationshipsResponse{
		Relationships: relationships,
		Total:         total,
		Limit:         limit,
		Offset:        offset,
	}, nil
}

func (s *RelationshipService) SetRelationship(ctx context.Context, playerID uuid.UUID, req *models.SetRelationshipRequest) (*models.Relationship, error) {
	if playerID == req.TargetID {
		return nil, fmt.Errorf("cannot set relationship with yourself")
	}

	return s.repo.SetRelationship(ctx, playerID, req)
}

func (s *RelationshipService) GetRelationshipBetween(ctx context.Context, playerID1, playerID2 uuid.UUID) (*models.Relationship, error) {
	return s.repo.GetRelationshipBetween(ctx, playerID1, playerID2)
}

func (s *RelationshipService) GetTrustLevel(ctx context.Context, playerID, targetID uuid.UUID) (*models.TrustLevel, error) {
	return s.repo.GetTrustLevel(ctx, playerID, targetID)
}

func (s *RelationshipService) UpdateTrust(ctx context.Context, playerID uuid.UUID, req *models.UpdateTrustRequest) (*models.TrustLevel, error) {
	if req.Delta < -100 || req.Delta > 100 {
		return nil, fmt.Errorf("trust delta must be between -100 and 100")
	}

	return s.repo.UpdateTrust(ctx, playerID, req)
}

func (s *RelationshipService) CreateTrustContract(ctx context.Context, playerID uuid.UUID, req *models.CreateTrustContractRequest) (*models.TrustContract, error) {
	if playerID == req.TargetID {
		return nil, fmt.Errorf("cannot create trust contract with yourself")
	}

	return s.repo.CreateTrustContract(ctx, playerID, req)
}

func (s *RelationshipService) GetTrustContract(ctx context.Context, contractID uuid.UUID) (*models.TrustContract, error) {
	return s.repo.GetTrustContract(ctx, contractID)
}

func (s *RelationshipService) TerminateTrustContract(ctx context.Context, contractID uuid.UUID) error {
	return s.repo.TerminateTrustContract(ctx, contractID)
}

func (s *RelationshipService) CreateAlliance(ctx context.Context, leaderID uuid.UUID, req *models.CreateAllianceRequest) (*models.Alliance, error) {
	if req.Name == "" {
		return nil, fmt.Errorf("alliance name is required")
	}

	alliance, err := s.allianceRepo.CreateAlliance(ctx, leaderID, req)
	if err != nil {
		return nil, err
	}

	if err := s.allianceRepo.JoinAlliance(ctx, alliance.ID, leaderID); err != nil {
		return nil, fmt.Errorf("failed to add leader to alliance: %w", err)
	}

	return alliance, nil
}

func (s *RelationshipService) GetAlliances(ctx context.Context, limit, offset int) (*models.AllianceListResponse, error) {
	alliances, total, err := s.allianceRepo.GetAlliances(ctx, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.AllianceListResponse{
		Alliances: alliances,
		Total:     total,
	}, nil
}

func (s *RelationshipService) GetAlliance(ctx context.Context, allianceID uuid.UUID) (*models.Alliance, error) {
	return s.allianceRepo.GetAlliance(ctx, allianceID)
}

func (s *RelationshipService) TerminateAlliance(ctx context.Context, allianceID uuid.UUID) error {
	return s.allianceRepo.TerminateAlliance(ctx, allianceID)
}

func (s *RelationshipService) InviteToAlliance(ctx context.Context, allianceID, inviterID uuid.UUID, req *models.AllianceInviteRequest) error {
	return s.allianceRepo.InviteToAlliance(ctx, allianceID, inviterID, req)
}

func (s *RelationshipService) JoinAlliance(ctx context.Context, allianceID, playerID uuid.UUID) error {
	return s.allianceRepo.JoinAlliance(ctx, allianceID, playerID)
}

func (s *RelationshipService) LeaveAlliance(ctx context.Context, allianceID, playerID uuid.UUID) error {
	return s.allianceRepo.LeaveAlliance(ctx, allianceID, playerID)
}

func (s *RelationshipService) GetPlayerRatings(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.PlayerRatingsResponse, error) {
	ratings, total, err := s.allianceRepo.GetPlayerRatings(ctx, playerID, limit, offset)
	if err != nil {
		return nil, err
	}

	var sum int
	for _, rating := range ratings {
		sum += rating.Rating
	}

	average := 0.0
	if len(ratings) > 0 {
		average = float64(sum) / float64(len(ratings))
	}

	return &models.PlayerRatingsResponse{
		Ratings: ratings,
		Average: average,
		Total:   total,
	}, nil
}

func (s *RelationshipService) UpdateRating(ctx context.Context, playerID uuid.UUID, req *models.UpdateRatingRequest) (*models.PlayerRating, error) {
	if req.Rating < 1 || req.Rating > 5 {
		return nil, fmt.Errorf("rating must be between 1 and 5")
	}

	return s.allianceRepo.UpdateRating(ctx, playerID, req)
}

func (s *RelationshipService) GetSocialCapital(ctx context.Context, playerID uuid.UUID) (*models.SocialCapital, error) {
	return s.allianceRepo.GetSocialCapital(ctx, playerID)
}

func (s *RelationshipService) GetInteractionHistory(ctx context.Context, playerID uuid.UUID, limit, offset int) (*models.InteractionHistoryResponse, error) {
	interactions, total, err := s.allianceRepo.GetInteractionHistory(ctx, playerID, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.InteractionHistoryResponse{
		Interactions: interactions,
		Total:        total,
		Limit:        limit,
		Offset:       offset,
	}, nil
}

func (s *RelationshipService) RequestArbitration(ctx context.Context, requesterID uuid.UUID, req *models.RequestArbitrationRequest) (*models.ArbitrationCase, error) {
	if req.Issue == "" {
		return nil, fmt.Errorf("issue description is required")
	}

	return s.allianceRepo.RequestArbitration(ctx, requesterID, req)
}

func (s *RelationshipService) GetArbitrationCase(ctx context.Context, caseID uuid.UUID) (*models.ArbitrationCase, error) {
	return s.allianceRepo.GetArbitrationCase(ctx, caseID)
}

