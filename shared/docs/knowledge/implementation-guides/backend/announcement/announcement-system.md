---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:30
**api-readiness-notes:** Announcement System. Новости, объявления, patch notes, in-game announcements. ~380 строк.
---

# Announcement System - Система объявлений

---

- **Status:** queued
- **Last Updated:** 2025-11-07 23:25
---

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:30  
**Приоритет:** HIGH (Communication!)  
**Автор:** AI Brain Manager

**Микрофича:** Announcements & news system  
**Размер:** ~380 строк ✅

---

## Краткое описание

**Announcement System** - система донесения информации до игроков (новости, обновления, события).

**Ключевые возможности:**
- ✅ Game News (новости игры)
- ✅ Patch Notes (описание обновлений)
- ✅ In-Game Announcements (объявления в игре)
- ✅ Maintenance Alerts (уведомления об обслуживании)
- ✅ Event Announcements (анонсы событий)
- ✅ Multi-Channel Delivery (email, push, in-game)

---

## Архитектура системы

```
Admin creates announcement
    ↓
Schedule or publish immediately
    ↓
Multi-channel delivery:
├─ In-game popup
├─ Email notification
├─ Push notification
└─ News feed
    ↓
Track read status
```

---

## Database Schema

### Таблица `announcements`

```sql
CREATE TABLE announcements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Content
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    summary VARCHAR(500),
    
    -- Type
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50),
    
    -- Priority
    priority VARCHAR(20) DEFAULT 'NORMAL',
    
    -- Publishing
    status VARCHAR(20) DEFAULT 'DRAFT',
    published_at TIMESTAMP,
    expires_at TIMESTAMP,
    
    -- Targeting
    target_audience VARCHAR(20) DEFAULT 'ALL',
    target_regions VARCHAR(100)[],
    target_player_levels INTEGER[],
    
    -- Display
    display_style VARCHAR(50) DEFAULT 'NEWS_FEED',
    is_dismissible BOOLEAN DEFAULT TRUE,
    show_popup BOOLEAN DEFAULT FALSE,
    
    -- Media
    banner_image VARCHAR(255),
    attachments JSONB,
    
    -- External links
    external_url VARCHAR(500),
    call_to_action VARCHAR(100),
    
    -- Metadata
    author_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_announcement_author FOREIGN KEY (author_id) 
        REFERENCES accounts(id) ON DELETE SET NULL
);

CREATE INDEX idx_announcements_published 
    ON announcements(published_at DESC) 
    WHERE status = 'PUBLISHED';
CREATE INDEX idx_announcements_type ON announcements(type);
CREATE INDEX idx_announcements_expires ON announcements(expires_at) 
    WHERE expires_at IS NOT NULL;
```

### Таблица `player_announcement_reads`

```sql
CREATE TABLE player_announcement_reads (
    player_id UUID NOT NULL,
    announcement_id UUID NOT NULL,
    
    -- Read tracking
    read_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dismissed BOOLEAN DEFAULT FALSE,
    dismissed_at TIMESTAMP,
    
    -- Interaction
    clicked_cta BOOLEAN DEFAULT FALSE,
    clicked_at TIMESTAMP,
    
    PRIMARY KEY (player_id, announcement_id),
    
    CONSTRAINT fk_read_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_read_announcement FOREIGN KEY (announcement_id) 
        REFERENCES announcements(id) ON DELETE CASCADE
);

CREATE INDEX idx_reads_announcement ON player_announcement_reads(announcement_id);
```

---

## Announcement Types

```java
public enum AnnouncementType {
    GAME_NEWS,          // Игровые новости
    PATCH_NOTES,        // Обновления
    MAINTENANCE,        // Обслуживание
    EVENT,              // Событие
    PROMOTION,          // Акция/промо
    COMMUNITY,          // Сообщество
    EMERGENCY           // Срочное сообщение
}

public enum AnnouncementPriority {
    LOW,        // Обычные новости
    NORMAL,     // Важные новости
    HIGH,       // Очень важные
    URGENT,     // Срочные (обслуживание)
    CRITICAL    // Критические (проблемы)
}

public enum DisplayStyle {
    NEWS_FEED,      // Лента новостей
    POPUP,          // Всплывающее окно
    MODAL,          // Модальное окно (блокирующее)
    BANNER,         // Баннер вверху экрана
    TOAST           // Короткое уведомление
}
```

---

## Create Announcement

```java
@Service
public class AnnouncementService {
    
    public Announcement createAnnouncement(CreateAnnouncementRequest request) {
        // Create
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setSummary(request.getSummary());
        announcement.setType(request.getType());
        announcement.setCategory(request.getCategory());
        announcement.setPriority(request.getPriority());
        announcement.setTargetAudience(request.getTargetAudience());
        announcement.setDisplayStyle(request.getDisplayStyle());
        announcement.setShowPopup(request.isShowPopup());
        announcement.setBannerImage(request.getBannerImage());
        announcement.setExternalUrl(request.getExternalUrl());
        announcement.setCallToAction(request.getCallToAction());
        announcement.setAuthorId(getCurrentAdmin().getId());
        announcement.setStatus(AnnouncementStatus.DRAFT);
        
        announcement = announcementRepository.save(announcement);
        
        log.info("Announcement created: {}", announcement.getId());
        
        return announcement;
    }
    
    public void publishAnnouncement(UUID announcementId, 
                                   Instant publishAt,
                                   Instant expiresAt) {
        Announcement announcement = announcementRepository.findById(announcementId)
            .orElseThrow();
        
        announcement.setPublishedAt(publishAt);
        announcement.setExpiresAt(expiresAt);
        
        if (publishAt.isBefore(Instant.now()) || publishAt.equals(Instant.now())) {
            // Publish immediately
            announcement.setStatus(AnnouncementStatus.PUBLISHED);
            announcementRepository.save(announcement);
            
            // Deliver to players
            deliverAnnouncement(announcement);
        } else {
            // Schedule for later
            announcement.setStatus(AnnouncementStatus.SCHEDULED);
            announcementRepository.save(announcement);
            
            scheduler.schedule(
                () -> publishScheduledAnnouncement(announcementId),
                publishAt
            );
        }
        
        log.info("Announcement published: {}", announcementId);
    }
}
```

---

## Delivery System

```java
@Service
public class AnnouncementDeliveryService {
    
    public void deliverAnnouncement(Announcement announcement) {
        // Get target players
        List<Player> targetPlayers = getTargetPlayers(announcement);
        
        log.info("Delivering announcement {} to {} players",
            announcement.getId(), targetPlayers.size());
        
        // Multi-channel delivery
        if (announcement.isShowPopup()) {
            deliverInGamePopup(announcement, targetPlayers);
        }
        
        if (announcement.getPriority() == AnnouncementPriority.URGENT || 
            announcement.getPriority() == AnnouncementPriority.CRITICAL) {
            deliverEmailNotification(announcement, targetPlayers);
        }
        
        deliverPushNotification(announcement, targetPlayers);
        
        // Always add to news feed
        addToNewsFeed(announcement);
    }
    
    private void deliverInGamePopup(Announcement announcement, List<Player> players) {
        for (Player player : players) {
            if (sessionManager.isOnline(player.getId())) {
                websocketService.send(player.getId(), 
                    "/queue/announcements",
                    new AnnouncementPopup(announcement)
                );
            }
        }
    }
    
    private List<Player> getTargetPlayers(Announcement announcement) {
        if (announcement.getTargetAudience() == TargetAudience.ALL) {
            return playerRepository.findAllActive();
        }
        
        // Filter by criteria
        return playerRepository.findByTargetCriteria(
            announcement.getTargetRegions(),
            announcement.getTargetPlayerLevels()
        );
    }
}
```

---

## Patch Notes

```java
@Service
public class PatchNotesService {
    
    public Announcement createPatchNotes(String version, 
                                        PatchNotesContent content) {
        Announcement announcement = new Announcement();
        announcement.setType(AnnouncementType.PATCH_NOTES);
        announcement.setTitle(String.format("Patch %s - Release Notes", version));
        announcement.setContent(formatPatchNotes(content));
        announcement.setSummary(content.getSummary());
        announcement.setPriority(AnnouncementPriority.HIGH);
        announcement.setShowPopup(true);
        announcement.setDisplayStyle(DisplayStyle.MODAL);
        
        // Add changelog
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("version", version);
        metadata.put("newFeatures", content.getNewFeatures());
        metadata.put("bugFixes", content.getBugFixes());
        metadata.put("improvements", content.getImprovements());
        metadata.put("knownIssues", content.getKnownIssues());
        
        announcement.setAttachments(metadata);
        
        return createAnnouncement(announcement);
    }
    
    private String formatPatchNotes(PatchNotesContent content) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("## New Features

");
        content.getNewFeatures().forEach(f -> sb.append("- ").append(f).append("
"));
        
        sb.append("
## Bug Fixes

");
        content.getBugFixes().forEach(f -> sb.append("- ").append(f).append("
"));
        
        sb.append("
## Improvements

");
        content.getImprovements().forEach(f -> sb.append("- ").append(f).append("
"));
        
        if (!content.getKnownIssues().isEmpty()) {
            sb.append("
## Known Issues

");
            content.getKnownIssues().forEach(f -> sb.append("- ").append(f).append("
"));
        }
        
        return sb.toString();
    }
}
```

---

## In-Game News Feed

```json
{
  "announcements": [
    {
      "id": "uuid",
      "type": "PATCH_NOTES",
      "title": "Patch 1.0.5 - Major Update!",
      "summary": "New features, bug fixes, and improvements",
      "publishedAt": "2025-11-07T00:00:00Z",
      "priority": "HIGH",
      "isRead": false,
      "bannerImage": "/images/patch-1.0.5.jpg"
    },
    {
      "id": "uuid",
      "type": "EVENT",
      "title": "Double XP Weekend!",
      "summary": "Earn 2x XP from Nov 8-10",
      "publishedAt": "2025-11-06T12:00:00Z",
      "priority": "NORMAL",
      "isRead": true
    }
  ],
  "unreadCount": 3
}
```

---

## Связанные документы

- [Notification System](../notification-system.md)
- [Maintenance Mode](../maintenance/maintenance-mode-system.md)
- [Admin Tools](../admin/admin-tools-core.md)
