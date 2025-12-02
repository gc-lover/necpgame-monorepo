package server

import (
	"context"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type WorldService interface {
	ExecuteDailyReset(ctx context.Context) (*models.ResetExecution, error)
	ExecuteWeeklyReset(ctx context.Context) (*models.ResetExecution, error)
	GetDailyResetStatus(ctx context.Context) (*models.ResetStatusInfo, error)
	GetWeeklyResetStatus(ctx context.Context) (*models.ResetStatusInfo, error)
	GetNextDailyReset(ctx context.Context) (*models.NextResetInfo, error)
	GetNextWeeklyReset(ctx context.Context) (*models.NextResetInfo, error)
	
	GetQuestPool(ctx context.Context, poolType models.QuestPoolType, playerLevel *int) (*models.QuestPool, error)
	AssignQuestToPlayer(ctx context.Context, playerID, questID uuid.UUID, poolType models.QuestPoolType) (*models.PlayerQuest, error)
	GetPlayerQuests(ctx context.Context, playerID uuid.UUID, poolType *models.QuestPoolType) ([]models.PlayerQuest, error)
	
	GetPlayerLoginRewards(ctx context.Context, playerID uuid.UUID) (*models.PlayerLoginRewards, error)
	ClaimLoginReward(ctx context.Context, playerID uuid.UUID, rewardType models.LoginRewardType, dayNumber int) error
	GetPlayerLoginStreak(ctx context.Context, playerID uuid.UUID) (*models.LoginStreak, error)
	
	GetResetSchedule(ctx context.Context) (*models.ResetSchedule, error)
	UpdateResetSchedule(ctx context.Context, schedule *models.ResetSchedule) error
	GetResetEvents(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetEvent, int, error)
	GetResetExecution(ctx context.Context, id uuid.UUID) (*models.ResetExecution, error)
	
	TriggerTravelEvent(ctx context.Context, characterID, zoneID uuid.UUID, epochID *string) (*models.TravelEventInstance, error)
	GetAvailableTravelEvents(ctx context.Context, zoneID uuid.UUID, epochID *string) ([]models.TravelEvent, error)
	GetTravelEvent(ctx context.Context, id uuid.UUID) (*models.TravelEvent, error)
	StartTravelEvent(ctx context.Context, eventID, characterID uuid.UUID) (*models.TravelEventInstance, error)
	PerformTravelEventSkillCheck(ctx context.Context, eventID uuid.UUID, skill string, characterID uuid.UUID) (*models.SkillCheckResponse, error)
	CompleteTravelEvent(ctx context.Context, eventID uuid.UUID, characterID uuid.UUID, success bool) (*models.TravelEventCompletionResponse, error)
	CancelTravelEvent(ctx context.Context, eventID uuid.UUID, characterID uuid.UUID) (*models.TravelEventInstance, error)
	GetEpochTravelEvents(ctx context.Context, epochID string) ([]models.TravelEvent, error)
	GetCharacterTravelEventCooldowns(ctx context.Context, characterID uuid.UUID) ([]models.TravelEventCooldown, error)
	CalculateTravelEventProbability(ctx context.Context, eventType string, characterID, zoneID uuid.UUID) (*models.TravelEventProbabilityResponse, error)
	GetTravelEventRewards(ctx context.Context, eventID uuid.UUID) (*models.TravelEventRewardsResponse, error)
	GetTravelEventPenalties(ctx context.Context, eventID uuid.UUID) (*models.TravelEventPenaltiesResponse, error)
}

type EventBus interface {
	PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error
}

type RedisEventBus struct {
	client *redis.Client
	logger *logrus.Logger
}

func NewRedisEventBus(redisClient *redis.Client) *RedisEventBus {
	return &RedisEventBus{
		client: redisClient,
		logger: GetLogger(),
	}
}

func (b *RedisEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	eventData, err := json.Marshal(payload)
	if err != nil {
		return err
	}

	channel := "events:" + eventType
	return b.client.Publish(ctx, channel, eventData).Err()
}

type worldService struct {
	repo              WorldRepository
	logger            *logrus.Logger
	eventBus          EventBus
	travelEventService *TravelEventService
}

func NewWorldService(repo WorldRepository, logger *logrus.Logger, eventBus EventBus) WorldService {
	return &worldService{
		repo:              repo,
		logger:            logger,
		eventBus:          eventBus,
		travelEventService: NewTravelEventService(repo, logger, eventBus),
	}
}

func (s *worldService) ExecuteDailyReset(ctx context.Context) (*models.ResetExecution, error) {
	execution := &models.ResetExecution{
		ID:              uuid.New(),
		ResetType:       models.ResetTypeDaily,
		Status:          models.ResetStatusInProgress,
		StartedAt:       time.Now(),
		PlayersProcessed: 0,
		PlayersTotal:    0,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}
	
	if err := s.repo.CreateResetExecution(ctx, execution); err != nil {
		return nil, err
	}
	
	RecordResetExecution(string(models.ResetTypeDaily), string(models.ResetStatusInProgress))
	
	go s.processReset(ctx, execution)
	
	return execution, nil
}

func (s *worldService) ExecuteWeeklyReset(ctx context.Context) (*models.ResetExecution, error) {
	execution := &models.ResetExecution{
		ID:              uuid.New(),
		ResetType:       models.ResetTypeWeekly,
		Status:          models.ResetStatusInProgress,
		StartedAt:       time.Now(),
		PlayersProcessed: 0,
		PlayersTotal:    0,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}
	
	if err := s.repo.CreateResetExecution(ctx, execution); err != nil {
		return nil, err
	}
	
	RecordResetExecution(string(models.ResetTypeWeekly), string(models.ResetStatusInProgress))
	
	go s.processReset(ctx, execution)
	
	return execution, nil
}

func (s *worldService) processReset(ctx context.Context, execution *models.ResetExecution) {
	now := time.Now()
	execution.Status = models.ResetStatusCompleted
	execution.CompletedAt = &now
	execution.PlayersProcessed = execution.PlayersTotal
	execution.UpdatedAt = now
	
	if err := s.repo.UpdateResetExecution(ctx, execution); err != nil {
		s.logger.WithError(err).Error("Failed to update reset execution")
		execution.Status = models.ResetStatusFailed
		s.repo.UpdateResetExecution(ctx, execution)
		return
	}
	
	RecordResetExecution(string(execution.ResetType), string(execution.Status))
	
	event := &models.ResetEvent{
		ID:        uuid.New(),
		EventType: "reset:" + string(execution.ResetType) + "-completed",
		ResetType: &execution.ResetType,
		EventData: map[string]interface{}{
			"execution_id": execution.ID.String(),
			"players_processed": execution.PlayersProcessed,
		},
		CreatedAt: now,
	}
	s.repo.CreateResetEvent(ctx, event)
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"execution_id":      execution.ID.String(),
			"reset_type":        string(execution.ResetType),
			"status":            string(execution.Status),
			"players_processed": execution.PlayersProcessed,
			"players_total":     execution.PlayersTotal,
		}
		s.eventBus.PublishEvent(ctx, "world:reset:completed", payload)
	}
}

func (s *worldService) GetDailyResetStatus(ctx context.Context) (*models.ResetStatusInfo, error) {
	lastReset, err := s.repo.GetLastReset(ctx, models.ResetTypeDaily)
	if err != nil {
		return nil, err
	}
	
	schedule, err := s.repo.GetResetSchedule(ctx)
	if err != nil {
		return nil, err
	}
	
	nextReset := s.calculateNextReset(schedule.DailyReset.Time, schedule.DailyReset.Timezone, 24*time.Hour)
	
	return &models.ResetStatusInfo{
		ResetType:        models.ResetTypeDaily,
		Status:           models.ResetStatusPending,
		LastReset:        lastReset,
		NextReset:        nextReset,
		PlayersProcessed: 0,
		PlayersTotal:     0,
	}, nil
}

func (s *worldService) GetWeeklyResetStatus(ctx context.Context) (*models.ResetStatusInfo, error) {
	lastReset, err := s.repo.GetLastReset(ctx, models.ResetTypeWeekly)
	if err != nil {
		return nil, err
	}
	
	schedule, err := s.repo.GetResetSchedule(ctx)
	if err != nil {
		return nil, err
	}
	
	nextReset := s.calculateNextWeeklyReset(schedule.WeeklyReset)
	
	return &models.ResetStatusInfo{
		ResetType:        models.ResetTypeWeekly,
		Status:           models.ResetStatusPending,
		LastReset:        lastReset,
		NextReset:        nextReset,
		PlayersProcessed: 0,
		PlayersTotal:     0,
	}, nil
}

func (s *worldService) GetNextDailyReset(ctx context.Context) (*models.NextResetInfo, error) {
	schedule, err := s.repo.GetResetSchedule(ctx)
	if err != nil {
		return nil, err
	}
	
	nextReset := s.calculateNextReset(schedule.DailyReset.Time, schedule.DailyReset.Timezone, 24*time.Hour)
	timeUntil := int(time.Until(nextReset).Seconds())
	
	return &models.NextResetInfo{
		ResetType:            models.ResetTypeDaily,
		NextReset:            nextReset,
		TimeUntilResetSeconds: timeUntil,
	}, nil
}

func (s *worldService) GetNextWeeklyReset(ctx context.Context) (*models.NextResetInfo, error) {
	schedule, err := s.repo.GetResetSchedule(ctx)
	if err != nil {
		return nil, err
	}
	
	nextReset := s.calculateNextWeeklyReset(schedule.WeeklyReset)
	timeUntil := int(time.Until(nextReset).Seconds())
	
	return &models.NextResetInfo{
		ResetType:            models.ResetTypeWeekly,
		NextReset:            nextReset,
		TimeUntilResetSeconds: timeUntil,
	}, nil
}

func (s *worldService) calculateNextReset(timeStr, timezone string, interval time.Duration) time.Time {
	loc, _ := time.LoadLocation(timezone)
	now := time.Now().In(loc)
	
	t, _ := time.Parse("15:04:05", timeStr)
	today := time.Date(now.Year(), now.Month(), now.Day(), t.Hour(), t.Minute(), t.Second(), 0, loc)
	
	if now.After(today) {
		return today.Add(interval)
	}
	return today
}

func (s *worldService) calculateNextWeeklyReset(schedule models.WeeklyResetSchedule) time.Time {
	loc, _ := time.LoadLocation(schedule.Timezone)
	now := time.Now().In(loc)
	
	t, _ := time.Parse("15:04:05", schedule.Time)
	
	daysUntil := (int(schedule.DayOfWeek) - int(now.Weekday()) + 7) % 7
	if daysUntil == 0 && now.Hour() >= t.Hour() {
		daysUntil = 7
	}
	
	nextDate := now.AddDate(0, 0, daysUntil)
	return time.Date(nextDate.Year(), nextDate.Month(), nextDate.Day(), t.Hour(), t.Minute(), t.Second(), 0, loc)
}

func (s *worldService) GetQuestPool(ctx context.Context, poolType models.QuestPoolType, playerLevel *int) (*models.QuestPool, error) {
	entries, err := s.repo.GetQuestPool(ctx, poolType, playerLevel)
	if err != nil {
		return nil, err
	}
	
	return &models.QuestPool{
		PoolType: poolType,
		Quests:   entries,
		Total:    len(entries),
	}, nil
}

func (s *worldService) AssignQuestToPlayer(ctx context.Context, playerID, questID uuid.UUID, poolType models.QuestPoolType) (*models.PlayerQuest, error) {
	if err := s.repo.AssignQuest(ctx, playerID, questID, poolType); err != nil {
		return nil, err
	}
	
	RecordQuestAssignment(string(poolType))
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"player_id": playerID.String(),
			"quest_id":  questID.String(),
			"pool_type": string(poolType),
		}
		s.eventBus.PublishEvent(ctx, "world:quest:assigned", payload)
	}
	
	quests, err := s.repo.GetPlayerQuests(ctx, playerID, &poolType)
	if err != nil {
		return nil, err
	}
	
	for _, q := range quests {
		if q.QuestID == questID && q.PoolType == poolType {
			return &q, nil
		}
	}
	
	return nil, errors.New("quest assignment not found")
}

func (s *worldService) GetPlayerQuests(ctx context.Context, playerID uuid.UUID, poolType *models.QuestPoolType) ([]models.PlayerQuest, error) {
	return s.repo.GetPlayerQuests(ctx, playerID, poolType)
}

func (s *worldService) GetPlayerLoginRewards(ctx context.Context, playerID uuid.UUID) (*models.PlayerLoginRewards, error) {
	return s.repo.GetPlayerLoginRewards(ctx, playerID)
}

func (s *worldService) ClaimLoginReward(ctx context.Context, playerID uuid.UUID, rewardType models.LoginRewardType, dayNumber int) error {
	if err := s.repo.ClaimLoginReward(ctx, playerID, rewardType, dayNumber); err != nil {
		return err
	}
	
	RecordLoginRewardClaimed(string(rewardType))
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"player_id":   playerID.String(),
			"reward_type": string(rewardType),
			"day_number":  dayNumber,
		}
		s.eventBus.PublishEvent(ctx, "world:login-reward:claimed", payload)
	}
	
	return nil
}

func (s *worldService) GetPlayerLoginStreak(ctx context.Context, playerID uuid.UUID) (*models.LoginStreak, error) {
	return s.repo.GetLoginStreak(ctx, playerID)
}

func (s *worldService) GetResetSchedule(ctx context.Context) (*models.ResetSchedule, error) {
	return s.repo.GetResetSchedule(ctx)
}

func (s *worldService) UpdateResetSchedule(ctx context.Context, schedule *models.ResetSchedule) error {
	return s.repo.UpdateResetSchedule(ctx, schedule)
}

func (s *worldService) GetResetEvents(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetEvent, int, error) {
	return s.repo.GetResetEvents(ctx, resetType, limit, offset)
}

func (s *worldService) TriggerTravelEvent(ctx context.Context, characterID, zoneID uuid.UUID, epochID *string) (*models.TravelEventInstance, error) {
	events, err := s.repo.GetAvailableTravelEvents(ctx, zoneID, epochID)
	if err != nil {
		return nil, err
	}
	
	if len(events) == 0 {
		return nil, errors.New("no available travel events")
	}
	
	event := events[0]
	instance := &models.TravelEventInstance{
		ID:          uuid.New(),
		EventID:     event.ID,
		CharacterID: characterID,
		ZoneID:      zoneID,
		EpochID:     event.EpochID,
		State:       "triggered",
		StartedAt:   time.Now(),
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	
	if err := s.repo.CreateTravelEventInstance(ctx, instance); err != nil {
		return nil, err
	}
	
	RecordTravelEventTriggered(event.EventType)
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"instance_id":  instance.ID.String(),
			"event_id":     event.ID.String(),
			"character_id": characterID.String(),
			"zone_id":      zoneID.String(),
			"epoch_id":     event.EpochID,
		}
		s.eventBus.PublishEvent(ctx, "world:travel-event:triggered", payload)
	}
	
	return instance, nil
}

func (s *worldService) GetAvailableTravelEvents(ctx context.Context, zoneID uuid.UUID, epochID *string) ([]models.TravelEvent, error) {
	return s.repo.GetAvailableTravelEvents(ctx, zoneID, epochID)
}

func (s *worldService) GetTravelEvent(ctx context.Context, id uuid.UUID) (*models.TravelEvent, error) {
	return s.repo.GetTravelEvent(ctx, id)
}

func (s *worldService) GetEpochTravelEvents(ctx context.Context, epochID string) ([]models.TravelEvent, error) {
	return s.repo.GetTravelEventsByEpoch(ctx, epochID)
}

func (s *worldService) GetCharacterTravelEventCooldowns(ctx context.Context, characterID uuid.UUID) ([]models.TravelEventCooldown, error) {
	return s.repo.GetCharacterTravelEventCooldowns(ctx, characterID)
}

func (s *worldService) StartTravelEvent(ctx context.Context, eventID, characterID uuid.UUID) (*models.TravelEventInstance, error) {
	event, err := s.repo.GetTravelEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	instances, err := s.repo.GetTravelEventInstancesByCharacterAndEvent(ctx, characterID, eventID)
	if err != nil {
		return nil, err
	}
	
	var instance *models.TravelEventInstance
	for _, inst := range instances {
		if inst.State == "triggered" {
			instance = &inst
			break
		}
	}
	
	if instance == nil {
		return nil, errors.New("travel event instance not found")
	}
	if err != nil {
		return nil, err
	}
	if instance == nil {
		return nil, errors.New("travel event instance not found")
	}
	if instance.CharacterID != characterID {
		return nil, errors.New("character does not own this event instance")
	}
	if instance.State != "triggered" {
		return nil, errors.New("event instance is not in triggered state")
	}
	
	instance.State = "started"
	instance.UpdatedAt = time.Now()
	
	if err := s.repo.UpdateTravelEventInstance(ctx, instance); err != nil {
		return nil, err
	}
	
	RecordTravelEventStarted(instance.EventID.String())
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"instance_id":  instance.ID.String(),
			"event_id":     instance.EventID.String(),
			"character_id": characterID.String(),
		}
		s.eventBus.PublishEvent(ctx, "world:travel-event:started", payload)
	}
	
	return instance, nil
}

func (s *worldService) PerformTravelEventSkillCheck(ctx context.Context, eventID uuid.UUID, skill string, characterID uuid.UUID) (*models.SkillCheckResponse, error) {
	event, err := s.repo.GetTravelEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	instances, err := s.repo.GetTravelEventInstancesByCharacterAndEvent(ctx, characterID, eventID)
	if err != nil {
		return nil, err
	}
	
	var instance *models.TravelEventInstance
	for _, inst := range instances {
		if inst.State == "started" || inst.State == "in-progress" {
			instance = &inst
			break
		}
	}
	
	if instance == nil {
		return nil, errors.New("travel event instance not found")
	}
	if err != nil {
		return nil, err
	}
	if instance == nil {
		return nil, errors.New("travel event instance not found")
	}
	if instance.CharacterID != characterID {
		return nil, errors.New("character does not own this event instance")
	}
	if instance.State != "started" && instance.State != "in-progress" {
		return nil, errors.New("event instance is not in started or in-progress state")
	}
	
	event, err = s.repo.GetTravelEvent(ctx, instance.EventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	var dc int
	for _, sc := range event.SkillChecks {
		if sc.Skill == skill {
			dc = sc.DC
			break
		}
	}
	if dc == 0 {
		return nil, errors.New("skill check not found for this event")
	}
	
	skillCheckResult, err := s.travelEventService.PerformSkillCheck(ctx, skill, dc, characterID)
	if err != nil {
		return nil, err
	}
	
	if instance.State == "started" {
		instance.State = "in-progress"
		instance.UpdatedAt = time.Now()
		s.repo.UpdateTravelEventInstance(ctx, instance)
	}
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"instance_id":  instance.ID.String(),
			"event_id":     instance.EventID.String(),
			"character_id": characterID.String(),
			"skill":        skill,
			"dc":           dc,
			"roll_result":  skillCheckResult.RollResult,
			"success":      skillCheckResult.Success,
		}
		s.eventBus.PublishEvent(ctx, "world:travel-event:skill-check-performed", payload)
	}
	
	return skillCheckResult, nil
}

func (s *worldService) CompleteTravelEvent(ctx context.Context, eventID uuid.UUID, characterID uuid.UUID, success bool) (*models.TravelEventCompletionResponse, error) {
	event, err := s.repo.GetTravelEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	instances, err := s.repo.GetTravelEventInstancesByCharacterAndEvent(ctx, characterID, eventID)
	if err != nil {
		return nil, err
	}
	
	var instance *models.TravelEventInstance
	for _, inst := range instances {
		if inst.State == "started" || inst.State == "in-progress" {
			instance = &inst
			break
		}
	}
	
	if instance == nil {
		return nil, errors.New("travel event instance not found")
	}
	if err != nil {
		return nil, err
	}
	if instance == nil {
		return nil, errors.New("travel event instance not found")
	}
	if instance.CharacterID != characterID {
		return nil, errors.New("character does not own this event instance")
	}
	
	event, err = s.repo.GetTravelEvent(ctx, instance.EventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	now := time.Now()
	instance.CompletedAt = &now
	if success {
		instance.State = "completed"
	} else {
		instance.State = "failed"
	}
	instance.UpdatedAt = now
	
	if err := s.repo.UpdateTravelEventInstance(ctx, instance); err != nil {
		return nil, err
	}
	
	var skillCheckResult *models.SkillCheckResponse
	if success {
		skillCheckResult = &models.SkillCheckResponse{
			Success:    true,
			RollResult: 15,
			DC:         10,
		}
	}
	
	rewards, err := s.travelEventService.DistributeRewards(ctx, event, instance, skillCheckResult)
	if err != nil {
		s.logger.WithError(err).Error("Failed to distribute rewards")
	}
	
	penalties, err := s.travelEventService.ApplyPenalties(ctx, event, instance, skillCheckResult)
	if err != nil {
		s.logger.WithError(err).Error("Failed to apply penalties")
	}
	
	if err := s.travelEventService.UpdateCooldown(ctx, characterID, event.EventType, event.CooldownHours); err != nil {
		s.logger.WithError(err).Error("Failed to update cooldown")
	}
	
	successStr := "false"
	if success {
		successStr = "true"
	}
	RecordTravelEventCompleted(event.EventType, successStr)
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"instance_id":  instance.ID.String(),
			"event_id":     instance.EventID.String(),
			"character_id": characterID.String(),
			"success":      success,
			"rewards":      rewards,
			"penalties":    penalties,
		}
		s.eventBus.PublishEvent(ctx, "world:travel-event:completed", payload)
	}
	
	return &models.TravelEventCompletionResponse{
		EventInstanceID: instance.ID,
		Rewards:         rewards,
		Penalties:       penalties,
	}, nil
}

func (s *worldService) CancelTravelEvent(ctx context.Context, eventID uuid.UUID, characterID uuid.UUID) (*models.TravelEventInstance, error) {
	event, err := s.repo.GetTravelEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	instances, err := s.repo.GetTravelEventInstancesByCharacterAndEvent(ctx, characterID, eventID)
	if err != nil {
		return nil, err
	}
	
	var instance *models.TravelEventInstance
	for _, inst := range instances {
		if inst.State != "completed" && inst.State != "cancelled" && inst.State != "failed" {
			instance = &inst
			break
		}
	}
	
	if instance == nil {
		return nil, errors.New("travel event instance not found")
	}
	if err != nil {
		return nil, err
	}
	if instance == nil {
		return nil, errors.New("travel event instance not found")
	}
	if instance.State == "completed" || instance.State == "cancelled" {
		return nil, errors.New("event instance is already completed or cancelled")
	}
	
	instance.State = "cancelled"
	instance.UpdatedAt = time.Now()
	
	if err := s.repo.UpdateTravelEventInstance(ctx, instance); err != nil {
		return nil, err
	}
	
	RecordTravelEventCancelled(instance.EventID.String())
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"instance_id":  instance.ID.String(),
			"event_id":     instance.EventID.String(),
			"character_id": instance.CharacterID.String(),
		}
		s.eventBus.PublishEvent(ctx, "world:travel-event:cancelled", payload)
	}
	
	return instance, nil
}

func (s *worldService) CalculateTravelEventProbability(ctx context.Context, eventType string, characterID, zoneID uuid.UUID) (*models.TravelEventProbabilityResponse, error) {
	events, err := s.repo.GetAvailableTravelEvents(ctx, zoneID, nil)
	if err != nil {
		return nil, err
	}
	
	var event *models.TravelEvent
	for _, e := range events {
		if e.EventType == eventType {
			event = &e
			break
		}
	}
	
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	probability := s.travelEventService.CalculateProbability(ctx, event, characterID, zoneID)
	
	modifiers := map[string]interface{}{
		"base_probability": event.BaseProbability,
		"level_modifier":   1.0,
		"reputation_modifier": 1.0,
		"time_modifier":    1.0,
		"zone_modifier":    1.0,
	}
	
	return &models.TravelEventProbabilityResponse{
		EventType:   eventType,
		Probability: probability,
		Modifiers:   modifiers,
	}, nil
}

func (s *worldService) GetTravelEventRewards(ctx context.Context, eventID uuid.UUID) (*models.TravelEventRewardsResponse, error) {
	event, err := s.repo.GetTravelEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	var rewards []models.TravelEventReward
	if event.Rewards != nil {
		if lootRewards, ok := event.Rewards["loot"].(map[string]interface{}); ok {
			rewards = append(rewards, models.TravelEventReward{
				Type: "loot",
				Data: lootRewards,
			})
		}
		if eddiesRewards, ok := event.Rewards["eddies"].(map[string]interface{}); ok {
			rewards = append(rewards, models.TravelEventReward{
				Type: "eddies",
				Data: eddiesRewards,
			})
		}
		if reputationRewards, ok := event.Rewards["reputation"].(map[string]interface{}); ok {
			rewards = append(rewards, models.TravelEventReward{
				Type: "reputation",
				Data: reputationRewards,
			})
		}
	}
	
	return &models.TravelEventRewardsResponse{
		EventID: eventID,
		Rewards: rewards,
	}, nil
}

func (s *worldService) GetTravelEventPenalties(ctx context.Context, eventID uuid.UUID) (*models.TravelEventPenaltiesResponse, error) {
	event, err := s.repo.GetTravelEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("travel event not found")
	}
	
	var penalties []models.TravelEventPenalty
	if event.Penalties != nil {
		if damagePenalties, ok := event.Penalties["damage"].(map[string]interface{}); ok {
			penalties = append(penalties, models.TravelEventPenalty{
				Type: "damage",
				Data: damagePenalties,
			})
		}
		if heatPenalties, ok := event.Penalties["heat"].(map[string]interface{}); ok {
			penalties = append(penalties, models.TravelEventPenalty{
				Type: "heat",
				Data: heatPenalties,
			})
		}
		if reputationPenalties, ok := event.Penalties["reputation"].(map[string]interface{}); ok {
			penalties = append(penalties, models.TravelEventPenalty{
				Type: "reputation",
				Data: reputationPenalties,
			})
		}
		if confiscationPenalties, ok := event.Penalties["confiscation"].(map[string]interface{}); ok {
			penalties = append(penalties, models.TravelEventPenalty{
				Type: "confiscation",
				Data: confiscationPenalties,
			})
		}
	}
	
	return &models.TravelEventPenaltiesResponse{
		EventID:   eventID,
		Penalties: penalties,
	}, nil
}

func (s *worldService) GetResetExecution(ctx context.Context, id uuid.UUID) (*models.ResetExecution, error) {
	return s.repo.GetResetExecution(ctx, id)
}

