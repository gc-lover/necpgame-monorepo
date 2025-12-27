package repository

import (
	"errors"
	"sync"
	"time"

	"combat-hacking-service-go/pkg/models"
)

type Repository struct {
	mu              sync.RWMutex
	blindZones      map[string]*models.BlindZone
	phantomEntities map[string]*models.PhantomEntity
	activeHacks     map[string]*models.ActiveHack
	enemyScans      map[string]*models.EnemyScanResult
	deviceScans     map[string]*models.DeviceScanResult
	networkAccess   map[string]*models.NetworkInfiltrationResult
}

func NewRepository() *Repository {
	return &Repository{
		blindZones:      make(map[string]*models.BlindZone),
		phantomEntities: make(map[string]*models.PhantomEntity),
		activeHacks:     make(map[string]*models.ActiveHack),
		enemyScans:      make(map[string]*models.EnemyScanResult),
		deviceScans:     make(map[string]*models.DeviceScanResult),
		networkAccess:   make(map[string]*models.NetworkInfiltrationResult),
	}
}

// CreateBlindZone creates a new blind zone from screen hack skill
// Issue: #143875347
func (r *Repository) CreateBlindZone(req models.ScreenHackBlindRequest) (*models.BlindZone, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Calculate zone parameters based on skill level
	var radius float64
	var duration int
	var detectionRisk float64

	switch req.SkillLevel {
	case 1:
		radius = 8.0
		duration = 4
		detectionRisk = 0.15
	case 2:
		radius = 12.0
		duration = 6
		detectionRisk = 0.20
	case 3:
		radius = 15.0
		duration = 8
		detectionRisk = 0.25
	default:
		return nil, errors.New("invalid skill level")
	}

	// Generate zone ID (in real implementation, use UUID)
	zoneID := generateID()

	zone := &models.BlindZone{
		ID:             zoneID,
		Position:       req.ScreenPosition,
		Radius:         radius,
		Duration:       duration,
		DetectionRisk:  detectionRisk,
		AffectedEnemies: 0, // Will be calculated based on enemy positions
		CreatedAt:      time.Now().Unix(),
	}

	r.blindZones[zoneID] = zone

	// Start cleanup goroutine
	go func() {
		time.Sleep(time.Duration(duration) * time.Second)
		r.mu.Lock()
		delete(r.blindZones, zoneID)
		r.mu.Unlock()
	}()

	return zone, nil
}

// CreateGlitchDoubles creates phantom entities from glitch doubles skill
// Issue: #143875814
func (r *Repository) CreateGlitchDoubles(req models.GlitchDoublesRequest) ([]*models.PhantomEntity, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Calculate phantom parameters based on skill level
	var phantomCount int
	var duration int
	var phantomRange float64
	var detectionRisk float64

	switch req.SkillLevel {
	case 1:
		phantomCount = 2
		duration = 6
		phantomRange = 15.0
		detectionRisk = 0.20
	case 2:
		phantomCount = 3
		duration = 9
		phantomRange = 20.0
		detectionRisk = 0.25
	case 3:
		phantomCount = 4
		duration = 12
		phantomRange = 25.0
		detectionRisk = 0.30
	default:
		return nil, errors.New("invalid skill level")
	}

	phantoms := make([]*models.PhantomEntity, phantomCount)

	for i := 0; i < phantomCount; i++ {
		phantomID := generateID()

		phantom := &models.PhantomEntity{
			ID:            phantomID,
			PlayerID:      req.PlayerID,
			Position:      models.Vector3{X: 0, Y: 0, Z: 0}, // Will be set to player position
			Duration:      duration,
			Range:         phantomRange,
			DetectionRisk: detectionRisk,
			CreatedAt:     time.Now().Unix(),
		}

		r.phantomEntities[phantomID] = phantom
		phantoms[i] = phantom

		// Start cleanup goroutine
		go func(id string) {
			time.Sleep(time.Duration(duration) * time.Second)
			r.mu.Lock()
			delete(r.phantomEntities, id)
			r.mu.Unlock()
		}(phantomID)
	}

	return phantoms, nil
}

// Enemy Hacking Operations
// Issue: #143875915

// ScanEnemyCyberware performs cyberware scanning on an enemy
func (r *Repository) ScanEnemyCyberware(req models.EnemyScanRequest) (*models.EnemyScanResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Generate scan result based on request
	result := &models.EnemyScanResult{
		Success: true,
		TargetID: req.TargetID,
		ScanDuration: 3, // seconds
		DetectionRisk: 0.15,
		Vulnerabilities: []models.VulnerabilityInfo{
			{
				Type: "implant",
				Severity: "medium",
				ExploitTime: 5,
				Description: "Neural implant vulnerable to overload",
			},
		},
	}

	r.enemyScans[req.TargetID] = result
	return result, nil
}

// HackEnemyCyberware attempts to hack enemy cyberware
func (r *Repository) HackEnemyCyberware(req models.EnemyHackRequest) (*models.EnemyHackResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Determine hack success based on skill level and vulnerability
	var success bool
	var effectType string
	var duration int
	var damage int

	switch req.SkillLevel {
	case 1, 2:
		success = true
		effectType = "stun"
		duration = 3
	case 3:
		success = true
		effectType = "damage"
		duration = 5
		damage = 150
	}

	result := &models.EnemyHackResult{
		Success: success,
		TargetID: req.TargetID,
		EffectType: effectType,
		Duration: duration,
		Damage: damage,
		DetectionRisk: 0.25,
	}

	return result, nil
}

// Device Hacking Operations
// Issue: #143875916

// ScanDeviceCyberware performs device scanning
func (r *Repository) ScanDeviceCyberware(req models.DeviceScanRequest) (*models.DeviceScanResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	result := &models.DeviceScanResult{
		Success: true,
		DeviceID: req.DeviceID,
		DeviceType: req.DeviceType,
		SecurityLevel: "basic",
		ScanDuration: 2,
		Vulnerabilities: []models.DeviceVulnerability{
			{
				Type: "password",
				Difficulty: "easy",
				ExploitTime: 3,
				Description: "Weak password protection",
			},
		},
	}

	r.deviceScans[req.DeviceID] = result
	return result, nil
}

// HackDevice attempts to hack a device
func (r *Repository) HackDevice(req models.DeviceHackRequest) (*models.DeviceHackResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	var success bool
	var effectType string
	var duration int
	var dataExtracted string

	switch req.VulnerabilityType {
	case "password":
		success = true
		effectType = "control"
		duration = 30
	case "backdoor":
		success = true
		effectType = "data_steal"
		duration = 10
		dataExtracted = "security_logs"
	}

	result := &models.DeviceHackResult{
		Success: success,
		DeviceID: req.DeviceID,
		EffectType: effectType,
		Duration: duration,
		DataExtracted: dataExtracted,
		DetectionRisk: 0.20,
	}

	return result, nil
}

// Network Hacking Operations
// Issue: #143875917

// InfiltrateNetwork attempts network infiltration
func (r *Repository) InfiltrateNetwork(req models.NetworkInfiltrationRequest) (*models.NetworkInfiltrationResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	result := &models.NetworkInfiltrationResult{
		Success: true,
		NetworkID: req.NetworkID,
		AccessLevel: "user",
		ICELevel: 2,
		Duration: 300, // 5 minutes
		DetectionRisk: 0.35,
	}

	r.networkAccess[req.NetworkID] = result
	return result, nil
}

// ExtractData performs data extraction from network
func (r *Repository) ExtractData(req models.DataExtractionRequest) (*models.DataExtractionResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	result := &models.DataExtractionResult{
		Success: true,
		DataType: req.DataType,
		DataSize: 50, // MB
		Value: 2500,  // in-game currency
		Sensitivity: "medium",
		DetectionRisk: 0.40,
		ExtractionTime: 45, // seconds
	}

	return result, nil
}

// Combat Support Operations
// Issue: #143875918

// ActivateCombatSupport provides combat support hacking
func (r *Repository) ActivateCombatSupport(req models.CombatSupportRequest) (*models.CombatSupportResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	result := &models.CombatSupportResult{
		Success: true,
		SupportType: req.SupportType,
		EffectArea: models.Vector3{X: 0, Y: 0, Z: 0}, // Would be calculated from player position
		Duration: 15,
		AffectedTargets: len(req.TargetIDs),
		DetectionRisk: 0.30,
	}

	return result, nil
}

// Anti-Cheat Operations
// Issue: #143875919

// ValidateHackingAttempt validates a hacking attempt for anti-cheat
func (r *Repository) ValidateHackingAttempt(req models.HackingValidationRequest) (*models.HackingValidationResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Simple validation logic - in real implementation would be more sophisticated
	result := &models.HackingValidationResult{
		Valid: true,
		PlayerID: req.PlayerID,
		ActionType: req.ActionType,
		Confidence: 0.95,
		AnomalyScore: 0.05,
		Flags: []string{}, // No anomalies detected
	}

	return result, nil
}

// GetActiveBlindZones returns all currently active blind zones
func (r *Repository) GetActiveBlindZones() map[string]*models.BlindZone {
	r.mu.RLock()
	defer r.mu.RUnlock()

	zones := make(map[string]*models.BlindZone)
	for id, zone := range r.blindZones {
		zones[id] = zone
	}
	return zones
}

// GetActivePhantoms returns all currently active phantom entities
func (r *Repository) GetActivePhantoms() map[string]*models.PhantomEntity {
	r.mu.RLock()
	defer r.mu.RUnlock()

	phantoms := make(map[string]*models.PhantomEntity)
	for id, phantom := range r.phantomEntities {
		phantoms[id] = phantom
	}
	return phantoms
}

// GetActiveHacks returns all active hacks for a player
func (r *Repository) GetActiveHacks(playerID string) []models.ActiveHack {
	r.mu.RLock()
	defer r.mu.RUnlock()

	var activeHacks []models.ActiveHack

	// Add blind zones
	for id, zone := range r.blindZones {
		activeHacks = append(activeHacks, models.ActiveHack{
			HackID: id,
			HackType: "blind_zone",
			TargetID: "",
			Status: "active",
			Duration: zone.Duration,
			TimeRemaining: zone.Duration, // Simplified
			Position: zone.Position,
		})
	}

	// Add phantom entities
	for id, phantom := range r.phantomEntities {
		if phantom.PlayerID == playerID {
			activeHacks = append(activeHacks, models.ActiveHack{
				HackID: id,
				HackType: "phantom",
				TargetID: "",
				Status: "active",
				Duration: phantom.Duration,
				TimeRemaining: phantom.Duration, // Simplified
				Position: phantom.Position,
			})
		}
	}

	return activeHacks
}

// Enemy hacking methods
func (r *Repository) ScanEnemy(req models.EnemyScanRequest) (*models.EnemyScanResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Simulate scan time based on scan type
	var scanDuration int
	var vulnerabilities []models.VulnerabilityInfo

	switch req.ScanType {
	case "quick":
		scanDuration = 2
		vulnerabilities = []models.VulnerabilityInfo{
			{Type: "implant", Severity: "medium", ExploitTime: 5, Description: "Neural interface vulnerability"},
		}
	case "deep":
		scanDuration = 5
		vulnerabilities = []models.VulnerabilityInfo{
			{Type: "implant", Severity: "medium", ExploitTime: 5, Description: "Neural interface vulnerability"},
			{Type: "cyberware", Severity: "high", ExploitTime: 8, Description: "Optical implant backdoor"},
		}
	case "comprehensive":
		scanDuration = 10
		vulnerabilities = []models.VulnerabilityInfo{
			{Type: "implant", Severity: "medium", ExploitTime: 5, Description: "Neural interface vulnerability"},
			{Type: "cyberware", Severity: "high", ExploitTime: 8, Description: "Optical implant backdoor"},
			{Type: "neural_link", Severity: "critical", ExploitTime: 15, Description: "Full system compromise possible"},
		}
	default:
		return nil, errors.New("invalid scan type")
	}

	result := &models.EnemyScanResult{
		Success:         true,
		TargetID:        req.TargetID,
		Vulnerabilities: vulnerabilities,
		ScanDuration:    scanDuration,
		DetectionRisk:   0.1 * float64(scanDuration), // Higher risk for longer scans
	}

	r.enemyScans[req.PlayerID+"_"+req.TargetID] = result
	return result, nil
}

func (r *Repository) HackEnemy(req models.EnemyHackRequest) (*models.EnemyHackResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Calculate hack success and effects based on vulnerability and skill
	var success bool
	var effectType string
	var duration int
	var damage int
	var detectionRisk float64

	// Simplified success calculation
	baseSuccess := float64(req.SkillLevel) * 0.3
	if req.VulnerabilityType == "critical" {
		baseSuccess += 0.4
	} else if req.VulnerabilityType == "high" {
		baseSuccess += 0.2
	}

	success = baseSuccess > 0.5 // 50% base threshold

	if success {
		switch req.VulnerabilityType {
		case "critical":
			effectType = "mind_control"
			duration = 30
			damage = 0
		case "high":
			effectType = "shutdown"
			duration = 15
			damage = 50
		default:
			effectType = "stun"
			duration = 5
			damage = 25
		}
		detectionRisk = 0.3
	} else {
		effectType = "failed"
		duration = 0
		damage = 0
		detectionRisk = 0.1
	}

	result := &models.EnemyHackResult{
		Success:       success,
		TargetID:      req.TargetID,
		EffectType:    effectType,
		Duration:      duration,
		Damage:        damage,
		DetectionRisk: detectionRisk,
	}

	return result, nil
}

// Device hacking methods
func (r *Repository) ScanDevice(req models.DeviceScanRequest) (*models.DeviceScanResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	var vulnerabilities []models.DeviceVulnerability
	var securityLevel string
	var scanDuration int

	switch req.DeviceType {
	case "camera":
		securityLevel = "basic"
		scanDuration = 3
		vulnerabilities = []models.DeviceVulnerability{
			{Type: "wireless", Difficulty: "easy", ExploitTime: 3, Description: "Unencrypted WiFi signal"},
		}
	case "terminal":
		securityLevel = "advanced"
		scanDuration = 8
		vulnerabilities = []models.DeviceVulnerability{
			{Type: "password", Difficulty: "medium", ExploitTime: 10, Description: "Weak admin password"},
			{Type: "backdoor", Difficulty: "hard", ExploitTime: 20, Description: "Firmware vulnerability"},
		}
	case "security_system":
		securityLevel = "military"
		scanDuration = 15
		vulnerabilities = []models.DeviceVulnerability{
			{Type: "physical", Difficulty: "hard", ExploitTime: 30, Description: "Tamper-evident seal bypass"},
		}
	default:
		securityLevel = "basic"
		scanDuration = 5
	}

	result := &models.DeviceScanResult{
		Success:         true,
		DeviceID:        req.DeviceID,
		DeviceType:      req.DeviceType,
		Vulnerabilities: vulnerabilities,
		SecurityLevel:   securityLevel,
		ScanDuration:    scanDuration,
	}

	r.deviceScans[req.DeviceID] = result
	return result, nil
}

func (r *Repository) HackDevice(req models.DeviceHackRequest) (*models.DeviceHackResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	var success bool
	var effectType string
	var duration int
	var dataExtracted string
	var detectionRisk float64

	// Simplified success calculation
	baseSuccess := float64(req.SkillLevel) * 0.25
	if req.VulnerabilityType == "easy" {
		baseSuccess += 0.5
	} else if req.VulnerabilityType == "medium" {
		baseSuccess += 0.25
	}

	success = baseSuccess > 0.4 // 40% base threshold

	if success {
		switch req.DeviceType {
		case "camera":
			effectType = "control"
			duration = 60
			dataExtracted = "surveillance footage"
		case "terminal":
			effectType = "data_steal"
			duration = 30
			dataExtracted = "system credentials"
		case "security_system":
			effectType = "disable"
			duration = 45
			dataExtracted = "access codes"
		default:
			effectType = "control"
			duration = 30
		}
		detectionRisk = 0.2
	} else {
		effectType = "alarm_trigger"
		duration = 0
		detectionRisk = 0.8
	}

	result := &models.DeviceHackResult{
		Success:       success,
		DeviceID:      req.DeviceID,
		EffectType:    effectType,
		Duration:      duration,
		DataExtracted: dataExtracted,
		DetectionRisk: detectionRisk,
	}

	return result, nil
}

// Network hacking methods
func (r *Repository) InfiltrateNetwork(req models.NetworkInfiltrationRequest) (*models.NetworkInfiltrationResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	var accessLevel string
	var iceLevel int
	var duration int
	var detectionRisk float64
	var success bool

	// Calculate infiltration difficulty
	baseSuccess := float64(req.SkillLevel) * 0.2

	switch req.NetworkType {
	case "personal":
		baseSuccess += 0.6
		iceLevel = 1
	case "corporate":
		baseSuccess += 0.3
		iceLevel = 3
	case "government":
		baseSuccess += 0.1
		iceLevel = 5
	case "criminal":
		baseSuccess += 0.4
		iceLevel = 4
	default:
		iceLevel = 2
	}

	if req.EntryPoint == "physical" {
		baseSuccess += 0.3
	}

	success = baseSuccess > 0.5

	if success {
		switch req.NetworkType {
		case "personal":
			accessLevel = "root"
		case "corporate":
			accessLevel = "admin"
		default:
			accessLevel = "user"
		}
		duration = 300 // 5 minutes
		detectionRisk = 0.15
	} else {
		accessLevel = "none"
		duration = 0
		detectionRisk = 0.9
	}

	result := &models.NetworkInfiltrationResult{
		Success:       success,
		NetworkID:     req.NetworkID,
		AccessLevel:   accessLevel,
		ICELevel:      iceLevel,
		Duration:      duration,
		DetectionRisk: detectionRisk,
	}

	if success {
		r.networkAccess[req.NetworkID] = result
	}

	return result, nil
}

func (r *Repository) ExtractData(req models.DataExtractionRequest) (*models.DataExtractionResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Check if we have network access
	access, exists := r.networkAccess[req.NetworkID]
	if !exists || access.AccessLevel == "none" {
		return nil, errors.New("no network access")
	}

	var success bool
	var dataSize int
	var value int
	var sensitivity string
	var extractionTime int
	var detectionRisk float64

	baseSuccess := float64(req.SkillLevel) * 0.3
	if access.AccessLevel == "root" {
		baseSuccess += 0.4
	} else if access.AccessLevel == "admin" {
		baseSuccess += 0.2
	}

	success = baseSuccess > 0.6

	if success {
		switch req.DataType {
		case "financial":
			dataSize = 500
			value = 5000
			sensitivity = "high"
			extractionTime = 60
		case "personal":
			dataSize = 200
			value = 1000
			sensitivity = "medium"
			extractionTime = 30
		case "corporate":
			dataSize = 1000
			value = 15000
			sensitivity = "critical"
			extractionTime = 120
		case "military":
			dataSize = 2000
			value = 50000
			sensitivity = "critical"
			extractionTime = 300
		default:
			dataSize = 100
			value = 500
			sensitivity = "low"
			extractionTime = 15
		}
		detectionRisk = 0.25
	} else {
		dataSize = 0
		value = 0
		sensitivity = "none"
		extractionTime = 0
		detectionRisk = 0.7
	}

	result := &models.DataExtractionResult{
		Success:        success,
		DataType:       req.DataType,
		DataSize:       dataSize,
		Value:          value,
		Sensitivity:    sensitivity,
		DetectionRisk:  detectionRisk,
		ExtractionTime: extractionTime,
	}

	return result, nil
}

// Combat support methods
func (r *Repository) RequestCombatSupport(req models.CombatSupportRequest) (*models.CombatSupportResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	var success bool
	var duration int
	var affectedTargets int
	var detectionRisk float64

	baseSuccess := float64(req.SkillLevel) * 0.4
	success = baseSuccess > 0.3

	if success {
		switch req.SupportType {
		case "recon":
			duration = 30
			affectedTargets = 1
		case "firewall":
			duration = 60
			affectedTargets = len(req.TargetIDs)
		case "decoy":
			duration = 45
			affectedTargets = 3
		case "overload":
			duration = 20
			affectedTargets = 1
		default:
			duration = 30
			affectedTargets = 1
		}
		detectionRisk = 0.15
	} else {
		duration = 0
		affectedTargets = 0
		detectionRisk = 0.05
	}

	result := &models.CombatSupportResult{
		Success:        success,
		SupportType:    req.SupportType,
		EffectArea:     req.Position,
		Duration:       duration,
		AffectedTargets: affectedTargets,
		DetectionRisk:  detectionRisk,
	}

	return result, nil
}

// Anti-cheat validation methods
func (r *Repository) ValidateHackingAttempt(req models.HackingValidationRequest) (*models.HackingValidationResult, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Simplified anti-cheat validation
	valid := true
	confidence := 0.95
	anomalyScore := 0.05
	var flags []string

	// Check for obvious anomalies (simplified)
	if req.SkillLevel > 10 {
		valid = false
		anomalyScore = 0.9
		flags = append(flags, "skill_level_anomaly")
		confidence = 0.1
	}

	if req.Timestamp < time.Now().Add(-time.Hour).Unix() {
		valid = false
		anomalyScore = 0.8
		flags = append(flags, "timestamp_anomaly")
		confidence = 0.2
	}

	result := &models.HackingValidationResult{
		Valid:        valid,
		PlayerID:     req.PlayerID,
		ActionType:   req.ActionType,
		Confidence:   confidence,
		AnomalyScore: anomalyScore,
		Flags:        flags,
	}

	return result, nil
}

// Active hacks management methods
func (r *Repository) GetActiveHacks(playerID string) (*models.ActiveHacksResponse, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	var activeHacks []models.ActiveHack

	// Collect active hacks for player
	for _, zone := range r.blindZones {
		// Check if this blind zone belongs to the player (simplified check)
		if true { // In real implementation, check ownership
			timeRemaining := zone.Duration - int(time.Now().Unix()-zone.CreatedAt)
			if timeRemaining > 0 {
				activeHacks = append(activeHacks, models.ActiveHack{
					HackID:        zone.ID,
					HackType:      "blind_zone",
					TargetID:      "",
					Status:        "active",
					Duration:      zone.Duration,
					TimeRemaining: timeRemaining,
					Position:      zone.Position,
				})
			}
		}
	}

	for _, phantom := range r.phantomEntities {
		if phantom.PlayerID == playerID {
			timeRemaining := phantom.Duration - int(time.Now().Unix()-phantom.CreatedAt)
			if timeRemaining > 0 {
				activeHacks = append(activeHacks, models.ActiveHack{
					HackID:        phantom.ID,
					HackType:      "phantom",
					TargetID:      "",
					Status:        "active",
					Duration:      phantom.Duration,
					TimeRemaining: timeRemaining,
					Position:      phantom.Position,
				})
			}
		}
	}

	result := &models.ActiveHacksResponse{
		PlayerID:    playerID,
		ActiveHacks: activeHacks,
		TotalCount:  len(activeHacks),
		LastUpdated: time.Now().Unix(),
	}

	return result, nil
}

func (r *Repository) CancelActiveHack(hackID string) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	// Try to cancel blind zone
	if zone, exists := r.blindZones[hackID]; exists {
		delete(r.blindZones, hackID)
		// In real implementation, notify affected clients
		return nil
	}

	// Try to cancel phantom
	if phantom, exists := r.phantomEntities[hackID]; exists {
		delete(r.phantomEntities, hackID)
		// In real implementation, notify affected clients
		return nil
	}

	return errors.New("hack not found or already expired")
}

// generateID generates a simple ID (in production, use proper UUID)
func generateID() string {
	return time.Now().Format("20060102150405") + string(rune(time.Now().Nanosecond()))
}

// BACKEND NOTE: Repository uses RWMutex for concurrent access.
// Blind zones, phantoms, and other entities are cleaned up automatically after duration expires.
// All methods are thread-safe and optimized for MMOFPS performance requirements.

// Issue: #143875347, #143875814, #143875915, #143875916, #143875917, #143875918, #143875919, #143875920

