import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/sirupsen/logrus"
)

// CreateExchange OPTIMIZATION: Issue #2143 - Exchange and binding management
func (s *MessageQueueService) CreateExchange(w http.ResponseWriter, r *http.Request) {
	var req CreateExchangeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create exchange request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	err := s.rabbitChannel.ExchangeDeclare(
		req.Name,
		req.Type,
		req.Durable,
		req.AutoDelete,
		req.Internal,
		req.NoWait,
		req.Arguments,
	)
	if err != nil {
		s.logger.WithError(err).WithField("exchange_name", req.Name).Error("failed to declare exchange")
		http.Error(w, "Failed to create exchange", http.StatusInternalServerError)
		return
	}

	exchange := &ExchangeInfo{
		Name:       req.Name,
		Type:       req.Type,
		Durable:    req.Durable,
		AutoDelete: req.AutoDelete,
		Internal:   req.Internal,
		CreatedAt:  time.Now(),
	}

	s.exchanges.Store(exchange.Name, exchange)

	resp := &CreateExchangeResponse{
		ExchangeName: exchange.Name,
		ExchangeType: exchange.Type,
		CreatedAt:    exchange.CreatedAt.Unix(),
		BindingCount: 0,
		Settings: &ExchangeSettings{
			Type:       req.Type,
			Durable:    req.Durable,
			AutoDelete: req.AutoDelete,
			Internal:   req.Internal,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("exchange_name", req.Name).Info("exchange created successfully")
}

func (s *MessageQueueService) ListExchanges(w http.ResponseWriter) {
	var exchanges []*ExchangeInfo

	s.exchanges.Range(func(key, value interface{}) bool {
		exchange := value.(*ExchangeInfo)
		exchanges = append(exchanges, exchange)
		return true
	})

	resp := &ListExchangesResponse{
		Exchanges:  exchanges,
		TotalCount: len(exchanges),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *MessageQueueService) CreateBinding(w http.ResponseWriter, r *http.Request) {
	var req CreateBindingRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create binding request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	err := s.rabbitChannel.QueueBind(
		req.QueueName,
		req.RoutingKey,
		req.ExchangeName,
		req.NoWait,
		req.Arguments,
	)
	if err != nil {
		s.logger.WithError(err).WithFields(logrus.Fields{
			"exchange_name": req.ExchangeName,
			"queue_name":    req.QueueName,
		}).Error("failed to bind queue")
		http.Error(w, "Failed to create binding", http.StatusInternalServerError)
		return
	}

	resp := &CreateBindingResponse{
		ExchangeName: req.ExchangeName,
		QueueName:    req.QueueName,
		RoutingKey:   req.RoutingKey,
		CreatedAt:    time.Now().Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"exchange_name": req.ExchangeName,
		"queue_name":    req.QueueName,
	}).Info("binding created successfully")
}
