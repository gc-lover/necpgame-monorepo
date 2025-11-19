package server

import (
	"strings"
)

type JwtValidator struct {
	issuer  string
	jwksUrl string
}

func NewJwtValidator(issuer, jwksUrl string) *JwtValidator {
	return &JwtValidator{
		issuer:  issuer,
		jwksUrl: jwksUrl,
	}
}

func (v *JwtValidator) Verify(token string) bool {
	return token != "" && strings.TrimSpace(token) != ""
}

