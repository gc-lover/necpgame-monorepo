// Issue: #1604
package server

// Service interface for loot business logic
type Service interface {
	// TODO: Implement service methods
}

// LootService implements Service
type LootService struct{}

// NewLootService creates new service
func NewLootService() Service {
	return &LootService{}
}
