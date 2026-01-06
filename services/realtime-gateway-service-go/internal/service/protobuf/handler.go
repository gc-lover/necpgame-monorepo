package protobuf

import (
	"context"
	"encoding/json"
	"time"

	"github.com/go-faster/errors"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
	"google.golang.org/protobuf/proto"

	"github.com/gc-lover/necp-game/services/realtime-gateway-service-go/internal/service/buffer"
	"github.com/gc-lover/necp-game/services/realtime-gateway-service-go/internal/service/session"
)

// Config holds protobuf handler configuration
type Config struct {
	BufferPool      *buffer.Pool
	Logger          *zap.Logger
	Meter           metric.Meter
	SessionManager  interface{} // Will be *session.Manager
}

// Handler handles protobuf message processing
type Handler struct {
	config         Config
	logger         *zap.Logger
	bufferPool     *buffer.Pool
	meter          metric.Meter
	sessionManager *session.Manager

	// Metrics
	messagesProcessed metric.Int64Counter
	messageSize       metric.Int64Histogram
	processingTime    metric.Float64Histogram
}

// NewHandler creates a new protobuf handler
func NewHandler(config Config) *Handler {
	var sm *session.Manager
	if config.SessionManager != nil {
		sm = config.SessionManager.(*session.Manager)
	}

	return &Handler{
		config:         config,
		logger:         config.Logger,
		bufferPool:     config.BufferPool,
		meter:          config.Meter,
		sessionManager: sm,
	}
}

// SetSessionManager sets the session manager for message sending
func (h *Handler) SetSessionManager(sm *session.Manager) {
	h.sessionManager = sm
}

// routeToGameplayService routes player input to the gameplay service via event bus
func (h *Handler) routeToGameplayService(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	// TODO: Implement actual event bus integration (Kafka, RabbitMQ, NATS, etc.)
	// For now, this is a placeholder that demonstrates the routing logic

	// Prepare gameplay event
	gameplayEvent := map[string]interface{}{
		"event_type":   "player_input",
		"session_id":   sessionID,
		"timestamp":    time.Now().Unix(),
		"player_input": map[string]interface{}{
			"move_x":     msg.MoveX,
			"move_y":     msg.MoveY,
			"shoot":      msg.Shoot,
			"timestamp":  msg.ClientTimeMs,
		},
	}

	// Serialize event for event bus
	eventJSON, err := json.Marshal(gameplayEvent)
	if err != nil {
		return errors.Wrap(err, "failed to marshal gameplay event")
	}

	// TODO: Publish to event bus topic "gameplay.player_input"
	// Example: h.eventBus.Publish("gameplay.player_input", eventJSON)

	h.logger.Debug("Gameplay event prepared for routing",
		zap.String("session_id", sessionID),
		zap.String("event_type", "player_input"),
		zap.Int("payload_size", len(eventJSON)))

	// Placeholder: In real implementation, this would be:
	// return h.eventBus.Publish(ctx, "gameplay.player_input", eventJSON)

	return nil // Success for now
}

// ProtocolSwitchData contains information about a protocol switch operation
type ProtocolSwitchData struct {
	SessionID       string
	CurrentProtocol string
	TargetProtocol  string
	Timestamp       time.Time
	Metadata        map[string]interface{}
}

// switchToUDPProtocol switches session to UDP protocol
func (h *Handler) switchToUDPProtocol(ctx context.Context, session interface{}, data *ProtocolSwitchData) error {
	// In a real implementation, this would:
	// 1. Allocate UDP port for the session
	// 2. Send UDP connection details to client
	// 3. Set up UDP transport for the session
	// 4. Migrate session state from WebSocket to UDP

	h.logger.Info("Switching to UDP protocol",
		zap.String("session_id", data.SessionID),
		zap.Time("timestamp", data.Timestamp))

	// Placeholder implementation - send protocol switch confirmation
	switchMessage := map[string]interface{}{
		"type":           "protocol_switched",
		"new_protocol":   "udp",
		"connection_details": map[string]interface{}{
			"transport": "udp",
			"port":      7777, // Example UDP port
			"endpoint":  "game.necpgame.com:7777",
		},
		"timestamp": data.Timestamp.Unix(),
	}

	messageJSON, err := json.Marshal(switchMessage)
	if err != nil {
		return errors.Wrap(err, "failed to marshal protocol switch message")
	}

	// Send confirmation via current connection before switch
	return h.sessionManager.SendMessage(data.SessionID, messageJSON)
}

// switchToWebSocketProtocol switches session to WebSocket protocol
func (h *Handler) switchToWebSocketProtocol(ctx context.Context, session interface{}, data *ProtocolSwitchData) error {
	h.logger.Info("Switching to WebSocket protocol",
		zap.String("session_id", data.SessionID),
		zap.Time("timestamp", data.Timestamp))

	// WebSocket is typically the default/fallback protocol
	// This would handle switching back from UDP/TCP to WebSocket

	switchMessage := map[string]interface{}{
		"type":         "protocol_switched",
		"new_protocol": "websocket",
		"endpoint":     "/ws/game", // WebSocket endpoint
		"timestamp":    data.Timestamp.Unix(),
	}

	messageJSON, err := json.Marshal(switchMessage)
	if err != nil {
		return errors.Wrap(err, "failed to marshal protocol switch message")
	}

	return h.sessionManager.SendMessage(data.SessionID, messageJSON)
}

// switchToTCPProtocol switches session to TCP protocol
func (h *Handler) switchToTCPProtocol(ctx context.Context, session interface{}, data *ProtocolSwitchData) error {
	h.logger.Info("Switching to TCP protocol",
		zap.String("session_id", data.SessionID),
		zap.Time("timestamp", data.Timestamp))

	// TCP protocol for reliable, ordered communication
	// Useful for complex game state synchronization

	switchMessage := map[string]interface{}{
		"type":         "protocol_switched",
		"new_protocol": "tcp",
		"endpoint":     "tcp://game.necpgame.com:8888",
		"timestamp":    data.Timestamp.Unix(),
	}

	messageJSON, err := json.Marshal(switchMessage)
	if err != nil {
		return errors.Wrap(err, "failed to marshal protocol switch message")
	}

	return h.sessionManager.SendMessage(data.SessionID, messageJSON)
}

// switchToGRPCProtocol switches session to gRPC protocol
func (h *Handler) switchToGRPCProtocol(ctx context.Context, session interface{}, data *ProtocolSwitchData) error {
	h.logger.Info("Switching to gRPC protocol",
		zap.String("session_id", data.SessionID),
		zap.Time("timestamp", data.Timestamp))

	// gRPC for high-performance, typed communication
	// Useful for complex operations requiring strong typing

	switchMessage := map[string]interface{}{
		"type":         "protocol_switched",
		"new_protocol": "grpc",
		"endpoint":     "grpc://game.necpgame.com:9090",
		"service":      "GameService",
		"timestamp":    data.Timestamp.Unix(),
	}

	messageJSON, err := json.Marshal(switchMessage)
	if err != nil {
		return errors.Wrap(err, "failed to marshal protocol switch message")
	}

	return h.sessionManager.SendMessage(data.SessionID, messageJSON)
}

// NetworkConfig represents network configuration for a session
type NetworkConfig struct {
	SessionID        string
	QoSLevel         *int    `json:"qos_level,omitempty"`
	Compression      *string `json:"compression,omitempty"`
	Encryption       *string `json:"encryption,omitempty"`
	MaxPacketSize    *int    `json:"max_packet_size,omitempty"`
	HeartbeatInterval *int   `json:"heartbeat_interval,omitempty"`
	TimeoutSettings  *TimeoutSettings `json:"timeout_settings,omitempty"`
}

// TimeoutSettings represents timeout configuration
type TimeoutSettings struct {
	ReadTimeout  int `json:"read_timeout"`
	WriteTimeout int `json:"write_timeout"`
	IdleTimeout  int `json:"idle_timeout"`
}

// validateNetworkConfig validates network configuration parameters
func (h *Handler) validateNetworkConfig(config *NetworkConfig) error {
	// Validate QoS level (0-5)
	if config.QoSLevel != nil {
		if *config.QoSLevel < 0 || *config.QoSLevel > 5 {
			return errors.New("QoS level must be between 0 and 5")
		}
	}

	// Validate compression type
	if config.Compression != nil {
		validCompression := map[string]bool{
			"none":     true,
			"gzip":     true,
			"lz4":      true,
			"zstd":     true,
		}
		if !validCompression[*config.Compression] {
			return errors.New("unsupported compression type")
		}
	}

	// Validate encryption type
	if config.Encryption != nil {
		validEncryption := map[string]bool{
			"none":    true,
			"tls":     true,
			"dtls":    true,
		}
		if !validEncryption[*config.Encryption] {
			return errors.New("unsupported encryption type")
		}
	}

	// Validate packet size (64KB - 10MB)
	if config.MaxPacketSize != nil {
		if *config.MaxPacketSize < 65536 || *config.MaxPacketSize > 10485760 {
			return errors.New("packet size must be between 64KB and 10MB")
		}
	}

	// Validate heartbeat interval (1-300 seconds)
	if config.HeartbeatInterval != nil {
		if *config.HeartbeatInterval < 1 || *config.HeartbeatInterval > 300 {
			return errors.New("heartbeat interval must be between 1 and 300 seconds")
		}
	}

	return nil
}

// applyQoSSettings applies Quality of Service settings to session
func (h *Handler) applyQoSSettings(session interface{}, qosLevel int) error {
	// In a real implementation, this would:
	// - Set packet priority levels
	// - Configure traffic shaping
	// - Adjust buffer allocation based on QoS
	// - Update network stack prioritization

	h.logger.Debug("Applying QoS settings",
		zap.Int("qos_level", qosLevel))

	// Placeholder implementation
	return nil
}

// applyCompressionSettings applies compression settings to session
func (h *Handler) applyCompressionSettings(session interface{}, compression string) error {
	// In a real implementation, this would:
	// - Initialize compression/decompression streams
	// - Configure compression level and algorithm
	// - Set up compression dictionaries
	// - Update session compression state

	h.logger.Debug("Applying compression settings",
		zap.String("compression", compression))

	// Placeholder implementation
	return nil
}

// applyEncryptionSettings applies encryption settings to session
func (h *Handler) applyEncryptionSettings(session interface{}, encryption string) error {
	// In a real implementation, this would:
	// - Initialize encryption/decryption contexts
	// - Set up TLS/DTLS handshakes
	// - Configure cipher suites
	// - Update session security state

	h.logger.Debug("Applying encryption settings",
		zap.String("encryption", encryption))

	// Placeholder implementation
	return nil
}

// HandleMessage processes a protobuf message from a session
func (h *Handler) HandleMessage(sessionID string, data []byte) error {
	ctx := context.Background()

	// Start timing
	start := h.meter.NewFloat64Histogram("protobuf_processing_time", metric.WithDescription("Time to process protobuf messages"))
	timer := start.NewTimer(metric.WithAttributes())

	defer func() {
		timer.Stop()
	}()

	// Update metrics
	h.messagesProcessed.Add(ctx, 1)
	h.messageSize.Record(ctx, int64(len(data)))

	h.logger.Debug("processing protobuf message",
		zap.String("session_id", sessionID),
		zap.Int("size", len(data)))

	// Borrow buffer from pool
	buf := h.bufferPool.Get()
	defer h.bufferPool.Put(buf)

	// Copy data to buffer for processing
	buf.Reset()
	if _, err := buf.Write(data); err != nil {
		return errors.Wrap(err, "failed to write to buffer")
	}

	// Parse protobuf message
	messageType, parsedMsg, err := h.parseProtobufMessage(data)
	if err != nil {
		h.logger.Error("failed to parse protobuf message",
			zap.String("session_id", sessionID),
			zap.Error(err))
		return errors.Wrap(err, "protobuf parsing failed")
	}

	// Route message to appropriate handler
	return h.routeProtobufMessage(sessionID, messageType, parsedMsg)
}

// MessageType represents different protobuf message types we can handle
type MessageType int

const (
	MessageTypeUnknown MessageType = iota
	MessageTypeHeartbeat
	MessageTypeEcho
	MessageTypePlayerInput
	MessageTypeNetworkConfig
	MessageTypeZoneJoin
	MessageTypeZoneLeave
	MessageTypeProtocolSwitch
)

// ParsedMessage represents a parsed protobuf message with its type
type ParsedMessage struct {
	Type    MessageType
	RawData []byte
	// For simple cases, we store common fields directly
	ClientTimeMs   *int64
	Payload        []byte
	PlayerID       *string
	Tick           *int64
	MoveX          *float32
	MoveY          *float32
	Shoot          *bool
	ZoneID         *string
	Protocol       *string
}

// parseProtobufMessage determines message type and extracts basic fields
func (h *Handler) parseProtobufMessage(data []byte) (string, *ParsedMessage, error) {
	// For now, we'll use a simple heuristic approach since we don't have generated proto types
	// In a real implementation, this would use proper protobuf parsing with generated types

	// Check message size and structure to determine type
	if len(data) < 4 {
		return "", nil, errors.New("message too small")
	}

	// Simple heuristic: check first few bytes to determine message type
	// This is a placeholder - real implementation would use proper proto.Unmarshal

	msg := &ParsedMessage{
		Type:    MessageTypeUnknown,
		RawData: data,
	}

	// Try to extract common fields based on known message structures
	// This is simplified - real implementation would parse actual protobuf

	return "generic_message", msg, nil
}

// routeProtobufMessage routes parsed messages to appropriate handlers
func (h *Handler) routeProtobufMessage(sessionID, messageType string, msg *ParsedMessage) error {
	ctx := context.Background()

	// Route based on determined message type
	switch msg.Type {
	case MessageTypeHeartbeat:
		return h.handleHeartbeat(ctx, sessionID, msg)
	case MessageTypeEcho:
		return h.handleEcho(ctx, sessionID, msg)
	case MessageTypePlayerInput:
		return h.handlePlayerInput(ctx, sessionID, msg)
	case MessageTypeNetworkConfig:
		return h.handleNetworkConfig(ctx, sessionID, msg)
	case MessageTypeZoneJoin:
		return h.handleZoneJoin(ctx, sessionID, msg)
	case MessageTypeZoneLeave:
		return h.handleZoneLeave(ctx, sessionID, msg)
	case MessageTypeProtocolSwitch:
		return h.handleProtocolSwitch(ctx, sessionID, msg)
	default:
		h.logger.Info("protobuf message processed (type detection pending)",
			zap.String("session_id", sessionID),
			zap.String("message_type", messageType),
			zap.Int("data_size", len(msg.RawData)))
		return nil // For now, just log and continue
	}
}

// Placeholder for future implementation when proto types are available
// These methods will be implemented once proper protobuf generation is set up

// Individual message handlers

func (h *Handler) handleHeartbeat(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	h.logger.Debug("heartbeat received",
		zap.String("session_id", sessionID),
		zap.Int64p("client_time", msg.ClientTimeMs))

	// Send heartbeat ack through session manager
	if err := h.sessionManager.SendMessage(sessionID, []byte("heartbeat_ack")); err != nil {
		h.logger.Error("failed to send heartbeat ack",
			zap.String("session_id", sessionID),
			zap.Error(err))
		return errors.Wrap(err, "failed to send heartbeat ack")
	}

	h.logger.Info("heartbeat ack sent",
		zap.String("session_id", sessionID))
	return nil
}

func (h *Handler) handleEcho(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	h.logger.Debug("echo received",
		zap.String("session_id", sessionID),
		zap.Int("payload_size", len(msg.RawData)))

	// Send echo ack through session manager
	if err := h.sessionManager.SendMessage(sessionID, []byte("echo_ack")); err != nil {
		h.logger.Error("failed to send echo ack",
			zap.String("session_id", sessionID),
			zap.Error(err))
		return errors.Wrap(err, "failed to send echo ack")
	}

	h.logger.Info("echo ack sent",
		zap.String("session_id", sessionID))
	return nil
}

func (h *Handler) handlePlayerInput(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	h.logger.Debug("player input received",
		zap.String("session_id", sessionID),
		zap.Stringp("player_id", msg.PlayerID),
		zap.Int64p("tick", msg.Tick),
		zap.Float32p("move_x", msg.MoveX),
		zap.Float32p("move_y", msg.MoveY),
		zap.Boolp("shoot", msg.Shoot))

	// Route to gameplay service through event bus
	if err := h.routeToGameplayService(ctx, sessionID, msg); err != nil {
		h.logger.Error("Failed to route to gameplay service",
			zap.String("session_id", sessionID),
			zap.Error(err))
		return errors.Wrap(err, "failed to route to gameplay service")
	}

	h.logger.Info("player input routed to gameplay service",
		zap.String("session_id", sessionID))
	return nil
}

func (h *Handler) handleNetworkConfig(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	h.logger.Info("Processing network config message",
		zap.String("session_id", sessionID),
		zap.Any("config", msg.NetworkConfig))

	// Validate network config parameters
	if msg.NetworkConfig == nil {
		h.logger.Warn("Network config message missing configuration",
			zap.String("session_id", sessionID))
		return errors.New("network_config is required")
	}

	// Get current session
	session, err := h.sessionManager.GetSession(sessionID)
	if err != nil {
		h.logger.Error("Failed to get session for network config",
			zap.String("session_id", sessionID),
			zap.Error(err))
		return errors.Wrap(err, "failed to get session")
	}

	// Apply network configuration changes
	networkConfig := &NetworkConfig{
		SessionID:      sessionID,
		QoSLevel:       msg.NetworkConfig.QoSLevel,
		Compression:    msg.NetworkConfig.Compression,
		Encryption:     msg.NetworkConfig.Encryption,
		MaxPacketSize:  msg.NetworkConfig.MaxPacketSize,
		HeartbeatInterval: msg.NetworkConfig.HeartbeatInterval,
		TimeoutSettings: msg.NetworkConfig.TimeoutSettings,
	}

	// Validate configuration parameters
	if err := h.validateNetworkConfig(networkConfig); err != nil {
		h.logger.Warn("Invalid network configuration",
			zap.String("session_id", sessionID),
			zap.Error(err))
		return errors.Wrap(err, "invalid network configuration")
	}

	// Apply QoS settings
	if networkConfig.QoSLevel != nil {
		err = h.applyQoSSettings(session, *networkConfig.QoSLevel)
		if err != nil {
			h.logger.Error("Failed to apply QoS settings",
				zap.String("session_id", sessionID),
				zap.Error(err))
			return errors.Wrap(err, "failed to apply QoS settings")
		}
	}

	// Apply compression settings
	if networkConfig.Compression != nil {
		err = h.applyCompressionSettings(session, *networkConfig.Compression)
		if err != nil {
			h.logger.Error("Failed to apply compression settings",
				zap.String("session_id", sessionID),
				zap.Error(err))
			return errors.Wrap(err, "failed to apply compression settings")
		}
	}

	// Apply encryption settings
	if networkConfig.Encryption != nil {
		err = h.applyEncryptionSettings(session, *networkConfig.Encryption)
		if err != nil {
			h.logger.Error("Failed to apply encryption settings",
				zap.String("session_id", sessionID),
				zap.Error(err))
			return errors.Wrap(err, "failed to apply encryption settings")
		}
	}

	// Apply packet size limits
	if networkConfig.MaxPacketSize != nil {
		session.SetMaxPacketSize(*networkConfig.MaxPacketSize)
	}

	// Apply heartbeat interval
	if networkConfig.HeartbeatInterval != nil {
		session.SetHeartbeatInterval(*networkConfig.HeartbeatInterval)
	}

	// Apply timeout settings
	if networkConfig.TimeoutSettings != nil {
		session.SetTimeoutSettings(*networkConfig.TimeoutSettings)
	}

	// Update session network configuration
	session.SetNetworkConfig(networkConfig)

	h.logger.Info("Network configuration applied successfully",
		zap.String("session_id", sessionID),
		zap.Any("applied_config", networkConfig))

	// Send confirmation back to client
	confirmation := map[string]interface{}{
		"type":             "network_config_applied",
		"session_id":       sessionID,
		"applied_config":   networkConfig,
		"timestamp":        time.Now().Unix(),
	}

	messageJSON, err := json.Marshal(confirmation)
	if err != nil {
		h.logger.Error("Failed to marshal network config confirmation",
			zap.String("session_id", sessionID),
			zap.Error(err))
		return errors.Wrap(err, "failed to marshal confirmation")
	}

	return h.sessionManager.SendMessage(sessionID, messageJSON)
}

func (h *Handler) handleZoneJoin(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	if msg.ZoneID == nil {
		h.logger.Warn("Zone join request missing zone_id",
			zap.String("session_id", sessionID))
		return errors.New("zone_id is required for zone join")
	}

	zoneID := *msg.ZoneID
	h.logger.Info("Processing zone join request",
		zap.String("session_id", sessionID),
		zap.String("zone_id", zoneID))

	// Join zone through session manager
	if err := h.sessionManager.JoinZone(sessionID, zoneID); err != nil {
		h.logger.Error("Failed to join zone",
			zap.String("session_id", sessionID),
			zap.String("zone_id", zoneID),
			zap.Error(err))
		return errors.Wrap(err, "failed to join zone")
	}

	h.logger.Info("Successfully joined zone",
		zap.String("session_id", sessionID),
		zap.String("zone_id", zoneID))
	return nil
}

func (h *Handler) handleZoneLeave(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	if msg.ZoneID == nil {
		h.logger.Warn("Zone leave request missing zone_id",
			zap.String("session_id", sessionID))
		return errors.New("zone_id is required for zone leave")
	}

	zoneID := *msg.ZoneID
	h.logger.Info("Processing zone leave request",
		zap.String("session_id", sessionID),
		zap.String("zone_id", zoneID))

	// Leave zone through session manager
	if err := h.sessionManager.LeaveZone(sessionID, zoneID); err != nil {
		h.logger.Error("Failed to leave zone",
			zap.String("session_id", sessionID),
			zap.String("zone_id", zoneID),
			zap.Error(err))
		return errors.Wrap(err, "failed to leave zone")
	}

	h.logger.Info("Successfully left zone",
		zap.String("session_id", sessionID),
		zap.String("zone_id", zoneID))
	return nil
}

func (h *Handler) handleProtocolSwitch(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	if msg.Protocol == nil {
		h.logger.Warn("Protocol switch request missing protocol",
			zap.String("session_id", sessionID))
		return errors.New("protocol is required for protocol switch")
	}

	targetProtocol := *msg.Protocol
	h.logger.Info("Processing protocol switch request",
		zap.String("session_id", sessionID),
		zap.String("target_protocol", targetProtocol))

	// Validate target protocol
	validProtocols := map[string]bool{
		"websocket": true,
		"udp":       true,
		"tcp":       true,
		"grpc":      true,
	}

	if !validProtocols[targetProtocol] {
		h.logger.Warn("Invalid target protocol requested",
			zap.String("session_id", sessionID),
			zap.String("protocol", targetProtocol))
		return errors.New("unsupported protocol: " + targetProtocol)
	}

	// Get current session
	session, err := h.sessionManager.GetSession(sessionID)
	if err != nil {
		h.logger.Error("Failed to get session for protocol switch",
			zap.String("session_id", sessionID),
			zap.Error(err))
		return errors.Wrap(err, "failed to get session")
	}

	// Check if protocol switch is already in progress
	if session.GetProtocolSwitchInProgress() {
		h.logger.Warn("Protocol switch already in progress",
			zap.String("session_id", sessionID))
		return errors.New("protocol switch already in progress")
	}

	// Validate protocol switch feasibility
	currentProtocol := session.GetProtocol()
	if currentProtocol == targetProtocol {
		h.logger.Info("Protocol switch requested to same protocol, no-op",
			zap.String("session_id", sessionID),
			zap.String("protocol", targetProtocol))
		return nil
	}

	// Mark protocol switch as in progress
	session.SetProtocolSwitchInProgress(true)

	// Prepare protocol switch data
	switchData := &ProtocolSwitchData{
		SessionID:       sessionID,
		CurrentProtocol: currentProtocol,
		TargetProtocol:  targetProtocol,
		Timestamp:       time.Now(),
		Metadata:        msg.Metadata,
	}

	// Execute protocol switch based on target
	switch targetProtocol {
	case "udp":
		err = h.switchToUDPProtocol(ctx, session, switchData)
	case "websocket":
		err = h.switchToWebSocketProtocol(ctx, session, switchData)
	case "tcp":
		err = h.switchToTCPProtocol(ctx, session, switchData)
	case "grpc":
		err = h.switchToGRPCProtocol(ctx, session, switchData)
	default:
		err = errors.New("unsupported protocol switch target")
	}

	if err != nil {
		// Reset protocol switch flag on failure
		session.SetProtocolSwitchInProgress(false)
		h.logger.Error("Protocol switch failed",
			zap.String("session_id", sessionID),
			zap.String("from_protocol", currentProtocol),
			zap.String("to_protocol", targetProtocol),
			zap.Error(err))
		return errors.Wrap(err, "protocol switch failed")
	}

	// Update session protocol
	session.SetProtocol(targetProtocol)
	session.SetProtocolSwitchInProgress(false)

	h.logger.Info("Protocol switch completed successfully",
		zap.String("session_id", sessionID),
		zap.String("from_protocol", currentProtocol),
		zap.String("to_protocol", targetProtocol))

	return nil
}

// EncodeMessage encodes a message to protobuf format
func (h *Handler) EncodeMessage(msg proto.Message) ([]byte, error) {
	// Borrow buffer from pool
	buf := h.bufferPool.Get()
	defer h.bufferPool.Put(buf)

	buf.Reset()

	// Encode message
	data, err := proto.Marshal(msg)
	if err != nil {
		return nil, errors.Wrap(err, "failed to marshal protobuf message")
	}

	// Write to buffer
	if _, err := buf.Write(data); err != nil {
		return nil, errors.Wrap(err, "failed to write to buffer")
	}

	// Return copy of buffer data
	result := make([]byte, buf.Len())
	copy(result, buf.Bytes())

	return result, nil
}

// DecodeMessage decodes a protobuf message
func (h *Handler) DecodeMessage(data []byte, msg proto.Message) error {
	if err := proto.Unmarshal(data, msg); err != nil {
		return errors.Wrap(err, "failed to unmarshal protobuf message")
	}
	return nil
}
