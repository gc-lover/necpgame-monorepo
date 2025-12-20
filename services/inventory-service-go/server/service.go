package server

import (
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1950 - Memory-aligned struct for performance
type InventoryService struct {
	logger       *logrus.Logger
	metrics      *InventoryMetrics
	activeOps    sync.Map // OPTIMIZATION: Thread-safe map for concurrent operations

	// OPTIMIZATION: Issue #1949 - Memory pooling for hot path structs (zero allocations target!)
	getInventoryResponsePool sync.Pool
	listItemsResponsePool    sync.Pool
	moveItemResponsePool     sync.Pool
	equipItemResponsePool    sync.Pool
	unequipItemResponsePool  sync.Pool
	useItemResponsePool      sync.Pool
	dropItemResponsePool     sync.Pool
	getItemResponsePool      sync.Pool
	searchItemsResponsePool  sync.Pool
	getEquipmentResponsePool sync.Pool
	equipmentStatsResponsePool sync.Pool
}

func NewInventoryService(logger *logrus.Logger, metrics *InventoryMetrics) *InventoryService {
	s := &InventoryService{
		logger:  logger,
		metrics: metrics,
	}

	// OPTIMIZATION: Issue #1949 - Initialize memory pools (zero allocations target!)
	s.getInventoryResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetInventoryResponse{}
		},
	}
	s.listItemsResponsePool = sync.Pool{
		New: func() interface{} {
			return &ListItemsResponse{}
		},
	}
	s.moveItemResponsePool = sync.Pool{
		New: func() interface{} {
			return &MoveItemResponse{}
		},
	}
	s.equipItemResponsePool = sync.Pool{
		New: func() interface{} {
			return &EquipItemResponse{}
		},
	}
	s.unequipItemResponsePool = sync.Pool{
		New: func() interface{} {
			return &UnequipItemResponse{}
		},
	}
	s.useItemResponsePool = sync.Pool{
		New: func() interface{} {
			return &UseItemResponse{}
		},
	}
	s.dropItemResponsePool = sync.Pool{
		New: func() interface{} {
			return &DropItemResponse{}
		},
	}
	s.getItemResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetItemResponse{}
		},
	}
	s.searchItemsResponsePool = sync.Pool{
		New: func() interface{} {
			return &SearchItemsResponse{}
		},
	}
	s.getEquipmentResponsePool = sync.Pool{
		New: func() interface{} {
			return &GetEquipmentResponse{}
		},
	}
	s.equipmentStatsResponsePool = sync.Pool{
		New: func() interface{} {
			return &EquipmentStatsResponse{}
		},
	}

	return s
}