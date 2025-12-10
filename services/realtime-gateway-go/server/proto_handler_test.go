// Issue: #1580 - UDP/Protobuf path tests
package server

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"

	pb "github.com/necpgame/realtime-gateway-go/pkg/proto"
	"google.golang.org/protobuf/proto"
)

func TestQuantizeCoordinate_RoundTrip(t *testing.T) {
	vals := []float32{0, 1.234, -2.5, 1234.567}
	for _, v := range vals {
		q := QuantizeCoordinate(v)
		d := DequantizeCoordinate(q)
		assert.InDelta(t, v, d, 0.001, "round-trip should stay within 1mm")
	}
}

func TestParseClientMessage_PlayerInput(t *testing.T) {
	raw := &pb.ClientMessage{
		Payload: &pb.ClientMessage_PlayerInput{
			PlayerInput: &pb.PlayerInput{
				PlayerId: "p1",
				Tick:     42,
				MoveX:    0.5,
				MoveY:    -0.25,
				Shoot:    true,
				AimX:     1.0,
				AimY:     2.0,
			},
		},
	}
	data, err := proto.Marshal(raw)
	require.NoError(t, err)

	input, err := ParseClientMessage(data)
	require.NoError(t, err)
	require.NotNil(t, input)

	assert.Equal(t, "p1", input.PlayerID)
	assert.Equal(t, int64(42), input.Tick)
	assert.NotZero(t, input.MoveX)
	assert.NotZero(t, input.MoveY)
	assert.True(t, input.Shoot)
	assert.NotZero(t, input.AimX)
	assert.NotZero(t, input.AimY)
}

func TestParseClientMessage_EmptyPayload(t *testing.T) {
	_, err := ParseClientMessage(nil)
	assert.Error(t, err)
}

func TestBuildAndParseGameStateMessage(t *testing.T) {
	state := &GameStateData{
		Tick: 7,
		Entities: []EntityState{
			{ID: "a", X: 1000, Y: -2000, Z: 0},
			{ID: "b", X: 0, Y: 0, Z: 500},
		},
	}

	data, err := BuildGameStateMessage(state)
	require.NoError(t, err)
	require.NotEmpty(t, data)

	parsed, err := ParseGameStateMessage(data)
	require.NoError(t, err)
	require.NotNil(t, parsed)
	assert.Equal(t, state.Tick, parsed.Tick)
	assert.Len(t, parsed.Entities, 2)
	assert.Equal(t, state.Entities[0].ID, parsed.Entities[0].ID)
}

func TestBuildGameStateMessage_NilState(t *testing.T) {
	_, err := BuildGameStateMessage(nil)
	assert.Error(t, err)
}

func TestParseGameStateMessage_Invalid(t *testing.T) {
	_, err := ParseGameStateMessage([]byte{0x01, 0x02})
	assert.Error(t, err)
}

