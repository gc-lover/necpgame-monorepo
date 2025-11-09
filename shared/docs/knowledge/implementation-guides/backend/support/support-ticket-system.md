---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:27
**api-readiness-notes:** Support Ticket System. Тикеты поддержки, категории, приоритеты, статусы. ~390 строк.
---

# Support Ticket System - Система тикетов поддержки

---

- **Status:** queued
- **Last Updated:** 2025-11-07 21:50
---

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:27  
**Приоритет:** HIGH (Customer Service!)  
**Автор:** AI Brain Manager

**Микрофича:** Support tickets & player feedback  
**Размер:** ~390 строк ✅

---

## Краткое описание

**Support Ticket System** - система обращений игроков в техническую поддержку.

**Ключевые возможности:**
- ✅ Ticket Creation (создание тикетов)
- ✅ Ticket Categories (категории проблем)
- ✅ Priority Levels (приоритеты)
- ✅ Status Tracking (отслеживание статуса)
- ✅ Agent Assignment (назначение агента)
- ✅ Response System (система ответов)

---

## Архитектура системы

```
Player creates ticket
    ↓
Auto-categorization
    ↓
Priority assignment
    ↓
Queue for agent
    ↓
Agent reviews and responds
    ↓
Player gets notification
    ↓
Ticket resolved
```

---

## Database Schema

### Таблица `support_tickets`

```sql
CREATE TABLE support_tickets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Ticket number (user-friendly)
    ticket_number VARCHAR(20) UNIQUE NOT NULL,
    
    -- Player
    player_id UUID NOT NULL,
    player_email VARCHAR(255),
    
    -- Issue details
    category VARCHAR(50) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    
    -- Priority & Status
    priority VARCHAR(20) DEFAULT 'NORMAL',
    status VARCHAR(20) DEFAULT 'OPEN',
    
    -- Assignment
    assigned_to UUID,
    assigned_at TIMESTAMP,
    
    -- Resolution
    resolved_at TIMESTAMP,
    resolution_note TEXT,
    
    -- Metadata
    platform VARCHAR(50),
    game_version VARCHAR(20),
    attachments JSONB,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_response_at TIMESTAMP,
    
    -- Rating
    satisfaction_rating INTEGER,
    feedback TEXT,
    
    CONSTRAINT fk_ticket_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_ticket_agent FOREIGN KEY (assigned_to) 
        REFERENCES accounts(id) ON DELETE SET NULL
);

CREATE INDEX idx_tickets_player ON support_tickets(player_id);
CREATE INDEX idx_tickets_status ON support_tickets(status);
CREATE INDEX idx_tickets_category ON support_tickets(category);
CREATE INDEX idx_tickets_assigned ON support_tickets(assigned_to);
CREATE INDEX idx_tickets_created ON support_tickets(created_at DESC);
```

### Таблица `ticket_responses`

```sql
CREATE TABLE ticket_responses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticket_id UUID NOT NULL,
    
    -- Author
    author_id UUID NOT NULL,
    author_type VARCHAR(20) NOT NULL,
    
    -- Content
    message TEXT NOT NULL,
    attachments JSONB,
    
    -- Visibility
    is_internal BOOLEAN DEFAULT FALSE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_response_ticket FOREIGN KEY (ticket_id) 
        REFERENCES support_tickets(id) ON DELETE CASCADE
);

CREATE INDEX idx_responses_ticket ON ticket_responses(ticket_id, created_at);
```

---

## Ticket Categories

```java
public enum TicketCategory {
    TECHNICAL_ISSUE("Technical Issue"),
    ACCOUNT_ISSUE("Account Issue"),
    PAYMENT_ISSUE("Payment/Billing"),
    GAMEPLAY_BUG("Gameplay Bug"),
    REPORT_PLAYER("Report Player"),
    REPORT_CONTENT("Report Content"),
    FEATURE_REQUEST("Feature Request"),
    GENERAL_INQUIRY("General Inquiry"),
    OTHER("Other");
    
    private final String displayName;
}

public enum TicketPriority {
    LOW(1),       // General inquiries
    NORMAL(2),    // Standard issues
    HIGH(3),      // Account/payment issues
    URGENT(4),    // Game-breaking bugs
    CRITICAL(5);  // Security issues
    
    private final int level;
}

public enum TicketStatus {
    OPEN,         // New ticket
    PENDING,      // Waiting for player response
    IN_PROGRESS,  // Agent working on it
    RESOLVED,     // Issue resolved
    CLOSED;       // Ticket closed
}
```

---

## Create Ticket

```java
@Service
public class SupportTicketService {
    
    public SupportTicket createTicket(CreateTicketRequest request) {
        // Validate
        validateTicketRequest(request);
        
        // Create ticket
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketNumber(generateTicketNumber());
        ticket.setPlayerId(request.getPlayerId());
        ticket.setPlayerEmail(request.getEmail());
        ticket.setCategory(request.getCategory());
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setAttachments(request.getAttachments());
        
        // Auto-assign priority
        ticket.setPriority(determinePriority(request.getCategory()));
        
        // Set metadata
        ticket.setPlatform(request.getPlatform());
        ticket.setGameVersion(request.getGameVersion());
        
        ticket.setStatus(TicketStatus.OPEN);
        
        ticket = ticketRepository.save(ticket);
        
        // Auto-categorization
        autoCategorize(ticket);
        
        // Auto-assignment (if rules exist)
        tryAutoAssignment(ticket);
        
        // Send confirmation email
        emailService.sendTicketConfirmation(ticket);
        
        // Notify support team
        notifySupportTeam(ticket);
        
        log.info("Support ticket created: {} for player: {}", 
            ticket.getTicketNumber(), request.getPlayerId());
        
        return ticket;
    }
    
    private String generateTicketNumber() {
        // Format: NECPG-YYYYMMDD-XXXX
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.BASIC_ISO_DATE);
        int sequence = getNextSequenceForToday();
        
        return String.format("NECPG-%s-%04d", datePart, sequence);
    }
    
    private TicketPriority determinePriority(TicketCategory category) {
        return switch (category) {
            case PAYMENT_ISSUE, ACCOUNT_ISSUE -> TicketPriority.HIGH;
            case GAMEPLAY_BUG -> TicketPriority.URGENT;
            case REPORT_PLAYER -> TicketPriority.HIGH;
            case TECHNICAL_ISSUE -> TicketPriority.NORMAL;
            default -> TicketPriority.LOW;
        };
    }
}
```

---

## Add Response

```java
public TicketResponse addResponse(UUID ticketId, AddResponseRequest request) {
    SupportTicket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new TicketNotFoundException(ticketId));
    
    // Create response
    TicketResponse response = new TicketResponse();
    response.setTicketId(ticketId);
    response.setAuthorId(request.getAuthorId());
    response.setAuthorType(request.getAuthorType());
    response.setMessage(request.getMessage());
    response.setAttachments(request.getAttachments());
    response.setIsInternal(request.isInternal());
    
    response = responseRepository.save(response);
    
    // Update ticket
    ticket.setLastResponseAt(Instant.now());
    ticket.setUpdatedAt(Instant.now());
    
    if (request.getAuthorType() == AuthorType.AGENT) {
        ticket.setStatus(TicketStatus.PENDING); // Waiting for player
        
        // Notify player
        notifyPlayerOfResponse(ticket, response);
    } else {
        ticket.setStatus(TicketStatus.IN_PROGRESS); // Agent needs to respond
    }
    
    ticketRepository.save(ticket);
    
    log.info("Response added to ticket: {} by: {}", 
        ticketId, request.getAuthorType());
    
    return response;
}
```

---

## Assign Ticket

```java
public void assignTicket(UUID ticketId, UUID agentId) {
    SupportTicket ticket = ticketRepository.findById(ticketId)
        .orElseThrow();
    
    Account agent = accountRepository.findById(agentId)
        .orElseThrow();
    
    // Check if agent has support role
    if (!agent.hasRole("SUPPORT_AGENT")) {
        throw new InsufficientPermissionsException();
    }
    
    // Assign
    ticket.setAssignedTo(agentId);
    ticket.setAssignedAt(Instant.now());
    ticket.setStatus(TicketStatus.IN_PROGRESS);
    
    ticketRepository.save(ticket);
    
    // Notify agent
    notificationService.send(agentId, 
        new TicketAssignedNotification(ticket));
    
    log.info("Ticket {} assigned to agent: {}", ticketId, agentId);
}
```

---

## Resolve Ticket

```java
public void resolveTicket(UUID ticketId, ResolveTicketRequest request) {
    SupportTicket ticket = ticketRepository.findById(ticketId)
        .orElseThrow();
    
    // Add resolution note
    ticket.setResolutionNote(request.getResolutionNote());
    ticket.setStatus(TicketStatus.RESOLVED);
    ticket.setResolvedAt(Instant.now());
    
    ticketRepository.save(ticket);
    
    // Notify player
    emailService.sendTicketResolved(ticket);
    
    // Request feedback
    requestFeedback(ticket);
    
    // Auto-close after 7 days if no response
    scheduleAutoClose(ticket.getId(), 7);
    
    log.info("Ticket resolved: {}", ticketId);
}
```

---

## SLA (Service Level Agreement)

```java
public class SLAManager {
    
    private static final Map<TicketPriority, Duration> RESPONSE_TIMES = Map.of(
        TicketPriority.CRITICAL, Duration.ofHours(1),
        TicketPriority.URGENT, Duration.ofHours(4),
        TicketPriority.HIGH, Duration.ofHours(12),
        TicketPriority.NORMAL, Duration.ofHours(24),
        TicketPriority.LOW, Duration.ofHours(48)
    );
    
    public boolean isBreachingSLA(SupportTicket ticket) {
        if (ticket.getLastResponseAt() != null) {
            return false; // Already responded
        }
        
        Duration targetTime = RESPONSE_TIMES.get(ticket.getPriority());
        Duration elapsed = Duration.between(ticket.getCreatedAt(), Instant.now());
        
        return elapsed.compareTo(targetTime) > 0;
    }
    
    @Scheduled(fixedDelay = 300000) // Every 5 minutes
    public void checkSLABreaches() {
        List<SupportTicket> openTickets = ticketRepository
            .findByStatusIn(List.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS));
        
        for (SupportTicket ticket : openTickets) {
            if (isBreachingSLA(ticket)) {
                escalateTicket(ticket);
            }
        }
    }
    
    private void escalateTicket(SupportTicket ticket) {
        log.warn("SLA breach detected for ticket: {}", ticket.getTicketNumber());
        
        // Escalate priority
        if (ticket.getPriority() != TicketPriority.CRITICAL) {
            ticket.setPriority(escalatePriority(ticket.getPriority()));
            ticketRepository.save(ticket);
        }
        
        // Notify supervisor
        notifySupervisor(ticket);
    }
}
```

---

## Auto-Assignment Rules

```java
@Component
public class TicketAutoAssigner {
    
    public void tryAutoAssignment(SupportTicket ticket) {
        // Find available agent with matching expertise
        Optional<Account> agent = findBestAgent(ticket);
        
        if (agent.isPresent()) {
            assignTicket(ticket.getId(), agent.get().getId());
        }
    }
    
    private Optional<Account> findBestAgent(SupportTicket ticket) {
        // Get agents with matching category expertise
        List<Account> agents = accountRepository
            .findByRoleAndCategoryExpertise(
                "SUPPORT_AGENT",
                ticket.getCategory()
            );
        
        if (agents.isEmpty()) {
            // Fallback to any available agent
            agents = accountRepository.findAvailableAgents();
        }
        
        // Select agent with lowest ticket load
        return agents.stream()
            .min(Comparator.comparing(this::getAgentTicketCount));
    }
    
    private int getAgentTicketCount(Account agent) {
        return ticketRepository.countByAssignedToAndStatusIn(
            agent.getId(),
            List.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS)
        );
    }
}
```

---

## Feedback & Rating

```java
public void submitFeedback(UUID ticketId, FeedbackRequest request) {
    SupportTicket ticket = ticketRepository.findById(ticketId)
        .orElseThrow();
    
    // Check if ticket is resolved
    if (ticket.getStatus() != TicketStatus.RESOLVED) {
        throw new TicketNotResolvedException();
    }
    
    // Save rating
    ticket.setSatisfactionRating(request.getRating());
    ticket.setFeedback(request.getFeedback());
    ticket.setStatus(TicketStatus.CLOSED);
    
    ticketRepository.save(ticket);
    
    // Update agent metrics
    if (ticket.getAssignedTo() != null) {
        updateAgentMetrics(ticket.getAssignedTo(), request.getRating());
    }
    
    log.info("Feedback submitted for ticket: {} - Rating: {}", 
        ticketId, request.getRating());
}
```

---

## API Endpoints

**POST `/api/v1/support/tickets`** - создать тикет

```json
Request:
{
  "playerId": "uuid",
  "email": "player@example.com",
  "category": "TECHNICAL_ISSUE",
  "subject": "Game crashes on startup",
  "description": "When I try to launch the game...",
  "platform": "PC",
  "gameVersion": "1.0.5",
  "attachments": ["screenshot1.png"]
}

Response:
{
  "ticketId": "uuid",
  "ticketNumber": "NECPG-20251107-0042",
  "status": "OPEN",
  "estimatedResponseTime": "24 hours"
}
```

**GET `/api/v1/support/tickets`** - список тикетов игрока

**GET `/api/v1/support/tickets/{id}`** - детали тикета

**POST `/api/v1/support/tickets/{id}/responses`** - добавить ответ

**POST `/api/v1/support/tickets/{id}/feedback`** - оставить отзыв

---

## Agent Portal API

**GET `/api/v1/admin/support/queue`** - очередь тикетов

**POST `/api/v1/admin/support/tickets/{id}/assign`** - назначить агента

**POST `/api/v1/admin/support/tickets/{id}/resolve`** - решить тикет

**GET `/api/v1/admin/support/metrics`** - метрики поддержки

---

## Связанные документы

- [Admin Tools](../admin/admin-tools-core.md)
- [Notification System](../notification-system.md)
- [Player Reports](../../moderation/player-reports.md)
