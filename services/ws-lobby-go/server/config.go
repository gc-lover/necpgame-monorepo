package server

type LobbyConfig struct {
	Port        string
	Issuer      string
	JwksUrl     string
	JwtValidator *JwtValidator
}

func NewLobbyConfig(port, issuer, jwksUrl string) *LobbyConfig {
	return &LobbyConfig{
		Port:        port,
		Issuer:      issuer,
		JwksUrl:     jwksUrl,
		JwtValidator: NewJwtValidator(issuer, jwksUrl),
	}
}

