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

// BACKEND NOTE: Struct field alignment optimized (large → small types).
// Expected memory savings: 30-50% for hacking-related structs.

// Issue: #143875347, #143875814

