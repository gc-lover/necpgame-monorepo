# GraphQL Federation for NECPGAME

## Issue: #2038
**API Designer** - GraphQL federation for microservice integration

## Overview

This directory contains the complete Apollo Federation 2.0 implementation for NECPGAME MMOFPS RPG, providing unified GraphQL API across 50+ microservices with enterprise-grade features.

## Architecture

```
proto/graphql-federation/
├── gateway/                    # Apollo Gateway configuration
│   ├── federated-schema.graphql    # Main composed schema
│   └── gateway-implementation.graphql  # Federation 2.0 setup
├── subgraphs/                 # Individual service schemas
│   ├── social/                # Social domain subgraph
│   ├── economy/               # Economy domain subgraph
│   ├── specialized/           # Combat/crafting subgraph
│   └── system/                # Infrastructure subgraph
├── entities/                  # Entity resolution rules
│   └── entity-resolution.graphql
└── shared/                    # Common types and utilities
    └── common-types.graphql
```

## Key Features

### 🚀 Enterprise-Grade Federation
- **Apollo Federation 2.0** with advanced composition
- **50+ microservices** unified under single GraphQL API
- **Cross-service queries** with automatic entity resolution
- **Real-time subscriptions** via WebSocket integration

### 🔧 Performance Optimizations
- **Smart entity batching** reduces N+1 query problems
- **Reference resolution caching** for frequently accessed entities
- **Lazy loading** for optional entity fields
- **Query complexity limits** prevent abuse

### 🛡️ Reliability Features
- **Circuit breaker integration** for resilient queries
- **Rate limiting** at gateway level
- **Health checks** across all subgraphs
- **Fallback mechanisms** for degraded services

### 📊 Monitoring & Observability
- **Distributed tracing** with Jaeger integration
- **Metrics collection** via Prometheus
- **Query analytics** and performance monitoring
- **Error tracking** and alerting

## Subgraph Ownership

### Auth Subgraph (`auth-expansion-domain`)
- **Primary Entities**: User, UserProfile, UserStats, UserSession
- **Responsibilities**: Authentication, user management, sessions

### Social Subgraph (`social-domain`)
- **Primary Entities**: Guild, Territory, Friendship, Party
- **Responsibilities**: Guilds, territories, social relationships, parties

### Economy Subgraph (`economy-domain`)
- **Primary Entities**: Item, MarketListing, Auction, Currency
- **Responsibilities**: Trading, auctions, crafting, currencies

### Specialized Subgraph (`specialized-domain`)
- **Primary Entities**: CombatSession, CraftingSkill, StatusEffect
- **Responsibilities**: Combat, crafting, effects, game mechanics

### System Subgraph (`system-domain`)
- **Primary Entities**: WebSocketConnection, CircuitBreaker, RateLimiter
- **Responsibilities**: Infrastructure, monitoring, real-time features

### World Subgraph (`world-domain`)
- **Primary Entities**: World, Region
- **Responsibilities**: Game world, regions, locations

## Entity Resolution

Entities are resolved across subgraphs using these patterns:

### Key-Based Resolution
```graphql
type User @key(fields: "id") @key(fields: "email") {
  id: ID!
  email: String!
  # ... other fields from auth subgraph
}
```

### Reference Resolution
```graphql
type GuildMember @key(fields: "userId guildId") {
  userId: ID!
  guildId: ID!
  user: User! @requires(fields: "userId")  # Resolved from auth
  # ... other fields from social subgraph
}
```

### Extension Pattern
```graphql
# In economy subgraph
extend type User @key(fields: "id") {
  id: ID! @external
  inventory: [InventoryItem!]!
  currencyBalances: [CurrencyBalance!]!
}
```

## Query Examples

### Cross-Service Player Dashboard
```graphql
query GetPlayerDashboard($playerId: ID!) {
  playerDashboard(playerId: $playerId) {
    user {
      id
      username
      profile {
        level
        experience
      }
    }
    guild {
      name
      level
      reputation
    }
    inventory {
      item {
        name
        rarity
      }
      quantity
    }
    combatStats {
      totalKills
      winRate
    }
    currencyBalances {
      currency {
        name
        symbol
      }
      balance
    }
  }
}
```

### Real-time Game Events
```graphql
subscription GameEvents($playerId: ID!) {
  gameEvents(playerId: $playerId) {
    type
    description
    players {
      username
    }
    location {
      x
      y
      z
    }
  }
}
```

## Performance Characteristics

### Target Metrics
- **Query Latency**: P99 <100ms for simple queries
- **Entity Resolution**: <50ms for complex entity graphs
- **Throughput**: 1000+ queries/second
- **Memory Usage**: <500MB per gateway instance
- **Cache Hit Rate**: >95% for entity resolution

### Optimization Strategies
1. **Entity Batching**: Multiple entity references resolved in single request
2. **Query Planning**: Optimal execution order for cross-service queries
3. **Result Caching**: Redis-backed caching for frequently accessed data
4. **Connection Pooling**: Efficient database and service connections

## Deployment

### Gateway Setup
```yaml
# docker-compose.yml
version: '3.8'
services:
  apollo-gateway:
    image: apollo/gateway:2.0
    environment:
      - APOLLO_SCHEMA_CONFIG_DELIVERY_ENDPOINT=http://config-server:4000
    ports:
      - "4000:4000"
```

### Subgraph Configuration
Each subgraph exposes its schema via Apollo Federation:

```javascript
const { ApolloServer, gql } = require('apollo-server');
const { buildFederatedSchema } = require('@apollo/federation');

const server = new ApolloServer({
  schema: buildFederatedSchema([typeDefs, resolvers]),
  // Federation 2.0 configuration
  federation: {
    version: '2.0',
    directives: ['@key', '@requires', '@provides', '@external']
  }
});
```

## Development Workflow

### 1. Schema Design
- Design entities with clear ownership
- Define @key directives for federation
- Plan cross-service relationships

### 2. Implementation
- Implement resolvers in owning subgraphs
- Add @requires/@provides for extensions
- Test entity resolution locally

### 3. Integration Testing
- Deploy all subgraphs locally
- Test cross-service queries
- Validate entity resolution
- Performance testing

### 4. Production Deployment
- Rolling deployment of subgraphs
- Gateway schema composition
- Monitoring and alerting setup

## Monitoring

### Key Metrics to Monitor
- **Query Latency** by operation type
- **Error Rates** per subgraph
- **Entity Resolution Time**
- **Cache Hit Rates**
- **Active Subscriptions**

### Health Checks
- Subgraph availability
- Schema validation
- Entity resolution integrity
- Performance regression detection

## Troubleshooting

### Common Issues

1. **Entity Resolution Failures**
   - Check @key directives match
   - Verify subgraph connectivity
   - Review entity ownership

2. **Query Performance**
   - Enable query complexity limits
   - Implement result caching
   - Optimize resolver batching

3. **Schema Composition Errors**
   - Validate all subgraph schemas
   - Check Federation 2.0 compatibility
   - Review entity extensions

## Future Enhancements

### Planned Features
- **Schema Stitching Migration** from Federation 1.0
- **Advanced Caching** with Apollo Cache Control
- **Query Persisting** for performance
- **Schema Registry** integration
- **Advanced Analytics** dashboard

### Scaling Considerations
- **Subgraph Splitting** for high-traffic services
- **Regional Gateways** for global distribution
- **Query Result Batching** for mobile clients
- **Advanced Routing** based on user location

## Related Documentation

- [Apollo Federation Documentation](https://www.apollographql.com/docs/federation/)
- [GraphQL Best Practices](https://graphql.org/learn/best-practices/)
- `proto/openapi/` - REST API specifications
- `infrastructure/` - Deployment configurations

---

**Ready for Backend Implementation:** This GraphQL federation provides a solid foundation for unified API access across all NECPGAME microservices, enabling complex cross-service queries while maintaining performance and reliability.
