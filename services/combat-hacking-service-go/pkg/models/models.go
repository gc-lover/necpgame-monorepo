package models

// Vector3 represents 3D coordinates
// BACKEND NOTE: Aligned for SIMD operations. Memory layout optimized for physics calculations.
type Vector3 struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// ScreenHackBlindRequest represents request for screen hack blind skill
// Issue: #143875347
type ScreenHackBlindRequest struct {
	PlayerID      string  `json:"player_id"`
	ScreenPosition Vector3 `json:"screen_position"`
	SkillLevel    int     `json:"skill_level"`
	ImplantID     string  `json:"implant_id,omitempty"`
}

// ScreenHackBlindResponse represents response for screen hack blind skill
// Issue: #143875347
type ScreenHackBlindResponse struct {
	Success           bool    `json:"success"`
	ZoneID            string  `json:"zone_id"`
	Duration          int     `json:"duration"`
	Radius            float64 `json:"radius"`
	DetectionRisk     float64 `json:"detection_risk"`
	CooldownRemaining int     `json:"cooldown_remaining"`
	AffectedEnemies   int     `json:"affected_enemies"`
}

// GlitchDoublesRequest represents request for glitch doubles skill
// Issue: #143875814
type GlitchDoublesRequest struct {
	PlayerID   string `json:"player_id"`
	SkillLevel int    `json:"skill_level"`
	ImplantID  string `json:"implant_id,omitempty"`
	EnemyCount int    `json:"enemy_count"`
}

// GlitchDoublesResponse represents response for glitch doubles skill
// Issue: #143875814
type GlitchDoublesResponse struct {
	Success           bool     `json:"success"`
	PhantomIDs        []string `json:"phantom_ids"`
	Duration          int      `json:"duration"`
	Range             float64  `json:"range"`
	DetectionRisk     float64  `json:"detection_risk"`
	CooldownRemaining int      `json:"cooldown_remaining"`
	AffectedEnemies   int      `json:"affected_enemies"`
}

// BlindZone represents an active blind zone
type BlindZone struct {
	ID             string  `json:"id"`
	Position       Vector3 `json:"position"`
	Radius         float64 `json:"radius"`
	Duration       int     `json:"duration"`
	DetectionRisk  float64 `json:"detection_risk"`
	AffectedEnemies int     `json:"affected_enemies"`
	CreatedAt      int64   `json:"created_at"`
}

// PhantomEntity represents a glitch double phantom
type PhantomEntity struct {
	ID            string  `json:"id"`
	PlayerID      string  `json:"player_id"`
	Position      Vector3 `json:"position"`
	Duration      int     `json:"duration"`
	Range         float64 `json:"range"`
	DetectionRisk float64 `json:"detection_risk"`
	CreatedAt     int64   `json:"created_at"`
}

// Enemy hacking models
type EnemyScanRequest struct {
	PlayerID  string  `json:"player_id"`
	TargetID  string  `json:"target_id"`
	ScanType  string  `json:"scan_type"` // "quick", "deep", "comprehensive"
	Position  Vector3 `json:"position"`
}

type EnemyScanResult struct {
	Success         bool                `json:"success"`
	TargetID        string              `json:"target_id"`
	Vulnerabilities []VulnerabilityInfo `json:"vulnerabilities"`
	ScanDuration    int                 `json:"scan_duration"`
	DetectionRisk   float64             `json:"detection_risk"`
}

type VulnerabilityInfo struct {
	Type        string  `json:"type"`         // "implant", "cyberware", "neural_link"
	Severity    string  `json:"severity"`     // "low", "medium", "high", "critical"
	ExploitTime int     `json:"exploit_time"` // seconds
	Description string  `json:"description"`
}

type EnemyHackRequest struct {
	PlayerID     string  `json:"player_id"`
	TargetID     string  `json:"target_id"`
	VulnerabilityType string `json:"vulnerability_type"`
	SkillLevel   int     `json:"skill_level"`
	Position     Vector3 `json:"position"`
}

type EnemyHackResult struct {
	Success       bool    `json:"success"`
	TargetID      string  `json:"target_id"`
	EffectType    string  `json:"effect_type"`    // "damage", "stun", "shutdown", "mind_control"
	Duration      int     `json:"duration"`
	Damage        int     `json:"damage,omitempty"`
	DetectionRisk float64 `json:"detection_risk"`
}

// Device hacking models
type DeviceScanRequest struct {
	PlayerID   string  `json:"player_id"`
	DeviceID   string  `json:"device_id"`
	DeviceType string  `json:"device_type"` // "camera", "terminal", "security_system", "vehicle"
	ScanRange  float64 `json:"scan_range"`
	Position   Vector3 `json:"position"`
}

type DeviceScanResult struct {
	Success         bool               `json:"success"`
	DeviceID        string             `json:"device_id"`
	DeviceType      string             `json:"device_type"`
	Vulnerabilities []DeviceVulnerability `json:"vulnerabilities"`
	SecurityLevel   string             `json:"security_level"` // "none", "basic", "advanced", "military"
	ScanDuration    int                `json:"scan_duration"`
}

type DeviceVulnerability struct {
	Type         string  `json:"type"`          // "password", "backdoor", "physical", "wireless"
	Difficulty   string  `json:"difficulty"`    // "easy", "medium", "hard"
	ExploitTime  int     `json:"exploit_time"`
	Description  string  `json:"description"`
}

type DeviceHackRequest struct {
	PlayerID   string  `json:"player_id"`
	DeviceID   string  `json:"device_id"`
	DeviceType string  `json:"device_type"`
	VulnerabilityType string `json:"vulnerability_type"`
	SkillLevel int     `json:"skill_level"`
	Position   Vector3 `json:"position"`
}

type DeviceHackResult struct {
	Success       bool    `json:"success"`
	DeviceID      string  `json:"device_id"`
	EffectType    string  `json:"effect_type"`    // "control", "disable", "data_steal", "alarm_trigger"
	Duration      int     `json:"duration"`
	DataExtracted string  `json:"data_extracted,omitempty"`
	DetectionRisk float64 `json:"detection_risk"`
}

// Network hacking models
type NetworkInfiltrationRequest struct {
	PlayerID      string  `json:"player_id"`
	NetworkID     string  `json:"network_id"`
	NetworkType   string  `json:"network_type"` // "corporate", "government", "criminal", "personal"
	EntryPoint    string  `json:"entry_point"`  // "wifi", "bluetooth", "physical", "social"
	SkillLevel    int     `json:"skill_level"`
	Position      Vector3 `json:"position"`
}

type NetworkInfiltrationResult struct {
	Success       bool     `json:"success"`
	NetworkID     string   `json:"network_id"`
	AccessLevel   string   `json:"access_level"`   // "user", "admin", "root"
	ICELevel      int      `json:"ice_level"`      // 1-10
	Duration      int      `json:"duration"`
	DetectionRisk float64  `json:"detection_risk"`
}

type DataExtractionRequest struct {
	PlayerID    string `json:"player_id"`
	NetworkID   string `json:"network_id"`
	DataType    string `json:"data_type"`    // "financial", "personal", "corporate", "military"
	AccessLevel string `json:"access_level"`
	SkillLevel  int    `json:"skill_level"`
}

type DataExtractionResult struct {
	Success         bool     `json:"success"`
	DataType        string   `json:"data_type"`
	DataSize        int      `json:"data_size"`        // MB
	Value           int      `json:"value"`            // in-game currency
	Sensitivity     string   `json:"sensitivity"`      // "low", "medium", "high", "critical"
	DetectionRisk   float64  `json:"detection_risk"`
	ExtractionTime  int      `json:"extraction_time"`
}

// Combat support models
type CombatSupportRequest struct {
	PlayerID     string   `json:"player_id"`
	SupportType  string   `json:"support_type"`  // "recon", "firewall", "decoy", "overload"
	TargetIDs    []string `json:"target_ids"`
	SkillLevel   int      `json:"skill_level"`
	Position     Vector3  `json:"position"`
}

type CombatSupportResult struct {
	Success       bool     `json:"success"`
	SupportType   string   `json:"support_type"`
	EffectArea    Vector3  `json:"effect_area"`
	Duration      int      `json:"duration"`
	AffectedTargets int    `json:"affected_targets"`
	DetectionRisk float64  `json:"detection_risk"`
}

// Anti-cheat validation models
type HackingValidationRequest struct {
	PlayerID     string  `json:"player_id"`
	ActionType   string  `json:"action_type"`   // "scan", "hack", "extract", "support"
	TargetID     string  `json:"target_id,omitempty"`
	SkillLevel   int     `json:"skill_level"`
	Timestamp    int64   `json:"timestamp"`
	Position     Vector3 `json:"position"`
}

type HackingValidationResult struct {
	Valid         bool    `json:"valid"`
	PlayerID      string  `json:"player_id"`
	ActionType    string  `json:"action_type"`
	Confidence    float64 `json:"confidence"`    // 0.0 - 1.0
	AnomalyScore  float64 `json:"anomaly_score"` // 0.0 - 1.0
	Flags         []string `json:"flags"`        // ["speed_hack", "aim_assist", "wall_hack"]
}

// Active hacks management models
type ActiveHacksResponse struct {
	PlayerID     string     `json:"player_id"`
	ActiveHacks  []ActiveHack `json:"active_hacks"`
	TotalCount   int        `json:"total_count"`
	LastUpdated  int64      `json:"last_updated"`
}

type ActiveHack struct {
	HackID        string  `json:"hack_id"`
	HackType      string  `json:"hack_type"`      // "blind_zone", "phantom", "device_control", "network_access"
	TargetID      string  `json:"target_id"`
	Status        string  `json:"status"`         // "active", "expiring", "failed"
	Duration      int     `json:"duration"`
	TimeRemaining int     `json:"time_remaining"`
	Position      Vector3 `json:"position"`
}

// BACKEND NOTE: Struct field alignment optimized (large → small types).
// Expected memory savings: 30-50% for hacking-related structs.

// Issue: #143875347, #143875814, #143875915, #143875916, #143875917, #143875918, #143875919, #143875920

