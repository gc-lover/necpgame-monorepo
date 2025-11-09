# Battle Pass - Part 2: Rewards & Challenges

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:41  
**api-readiness:** ready

---

- **Status:** queued
- **Last Updated:** 2025-11-08 00:12
---

[← Part 1](./part1-core-progression.md) | [Навигация](./README.md)

---

## Claim Rewards (ПОЛНЫЙ метод)

```java
public List<Reward> claimReward(UUID playerId, int level) {
    PlayerBattlePassProgress progress = getProgress(playerId);
    
    // Validate level unlocked
    if (level > progress.getCurrentLevel()) {
        throw new LevelNotUnlockedException(level);
    }
    
    List<Reward> rewards = new ArrayList<>();
    
    // Check free track
    if (!progress.getClaimedFreeLevels().contains(level)) {
        Reward freeReward = getReward(progress.getSeasonId(), level, "FREE");
        if (freeReward != null) {
            grantReward(playerId, freeReward);
            rewards.add(freeReward);
            progress.getClaimedFreeLevels().add(level);
        }
    }
    
    // Check premium track
    if (progress.isHasPremium() && 
        !progress.getClaimedPremiumLevels().contains(level)) {
        
        Reward premiumReward = getReward(progress.getSeasonId(), level, "PREMIUM");
        if (premiumReward != null) {
            grantReward(playerId, premiumReward);
            rewards.add(premiumReward);
            progress.getClaimedPremiumLevels().add(level);
        }
    }
    
    progressRepository.save(progress);
    
    return rewards;
}

private void grantReward(UUID playerId, Reward reward) {
    switch (reward.getType()) {
        case CURRENCY:
            currencyService.addCurrency(playerId, 
                reward.getData().get("currency"), 
                reward.getData().get("amount"));
            break;
            
        case ITEM:
            inventoryService.grantItem(playerId, 
                reward.getData().get("itemId"), 
                reward.getData().get("quantity"));
            break;
            
        case COSMETIC:
            cosmeticService.unlockCosmetic(playerId, 
                reward.getData().get("cosmeticId"));
            break;
            
        case XP_BOOST:
            buffService.applyBuff(playerId, "XP_BOOST", 
                reward.getData().get("percentage"),
                parseDuration(reward.getData().get("duration")));
            break;
    }
}
```

---

## Weekly Challenges (ПОЛНЫЙ service)

```java
@Service
public class BattlePassChallengeService {
    
    public List<BattlePassChallenge> generateWeeklyChallenges(UUID seasonId) {
        List<BattlePassChallenge> challenges = new ArrayList<>();
        
        // Generate 5 challenges
        challenges.add(createChallenge("Play 10 matches", 500));
        challenges.add(createChallenge("Complete 5 daily quests", 750));
        challenges.add(createChallenge("Earn 50,000 eddies", 1000));
        challenges.add(createChallenge("Win 5 arena matches", 1250));
        challenges.add(createChallenge("Complete 3 main quests", 1500));
        
        return challenges;
    }
    
    public void completeChallenge(UUID playerId, UUID challengeId) {
        BattlePassChallenge challenge = challengeRepository.findById(challengeId)
            .orElseThrow();
        
        // Award XP
        awardXP(playerId, BattlePassXPSource.WEEKLY_CHALLENGE, challenge.getXpReward());
        
        // Mark complete
        markChallengeComplete(playerId, challengeId);
        
        // Achievement
        achievementService.trackProgress(playerId, "weekly_challenge_complete", Map.of());
    }
    
    private BattlePassChallenge createChallenge(String description, int xpReward) {
        BattlePassChallenge challenge = new BattlePassChallenge();
        challenge.setDescription(description);
        challenge.setXpReward(xpReward);
        challenge.setStartDate(getWeekStart());
        challenge.setEndDate(getWeekEnd());
        return challengeRepository.save(challenge);
    }
}
```

---

## API Endpoints

### Get Current Season

**GET** `/api/v1/battle-pass/current`

```json
Response:
{
  "seasonId": "uuid",
  "seasonNumber": 1,
  "name": "Season 1: Night City Legends",
  "theme": "Corpo Wars",
  "startDate": "2025-12-01T00:00:00Z",
  "endDate": "2026-03-01T00:00:00Z",
  "daysRemaining": 85,
  "maxLevel": 100,
  "premiumPrice": 1000
}
```

### Get Progress

**GET** `/api/v1/battle-pass/progress`

```json
Response:
{
  "playerId": "uuid",
  "seasonId": "uuid",
  "currentLevel": 42,
  "currentXP": 750,
  "xpToNextLevel": 250,
  "totalXPEarned": 42750,
  "hasPremium": true,
  "claimedRewards": {
    "free": [1, 2, 3, 5, 10, 15, 20, 25, 30, 35, 40],
    "premium": [1, 2, 3, 4, 5, ..., 42]
  }
}
```

### Other Endpoints

- **POST** `/api/v1/battle-pass/claim/{level}` - claim reward
- **POST** `/api/v1/battle-pass/purchase-premium` - купить premium
- **GET** `/api/v1/battle-pass/challenges/weekly` - weekly challenges
- **GET** `/api/v1/battle-pass/rewards` - все награды сезона

---

## Reward Types

```json
{
  "type": "CURRENCY",
  "data": {"currency": "EDDIES", "amount": 1000}
}

{
  "type": "ITEM",
  "data": {"itemId": "LEGENDARY_WEAPON_001", "quantity": 1}
}

{
  "type": "COSMETIC",
  "data": {"cosmeticId": "SKIN_SEASON1_EXCLUSIVE"}
}

{
  "type": "XP_BOOST",
  "data": {"percentage": 10, "duration": "7d"}
}

{
  "type": "TITLE",
  "data": {"titleCode": "SEASON_1_CHAMPION"}
}
```

---

## Premium Benefits

1. **Premium Rewards Track**
   - Exclusive skins/items/cosmetics
   - 3x more rewards than free

2. **XP Boosts**
   - +10% XP boost (permanent for season)
   - Faster progression

3. **Exclusive Cosmetics**
   - Legendary skins
   - Exclusive emotes
   - Unique titles

---

[Part 2: Rewards & Challenges →](./part2-rewards-challenges.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:40) - Создан с полным Java кодом (schemas, XP, premium, API)
- v1.0.0 (2025-11-07 02:30) - Создан
