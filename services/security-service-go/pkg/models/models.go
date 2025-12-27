// Issue: #2212 - Anti-Cheat Player Behavior Analytics
// Models for Security Service - Anti-cheat and behavior analytics system

package models

import (
	"time"
)

// PlayerBehaviorProfile represents a comprehensive player behavior profile
type PlayerBehaviorProfile struct {
	PlayerID        string                 `json:"player_id" db:"player_id"`
	ProfileVersion  int                    `json:"profile_version" db:"profile_version"`
	TrustScore      float64                `json:"trust_score" db:"trust_score"`           // 0.0-1.0 overall trust
	RiskLevel       string                 `json:"risk_level" db:"risk_level"`             // "low", "medium", "high", "banned"
	LastAnalyzed    time.Time              `json:"last_analyzed" db:"last_analyzed"`
	BehaviorMetrics BehaviorMetrics        `json:"behavior_metrics" db:"behavior_metrics"` // JSON
	CheatingFlags   []CheatingFlag         `json:"cheating_flags" db:"cheating_flags"`     // JSON array
	PlayPatterns    PlayPatterns           `json:"play_patterns" db:"play_patterns"`       // JSON
	SocialBehavior  SocialBehavior         `json:"social_behavior" db:"social_behavior"`   // JSON
	DeviceFingerprint DeviceFingerprint     `json:"device_fingerprint" db:"device_fingerprint"` // JSON
	AccountHistory  AccountHistory         `json:"account_history" db:"account_history"`   // JSON
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

// BehaviorMetrics contains quantitative behavior metrics
type BehaviorMetrics struct {
	TotalPlayTime      int64   `json:"total_play_time"`      // minutes
	SessionsCount      int64   `json:"sessions_count"`
	AverageSessionTime float64 `json:"average_session_time"` // minutes
	PeakConcurrentPlay int     `json:"peak_concurrent_play"` // hours per day
	SkillProgression   float64 `json:"skill_progression"`    // rating change per hour
	AccuracyScore      float64 `json:"accuracy_score"`       // 0.0-1.0
	ReactionTime       int     `json:"reaction_time"`        // milliseconds
	MovementSpeed      float64 `json:"movement_speed"`       // units per second
	ActionFrequency    float64 `json:"action_frequency"`     // actions per minute
	ResourceEfficiency float64 `json:"resource_efficiency"`  // 0.0-1.0
	TeamContribution   float64 `json:"team_contribution"`    // 0.0-1.0
}

// CheatingFlag represents a detected cheating indicator
type CheatingFlag struct {
	FlagType      string    `json:"flag_type"`      // "aimbot", "speedhack", "wallhack", "macro", "multi-account"
	Severity      string    `json:"severity"`       // "low", "medium", "high", "critical"
	Confidence    float64   `json:"confidence"`     // 0.0-1.0 detection confidence
	FirstDetected time.Time `json:"first_detected"`
	LastDetected  time.Time `json:"last_detected"`
	DetectionCount int      `json:"detection_count"`
	Evidence      []Evidence `json:"evidence"`      // supporting evidence
	Status        string    `json:"status"`         // "active", "investigating", "confirmed", "dismissed"
	Investigator  string    `json:"investigator,omitempty"`
	Notes         string    `json:"notes,omitempty"`
}

// Evidence represents evidence supporting a cheating flag
type Evidence struct {
	Type        string                 `json:"type"`        // "screenshot", "video", "log", "statistical"
	Description string                 `json:"description"`
	Data        map[string]interface{} `json:"data"`
	Timestamp   time.Time              `json:"timestamp"`
	Source      string                 `json:"source"`      // "automated", "manual", "player_report"
}

// PlayPatterns contains patterns in player behavior
type PlayPatterns struct {
	PreferredTimes []int  `json:"preferred_times"` // hours of day (0-23)
	PreferredDays  []int  `json:"preferred_days"`  // days of week (0-6)
	SessionLengths []int  `json:"session_lengths"` // minutes
	PlayStreaks    []int  `json:"play_streaks"`    // consecutive days
	BreakPatterns  []int  `json:"break_patterns"`  // minutes between sessions
	RegionSwitches []RegionSwitch `json:"region_switches"`
}

// RegionSwitch represents a change in playing region
type RegionSwitch struct {
	FromRegion  string    `json:"from_region"`
	ToRegion    string    `json:"to_region"`
	Timestamp   time.Time `json:"timestamp"`
	Reason      string    `json:"reason"` // "legitimate", "suspicious", "vpn"
}

// SocialBehavior contains social interaction patterns
type SocialBehavior struct {
	FriendsCount      int                    `json:"friends_count"`
	GuildMemberships  []GuildMembership      `json:"guild_memberships"`
	ChatActivity      ChatActivity           `json:"chat_activity"`
	TradeActivity     TradeActivity          `json:"trade_activity"`
	ReportHistory     []PlayerReport         `json:"report_history"`
	CooperationScore  float64                `json:"cooperation_score"` // 0.0-1.0
}

// GuildMembership represents guild participation
type GuildMembership struct {
	GuildID      string    `json:"guild_id"`
	GuildName    string    `json:"guild_name"`
	JoinedAt     time.Time `json:"joined_at"`
	Role         string    `json:"role"`
	Contribution float64   `json:"contribution"` // participation score
	Status       string    `json:"status"`       // "active", "inactive", "left", "kicked"
}

// ChatActivity represents chat behavior patterns
type ChatActivity struct {
	MessagesPerHour    float64                `json:"messages_per_hour"`
	SpamScore          float64                `json:"spam_score"`          // 0.0-1.0
	ToxicityScore      float64                `json:"toxicity_score"`      // 0.0-1.0
	LanguagePatterns   map[string]interface{} `json:"language_patterns"`
	ChatChannels       []string               `json:"chat_channels"`
	ResponsePatterns   []ResponsePattern      `json:"response_patterns"`
}

// ResponsePattern represents automated response detection
type ResponsePattern struct {
	Pattern     string  `json:"pattern"`
	Frequency   int     `json:"frequency"`
	Similarity  float64 `json:"similarity"` // 0.0-1.0
	IsAutomated bool    `json:"is_automated"`
}

// TradeActivity represents trading behavior
type TradeActivity struct {
	TotalTrades       int64                `json:"total_trades"`
	SuccessfulTrades  int64                `json:"successful_trades"`
	TradeValue        int64                `json:"trade_value"`        // total currency traded
	TradeFrequency    float64              `json:"trade_frequency"`    // trades per day
	SuspiciousTrades  []SuspiciousTrade    `json:"suspicious_trades"`
	ReliabilityScore  float64              `json:"reliability_score"`  // 0.0-1.0
}

// SuspiciousTrade represents a potentially suspicious trade
type SuspiciousTrade struct {
	TradeID     string    `json:"trade_id"`
	Description string    `json:"description"`
	RiskLevel   string    `json:"risk_level"`
	Timestamp   time.Time `json:"timestamp"`
	Investigated bool     `json:"investigated"`
}

// PlayerReport represents a player report
type PlayerReport struct {
	ReporterID   string    `json:"reporter_id"`
	ReportType   string    `json:"report_type"`   // "cheating", "toxicity", "griefing"
	Description  string    `json:"description"`
	Severity     string    `json:"severity"`
	Status       string    `json:"status"`       // "pending", "investigating", "resolved", "dismissed"
	CreatedAt    time.Time `json:"created_at"`
	ResolvedAt   *time.Time `json:"resolved_at,omitempty"`
	Resolver     string    `json:"resolver,omitempty"`
}

// DeviceFingerprint contains device identification data
type DeviceFingerprint struct {
	HardwareID     string            `json:"hardware_id"`
	OSInfo         OSInfo            `json:"os_info"`
	NetworkInfo    NetworkInfo       `json:"network_info"`
	GraphicsInfo   GraphicsInfo      `json:"graphics_info"`
	InputDevices   []InputDevice     `json:"input_devices"`
	BrowserInfo    BrowserInfo       `json:"browser_info"`
	LocationHistory []LocationRecord `json:"location_history"`
	FingerprintHash string           `json:"fingerprint_hash"` // unique hash
}

// OSInfo contains operating system information
type OSInfo struct {
	Name         string `json:"name"`
	Version      string `json:"version"`
	Architecture string `json:"architecture"`
	Language     string `json:"language"`
	Timezone     string `json:"timezone"`
}

// NetworkInfo contains network identification data
type NetworkInfo struct {
	IPAddress    string   `json:"ip_address"`
	MACAddress   string   `json:"mac_address,omitempty"`
	ISP          string   `json:"isp"`
	ASN          string   `json:"asn"`
	Country      string   `json:"country"`
	Region       string   `json:"region"`
	City         string   `json:"city"`
	VPNDetected  bool     `json:"vpn_detected"`
	ProxyDetected bool    `json:"proxy_detected"`
}

// GraphicsInfo contains graphics card information
type GraphicsInfo struct {
	Vendor       string `json:"vendor"`
	Renderer     string `json:"renderer"`
	Version      string `json:"version"`
	VRAM         int    `json:"vram"` // MB
	DriverVersion string `json:"driver_version"`
}

// InputDevice represents an input device
type InputDevice struct {
	Type         string `json:"type"`         // "keyboard", "mouse", "controller"
	Vendor       string `json:"vendor"`
	Product      string `json:"product"`
	SerialNumber string `json:"serial_number,omitempty"`
}

// BrowserInfo contains browser fingerprinting data
type BrowserInfo struct {
	UserAgent    string            `json:"user_agent"`
	Plugins      []string          `json:"plugins"`
	ScreenResolution string        `json:"screen_resolution"`
	ColorDepth   int               `json:"color_depth"`
	TimezoneOffset int             `json:"timezone_offset"`
	Language     string            `json:"language"`
	CookiesEnabled bool            `json:"cookies_enabled"`
	LocalStorageEnabled bool       `json:"local_storage_enabled"`
}

// LocationRecord represents a location detection
type LocationRecord struct {
	Latitude    float64   `json:"latitude"`
	Longitude   float64   `json:"longitude"`
	Accuracy    float64   `json:"accuracy"`    // meters
	Timestamp   time.Time `json:"timestamp"`
	Method      string    `json:"method"`      // "ip", "gps", "wifi"
}

// AccountHistory contains account lifecycle data
type AccountHistory struct {
	CreatedAt      time.Time          `json:"created_at"`
	EmailChanges   []EmailChange      `json:"email_changes"`
	PasswordChanges []PasswordChange  `json:"password_changes"`
	RecoveryRequests []RecoveryRequest `json:"recovery_requests"`
	Suspensions    []AccountSuspension `json:"suspensions"`
	Warnings       []AccountWarning    `json:"warnings"`
}

// EmailChange represents an email address change
type EmailChange struct {
	FromEmail string    `json:"from_email"`
	ToEmail   string    `json:"to_email"`
	ChangedAt time.Time `json:"changed_at"`
	Reason    string    `json:"reason"`
}

// PasswordChange represents a password change
type PasswordChange struct {
	ChangedAt time.Time `json:"changed_at"`
	Method    string    `json:"method"` // "user", "admin", "recovery"
	Strength  string    `json:"strength"` // "weak", "medium", "strong"
}

// RecoveryRequest represents a password recovery request
type RecoveryRequest struct {
	RequestedAt time.Time `json:"requested_at"`
	Method      string    `json:"method"` // "email", "phone", "security_questions"
	Successful  bool      `json:"successful"`
	IPAddress   string    `json:"ip_address"`
}

// AccountSuspension represents an account suspension
type AccountSuspension struct {
	SuspensionID   string     `json:"suspension_id"`
	Reason         string     `json:"reason"`
	Severity       string     `json:"severity"`       // "warning", "temporary", "permanent"
	SuspendedAt    time.Time  `json:"suspended_at"`
	SuspendedUntil *time.Time `json:"suspended_until,omitempty"`
	SuspendedBy    string     `json:"suspended_by"`
	LiftedAt       *time.Time `json:"lifted_at,omitempty"`
	LiftedBy       string     `json:"lifted_by,omitempty"`
}

// AccountWarning represents an account warning
type AccountWarning struct {
	WarningID  string    `json:"warning_id"`
	Reason     string    `json:"reason"`
	Severity   string    `json:"severity"`
	IssuedAt   time.Time `json:"issued_at"`
	IssuedBy   string    `json:"issued_by"`
	ExpiresAt  *time.Time `json:"expires_at,omitempty"`
}

// DetectionEvent represents a cheating detection event
type DetectionEvent struct {
	EventID       string                 `json:"event_id" db:"event_id"`
	PlayerID      string                 `json:"player_id" db:"player_id"`
	DetectionType string                 `json:"detection_type" db:"detection_type"`
	Severity      string                 `json:"severity" db:"severity"`
	Confidence    float64                `json:"confidence" db:"confidence"`
	GameSessionID string                 `json:"game_session_id" db:"game_session_id"`
	Timestamp     time.Time              `json:"timestamp" db:"timestamp"`
	Data          map[string]interface{} `json:"data" db:"data"`                   // detection data
	Evidence      []Evidence             `json:"evidence" db:"evidence"`           // JSON array
	Status        string                 `json:"status" db:"status"`               // "new", "investigating", "confirmed", "false_positive"
	Investigator  string                 `json:"investigator" db:"investigator"`
	Notes         string                 `json:"notes" db:"notes"`
	CreatedAt     time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time              `json:"updated_at" db:"updated_at"`
}

// InvestigationCase represents an investigation case
type InvestigationCase struct {
	CaseID       string            `json:"case_id" db:"case_id"`
	PlayerID     string            `json:"player_id" db:"player_id"`
	Investigator string            `json:"investigator" db:"investigator"`
	Status       string            `json:"status" db:"status"`             // "open", "closed", "escalated"
	Priority     string            `json:"priority" db:"priority"`         // "low", "medium", "high", "critical"
	CreatedAt    time.Time         `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time         `json:"updated_at" db:"updated_at"`
	DetectionEvents []string       `json:"detection_events" db:"detection_events"` // JSON array of event IDs
	Evidence     []Evidence        `json:"evidence" db:"evidence"`         // JSON array
	ActionsTaken []CaseAction      `json:"actions_taken" db:"actions_taken"` // JSON array
	Conclusion   string            `json:"conclusion" db:"conclusion"`
	ClosedAt     *time.Time        `json:"closed_at" db:"closed_at"`
}

// CaseAction represents an action taken in an investigation
type CaseAction struct {
	ActionType string    `json:"action_type"` // "warning", "suspension", "ban", "appeal"
	Description string   `json:"description"`
	TakenAt     time.Time `json:"taken_at"`
	TakenBy     string    `json:"taken_by"`
	Duration    *time.Duration `json:"duration,omitempty"`
}

// AppealRequest represents a player appeal against a sanction
type AppealRequest struct {
	AppealID    string    `json:"appeal_id" db:"appeal_id"`
	PlayerID    string    `json:"player_id" db:"player_id"`
	CaseID      string    `json:"case_id" db:"case_id"`
	AppealType  string    `json:"appeal_type" db:"appeal_type"` // "warning", "suspension", "ban"
	Description string    `json:"description" db:"description"`
	Evidence    []Evidence `json:"evidence" db:"evidence"`     // JSON array
	Status      string    `json:"status" db:"status"`           // "pending", "reviewing", "approved", "denied"
	SubmittedAt time.Time `json:"submitted_at" db:"submitted_at"`
	ReviewedAt  *time.Time `json:"reviewed_at" db:"reviewed_at"`
	ReviewedBy  string    `json:"reviewed_by" db:"reviewed_by"`
	Response    string    `json:"response" db:"response"`
}

// AnalyticsReport represents an analytics report
type AnalyticsReport struct {
	ReportID      string                 `json:"report_id" db:"report_id"`
	ReportType    string                 `json:"report_type" db:"report_type"`   // "daily", "weekly", "monthly", "custom"
	TimeRange     TimeRange              `json:"time_range" db:"time_range"`     // JSON
	GeneratedAt   time.Time              `json:"generated_at" db:"generated_at"`
	GeneratedBy   string                 `json:"generated_by" db:"generated_by"`
	Summary       ReportSummary          `json:"summary" db:"summary"`           // JSON
	Data          map[string]interface{} `json:"data" db:"data"`                 // JSON report data
	Recommendations []string             `json:"recommendations" db:"recommendations"` // JSON array
	Status        string                 `json:"status" db:"status"`             // "generating", "completed", "failed"
}

// TimeRange represents a time range for reports
type TimeRange struct {
	Start time.Time `json:"start"`
	End   time.Time `json:"end"`
}

// ReportSummary contains report summary statistics
type ReportSummary struct {
	TotalPlayers     int64   `json:"total_players"`
	DetectionEvents  int64   `json:"detection_events"`
	FalsePositives   int64   `json:"false_positives"`
	ConfirmedCheats  int64   `json:"confirmed_cheats"`
	BannedAccounts   int64   `json:"banned_accounts"`
	AppealRate       float64 `json:"appeal_rate"`
	ResolutionTime   int64   `json:"resolution_time"` // hours
}

// BanWave represents a coordinated ban action
type BanWave struct {
	WaveID       string       `json:"wave_id" db:"wave_id"`
	Name         string       `json:"name" db:"name"`
	Description  string       `json:"description" db:"description"`
	Status       string       `json:"status" db:"status"`       // "planned", "executing", "completed"
	PlannedAt    time.Time    `json:"planned_at" db:"planned_at"`
	ExecutedAt   *time.Time   `json:"executed_at" db:"executed_at"`
	TargetPlayers []BanTarget `json:"target_players" db:"target_players"` // JSON array
	Results      BanResults   `json:"results" db:"results"`     // JSON
	CreatedBy    string       `json:"created_by" db:"created_by"`
	ExecutedBy   string       `json:"executed_by" db:"executed_by"`
}

// BanTarget represents a player targeted for banning
type BanTarget struct {
	PlayerID  string `json:"player_id"`
	Reason    string `json:"reason"`
	Severity  string `json:"severity"`
	Evidence  []string `json:"evidence"` // evidence IDs
}

// BanResults contains results of a ban wave execution
type BanResults struct {
	TotalTargets    int `json:"total_targets"`
	SuccessfulBans  int `json:"successful_bans"`
	FailedBans      int `json:"failed_bans"`
	AppealsFiled    int `json:"appeals_filed"`
	Errors          []string `json:"errors"`
}

// RiskAssessment represents a risk assessment for a player
type RiskAssessment struct {
	AssessmentID string    `json:"assessment_id" db:"assessment_id"`
	PlayerID     string    `json:"player_id" db:"player_id"`
	RiskScore    float64   `json:"risk_score" db:"risk_score"`     // 0.0-1.0
	RiskLevel    string    `json:"risk_level" db:"risk_level"`     // "low", "medium", "high", "extreme"
	Factors      []RiskFactor `json:"factors" db:"factors"`       // JSON array
	Recommendations []string `json:"recommendations" db:"recommendations"` // JSON array
	AssessedAt   time.Time `json:"assessed_at" db:"assessed_at"`
	AssessedBy   string    `json:"assessed_by" db:"assessed_by"`
	NextAssessment *time.Time `json:"next_assessment" db:"next_assessment"`
}

// RiskFactor represents a factor contributing to risk score
type RiskFactor struct {
	FactorType string  `json:"factor_type"` // "behavior", "detection", "social", "account"
	Weight     float64 `json:"weight"`      // contribution to risk score
	Description string `json:"description"`
	Data       map[string]interface{} `json:"data"`
}
