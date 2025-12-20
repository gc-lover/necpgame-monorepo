package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2156 - Bulkhead isolation management operations
func (s *CircuitBreakerService) CreateBulkhead(w http.ResponseWriter, r *http.Request) {
	var req CreateBulkheadRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create bulkhead request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	bulkhead := &Bulkhead{
		BulkheadID:           req.BulkheadID,
		ServiceName:          req.ServiceName,
		Config: BulkheadConfig{
			MaxConcurrentCalls: req.MaxConcurrentCalls,
			MaxWaitDuration:    time.Duration(req.MaxWaitDuration) * time.Millisecond,
			IsolationStrategy:  req.IsolationStrategy,
			ThreadPoolSize:     req.ThreadPoolSize,
			QueueSize:          req.QueueSize,
			Fairness:           req.Fairness,
			MetricsEnabled:     req.MetricsEnabled,
		},
		ActiveThreads:       0,
		QueuedRequests:      0,
		RejectedRequests:    0,
		CompletedRequests:   0,
		AverageExecutionTime: 0,
		MaxExecutionTime:    0,
		Metrics:             BulkheadMetricsData{},
		CreatedAt:          time.Now(),
	}

	s.bulkheads.Store(bulkhead.BulkheadID, bulkhead)
	s.metrics.ActiveBulkheads.Inc()

	resp := &CreateBulkheadResponse{
		BulkheadID:  bulkhead.BulkheadID,
		ServiceName: bulkhead.ServiceName,
		CreatedAt:   bulkhead.CreatedAt.Unix(),
		Config:      &bulkhead.Config,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("bulkhead_id", bulkhead.BulkheadID).Info("bulkhead created successfully")
}

func (s *CircuitBreakerService) ListBulkheads(w http.ResponseWriter, r *http.Request) {
	serviceFilter := r.URL.Query().Get("service")

	var bulkheads []*BulkheadSummary
	s.bulkheads.Range(func(key, value interface{}) bool {
		bulkhead := value.(*Bulkhead)

		if serviceFilter != "" && bulkhead.ServiceName != serviceFilter {
			return true
		}

		summary := &BulkheadSummary{
			BulkheadID:       bulkhead.BulkheadID,
			ServiceName:      bulkhead.ServiceName,
			ActiveThreads:    bulkhead.ActiveThreads,
			QueuedRequests:   bulkhead.QueuedRequests,
			RejectedRequests: bulkhead.RejectedRequests,
			CreatedAt:        bulkhead.CreatedAt.Unix(),
		}
		bulkheads = append(bulkheads, summary)
		return true
	})

	resp := &ListBulkheadsResponse{
		Bulkheads:   bulkheads,
		TotalCount:  len(bulkheads),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *CircuitBreakerService) GetBulkhead(w http.ResponseWriter, r *http.Request) {
	bulkheadID := chi.URLParam(r, "bulkheadId")

	bulkheadValue, exists := s.bulkheads.Load(bulkheadID)
	if !exists {
		http.Error(w, "Bulkhead not found", http.StatusNotFound)
		return
	}

	bulkhead := bulkheadValue.(*Bulkhead)

	details := &BulkheadDetails{
		BulkheadID: bulkhead.BulkheadID,
		ServiceName: bulkhead.ServiceName,
		Config:      &bulkhead.Config,
		Metrics:     &bulkhead.Metrics,
		CreatedAt:   bulkhead.CreatedAt.Unix(),
	}

	resp := &GetBulkheadResponse{
		Bulkhead: details,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *CircuitBreakerService) DeleteBulkhead(w http.ResponseWriter, r *http.Request) {
	bulkheadID := chi.URLParam(r, "bulkheadId")

	_, exists := s.bulkheads.Load(bulkheadID)
	if !exists {
		http.Error(w, "Bulkhead not found", http.StatusNotFound)
		return
	}

	s.bulkheads.Delete(bulkheadID)
	s.metrics.ActiveBulkheads.Dec()

	w.WriteHeader(http.StatusNoContent)

	s.logger.WithField("bulkhead_id", bulkheadID).Info("bulkhead deleted successfully")
}
