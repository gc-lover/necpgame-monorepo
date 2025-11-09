---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:40
**api-readiness-notes:** Referral System. Реферальная программа, invite rewards, milestone bonuses. ~370 строк.
---

# Referral System - Реферальная программа

---

- **Status:** queued
- **Last Updated:** 2025-11-07 22:05
---

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:40  
**Приоритет:** MEDIUM (Growth!)  
**Автор:** AI Brain Manager

**Микрофича:** Referral & invite rewards  
**Размер:** ~370 строк ✅

---

## Краткое описание

**Referral System** - программа привлечения новых игроков через существующих.

**Ключевые возможности:**
- ✅ Referral Codes (уникальные коды)
- ✅ Invite Rewards (награды за приглашения)
- ✅ Milestone Bonuses (бонусы за количество)
- ✅ Two-Way Rewards (обоим игрокам)
- ✅ Referral Leaderboard (рейтинг рефералов)
- ✅ Special Rewards (эксклюзивные награды)

---

## Система работы

```
Existing Player generates referral code
    ↓
Shares code with friend
    ↓
New Player registers with code
    ↓
New Player reaches level 10
    ↓
Both players get rewards
    ↓
Referrer gets milestone bonus (5/10/25/50/100 referrals)
```

---

## Database Schema

### Таблица `referral_codes`

```sql
CREATE TABLE referral_codes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Owner
    player_id UUID NOT NULL,
    
    -- Code
    code VARCHAR(20) UNIQUE NOT NULL,
    
    -- Usage
    uses_count INTEGER DEFAULT 0,
    max_uses INTEGER,
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Tracking
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    
    CONSTRAINT fk_referral_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE
);

CREATE INDEX idx_referral_codes_player ON referral_codes(player_id);
CREATE INDEX idx_referral_codes_code ON referral_codes(code);
```

### Таблица `referrals`

```sql
CREATE TABLE referrals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Referrer
    referrer_id UUID NOT NULL,
    referral_code_id UUID NOT NULL,
    
    -- Referred player
    referred_player_id UUID NOT NULL,
    
    -- Status
    status VARCHAR(20) DEFAULT 'PENDING',
    
    -- Milestones
    level_10_reached BOOLEAN DEFAULT FALSE,
    level_10_reached_at TIMESTAMP,
    
    first_purchase_made BOOLEAN DEFAULT FALSE,
    first_purchase_at TIMESTAMP,
    
    -- Rewards
    rewards_granted BOOLEAN DEFAULT FALSE,
    rewards_granted_at TIMESTAMP,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_referral_referrer FOREIGN KEY (referrer_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_referral_code FOREIGN KEY (referral_code_id) 
        REFERENCES referral_codes(id) ON DELETE CASCADE,
    CONSTRAINT fk_referral_referred FOREIGN KEY (referred_player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    
    UNIQUE(referred_player_id)
);

CREATE INDEX idx_referrals_referrer ON referrals(referrer_id);
CREATE INDEX idx_referrals_status ON referrals(status);
```

---

## Generate Referral Code

```java
@Service
public class ReferralService {
    
    public ReferralCode generateCode(UUID playerId) {
        // Check if player already has code
        Optional<ReferralCode> existing = codeRepository
            .findByPlayerIdAndIsActive(playerId, true);
        
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Generate unique code
        String code = generateUniqueCode(playerId);
        
        // Create code
        ReferralCode referralCode = new ReferralCode();
        referralCode.setPlayerId(playerId);
        referralCode.setCode(code);
        referralCode.setIsActive(true);
        
        return codeRepository.save(referralCode);
    }
    
    private String generateUniqueCode(UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        
        // Format: PLAYERNAME-XXXX
        String baseName = player.getUsername()
            .replaceAll("[^A-Za-z0-9]", "")
            .toUpperCase()
            .substring(0, Math.min(8, player.getUsername().length()));
        
        String randomPart = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
        
        String code = baseName + "-" + randomPart;
        
        // Ensure unique
        while (codeRepository.existsByCode(code)) {
            randomPart = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
            code = baseName + "-" + randomPart;
        }
        
        return code;
    }
}
```

---

## Register with Referral Code

```java
public void registerWithReferralCode(UUID newPlayerId, String referralCode) {
    // Validate code
    ReferralCode code = codeRepository.findByCodeAndIsActive(referralCode, true)
        .orElseThrow(() -> new InvalidReferralCodeException());
    
    // Cannot refer yourself
    if (code.getPlayerId().equals(newPlayerId)) {
        throw new CannotReferYourselfException();
    }
    
    // Create referral
    Referral referral = new Referral();
    referral.setReferrerId(code.getPlayerId());
    referral.setReferralCodeId(code.getId());
    referral.setReferredPlayerId(newPlayerId);
    referral.setStatus(ReferralStatus.PENDING);
    
    referralRepository.save(referral);
    
    // Update code usage
    code.setUsesCount(code.getUsesCount() + 1);
    codeRepository.save(code);
    
    // Grant immediate welcome bonus to new player
    grantWelcomeBonus(newPlayerId);
    
    log.info("New player registered with referral code: {} referred by {}", 
        newPlayerId, code.getPlayerId());
}
```

---

## Milestone Tracking

```java
@EventListener
public void onPlayerLevelUp(PlayerLevelUpEvent event) {
    // Check if player was referred
    Optional<Referral> referral = referralRepository
        .findByReferredPlayer(event.getPlayerId());
    
    if (referral.isEmpty()) {
        return;
    }
    
    Referral ref = referral.get();
    
    // Check level 10 milestone
    if (event.getNewLevel() >= 10 && !ref.isLevel10Reached()) {
        ref.setLevel10Reached(true);
        ref.setLevel10ReachedAt(Instant.now());
        ref.setStatus(ReferralStatus.ACTIVE);
        
        referralRepository.save(ref);
        
        // Grant rewards to both players
        grantReferralRewards(ref);
        
        // Check referrer milestones
        checkReferrerMilestones(ref.getReferrerId());
    }
}

private void grantReferralRewards(Referral referral) {
    // Referrer rewards
    rewardService.grant(referral.getReferrerId(), ReferrerRewards.builder()
        .eddies(5000)
        .premiumCurrency(100)
        .exclusiveCosmetic("REFERRAL_REWARD_SKIN")
        .build()
    );
    
    // Referred player rewards
    rewardService.grant(referral.getReferredPlayerId(), ReferredRewards.builder()
        .eddies(2000)
        .xpBoost(20, Duration.ofDays(7))
        .starterPack("STARTER_PACK_PREMIUM")
        .build()
    );
    
    referral.setRewardsGranted(true);
    referral.setRewardsGrantedAt(Instant.now());
    referralRepository.save(referral);
}

private void checkReferrerMilestones(UUID referrerId) {
    long activeReferrals = referralRepository
        .countByReferrerAndStatus(referrerId, ReferralStatus.ACTIVE);
    
    // Milestone rewards
    if (activeReferrals == 5) {
        grantMilestone(referrerId, "5_REFERRALS", 10000, "TITLE_RECRUITER");
    } else if (activeReferrals == 10) {
        grantMilestone(referrerId, "10_REFERRALS", 25000, "TITLE_AMBASSADOR");
    } else if (activeReferrals == 25) {
        grantMilestone(referrerId, "25_REFERRALS", 100000, "MOUNT_LEGENDARY");
    } else if (activeReferrals == 50) {
        grantMilestone(referrerId, "50_REFERRALS", 500000, "TITLE_LEGEND");
    } else if (activeReferrals == 100) {
        grantMilestone(referrerId, "100_REFERRALS", 1000000, "ULTIMATE_REWARD");
    }
}
```

---

## Referral Leaderboard

```sql
CREATE MATERIALIZED VIEW referral_leaderboard AS
SELECT 
    p.id as player_id,
    p.username,
    COUNT(*) FILTER (WHERE r.status = 'ACTIVE') as active_referrals,
    COUNT(*) as total_referrals,
    MIN(r.created_at) as first_referral_date
FROM players p
JOIN referrals r ON r.referrer_id = p.id
GROUP BY p.id, p.username
ORDER BY active_referrals DESC;
```

---

## API Endpoints

**GET `/api/v1/referral/my-code`** - мой реферальный код

**POST `/api/v1/referral/generate`** - сгенерировать код

**GET `/api/v1/referral/stats`** - статистика рефералов

**GET `/api/v1/referral/leaderboard`** - referral leaderboard

---

## Связанные документы

- [Achievement System](../achievement/achievement-core.md)
- [Premium Currency](../../economy/premium-currency.md)
- [Notification System](../notification-system.md)
