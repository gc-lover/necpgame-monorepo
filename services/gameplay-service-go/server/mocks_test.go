package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/models"
)

type mockProgressionService struct {
	progression      map[uuid.UUID]*models.CharacterProgression
	skillProgression map[uuid.UUID][]models.SkillExperience
	addExpErr        error
	addSkillExpErr   error
	allocateAttrErr  error
	allocateSkillErr error
}

func (m *mockProgressionService) GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error) {
	prog, ok := m.progression[characterID]
	if !ok {
		return nil, errors.New("progression not found")
	}
	return prog, nil
}

func (m *mockProgressionService) AddExperience(ctx context.Context, characterID uuid.UUID, amount int64, source string) error {
	return m.addExpErr
}

func (m *mockProgressionService) AddSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string, amount int64) error {
	return m.addSkillExpErr
}

func (m *mockProgressionService) AllocateAttributePoint(ctx context.Context, characterID uuid.UUID, attribute string) error {
	return m.allocateAttrErr
}

func (m *mockProgressionService) AllocateSkillPoint(ctx context.Context, characterID uuid.UUID, skillID string) error {
	return m.allocateSkillErr
}

func (m *mockProgressionService) GetSkillProgression(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.SkillProgressionResponse, error) {
	skills, ok := m.skillProgression[characterID]
	if !ok {
		skills = []models.SkillExperience{}
	}

	total := len(skills)
	if offset >= total {
		return &models.SkillProgressionResponse{
			Skills: []models.SkillExperience{},
			Total:  total,
		}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.SkillProgressionResponse{
		Skills: skills[offset:end],
		Total:  total,
	}, nil
}

type mockQuestService struct {
	questInstances map[uuid.UUID]*models.QuestInstance
	questLists     map[uuid.UUID][]models.QuestInstance
	startQuestErr  error
	updateDialErr  error
	skillCheckPass bool
	skillCheckErr  error
	completeObjErr error
	completeErr    error
	failErr        error
}

func (m *mockQuestService) StartQuest(ctx context.Context, characterID uuid.UUID, questID string) (*models.QuestInstance, error) {
	if m.startQuestErr != nil {
		return nil, m.startQuestErr
	}

	instance := &models.QuestInstance{
		ID:            uuid.New(),
		CharacterID:   characterID,
		QuestID:       questID,
		Status:       models.QuestStatusInProgress,
		CurrentNode:   "start",
		DialogueState: make(map[string]interface{}),
		Objectives:    make(map[string]interface{}),
		StartedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	m.questInstances[instance.ID] = instance
	return instance, nil
}

func (m *mockQuestService) GetQuestInstance(ctx context.Context, instanceID uuid.UUID) (*models.QuestInstance, error) {
	instance, ok := m.questInstances[instanceID]
	if !ok {
		return nil, errors.New("quest instance not found")
	}
	return instance, nil
}

func (m *mockQuestService) UpdateDialogue(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, nodeID string, choiceID *string) error {
	return m.updateDialErr
}

func (m *mockQuestService) PerformSkillCheck(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, skillID string, requiredLevel int) (bool, error) {
	if m.skillCheckErr != nil {
		return false, m.skillCheckErr
	}
	return m.skillCheckPass, nil
}

func (m *mockQuestService) CompleteObjective(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, objectiveID string) error {
	return m.completeObjErr
}

func (m *mockQuestService) CompleteQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error {
	return m.completeErr
}

func (m *mockQuestService) FailQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error {
	return m.failErr
}

func (m *mockQuestService) ListQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) (*models.QuestListResponse, error) {
	quests, ok := m.questLists[characterID]
	if !ok {
		quests = []models.QuestInstance{}
	}

	var filtered []models.QuestInstance
	for _, q := range quests {
		if status != nil && q.Status != *status {
			continue
		}
		filtered = append(filtered, q)
	}

	total := len(filtered)
	if offset >= total {
		return &models.QuestListResponse{
			Quests: []models.QuestInstance{},
			Total:  total,
		}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.QuestListResponse{
		Quests: filtered[offset:end],
		Total:  total,
	}, nil
}

type mockAffixService struct{}

func (m *mockAffixService) GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error) {
	return nil, nil
}

func (m *mockAffixService) GetAffix(ctx context.Context, affixID uuid.UUID) (*models.Affix, error) {
	return nil, nil
}

func (m *mockAffixService) GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error) {
	return nil, nil
}

func (m *mockAffixService) GetRotationHistory(ctx context.Context, weeksBack, limit, offset int) (*models.AffixRotationHistoryResponse, error) {
	return nil, nil
}

func (m *mockAffixService) TriggerRotation(ctx context.Context, force bool, customAffixes []uuid.UUID) (*models.AffixRotation, error) {
	return nil, nil
}

func (m *mockAffixService) GenerateInstanceAffixes(ctx context.Context, instanceID uuid.UUID) error {
	return nil
}

type mockTimeTrialService struct{}

func (m *mockTimeTrialService) StartTimeTrial(ctx context.Context, playerID uuid.UUID, req *models.StartTimeTrialRequest) (*models.TimeTrialSession, error) {
	return nil, nil
}

func (m *mockTimeTrialService) CompleteTimeTrial(ctx context.Context, playerID uuid.UUID, req *models.CompleteTimeTrialRequest) (*models.TimeTrialCompletionResponse, error) {
	return nil, nil
}

func (m *mockTimeTrialService) GetTimeTrialSession(ctx context.Context, sessionID uuid.UUID, playerID uuid.UUID) (*models.TimeTrialSession, error) {
	return nil, nil
}

func (m *mockTimeTrialService) GetCurrentWeeklyChallenge(ctx context.Context) (*models.WeeklyTimeChallenge, error) {
	return nil, nil
}

func (m *mockTimeTrialService) GetWeeklyChallengeHistory(ctx context.Context, weeksBack, limit, offset int) (*models.WeeklyChallengeHistoryResponse, error) {
	return nil, nil
}

type mockComboService struct{}

func (m *mockComboService) GetLoadout(ctx context.Context, characterID uuid.UUID) (*models.ComboLoadout, error) {
	return nil, nil
}

func (m *mockComboService) UpdateLoadout(ctx context.Context, characterID uuid.UUID, req *models.UpdateLoadoutRequest) (*models.ComboLoadout, error) {
	return nil, nil
}

func (m *mockComboService) SubmitScore(ctx context.Context, req *models.SubmitScoreRequest) (*models.ScoreSubmissionResponse, error) {
	return nil, nil
}

func (m *mockComboService) GetAnalytics(ctx context.Context, comboID, characterID *uuid.UUID, periodStart, periodEnd *time.Time, limit, offset int) (*models.AnalyticsResponse, error) {
	return nil, nil
}

type mockWeaponMechanicsService struct{}

func (m *mockWeaponMechanicsService) ApplySpecialMechanics(ctx context.Context, weaponID, characterID, targetID uuid.UUID, mechanicType string, mechanicData map[string]interface{}) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponMechanicsService) GetWeaponSpecialMechanics(ctx context.Context, weaponID uuid.UUID) ([]map[string]interface{}, error) {
	return nil, nil
}

func (m *mockWeaponMechanicsService) CreatePersistentEffect(ctx context.Context, targetID uuid.UUID, projectileType string, position map[string]float64, damagePerTick, tickInterval float64, remainingTicks int) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponMechanicsService) GetPersistentEffects(ctx context.Context, targetID uuid.UUID) ([]map[string]interface{}, error) {
	return nil, nil
}

func (m *mockWeaponMechanicsService) CalculateChainDamage(ctx context.Context, sourceTargetID, weaponID uuid.UUID, damageType string, baseDamage float64, maxJumps int, jumpDamageReduction float64) ([]map[string]interface{}, float64, error) {
	return nil, 0, nil
}

func (m *mockWeaponMechanicsService) DestroyEnvironment(ctx context.Context, explosionPosition map[string]float64, explosionRadius float64, weaponID uuid.UUID, damage float64) ([]map[string]interface{}, []map[string]interface{}, error) {
	return nil, nil, nil
}

func (m *mockWeaponMechanicsService) PlaceDeployableWeapon(ctx context.Context, characterID uuid.UUID, weaponType string, position map[string]float64, activationRadius float64, ammoRemaining *int) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponMechanicsService) GetDeployableWeapon(ctx context.Context, deploymentID uuid.UUID) (map[string]interface{}, error) {
	return nil, nil
}

func (m *mockWeaponMechanicsService) FireLaser(ctx context.Context, weaponID, characterID uuid.UUID, laserType string, direction map[string]float64, duration *float64, chargeLevel *float64) (map[string]interface{}, error) {
	return nil, nil
}

func (m *mockWeaponMechanicsService) PerformMeleeAttack(ctx context.Context, characterID, targetID uuid.UUID, weaponType, attackType string) (uuid.UUID, float64, int, bool, error) {
	return uuid.New(), 0, 0, false, nil
}

func (m *mockWeaponMechanicsService) ApplyElementalEffect(ctx context.Context, targetID uuid.UUID, elementType string, damage float64, duration *float64, stacks *int) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponMechanicsService) ApplyTemporalEffect(ctx context.Context, targetID uuid.UUID, effectType string, modifierValue map[string]interface{}, duration float64) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponMechanicsService) ApplyControl(ctx context.Context, targetID uuid.UUID, controlType string, controlData map[string]interface{}) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponMechanicsService) CreateSummon(ctx context.Context, characterID uuid.UUID, summonType string, position map[string]float64, duration *float64) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponMechanicsService) CalculateProjectileForm(ctx context.Context, weaponID uuid.UUID, formType string, formData map[string]interface{}) ([]map[string]interface{}, int, error) {
	return nil, 0, nil
}

func (m *mockWeaponMechanicsService) CalculateClassSynergy(ctx context.Context, characterID, weaponID uuid.UUID, classID string) (map[string]interface{}, []string, error) {
	return nil, nil, nil
}

func (m *mockWeaponMechanicsService) FireDualWielding(ctx context.Context, characterID, leftWeaponID, rightWeaponID uuid.UUID, firingMode string, targetID *uuid.UUID) (bool, bool, float64, float64, error) {
	return false, false, 0, 0, nil
}
