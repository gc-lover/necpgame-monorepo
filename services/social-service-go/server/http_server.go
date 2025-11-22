package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr          string
	router        *mux.Router
	socialService *SocialService
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, socialService *SocialService, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:          addr,
		router:        router,
		socialService: socialService,
		logger:        GetLogger(),
		jwtValidator:  jwtValidator,
		authEnabled:   authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	api := router.PathPrefix("/api/v1").Subrouter()

	if authEnabled {
		api.Use(server.authMiddleware)
	}

	social := api.PathPrefix("/social").Subrouter()

	social.HandleFunc("/notifications", server.createNotification).Methods("POST")
	social.HandleFunc("/notifications", server.getNotifications).Methods("GET")
	social.HandleFunc("/notifications/{id}", server.getNotification).Methods("GET")
	social.HandleFunc("/notifications/{id}/status", server.updateNotificationStatus).Methods("PUT")

	social.HandleFunc("/chat/channels", server.getChannels).Methods("GET")
	social.HandleFunc("/chat/channels/{id}", server.getChannel).Methods("GET")
	social.HandleFunc("/chat/messages", server.createMessage).Methods("POST")
	social.HandleFunc("/chat/messages/{channelId}", server.getMessages).Methods("GET")

	social.HandleFunc("/mail", server.sendMail).Methods("POST")
	social.HandleFunc("/mail", server.getMails).Methods("GET")
	social.HandleFunc("/mail/{id}", server.getMail).Methods("GET")
	social.HandleFunc("/mail/{id}/read", server.markMailAsRead).Methods("PUT")
	social.HandleFunc("/mail/{id}/claim", server.claimAttachment).Methods("POST")
	social.HandleFunc("/mail/{id}", server.deleteMail).Methods("DELETE")

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

func (s *HTTPServer) createNotification(w http.ResponseWriter, r *http.Request) {
	var req models.CreateNotificationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.AccountID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "account_id is required")
		return
	}

	if req.Title == "" {
		s.respondError(w, http.StatusBadRequest, "title is required")
		return
	}

	notification, err := s.socialService.CreateNotification(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create notification")
		s.respondError(w, http.StatusInternalServerError, "failed to create notification")
		return
	}

	s.respondJSON(w, http.StatusCreated, notification)
}

func (s *HTTPServer) getNotifications(w http.ResponseWriter, r *http.Request) {
	accountIDStr := r.URL.Query().Get("account_id")
	if accountIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "account_id query parameter is required")
		return
	}

	accountID, err := uuid.Parse(accountIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid account id")
		return
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	response, err := s.socialService.GetNotifications(r.Context(), accountID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get notifications")
		s.respondError(w, http.StatusInternalServerError, "failed to get notifications")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getNotification(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	notificationIDStr := vars["id"]

	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid notification id")
		return
	}

	notification, err := s.socialService.GetNotification(r.Context(), notificationID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get notification")
		s.respondError(w, http.StatusInternalServerError, "failed to get notification")
		return
	}

	if notification == nil {
		s.respondError(w, http.StatusNotFound, "notification not found")
		return
	}

	s.respondJSON(w, http.StatusOK, notification)
}

func (s *HTTPServer) updateNotificationStatus(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	notificationIDStr := vars["id"]

	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid notification id")
		return
	}

	var req models.UpdateNotificationStatusRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	notification, err := s.socialService.UpdateNotificationStatus(r.Context(), notificationID, req.Status)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update notification status")
		s.respondError(w, http.StatusInternalServerError, "failed to update notification status")
		return
	}

	if notification == nil {
		s.respondError(w, http.StatusNotFound, "notification not found")
		return
	}

	s.respondJSON(w, http.StatusOK, notification)
}

func (s *HTTPServer) createMessage(w http.ResponseWriter, r *http.Request) {
	var req models.CreateMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Content == "" {
		s.respondError(w, http.StatusBadRequest, "content is required")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	senderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	formatted := FormatMessage(req.Content)

	message := &models.ChatMessage{
		ID:          uuid.New(),
		ChannelID:   req.ChannelID,
		ChannelType: req.ChannelType,
		SenderID:    senderID,
		SenderName:  r.Context().Value("username").(string),
		Content:     req.Content,
		Formatted:   formatted,
		CreatedAt:   time.Now(),
	}

	message, err = s.socialService.CreateMessage(r.Context(), message)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create chat message")
		s.respondError(w, http.StatusInternalServerError, "failed to create message")
		return
	}

	s.respondJSON(w, http.StatusCreated, message)
}

func (s *HTTPServer) getMessages(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelIDStr := vars["channelId"]

	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel id")
		return
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	messages, total, err := s.socialService.GetMessages(r.Context(), channelID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chat messages")
		s.respondError(w, http.StatusInternalServerError, "failed to get messages")
		return
	}

	response := models.MessageListResponse{
		Messages: messages,
		Total:    total,
		HasMore:  offset+limit < total,
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getChannels(w http.ResponseWriter, r *http.Request) {
	channelTypeStr := r.URL.Query().Get("type")
	var channelType *models.ChannelType

	if channelTypeStr != "" {
		ct := models.ChannelType(channelTypeStr)
		channelType = &ct
	}

	channels, err := s.socialService.GetChannels(r.Context(), channelType)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chat channels")
		s.respondError(w, http.StatusInternalServerError, "failed to get channels")
		return
	}

	response := models.ChannelListResponse{
		Channels: channels,
		Total:    len(channels),
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getChannel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	channelIDStr := vars["id"]

	channelID, err := uuid.Parse(channelIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid channel id")
		return
	}

	channel, err := s.socialService.GetChannel(r.Context(), channelID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chat channel")
		s.respondError(w, http.StatusInternalServerError, "failed to get channel")
		return
	}

	if channel == nil {
		s.respondError(w, http.StatusNotFound, "channel not found")
		return
	}

	s.respondJSON(w, http.StatusOK, channel)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":      recorder.statusCode,
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, http.StatusText(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
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

func (s *HTTPServer) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.authEnabled || s.jwtValidator == nil {
			next.ServeHTTP(w, r)
			return
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "authorization header required")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondError(w, http.StatusUnauthorized, "invalid or expired token")
			return
		}

		ctx := context.WithValue(r.Context(), "claims", claims)
		userID := claims.Subject
		if userID == "" {
			userID = claims.RegisteredClaims.Subject
		}
		ctx = context.WithValue(ctx, "user_id", userID)
		ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}

func (s *HTTPServer) sendMail(w http.ResponseWriter, r *http.Request) {
	var req models.CreateMailRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Subject == "" {
		s.respondError(w, http.StatusBadRequest, "subject is required")
		return
	}

	var senderID *uuid.UUID
	senderName := "System"
	if userIDStr := r.Context().Value("user_id"); userIDStr != nil {
		if userID, err := uuid.Parse(userIDStr.(string)); err == nil {
			senderID = &userID
		}
	}
	if username := r.Context().Value("username"); username != nil {
		senderName = username.(string)
	}

	mail, err := s.socialService.SendMail(r.Context(), &req, senderID, senderName)
	if err != nil {
		s.logger.WithError(err).Error("Failed to send mail")
		s.respondError(w, http.StatusInternalServerError, "failed to send mail")
		return
	}

	s.respondJSON(w, http.StatusCreated, mail)
}

func (s *HTTPServer) getMails(w http.ResponseWriter, r *http.Request) {
	recipientIDStr := r.URL.Query().Get("recipient_id")
	if recipientIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "recipient_id query parameter is required")
		return
	}

	recipientID, err := uuid.Parse(recipientIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid recipient_id")
		return
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.socialService.GetMails(r.Context(), recipientID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get mails")
		s.respondError(w, http.StatusInternalServerError, "failed to get mails")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getMail(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	mail, err := s.socialService.GetMail(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get mail")
		s.respondError(w, http.StatusInternalServerError, "failed to get mail")
		return
	}

	if mail == nil {
		s.respondError(w, http.StatusNotFound, "mail not found")
		return
	}

	s.respondJSON(w, http.StatusOK, mail)
}

func (s *HTTPServer) markMailAsRead(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	err = s.socialService.MarkMailAsRead(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to mark mail as read")
		s.respondError(w, http.StatusInternalServerError, "failed to mark mail as read")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) claimAttachment(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	response, err := s.socialService.ClaimAttachment(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to claim attachment")
		s.respondError(w, http.StatusInternalServerError, "failed to claim attachment")
		return
	}

	if !response.Success {
		s.respondError(w, http.StatusBadRequest, "cannot claim attachment")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) deleteMail(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	err = s.socialService.DeleteMail(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to delete mail")
		s.respondError(w, http.StatusInternalServerError, "failed to delete mail")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}
