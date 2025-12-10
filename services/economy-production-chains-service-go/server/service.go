package server

import (
	"encoding/json"
	"net/http"
	"strings"
	"sync"
	"time"

	"github.com/google/uuid"
)

// Service keeps in-memory data for production chains and orders.
type Service struct {
	mu     sync.RWMutex
	chains map[string]ProductionChainDetails
	orders map[string]ProductionOrderDetails
}

func NewService() *Service {
	s := &Service{
		chains: make(map[string]ProductionChainDetails),
		orders: make(map[string]ProductionOrderDetails),
	}
	s.seed()
	return s
}

// Handlers

func (s *Service) HandleGetChains(w http.ResponseWriter, r *http.Request) {
	itemTier := r.URL.Query().Get("item_tier")
	itemType := r.URL.Query().Get("item_type")

	s.mu.RLock()
	defer s.mu.RUnlock()

	res := make([]ProductionChain, 0)
	for _, c := range s.chains {
		if itemTier != "" && c.ItemTier != itemTier {
			continue
		}
		if itemType != "" && c.ItemType != itemType {
			continue
		}
		res = append(res, c.ProductionChain)
	}
	writeJSON(w, map[string]any{
		"chains": res,
		"total":  len(res),
	})
}

func (s *Service) HandleGetChainDetails(w http.ResponseWriter, r *http.Request) {
	id := extractID(r.URL.Path, "/api/v1/production/chains/")
	s.mu.RLock()
	defer s.mu.RUnlock()
	chain, ok := s.chains[id]
	if !ok {
		http.Error(w, "not found", http.StatusNotFound)
		return
	}
	writeJSON(w, chain)
}

func (s *Service) HandleStartProductionChain(w http.ResponseWriter, r *http.Request) {
	chainID := strings.TrimSuffix(extractID(r.URL.Path, "/api/v1/production/chains/"), "/start")
	var req StartProductionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil || req.Quantity < 1 {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}
	order := s.createOrder(chainID, req.Quantity, req.StationID, false)
	writeStatusJSON(w, http.StatusCreated, order)
}

func (s *Service) HandleCreateOrder(w http.ResponseWriter, r *http.Request) {
	var req CreateProductionOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil || req.Quantity < 1 || req.ChainID == "" {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}
	order := s.createOrder(req.ChainID, req.Quantity, req.StationID, false)
	writeStatusJSON(w, http.StatusCreated, order)
}

func (s *Service) HandleGetOrder(w http.ResponseWriter, r *http.Request) {
	id := extractID(r.URL.Path, "/api/v1/production/orders/")
	s.mu.RLock()
	defer s.mu.RUnlock()
	order, ok := s.orders[id]
	if !ok {
		http.Error(w, "not found", http.StatusNotFound)
		return
	}
	writeJSON(w, order)
}

func (s *Service) HandleCancelOrder(w http.ResponseWriter, r *http.Request) {
	id := extractID(r.URL.Path, "/api/v1/production/orders/")
	s.mu.Lock()
	defer s.mu.Unlock()
	if _, ok := s.orders[id]; !ok {
		http.Error(w, "not found", http.StatusNotFound)
		return
	}
	delete(s.orders, id)
	w.WriteHeader(http.StatusNoContent)
}

func (s *Service) HandleCreateRushOrder(w http.ResponseWriter, r *http.Request) {
	id := strings.TrimSuffix(extractID(r.URL.Path, "/api/v1/production/orders/"), "/rush")
	var req CreateRushOrderRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil || req.TimeReduction < 1 {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}
	s.mu.Lock()
	defer s.mu.Unlock()
	order, ok := s.orders[id]
	if !ok {
		http.Error(w, "not found", http.StatusNotFound)
		return
	}
	order.RushOrder = true
	if order.EstimatedCompletion != nil {
		reduced := order.EstimatedCompletion.Add(-time.Duration(req.TimeReduction) * time.Second)
		order.EstimatedCompletion = &reduced
	}
	s.orders[id] = order
	writeJSON(w, order)
}

// Helpers

func extractID(path, prefix string) string {
	if strings.HasPrefix(path, prefix) {
		return strings.TrimPrefix(path, prefix)
	}
	return ""
}

func (s *Service) createOrder(chainID string, qty int, stationID string, rush bool) ProductionOrderDetails {
	s.mu.Lock()
	defer s.mu.Unlock()

	chain, ok := s.chains[chainID]
	if !ok {
		// If chain unknown, synthesize minimal to avoid 404 for demo.
		chain = ProductionChainDetails{
			ProductionChain: ProductionChain{
				ID:            chainID,
				Name:          "Unknown chain",
				ItemType:      "generic",
				ItemTier:      "common",
				StagesCount:   1,
				EstimatedTime: 60,
			},
		}
		s.chains[chainID] = chain
	}

	now := time.Now().UTC()
	orderID := uuid.NewString()
	est := now.Add(time.Duration(chain.EstimatedTime) * time.Second)

	order := ProductionOrderDetails{
		ProductionOrder: ProductionOrder{
			ID:                  orderID,
			ChainID:             chain.ID,
			ChainName:           chain.Name,
			Status:              "pending",
			StartedAt:           &now,
			EstimatedCompletion: &est,
			CreatedAt:           &now,
			Quantity:            qty,
			CurrentStage:        1,
			TotalStages:         maxInt(chain.StagesCount, 1),
		},
		StationID: stationID,
		RushOrder: rush,
	}
	order.Stages = []ProductionStageProgress{
		{
			StageID:     uuid.NewString(),
			Status:      "pending",
			StartedAt:   &now,
			Progress:    0,
			StageNumber: 1,
		},
	}
	s.orders[orderID] = order
	return order
}

func (s *Service) seed() {
	chainID := uuid.NewString()
	s.chains[chainID] = ProductionChainDetails{
		ProductionChain: ProductionChain{
			ID:            chainID,
			Name:          "Smartgun Assembly",
			Description:   "Сборка умного оружия с автонаведением",
			ItemType:      "smartgun",
			ItemTier:      "epic",
			BaseCost:      1200,
			StagesCount:   3,
			EstimatedTime: 1800,
		},
		Stages: []ProductionStage{
			{
				ID:            uuid.NewString(),
				Name:          "Resource Extraction",
				StageType:     "resource_extraction",
				FailureChance: 0.05,
				StageNumber:   1,
				EstimatedTime: 600,
				RequiredResources: []ResourceRequirement{
					{ResourceID: uuid.NewString(), ResourceName: "Titanium", Quantity: 10},
					{ResourceID: uuid.NewString(), ResourceName: "Polymer", Quantity: 5},
				},
			},
			{
				ID:            uuid.NewString(),
				Name:          "Processing",
				StageType:     "processing",
				FailureChance: 0.03,
				StageNumber:   2,
				EstimatedTime: 600,
			},
			{
				ID:            uuid.NewString(),
				Name:          "Assembly",
				StageType:     "crafting",
				FailureChance: 0.02,
				StageNumber:   3,
				EstimatedTime: 600,
			},
		},
	}
}

func writeJSON(w http.ResponseWriter, v any) {
	w.Header().Set("Content-Type", "application/json")
	enc := json.NewEncoder(w)
	enc.SetEscapeHTML(false)
	if err := enc.Encode(v); err != nil {
		http.Error(w, "encode error", http.StatusInternalServerError)
	}
}

func writeStatusJSON(w http.ResponseWriter, status int, v any) {
	w.WriteHeader(status)
	writeJSON(w, v)
}

func maxInt(v int, fallback int) int {
	if v > fallback {
		return v
	}
	return fallback
}







