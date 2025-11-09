---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 12:20
**api-readiness-notes:** UI для Leaderboard System. Рейтинги, фильтры, позиция игрока. WebSocket обновлён на `wss://api.necp.game/v1/social/leaderboard`. ~370 строк.
---

# UI - Leaderboard System

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:18  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** UI Leaderboard screens  
**Размер:** ~370 строк ✅

---

- **Status:** queued
- **Last Updated:** 2025-11-08 00:59
---

## Краткое описание

**UI Leaderboard System** - интерфейс для просмотра глобальных и локальных рейтингов.

**Ключевые экраны:**
- ✅ Global Leaderboards (глобальные)
- ✅ Category Leaderboards (по категориям)
- ✅ Player Position (позиция игрока)
- ✅ Nearby Players (соседи по рейтингу)
- ✅ Seasonal Leagues (сезонные лиги)

---

## Главный экран рейтингов

### Layout

```
┌─────────────────────────────────────────────────────────────────┐
│ LEADERBOARDS                              [Season 2093] [⚙️]     │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ [Overall] [Combat] [Economy] [Social] [PvP] [Achievements]      │
│                                                                   │
│ [Global] [Regional: Night City] [My Server]                     │
│                                                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ 🎯 YOUR POSITION                                                 │
│                                                                   │
│ Rank #1,523 (Top 3%)                                            │
│ Score: 125,480                                                   │
│ ↑ +15 from yesterday                                            │
│                                                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ TOP 100 - OVERALL POWER                                         │
│                                                                   │
│ ┌────┬──────────────────────────┬────────────┬──────────┐      │
│ │ #  │ Player                   │ Score      │ Change   │      │
│ ├────┼──────────────────────────┼────────────┼──────────┤      │
│ │ 🥇1 │ [👤] V                   │ 500,250    │ --       │      │
│ │    │ Lvl 50 | Night City      │            │          │      │
│ ├────┼──────────────────────────┼────────────┼──────────┤      │
│ │ 🥈2 │ [👤] Johnny              │ 489,100    │ ↓ -1     │      │
│ │    │ Lvl 48 | Night City      │            │          │      │
│ ├────┼──────────────────────────┼────────────┼──────────┤      │
│ │ 🥉3 │ [👤] Judy                │ 475,800    │ ↑ +1     │      │
│ │    │ Lvl 49 | Watson          │            │          │      │
│ ├────┼──────────────────────────┼────────────┼──────────┤      │
│ │  4 │ [👤] Panam               │ 470,200    │ --       │      │
│ │  5 │ [👤] River               │ 468,500    │ ↑ +2     │      │
│ │  6 │ [👤] Takemura            │ 465,900    │ ↓ -1     │      │
│ │    ...                                                        │
│ └────┴──────────────────────────┴────────────┴──────────┘      │
│                                                                   │
│                                     [Load More]                   │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Leaderboard Component

```tsx
interface LeaderboardProps {
  type: 'overall' | 'combat' | 'economy' | 'social' | 'pvp' | 'achievements';
  scope: 'global' | 'regional' | 'server';
  seasonId?: string;
}

const Leaderboard: React.FC<LeaderboardProps> = ({
  type,
  scope,
  seasonId
}) => {
  const { data, isLoading } = useLeaderboard(type, scope, seasonId);
  const currentPlayer = useCurrentPlayer();
  
  if (isLoading) return <LoadingSpinner />;
  
  return (
    <div className="leaderboard">
      {/* Player Position Card */}
      <PlayerPositionCard 
        rank={data.playerPosition.rank}
        score={data.playerPosition.score}
        change={data.playerPosition.change}
        percentile={data.playerPosition.percentile}
      />
      
      {/* Nearby Players (optional) */}
      {data.nearbyPlayers && (
        <NearbyPlayers players={data.nearbyPlayers} />
      )}
      
      {/* Top 100 Table */}
      <div className="leaderboard-table">
        <table>
          <thead>
            <tr>
              <th>Rank</th>
              <th>Player</th>
              <th>Score</th>
              <th>Change</th>
            </tr>
          </thead>
          <tbody>
            {data.entries.map((entry, index) => (
              <LeaderboardRow 
                key={entry.playerId}
                entry={entry}
                isCurrentPlayer={entry.playerId === currentPlayer.id}
              />
            ))}
          </tbody>
        </table>
      </div>
      
      {/* Load More */}
      {data.hasMore && (
        <button 
          onClick={() => loadMore()}
          className="load-more-btn"
        >
          Load More
        </button>
      )}
    </div>
  );
};
```

---

## Player Position Card

```tsx
const PlayerPositionCard: React.FC<{rank, score, change, percentile}> = ({
  rank,
  score,
  change,
  percentile
}) => {
  return (
    <div className="player-position-card">
      <div className="position-header">
        <h3>YOUR POSITION</h3>
        <TierBadge rank={rank} />
      </div>
      
      <div className="position-stats">
        <div className="stat">
          <label>Rank</label>
          <span className="rank-value">
            #{rank.toLocaleString()}
            <small>(Top {percentile}%)</small>
          </span>
        </div>
        
        <div className="stat">
          <label>Score</label>
          <span className="score-value">
            {score.toLocaleString()}
          </span>
        </div>
        
        <div className="stat">
          <label>24h Change</label>
          <span className={`change ${change >= 0 ? 'positive' : 'negative'}`}>
            {change >= 0 ? '↑' : '↓'} {Math.abs(change)}
          </span>
        </div>
      </div>
      
      {/* Progress to Next Tier */}
      <div className="tier-progress">
        <label>Next Tier: Gold (Top 1000)</label>
        <ProgressBar 
          current={rank}
          max={1000}
          inverted
        />
        <span>{1000 - rank} ranks to go</span>
      </div>
    </div>
  );
};
```

---

## Leaderboard Row

```tsx
const LeaderboardRow: React.FC<{entry, isCurrentPlayer}> = ({
  entry,
  isCurrentPlayer
}) => {
  return (
    <tr className={`leaderboard-row ${isCurrentPlayer ? 'current-player' : ''}`}>
      {/* Rank */}
      <td className="rank-cell">
        {entry.rank <= 3 ? (
          <MedalIcon rank={entry.rank} />
        ) : (
          <span className="rank-number">#{entry.rank}</span>
        )}
      </td>
      
      {/* Player Info */}
      <td className="player-cell">
        <div className="player-info">
          <Avatar 
            src={entry.playerAvatar}
            size="small"
            border={getRankBorderColor(entry.rank)}
          />
          <div className="player-details">
            <div className="player-name">
              {entry.playerName}
              {isCurrentPlayer && <span className="you-badge">YOU</span>}
            </div>
            <div className="player-meta">
              Lvl {entry.playerLevel} | {entry.region}
              {entry.guildName && (
                <span className="guild">
                  [<GuildIcon />{entry.guildName}]
                </span>
              )}
            </div>
          </div>
        </div>
      </td>
      
      {/* Score */}
      <td className="score-cell">
        <span className="score">
          {entry.score.toLocaleString()}
        </span>
      </td>
      
      {/* Change */}
      <td className="change-cell">
        {entry.previousRank && (
          <span className={`rank-change ${getRankChangeClass(entry)}`}>
            {getRankChangeIcon(entry)} {Math.abs(entry.rank - entry.previousRank)}
          </span>
        )}
      </td>
    </tr>
  );
};
```

---

## Category Tabs

```tsx
const LeaderboardCategories: React.FC = () => {
  const [activeCategory, setActiveCategory] = useState('overall');
  
  const categories = [
    { id: 'overall', name: 'Overall Power', icon: '⚡' },
    { id: 'combat', name: 'Combat', icon: '⚔️' },
    { id: 'economy', name: 'Economy', icon: '💰' },
    { id: 'social', name: 'Social', icon: '👥' },
    { id: 'pvp', name: 'PvP Rating', icon: '🎯' },
    { id: 'achievements', name: 'Achievements', icon: '🏆' }
  ];
  
  return (
    <div className="category-tabs">
      {categories.map(category => (
        <button
          key={category.id}
          className={`category-tab ${activeCategory === category.id ? 'active' : ''}`}
          onClick={() => setActiveCategory(category.id)}
        >
          <span className="icon">{category.icon}</span>
          <span className="name">{category.name}</span>
        </button>
      ))}
    </div>
  );
};
```

---

## Scope Filter

```tsx
const LeaderboardScopeFilter: React.FC = () => {
  const [scope, setScope] = useState('global');
  const currentPlayer = useCurrentPlayer();
  
  return (
    <div className="scope-filter">
      <button 
        className={scope === 'global' ? 'active' : ''}
        onClick={() => setScope('global')}
      >
        🌍 Global
      </button>
      
      <button 
        className={scope === 'regional' ? 'active' : ''}
        onClick={() => setScope('regional')}
      >
        🏙️ {currentPlayer.region}
      </button>
      
      <button 
        className={scope === 'server' ? 'active' : ''}
        onClick={() => setScope('server')}
      >
        🖥️ My Server
      </button>
    </div>
  );
};
```

---

## Seasonal League View

```tsx
const SeasonalLeagueView: React.FC = () => {
  const { data: season } = useCurrentSeason();
  
  return (
    <div className="seasonal-league">
      {/* Season Header */}
      <div className="season-header">
        <h2>Season {season.name}</h2>
        <div className="season-meta">
          <span>
            <CalendarIcon /> 
            Ends in {formatTimeRemaining(season.endDate)}
          </span>
        </div>
      </div>
      
      {/* Season Rewards */}
      <div className="season-rewards">
        <h3>Season Rewards</h3>
        <div className="rewards-grid">
          <RewardTier 
            tier="Diamond"
            ranks="1-100"
            rewards={season.rewards.diamond}
          />
          <RewardTier 
            tier="Platinum"
            ranks="101-1000"
            rewards={season.rewards.platinum}
          />
          <RewardTier 
            tier="Gold"
            ranks="1001-10000"
            rewards={season.rewards.gold}
          />
        </div>
      </div>
      
      {/* Leaderboard */}
      <Leaderboard 
        type="overall"
        scope="global"
        seasonId={season.id}
      />
    </div>
  );
};
```

---

## Real-Time Updates

```tsx
// WebSocket для live updates
useEffect(() => {
  const ws = new WebSocket('wss://api.necp.game/v1/social/leaderboard');
  
  ws.on('rank_change', (data) => {
    // Update player's rank in real-time
    updatePlayerRank(data.playerId, data.newRank);
    
    if (data.playerId === currentPlayer.id) {
      showNotification({
        type: 'rank_change',
        message: `Your rank changed: ${data.oldRank} → ${data.newRank}`
      });
    }
  });
  
  ws.on('new_leader', (data) => {
    // Show notification when someone becomes #1
    showNotification({
      type: 'new_leader',
      message: `${data.playerName} is now rank #1!`
    });
  });
  
  return () => ws.close();
}, [currentPlayer.id]);
```

---

## API Integration

```tsx
const useLeaderboard = (type: string, scope: string, seasonId?: string) => {
  return useQuery({
    queryKey: ['leaderboard', type, scope, seasonId],
    queryFn: () => api.get(`/api/v1/leaderboards/${type}`, {
      params: { scope, seasonId }
    }),
    refetchInterval: 60000 // Refresh every minute
  });
};

const usePlayerPosition = (playerId: string, leaderboardCode: string) => {
  return useQuery({
    queryKey: ['player-position', playerId, leaderboardCode],
    queryFn: () => api.get(`/api/v1/leaderboards/${leaderboardCode}/player/${playerId}`)
  });
};
```

---

## Связанные документы

- [Leaderboard System Backend](../../backend/leaderboard/leaderboard-core.md)
- [Achievement UI](../achievements/ui-achievements-main.md)
- [Profile UI](../profile/ui-profile.md)
