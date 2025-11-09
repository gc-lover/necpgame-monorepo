---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:18
**api-readiness-notes:** UI для Daily Quest System. Daily/weekly quests, rewards, login streaks. ~390 строк.
---

# UI - Daily Quest System

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:18  
**Приоритет:** HIGH (ENGAGEMENT!)  
**Автор:** AI Brain Manager

**Микрофича:** UI Daily quests & rewards  
**Размер:** ~390 строк ✅

---

- **Status:** queued
- **Last Updated:** 2025-11-08 00:45
---

## Краткое описание

**UI Daily Quest System** - интерфейс для ежедневных и еженедельных заданий.

**Ключевые элементы:**
- ✅ Daily Quests (5 заданий/день)
- ✅ Weekly Quests (бонусные)
- ✅ Login Streak (серия входов)
- ✅ Daily Rewards (награды за вход)
- ✅ Progress Tracking (прогресс)

---

## Главный экран Daily Quests

### Layout

```
┌─────────────────────────────────────────────────────────────────┐
│ DAILY ACTIVITIES                    ⏰ Resets in: 18h 32m       │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ ┌─ LOGIN STREAK ──────────────────────────────────────────────┐ │
│ │                                                              │ │
│ │  🔥 12-Day Streak!                    Next Reward: Day 13   │ │
│ │                                                              │ │
│ │  [✅][✅][✅][✅][✅][✅][✅] [⏳][⚪][⚪][⚪][⚪][⚪]              │ │
│ │   D1  D2  D3  D4  D5  D6  D7  D8  D9  D10 D11 D12 D13       │ │
│ │                                                              │ │
│ │  [ CLAIM TODAY'S REWARD: 300 💰 + Rare Item ]               │ │
│ │                                                              │ │
│ └──────────────────────────────────────────────────────────────┘ │
│                                                                   │
│ ┌─ DAILY QUESTS (2/5 Complete) ──────────────────────────────┐ │
│ │                                                             │ │
│ │  ✅ [Combat] Street Cleaner                     +500 XP    │ │
│ │     Kill 50 gang members                        +200 💰    │ │
│ │     ████████████████████ 50/50                  +1 🎫      │ │
│ │     [CLAIM REWARD]                                         │ │
│ │                                                             │ │
│ │  ✅ [Craft] Master Craftsman                    +500 XP    │ │
│ │     Craft 10 items                              +200 💰    │ │
│ │     ████████████████████ 10/10                  +1 🎫      │ │
│ │     [CLAIM REWARD]                                         │ │
│ │                                                             │ │
│ │  ⏳ [Social] Helping Hand                       +500 XP    │ │
│ │     Help 3 players                              +200 💰    │ │
│ │     ████████░░░░░░░░░░░░ 2/3 (67%)              +1 🎫      │ │
│ │                                                             │ │
│ │  ⏳ [Economy] Market Maven                      +500 XP    │ │
│ │     Complete 5 trades                           +200 💰    │ │
│ │     ████░░░░░░░░░░░░░░░░ 1/5 (20%)              +1 🎫      │ │
│ │                                                             │ │
│ │  ⚪ [Combat] Headshot Master                    +500 XP    │ │
│ │     Get 20 headshot kills                       +200 💰    │ │
│ │     ░░░░░░░░░░░░░░░░░░░░ 0/20                   +1 🎫      │ │
│ │                                                             │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                                                                   │
│ ┌─ WEEKLY CHALLENGES (1/3 Complete) ─────────────────────────┐ │
│ │                                                             │ │
│ │  ✅ Complete 20 Daily Quests             +2000 XP +5 🎫   │ │
│ │     ████████████████████ 20/20                             │ │
│ │                                                             │ │
│ │  ⏳ Earn 50,000 eddies                   +2000 XP +5 🎫   │ │
│ │     ████████████████░░░░ 42K/50K (84%)                     │ │
│ │                                                             │ │
│ │  ⏳ Win 10 Arena matches                 +2000 XP +5 🎫   │ │
│ │     ████████░░░░░░░░░░░░ 6/10 (60%)                        │ │
│ │                                                             │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Daily Quest Card Component

```tsx
interface DailyQuestCardProps {
  quest: DailyQuest;
  progress: PlayerDailyQuestProgress;
  onClaimReward: () => void;
}

const DailyQuestCard: React.FC<DailyQuestCardProps> = ({
  quest,
  progress,
  onClaimReward
}) => {
  const isComplete = progress.status === 'COMPLETED';
  const isClaimed = progress.rewardsClaimed;
  const progressPercentage = (progress.progress / progress.maxProgress) * 100;
  
  return (
    <div className={`daily-quest-card ${isComplete ? 'complete' : 'in-progress'}`}>
      {/* Status Icon */}
      <div className="quest-status-icon">
        {isComplete ? <CheckIcon /> : <ClockIcon />}
      </div>
      
      {/* Quest Info */}
      <div className="quest-info">
        <div className="quest-header">
          <span className={`category ${quest.category.toLowerCase()}`}>
            [{quest.category}]
          </span>
          <h4 className="quest-name">{quest.name}</h4>
        </div>
        
        <p className="quest-description">{quest.description}</p>
        
        {/* Progress Bar */}
        <div className="progress-container">
          <ProgressBar 
            current={progress.progress}
            max={progress.maxProgress}
            showLabel
          />
          <span className="progress-text">
            {progress.progress}/{progress.maxProgress} ({progressPercentage.toFixed(0)}%)
          </span>
        </div>
      </div>
      
      {/* Rewards */}
      <div className="quest-rewards">
        <div className="reward-item">
          <ExperienceIcon /> +{quest.rewards.experience} XP
        </div>
        <div className="reward-item">
          <CurrencyIcon /> +{quest.rewards.eddies} 💰
        </div>
        <div className="reward-item">
          <TokenIcon /> +{quest.rewards.dailyTokens} 🎫
        </div>
        
        {/* Claim Button */}
        {isComplete && !isClaimed && (
          <button 
            className="claim-reward-btn"
            onClick={onClaimReward}
          >
            CLAIM REWARD
          </button>
        )}
        
        {isClaimed && (
          <span className="claimed-badge">CLAIMED</span>
        )}
      </div>
    </div>
  );
};
```

---

## Login Streak Component

```tsx
const LoginStreakWidget: React.FC = () => {
  const { data: streak } = useLoginStreak();
  const [canClaim, setCanClaim] = useState(false);
  
  useEffect(() => {
    // Check if reward can be claimed today
    const lastClaim = streak.lastRewardClaimDate;
    const today = new Date().toDateString();
    setCanClaim(!lastClaim || lastClaim !== today);
  }, [streak]);
  
  return (
    <div className="login-streak-widget">
      {/* Header */}
      <div className="streak-header">
        <h3>
          <FireIcon /> {streak.currentStreak}-Day Streak!
        </h3>
        <div className="streak-info">
          <span>Next Reward: Day {streak.currentStreak + 1}</span>
          <span>Record: {streak.longestStreak} days</span>
        </div>
      </div>
      
      {/* Day Progress */}
      <div className="streak-days">
        {[...Array(14)].map((_, index) => {
          const dayNumber = index + 1;
          const isCompleted = dayNumber <= streak.currentStreak;
          const isToday = dayNumber === streak.currentStreak + 1;
          
          return (
            <div 
              key={dayNumber}
              className={`streak-day ${isCompleted ? 'completed' : ''} ${isToday ? 'today' : ''}`}
            >
              {isCompleted ? <CheckIcon /> : <LockIcon />}
              <span className="day-number">D{dayNumber}</span>
              
              {/* Tooltip with reward */}
              <Tooltip>
                Day {dayNumber} Reward: {getRewardForDay(dayNumber)}
              </Tooltip>
            </div>
          );
        })}
      </div>
      
      {/* Claim Today's Reward */}
      {canClaim && (
        <div className="claim-section">
          <div className="today-reward">
            <h4>Today's Reward</h4>
            <div className="reward-preview">
              {getTodayReward(streak.currentStreak + 1).map(reward => (
                <RewardItem key={reward.type} reward={reward} />
              ))}
            </div>
          </div>
          
          <button 
            className="claim-streak-reward-btn"
            onClick={() => claimLoginReward()}
          >
            CLAIM TODAY'S REWARD
          </button>
        </div>
      )}
      
      {/* Milestones */}
      <div className="streak-milestones">
        <h4>Upcoming Milestones</h4>
        <div className="milestones-list">
          <MilestoneItem 
            days={7}
            reward="7-Day Bonus: +500 💰"
            achieved={streak.currentStreak >= 7}
          />
          <MilestoneItem 
            days={30}
            reward="30-Day Bonus: Exclusive Title"
            achieved={streak.currentStreak >= 30}
          />
          <MilestoneItem 
            days={100}
            reward="100-Day Bonus: Legendary Mount"
            achieved={streak.currentStreak >= 100}
          />
        </div>
      </div>
    </div>
  );
};
```

---

## Quest Progress Tracking

```tsx
const QuestProgressTracker: React.FC<{quest, progress}> = ({
  quest,
  progress
}) => {
  return (
    <div className="quest-progress-tracker">
      {/* Objectives */}
      <div className="objectives-list">
        {quest.objectives.map((objective, index) => (
          <div key={index} className="objective">
            <div className="objective-header">
              <span className="objective-type">
                {getObjectiveIcon(objective.type)}
              </span>
              <span className="objective-description">
                {objective.description}
              </span>
            </div>
            
            <div className="objective-progress">
              <ProgressBar 
                current={progress.objectives[index].current}
                max={objective.target}
              />
              <span>
                {progress.objectives[index].current}/{objective.target}
              </span>
            </div>
          </div>
        ))}
      </div>
      
      {/* Time Remaining */}
      <div className="time-remaining">
        <ClockIcon />
        <span>Expires in: {formatTimeRemaining(quest.expiresAt)}</span>
      </div>
    </div>
  );
};
```

---

## Weekly Challenges

```tsx
const WeeklyChallenges: React.FC = () => {
  const { data: challenges } = useWeeklyChallenges();
  
  return (
    <div className="weekly-challenges">
      <div className="challenges-header">
        <h3>Weekly Challenges</h3>
        <span className="reset-timer">
          Resets in: {formatTimeRemaining(challenges.resetDate)}
        </span>
      </div>
      
      <div className="challenges-list">
        {challenges.list.map(challenge => (
          <WeeklyChallengeCard 
            key={challenge.id}
            challenge={challenge}
          />
        ))}
      </div>
      
      {/* Completion Bonus */}
      <div className="completion-bonus">
        <h4>Complete All 3 for Bonus:</h4>
        <div className="bonus-rewards">
          <RewardItem type="experience" amount={5000} />
          <RewardItem type="currency" amount={10000} />
          <RewardItem type="item" item="Legendary Crate" />
        </div>
      </div>
    </div>
  );
};
```

---

## Daily Shop Integration

```tsx
const DailyTokenShop: React.FC = () => {
  const { data: shop } = useDailyShop();
  const playerTokens = usePlayerTokens();
  
  return (
    <div className="daily-token-shop">
      <div className="shop-header">
        <h3>Daily Token Shop</h3>
        <div className="player-tokens">
          <TokenIcon /> {playerTokens} tokens
        </div>
      </div>
      
      <div className="shop-items">
        {shop.items.map(item => (
          <ShopItemCard 
            key={item.id}
            item={item}
            canAfford={playerTokens >= item.cost}
            onPurchase={() => purchaseItem(item.id)}
          />
        ))}
      </div>
      
      <div className="shop-reset">
        <RefreshIcon />
        <span>Shop resets daily at 00:00 UTC</span>
      </div>
    </div>
  );
};
```

---

## Notifications

### Daily Quest Complete

```tsx
const DailyQuestCompleteNotification: React.FC<{quest}> = ({quest}) => {
  return (
    <div className="daily-quest-complete-notification">
      <div className="notification-header">
        <CheckIcon />
        <span>DAILY QUEST COMPLETE!</span>
      </div>
      
      <div className="quest-info">
        <h4>{quest.name}</h4>
        <p>{quest.description}</p>
      </div>
      
      <div className="rewards">
        <span>Rewards Ready to Claim:</span>
        <div className="reward-list">
          {quest.rewards.map(reward => (
            <RewardItem key={reward.type} reward={reward} />
          ))}
        </div>
      </div>
      
      <button onClick={() => navigateTo('/daily-quests')}>
        CLAIM REWARDS
      </button>
    </div>
  );
};
```

---

## API Integration

```tsx
// Hooks
const useDailyQuests = () => {
  return useQuery({
    queryKey: ['daily-quests'],
    queryFn: () => api.get('/api/v1/daily/quests'),
    refetchInterval: 60000 // Refresh every minute
  });
};

const useLoginStreak = () => {
  return useQuery({
    queryKey: ['login-streak'],
    queryFn: () => api.get('/api/v1/daily/streak')
  });
};

const useWeeklyChallenges = () => {
  return useQuery({
    queryKey: ['weekly-challenges'],
    queryFn: () => api.get('/api/v1/weekly/challenges')
  });
};

// Mutations
const useClaimDailyReward = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (questId: string) => 
      api.post(`/api/v1/daily/quests/${questId}/claim`),
    onSuccess: () => {
      queryClient.invalidateQueries(['daily-quests']);
      queryClient.invalidateQueries(['player-currency']);
    }
  });
};

const useClaimLoginReward = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: () => api.post('/api/v1/daily/rewards/claim'),
    onSuccess: () => {
      queryClient.invalidateQueries(['login-streak']);
      queryClient.invalidateQueries(['player-currency']);
    }
  });
};
```

---

## Reset Timer Component

```tsx
const DailyResetTimer: React.FC = () => {
  const [timeUntilReset, setTimeUntilReset] = useState('');
  
  useEffect(() => {
    const updateTimer = () => {
      const now = new Date();
      const tomorrow = new Date(now);
      tomorrow.setDate(tomorrow.getDate() + 1);
      tomorrow.setHours(0, 0, 0, 0);
      
      const diff = tomorrow.getTime() - now.getTime();
      const hours = Math.floor(diff / (1000 * 60 * 60));
      const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
      
      setTimeUntilReset(`${hours}h ${minutes}m`);
    };
    
    updateTimer();
    const interval = setInterval(updateTimer, 60000); // Update every minute
    
    return () => clearInterval(interval);
  }, []);
  
  return (
    <div className="daily-reset-timer">
      <ClockIcon />
      <span>Resets in: {timeUntilReset}</span>
    </div>
  );
};
```

---

## Связанные документы

- [Daily Reset System Backend](../../backend/daily-reset/daily-reset-compact.md)
- [Achievement UI](../achievements/ui-achievements-main.md)
- [Quest System](../../backend/quest-engine-backend.md)
