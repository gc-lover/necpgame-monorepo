package protobuf

import (
	"context"

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

	// TODO: Route to gameplay service through event bus
	h.logger.Info("player input processed",
		zap.String("session_id", sessionID))
	return nil
}

func (h *Handler) handleNetworkConfig(ctx context.Context, sessionID string, msg *ParsedMessage) error {
	h.logger.Info("network config message processed",
		zap.String("session_id", sessionID))

	// TODO: Apply network configuration changes
	return nil
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
	h.logger.Info("protocol switch request processed",
		zap.String("session_id", sessionID),
		zap.Stringp("protocol", msg.Protocol))

	// TODO: Handle protocol switching logic
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
