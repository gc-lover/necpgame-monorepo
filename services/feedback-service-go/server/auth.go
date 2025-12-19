package server

import (
	"context"
	"crypto/rsa"
	"encoding/base64"
	"encoding/binary"
	"encoding/json"
	"fmt"
	"io"
	"math/big"
	"net/http"
	"strings"
	"sync"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/sirupsen/logrus"
)

type JWKS struct {
	Keys []JWK `json:"keys"`
}

type JWK struct {
	Kty string `json:"kty"`
	Kid string `json:"kid"`
	Use string `json:"use"`
	N   string `json:"n"`
	E   string `json:"e"`
	Alg string `json:"alg"`
}

type JwtValidator struct {
	issuer      string
	jwksURL     string
	httpClient  *http.Client
	cache       map[string]*rsa.PublicKey
	cacheMutex  sync.RWMutex
	logger      *logrus.Logger
	lastFetch   time.Time
	cacheExpiry time.Duration
}

type Claims struct {
	jwt.RegisteredClaims
	Subject           string   `json:"sub,omitempty"`
	PreferredUsername string   `json:"preferred_username,omitempty"`
	Email             string   `json:"email,omitempty"`
	RealmAccess       struct {
		Roles []string `json:"roles,omitempty"`
	} `json:"realm_access,omitempty"`
}

func NewJwtValidator(issuer, jwksURL string, logger *logrus.Logger) *JwtValidator {
	return &JwtValidator{
		issuer:      issuer,
		jwksURL:     jwksURL,
		httpClient:  &http.Client{Timeout: 10 * time.Second},
		cache:       make(map[string]*rsa.PublicKey),
		logger:      logger,
		cacheExpiry: 1 * time.Hour,
	}
}

func (v *JwtValidator) fetchJWKS(ctx context.Context) (*JWKS, error) {
	req, err := http.NewRequestWithContext(ctx, "GET", v.jwksURL, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}

	resp, err := v.httpClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("failed to fetch JWKS: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, fmt.Errorf("failed to read response: %w", err)
	}

	var jwks JWKS
	if err := json.Unmarshal(body, &jwks); err != nil {
		return nil, fmt.Errorf("failed to parse JWKS: %w", err)
	}

	return &jwks, nil
}

func (v *JwtValidator) jwkToRSAPublicKey(jwk JWK) (*rsa.PublicKey, error) {
	nBytes, err := base64.RawURLEncoding.DecodeString(jwk.N)
	if err != nil {
		return nil, fmt.Errorf("failed to decode modulus: %w", err)
	}

	eBytes, err := base64.RawURLEncoding.DecodeString(jwk.E)
	if err != nil {
		return nil, fmt.Errorf("failed to decode exponent: %w", err)
	}

	if len(eBytes) < 4 {
		ne := make([]byte, 4)
		copy(ne[4-len(eBytes):], eBytes)
		eBytes = ne
	}

	e := binary.BigEndian.Uint32(eBytes)
	n := new(big.Int).SetBytes(nBytes)

	return &rsa.PublicKey{
		N: n,
		E: int(e),
	}, nil
}

func (v *JwtValidator) getPublicKey(ctx context.Context, kid string) (*rsa.PublicKey, error) {
	v.cacheMutex.RLock()
	if key, ok := v.cache[kid]; ok {
		if time.Since(v.lastFetch) < v.cacheExpiry {
			v.cacheMutex.RUnlock()
			return key, nil
		}
	}
	v.cacheMutex.RUnlock()

	jwks, err := v.fetchJWKS(ctx)
	if err != nil {
		v.logger.WithError(err).Warn("Failed to fetch JWKS, using cache")
		v.cacheMutex.RLock()
		if key, ok := v.cache[kid]; ok {
			v.cacheMutex.RUnlock()
			return key, nil
		}
		v.cacheMutex.RUnlock()
		return nil, err
	}

	v.cacheMutex.Lock()
	defer v.cacheMutex.Unlock()

	for _, jwk := range jwks.Keys {
		if jwk.Kid == kid && jwk.Use == "sig" && jwk.Kty == "RSA" {
			key, err := v.jwkToRSAPublicKey(jwk)
			if err != nil {
				v.logger.WithError(err).Errorf("Failed to convert JWK to RSA public key for kid: %s", kid)
				continue
			}
			v.cache[kid] = key
			v.lastFetch = time.Now()
			return key, nil
		}
	}

	return nil, fmt.Errorf("public key not found for kid: %s", kid)
}

func (v *JwtValidator) Verify(ctx context.Context, tokenString string) (*Claims, error) {
	if tokenString == "" {
		return nil, fmt.Errorf("token is empty")
	}

	tokenString = strings.TrimPrefix(tokenString, "Bearer ")
	tokenString = strings.TrimSpace(tokenString)

	token, err := jwt.ParseWithClaims(tokenString, &Claims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodRSA); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}

		kid, ok := token.Header["kid"].(string)
		if !ok {
			return nil, fmt.Errorf("kid not found in token header")
		}

		publicKey, err := v.getPublicKey(ctx, kid)
		if err != nil {
			return nil, fmt.Errorf("failed to get public key: %w", err)
		}

		return publicKey, nil
	})

	if err != nil {
		return nil, fmt.Errorf("failed to parse token: %w", err)
	}

	claims, ok := token.Claims.(*Claims)
	if !ok || !token.Valid {
		return nil, fmt.Errorf("invalid token")
	}

	if claims.Issuer != v.issuer {
		return nil, fmt.Errorf("invalid issuer: expected %s, got %s", v.issuer, claims.Issuer)
	}

	if claims.ExpiresAt != nil && claims.ExpiresAt.Time.Before(time.Now()) {
		return nil, fmt.Errorf("token expired")
	}

	return claims, nil
}





















































