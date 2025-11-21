package models

import (
	"time"

	"github.com/google/uuid"
)

type CharacterPosition struct {
	ID        uuid.UUID `json:"id" db:"id"`
	CharacterID uuid.UUID `json:"character_id" db:"character_id"`
	PositionX float64   `json:"position_x" db:"position_x"`
	PositionY float64   `json:"position_y" db:"position_y"`
	PositionZ float64   `json:"position_z" db:"position_z"`
	Yaw       float64   `json:"yaw" db:"yaw"`
	VelocityX float64   `json:"velocity_x,omitempty" db:"velocity_x"`
	VelocityY float64   `json:"velocity_y,omitempty" db:"velocity_y"`
	VelocityZ float64   `json:"velocity_z,omitempty" db:"velocity_z"`
	UpdatedAt time.Time `json:"updated_at" db:"updated_at"`
	CreatedAt time.Time `json:"created_at" db:"created_at"`
}

type PositionHistory struct {
	ID        uuid.UUID `json:"id" db:"id"`
	CharacterID uuid.UUID `json:"character_id" db:"character_id"`
	PositionX float64   `json:"position_x" db:"position_x"`
	PositionY float64   `json:"position_y" db:"position_y"`
	PositionZ float64   `json:"position_z" db:"position_z"`
	Yaw       float64   `json:"yaw" db:"yaw"`
	VelocityX float64   `json:"velocity_x,omitempty" db:"velocity_x"`
	VelocityY float64   `json:"velocity_y,omitempty" db:"velocity_y"`
	VelocityZ float64   `json:"velocity_z,omitempty" db:"velocity_z"`
	CreatedAt time.Time `json:"created_at" db:"created_at"`
}

type SavePositionRequest struct {
	PositionX float64 `json:"position_x"`
	PositionY float64 `json:"position_y"`
	PositionZ float64 `json:"position_z"`
	Yaw       float64 `json:"yaw"`
	VelocityX float64 `json:"velocity_x,omitempty"`
	VelocityY float64 `json:"velocity_y,omitempty"`
	VelocityZ float64 `json:"velocity_z,omitempty"`
}

type EntityState struct {
	ID  string  `json:"id"`
	X   float32 `json:"x"`
	Y   float32 `json:"y"`
	Z   float32 `json:"z"`
	VX  float32 `json:"vx"`
	VY  float32 `json:"vy"`
	VZ  float32 `json:"vz"`
	Yaw float32 `json:"yaw"`
}
