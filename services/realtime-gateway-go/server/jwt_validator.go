package server

import (
	"strings"
)

type JwtValidator struct {
	issuer  string
	jwksUrl string
}

func (v *JwtValidator) Verify(token string) bool {
	return token != "" && strings.TrimSpace(token) != ""
}
