# Crafting Network Service API

## Overview

The Crafting Network Service provides real-time networking capabilities for the NECPGAME crafting system. This enterprise-grade microservice handles WebSocket connections for crafting session monitoring and UDP protocols for high-frequency crafting state synchronization.

## Key Features

- **Real-Time Progress Monitoring**: WebSocket-based crafting session updates with <5ms latency
- **High-Frequency State Sync**: UDP protocol for compressed crafting progress updates (<1ms latency)
- **Queue Management**: Real-time crafting queue monitoring and notifications
- **Material Availability**: Instant notifications for material changes and availability
- **Network Optimization**: Compressed data structures and delta encoding for efficient bandwidth usage
- **Latency Compensation**: Advanced algorithms to mitigate network lag effects in crafting operations

## Architecture

### Domain Separation
This API follows strict domain separation principles:
- Core crafting logic handled by `crafting-service`
- Real-time networking handled by `crafting-network-service`
- Network infrastructure managed through dedicated UDP/WebSocket endpoints

### Performance Targets
- **WebSocket Latency**: <5ms message delivery
- **UDP Sync Latency**: <1ms state updates
- **Memory per Session**: <10KB active crafting session
- **Concurrent Crafters**: 10,000+ simultaneous sessions
- **Network Bandwidth**: <50KB/s per active session

## API Endpoints

### Health Monitoring
- `GET /health` - Service health check
- `POST /health/batch` - Batch health check for multiple services
- `GET /health/ws` - Real-time health monitoring WebSocket

### Real-Time Crafting
- `GET /ws/crafting/{session_id}` - WebSocket for crafting session monitoring
- `GET /ws/queue/{player_id}` - WebSocket for queue status monitoring

### UDP Synchronization
- `POST /udp/sessions/{session_id}/connect` - Establish UDP connection
- `POST /udp/sessions/{session_id}/progress` - UDP progress synchronization
- `POST /udp/sessions/{session_id}/materials` - UDP material availability sync

## Data Structures

### WebSocket Messages
- `WebSocketCraftingMessage` - Real-time crafting updates
- `WebSocketQueueMessage` - Queue status notifications
- `WebSocketHealthMessage` - Health monitoring updates

### UDP Packets
- `UdpProgressUpdate` - Compressed progress data
- `UdpMaterialUpdate` - Material availability changes
- `UdpPacketHeader` - UDP packet metadata

### Key Schemas
- `CraftingProgressData` - Detailed progress information
- `MaterialNotification` - Material availability alerts
- `QueueStatusData` - Queue position and timing

## Network Protocols

### WebSocket Protocol
- **Compression**: LZ4 for message payloads
- **Reconnection**: Automatic with exponential backoff
- **Heartbeat**: 30-second intervals
- **Authentication**: Bearer token based

### UDP Protocol
- **Reliability Layer**: Best-effort delivery with periodic full sync
- **Compression**: Custom binary protocol with delta encoding
- **Authentication**: Session token validation
- **Packet Size**: Negotiable (64B-64KB)

## Performance Optimizations

### Memory Optimization
- Struct alignment hints for 30-50% memory savings
- Object pooling for connection management
- Compressed data structures for network transmission

### Network Optimization
- Delta encoding for state updates
- Bit-packing for material changes
- Binary protocols for UDP communication
- Connection pooling and reuse

### Latency Mitigation
- Geographic server distribution
- Predictive state synchronization
- Client-side prediction algorithms
- Network condition monitoring

## Integration Points

### Dependencies
- `crafting-service` - Core crafting logic and data
- `real-time-combat-service` - Network patterns reference
- Common schemas and security frameworks

### Clients
- **UE5 Client** - Real-time crafting UI updates
- **Mobile App** - Queue status and notifications
- **Web Dashboard** - Administrative monitoring

## Security Considerations

### Authentication
- Bearer token authentication for WebSocket connections
- Session token validation for UDP connections
- Player authorization verification

### Rate Limiting
- WebSocket connection limits (per player, per session)
- UDP packet rate limiting
- Health check rate limiting

### Data Protection
- Encrypted WebSocket connections (WSS)
- Session token rotation
- Audit logging for security events

## Deployment Considerations

### Infrastructure Requirements
- **WebSocket Servers**: Auto-scaling based on connection count
- **UDP Servers**: Low-latency network infrastructure
- **Load Balancing**: Geographic distribution for reduced latency
- **Monitoring**: Real-time performance metrics and alerting

### Scaling Strategy
- Horizontal scaling for WebSocket connections
- Regional deployment for UDP servers
- Connection pooling for resource optimization
- Auto-scaling based on crafting activity

## Development Guidelines

### Code Generation
- Compatible with ogen for Go code generation
- Struct alignment hints for performance optimization
- Domain separation maintained in generated code

### Testing Strategy
- Unit tests for network protocol handling
- Integration tests for WebSocket/UDP communication
- Performance tests for latency requirements
- Load testing for concurrent user scenarios

### Monitoring and Observability
- Prometheus metrics for performance monitoring
- Distributed tracing for request correlation
- Health check endpoints for service discovery
- Real-time alerting for performance degradation

## Future Enhancements

### Planned Features
- **Advanced Compression**: AI-powered data compression algorithms
- **Predictive Synchronization**: Machine learning-based state prediction
- **Cross-Platform Sync**: Unified state across multiple devices
- **Advanced Analytics**: Crafting behavior analysis and optimization

### Performance Improvements
- **Edge Computing**: Localized crafting processing
- **5G Integration**: Ultra-low latency mobile crafting
- **Quantum-Safe Crypto**: Future-proof security protocols
- **Neural Network Optimization**: AI-driven network optimization

## Issue Tracking

- **API Design**: #CRAFTING-NETWORK-API
- **Network Implementation**: #2204, #2189
- **Performance Optimization**: Ongoing monitoring
- **Security Audits**: Regular reviews

---

*This API specification follows enterprise-grade patterns established in the NECPGAME project, ensuring scalability, performance, and maintainability for a first-class MMOFPS RPG experience.*