package models

import (
	"testing"
	"time"

	"github.com/google/uuid"
)

func TestCharacterPositionFields(t *testing.T) {
	id := uuid.New()
	now := time.Now()

	pos := CharacterPosition{
		ID:          id,
		CharacterID: id,
		PositionX:   1.0,
		PositionY:   2.0,
		PositionZ:   3.0,
		Yaw:         90.0,
		VelocityX:   0.1,
		VelocityY:   0.2,
		VelocityZ:   0.3,
		UpdatedAt:   now,
		CreatedAt:   now,
	}

	if pos.ID != id {
		t.Fatalf("unexpected ID: %v", pos.ID)
	}
	if pos.PositionZ != 3.0 {
		t.Fatalf("unexpected PositionZ: %v", pos.PositionZ)
	}
	if pos.Yaw != 90.0 {
		t.Fatalf("unexpected Yaw: %v", pos.Yaw)
	}
}

func TestSavePositionRequestFields(t *testing.T) {
	req := SavePositionRequest{
		PositionX: 1,
		PositionY: 2,
		PositionZ: 3,
		Yaw:       45,
		VelocityX: 0.5,
		VelocityY: 0.6,
		VelocityZ: 0.7,
	}

	if req.PositionX != 1 || req.Yaw != 45 {
		t.Fatalf("unexpected values: %+v", req)
	}
}




