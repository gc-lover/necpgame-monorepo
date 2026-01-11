// Package crypto provides quantum-resistant cryptographic primitives
// Issue: #1991

package crypto

import (
	"context"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestKyberKeyGeneration(t *testing.T) {
	ctx := context.Background()

	tests := []struct {
		name  string
		level SecurityLevel
	}{
		{"Kyber512", Kyber512},
		{"Kyber768", Kyber768},
		{"Kyber1024", Kyber1024},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			keyPair, err := GenerateKyberKeyPair(ctx, tt.level)
			require.NoError(t, err)
			require.NotNil(t, keyPair)

			// Validate key pair
			err = ValidateKyberKeyPair(keyPair)
			assert.NoError(t, err)

			// Check key sizes
			pubSize, privSize, err := GetKyberKeySizes(tt.level)
			require.NoError(t, err)
			assert.Equal(t, pubSize, len(keyPair.PublicKey))
			assert.Equal(t, privSize, len(keyPair.PrivateKey))
			assert.Equal(t, tt.level, keyPair.Level)
		})
	}
}

func TestKyberEncapsulationDecapsulation(t *testing.T) {
	ctx := context.Background()

	// Test Kyber768 for balance of security and performance
	keyPair, err := GenerateKyberKeyPair(ctx, Kyber768)
	require.NoError(t, err)

	// Encapsulate
	sharedSecret1, ciphertext, err := EncapsulateKyber(ctx, keyPair.PublicKey, Kyber768)
	require.NoError(t, err)
	require.NotNil(t, sharedSecret1)
	require.NotNil(t, ciphertext)

	// Decapsulate
	sharedSecret2, err := DecapsulateKyber(ctx, ciphertext, keyPair.PrivateKey)
	require.NoError(t, err)
	require.NotNil(t, sharedSecret2)

	// Shared secrets should match
	assert.Equal(t, sharedSecret1, sharedSecret2)
}

func TestDilithiumKeyGeneration(t *testing.T) {
	ctx := context.Background()

	tests := []struct {
		name  string
		level DilithiumLevel
	}{
		{"Dilithium2", Dilithium2},
		{"Dilithium3", Dilithium3},
		{"Dilithium5", Dilithium5},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			keyPair, err := GenerateDilithiumKeyPair(ctx, tt.level)
			require.NoError(t, err)
			require.NotNil(t, keyPair)

			// Validate key pair
			err = ValidateDilithiumKeyPair(keyPair)
			assert.NoError(t, err)

			// Check key sizes
			pubSize, privSize, _, err := GetDilithiumKeySizes(tt.level)
			require.NoError(t, err)
			assert.Equal(t, pubSize, len(keyPair.PublicKey))
			assert.Equal(t, privSize, len(keyPair.PrivateKey))
			assert.Equal(t, tt.level, keyPair.Level)
		})
	}
}

func TestDilithiumSignVerify(t *testing.T) {
	ctx := context.Background()
	message := []byte("Hello, quantum-resistant world!")

	// Test Dilithium3 for balance
	keyPair, err := GenerateDilithiumKeyPair(ctx, Dilithium3)
	require.NoError(t, err)

	// Sign message
	signature, err := SignDilithium(ctx, message, keyPair.PrivateKey, Dilithium3)
	require.NoError(t, err)
	require.NotNil(t, signature)

	// Verify signature
	valid, err := VerifyDilithium(ctx, message, signature, keyPair.PublicKey)
	require.NoError(t, err)
	assert.True(t, valid)

	// Test with wrong message
	wrongMessage := []byte("Wrong message")
	valid, err = VerifyDilithium(ctx, wrongMessage, signature, keyPair.PublicKey)
	require.NoError(t, err)
	assert.False(t, valid)
}

func TestHybridEncryption(t *testing.T) {
	ctx := context.Background()
	plaintext := []byte("This is a test message for hybrid encryption")

	// Generate recipient key pair
	recipientKeyPair, err := GenerateKyberKeyPair(ctx, Kyber768)
	require.NoError(t, err)

	// Create encryptor
	encryptor := NewHybridEncryptor()

	// Encrypt
	ciphertext, err := encryptor.Encrypt(ctx, plaintext, recipientKeyPair.PublicKey)
	require.NoError(t, err)
	require.NotNil(t, ciphertext)

	// Decrypt
	decrypted, err := encryptor.Decrypt(ctx, ciphertext, recipientKeyPair.PrivateKey)
	require.NoError(t, err)

	// Verify
	assert.Equal(t, plaintext, decrypted)
}

func TestSecureChannel(t *testing.T) {
	ctx := context.Background()
	message := []byte("Secure channel test message")

	// Generate key pairs for both parties
	aliceKeyPair, err := GenerateKyberKeyPair(ctx, Kyber768)
	require.NoError(t, err)

	bobKeyPair, err := GenerateKyberKeyPair(ctx, Kyber768)
	require.NoError(t, err)

	// Alice creates channel to Bob
	aliceChannel, err := NewSecureChannel(ctx, aliceKeyPair, bobKeyPair.PublicKey)
	require.NoError(t, err)

	// Bob creates channel to Alice
	bobChannel, err := NewSecureChannel(ctx, bobKeyPair, aliceKeyPair.PublicKey)
	require.NoError(t, err)

	// Alice sends message to Bob
	encrypted, err := aliceChannel.Send(ctx, message)
	require.NoError(t, err)

	// Bob receives message from Alice
	decrypted, err := bobChannel.Receive(ctx, encrypted)
	require.NoError(t, err)

	assert.Equal(t, message, decrypted)
	assert.Equal(t, aliceChannel.GetSessionID(), bobChannel.GetSessionID())
}

func TestErrorHandling(t *testing.T) {
	ctx := context.Background()

	// Test empty inputs
	_, _, err := EncapsulateKyber(ctx, []byte{}, Kyber768)
	assert.Error(t, err)

	_, err = DecapsulateKyber(ctx, &KyberCiphertext{}, []byte{})
	assert.Error(t, err)

	_, err = SignDilithium(ctx, []byte{}, []byte{}, Dilithium3)
	assert.Error(t, err)

	_, err = VerifyDilithium(ctx, []byte{}, &DilithiumSignature{}, []byte{})
	assert.Error(t, err)

	// Test invalid security levels
	_, err = GenerateKyberKeyPair(ctx, SecurityLevel(999))
	assert.Error(t, err)

	_, err = GenerateDilithiumKeyPair(ctx, DilithiumLevel(999))
	assert.Error(t, err)
}

func BenchmarkKyberEncapsulation(b *testing.B) {
	ctx := context.Background()
	keyPair, _ := GenerateKyberKeyPair(ctx, Kyber768)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, _, _ = EncapsulateKyber(ctx, keyPair.PublicKey, Kyber768)
	}
}

func BenchmarkDilithiumSign(b *testing.B) {
	ctx := context.Background()
	keyPair, _ := GenerateDilithiumKeyPair(ctx, Dilithium3)
	message := []byte("Benchmark message")

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, _ = SignDilithium(ctx, message, keyPair.PrivateKey, Dilithium3)
	}
}

func BenchmarkDilithiumVerify(b *testing.B) {
	ctx := context.Background()
	keyPair, _ := GenerateDilithiumKeyPair(ctx, Dilithium3)
	message := []byte("Benchmark message")
	signature, _ := SignDilithium(ctx, message, keyPair.PrivateKey, Dilithium3)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, _ = VerifyDilithium(ctx, message, signature, keyPair.PublicKey)
	}
}

func BenchmarkHybridEncryption(b *testing.B) {
	ctx := context.Background()
	keyPair, _ := GenerateKyberKeyPair(ctx, Kyber768)
	encryptor := NewHybridEncryptor()
	data := []byte("Benchmark data for hybrid encryption")

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, _ = encryptor.Encrypt(ctx, data, keyPair.PublicKey)
	}
}