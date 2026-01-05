// Issue: #2266 - Refactor system-domain AI services
// PERFORMANCE: Minimal API interfaces for testing enterprise-grade service

package api

// Handler interface for AI behavior service
type Handler interface {
	AiBehaviorServiceHealthCheck(ctx context.Context, params AiBehaviorServiceHealthCheckParams) (AiBehaviorServiceHealthCheckRes, error)
	AnalyzeSuspiciousBehavior(ctx context.Context, req *SuspiciousBehaviorReport, params AnalyzeSuspiciousBehaviorParams) (AnalyzeSuspiciousBehaviorRes, error)
	GenerateProceduralNpc(ctx context.Context, req *ProceduralNpcRequest, params GenerateProceduralNpcParams) (GenerateProceduralNpcRes, error)
}

// UnimplementedHandler provides default implementations
type UnimplementedHandler struct{}

func (UnimplementedHandler) AiBehaviorServiceHealthCheck(ctx context.Context, params AiBehaviorServiceHealthCheckParams) (AiBehaviorServiceHealthCheckRes, error) {
	return &AiBehaviorServiceHealthCheckInternalServerError{}, nil
}

func (UnimplementedHandler) AnalyzeSuspiciousBehavior(ctx context.Context, req *SuspiciousBehaviorReport, params AnalyzeSuspiciousBehaviorParams) (AnalyzeSuspiciousBehaviorRes, error) {
	return &AnalyzeSuspiciousBehaviorInternalServerError{}, nil
}

func (UnimplementedHandler) GenerateProceduralNpc(ctx context.Context, req *ProceduralNpcRequest, params GenerateProceduralNpcParams) (GenerateProceduralNpcRes, error) {
	return &GenerateProceduralNpcInternalServerError{}, nil
}

// Parameter and response types
type AiBehaviorServiceHealthCheckParams struct{}

type AiBehaviorServiceHealthCheckRes interface {
	isAiBehaviorServiceHealthCheckRes()
}

type AiBehaviorServiceHealthCheckOK struct {
	Data AiBehaviorServiceHealthCheckOKData
}

type AiBehaviorServiceHealthCheckOKData struct {
	Status   AiBehaviorServiceHealthCheckOKDataStatus
	Timestamp int64
	Version  string
}

type AiBehaviorServiceHealthCheckOKDataStatus string

type AiBehaviorServiceHealthCheckInternalServerError struct{}

func (*AiBehaviorServiceHealthCheckOK) isAiBehaviorServiceHealthCheckRes() {}
func (*AiBehaviorServiceHealthCheckInternalServerError) isAiBehaviorServiceHealthCheckRes() {}

// Suspicious behavior types
type SuspiciousBehaviorReport struct {
	PlayerID  string `json:"player_id"`
	Timestamp string `json:"timestamp"`
	Action    string `json:"action"`
	Severity  int    `json:"severity"`
}

type AnalyzeSuspiciousBehaviorParams struct {
	AccountID string `path:"account_id"`
}

type AnalyzeSuspiciousBehaviorRes interface {
	isAnalyzeSuspiciousBehaviorRes()
}

type AnalyzeSuspiciousBehaviorOK struct {
	Data AnalyzeSuspiciousBehaviorOKData
}

type AnalyzeSuspiciousBehaviorOKData struct {
	Suspicious bool    `json:"suspicious"`
	RiskScore  float64 `json:"risk_score"`
	Analysis   string  `json:"analysis"`
}

type AnalyzeSuspiciousBehaviorInternalServerError struct{}

func (*AnalyzeSuspiciousBehaviorOK) isAnalyzeSuspiciousBehaviorRes() {}
func (*AnalyzeSuspiciousBehaviorInternalServerError) isAnalyzeSuspiciousBehaviorRes() {}

// Procedural NPC types
type ProceduralNpcRequest struct {
	Seed       int64  `json:"seed"`
	Location   string `json:"location"`
	Difficulty string `json:"difficulty"`
}

type GenerateProceduralNpcParams struct{}

type GenerateProceduralNpcRes interface {
	isGenerateProceduralNpcRes()
}

type GenerateProceduralNpcOK struct {
	Data GenerateProceduralNpcOKData
}

type GenerateProceduralNpcOKData struct {
	NpcId       string `json:"npc_id"`
	Name        string `json:"name"`
	Personality string `json:"personality"`
	BehaviorType string `json:"behavior_type"`
}

type GenerateProceduralNpcInternalServerError struct{}

func (*GenerateProceduralNpcOK) isGenerateProceduralNpcRes() {}
func (*GenerateProceduralNpcInternalServerError) isGenerateProceduralNpcRes() {}
