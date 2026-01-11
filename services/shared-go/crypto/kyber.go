// Package crypto provides quantum-resistant cryptographic primitives
// Issue: #1991

package crypto

import (
	"context"
	"crypto/rand"
	"errors"
	"fmt"

	"github.com/cloudflare/circl/kem/kyber/kyber1024"
	"github.com/cloudflare/circl/kem/kyber/kyber512"
	"github.com/cloudflare/circl/kem/kyber/kyber768"
	"go.uber.org/zap"
)

// SecurityLevel represents the security level for Kyber operations
type SecurityLevel int

const (
	// Kyber512 provides AES-128 equivalent security
	Kyber512 SecurityLevel = iota
	// Kyber768 provides AES-192 equivalent security
	Kyber768
	// Kyber1024 provides AES-256 equivalent security
	Kyber1024
)

// KyberKeyPair holds public and private keys for Kyber operations
type KyberKeyPair struct {
	PublicKey  []byte
	PrivateKey []byte
	Level      SecurityLevel
}

// KyberCiphertext represents encrypted Kyber data
type KyberCiphertext struct {
	Data  []byte
	Level SecurityLevel
}

// GenerateKyberKeyPair generates a new Kyber key pair
func GenerateKyberKeyPair(ctx context.Context, level SecurityLevel) (*KyberKeyPair, error) {
	logger := zap.L().With(zap.String("operation", "GenerateKyberKeyPair"))

	var publicKey, privateKey []byte
	var err error

	switch level {
	case Kyber512:
		publicKey, privateKey, err = kyber512.GenerateKeyPair(rand.Reader)
		if err != nil {
			logger.Error("failed to generate Kyber512 key pair", zap.Error(err))
			return nil, fmt.Errorf("failed to generate Kyber512 key pair: %w", err)
		}
	case Kyber768:
		publicKey, privateKey, err = kyber768.GenerateKeyPair(rand.Reader)
		if err != nil {
			logger.Error("failed to generate Kyber768 key pair", zap.Error(err))
			return nil, fmt.Errorf("failed to generate Kyber768 key pair: %w", err)
		}
	case Kyber1024:
		publicKey, privateKey, err = kyber1024.GenerateKeyPair(rand.Reader)
		if err != nil {
			logger.Error("failed to generate Kyber1024 key pair", zap.Error(err))
			return nil, fmt.Errorf("failed to generate Kyber1024 key pair: %w", err)
		}
	default:
		return nil, fmt.Errorf("unsupported security level: %d", level)
	}

	logger.Info("Kyber key pair generated successfully",
		zap.Int("security_level", int(level)),
		zap.Int("public_key_size", len(publicKey)),
		zap.Int("private_key_size", len(privateKey)))

	return &KyberKeyPair{
		PublicKey:  publicKey,
		PrivateKey: privateKey,
		Level:      level,
	}, nil
}

// EncapsulateKyber encapsulates a shared secret using the recipient's public key
func EncapsulateKyber(ctx context.Context, publicKey []byte, level SecurityLevel) ([]byte, *KyberCiphertext, error) {
	logger := zap.L().With(zap.String("operation", "EncapsulateKyber"))

	if len(publicKey) == 0 {
		return nil, nil, errors.New("public key cannot be empty")
	}

	var sharedSecret, ciphertext []byte
	var err error

	switch level {
	case Kyber512:
		sharedSecret, ciphertext, err = kyber512.Encapsulate(rand.Reader, publicKey)
		if err != nil {
			logger.Error("failed to encapsulate Kyber512", zap.Error(err))
			return nil, nil, fmt.Errorf("failed to encapsulate Kyber512: %w", err)
		}
	case Kyber768:
		sharedSecret, ciphertext, err = kyber768.Encapsulate(rand.Reader, publicKey)
		if err != nil {
			logger.Error("failed to encapsulate Kyber768", zap.Error(err))
			return nil, nil, fmt.Errorf("failed to encapsulate Kyber768: %w", err)
		}
	case Kyber1024:
		sharedSecret, ciphertext, err = kyber1024.Encapsulate(rand.Reader, publicKey)
		if err != nil {
			logger.Error("failed to encapsulate Kyber1024", zap.Error(err))
			return nil, nil, fmt.Errorf("failed to encapsulate Kyber1024: %w", err)
		}
	default:
		return nil, nil, fmt.Errorf("unsupported security level: %d", level)
	}

	logger.Info("Kyber encapsulation completed successfully",
		zap.Int("security_level", int(level)),
		zap.Int("shared_secret_size", len(sharedSecret)),
		zap.Int("ciphertext_size", len(ciphertext)))

	return sharedSecret, &KyberCiphertext{
		Data:  ciphertext,
		Level: level,
	}, nil
}

// DecapsulateKyber decapsulates a shared secret using the recipient's private key
func DecapsulateKyber(ctx context.Context, ciphertext *KyberCiphertext, privateKey []byte) ([]byte, error) {
	logger := zap.L().With(zap.String("operation", "DecapsulateKyber"))

	if ciphertext == nil || len(ciphertext.Data) == 0 {
		return nil, errors.New("ciphertext cannot be empty")
	}

	if len(privateKey) == 0 {
		return nil, errors.New("private key cannot be empty")
	}

	var sharedSecret []byte
	var err error

	switch ciphertext.Level {
	case Kyber512:
		sharedSecret, err = kyber512.Decapsulate(privateKey, ciphertext.Data)
		if err != nil {
			logger.Error("failed to decapsulate Kyber512", zap.Error(err))
			return nil, fmt.Errorf("failed to decapsulate Kyber512: %w", err)
		}
	case Kyber768:
		sharedSecret, err = kyber768.Decapsulate(privateKey, ciphertext.Data)
		if err != nil {
			logger.Error("failed to decapsulate Kyber768", zap.Error(err))
			return nil, fmt.Errorf("failed to decapsulate Kyber768: %w", err)
		}
	case Kyber1024:
		sharedSecret, err = kyber1024.Decapsulate(privateKey, ciphertext.Data)
		if err != nil {
			logger.Error("failed to decapsulate Kyber1024", zap.Error(err))
			return nil, fmt.Errorf("failed to decapsulate Kyber1024: %w", err)
		}
	default:
		return nil, fmt.Errorf("unsupported security level: %d", ciphertext.Level)
	}

	logger.Info("Kyber decapsulation completed successfully",
		zap.Int("security_level", int(ciphertext.Level)),
		zap.Int("shared_secret_size", len(sharedSecret)))

	return sharedSecret, nil
}

// ValidateKyberKeyPair validates that a Kyber key pair is correctly formatted
func ValidateKyberKeyPair(keyPair *KyberKeyPair) error {
	if keyPair == nil {
		return errors.New("key pair cannot be nil")
	}

	if len(keyPair.PublicKey) == 0 {
		return errors.New("public key cannot be empty")
	}

	if len(keyPair.PrivateKey) == 0 {
		return errors.New("private key cannot be empty")
	}

	// Validate key sizes based on security level
	var expectedPubSize, expectedPrivSize int

	switch keyPair.Level {
	case Kyber512:
		expectedPubSize = kyber512.PublicKeySize
		expectedPrivSize = kyber512.PrivateKeySize
	case Kyber768:
		expectedPubSize = kyber768.PublicKeySize
		expectedPrivSize = kyber768.PrivateKeySize
	case Kyber1024:
		expectedPubSize = kyber1024.PublicKeySize
		expectedPrivSize = kyber1024.PrivateKeySize
	default:
		return fmt.Errorf("unsupported security level: %d", keyPair.Level)
	}

	if len(keyPair.PublicKey) != expectedPubSize {
		return fmt.Errorf("invalid public key size: expected %d, got %d", expectedPubSize, len(keyPair.PublicKey))
	}

	if len(keyPair.PrivateKey) != expectedPrivSize {
		return fmt.Errorf("invalid private key size: expected %d, got %d", expectedPrivSize, len(keyPair.PrivateKey))
	}

	return nil
}

// GetKyberKeySizes returns the expected key sizes for a security level
func GetKyberKeySizes(level SecurityLevel) (publicKeySize, privateKeySize int, err error) {
	switch level {
	case Kyber512:
		return kyber512.PublicKeySize, kyber512.PrivateKeySize, nil
	case Kyber768:
		return kyber768.PublicKeySize, kyber768.PrivateKeySize, nil
	case Kyber1024:
		return kyber1024.PublicKeySize, kyber1024.PrivateKeySize, nil
	default:
		return 0, 0, fmt.Errorf("unsupported security level: %d", level)
	}
}