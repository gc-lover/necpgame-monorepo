package server

type MatchmakerConfig struct {
	RedisUrl string
	Mode     string
	TeamSize int
}

func NewMatchmakerConfig(redisUrl, mode string, teamSize int) *MatchmakerConfig {
	return &MatchmakerConfig{
		RedisUrl: redisUrl,
		Mode:     mode,
		TeamSize: teamSize,
	}
}

