# UI Admin Panel - Part 1: Dashboard & Moderation

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:31  
**api-readiness:** ready

[Навигация](./README.md) | [Part 2 →](./part2-analytics-controls.md)

---

## Краткое описание

**UI Admin Panel** - защищенный интерфейс для администраторов и модераторов.

**Ключевые разделы:**
- ✅ Player Management
- ✅ Moderation Tools
- ✅ Real-Time Analytics
- ✅ World State Control

---

- **Status:** queued
- **Last Updated:** 2025-11-08 01:12
---

## Главный Dashboard

### Layout

```
┌─────────────────────────────────────────────────────────────────┐
│ ADMIN PANEL                   [Admin: V]  [Role: SUPER_ADMIN]   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ [Dashboard] [Players] [Moderation] [Analytics] [World] [Events] │
│                                                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ 📊 REAL-TIME METRICS                                             │
│                                                                   │
│ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐             │
│ │ 👥 Online    │ │ 💬 Reports   │ │ 🚫 Bans      │             │
│ │ 12,543       │ │ 15 Pending   │ │ 3 Today      │             │
│ │ ↑ +523 (1h)  │ │ ⚠️ 3 Urgent   │ │ 1,234 Total  │             │
│ └──────────────┘ └──────────────┘ └──────────────┘             │
│                                                                   │
│ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐             │
│ │ 💰 Economy   │ │ ⚔️ Combat     │ │ 📈 Growth    │             │
│ │ 45.2M Eddies │ │ 234 Active   │ │ +12% (7d)    │             │
│ │ in Market    │ │ Sessions     │ │ New Players  │             │
│ └──────────────┘ └──────────────┘ └──────────────┘             │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Player Management

### Player Search

```tsx
const PlayerManagementPanel: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedPlayer, setSelectedPlayer] = useState<Player | null>(null);
  
  return (
    <div className="player-management-panel">
      {/* Search Bar */}
      <div className="search-section">
        <input
          type="text"
          placeholder="Search by username, email, or player ID..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="player-search-input"
        />
        <button onClick={() => searchPlayers(searchQuery)}>
          Search
        </button>
      </div>
      
      {/* Search Results */}
      {searchResults && (
        <div className="search-results">
          <table className="players-table">
            <thead>
              <tr>
                <th>Player ID</th>
                <th>Username</th>
                <th>Level</th>
                <th>Status</th>
                <th>Last Login</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {searchResults.map(player => (
                <tr key={player.id}>
                  <td>{player.id}</td>
                  <td>
                    <PlayerLink player={player} />
                  </td>
                  <td>Lvl {player.level}</td>
                  <td>
                    <StatusBadge status={player.accountStatus} />
                  </td>
                  <td>{formatRelativeTime(player.lastLoginAt)}</td>
                  <td>
                    <ActionButtons player={player} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      
      {/* Player Detail Modal */}
      {selectedPlayer && (
        <PlayerDetailModal 
          player={selectedPlayer}
          onClose={() => setSelectedPlayer(null)}
        />
      )}
    </div>
  );
};
```

---

## Moderation Tools

### Report Queue

```tsx
const ModerationReportsQueue: React.FC = () => {
  const { data: reports } = useModerationReports();
  const [selectedReport, setSelectedReport] = useState<Report | null>(null);
  
  return (
    <div className="moderation-reports-queue">
      {/* Filters */}
      <div className="reports-filters">
        <select>
          <option value="all">All Status</option>
          <option value="pending">Pending</option>
          <option value="in_review">In Review</option>
          <option value="resolved">Resolved</option>
        </select>
        
        <select>
          <option value="all">All Reasons</option>
          <option value="cheating">Cheating</option>
          <option value="harassment">Harassment</option>
          <option value="offensive_name">Offensive Name</option>
          <option value="exploit">Exploit Abuse</option>
        </select>
        
        <button onClick={() => refreshReports()}>
          <RefreshIcon /> Refresh
        </button>
      </div>
      
      {/* Reports List */}
      <div className="reports-list">
        {reports.map(report => (
          <div 
            key={report.id}
            className={`report-card ${report.priority === 'urgent' ? 'urgent' : ''}`}
            onClick={() => setSelectedReport(report)}
          >
            <div className="report-header">
              <span className="report-id">#{report.id}</span>
              <span className={`priority ${report.priority}`}>
                {report.priority === 'urgent' ? '⚠️' : ''} {report.priority}
              </span>
              <span className="status">{report.status}</span>
            </div>
            
            <div className="report-content">
              <div className="reported-player">
                <strong>Reported:</strong> {report.reportedPlayer.username}
              </div>
              <div className="reason">
                <strong>Reason:</strong> {report.reason}
              </div>
              <div className="reporter">
                <strong>Reporter:</strong> {report.reporter.username}
              </div>
              <div className="timestamp">
                {formatRelativeTime(report.createdAt)}
              </div>
            </div>
          </div>
        ))}
      </div>
      
      {/* Report Detail Modal */}
      {selectedReport && (
        <ReportDetailModal 
          report={selectedReport}
          onClose={() => setSelectedReport(null)}
        />
      )}
    </div>
  );
};
```

### Ban Player Modal

```tsx
const BanPlayerModal: React.FC<{player}> = ({player}) => {
  const [duration, setDuration] = useState('7d');
  const [reason, setReason] = useState('');
  const [publicReason, setPublicReason] = useState('');
  
  const handleBan = async () => {
    await banPlayer({
      playerId: player.id,
      duration: parseDuration(duration),
      reason,
      publicReason,
      adminId: getCurrentAdmin().id
    });
    
    showNotification('Player banned successfully');
  };
  
  return (
    <Modal>
      <h2>Ban Player: {player.username}</h2>
      
      <div className="ban-form">
        <label>
          Duration:
          <select value={duration} onChange={(e) => setDuration(e.target.value)}>
            <option value="1h">1 Hour</option>
            <option value="24h">24 Hours</option>
            <option value="7d">7 Days</option>
            <option value="30d">30 Days</option>
            <option value="permanent">Permanent</option>
          </select>
        </label>
        
        <label>
          Internal Reason:
          <textarea
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            placeholder="Detailed reason for internal audit log..."
          />
        </label>
        
        <label>
          Public Reason:
          <input
            type="text"
            value={publicReason}
            onChange={(e) => setPublicReason(e.target.value)}
            placeholder="Reason shown to player..."
          />
        </label>
        
        <div className="ban-preview">
          <strong>Preview:</strong>
          <p>Player will be banned until: {calculateBanExpiry(duration)}</p>
          <p>Public message: "{publicReason || 'Violation of Terms of Service'}"</p>
        </div>
        
        <div className="modal-actions">
          <button className="btn-danger" onClick={handleBan}>
            Confirm Ban
          </button>
          <button className="btn-secondary" onClick={onClose}>
            Cancel
          </button>
        </div>
      </div>
    </Modal>
  );
};
```

---

[Part 2: Analytics & Controls →](./part2-analytics-controls.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:31) - Создан с полным TSX кодом (dashboard, player management, moderation)
- v1.0.0 (2025-11-07 02:20) - Создан
