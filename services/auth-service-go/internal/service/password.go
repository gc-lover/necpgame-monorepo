package service

import (
	"crypto/rand"
	"crypto/subtle"
	"encoding/base64"
	"fmt"
	"strings"

	"golang.org/x/crypto/argon2"
)

// PasswordConfig holds the parameters for Argon2 hashing
type PasswordConfig struct {
	Time    uint32 // Number of iterations
	Memory  uint32 // Memory usage in KiB
	Threads uint8  // Number of threads
	KeyLen  uint32 // Length of the hash
}

// DefaultPasswordConfig returns secure default configuration for password hashing
func DefaultPasswordConfig() *PasswordConfig {
	return &PasswordConfig{
		Time:    3,     // 3 iterations
		Memory:  64 * 1024, // 64 MiB
		Threads: 4,     // 4 threads
		KeyLen:  32,    // 32 bytes
	}
}

// PasswordService handles password hashing and verification
type PasswordService struct {
	config *PasswordConfig
}

// NewPasswordService creates a new password service
func NewPasswordService() *PasswordService {
	return &PasswordService{
		config: DefaultPasswordConfig(),
	}
}

// HashPassword hashes a password using Argon2id
func (p *PasswordService) HashPassword(password string) (string, error) {
	// Generate a salt
	salt := make([]byte, 32)
	if _, err := rand.Read(salt); err != nil {
		return "", fmt.Errorf("failed to generate salt: %w", err)
	}

	// Hash the password
	hash := argon2.IDKey([]byte(password), salt, p.config.Time, p.config.Memory, p.config.Threads, p.config.KeyLen)

	// Encode salt and hash
	saltEncoded := base64.RawStdEncoding.EncodeToString(salt)
	hashEncoded := base64.RawStdEncoding.EncodeToString(hash)

	// Format: $argon2id$v=19$m=65536,t=3,p=4$salt$hash
	result := fmt.Sprintf("$argon2id$v=19$m=%d,t=%d,p=%d$%s$%s",
		p.config.Memory, p.config.Time, p.config.Threads, saltEncoded, hashEncoded)

	return result, nil
}

// VerifyPassword verifies a password against a hash
func (p *PasswordService) VerifyPassword(password, hash string) (bool, error) {
	// Parse the hash format
	parts := strings.Split(hash, "$")
	if len(parts) != 6 {
		return false, fmt.Errorf("invalid hash format")
	}

	if parts[1] != "argon2id" {
		return false, fmt.Errorf("unsupported hash type: %s", parts[1])
	}

	// Extract parameters
	var memory, time uint32
	var threads uint8

	params := strings.Split(parts[3], ",")
	for _, param := range params {
		kv := strings.Split(param, "=")
		if len(kv) != 2 {
			continue
		}
		switch kv[0] {
		case "m":
			fmt.Sscanf(kv[1], "%d", &memory)
		case "t":
			fmt.Sscanf(kv[1], "%d", &time)
		case "p":
			fmt.Sscanf(kv[1], "%d", &threads)
		}
	}

	// Decode salt and hash
	salt, err := base64.RawStdEncoding.DecodeString(parts[4])
	if err != nil {
		return false, fmt.Errorf("failed to decode salt: %w", err)
	}

	storedHash, err := base64.RawStdEncoding.DecodeString(parts[5])
	if err != nil {
		return false, fmt.Errorf("failed to decode hash: %w", err)
	}

	// Hash the input password with the same parameters
	computedHash := argon2.IDKey([]byte(password), salt, time, memory, threads, uint32(len(storedHash)))

	// Compare hashes using constant-time comparison
	return subtle.ConstantTimeCompare(computedHash, storedHash) == 1, nil
}

// IsValidPassword checks if a password meets security requirements
func (p *PasswordService) IsValidPassword(password string) error {
	if len(password) < 8 {
		return fmt.Errorf("password must be at least 8 characters long")
	}

	if len(password) > 128 {
		return fmt.Errorf("password must be less than 128 characters long")
	}

	// Check for at least one uppercase letter
	hasUpper := false
	for _, char := range password {
		if char >= 'A' && char <= 'Z' {
			hasUpper = true
			break
		}
	}
	if !hasUpper {
		return fmt.Errorf("password must contain at least one uppercase letter")
	}

	// Check for at least one lowercase letter
	hasLower := false
	for _, char := range password {
		if char >= 'a' && char <= 'z' {
			hasLower = true
			break
		}
	}
	if !hasLower {
		return fmt.Errorf("password must contain at least one lowercase letter")
	}

	// Check for at least one digit
	hasDigit := false
	for _, char := range password {
		if char >= '0' && char <= '9' {
			hasDigit = true
			break
		}
	}
	if !hasDigit {
		return fmt.Errorf("password must contain at least one digit")
	}

	return nil
}