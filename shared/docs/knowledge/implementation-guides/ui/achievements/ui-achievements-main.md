---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 12:20
**api-readiness-notes:** UI для Achievement System. Список достижений, прогресс, categories, filters. WebSocket обновлён на `wss://api.necp.game/v1/gameplay/achievements`. ~390 строк.
---

# UI - Achievement System

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:18  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** UI Achievement main screen  
**Размер:** ~390 строк ✅

---

- **Status:** queued
- **Last Updated:** 2025-11-08 00:28
---

## Краткое описание

**UI Achievement System** - интерфейс для просмотра и отслеживания достижений игрока.

**Ключевые экраны:**
- ✅ Achievement List (список всех)
- ✅ Category View (по категориям)
- ✅ Progress Tracking (текущий прогресс)
- ✅ Recent Unlocks (недавние)
- ✅ Near Completion (почти завершенные)
- ✅ Leaderboard (таблица лидеров по очкам)

---

## Главный экран достижений

### Layout

```
┌─────────────────────────────────────────────────────────────────┐
│ ACHIEVEMENTS                                     [Search] [⚙️]   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ 📊 Progress: 125/500 (25%)  |  🏆 2,450 Points  |  🎯 Rank #523 │
│                                                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ [All] [Combat] [Quests] [Social] [Economy] [Exploration] [...]  │
│                                                                   │
├─────────────────────────────────────────────────────────────────┤
│ ┌─ RECENT UNLOCKS ───────────────────────────────────────────┐  │
│ │                                                             │  │
│ │  🏆 Quest Master           🎯 Millionaire                   │  │
│ │  Complete 100 quests       Reach 1M eddies                 │  │
│ │  [RARE] 2 hours ago        [EPIC] 5 hours ago             │  │
│ │                                                             │  │
│ └─────────────────────────────────────────────────────────────┘  │
│                                                                   │
│ ┌─ NEAR COMPLETION ──────────────────────────────────────────┐  │
│ │                                                             │  │
│ │  ⚡ Killer III (95%)       💰 Trader (87%)                  │  │
│ │  ████████████████░░ 475/500  ████████████████░░░ 870/1000   │  │
│ │                                                             │  │
│ └─────────────────────────────────────────────────────────────┘  │
│                                                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ ALL ACHIEVEMENTS (500)                          [Grid][List]     │
│                                                                   │
│ ┌───────────────┐ ┌───────────────┐ ┌───────────────┐          │
│ │ ✅ First Blood│ │ ✅ Killer I   │ │ ⏳ Killer II  │          │
│ │ [COMMON]      │ │ [COMMON]      │ │ [UNCOMMON]    │          │
│ │ 10 pts        │ │ 10 pts        │ │ 25 pts        │          │
│ │               │ │               │ │ 245/500 (49%) │          │
│ └───────────────┘ └───────────────┘ └───────────────┘          │
│                                                                   │
│ ┌───────────────┐ ┌───────────────┐ ┌───────────────┐          │
│ │ 🔒 ???        │ │ ✅ Headhunter │ │ ⏳ Millionaire│          │
│ │ [LEGENDARY]   │ │ [RARE]        │ │ [EPIC]        │          │
│ │ Hidden        │ │ 50 pts        │ │ 100 pts       │          │
│ │               │ │               │ │ 950K/1M (95%) │          │
│ └───────────────┘ └───────────────┘ └───────────────┘          │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Achievement Card Component

### Unlocked Achievement

```tsx
interface AchievementCardProps {
  achievement: Achievement;
  playerProgress: PlayerAchievement;
  onClick: () => void;
}

const AchievementCard: React.FC<AchievementCardProps> = ({
  achievement,
  playerProgress,
  onClick
}) => {
  const isUnlocked = playerProgress.status === 'UNLOCKED';
  const isHidden = achievement.isHidden && !isUnlocked;
  
  return (
    <div 
      className={`achievement-card ${getRarityClass(achievement.rarity)} ${isUnlocked ? 'unlocked' : 'locked'}`}
      onClick={onClick}
    >
      {/* Icon */}
      <div className="achievement-icon">
        {isHidden ? (
          <LockIcon />
        ) : (
          <img src={achievement.icon} alt={achievement.name} />
        )}
        {isUnlocked && <CheckmarkBadge />}
      </div>
      
      {/* Content */}
      <div className="achievement-content">
        <h3 className="achievement-name">
          {isHidden ? '???' : achievement.name}
        </h3>
        
        <p className="achievement-description">
          {isHidden ? 'Hidden achievement' : achievement.description}
        </p>
        
        {/* Rarity & Points */}
        <div className="achievement-meta">
          <span className={`rarity ${achievement.rarity.toLowerCase()}`}>
            {achievement.rarity}
          </span>
          <span className="points">{achievement.points} pts</span>
        </div>
        
        {/* Progress Bar (if in progress) */}
        {!isUnlocked && playerProgress.progress > 0 && (
          <div className="progress-container">
            <ProgressBar 
              current={playerProgress.progress}
              max={playerProgress.maxProgress}
            />
            <span className="progress-text">
              {playerProgress.progress}/{playerProgress.maxProgress} 
              ({Math.round(playerProgress.progressPercentage)}%)
            </span>
          </div>
        )}
        
        {/* Unlock Date */}
        {isUnlocked && (
          <div className="unlock-info">
            <CalendarIcon />
            <span>Unlocked {formatRelativeTime(playerProgress.unlockedAt)}</span>
          </div>
        )}
      </div>
    </div>
  );
};
```

---

## Category Filter

```tsx
const AchievementCategories: React.FC = () => {
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  
  const categories = [
    { code: 'ALL', name: 'All', count: 500, icon: '🎯' },
    { code: 'COMBAT', name: 'Combat', count: 50, icon: '⚔️' },
    { code: 'QUEST', name: 'Quests', count: 100, icon: '📜' },
    { code: 'SOCIAL', name: 'Social', count: 40, icon: '👥' },
    { code: 'ECONOMY', name: 'Economy', count: 50, icon: '💰' },
    { code: 'EXPLORATION', name: 'Exploration', count: 60, icon: '🗺️' },
    { code: 'SKILLS', name: 'Skills', count: 40, icon: '⭐' },
    { code: 'COLLECTIONS', name: 'Collections', count: 30, icon: '🎁' },
    { code: 'SPECIAL', name: 'Special', count: 30, icon: '✨' }
  ];
  
  return (
    <div className="category-filter">
      {categories.map(category => (
        <button
          key={category.code}
          className={`category-button ${selectedCategory === category.code ? 'active' : ''}`}
          onClick={() => setSelectedCategory(category.code)}
        >
          <span className="category-icon">{category.icon}</span>
          <span className="category-name">{category.name}</span>
          <span className="category-count">({category.count})</span>
        </button>
      ))}
    </div>
  );
};
```

---

## Achievement Detail Modal

```tsx
const AchievementDetailModal: React.FC<{achievement, playerProgress}> = ({
  achievement,
  playerProgress
}) => {
  return (
    <Modal>
      <div className="achievement-detail">
        {/* Header */}
        <div className="detail-header">
          <img src={achievement.icon} className="large-icon" />
          <div>
            <h2>{achievement.name}</h2>
            <p className={`rarity ${achievement.rarity.toLowerCase()}`}>
              {achievement.rarity}
            </p>
          </div>
        </div>
        
        {/* Description */}
        <div className="detail-description">
          <p>{achievement.description}</p>
        </div>
        
        {/* Progress */}
        <div className="detail-progress">
          {playerProgress.status === 'UNLOCKED' ? (
            <div className="unlocked-badge">
              <CheckIcon /> UNLOCKED
              <span className="unlock-date">
                {formatDate(playerProgress.unlockedAt)}
              </span>
            </div>
          ) : (
            <div className="progress-section">
              <h3>Progress</h3>
              <ProgressBar 
                current={playerProgress.progress}
                max={playerProgress.maxProgress}
              />
              <span>
                {playerProgress.progress}/{playerProgress.maxProgress} 
                ({playerProgress.progressPercentage}%)
              </span>
            </div>
          )}
        </div>
        
        {/* Rewards */}
        <div className="detail-rewards">
          <h3>Rewards</h3>
          <div className="rewards-list">
            {achievement.rewards.title && (
              <RewardItem 
                type="title"
                value={achievement.rewards.title}
              />
            )}
            {achievement.rewards.perks?.map(perk => (
              <RewardItem 
                key={perk.perkId}
                type="perk"
                value={perk}
              />
            ))}
            {achievement.rewards.currency && (
              <RewardItem 
                type="currency"
                value={achievement.rewards.currency}
              />
            )}
          </div>
        </div>
        
        {/* Stats */}
        <div className="detail-stats">
          <h3>Statistics</h3>
          <div className="stats-grid">
            <StatItem 
              label="Players Unlocked"
              value={`${achievement.stats.unlockPercentage}%`}
            />
            <StatItem 
              label="Average Time"
              value={achievement.stats.averageTimeToUnlock}
            />
            <StatItem 
              label="First Unlock"
              value={achievement.stats.firstUnlockedBy.playerName}
            />
          </div>
        </div>
      </div>
    </Modal>
  );
};
```

---

## Search & Filters

```tsx
const AchievementSearch: React.FC = () => {
  const [filters, setFilters] = useState({
    search: '',
    rarity: 'ALL',
    status: 'ALL', // ALL | UNLOCKED | IN_PROGRESS | LOCKED
    category: 'ALL'
  });
  
  return (
    <div className="achievement-search">
      {/* Search Input */}
      <input
        type="text"
        placeholder="Search achievements..."
        value={filters.search}
        onChange={(e) => setFilters({...filters, search: e.target.value})}
        className="search-input"
      />
      
      {/* Filters */}
      <div className="filters">
        {/* Rarity Filter */}
        <select 
          value={filters.rarity}
          onChange={(e) => setFilters({...filters, rarity: e.target.value})}
        >
          <option value="ALL">All Rarities</option>
          <option value="COMMON">Common</option>
          <option value="UNCOMMON">Uncommon</option>
          <option value="RARE">Rare</option>
          <option value="EPIC">Epic</option>
          <option value="LEGENDARY">Legendary</option>
        </select>
        
        {/* Status Filter */}
        <select
          value={filters.status}
          onChange={(e) => setFilters({...filters, status: e.target.value})}
        >
          <option value="ALL">All Status</option>
          <option value="UNLOCKED">Unlocked</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="LOCKED">Locked</option>
        </select>
      </div>
    </div>
  );
};
```

---

## Leaderboard Integration

```tsx
const AchievementLeaderboard: React.FC = () => {
  const { data: leaderboard } = useQuery('/api/v1/achievements/leaderboard');
  
  return (
    <div className="achievement-leaderboard">
      <h2>Top Achievement Hunters</h2>
      
      <table className="leaderboard-table">
        <thead>
          <tr>
            <th>Rank</th>
            <th>Player</th>
            <th>Points</th>
            <th>Achievements</th>
            <th>Completion</th>
          </tr>
        </thead>
        <tbody>
          {leaderboard.players.map(player => (
            <tr key={player.playerId}>
              <td className="rank">
                {player.rank <= 3 ? (
                  <MedalIcon rank={player.rank} />
                ) : (
                  player.rank
                )}
              </td>
              <td className="player">
                <Avatar src={player.avatar} />
                <span>{player.playerName}</span>
                <span className="level">Lvl {player.playerLevel}</span>
              </td>
              <td className="points">
                {player.totalPoints.toLocaleString()}
              </td>
              <td className="achievements">
                {player.achievementsUnlocked}/500
              </td>
              <td className="completion">
                <ProgressBar 
                  current={player.completionPercentage}
                  max={100}
                  showLabel
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
```

---

## Notifications

### Achievement Unlock Popup

```tsx
const AchievementUnlockNotification: React.FC<{achievement}> = ({
  achievement
}) => {
  return (
    <div className={`achievement-unlock-popup ${achievement.rarity.toLowerCase()}`}>
      <div className="popup-header">
        <StarIcon />
        <span>ACHIEVEMENT UNLOCKED!</span>
      </div>
      
      <div className="popup-content">
        <img src={achievement.icon} className="achievement-icon" />
        
        <div className="achievement-info">
          <h3>{achievement.name}</h3>
          <p>{achievement.description}</p>
          
          <div className="rewards">
            <span className="points">+{achievement.points} pts</span>
            {achievement.rewards.title && (
              <span className="title">Title: {achievement.rewards.title}</span>
            )}
          </div>
        </div>
      </div>
      
      <div className="popup-actions">
        <button onClick={() => navigateTo('/achievements')}>
          View All Achievements
        </button>
      </div>
    </div>
  );
};
```

---

## API Integration

```tsx
// Hooks
const useAchievements = () => {
  return useQuery({
    queryKey: ['achievements'],
    queryFn: () => api.get('/api/v1/achievements')
  });
};

const usePlayerAchievements = (playerId: string) => {
  return useQuery({
    queryKey: ['player-achievements', playerId],
    queryFn: () => api.get(`/api/v1/players/${playerId}/achievements`)
  });
};

const useAchievementStats = (achievementId: string) => {
  return useQuery({
    queryKey: ['achievement-stats', achievementId],
    queryFn: () => api.get(`/api/v1/achievements/${achievementId}/stats`)
  });
};

// WebSocket для real-time updates
useEffect(() => {
  const ws = new WebSocket('wss://api.necp.game/v1/gameplay/achievements');
  
  ws.on('achievement_unlocked', (data) => {
    showAchievementUnlockNotification(data.achievement);
    queryClient.invalidateQueries(['player-achievements']);
  });
  
  ws.on('achievement_progress', (data) => {
    updateAchievementProgress(data);
  });
  
  return () => ws.close();
}, []);
```

---

## Связанные документы

- [Achievement System Backend](../../backend/achievement/achievement-core.md)
- [Leaderboard UI](../leaderboards/ui-leaderboards.md)
- [Notification System](../../backend/notification-system.md)
