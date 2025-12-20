	// Disconnect all participants
	channel.Participants.Range(func(key, value interface{}) bool {
		conn := value.(*WSVoiceConnection)
		if conn.Conn != nil {
			conn.Conn.Close()
		}
		return true
	})

	s.channels.Delete(channelID)
	s.metrics.ActiveChannels.Dec()

	w.WriteHeader(http.StatusNoContent)
	s.logger.WithField("channel_id", channelID).Info("voice channel deleted successfully")
}

func (s *VoiceChatService) JoinChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")

	var req JoinChannelRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode join channel request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	if channel.ParticipantCount >= channel.MaxParticipants {
		http.Error(w, "Channel is full", http.StatusConflict)
		return
	}

	// Create WebSocket connection
	wsConn, err := voiceUpgrader.Upgrade(w, r, nil)
	if err != nil {
		s.logger.WithError(err).Error("failed to upgrade to WebSocket")
		return
	}

	connectionID := uuid.New().String()
	sessionToken := uuid.New().String()

	voiceConn := &WSVoiceConnection{
		ConnectionID:  connectionID,
		UserID:        req.UserID,
		ClientID:      req.UserID, // Would be from client
		ChannelID:     channelID,
		Conn:          wsConn,
		ConnectedAt:   time.Now(),
		LastHeartbeat: time.Now(),
		IsMuted:       req.MuteOnJoin,
		IsDeafened:    req.DeafenOnJoin,
		SendChan:      make(chan []byte, 100),
		SessionToken:  sessionToken,
	}

	// Store connection
	s.connections.Store(connectionID, voiceConn)
	channel.Participants.Store(connectionID, voiceConn)
	channel.ParticipantCount++
	channel.LastActivity = time.Now()
	s.channels.Store(channelID, channel)

	s.metrics.ActiveConnections.Inc()

	// Start WebSocket handlers
	go s.handleWebSocketConnection(voiceConn)
	go s.handleWebSocketSend(voiceConn)

	// Send join response
	resp := &JoinChannelResponse{
		ChannelID:     channelID,
		UserID:        req.UserID,
		WebsocketURL:  fmt.Sprintf("ws://localhost:%s/voice-chat/ws/%s", s.config.Port, channelID),
		SessionToken:  sessionToken,
		JoinedAt:      voiceConn.ConnectedAt.Unix(),
	}

	// Since we upgraded to WebSocket, we need to send response differently
	// For now, we'll just log success
	s.logger.WithFields(logrus.Fields{
		"channel_id":    channelID,
		"user_id":       req.UserID,
		"connection_id": connectionID,
	}).Info("user joined voice channel")

	// Note: In production, the WebSocket upgrade response would be handled differently
}

func (s *VoiceChatService) LeaveChannel(w http.ResponseWriter, r *http.Request) {
	channelID := chi.URLParam(r, "channelId")
	userID := r.URL.Query().Get("user_id")

	if userID == "" {
		http.Error(w, "User ID is required", http.StatusBadRequest)
		return
	}

	channelValue, exists := s.channels.Load(channelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	// Find and remove connection
	var connectionIDToRemove string
	channel.Participants.Range(func(key, value interface{}) bool {
		conn := value.(*WSVoiceConnection)
		if conn.UserID == userID {
			connectionIDToRemove = conn.ConnectionID
			if conn.Conn != nil {
				conn.Conn.Close()
			}
			return false
		}
		return true
	})

	if connectionIDToRemove != "" {
		channel.Participants.Delete(connectionIDToRemove)
		s.connections.Delete(connectionIDToRemove)
		channel.ParticipantCount--
		channel.LastActivity = time.Now()
		s.channels.Store(channelID, channel)
		s.metrics.ActiveConnections.Dec()
	}

	resp := &LeaveChannelResponse{
		ChannelID: channelID,
		UserID:    userID,
		LeftAt:    time.Now().Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"channel_id": channelID,
		"user_id":    userID,
	}).Info("user left voice channel")
}

// Audio Streaming Handlers
func (s *VoiceChatService) StartAudioStream(w http.ResponseWriter, r *http.Request) {
	var req StartAudioStreamRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode start audio stream request")
		s.metrics.Errors.Inc()
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Verify channel exists
	if _, exists := s.channels.Load(req.ChannelID); !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	streamID := uuid.New().String()
	sessionToken := uuid.New().String()

	resp := s.audioStreamPool.Get().(*StartAudioStreamResponse)
	defer s.audioStreamPool.Put(resp)

	resp.StreamID = streamID
	resp.ChannelID = req.ChannelID
	resp.UserID = req.UserID
	resp.WebsocketURL = fmt.Sprintf("ws://localhost:%s/voice-chat/audio/%s", s.config.Port, streamID)
	resp.ICEServers = []string{"stun:stun.l.google.com:19302"}
	resp.SessionToken = sessionToken
	resp.StartedAt = time.Now().Unix()

	s.metrics.AudioStreams.Inc()

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"stream_id":  streamID,
		"channel_id": req.ChannelID,
		"user_id":    req.UserID,
	}).Info("audio stream started successfully")
}

func (s *VoiceChatService) UpdateProximity(w http.ResponseWriter, r *http.Request) {
	var req UpdateProximityRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode update proximity request")
		s.metrics.Errors.Inc()
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	channelValue, exists := s.channels.Load(req.ChannelID)
	if !exists {
		http.Error(w, "Channel not found", http.StatusNotFound)
		return
	}

	channel := channelValue.(*VoiceChannel)

	// Calculate audible users based on proximity
	var audibleUsers []*AudibleUser
	channel.Participants.Range(func(key, value interface{}) bool {
		conn := value.(*WSVoiceConnection)
		if conn.UserID != req.UserID && conn.Location != nil {
			distance := s.calculateDistance(req.Position, *conn.Location)
			volume := s.calculateVolume(distance, channel.Settings.ProximityRadius)

			if volume > 0 {
				audibleUser := &AudibleUser{
					UserID:  conn.UserID,
					Distance: distance,
					Volume:   volume,
				}
				audibleUsers = append(audibleUsers, audibleUser)
			}
		}
		return true
	})

	resp := &UpdateProximityResponse{
		UserID:       req.UserID,
		ChannelID:    req.ChannelID,
		AudibleUsers: audibleUsers,
		UpdatedAt:    time.Now().Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// Text-to-Speech Handler
func (s *VoiceChatService) SynthesizeSpeech(w http.ResponseWriter, r *http.Request) {
	var req SynthesizeSpeechRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode synthesize speech request")
		s.metrics.Errors.Inc()
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate text length
	if len(req.Text) > 1000 {
		http.Error(w, "Text too long", http.StatusBadRequest)
		return
	}

	// In production, this would call a TTS service
	audioID := uuid.New().String()
	audioURL := fmt.Sprintf("https://cdn.example.com/tts/%s.wav", audioID)

	resp := s.ttsResponsePool.Get().(*SynthesizeSpeechResponse)
	defer s.ttsResponsePool.Put(resp)

	resp.AudioID = audioID
	resp.Text = req.Text
	resp.AudioURL = audioURL
	resp.Duration = float64(len(req.Text)) / 10.0 // Rough estimate
	resp.SizeBytes = int(resp.Duration * 16000)    // Rough estimate for 16kHz mono
	resp.GeneratedAt = time.Now().Unix()

	s.metrics.TTSSynthesizations.Inc()

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("audio_id", audioID).Info("speech synthesized successfully")
}

// Moderation Handler
func (s *VoiceChatService) ReportVoiceAbuse(w http.ResponseWriter, r *http.Request) {
	var req ReportVoiceAbuseRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode report voice abuse request")
		s.metrics.Errors.Inc()
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	reportID := uuid.New().String()

	resp := s.moderationReportPool.Get().(*ReportVoiceAbuseResponse)
	defer s.moderationReportPool.Put(resp)

	resp.ReportID = reportID
	resp.ReportedUserID = req.ReportedUserID
	resp.ChannelID = req.ChannelID
	resp.Status = "submitted"
	resp.SubmittedAt = time.Now().Unix()

	s.metrics.ModerationReports.Inc()

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("report_id", reportID).Info("voice abuse reported successfully")
}

// WebSocket connection handlers
func (s *VoiceChatService) handleWebSocketConnection(conn *WSVoiceConnection) {
	defer func() {
		conn.Conn.Close()
		s.connections.Delete(conn.ConnectionID)
		s.metrics.ActiveConnections.Dec()
	}()

	conn.Conn.SetReadDeadline(time.Now().Add(s.config.WebSocketReadTimeout))

	for {
		messageType, data, err := conn.Conn.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				s.logger.WithError(err).Error("WebSocket error")
				s.metrics.WebSocketErrors.Inc()
			}
			break
		}

		switch messageType {
		case websocket.TextMessage:
			// Handle text messages (control messages)
			s.handleWebSocketTextMessage(conn, data)
		case websocket.BinaryMessage:
			// Handle audio data
			s.handleWebSocketAudioData(conn, data)
		}

		conn.LastHeartbeat = time.Now()
		s.metrics.BytesReceived += float64(len(data))
	}
}

func (s *VoiceChatService) handleWebSocketSend(conn *WSVoiceConnection) {
	defer conn.Conn.Close()

	for data := range conn.SendChan {
		conn.Conn.SetWriteDeadline(time.Now().Add(s.config.WebSocketWriteTimeout))
		if err := conn.Conn.WriteMessage(websocket.BinaryMessage, data); err != nil {
			s.logger.WithError(err).Error("failed to send WebSocket message")
			s.metrics.WebSocketErrors.Inc()
			return
		}
		s.metrics.BytesSent += float64(len(data))
	}
}

func (s *VoiceChatService) handleWebSocketTextMessage(conn *WSVoiceConnection, data []byte) {
	// Handle control messages
	var msg map[string]interface{}
	if err := json.Unmarshal(data, &msg); err != nil {
		s.logger.WithError(err).Error("failed to parse WebSocket text message")
		return
	}

	switch msg["type"] {
	case "heartbeat":
		conn.LastHeartbeat = time.Now()
	case "mute":
		conn.IsMuted = true
	case "unmute":
		conn.IsMuted = false
	case "deafen":
		conn.IsDeafened = true
	case "undeafen":
		conn.IsDeafened = false
	}
}

func (s *VoiceChatService) handleWebSocketAudioData(conn *WSVoiceConnection, data []byte) {
	// Forward audio data to other channel participants
	channelValue, exists := s.channels.Load(conn.ChannelID)
	if !exists {
		return
	}

	channel := channelValue.(*VoiceChannel)

	channel.Participants.Range(func(key, value interface{}) bool {
		otherConn := value.(*WSVoiceConnection)
		if otherConn.ConnectionID != conn.ConnectionID && !otherConn.IsDeafened {
			select {
			case otherConn.SendChan <- data:
			default:
				// Channel full, drop packet
			}
		}
		return true
	})
}

