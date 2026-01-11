// Package crypto provides quantum-resistant cryptographic primitives
// Issue: #1991

package crypto

import (
	"context"
	"crypto/rand"
	"errors"
	"fmt"

	"github.com/cloudflare/circl/sign/dilithium2"
	"github.com/cloudflare/circl/sign/dilithium3"
	"github.com/cloudflare/circl/sign/dilithium5"
	"go.uber.org/zap"
)

// DilithiumLevel represents the security level for Dilithium operations
type DilithiumLevel int

const (
	// Dilithium2 provides AES-128 equivalent security
	Dilithium2 DilithiumLevel = iota
	// Dilithium3 provides AES-192 equivalent security
	Dilithium3
	// Dilithium5 provides AES-256 equivalent security
	Dilithium5
)

// DilithiumKeyPair holds public and private keys for Dilithium operations
type DilithiumKeyPair struct {
	PublicKey  []byte
	PrivateKey []byte
	Level      DilithiumLevel
}

// DilithiumSignature represents a Dilithium signature
type DilithiumSignature struct {
	Data  []byte
	Level DilithiumLevel
}

// GenerateDilithiumKeyPair generates a new Dilithium key pair
func GenerateDilithiumKeyPair(ctx context.Context, level DilithiumLevel) (*DilithiumKeyPair, error) {
	logger := zap.L().With(zap.String("operation", "GenerateDilithiumKeyPair"))

	var publicKey, privateKey []byte
	var err error

	switch level {
	case Dilithium2:
		publicKey, privateKey, err = dilithium2.GenerateKey(rand.Reader)
		if err != nil {
			logger.Error("failed to generate Dilithium2 key pair", zap.Error(err))
			return nil, fmt.Errorf("failed to generate Dilithium2 key pair: %w", err)
		}
	case Dilithium3:
		publicKey, privateKey, err = dilithium3.GenerateKey(rand.Reader)
		if err != nil {
			logger.Error("failed to generate Dilithium3 key pair", zap.Error(err))
			return nil, fmt.Errorf("failed to generate Dilithium3 key pair: %w", err)
		}
	case Dilithium5:
		publicKey, privateKey, err = dilithium5.GenerateKey(rand.Reader)
		if err != nil {
			logger.Error("failed to generate Dilithium5 key pair", zap.Error(err))
			return nil, fmt.Errorf("failed to generate Dilithium5 key pair: %w", err)
		}
	default:
		return nil, fmt.Errorf("unsupported security level: %d", level)
	}

	logger.Info("Dilithium key pair generated successfully",
		zap.Int("security_level", int(level)),
		zap.Int("public_key_size", len(publicKey)),
		zap.Int("private_key_size", len(privateKey)))

	return &DilithiumKeyPair{
		PublicKey:  publicKey,
		PrivateKey: privateKey,
		Level:      level,
	}, nil
}

// SignDilithium signs a message using Dilithium
func SignDilithium(ctx context.Context, message []byte, privateKey []byte, level DilithiumLevel) (*DilithiumSignature, error) {
	logger := zap.L().With(zap.String("operation", "SignDilithium"))

	if len(message) == 0 {
		return nil, errors.New("message cannot be empty")
	}

	if len(privateKey) == 0 {
		return nil, errors.New("private key cannot be empty")
	}

	var signature []byte
	var err error

	switch level {
	case Dilithium2:
		signature, err = dilithium2.Sign(privateKey, message)
		if err != nil {
			logger.Error("failed to sign with Dilithium2", zap.Error(err))
			return nil, fmt.Errorf("failed to sign with Dilithium2: %w", err)
		}
	case Dilithium3:
		signature, err = dilithium3.Sign(privateKey, message)
		if err != nil {
			logger.Error("failed to sign with Dilithium3", zap.Error(err))
			return nil, fmt.Errorf("failed to sign with Dilithium3: %w", err)
		}
	case Dilithium5:
		signature, err = dilithium5.Sign(privateKey, message)
		if err != nil {
			logger.Error("failed to sign with Dilithium5", zap.Error(err))
			return nil, fmt.Errorf("failed to sign with Dilithium5: %w", err)
		}
	default:
		return nil, fmt.Errorf("unsupported security level: %d", level)
	}

	logger.Info("Dilithium signature created successfully",
		zap.Int("security_level", int(level)),
		zap.Int("message_size", len(message)),
		zap.Int("signature_size", len(signature)))

	return &DilithiumSignature{
		Data:  signature,
		Level: level,
	}, nil
}

// VerifyDilithium verifies a Dilithium signature
func VerifyDilithium(ctx context.Context, message []byte, signature *DilithiumSignature, publicKey []byte) (bool, error) {
	logger := zap.L().With(zap.String("operation", "VerifyDilithium"))

	if len(message) == 0 {
		return false, errors.New("message cannot be empty")
	}

	if signature == nil || len(signature.Data) == 0 {
		return false, errors.New("signature cannot be empty")
	}

	if len(publicKey) == 0 {
		return false, errors.New("public key cannot be empty")
	}

	var valid bool
	var err error

	switch signature.Level {
	case Dilithium2:
		valid, err = dilithium2.Verify(publicKey, message, signature.Data)
		if err != nil {
			logger.Error("failed to verify Dilithium2 signature", zap.Error(err))
			return false, fmt.Errorf("failed to verify Dilithium2 signature: %w", err)
		}
	case Dilithium3:
		valid, err = dilithium3.Verify(publicKey, message, signature.Data)
		if err != nil {
			logger.Error("failed to verify Dilithium3 signature", zap.Error(err))
			return false, fmt.Errorf("failed to verify Dilithium3 signature: %w", err)
		}
	case Dilithium5:
		valid, err = dilithium5.Verify(publicKey, message, signature.Data)
		if err != nil {
			logger.Error("failed to verify Dilithium5 signature", zap.Error(err))
			return false, fmt.Errorf("failed to verify Dilithium5 signature: %w", err)
		}
	default:
		return false, fmt.Errorf("unsupported security level: %d", signature.Level)
	}

	logger.Info("Dilithium signature verification completed",
		zap.Int("security_level", int(signature.Level)),
		zap.Bool("signature_valid", valid))

	return valid, nil
}

// ValidateDilithiumKeyPair validates that a Dilithium key pair is correctly formatted
func ValidateDilithiumKeyPair(keyPair *DilithiumKeyPair) error {
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
	case Dilithium2:
		expectedPubSize = dilithium2.PublicKeySize
		expectedPrivSize = dilithium2.PrivateKeySize
	case Dilithium3:
		expectedPubSize = dilithium3.PublicKeySize
		expectedPrivSize = dilithium3.PrivateKeySize
	case Dilithium5:
		expectedPubSize = dilithium5.PublicKeySize
		expectedPrivSize = dilithium5.PrivateKeySize
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

// GetDilithiumKeySizes returns the expected key sizes for a security level
func GetDilithiumKeySizes(level DilithiumLevel) (publicKeySize, privateKeySize, signatureSize int, err error) {
	switch level {
	case Dilithium2:
		return dilithium2.PublicKeySize, dilithium2.PrivateKeySize, dilithium2.SignatureSize, nil
	case Dilithium3:
		return dilithium3.PublicKeySize, dilithium3.PrivateKeySize, dilithium3.SignatureSize, nil
	case Dilithium5:
		return dilithium5.PublicKeySize, dilithium5.PrivateKeySize, dilithium5.SignatureSize, nil
	default:
		return 0, 0, 0, fmt.Errorf("unsupported security level: %d", level)
	}
}