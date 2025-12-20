import (
"encoding/json"
"net/http"
"time"

"github.com/go-chi/chi/v5"
)

type RegisterConsumerRequest

// RegisterConsumer OPTIMIZATION: Issue #2143 - Consumer management and monitoring
func (s *MessageQueueService) RegisterConsumer(w http.ResponseWriter, r *http.Request) {
var req RegisterConsumerRequest
if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
s.logger.WithError(err).Error("failed to decode register consumer request")
http.Error(w, "Invalid request body", http.StatusBadRequest)
return
}

consumer := &MessageConsumer{
ConsumerID:    req.ConsumerID,
ConsumerTag:   req.ConsumerTag,
QueueName:     req.QueueName,
PrefetchCount: req.PrefetchCount,
AutoAck:       req.AutoAck,
Exclusive:     req.Exclusive,
Status:        "active",
ConnectedAt:   time.Now(),
LastHeartbeat: time.Now(),
MessageCount:  0,
ErrorCount:    0,
Arguments:     req.Arguments,
}

s.consumers.Store(consumer.ConsumerID, consumer)
s.metrics.ActiveConsumers.Inc()

resp := &RegisterConsumerResponse{
ConsumerID:   consumer.ConsumerID,
ConsumerTag:  consumer.ConsumerTag,
QueueName:    consumer.QueueName,
RegisteredAt: consumer.ConnectedAt.Unix(),
Settings: &ConsumerSettings{
PrefetchCount: consumer.PrefetchCount,
AutoAck:       consumer.AutoAck,
Exclusive:     consumer.Exclusive,
Status:        consumer.Status,
},
}

w.Header().Set("Content-Type", "application/json")
w.WriteHeader(http.StatusCreated)
json.NewEncoder(w).Encode(resp)

s.logger.WithField("consumer_id", consumer.ConsumerID).Info("consumer registered successfully")
}

func (s *MessageQueueService) ListConsumers(w http.ResponseWriter, r *http.Request) {
var consumers []*ConsumerInfo

s.consumers.Range(func (key, value interface{}) bool {
consumer := value.(*MessageConsumer)

queueFilter := r.URL.Query().Get("queue")
statusFilter := r.URL.Query().Get("status")

if queueFilter != "" && consumer.QueueName != queueFilter {
return true
}
if statusFilter != "" && consumer.Status != statusFilter {
return true
}

info := &ConsumerInfo{
ConsumerID:    consumer.ConsumerID,
ConsumerTag:   consumer.ConsumerTag,
QueueName:     consumer.QueueName,
Status:        consumer.Status,
ConnectedAt:   consumer.ConnectedAt.Unix(),
PrefetchCount: consumer.PrefetchCount,
AckCount:      int(consumer.MessageCount),
NackCount:     int(consumer.ErrorCount),
}
consumers = append(consumers, info)
return true
})

resp := &ListConsumersResponse{
Consumers:  consumers,
TotalCount: len(consumers),
}

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)
}

func (s *MessageQueueService) UpdateConsumer(w http.ResponseWriter, r *http.Request) {
consumerID := chi.URLParam(r, "consumerId")

var req UpdateConsumerRequest
if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
s.logger.WithError(err).Error("failed to decode update consumer request")
http.Error(w, "Invalid request body", http.StatusBadRequest)
return
}

consumerValue, exists := s.consumers.Load(consumerID)
if !exists {
http.Error(w, "Consumer not found", http.StatusNotFound)
return
}

consumer := consumerValue.(*MessageConsumer)

// Update fields
if req.PrefetchCount > 0 {
consumer.PrefetchCount = req.PrefetchCount
}
if req.Status != "" {
consumer.Status = req.Status
if req.Status == "active" {
s.metrics.ActiveConsumers.Inc()
} else {
s.metrics.ActiveConsumers.Dec()
}
}
if req.Arguments != nil {
consumer.Arguments = req.Arguments
}

resp := &UpdateConsumerResponse{
ConsumerID:    consumer.ConsumerID,
UpdatedFields: []string{"prefetch_count", "status"}, // Would be dynamic based on actual updates
UpdatedAt:     time.Now().Unix(),
Settings: &ConsumerSettings{
PrefetchCount: consumer.PrefetchCount,
AutoAck:       consumer.AutoAck,
Exclusive:     consumer.Exclusive,
Status:        consumer.Status,
},
}

w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(resp)

s.logger.WithField("consumer_id", consumerID).Info("consumer updated successfully")
}

func (s *MessageQueueService) UnregisterConsumer(w http.ResponseWriter, r *http.Request) {
consumerID := chi.URLParam(r, "consumerId")

consumerValue, exists := s.consumers.Load(consumerID)
if !exists {
http.Error(w, "Consumer not found", http.StatusNotFound)
return
}

consumer := consumerValue.(*MessageConsumer)

// Cancel consumer (would need to store delivery channel to properly cancel)
// For now, just mark as inactive
consumer.Status = "inactive"

s.consumers.Delete(consumerID)
s.metrics.ActiveConsumers.Dec()

w.WriteHeader(http.StatusNoContent)

s.logger.WithField("consumer_id", consumerID).Info("consumer unregistered successfully")
}
