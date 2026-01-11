# Economy Service API

## Overview

The Economy Service provides comprehensive economic simulation capabilities for the NECPGAME. This enterprise-grade microservice handles market mechanics, trading systems, auction houses, and currency exchange in the Night City universe.

## Key Features

- **Market Mechanics**: Real-time pricing and supply/demand simulation
- **Trading Systems**: Order management with buy/sell orders and matching
- **Auction House**: Competitive bidding system with time-based auctions
- **Currency Exchange**: Multi-currency trading and conversion
- **Economic Events**: Market volatility and economic notifications
- **Performance Optimized**: MMOFPS-grade performance with <25ms P99 latency

## Architecture

### Domain Separation
This API follows strict domain separation principles:
- Core economic logic handled by `economy-service`
- Market data and trading operations
- Auction management and bidding mechanics
- Currency exchange and wallet operations

### Performance Targets
- **P99 Latency**: <25ms for trading operations
- **Memory per Session**: <15KB active trading session
- **Concurrent Traders**: 5,000+ simultaneous users
- **Market Updates**: <100ms propagation time
- **Transaction Throughput**: 10,000+ TPS

## API Endpoints

### Health Monitoring
- `GET /health` - Service health check
- `POST /health/batch` - Batch health check for multiple services

### Market Data
- `GET /market/prices` - Real-time market prices with filtering

### Trading Operations
- `GET /trading/orders` - List user's trading orders
- `POST /trading/orders` - Create new trading order
- `GET /trading/orders/{order_id}` - Get order details
- `DELETE /trading/orders/{order_id}` - Cancel trading order

### Auction House
- `GET /auctions` - List active auctions
- `POST /auctions` - Create new auction
- `POST /auctions/{auction_id}/bid` - Place bid on auction

### Currency Operations
- `POST /currency/exchange` - Exchange between currencies
- `GET /wallet/transactions` - Get transaction history

## Data Structures

### Market Data
- `MarketPrice` - Real-time pricing information
- `MarketPricesResponse` - Paginated market data

### Trading
- `TradingOrder` - Buy/sell order details
- `CreateTradingOrderRequest` - Order creation parameters
- `TradingOrdersResponse` - Paginated order list

### Auctions
- `Auction` - Auction details and current state
- `CreateAuctionRequest` - Auction creation parameters
- `PlaceBidRequest` - Bid placement parameters

### Currency
- `CurrencyExchangeRequest` - Exchange parameters
- `CurrencyExchangeResponse` - Exchange results
- `WalletTransaction` - Transaction history record

## Economic Models

### Market Pricing
- **Dynamic Pricing**: Supply/demand based price adjustments
- **Price Trends**: Rising/falling/stable market indicators
- **Volume Tracking**: 24-hour trading volume metrics

### Trading Orders
- **Order Types**: Buy and sell orders with limit pricing
- **Order Matching**: Automatic order fulfillment
- **Order States**: Active, filled, cancelled, expired

### Auction System
- **Bid Increments**: Minimum bid increases
- **Time-based Auctions**: Fixed duration auctions
- **Reserve Prices**: Hidden minimum selling prices

### Currency Exchange
- **Multiple Currencies**: Eurodollars, Bitcoin, Eddies
- **Real-time Rates**: Dynamic exchange rates
- **Exchange Limits**: Anti-manipulation safeguards

## Security Considerations

### Authentication
- Bearer token authentication for all trading operations
- User authorization for order and auction ownership
- Rate limiting on high-frequency operations

### Anti-Cheat Measures
- Price manipulation detection
- Auction sniping prevention
- Unusual trading pattern monitoring
- Balance validation before transactions

### Data Protection
- Encrypted transaction data
- Secure wallet operations
- Audit trails for all economic activities

## Performance Optimizations

### Memory Optimization
- Struct alignment hints for 30-50% memory savings
- Object pooling for order management
- Compressed market data structures

### Database Optimization
- Indexed queries for fast order matching
- Partitioned tables for historical data
- Cached market prices with Redis

### Network Optimization
- Paginated responses for large datasets
- Compressed JSON payloads
- Efficient WebSocket updates for real-time data

## Integration Points

### Dependencies
- `common/schemas` - Shared data structures
- `common/security` - Authentication frameworks
- `inventory-service` - Item ownership validation
- `user-service` - User balance management

### Clients
- **Game Client** - Real-time trading interface
- **Web Dashboard** - Administrative market monitoring
- **Mobile App** - Auction house and wallet management

## Development Guidelines

### Code Generation
- Compatible with ogen for Go code generation
- Struct alignment hints for performance optimization
- Domain separation maintained in generated code

### Testing Strategy
- Unit tests for economic calculations
- Integration tests for order matching
- Performance tests for latency requirements
- Load testing for concurrent trading scenarios

### Monitoring and Observability
- Prometheus metrics for economic KPIs
- Distributed tracing for transaction flows
- Real-time alerting for market anomalies
- Health check endpoints for service monitoring

## Future Enhancements

### Planned Features
- **Advanced Trading**: Options, futures, and derivatives
- **Market Prediction**: AI-powered price forecasting
- **Cross-Platform Trading**: Unified markets across devices
- **Economic Analytics**: Advanced market analysis tools

### Performance Improvements
- **Edge Computing**: Localized market processing
- **Machine Learning**: Predictive market modeling
- **Real-time Analytics**: Live economic dashboards
- **Advanced Caching**: Multi-level caching strategies

## Issue Tracking

- **API Design**: #ECONOMY-API-SPECIFICATION
- **Backend Implementation**: #ECONOMY-SERVICE-IMPLEMENTATION
- **Performance Optimization**: Ongoing monitoring
- **Security Audits**: Regular reviews

---

*This API specification follows enterprise-grade patterns established in the NECPGAME project, ensuring scalability, performance, and maintainability for a first-class MMOFPS RPG experience.*