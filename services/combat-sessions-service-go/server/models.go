// Issue: #130

package server

import "time"

// CombatSession internal model
type CombatSession struct {
	ExpiresAt       time.Time
	CreatedAt       time.Time
	EndedAt         time.Time
	StartedAt       time.Time
	Settings        map[string]interface{}
	CurrentTurn     string
	ZoneID          string
	Status          string
	SessionType     string
	ID              string
	WinnerTeam      string
	MaxParticipants int
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
	Status      string
	MaxHealth   int
	Health      int
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
	Timestamp      time.Time
	ActionData     map[string]interface{}
	ResultData     map[string]interface{}
	SessionID      string
	EventType      string
	ActorID        string
	TargetID       string
	ID             int64
	SequenceNumber int64
}

















