# UI Admin Panel - Part 2: Analytics & Controls

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:32  
**api-readiness:** ready

[← Part 1](./part1-dashboard-moderation.md) | [Навигация](./README.md)

---

- **Status:** queued
- **Last Updated:** 2025-11-08 01:26
---

## Analytics Dashboard

```tsx
const AdminAnalyticsDashboard: React.FC = () => {
  const { data: metrics } = useRealtimeMetrics();
  
  return (
    <div className="admin-analytics-dashboard">
      {/* Real-Time Metrics */}
      <div className="metrics-grid">
        <MetricCard
          title="Online Players"
          value={metrics.onlinePlayers}
          change={metrics.onlinePlayersChange}
          icon={<UsersIcon />}
        />
        
        <MetricCard
          title="Active Sessions"
          value={metrics.activeSessions}
          icon={<GameIcon />}
        />
        
        <MetricCard
          title="Server Load"
          value={`${metrics.serverLoad}%`}
          status={metrics.serverLoad > 80 ? 'warning' : 'ok'}
          icon={<ServerIcon />}
        />
        
        <MetricCard
          title="Economy Health"
          value={metrics.economyHealth}
          icon={<CurrencyIcon />}
        />
      </div>
      
      {/* Charts */}
      <div className="charts-section">
        <div className="chart-container">
          <h3>Player Activity (24h)</h3>
          <LineChart
            data={metrics.playerActivity24h}
            xAxis="time"
            yAxis="players"
          />
        </div>
        
        <div className="chart-container">
          <h3>Revenue (7d)</h3>
          <BarChart
            data={metrics.revenue7d}
            xAxis="date"
            yAxis="revenue"
          />
        </div>
      </div>
    </div>
  );
};
```

---

## World State Control

```tsx
const WorldStateControlPanel: React.FC = () => {
  const { data: worldState } = useWorldState();
  const [selectedZone, setSelectedZone] = useState<string | null>(null);
  
  return (
    <div className="world-state-control-panel">
      {/* Zone Selection */}
      <div className="zone-selector">
        <h3>Select Zone</h3>
        <select 
          value={selectedZone || ''}
          onChange={(e) => setSelectedZone(e.target.value)}
        >
          <option value="">-- Select Zone --</option>
          {worldState.zones.map(zone => (
            <option key={zone.id} value={zone.id}>
              {zone.name} ({zone.currentPlayers}/{zone.maxPlayers} players)
            </option>
          ))}
        </select>
      </div>
      
      {/* World Controls */}
      <div className="world-controls">
        <h3>Global Controls</h3>
        
        <div className="control-group">
          <label>Time of Day:</label>
          <select onChange={(e) => setTimeOfDay(e.target.value)}>
            <option value="dawn">Dawn</option>
            <option value="day">Day</option>
            <option value="dusk">Dusk</option>
            <option value="night">Night</option>
          </select>
        </div>
        
        <div className="control-group">
          <label>Weather:</label>
          <select onChange={(e) => setWeather(e.target.value)}>
            <option value="clear">Clear</option>
            <option value="rain">Rain</option>
            <option value="storm">Storm</option>
            <option value="fog">Fog</option>
            <option value="acid_rain">Acid Rain (Cyberpunk)</option>
          </select>
        </div>
        
        <div className="control-group">
          <label>XP Multiplier:</label>
          <input
            type="number"
            min="0.5"
            max="5.0"
            step="0.1"
            defaultValue="1.0"
            onChange={(e) => setXpMultiplier(parseFloat(e.target.value))}
          />
        </div>
        
        <div className="control-group">
          <label>Loot Multiplier:</label>
          <input
            type="number"
            min="0.5"
            max="5.0"
            step="0.1"
            defaultValue="1.0"
            onChange={(e) => setLootMultiplier(parseFloat(e.target.value))}
          />
        </div>
        
        <button 
          className="btn-primary"
          onClick={applyWorldSettings}
        >
          Apply Changes
        </button>
      </div>
    </div>
  );
};
```

---

## Event Management

```tsx
const AdminEventManager: React.FC = () => {
  const [eventType, setEventType] = useState<EventType>('DOUBLE_XP');
  const [duration, setDuration] = useState(24);
  const [zones, setZones] = useState<string[]>([]);
  
  const handleCreateEvent = async () => {
    await createAdminEvent({
      type: eventType,
      duration: duration * 3600, // hours to seconds
      affectedZones: zones,
      startTime: new Date(),
      createdBy: getCurrentAdmin().id
    });
    
    showNotification('Event created successfully');
  };
  
  return (
    <div className="admin-event-manager">
      <h2>Create World Event</h2>
      
      <div className="event-form">
        <label>
          Event Type:
          <select value={eventType} onChange={(e) => setEventType(e.target.value as EventType)}>
            <option value="DOUBLE_XP">Double XP</option>
            <option value="DOUBLE_LOOT">Double Loot</option>
            <option value="BOSS_SPAWN">Special Boss Spawn</option>
            <option value="FACTION_WAR">Faction War</option>
            <option value="CYBERPSYCHO_ATTACK">Cyberpsycho Attack</option>
            <option value="ARASAKA_RAID">Arasaka Corp Raid</option>
          </select>
        </label>
        
        <label>
          Duration (hours):
          <input
            type="number"
            min="1"
            max="168"
            value={duration}
            onChange={(e) => setDuration(parseInt(e.target.value))}
          />
        </label>
        
        <label>
          Affected Zones:
          <MultiSelect
            options={availableZones}
            value={zones}
            onChange={setZones}
          />
        </label>
        
        <label>
          Description:
          <textarea placeholder="Event description..." />
        </label>
        
        <button className="btn-primary" onClick={handleCreateEvent}>
          Create Event
        </button>
      </div>
      
      {/* Active Events */}
      <div className="active-events">
        <h3>Active Events</h3>
        {activeEvents.map(event => (
          <div key={event.id} className="event-card">
            <div className="event-header">
              <strong>{event.type}</strong>
              <span className="event-time">
                Ends in: {formatDuration(event.endsAt)}
              </span>
            </div>
            <div className="event-zones">
              Zones: {event.zones.join(', ')}
            </div>
            <button 
              className="btn-danger-small"
              onClick={() => cancelEvent(event.id)}
            >
              Cancel Event
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};
```

---

## Audit Log

```tsx
const AdminAuditLog: React.FC = () => {
  const { data: auditLog } = useAuditLog();
  
  return (
    <div className="admin-audit-log">
      <h2>Admin Action History</h2>
      
      <table className="audit-log-table">
        <thead>
          <tr>
            <th>Timestamp</th>
            <th>Admin</th>
            <th>Action</th>
            <th>Target</th>
            <th>Details</th>
            <th>Result</th>
          </tr>
        </thead>
        <tbody>
          {auditLog.map(entry => (
            <tr key={entry.id}>
              <td>{formatDateTime(entry.timestamp)}</td>
              <td>{entry.adminUsername}</td>
              <td>
                <ActionBadge action={entry.action} />
              </td>
              <td>
                {entry.targetType}: {entry.targetId}
              </td>
              <td>
                <pre>{JSON.stringify(entry.details, null, 2)}</pre>
              </td>
              <td>
                <StatusBadge status={entry.success ? 'success' : 'failed'} />
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

## API Endpoints

### Admin API
- **GET** `/api/v1/admin/dashboard` - метрики dashboard
- **GET** `/api/v1/admin/players/search` - поиск игроков
- **GET** `/api/v1/admin/players/{id}` - детали игрока
- **POST** `/api/v1/admin/players/{id}/ban` - забанить
- **POST** `/api/v1/admin/players/{id}/kick` - кикнуть
- **GET** `/api/v1/admin/reports` - список репортов
- **POST** `/api/v1/admin/reports/{id}/resolve` - закрыть репорт
- **POST** `/api/v1/admin/events/create` - создать событие
- **GET** `/api/v1/admin/audit-log` - журнал действий

---

## Связанные документы

- [Session Management](../../backend/session-management/README.md)
- [Realtime Server](../../backend/realtime-server/README.md)
- [Authentication](../../backend/authentication-authorization/README.md)

---

[← Назад к навигации](./README.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:32) - Создан с полным TSX кодом (analytics, world control, events, audit)
- v1.0.0 (2025-11-07 02:20) - Создан
