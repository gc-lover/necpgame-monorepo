package server

import (
	"context"
	"crypto/rsa"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"math/big"
	"net/http"
	"strings"
	"sync"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/sirupsen/logrus"
)

type JwtValidator struct {
	issuer   string
	jwksURL  string
	logger   *logrus.Logger
	keys     map[string]*rsa.PublicKey
	keysMu   sync.RWMutex
	lastFetch time.Time
	fetchMu  sync.Mutex
}

type Claims struct {
	jwt.RegisteredClaims
	PreferredUsername string   `json:"preferred_username"`
	Email            string   `json:"email"`
	RealmAccess     struct {
		Roles []string `json:"roles"`
	} `json:"realm_access"`
}

type JWKS struct {
	Keys []JWK `json:"keys"`
}

type JWK struct {
	Kty string `json:"kty"`
	Kid string `json:"kid"`
	Use string `json:"use"`
	N   string `json:"n"`
	E   string `json:"e"`
}

func NewJwtValidator(issuer, jwksURL string, logger *logrus.Logger) *JwtValidator {
	return &JwtValidator{
		issuer:  issuer,
		jwksURL: jwksURL,
		logger:  logger,
		keys:    make(map[string]*rsa.PublicKey),
	}
}

func (v *JwtValidator) fetchJWKS(ctx context.Context) error {
	v.fetchMu.Lock()
	defer v.fetchMu.Unlock()

	if time.Since(v.lastFetch) < 5*time.Minute {
		return nil
	}

	req, err := http.NewRequestWithContext(ctx, "GET", v.jwksURL, nil)
	if err != nil {
		return err
	}

	client := &http.Client{Timeout: 10 * time.Second}
	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("failed to fetch JWKS: status %d", resp.StatusCode)
	}

	var jwks JWKS
	if err := json.NewDecoder(resp.Body).Decode(&jwks); err != nil {
		return err
	}

	v.keysMu.Lock()
	defer v.keysMu.Unlock()

	v.keys = make(map[string]*rsa.PublicKey)
	for _, key := range jwks.Keys {
		if key.Kty == "RSA" && key.Use == "sig" {
			publicKey, err := v.convertJWKToRSAPublicKey(key)
			if err != nil {
				v.logger.WithError(err).Warn("Failed to convert JWK to RSA public key")
				continue
			}
			v.keys[key.Kid] = publicKey
		}
	}

	v.lastFetch = time.Now()
	return nil
}

func (v *JwtValidator) convertJWKToRSAPublicKey(jwk JWK) (*rsa.PublicKey, error) {
	nBytes, err := base64.RawURLEncoding.DecodeString(jwk.N)
	if err != nil {
		return nil, err
	}

	eBytes, err := base64.RawURLEncoding.DecodeString(jwk.E)
	if err != nil {
		return nil, err
	}

	n := new(big.Int).SetBytes(nBytes)
	e := int(new(big.Int).SetBytes(eBytes).Int64())

	return &rsa.PublicKey{
		N: n,
		E: e,
	}, nil
}

func (v *JwtValidator) getPublicKey(kid string) (*rsa.PublicKey, error) {
	v.keysMu.RLock()
	key, exists := v.keys[kid]
	v.keysMu.RUnlock()

	if exists {
		return key, nil
	}

	return nil, fmt.Errorf("public key not found for kid: %s", kid)
}

func (v *JwtValidator) Verify(ctx context.Context, authHeader string) (*Claims, error) {
	if err := v.fetchJWKS(ctx); err != nil {
		v.logger.WithError(err).Warn("Failed to fetch JWKS, using cached keys")
	}

	tokenString := strings.TrimPrefix(authHeader, "Bearer ")
	if tokenString == authHeader {
		return nil, fmt.Errorf("invalid authorization header format")
	}

	token, err := jwt.ParseWithClaims(tokenString, &Claims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodRSA); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}

		kid, ok := token.Header["kid"].(string)
		if !ok {
			return nil, fmt.Errorf("kid not found in token header")
		}

		return v.getPublicKey(kid)
	})

	if err != nil {
		return nil, err
	}

	claims, ok := token.Claims.(*Claims)
	if !ok || !token.Valid {
		return nil, fmt.Errorf("invalid token claims")
	}

	if claims.Issuer != v.issuer {
		return nil, fmt.Errorf("invalid issuer: %s", claims.Issuer)
	}

	return claims, nil
}
