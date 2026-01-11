package handlers

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/gc-lover/necp-game/services/analytics-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/analytics-service-go/internal/service"
	"github.com/gorilla/mux"
	"go.uber.org/zap"
)

// Handlers содержит HTTP обработчики для Analytics API
type Handlers struct {
	service    *service.Service
	logger     *zap.Logger
	router     *mux.Router
}

// NewHandlers создает новый экземпляр обработчиков
func NewHandlers(svc *service.Service, logger *zap.Logger) *Handlers {
	h := &Handlers{
		service: svc,
		logger:  logger,
		router:  mux.NewRouter(),
	}

	h.registerRoutes()
	return h
}

// registerRoutes регистрирует маршруты API
func (h *Handlers) registerRoutes() {
	api := h.router.PathPrefix("/api/v1").Subrouter()

	// Player behavior analytics
	api.HandleFunc("/analytics/behavior/{player_id}", h.getPlayerBehavior).Methods("GET")
	api.HandleFunc("/analytics/behavior/{player_id}/analyze", h.analyzePlayerBehavior).Methods("POST")

	// Retention metrics
	api.HandleFunc("/analytics/retention", h.getRetentionMetrics).Methods("GET")

	// A/B testing
	api.HandleFunc("/analytics/ab-tests", h.createABTest).Methods("POST")
	api.HandleFunc("/analytics/ab-tests/{test_id}/assign/{player_id}", h.assignABTestVariant).Methods("POST")

	// Reports
	api.HandleFunc("/analytics/reports", h.generateAnalyticsReport).Methods("POST")
	api.HandleFunc("/analytics/reports/{report_id}", h.getAnalyticsReport).Methods("GET")

	// System health
	api.HandleFunc("/health", h.healthCheck).Methods("GET")
	api.HandleFunc("/system/health", h.getSystemHealth).Methods("GET")

	// Middleware
	h.router.Use(h.loggingMiddleware)
	h.router.Use(h.corsMiddleware)
}

// loggingMiddleware логирует HTTP запросы
func (h *Handlers) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(wrapped, r)

		h.logger.Info("HTTP Request",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.String("remote_addr", r.RemoteAddr),
			zap.Int("status", wrapped.statusCode),
			zap.Duration("duration", time.Since(start)))
	})
}

// corsMiddleware добавляет CORS заголовки
func (h *Handlers) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// getPlayerBehavior получает поведение игрока
func (h *Handlers) getPlayerBehavior(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID := vars["player_id"]

	behavior, err := h.service.AnalyzePlayerBehavior(r.Context(), playerID)
	if err != nil {
		h.respondWithError(w, http.StatusNotFound, "Player behavior not found", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, behavior)
}

// analyzePlayerBehavior анализирует поведение игрока
func (h *Handlers) analyzePlayerBehavior(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID := vars["player_id"]

	behavior, err := h.service.AnalyzePlayerBehavior(r.Context(), playerID)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to analyze player behavior", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, behavior)
}

// getRetentionMetrics получает метрики удержания
func (h *Handlers) getRetentionMetrics(w http.ResponseWriter, r *http.Request) {
	// Параметры периода (по умолчанию последние 90 дней)
	daysStr := r.URL.Query().Get("days")
	days := 90
	if daysStr != "" {
		if d, err := strconv.Atoi(daysStr); err == nil && d > 0 {
			days = d
		}
	}

	endDate := time.Now()
	startDate := endDate.AddDate(0, 0, -days)

	metrics, err := h.service.GetRetentionMetrics(r.Context(), startDate, endDate)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to get retention metrics", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, metrics)
}

// createABTest создает новый A/B тест
func (h *Handlers) createABTest(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Name        string   `json:"name"`
		Description string   `json:"description"`
		Variants    []string `json:"variants"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	if len(req.Variants) < 2 {
		h.respondWithError(w, http.StatusBadRequest, "At least 2 variants required", fmt.Errorf("insufficient variants"))
		return
	}

	test, err := h.service.CreateABTest(r.Context(), req.Name, req.Description, req.Variants)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to create A/B test", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, test)
}

// assignABTestVariant присваивает вариант A/B теста игроку
func (h *Handlers) assignABTestVariant(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	testID := vars["test_id"]
	playerID := vars["player_id"]

	variant, err := h.service.AssignABTestVariant(r.Context(), playerID, testID)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to assign A/B test variant", err)
		return
	}

	response := map[string]interface{}{
		"test_id":   testID,
		"player_id": playerID,
		"variant":   variant,
		"assigned_at": time.Now().Format(time.RFC3339),
	}

	h.respondWithJSON(w, http.StatusOK, response)
}

// generateAnalyticsReport генерирует отчет аналитики
func (h *Handlers) generateAnalyticsReport(w http.ResponseWriter, r *http.Request) {
	var req struct {
		ReportType string `json:"report_type"`
		StartDate  string `json:"start_date"`
		EndDate    string `json:"end_date"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	// Парсим даты
	startDate := time.Now().AddDate(0, 0, -30) // по умолчанию 30 дней назад
	endDate := time.Now()

	if req.StartDate != "" {
		if sd, err := time.Parse("2006-01-02", req.StartDate); err == nil {
			startDate = sd
		}
	}

	if req.EndDate != "" {
		if ed, err := time.Parse("2006-01-02", req.EndDate); err == nil {
			endDate = ed
		}
	}

	report, err := h.service.GenerateAnalyticsReport(r.Context(), req.ReportType, startDate, endDate)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to generate analytics report", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, report)
}

// getAnalyticsReport получает отчет аналитики
func (h *Handlers) getAnalyticsReport(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	reportID := vars["report_id"]

	report, err := h.service.GetAnalyticsReport(r.Context(), reportID)
	if err != nil {
		h.respondWithError(w, http.StatusNotFound, "Analytics report not found", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, report)
}

// healthCheck проверка здоровья сервиса
func (h *Handlers) healthCheck(w http.ResponseWriter, r *http.Request) {
	health := map[string]interface{}{
		"status":      "healthy",
		"service":     "analytics-service",
		"version":     "1.0.0",
		"timestamp":   time.Now().Format(time.RFC3339),
		"endpoints": []string{
			"/api/v1/analytics/behavior/{player_id}",
			"/api/v1/analytics/retention",
			"/api/v1/analytics/ab-tests",
			"/api/v1/analytics/reports",
		},
	}

	h.respondWithJSON(w, http.StatusOK, health)
}

// getSystemHealth получает состояние здоровья системы
func (h *Handlers) getSystemHealth(w http.ResponseWriter, r *http.Request) {
	health, err := h.service.GetSystemHealth(r.Context())
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to get system health", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, health)
}

// Router возвращает HTTP роутер
func (h *Handlers) Router() *mux.Router {
	return h.router
}

// responseWriter обертка для захвата статус кода
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// respondWithJSON отправляет JSON ответ
func (h *Handlers) respondWithJSON(w http.ResponseWriter, code int, payload interface{}) {
	response, err := json.Marshal(payload)
	if err != nil {
		h.logger.Error("Failed to marshal JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	w.Write(response)
}

// respondWithError отправляет ошибку в JSON формате
func (h *Handlers) respondWithError(w http.ResponseWriter, code int, message string, err error) {
	h.logger.Error(message, zap.Error(err))

	response := map[string]interface{}{
		"error":   message,
		"details": err.Error(),
		"code":    code,
		"timestamp": time.Now().Format(time.RFC3339),
	}

	h.respondWithJSON(w, code, response)
}