# Guild Service API

## Overview

The Guild Service provides comprehensive guild management capabilities for the NECPGAME. This enterprise-grade microservice handles social organization, cooperative gameplay, and competitive guild mechanics in the Night City universe.

## Key Features

- **Guild Management**: Creation, administration, and hierarchical organization
- **Member Management**: Recruitment, roles, permissions, and expulsion mechanics
- **Social Systems**: Communication, announcements, and coordination features
- **Economic Features**: Guild banks, resource sharing, and asset management
- **Competitive Elements**: Rankings, reputation, and inter-guild rivalries
- **Performance Optimized**: MMOFPS-grade performance with <20ms P99 latency

## Architecture

### Domain Separation
This API follows strict domain separation principles:
- Core guild logic handled by `guild-service`
- Social features integration with communication services
- Economic features integration with banking systems
- Competitive features integration with ranking systems

### Performance Targets
- **P99 Latency**: <20ms for guild operations
- **Memory per Member**: <12KB active guild member
- **Concurrent Guilds**: 50,000+ active guilds
- **Real-time Updates**: <50ms propagation time
- **Chat Performance**: 1000+ concurrent users

## API Endpoints

### Health Monitoring
- `GET /health` - Service health check
- `POST /health/batch` - Batch health check for multiple services

### Guild Management
- `GET /guilds` - List guilds with filtering
- `POST /guilds` - Create new guild
- `GET /guilds/{guild_id}` - Get guild details
- `PUT /guilds/{guild_id}` - Update guild configuration
- `DELETE /guilds/{guild_id}` - Disband guild

### Member Management
- `GET /guilds/{guild_id}/members` - List guild members
- `POST /guilds/{guild_id}/members` - Invite player to guild
- `PUT /guilds/{guild_id}/members/{user_id}` - Update member role
- `DELETE /guilds/{guild_id}/members/{user_id}` - Remove member

### Guild Economy
- `GET /guilds/{guild_id}/bank` - Get guild bank contents
- `POST /guilds/{guild_id}/bank` - Deposit to guild bank

### Guild Competition
- `GET /guilds/rankings` - Get guild rankings and statistics

## Data Structures

### Core Guild Data
- `Guild` - Complete guild information and configuration
- `GuildSummary` - Condensed guild data for listings
- `GuildRequirements` - Membership requirements and restrictions

### Member Management
- `GuildMember` - Member details, roles, and permissions
- `InviteToGuildRequest` - Guild invitation parameters
- `UpdateGuildMemberRequest` - Member role/permission updates

### Economic Features
- `GuildBankResponse` - Guild bank inventory and currency
- `GuildBankItem` - Individual bank items with metadata
- `GuildBankDepositRequest` - Bank deposit operations

### Competitive Features
- `GuildRanking` - Guild ranking positions and statistics
- `GuildRankingsResponse` - Paginated ranking results

## Guild Mechanics

### Guild Hierarchy
- **Leader**: Full administrative control
- **Officer**: Member management and announcements
- **Veteran**: Senior member with enhanced permissions
- **Member**: Standard member with basic access
- **Recruit**: New member with limited access

### Permission System
- **invite_members**: Send guild invitations
- **kick_members**: Remove members from guild
- **manage_bank**: Access and modify guild bank
- **manage_settings**: Change guild configuration
- **send_announcements**: Post guild-wide messages

### Economic System
- **Guild Bank**: Shared storage for items and currency
- **Resource Sharing**: Equipment and materials for all members
- **Contribution Tracking**: Member participation rewards
- **Asset Distribution**: Fair distribution upon disbanding

### Competitive Features
- **Guild Rankings**: Multiple categories (level, reputation, activity)
- **Time-based Rankings**: Daily, weekly, monthly, all-time
- **PvP Statistics**: Wins, losses, tournament participation
- **Reputation System**: Guild-wide reputation affecting diplomacy

## Security Considerations

### Authentication
- Bearer token authentication for all guild operations
- User authorization for guild-specific actions
- Hierarchical permission validation

### Anti-Abuse Measures
- Guild creation rate limiting
- Invitation spam prevention
- Bank manipulation detection
- Member removal restrictions

### Data Protection
- Encrypted guild communication
- Secure member data handling
- Audit trails for administrative actions
- Privacy controls for member information

## Performance Optimizations

### Memory Optimization
- Struct alignment hints for 30-50% memory savings
- Object pooling for member management
- Compressed guild data structures

### Database Optimization
- Indexed queries for member lookups
- Partitioned tables for large guilds
- Cached guild statistics and rankings

### Network Optimization
- Paginated responses for large member lists
- Compressed JSON payloads for bank operations
- Efficient real-time update broadcasting

## Integration Points

### Dependencies
- `common/schemas` - Shared data structures and validation
- `communication-service` - Guild chat and messaging
- `economy-service` - Currency and item integration
- `ranking-service` - Guild ranking calculations

### Clients
- **Game Client** - Guild UI, member management, bank access
- **Web Dashboard** - Administrative guild controls
- **Mobile App** - Guild notifications and basic management

## Development Guidelines

### Code Generation
- Compatible with ogen for Go code generation
- Struct alignment hints for performance optimization
- Domain separation maintained in generated code

### Testing Strategy
- Unit tests for guild logic and permissions
- Integration tests for member management
- Performance tests for large guild operations
- Load testing for concurrent guild activities

### Monitoring and Observability
- Prometheus metrics for guild KPIs
- Distributed tracing for member operations
- Real-time alerting for guild performance issues
- Health check endpoints for service monitoring

## Future Enhancements

### Planned Features
- **Guild Alliances**: Multi-guild cooperation mechanics
- **Guild Wars**: Large-scale PvP between guilds
- **Advanced Economy**: Guild-owned businesses and markets
- **Social Features**: Guild events, tournaments, and celebrations

### Performance Improvements
- **Edge Computing**: Regional guild server distribution
- **Advanced Caching**: Multi-level guild data caching
- **Real-time Analytics**: Live guild activity dashboards
- **AI Assistance**: Automated guild management features

## Issue Tracking

- **API Design**: #GUILD-API-SPECIFICATION
- **Backend Implementation**: #GUILD-SERVICE-IMPLEMENTATION
- **Performance Optimization**: Ongoing monitoring
- **Security Audits**: Regular reviews

---

*This API specification follows enterprise-grade patterns established in the NECPGAME project, ensuring scalability, performance, and maintainability for a first-class MMOFPS RPG experience.*