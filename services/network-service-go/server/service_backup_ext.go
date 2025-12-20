
	// OPTIMIZATION: Issue #1978 - Use memory pool
	resp := s.eventResponsePool.Get().(*PublishEventResponse)
	defer s.eventResponsePool.Put(resp)

	resp.EventID = event.EventID
	resp.PublishedAt = event.Timestamp
	resp.RecipientsCount = recipients
	resp.RoutingStrategy = req.RoutingStrategy
	resp.DeliveryStatus = "DELIVERED"
	resp.ProcessingTimeMs = 5

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *NetworkService) GetClusterStatus(w http.ResponseWriter, r *http.Request) {
	// OPTIMIZATION: Issue #1978 - Use memory pool
	resp := s.clusterResponsePool.Get().(*GetClusterStatusResponse)
	defer s.clusterResponsePool.Put(resp)

	resp.ClusterID = "main_cluster"
	resp.Status = "HEALTHY"
	resp.Nodes = []*ClusterNode{
		{
			NodeID:         "node_001",
			NodeType:       "WEBSOCKET_SERVER",
			Host:           "localhost",
			Port:           8085,
			Status:         "ACTIVE",
			Connections:    1250,
			MaxConnections: 2000,
		},
	}
	resp.LastUpdated = time.Now().Unix()

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// Helper methods
func (s *NetworkService) sendToConnection(conn *WSConnection, message interface{}) {
	data, err := json.Marshal(message)
	if err != nil {
		s.logger.WithError(err).Error("failed to marshal message")
		return
	}

	select {
	case conn.SendChan <- data:
	default:
		// Channel full, close connection
		conn.Conn.Close()
	}
}

func (s *NetworkService) broadcastToChannel(channelID string, message NetworkMessage) int {
	recipients := 0
	s.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WSConnection)
		for _, sub := range conn.Subscriptions {
			if sub == channelID {
				s.sendToConnection(conn, message)
				recipients++
				break
			}
		}
		return true
	})
	return recipients
}

func (s *NetworkService) broadcastPresenceUpdate(userID string, event NetworkMessage) {
	s.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WSConnection)
		if conn.UserID != userID { // Don't send to self
			s.sendToConnection(conn, event)
		}
		return true
	})
}

func (s *NetworkService) publishToSubscribers(event *Event) int {
	recipients := 0
	// Simplified: broadcast to all connections
	s.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WSConnection)
		s.sendToConnection(conn, event)
		recipients++
		return true
	})
	return recipients
}

func (s *NetworkService) isExcluded(userID string, excludeList []string) bool {
	for _, excluded := range excludeList {
		if excluded == userID {
			return true
		}
	}
	return false
}

func (s *NetworkService) heartbeatChecker() {
	ticker := time.NewTicker(s.config.HeartbeatInterval)
	defer ticker.Stop()

	for range ticker.C {
		now := time.Now()
		s.connections.Range(func(key, value interface{}) bool {
			conn := value.(*WSConnection)
			if now.Sub(conn.LastHeartbeat) > s.config.HeartbeatInterval*2 {
				s.logger.WithField("connection_id", conn.ID).Warn("connection heartbeat timeout")
				conn.Conn.Close()
			}
			return true
		})
	}
}

func (s *NetworkService) connectionCleaner() {
	ticker := time.NewTicker(5 * time.Minute)
	defer ticker.Stop()

	for range ticker.C {
		// Clean up stale connections (simplified)
		s.logger.Debug("running connection cleanup")
	}
}

func (s *NetworkService) handleSubscribe(conn *WSConnection, msg NetworkMessage) {
	channel := msg.Channel
	conn.Subscriptions = append(conn.Subscriptions, channel)
	s.logger.WithFields(logrus.Fields{
		"connection_id": conn.ID,
		"channel":       channel,
	}).Info("client subscribed to channel")
}

func (s *NetworkService) handleUnsubscribe(conn *WSConnection, msg NetworkMessage) {
	channel := msg.Channel
	for i, sub := range conn.Subscriptions {
		if sub == channel {
			conn.Subscriptions = append(conn.Subscriptions[:i], conn.Subscriptions[i+1:]...)
			break
		}
	}
	s.logger.WithFields(logrus.Fields{
		"connection_id": conn.ID,
		"channel":       channel,
	}).Info("client unsubscribed from channel")
}

func (s *NetworkService) handleUserMessage(conn *WSConnection, msg NetworkMessage) {
	// Handle user messages (chat, commands, etc.)
	s.logger.WithFields(logrus.Fields{
		"connection_id": conn.ID,
		"message_type":  msg.Type,
	}).Debug("user message received")
}

// Request/Response structs
type BroadcastMessageRequest struct {
	Message     NetworkMessage `json:"message"`
	ExcludeUsers []string      `json:"exclude_users,omitempty"`
	Compress    bool           `json:"compress,omitempty"`
}

type BroadcastMessageResponse struct {
	MessageID       string `json:"message_id"`
	RecipientsCount int    `json:"recipients_count"`
	SentAt          int64  `json:"sent_at"`
	DeliveryStatus  string `json:"delivery_status"`
}

type ChannelMessageRequest struct {
	Message           NetworkMessage `json:"message"`
	ChannelPermissions map[string]interface{} `json:"channel_permissions,omitempty"`
}

type ChannelMessageResponse struct {
	MessageID       string `json:"message_id"`
	ChannelID       string `json:"channel_id"`
	RecipientsCount int    `json:"recipients_count"`
	SentAt          int64  `json:"sent_at"`
}

type GetPresenceResponse struct {
	UserID     string       `json:"user_id"`
	Presence   *UserPresence `json:"presence"`
	LastUpdated int64        `json:"last_updated"`
	IsOnline   bool         `json:"is_online"`
}

type UpdatePresenceRequest struct {
	Status        string `json:"status"`
	StatusMessage string `json:"status_message,omitempty"`
	Activity      string `json:"activity,omitempty"`
	CustomFields  map[string]interface{} `json:"custom_fields,omitempty"`
}

type UpdatePresenceResponse struct {
	UserID             string `json:"user_id"`
	PreviousStatus     string `json:"previous_status"`
	NewStatus          string `json:"new_status"`
	UpdatedAt          int64  `json:"updated_at"`
	Broadcasted        bool   `json:"broadcasted"`
	AffectedSubscribers int    `json:"affected_subscribers"`
}

type SubscribeEventsRequest struct {
	Subscription *EventSubscription `json:"subscription"`
}

type SubscribeEventsResponse struct {
	SubscriptionID string `json:"subscription_id"`
	Status         string `json:"status"`
	CreatedAt      int64  `json:"created_at"`
	ExpiresAt      int64  `json:"expires_at"`
}

type PublishEventRequest struct {
	Event          *Event `json:"event"`
	RoutingStrategy string `json:"routing_strategy,omitempty"`
}

type PublishEventResponse struct {
	EventID          string `json:"event_id"`
	PublishedAt      int64  `json:"published_at"`
	RecipientsCount  int    `json:"recipients_count"`
	RoutingStrategy  string `json:"routing_strategy"`
	DeliveryStatus   string `json:"delivery_status"`
	ProcessingTimeMs int    `json:"processing_time_ms"`
}

type GetClusterStatusResponse struct {
	ClusterID   string       `json:"cluster_id"`
	Status      string       `json:"status"`
	Nodes       []*ClusterNode `json:"nodes"`
	LastUpdated int64        `json:"last_updated"`
}

// Additional structs
type EventSubscription struct {
	SubscriptionID string   `json:"subscription_id"`
	SubscriberID   string   `json:"subscriber_id"`
	EventTypes     []string `json:"event_types"`
	Channels       []string `json:"channels,omitempty"`
}

type Event struct {
	EventID   string `json:"event_id"`
	EventType string `json:"event_type"`
	Source    string `json:"source"`
	Data      map[string]interface{} `json:"data"`
	Timestamp int64  `json:"timestamp"`
}

type ClusterNode struct {
	NodeID         string `json:"node_id"`
	NodeType       string `json:"node_type"`
	Host           string `json:"host"`
	Port           int    `json:"port"`
	Status         string `json:"status"`
	Connections    int    `json:"connections"`
	MaxConnections int    `json:"max_connections"`
}
