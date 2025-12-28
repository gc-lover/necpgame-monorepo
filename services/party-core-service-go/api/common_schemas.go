package api

import "time"

// Error представляет структуру ошибки
type Error struct {
	Message   string `json:"message"`
	Code      int    `json:"code"`
	Timestamp time.Time `json:"timestamp,omitempty"`
	Domain    string `json:"domain,omitempty"`
}

// SuccessResponse представляет успешный ответ
type SuccessResponse struct {
	Success   bool        `json:"success"`
	Message   string      `json:"message"`
	Data      interface{} `json:"data,omitempty"`
	Timestamp time.Time   `json:"timestamp,omitempty"`
}

// Position3D представляет 3D позицию
type Position3D struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// Direction3D представляет 3D направление
type Direction3D struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}
