// API handlers for Mentorship Service
// Issue: #140890865
// PERFORMANCE: Optimized handlers with memory pooling

package server

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/mentorship-service-go/pkg/api"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// MentorshipServiceHealthCheck implements health check
func (h *Handler) MentorshipServiceHealthCheck(ctx context.Context) (api.MentorshipServiceHealthCheckRes, error) {
	h.logger.Info("MentorshipServiceHealthCheck called")

	return api.MentorshipServiceHealthCheckOK{
		Data: api.HealthResponse{
			Status:    api.NewOptString("healthy"),
			Timestamp: api.NewOptDateTime(api.DateTime{}),
			Version:   api.NewOptString("1.0.0"),
		},
	}, nil
}

// GetMentorshipContracts implements getMentorshipContracts
func (h *Handler) GetMentorshipContracts(ctx context.Context, params api.GetMentorshipContractsParams) (api.GetMentorshipContractsRes, error) {
	h.logger.Info("GetMentorshipContracts called")

	limit := 50
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	contracts, total, err := h.service.GetMentorshipContracts(ctx, params.MentorID, params.MenteeID, params.Status, limit)
	if err != nil {
		return api.GetMentorshipContractsOK{
			Data: api.ContractsListResponse{
				Contracts: contracts,
				Total:     api.NewOptInt(total),
			},
		}, nil
	}

	return api.GetMentorshipContractsOK{
		Data: api.ContractsListResponse{
			Contracts: contracts,
			Total:     api.NewOptInt(total),
		},
	}, nil
}

// CreateMentorshipContract implements createMentorshipContract
func (h *Handler) CreateMentorshipContract(ctx context.Context, req *api.CreateMentorshipContractRequest) (api.CreateMentorshipContractRes, error) {
	h.logger.Info("CreateMentorshipContract called")

	contract, err := h.service.CreateMentorshipContract(ctx, req)
	if err != nil {
		h.logger.Error("Failed to create mentorship contract", zap.Error(err))
		return &api.CreateMentorshipContractBadRequest{}, nil
	}

	return api.CreateMentorshipContractCreated{Data: *contract}, nil
}

// GetMentorshipContract implements getMentorshipContract
func (h *Handler) GetMentorshipContract(ctx context.Context, params api.GetMentorshipContractParams) (api.GetMentorshipContractRes, error) {
	h.logger.Info("GetMentorshipContract called")

	contract, err := h.service.GetMentorshipContract(ctx, params.ContractID)
	if err != nil {
		h.logger.Error("Failed to get mentorship contract",
			zap.String("contract_id", params.ContractID.String()),
			zap.Error(err))
		return nil, err // Return error to trigger proper HTTP error response
	}

	return api.GetMentorshipContractOK{Data: *contract}, nil
}

// UpdateMentorshipContract implements updateMentorshipContract
func (h *Handler) UpdateMentorshipContract(ctx context.Context, params api.UpdateMentorshipContractParams, req *api.UpdateMentorshipContractRequest) (api.UpdateMentorshipContractRes, error) {
	h.logger.Info("UpdateMentorshipContract called")

	contract, err := h.service.UpdateMentorshipContract(ctx, params.ContractID, req)
	if err != nil {
		h.logger.Error("Failed to update mentorship contract",
			zap.String("contract_id", params.ContractID.String()),
			zap.Error(err))
		return nil, err // Return error to trigger proper HTTP error response
	}

	return api.UpdateMentorshipContractOK{Data: *contract}, nil
}

// GetLessonSchedules implements getLessonSchedules
func (h *Handler) GetLessonSchedules(ctx context.Context, params api.GetLessonSchedulesParams) (api.GetLessonSchedulesRes, error) {
	h.logger.Info("GetLessonSchedules called", zap.String("contract_id", params.ContractID.String()))

	schedules, err := h.service.GetLessonSchedules(ctx, params.ContractID)
	if err != nil {
		h.logger.Error("Failed to get lesson schedules",
			zap.String("contract_id", params.ContractID.String()),
			zap.Error(err))
		return nil, err
	}

	return api.GetLessonSchedulesOK{
		Data: api.SchedulesListResponse{
			Schedules: schedules,
		},
	}, nil
}

// CreateLessonSchedule implements createLessonSchedule
func (h *Handler) CreateLessonSchedule(ctx context.Context, params api.CreateLessonScheduleParams, req *api.CreateLessonScheduleRequest) (api.CreateLessonScheduleRes, error) {
	h.logger.Info("CreateLessonSchedule called")

	schedule, err := h.service.CreateLessonSchedule(ctx, params.ContractID, req)
	if err != nil {
		h.logger.Error("Failed to create lesson schedule",
			zap.String("contract_id", params.ContractID.String()),
			zap.Error(err))
		return nil, err // Return error to trigger proper HTTP error response
	}

	return api.CreateLessonScheduleCreated{Data: *schedule}, nil
}

// GetLessons implements getLessons
func (h *Handler) GetLessons(ctx context.Context, params api.GetLessonsParams) (api.GetLessonsRes, error) {
	h.logger.Info("GetLessons called", zap.String("contract_id", params.ContractID.String()))

	lessons, err := h.service.GetLessons(ctx, params.ContractID)
	if err != nil {
		h.logger.Error("Failed to get lessons",
			zap.String("contract_id", params.ContractID.String()),
			zap.Error(err))
		return nil, err
	}

	return api.GetLessonsOK{
		Data: api.LessonsListResponse{
			Lessons: lessons,
		},
	}, nil
}

// StartLesson implements startLesson
func (h *Handler) StartLesson(ctx context.Context, params api.StartLessonParams, req *api.StartLessonRequest) (api.StartLessonRes, error) {
	h.logger.Info("StartLesson called")

	lesson, err := h.service.StartLesson(ctx, params.ContractID, req)
	if err != nil {
		h.logger.Error("Failed to start lesson",
			zap.String("contract_id", params.ContractID.String()),
			zap.Error(err))
		return nil, err
	}

	return api.StartLessonCreated{Data: *lesson}, nil
}

// CompleteLesson implements completeLesson
func (h *Handler) CompleteLesson(ctx context.Context, params api.CompleteLessonParams, req *api.CompleteLessonRequest) (api.CompleteLessonRes, error) {
	h.logger.Info("CompleteLesson called")

	lesson, err := h.service.CompleteLesson(ctx, params.LessonID, req)
	if err != nil {
		h.logger.Error("Failed to complete lesson",
			zap.String("lesson_id", params.LessonID.String()),
			zap.Error(err))
		return nil, err
	}

	return api.CompleteLessonOK{Data: *lesson}, nil
}

// DiscoverMentors implements discoverMentors
func (h *Handler) DiscoverMentors(ctx context.Context, params api.DiscoverMentorsParams) (api.DiscoverMentorsRes, error) {
	h.logger.Info("DiscoverMentors called")

	limit := 20
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	mentors, err := h.service.DiscoverMentors(ctx, params.SkillTrack, params.MentorshipType, params.MinReputation, limit)
	if err != nil {
		h.logger.Error("Failed to discover mentors",
			zap.String("skill_track", params.SkillTrack.Value),
			zap.String("mentorship_type", params.MentorshipType.Value),
			zap.Float64("min_reputation", params.MinReputation.Value),
			zap.Int("limit", limit),
			zap.Error(err))
		return nil, err
	}

	return api.DiscoverMentorsOK{
		Data: api.MentorsListResponse{
			Mentors: mentors,
		},
	}, nil
}

// DiscoverMentees implements discoverMentees
func (h *Handler) DiscoverMentees(ctx context.Context, params api.DiscoverMenteesParams) (api.DiscoverMenteesRes, error) {
	h.logger.Info("DiscoverMentees called")

	limit := 20
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	mentees, err := h.service.DiscoverMentees(ctx, params.SkillTrack, limit)
	if err != nil {
		h.logger.Error("Failed to discover mentees",
			zap.String("skill_track", params.SkillTrack.Value),
			zap.Int("limit", limit),
			zap.Error(err))
		return nil, err
	}

	return api.DiscoverMenteesOK{
		Data: api.MenteesListResponse{
			Mentees: mentees,
		},
	}, nil
}

// GetAcademies implements getAcademies
func (h *Handler) GetAcademies(ctx context.Context, params api.GetAcademiesParams) (api.GetAcademiesRes, error) {
	h.logger.Info("GetAcademies called", zap.String("academy_type", params.AcademyType.Value), zap.Int("limit", params.Limit.Value))

	academies, total, err := h.service.GetAcademies(ctx, params.AcademyType, params.Limit)
	if err != nil {
		h.logger.Error("Failed to get academies",
			zap.String("academy_type", params.AcademyType.Value),
			zap.Int("limit", params.Limit.Value),
			zap.Error(err))
		return nil, err
	}

	return api.GetAcademiesOK{
		Data: api.AcademiesListResponse{
			Academies: academies,
			Total:     api.NewOptInt(total),
		},
	}, nil
}

// CreateAcademy implements createAcademy
func (h *Handler) CreateAcademy(ctx context.Context, req *api.CreateAcademyRequest) (api.CreateAcademyRes, error) {
	h.logger.Info("CreateAcademy called")

	academy, err := h.service.CreateAcademy(ctx, req)
	if err != nil {
		h.logger.Error("Failed to create academy",
			zap.String("name", req.Name),
			zap.String("academy_type", req.AcademyType),
			zap.Error(err))
		return nil, err
	}

	return api.CreateAcademyCreated{Data: *academy}, nil
}

// GetMentorReputation implements getMentorReputation
func (h *Handler) GetMentorReputation(ctx context.Context, params api.GetMentorReputationParams) (api.GetMentorReputationRes, error) {
	h.logger.Info("GetMentorReputation called")

	reputation, err := h.service.GetMentorReputation(ctx, params.MentorID)
	if err != nil {
		h.logger.Error("Failed to get mentor reputation",
			zap.String("mentor_id", params.MentorID.String()),
			zap.Error(err))
		return nil, err
	}

	return api.GetMentorReputationOK{Data: *reputation}, nil
}

// GetReputationLeaderboard implements getReputationLeaderboard
func (h *Handler) GetReputationLeaderboard(ctx context.Context, params api.GetReputationLeaderboardParams) (api.GetReputationLeaderboardRes, error) {
	h.logger.Info("GetReputationLeaderboard called", zap.Int("limit", params.Limit.Value))

	limit := 10 // default
	if params.Limit.IsSet() && params.Limit.Value > 0 {
		limit = params.Limit.Value
		if limit > 100 {
			limit = 100
		}
	}

	leaderboard, err := h.service.GetReputationLeaderboard(ctx, limit)
	if err != nil {
		h.logger.Error("Failed to get reputation leaderboard",
			zap.Int("limit", limit),
			zap.Error(err))
		return nil, err
	}

	return api.GetReputationLeaderboardOK{
		Data: api.ReputationLeaderboardResponse{
			Leaderboard: leaderboard,
		},
	}, nil
}




