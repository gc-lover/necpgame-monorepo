---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:59
**api-readiness-notes:** Achievement System Tracking. Event tracking, прогресс calculation, уведомления. ~380 строк.
---

- **Status:** queued
- **Last Updated:** 2025-11-08 02:52
---

# Achievement System - Tracking & Notifications

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:59  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Achievement tracking & notifications  
**Размер:** ~380 строк ✅

**Связанные микрофичи:**
- [Achievement Core](./achievement-core.md)

---

## Event Tracking Architecture

```
Game Event → Event Bus
    ↓
Achievement Listener (async)
    ↓
Match Relevant Achievements
    ↓
Update Progress (batch)
    ↓
Check Unlock Conditions
    ↓
Grant Rewards + Notify
```

---

## Event Types Mapping

### Combat Events → Achievements

```java
public class CombatAchievementTracker {
    
    @Async
    @EventListener
    public void onEnemyKilled(EnemyKilledEvent event) {
        Map<String, Object> context = Map.of(
            "enemy_type", event.getEnemyType(),
            "enemy_level", event.getEnemyLevel(),
            "weapon_used", event.getWeaponId(),
            "kill_type", event.getKillType(),
            "headshot", event.isHeadshot(),
            "stealth_kill", event.isStealthKill()
        );
        
        // Track multiple achievements
        achievementService.trackProgress(event.getPlayerId(), "enemy_killed", context);
        
        // Specific trackers
        if (event.isHeadshot()) {
            achievementService.trackProgress(event.getPlayerId(), "headshot_kill", context);
        }
        
        if (event.getEnemyType() == "BOSS") {
            achievementService.trackProgress(event.getPlayerId(), "boss_killed", context);
        }
    }
    
    @EventListener
    public void onCombatSessionCompleted(CombatSessionCompletedEvent event) {
        achievementService.trackProgress(
            event.getPlayerId(),
            "combat_session_completed",
            Map.of(
                "duration", event.getDuration(),
                "kills", event.getTotalKills(),
                "deaths", event.getDeaths(),
                "damage_dealt", event.getDamageDealt()
            )
        );
    }
}
```

### Quest Events → Achievements

```java
@EventListener
public void onQuestCompleted(QuestCompletedEvent event) {
    Map<String, Object> context = Map.of(
        "quest_id", event.getQuestId(),
        "quest_type", event.getQuestType(),
        "branch_taken", event.getBranchName(),
        "completion_time", event.getCompletionTime(),
        "choices_made", event.getChoices()
    );
    
    achievementService.trackProgress(event.getPlayerId(), "quest_completed", context);
    
    // Specific quest achievements
    achievementService.trackProgress(
        event.getPlayerId(),
        "quest_specific_" + event.getQuestId(),
        context
    );
}
```

### Economy Events → Achievements

```java
@EventListener
public void onTradeCompleted(TradeCompletedEvent event) {
    achievementService.trackProgress(
        event.getSellerId(),
        "trade_completed",
        Map.of(
            "trade_value", event.getAmount(),
            "item_rarity", event.getItemRarity()
        )
    );
}

@EventListener
public void onCurrencyEarned(CurrencyEarnedEvent event) {
    achievementService.trackProgress(
        event.getPlayerId(),
        "currency_earned",
        Map.of(
            "amount", event.getAmount(),
            "total_balance", event.getNewBalance()
        )
    );
}
```

---

## Progress Calculation Strategies

### Simple Counter

```python
def calculate_progress_counter(current, event_data):
    """Простой счетчик (kills, crafts, etc)"""
    return current + 1
```

### Threshold Checker

```python
def calculate_progress_threshold(current, event_data, threshold):
    """Проверка порога (накопить 1M eddies)"""
    if event_data['total_balance'] >= threshold:
        return threshold  # Unlocked!
    return event_data['total_balance']
```

### Collection Tracker

```python
def calculate_progress_collection(current_items, event_data, required_items):
    """Коллекционирование (all legendary weapons)"""
    current_items.add(event_data['item_id'])
    return len(current_items.intersection(required_items))
```

### Composite Conditions

```python
def check_composite_conditions(player, conditions):
    """Несколько условий одновременно"""
    results = []
    
    for condition in conditions:
        result = evaluate_single_condition(player, condition)
        results.append(result)
    
    if conditions['operator'] == 'AND':
        return all(results)
    elif conditions['operator'] == 'OR':
        return any(results)
    else:
        return False
```

---

## Batch Progress Updates

### Batch Processor

```java
@Service
public class AchievementBatchProcessor {
    
    private Queue<ProgressUpdate> updateQueue = new ConcurrentLinkedQueue<>();
    
    public void queueUpdate(UUID playerId, String achievementCode, int progressDelta) {
        updateQueue.add(new ProgressUpdate(playerId, achievementCode, progressDelta));
    }
    
    @Scheduled(fixedDelay = 5000) // Every 5 seconds
    public void processBatch() {
        if (updateQueue.isEmpty()) {
            return;
        }
        
        List<ProgressUpdate> batch = new ArrayList<>();
        ProgressUpdate update;
        
        while ((update = updateQueue.poll()) != null) {
            batch.add(update);
            
            if (batch.size() >= 1000) {
                break; // Max batch size
            }
        }
        
        if (batch.isEmpty()) {
            return;
        }
        
        // Group by player + achievement
        Map<String, Integer> grouped = batch.stream()
            .collect(Collectors.groupingBy(
                u -> u.playerId + ":" + u.achievementCode,
                Collectors.summingInt(u -> u.delta)
            ));
        
        // Batch update DB
        playerAchievementRepository.batchUpdateProgress(grouped);
        
        log.info("Processed {} achievement updates in batch", batch.size());
    }
}
```

---

## Notification System

### Achievement Unlock Notification

```json
{
  "type": "ACHIEVEMENT_UNLOCKED",
  "priority": "HIGH",
  "data": {
    "achievementId": "uuid",
    "name": "Killer I",
    "description": "Kill 100 enemies",
    "rarity": "COMMON",
    "points": 10,
    "rewards": {
      "title": "Killer",
      "items": []
    }
  },
  "display": {
    "style": "POPUP",
    "duration": 5000,
    "sound": "achievement_unlock.mp3",
    "icon": "achievement_combat.png"
  }
}
```

### Progress Milestone Notification

```json
{
  "type": "ACHIEVEMENT_PROGRESS",
  "priority": "LOW",
  "data": {
    "achievementId": "uuid",
    "name": "Killer II",
    "currentProgress": 250,
    "maxProgress": 500,
    "percentageComplete": 50
  },
  "display": {
    "style": "TOAST",
    "message": "Killer II: 50% complete!"
  }
}
```

---

## Near Completion Detection

```java
@Service
public class AchievementReminderService {
    
    @Scheduled(cron = "0 0 12 * * *") // Daily at noon
    public void sendNearCompletionReminders() {
        List<Player> activePlayers = getActivePlayers();
        
        for (Player player : activePlayers) {
            // Find achievements >80% complete
            List<PlayerAchievement> nearCompletion = 
                playerAchievementRepository.findNearCompletion(
                    player.getId(),
                    0.80 // 80%+
                );
            
            if (!nearCompletion.isEmpty()) {
                notificationService.send(player.getId(), 
                    new NearCompletionNotification(nearCompletion));
            }
        }
    }
}
```

---

## Meta Achievement System

### Auto-unlock Meta Achievements

```java
@Service
public class MetaAchievementService {
    
    public void checkMetaAchievements(UUID playerId, String category) {
        // Find meta achievements for this category
        List<Achievement> metaAchievements = achievementRepository
            .findMetaByCategory(category);
        
        for (Achievement meta : metaAchievements) {
            checkMetaUnlock(playerId, meta);
        }
    }
    
    private void checkMetaUnlock(UUID playerId, Achievement meta) {
        UUID[] requiredIds = meta.getRequiredAchievements();
        
        // Check if player has all required
        long unlockedCount = playerAchievementRepository
            .countUnlockedByPlayerAndAchievements(playerId, requiredIds);
        
        if (unlockedCount == requiredIds.length) {
            // All required achievements unlocked!
            unlockAchievement(playerId, meta);
        } else {
            // Update progress
            updateMetaProgress(playerId, meta.getId(), 
                (int) unlockedCount, requiredIds.length);
        }
    }
}
```

---

## Achievement Points System

### Point Values by Rarity

```
COMMON: 10 points
UNCOMMON: 25 points
RARE: 50 points
EPIC: 100 points
LEGENDARY: 250 points
```

### Point Rewards

```
Total Points Milestones:
- 1,000 points → Title "Achiever"
- 5,000 points → Cosmetic reward
- 10,000 points → Epic mount
- 25,000 points → Legendary title "Master of Night City"
```

---

## Analytics & Metrics

### Achievement Metrics

```sql
CREATE MATERIALIZED VIEW achievement_stats AS
SELECT 
    achievement_id,
    COUNT(*) as total_players_unlocked,
    COUNT(*) FILTER (WHERE unlocked_at > NOW() - INTERVAL '7 days') as unlocked_last_week,
    AVG(EXTRACT(EPOCH FROM (unlocked_at - first_progress_at))) as avg_time_to_unlock,
    MIN(unlocked_at) as first_unlock_date,
    COUNT(*) * 100.0 / (SELECT COUNT(*) FROM players) as unlock_percentage
FROM player_achievements
WHERE status = 'UNLOCKED'
GROUP BY achievement_id;
```

### Rarest Achievements

```sql
SELECT 
    a.name,
    a.rarity,
    stats.unlock_percentage,
    stats.total_players_unlocked
FROM achievements a
JOIN achievement_stats stats ON stats.achievement_id = a.id
ORDER BY stats.unlock_percentage ASC
LIMIT 10;
```

---

## Связанные документы

- [Achievement Core](./achievement-core.md)
- [Notification System](../notification-system.md)
- [Event Bus Architecture](../event-bus-system.md)
