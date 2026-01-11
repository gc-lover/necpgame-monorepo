// Package crypto provides quantum-resistant cryptographic primitives
// Issue: #1991

package crypto

import (
	"context"
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
	"errors"
	"fmt"
	"io"

	"go.uber.org/zap"
)

// HybridEncryptor provides hybrid encryption combining classical and post-quantum cryptography
type HybridEncryptor struct {
	logger *zap.Logger
}

// NewHybridEncryptor creates a new hybrid encryptor
func NewHybridEncryptor() *HybridEncryptor {
	return &HybridEncryptor{
		logger: zap.L().With(zap.String("component", "HybridEncryptor")),
	}
}

// HybridCiphertext represents encrypted data with Kyber encapsulation
type HybridCiphertext struct {
	KyberCiphertext []byte      // Post-quantum encapsulated key
	AESCiphertext   []byte      // Classical AES-encrypted data
	SecurityLevel   SecurityLevel
	IV              []byte      // Initialization vector for AES
}

// Encrypt performs hybrid encryption: Kyber for key exchange + AES for data
func (he *HybridEncryptor) Encrypt(ctx context.Context, plaintext []byte, recipientPublicKey []byte) (*HybridCiphertext, error) {
	logger := he.logger.With(zap.String("operation", "Encrypt"))

	if len(plaintext) == 0 {
		return nil, errors.New("plaintext cannot be empty")
	}

	if len(recipientPublicKey) == 0 {
		return nil, errors.New("recipient public key cannot be empty")
	}

	// Use Kyber768 for balanced security/performance
	sharedSecret, kyberCiphertext, err := EncapsulateKyber(ctx, recipientPublicKey, Kyber768)
	if err != nil {
		logger.Error("failed to encapsulate Kyber key", zap.Error(err))
		return nil, fmt.Errorf("failed to encapsulate Kyber key: %w", err)
	}

	// Use shared secret as AES key (first 32 bytes)
	aesKey := sharedSecret[:32]

	// Generate random IV
	iv := make([]byte, aes.BlockSize)
	if _, err := io.ReadFull(rand.Reader, iv); err != nil {
		logger.Error("failed to generate IV", zap.Error(err))
		return nil, fmt.Errorf("failed to generate IV: %w", err)
	}

	// Create AES cipher
	block, err := aes.NewCipher(aesKey)
	if err != nil {
		logger.Error("failed to create AES cipher", zap.Error(err))
		return nil, fmt.Errorf("failed to create AES cipher: %w", err)
	}

	// Encrypt data with AES-GCM
	aesgcm, err := cipher.NewGCM(block)
	if err != nil {
		logger.Error("failed to create AES-GCM", zap.Error(err))
		return nil, fmt.Errorf("failed to create AES-GCM: %w", err)
	}

	aesCiphertext := aesgcm.Seal(nil, iv, plaintext, nil)

	logger.Info("hybrid encryption completed successfully",
		zap.Int("plaintext_size", len(plaintext)),
		zap.Int("ciphertext_size", len(aesCiphertext)),
		zap.Int("kyber_ciphertext_size", len(kyberCiphertext.Data)))

	return &HybridCiphertext{
		KyberCiphertext: kyberCiphertext.Data,
		AESCiphertext:   aesCiphertext,
		SecurityLevel:   Kyber768,
		IV:              iv,
	}, nil
}

// Decrypt performs hybrid decryption
func (he *HybridEncryptor) Decrypt(ctx context.Context, ciphertext *HybridCiphertext, recipientPrivateKey []byte) ([]byte, error) {
	logger := he.logger.With(zap.String("operation", "Decrypt"))

	if ciphertext == nil {
		return nil, errors.New("ciphertext cannot be nil")
	}

	if len(ciphertext.AESCiphertext) == 0 {
		return nil, errors.New("AES ciphertext cannot be empty")
	}

	if len(ciphertext.KyberCiphertext) == 0 {
		return nil, errors.New("Kyber ciphertext cannot be empty")
	}

	if len(recipientPrivateKey) == 0 {
		return nil, errors.New("recipient private key cannot be empty")
	}

	if len(ciphertext.IV) != aes.BlockSize {
		return nil, fmt.Errorf("invalid IV size: expected %d, got %d", aes.BlockSize, len(ciphertext.IV))
	}

	// Decapsulate Kyber to get shared secret
	kyberCiphertext := &KyberCiphertext{
		Data:  ciphertext.KyberCiphertext,
		Level: ciphertext.SecurityLevel,
	}

	sharedSecret, err := DecapsulateKyber(ctx, kyberCiphertext, recipientPrivateKey)
	if err != nil {
		logger.Error("failed to decapsulate Kyber key", zap.Error(err))
		return nil, fmt.Errorf("failed to decapsulate Kyber key: %w", err)
	}

	// Use shared secret as AES key
	aesKey := sharedSecret[:32]

	// Create AES cipher
	block, err := aes.NewCipher(aesKey)
	if err != nil {
		logger.Error("failed to create AES cipher", zap.Error(err))
		return nil, fmt.Errorf("failed to create AES cipher: %w", err)
	}

	// Decrypt data with AES-GCM
	aesgcm, err := cipher.NewGCM(block)
	if err != nil {
		logger.Error("failed to create AES-GCM", zap.Error(err))
		return nil, fmt.Errorf("failed to create AES-GCM: %w", err)
	}

	plaintext, err := aesgcm.Open(nil, ciphertext.IV, ciphertext.AESCiphertext, nil)
	if err != nil {
		logger.Error("failed to decrypt AES data", zap.Error(err))
		return nil, fmt.Errorf("failed to decrypt AES data: %w", err)
	}

	logger.Info("hybrid decryption completed successfully",
		zap.Int("ciphertext_size", len(ciphertext.AESCiphertext)),
		zap.Int("plaintext_size", len(plaintext)))

	return plaintext, nil
}

// SecureChannel provides end-to-end encrypted communication
type SecureChannel struct {
	keyPair   *KyberKeyPair
	peerKey   []byte
	sessionID string
	logger    *zap.Logger
}

// NewSecureChannel creates a new secure communication channel
func NewSecureChannel(ctx context.Context, localKeyPair *KyberKeyPair, peerPublicKey []byte) (*SecureChannel, error) {
	if localKeyPair == nil {
		return nil, errors.New("local key pair cannot be nil")
	}

	if len(peerPublicKey) == 0 {
		return nil, errors.New("peer public key cannot be empty")
	}

	// Generate session ID (simple hash of keys for demo)
	sessionID := fmt.Sprintf("session_%x", peerPublicKey[:8])

	return &SecureChannel{
		keyPair:   localKeyPair,
		peerKey:   peerPublicKey,
		sessionID: sessionID,
		logger:    zap.L().With(zap.String("session_id", sessionID)),
	}, nil
}

// Send encrypts and sends a message
func (sc *SecureChannel) Send(ctx context.Context, message []byte) (*HybridCiphertext, error) {
	sc.logger.Info("sending encrypted message", zap.Int("size", len(message)))
	return NewHybridEncryptor().Encrypt(ctx, message, sc.peerKey)
}

// Receive decrypts a received message
func (sc *SecureChannel) Receive(ctx context.Context, ciphertext *HybridCiphertext) ([]byte, error) {
	sc.logger.Info("receiving encrypted message")
	return NewHybridEncryptor().Decrypt(ctx, ciphertext, sc.keyPair.PrivateKey)
}

// GetSessionID returns the session identifier
func (sc *SecureChannel) GetSessionID() string {
	return sc.sessionID
}