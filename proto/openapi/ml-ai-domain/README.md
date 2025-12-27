# ML/AI Domain API Specification

## Overview

**ML/AI Domain** provides enterprise-grade machine learning and artificial intelligence services for NECPGAME. This domain handles model management, real-time predictions, training operations, and analytics for intelligent game features.

## Key Features

### 🔬 **Model Management**
- CRUD operations for ML models
- Version control and model lifecycle
- Multi-algorithm support (neural networks, random forests, etc.)
- Model performance tracking and analytics

### ⚡ **Real-time Predictions**
- Low-latency inference endpoints (<100ms P99)
- Batch prediction support (up to 1000 requests)
- Confidence scoring and result caching
- GPU acceleration support

### 🎯 **Training Operations**
- Asynchronous model training jobs
- Distributed training support
- Hyperparameter optimization
- Training progress monitoring

### 📊 **Analytics & Monitoring**
- Model performance metrics
- Prediction usage statistics
- Error rate tracking
- Resource utilization monitoring

## Performance Targets

### Latency Requirements
- **Prediction endpoints**: P99 < 100ms, P95 < 50ms
- **Model CRUD operations**: P99 < 200ms
- **Analytics queries**: P99 < 500ms

### Throughput Targets
- **Predictions**: 10,000 RPS sustained
- **Model operations**: 1,000 RPS
- **Training jobs**: 100 concurrent jobs

### Resource Optimization
- **Memory alignment**: 30-50% savings on struct layouts
- **Connection pooling**: 25-50 database connections
- **Worker pools**: 25 concurrent ML operations
- **GPU utilization**: 80%+ for inference workloads

## API Endpoints

### Health Monitoring
```http
GET /health           # Basic health check
GET /health/batch     # ML models health status
GET /health/ws        # WebSocket health monitoring
```

### Model Management
```http
GET  /ml/models       # List models with pagination
POST /ml/models       # Create new model
GET  /ml/models/{id}  # Get model details
PUT  /ml/models/{id}  # Update model
DEL  /ml/models/{id}  # Delete model
```

### Predictions
```http
POST /ml/predict      # Single prediction
POST /ml/predict/batch # Batch predictions
```

### Training
```http
POST /ml/train               # Start training job
GET  /ml/train/{jobId}/status # Training progress
```

### Analytics
```http
GET /ml/analytics/models     # Model performance
GET /ml/analytics/predictions # Prediction usage
```

## Schema Design

### Struct Alignment Optimization

All schemas follow the **large → small** field ordering principle for optimal memory usage:

```yaml
# CORRECT: Large fields first
properties:
  model_id:    # string (16 bytes)
  name:        # string (16 bytes)
  metadata:    # object (8 bytes)
  accuracy:    # float64 (8 bytes)
  version:     # string (16 bytes)
  is_active:   # boolean (1 byte) - LAST
```

### Key Schemas

#### MLModel
Core model entity with versioning and performance metrics.

#### PredictionRequest/Response
Real-time inference with confidence scoring.

#### TrainingJob
Asynchronous training operations with progress tracking.

#### ModelAnalytics
Performance monitoring and usage statistics.

## Security

### Authentication
- JWT Bearer tokens required for all operations
- Role-based access control (admin/user roles)

### Authorization
- Model owners can modify their models
- Read-only access for prediction endpoints
- Admin access for training and analytics

### Rate Limiting
- Prediction endpoints: 1000 req/min per user
- Model operations: 100 req/min per user
- Training jobs: 10 jobs/hour per user

## Error Handling

### Standard Error Codes
- `MODEL_NOT_FOUND`: Model doesn't exist
- `INVALID_FEATURES`: Bad prediction input
- `TRAINING_FAILED`: Training job error
- `RATE_LIMIT_EXCEEDED`: Too many requests
- `INSUFFICIENT_PERMISSIONS`: Access denied

### Error Response Format
```json
{
  "message": "Human-readable error message",
  "code": "MACHINE_READABLE_CODE",
  "details": {
    "field": "specific_field",
    "constraint": "validation_rule"
  },
  "timestamp": "2025-12-27T12:00:00Z"
}
```

## Implementation Notes

### Backend Optimizations
- **Memory pooling** for prediction requests
- **Prepared statements** for hot database queries
- **Worker pools** for concurrent ML operations
- **Result caching** for frequent predictions
- **Async processing** for training jobs

### Database Design
- **Partitioning** by model type and time
- **Indexing** on frequently queried fields
- **Archiving** of old prediction data
- **Replication** for read-heavy workloads

### Monitoring
- **Prometheus metrics** for all endpoints
- **Distributed tracing** with Jaeger
- **Log aggregation** with structured logging
- **Alerting** on performance degradation

## Development Workflow

### 1. API Design
```bash
# Validate OpenAPI specification
npx @redocly/cli lint proto/openapi/ml-ai-domain/main.yaml

# Bundle for code generation
npx @redocly/cli bundle proto/openapi/ml-ai-domain/main.yaml -o bundled.yaml
```

### 2. Code Generation
```bash
# Generate Go server code
ogen --target ml-ai-service --package api --clean bundled.yaml
```

### 3. Implementation
```bash
cd ml-ai-service
go mod tidy
go build .
```

### 4. Testing
```bash
# Unit tests
go test ./...

# Integration tests
go test -tags=integration ./...

# Performance tests
go test -bench=. -benchmem ./...
```

## Related Systems

### Dependencies
- **Narrative Service**: ML-powered story generation
- **Economy Service**: Predictive market analysis
- **Social Service**: User behavior modeling
- **Combat Service**: AI opponent behavior

### Data Sources
- **Game telemetry**: Player behavior data
- **Market data**: Economic trends and patterns
- **Social graphs**: Relationship network analysis
- **Combat logs**: Strategy and tactic patterns

## Future Enhancements

### Phase 2 Features
- **Federated Learning**: Distributed model training
- **AutoML**: Automatic model selection and tuning
- **Edge Computing**: On-device inference
- **Reinforcement Learning**: Dynamic game balancing

### Performance Improvements
- **Model quantization**: Reduced memory footprint
- **ONNX Runtime**: Cross-platform inference
- **Kubernetes GPU scheduling**: Optimal resource utilization
- **Prediction caching**: Redis-based result caching

---

## Contact

**ML/AI Team**
- Email: ml@necp.game
- Slack: #ml-ai-services
- Documentation: [Internal Wiki](https://wiki.necp.game/ml-ai)

---

*This specification follows NECPGAME's enterprise-grade API design standards with performance optimizations and security best practices.*
