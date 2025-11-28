package server

type PlayerInputData struct {
	PlayerID string
	Tick     int64
	MoveX    int32
	MoveY    int32
	Shoot    bool
	AimX     int32
	AimY     int32
}

type EntityState struct {
	ID  string
	X   int32
	Y   int32
	Z   int32
	VX  int32
	VY  int32
	VZ  int32
	Yaw int32
}

type GameStateData struct {
	Tick     int64
	Entities []EntityState
}

