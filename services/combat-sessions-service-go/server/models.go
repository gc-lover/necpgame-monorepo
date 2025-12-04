// Issue: #130

package server

import "time"

// CombatSession internal model
type CombatSession struct {
	ID              string
	SessionType     string
	ZoneID          string
	Status          string
	MaxParticipants int
	Settings        map[string]interface{}
	CreatedAt       time.Time
	StartedAt       time.Time
	EndedAt         time.Time
	ExpiresAt       time.Time
	WinnerTeam      string
	CurrentTurn     string
	TurnNumber      int
	NextSequence    int64
}

// CombatParticipant internal model
type CombatParticipant struct {
	PlayerID    string
	CharacterID string
	SessionID   string
	Team        string
	Role        string
	Health      int
	MaxHealth   int
	Status      string
	DamageDealt int64
	DamageTaken int64
	Kills       int
	Deaths      int
	Assists     int
	ShotsFired  int
	ShotsHit    int
}

// CombatLog internal model
type CombatLog struct {
	ID             int64
	SessionID      string
	EventType      string
	ActorID        string
	TargetID       string
	ActionData     map[string]interface{}
	ResultData     map[string]interface{}
	Timestamp      time.Time
	SequenceNumber int64
}






