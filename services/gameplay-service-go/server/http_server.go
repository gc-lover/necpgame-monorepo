// Issue: #104
package server

import (
	"context"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/necpgame/gameplay-service-go/pkg/api"
	"github.com/necpgame/gameplay-service-go/pkg/combosapi"
	"github.com/necpgame/gameplay-service-go/pkg/damageapi"
	"github.com/necpgame/gameplay-service-go/pkg/implantsmaintenanceapi"
	"github.com/necpgame/gameplay-service-go/pkg/implantsstatsapi"
	"github.com/sirupsen/logrus"
)

type ProgressionServiceInterface interface {
	GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error)
	AddExperience(ctx context.Context, characterID uuid.UUID, amount int64, source string) error
	AddSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string, amount int64) error
	AllocateAttributePoint(ctx context.Context, characterID uuid.UUID, attribute string) error
	AllocateSkillPoint(ctx context.Context, characterID uuid.UUID, skillID string) error
	GetSkillProgression(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.SkillProgressionResponse, error)
}

type QuestServiceInterface interface {
	StartQuest(ctx context.Context, characterID uuid.UUID, questID string) (*models.QuestInstance, error)
	GetQuestInstance(ctx context.Context, instanceID uuid.UUID) (*models.QuestInstance, error)
	UpdateDialogue(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, nodeID string, choiceID *string) error
	PerformSkillCheck(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, skillID string, requiredLevel int) (bool, error)
	CompleteObjective(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, objectiveID string) error
	CompleteQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error
	FailQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error
	ListQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) (*models.QuestListResponse, error)
}

type HTTPServer struct {
	addr             string
	router           *mux.Router
	progressionService ProgressionServiceInterface
	questService     QuestServiceInterface
	affixService     AffixServiceInterface
	timeTrialService TimeTrialServiceInterface
	comboService     ComboServiceInterface
	implantsStatsService ImplantsStatsServiceInterface
	implantsMaintenanceService ImplantsMaintenanceServiceInterface
	damageService DamageServiceInterface
	weaponMechanicsService WeaponMechanicsServiceInterface
	logger           *logrus.Logger
	server           *http.Server
}

func NewHTTPServer(addr string, progressionService ProgressionServiceInterface, questService QuestServiceInterface, affixService AffixServiceInterface, timeTrialService TimeTrialServiceInterface, comboService ComboServiceInterface, implantsStatsService ImplantsStatsServiceInterface, implantsMaintenanceService ImplantsMaintenanceServiceInterface, damageService DamageServiceInterface, weaponMechanicsService WeaponMechanicsServiceInterface) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:             addr,
		router:           router,
		progressionService: progressionService,
		questService:     questService,
		affixService:     affixService,
		timeTrialService: timeTrialService,
		comboService:     comboService,
		implantsStatsService: implantsStatsService,
		implantsMaintenanceService: implantsMaintenanceService,
		damageService: damageService,
		weaponMechanicsService: weaponMechanicsService,
		logger:           GetLogger(),
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	progressionAPI := router.PathPrefix("/api/v1/gameplay/progression").Subrouter()
	progressionAPI.HandleFunc("/characters/{character_id}", server.getProgression).Methods("GET")
	progressionAPI.HandleFunc("/characters/{character_id}/experience", server.addExperience).Methods("POST")
	progressionAPI.HandleFunc("/characters/{character_id}/skills/experience", server.addSkillExperience).Methods("POST")
	progressionAPI.HandleFunc("/characters/{character_id}/attributes/allocate", server.allocateAttributePoint).Methods("POST")
	progressionAPI.HandleFunc("/characters/{character_id}/skills/allocate", server.allocateSkillPoint).Methods("POST")
	progressionAPI.HandleFunc("/characters/{character_id}/skills", server.getSkillProgression).Methods("GET")

	questAPI := router.PathPrefix("/api/v1/gameplay/quests").Subrouter()
	questAPI.HandleFunc("/start", server.startQuest).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}", server.getQuestInstance).Methods("GET")
	questAPI.HandleFunc("/instances/{instance_id}/dialogue", server.updateDialogue).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}/skill-check", server.performSkillCheck).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}/objectives/complete", server.completeObjective).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}/complete", server.completeQuest).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}/fail", server.failQuest).Methods("POST")
	questAPI.HandleFunc("/characters/{character_id}", server.listQuestInstances).Methods("GET")

	affixHandlers := NewAffixHandlers(affixService)
	affixAPI := router.PathPrefix("/api/v1/gameplay").Subrouter()
	api.HandlerFromMux(affixHandlers, affixAPI)

	timeTrialHandlers := NewTimeTrialHandlers(timeTrialService)
	timeTrialAPI := router.PathPrefix("/api/v1/gameplay/time-trials").Subrouter()
	timeTrialAPI.HandleFunc("/start", timeTrialHandlers.StartTimeTrial).Methods("POST")
	timeTrialAPI.HandleFunc("/complete", timeTrialHandlers.CompleteTimeTrial).Methods("POST")
	timeTrialAPI.HandleFunc("/sessions/{session_id}", timeTrialHandlers.GetTimeTrialSession).Methods("GET")
	timeTrialAPI.HandleFunc("/weekly/current", timeTrialHandlers.GetCurrentWeeklyChallenge).Methods("GET")
	timeTrialAPI.HandleFunc("/weekly/history", timeTrialHandlers.GetWeeklyChallengeHistory).Methods("GET")

	comboHandlers := NewComboHandlers(comboService)
	comboAPI := router.PathPrefix("/api/v1/gameplay/combat/combos").Subrouter()
	combosapi.HandlerFromMux(comboHandlers, comboAPI)
	if implantsStatsService != nil {
		implantsStatsHandlers := NewImplantsStatsHandlers(implantsStatsService)
		implantsStatsAPI := router.PathPrefix("/api/v1/gameplay/combat/implants").Subrouter()
		implantsstatsapi.HandlerFromMux(implantsStatsHandlers, implantsStatsAPI)
	}
	if implantsMaintenanceService != nil {
		implantsMaintenanceHandlers := NewImplantsMaintenanceHandlers(implantsMaintenanceService)
		implantsMaintenanceAPI := router.PathPrefix("/api/v1/gameplay/combat/implants").Subrouter()
		implantsmaintenanceapi.HandlerFromMux(implantsMaintenanceHandlers, implantsMaintenanceAPI)
	}
	if damageService != nil {
		damageHandlers := NewDamageHandlers(damageService)
		damageAPI := router.PathPrefix("/api/v1/gameplay/combat").Subrouter()
		damageapi.HandlerFromMux(damageHandlers, damageAPI)
	}
	if weaponMechanicsService != nil {
		// TODO: After running `make generate-all-weapon-apis`, uncomment these lines:
		// weaponCoreHandlers := NewWeaponCoreHandlers(weaponMechanicsService)
		// weaponCombatHandlers := NewWeaponCombatHandlers(weaponMechanicsService)
		// weaponEffectsHandlers := NewWeaponEffectsHandlers(weaponMechanicsService)
		// weaponAdvancedHandlers := NewWeaponAdvancedHandlers(weaponMechanicsService)
		// weaponAPI := router.PathPrefix("/api/v1/gameplay/combat/weapons").Subrouter()
		// weaponcoreapi.HandlerFromMux(weaponCoreHandlers, weaponAPI)
		// weaponcombatapi.HandlerFromMux(weaponCombatHandlers, weaponAPI)
		// weaponeffectsapi.HandlerFromMux(weaponEffectsHandlers, weaponAPI)
		// weaponadvancedapi.HandlerFromMux(weaponAdvancedHandlers, weaponAPI)
		_ = weaponMechanicsService
	}
	router.HandleFunc("/health", server.healthCheck).Methods("GET")
	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}
	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}
