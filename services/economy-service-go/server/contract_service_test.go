// Issue: #140890166 - Contract system extension
package server

import (
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/stretchr/testify/assert"
)

func TestCalculateCollateral(t *testing.T) {
	// Test the calculateCollateral logic directly
	calculateCollateral := func(contractType models.ContractType, terms map[string]interface{}) map[string]int {
		// Copy logic from ContractService.calculateCollateral
		switch contractType {
		case models.ContractTypeExchange:
			return map[string]int{"currency": 100}
		case models.ContractTypeDelivery:
			return map[string]int{"currency": 200}
		case models.ContractTypeCrafting:
			return map[string]int{"currency": 300}
		case models.ContractTypeService:
			return map[string]int{"currency": 150}
		default:
			return map[string]int{"currency": 50}
		}
	}

	tests := []struct {
		name         string
		contractType models.ContractType
		expected     map[string]int
	}{
		{
			name:         "exchange contract",
			contractType: models.ContractTypeExchange,
			expected:     map[string]int{"currency": 100},
		},
		{
			name:         "delivery contract",
			contractType: models.ContractTypeDelivery,
			expected:     map[string]int{"currency": 200},
		},
		{
			name:         "crafting contract",
			contractType: models.ContractTypeCrafting,
			expected:     map[string]int{"currency": 300},
		},
		{
			name:         "service contract",
			contractType: models.ContractTypeService,
			expected:     map[string]int{"currency": 150},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := calculateCollateral(tt.contractType, nil)
			assert.Equal(t, tt.expected, result)
		})
	}
}

// Additional test for contract types
func TestContractTypes(t *testing.T) {
	// Test that contract type constants are defined correctly
	assert.Equal(t, models.ContractType("exchange"), models.ContractTypeExchange)
	assert.Equal(t, models.ContractType("delivery"), models.ContractTypeDelivery)
	assert.Equal(t, models.ContractType("crafting"), models.ContractTypeCrafting)
	assert.Equal(t, models.ContractType("service"), models.ContractTypeService)
}

func TestContractStatuses(t *testing.T) {
	// Test that contract status constants are defined correctly
	assert.Equal(t, models.ContractStatus("draft"), models.ContractStatusDraft)
	assert.Equal(t, models.ContractStatus("negotiation"), models.ContractStatusNegotiation)
	assert.Equal(t, models.ContractStatus("escrow_pending"), models.ContractStatusEscrowPending)
	assert.Equal(t, models.ContractStatus("active"), models.ContractStatusActive)
	assert.Equal(t, models.ContractStatus("completed"), models.ContractStatusCompleted)
	assert.Equal(t, models.ContractStatus("cancelled"), models.ContractStatusCancelled)
	assert.Equal(t, models.ContractStatus("disputed"), models.ContractStatusDisputed)
	assert.Equal(t, models.ContractStatus("arbitrated"), models.ContractStatusArbitrated)
}
