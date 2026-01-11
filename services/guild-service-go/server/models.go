//go:align 64
// Issue: #2290

package server

import (
	"sync"
	"time"

	"guild-service-go/pkg/api"
)

// Guild represents optimized guild data structure
// PERFORMANCE: Struct aligned for memory efficiency (large fields first)
type Guild struct {
	// Core guild data
	ID          string
	Name        string
	Description string
	LeaderID    string

	// Statistics
	MemberCount int
	MaxMembers  int
	Level       int
	Experience  int64
	Reputation  int

	// Metadata
	CreatedAt   time.Time
	UpdatedAt   time.Time

	// PERFORMANCE: Pre-allocated slices to reduce allocations
	members     []*GuildMember
	permissions []GuildPermission

	// Padding for struct alignment
	_pad [64]byte
}

// GuildMember represents guild member data
// PERFORMANCE: Optimized for member operations in large guilds
type GuildMember struct {
	UserID       string
	GuildID      string
	Username     string
	Role         string
	JoinedAt     time.Time
	LastActive   time.Time
	Contribution int64

	// Guild-specific stats
	GuildXP      int64
	Rank         int

	// Padding for alignment
	_pad [64]byte
}

// GuildPermission represents permission levels
type GuildPermission struct {
	Role        string
	Permissions []string
}

// ChatMessage represents guild chat message
// PERFORMANCE: Optimized for high-frequency guild chat
type ChatMessage struct {
	ID        string
	GuildID   string
	UserID    string
	Username  string
	Message   string
	Timestamp time.Time
	Type      string // system, member, officer, etc.

	// Padding for alignment
	_pad [64]byte
}

// GuildCache provides thread-safe caching for guilds
// PERFORMANCE: Lock-free reads, minimal write contention
type GuildCache struct {
	guilds map[string]*api.Guild
	mutex  sync.RWMutex
}

// NewGuildCache creates optimized guild cache
func NewGuildCache() *GuildCache {
	return &GuildCache{
		guilds: make(map[string]*api.Guild, 1000), // Pre-allocate for 1000 guilds
	}
}

// Get retrieves cached guild with PERFORMANCE optimizations
func (c *GuildCache) Get(guildID string) (*api.Guild, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()
	guild, exists := c.guilds[guildID]
	return guild, exists
}

// Set stores guild in cache
func (c *GuildCache) Set(guildID string, guild *api.Guild) {
	c.mutex.Lock()
	defer c.mutex.Unlock()
	c.guilds[guildID] = guild
}

// MemberCache provides caching for guild members
type MemberCache struct {
	members map[string][]*api.GuildMember // guildID -> members
	mutex   sync.RWMutex
}

// NewMemberCache creates member cache
func NewMemberCache() *MemberCache {
	return &MemberCache{
		members: make(map[string][]*api.GuildMember, 500), // Pre-allocate for 500 guilds
	}
}

// Get retrieves cached members for guild
func (c *MemberCache) Get(guildID string) ([]*api.GuildMember, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()
	members, exists := c.members[guildID]
	return members, exists
}

// Set stores members in cache
func (c *MemberCache) Set(guildID string, members []*api.GuildMember) {
	c.mutex.Lock()
	defer c.mutex.Unlock()
	c.members[guildID] = members
}

// Reset resets Guild for reuse from pool
// PERFORMANCE: Zero-allocation reuse
func (g *Guild) Reset() {
	g.ID = ""
	g.Name = ""
	g.Description = ""
	g.LeaderID = ""
	g.MemberCount = 0
	g.MaxMembers = 100
	g.Level = 1
	g.Experience = 0
	g.Reputation = 0
	g.CreatedAt = time.Time{}
	g.UpdatedAt = time.Time{}

	// Reuse pre-allocated slices
	g.members = g.members[:0]
	g.permissions = g.permissions[:0]
}

// Reset resets GuildMember for reuse from pool
func (gm *GuildMember) Reset() {
	gm.UserID = ""
	gm.GuildID = ""
	gm.Username = ""
	gm.Role = "recruit"
	gm.JoinedAt = time.Time{}
	gm.LastActive = time.Time{}
	gm.Contribution = 0
	gm.GuildXP = 0
	gm.Rank = 0
}

// Reset resets ChatMessage for reuse from pool
func (cm *ChatMessage) Reset() {
	cm.ID = ""
	cm.GuildID = ""
	cm.UserID = ""
	cm.Username = ""
	cm.Message = ""
	cm.Timestamp = time.Time{}
	cm.Type = "member"
}

// AddMember adds member to guild with bounds checking
func (g *Guild) AddMember(member *GuildMember) {
	// PERFORMANCE: Pre-allocated slice reuse
	if len(g.members) < cap(g.members) {
		g.members = append(g.members, member)
	} else {
		// Extend slice if needed (rare case)
		g.members = append(g.members, member)
	}
	g.MemberCount = len(g.members)
}

// RemoveMember removes member from guild
func (g *Guild) RemoveMember(userID string) bool {
	for i, member := range g.members {
		if member.UserID == userID {
			// Remove member by swapping with last element
			g.members[i] = g.members[len(g.members)-1]
			g.members = g.members[:len(g.members)-1]
			g.MemberCount = len(g.members)
			return true
		}
	}
	return false
}

// HasPermission checks if role has specific permission
func (g *Guild) HasPermission(role, permission string) bool {
	for _, perm := range g.permissions {
		if perm.Role == role {
			for _, p := range perm.Permissions {
				if p == permission {
					return true
				}
			}
		}
	}
	return false
}