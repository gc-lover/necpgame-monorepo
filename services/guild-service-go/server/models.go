package server

// API Request/Response models for Guild Service

// CreateGuildRequest represents the request to create a new guild
type CreateGuildRequest struct {
	GuildID              string                     `json:"guild_id,omitempty"`
	Name                 string                     `json:"name"`
	Description          string                     `json:"description,omitempty"`
	Motto                string                     `json:"motto,omitempty"`
	Faction              string                     `json:"faction,omitempty"`
	LeaderID             string                     `json:"leader_id"`
	MaxMembers           int                        `json:"max_members"`
	Region               string                     `json:"region,omitempty"`
	RecruitmentOpen      bool                       `json:"recruitment_open,omitempty"`
	ApplicationRequired  bool                       `json:"application_required,omitempty"`
	MinLevelRequirement  int                        `json:"min_level_requirement,omitempty"`
	HeadquartersLocation GuildHeadquartersLocation `json:"headquarters_location,omitempty"`
	Colors               GuildColorScheme          `json:"colors,omitempty"`
	Policies             GuildPolicySettings       `json:"policies,omitempty"`
}

// GuildHeadquartersLocation represents guild headquarters coordinates
type GuildHeadquartersLocation struct {
	Zone        string  `json:"zone"`
	Coordinates Coordinates `json:"coordinates"`
}

// Coordinates represents 3D coordinates
type Coordinates struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// GuildColorScheme represents guild color customization
type GuildColorScheme struct {
	Primary   string `json:"primary,omitempty"`
	Secondary string `json:"secondary,omitempty"`
	EmblemURL string `json:"emblem_url,omitempty"`
}

// GuildPolicySettings represents guild policy configuration
type GuildPolicySettings struct {
	PvpEnabled             bool    `json:"pvp_enabled,omitempty"`
	TerritoryClaimsEnabled bool    `json:"territory_claims_enabled,omitempty"`
	WarParticipation       string  `json:"war_participation,omitempty"` // "offensive", "defensive", "neutral"
	ContractSharing        bool    `json:"contract_sharing,omitempty"`
	ResourceSharing        bool    `json:"resource_sharing,omitempty"`
	TaxRate                float64 `json:"tax_rate,omitempty"`
}

// CreateGuildResponse represents the response after creating a guild
type CreateGuildResponse struct {
	GuildID     string `json:"guild_id"`
	Name        string `json:"name"`
	LeaderID    string `json:"leader_id"`
	Status      string `json:"status"`
	MemberCount int    `json:"member_count"`
	CreatedAt   int64  `json:"created_at"`
}

// ListGuildsResponse represents the response for listing guilds
type ListGuildsResponse struct {
	Guilds     []GuildSummary `json:"guilds"`
	TotalCount int            `json:"total_count"`
}

// GuildSummary represents a summary of guild information
type GuildSummary struct {
	GuildID       string `json:"guild_id"`
	Name          string `json:"name"`
	Faction       string `json:"faction"`
	LeaderID      string `json:"leader_id"`
	MemberCount   int    `json:"member_count"`
	Level         int    `json:"level"`
	Reputation    int    `json:"reputation"`
	Status        string `json:"status"`
	Region        string `json:"region"`
	RecruitmentOpen bool  `json:"recruitment_open"`
	CreatedAt     int64  `json:"created_at"`
}

// GetGuildResponse represents the response for getting guild details
type GetGuildResponse struct {
	Guild *GuildDetails `json:"guild"`
}

// GuildDetails represents detailed guild information
type GuildDetails struct {
	GuildID           string        `json:"guild_id"`
	Name              string        `json:"name"`
	Description       string        `json:"description"`
	Motto             string        `json:"motto"`
	Faction           string        `json:"faction"`
	LeaderID          string        `json:"leader_id"`
	MemberCount       int           `json:"member_count"`
	Level             int           `json:"level"`
	Experience        int           `json:"experience"`
	Reputation        int           `json:"reputation"`
	Wealth            int           `json:"wealth"`
	Status            string        `json:"status"`
	Region            string        `json:"region"`
	Headquarters      GuildLocation `json:"headquarters"`
	Colors            GuildColors   `json:"colors"`
	Policies          GuildPolicies `json:"policies"`
	RecruitmentOpen   bool          `json:"recruitment_open"`
	ApplicationRequired bool        `json:"application_required"`
	MinLevelRequirement int         `json:"min_level_requirement"`
	TerritoriesControlled int       `json:"territories_controlled"`
	WarsActive        int           `json:"wars_active"`
	AlliancesActive   int           `json:"alliances_active"`
	CreatedAt         int64         `json:"created_at"`
	LastUpdatedAt     int64         `json:"last_updated_at"`
	LastActivity      int64         `json:"last_activity"`
}

// JoinGuildRequest represents the request to join a guild
type JoinGuildRequest struct {
	PlayerID    string `json:"player_id"`
	Message     string `json:"message,omitempty"`
}

// JoinGuildResponse represents the response after requesting to join a guild
type JoinGuildResponse struct {
	GuildID   string `json:"guild_id"`
	PlayerID  string `json:"player_id"`
	Status    string `json:"status"` // "accepted", "pending_approval", "rejected"
	JoinedAt  int64  `json:"joined_at,omitempty"`
	Message   string `json:"message,omitempty"`
}

// GetGuildMembersResponse represents the response for getting guild members
type GetGuildMembersResponse struct {
	GuildID    string        `json:"guild_id"`
	Members    []GuildMember `json:"members"`
	TotalCount int           `json:"total_count"`
}

// ClaimTerritoryRequest represents the request to claim territory
type ClaimTerritoryRequest struct {
	TerritoryID string  `json:"territory_id"`
	Zone        string  `json:"zone"`
	Coordinates Coordinates `json:"coordinates"`
	ClaimType   string  `json:"claim_type"` // "permanent", "temporary"
	Duration    int64   `json:"duration,omitempty"` // for temporary claims
}

// ClaimTerritoryResponse represents the response after claiming territory
type ClaimTerritoryResponse struct {
	TerritoryID   string `json:"territory_id"`
	GuildID       string `json:"guild_id"`
	Status        string `json:"status"`
	ClaimedAt     int64  `json:"claimed_at"`
	ExpiresAt     int64  `json:"expires_at,omitempty"`
	ResourceBonus int    `json:"resource_bonus"`
}

// DeclareWarRequest represents the request to declare war
type DeclareWarRequest struct {
	TargetGuildID string `json:"target_guild_id"`
	Reason        string `json:"reason,omitempty"`
	WarType       string `json:"war_type"` // "territorial", "resource", "honor"
}

// DeclareWarResponse represents the response after declaring war
type DeclareWarResponse struct {
	WarID         string `json:"war_id"`
	AttackerID    string `json:"attacker_id"`
	DefenderID    string `json:"defender_id"`
	Status        string `json:"status"`
	DeclaredAt    int64  `json:"declared_at"`
	WarType       string `json:"war_type"`
	Reason        string `json:"reason"`
}

// FormAllianceRequest represents the request to form an alliance
type FormAllianceRequest struct {
	TargetGuildID string `json:"target_guild_id"`
	AllianceType  string `json:"alliance_type"` // "trade", "mutual_defense", "full"
	Duration      int64  `json:"duration,omitempty"` // in seconds, 0 for permanent
	Terms         string `json:"terms,omitempty"`
}

// FormAllianceResponse represents the response after forming an alliance
type FormAllianceResponse struct {
	AllianceID     string `json:"alliance_id"`
	Guild1ID       string `json:"guild1_id"`
	Guild2ID       string `json:"guild2_id"`
	Status         string `json:"status"`
	AllianceType   string `json:"alliance_type"`
	FormedAt       int64  `json:"formed_at"`
	ExpiresAt      int64  `json:"expires_at,omitempty"`
	Terms          string `json:"terms"`
}

// CreateContractRequest represents the request to create a guild contract
type CreateContractRequest struct {
	Title       string            `json:"title"`
	Description string            `json:"description"`
	Type        string            `json:"type"` // "bounty", "escort", "resource_gathering"
	Reward      ContractReward    `json:"reward"`
	Requirements map[string]interface{} `json:"requirements"`
	Deadline    int64             `json:"deadline,omitempty"`
}

// ContractReward represents the reward for completing a contract
type ContractReward struct {
	CurrencyType string `json:"currency_type"`
	Amount       int    `json:"amount"`
	Items        []string `json:"items,omitempty"`
	Experience   int      `json:"experience,omitempty"`
}

// CreateContractResponse represents the response after creating a contract
type CreateContractResponse struct {
	ContractID string `json:"contract_id"`
	GuildID    string `json:"guild_id"`
	Title      string `json:"title"`
	Status     string `json:"status"`
	CreatedAt  int64  `json:"created_at"`
	Deadline   int64  `json:"deadline,omitempty"`
}

// GetGuildBankResponse represents the response for guild bank status
type GetGuildBankResponse struct {
	GuildID      string         `json:"guild_id"`
	TotalWealth  int            `json:"total_wealth"`
	Currency     int            `json:"currency"`
	Resources    map[string]int `json:"resources"`
	Items        []BankItem     `json:"items"`
	LastUpdated  int64          `json:"last_updated"`
}

// BankItem represents an item stored in the guild bank
type BankItem struct {
	ItemID   string `json:"item_id"`
	Name     string `json:"name"`
	Quantity int    `json:"quantity"`
	Quality  string `json:"quality"`
}

// DepositToBankRequest represents the request to deposit resources
type DepositToBankRequest struct {
	PlayerID   string         `json:"player_id"`
	Currency   int            `json:"currency,omitempty"`
	Resources  map[string]int `json:"resources,omitempty"`
	Items      []DepositItem  `json:"items,omitempty"`
}

// DepositItem represents an item being deposited
type DepositItem struct {
	ItemID   string `json:"item_id"`
	Quantity int    `json:"quantity"`
}

// DepositToBankResponse represents the response after depositing to bank
type DepositToBankResponse struct {
	GuildID         string `json:"guild_id"`
	PlayerID        string `json:"player_id"`
	DepositedCurrency int   `json:"deposited_currency"`
	DepositedResources map[string]int `json:"deposited_resources"`
	DepositedItems  []DepositItem `json:"deposited_items"`
	NewTotalWealth  int           `json:"new_total_wealth"`
	DepositedAt     int64         `json:"deposited_at"`
}

// WithdrawFromBankRequest represents the request to withdraw from bank
type WithdrawFromBankRequest struct {
	PlayerID   string         `json:"player_id"`
	Currency   int            `json:"currency,omitempty"`
	Resources  map[string]int `json:"resources,omitempty"`
	Items      []WithdrawItem `json:"items,omitempty"`
}

// WithdrawItem represents an item being withdrawn
type WithdrawItem struct {
	ItemID   string `json:"item_id"`
	Quantity int    `json:"quantity"`
}

// WithdrawFromBankResponse represents the response after withdrawing from bank
type WithdrawFromBankResponse struct {
	GuildID           string         `json:"guild_id"`
	PlayerID          string         `json:"player_id"`
	WithdrawnCurrency int            `json:"withdrawn_currency"`
	WithdrawnResources map[string]int `json:"withdrawn_resources"`
	WithdrawnItems    []WithdrawItem `json:"withdrawn_items"`
	RemainingWealth   int            `json:"remaining_wealth"`
	WithdrawnAt       int64          `json:"withdrawn_at"`
}

// GetGuildLeaderboardResponse represents the response for guild leaderboard
type GetGuildLeaderboardResponse struct {
	LeaderboardType string         `json:"leaderboard_type"`
	Guilds          []GuildRanking `json:"guilds"`
	TotalCount      int            `json:"total_count"`
	GeneratedAt     int64          `json:"generated_at"`
}

// GuildRanking represents a guild's ranking in the leaderboard
type GuildRanking struct {
	GuildID    string `json:"guild_id"`
	Name       string `json:"name"`
	Rank       int    `json:"rank"`
	Reputation int    `json:"reputation"`
	Wealth     int    `json:"wealth"`
	Level      int     `json:"level"`
	Members    int    `json:"members"`
	Wins       int    `json:"wins"`
	Losses     int    `json:"losses"`
	WinRate    float64 `json:"win_rate"`
}

// Error response model
type ErrorResponse struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
	Details string `json:"details,omitempty"`
}
