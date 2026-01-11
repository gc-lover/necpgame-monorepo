# Economy Service Database Migrations

This directory contains database migrations for the Economy Service, implementing enterprise-grade market mechanics for the Night City universe.

## Overview

The Economy Service manages complex economic systems including:
- Dynamic market pricing with BazaarBot AI simulation
- Order book management for trading operations
- Auction house functionality
- Currency exchange systems
- Player portfolio and transaction tracking

## Schema Design

### Core Tables

#### `economy.market_prices`
Real-time market pricing data with historical tracking.
- Supports multiple commodities (weapons, armor, implants, vehicles, consumables, materials)
- Price history for trend analysis
- Volume tracking for market depth

#### `economy.trading_orders`
Order book implementation for buy/sell orders.
- Supports limit orders with expiration
- Order matching engine integration
- Player portfolio integration

#### `economy.auctions`
Auction house functionality with bidding mechanics.
- Starting price, bid increments, duration
- Winner determination and settlement
- Item ownership validation

#### `economy.currency_exchanges`
Multi-currency trading and exchange rates.
- Real-time exchange rate calculations
- Transaction history and audit trails
- Currency conversion logic

#### `economy.player_portfolios`
Player economic state management.
- Wealth tracking across currencies
- Inventory integration
- Active order monitoring

#### `economy.transaction_history`
Comprehensive transaction logging.
- All economic activities (trading, auctions, exchanges)
- Audit trails for anti-cheat measures
- Financial reporting capabilities

### Performance Optimizations

- Struct alignment directives (`//go:align 64`) for memory efficiency
- Database indexing for high-frequency queries
- Partitioning for large transaction histories
- Redis caching for market data

### Migration Strategy

Migrations follow semantic versioning:
- `V001`: Initial schema creation
- `V002+`: Incremental updates and optimizations

## Dependencies

- Common infrastructure schemas
- User management tables
- Item/inventory systems

## Testing

Migrations include comprehensive tests for:
- Schema integrity
- Performance benchmarks
- Data consistency
- Rollback procedures

## Performance Targets

- P99 Latency: <25ms for trading operations
- Memory: <15KB per active trading session
- Concurrent users: 5,000+ simultaneous traders
- Market updates: <100ms propagation time
- Transaction throughput: 10,000+ TPS