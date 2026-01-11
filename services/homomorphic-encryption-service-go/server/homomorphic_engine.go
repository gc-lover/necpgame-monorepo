package server

import (
	"crypto/rand"
	"encoding/json"
	"fmt"
	"log/slog"
	"math"
	"sync"

	"github.com/google/uuid"
	"github.com/tuneinsight/lattigo/v5/core/rlwe"
	"github.com/tuneinsight/lattigo/v5/he/heint"
	"github.com/tuneinsight/lattigo/v5/ring"
	"github.com/tuneinsight/lattigo/v5/utils/sampling"
)

// HomomorphicEngine provides homomorphic encryption capabilities for game data
type HomomorphicEngine struct {
	params      heint.Parameters
	encryptor   *rlwe.Encryptor
	decryptor   *rlwe.Decryptor
	evaluator   *heint.Evaluator
	keyManager  *KeyManager
	operationPool sync.Pool
}

// KeyManager manages encryption keys for different game entities
type KeyManager struct {
	keys     map[string]*KeyPair
	mu       sync.RWMutex
	keyStore KeyStore
}

// KeyPair holds public and private keys
type KeyPair struct {
	ID         string
	PublicKey  *rlwe.PublicKey
	PrivateKey *rlwe.SecretKey
	CreatedAt  int64
	UsageCount int64
}

// KeyStore interface for persistent key storage
type KeyStore interface {
	StoreKeyPair(keyPair *KeyPair) error
	GetKeyPair(keyID string) (*KeyPair, error)
	ListKeyPairs() ([]*KeyPair, error)
	DeleteKeyPair(keyID string) error
}

// EncryptedData represents homomorphically encrypted data
type EncryptedData struct {
	ID           string          `json:"id"`
	KeyID        string          `json:"key_id"`
	Ciphertext   []byte          `json:"ciphertext"`
	DataType     string          `json:"data_type"`
	Schema       json.RawMessage `json:"schema"`
	CreatedAt    int64           `json:"created_at"`
	OperationLog []OperationLog  `json:"operation_log"`
}

// OperationLog tracks operations performed on encrypted data
type OperationLog struct {
	Operation string `json:"operation"`
	Timestamp int64  `json:"timestamp"`
	ResultID  string `json:"result_id,omitempty"`
}

// NewHomomorphicEngine creates a new homomorphic encryption engine
func NewHomomorphicEngine(keyStore KeyStore) (*HomomorphicEngine, error) {
	// Use BFV scheme parameters optimized for game data (32-bit integers)
	params, err := heint.NewParametersFromLiteral(heint.ParametersLiteral{
		LogN:             14,                           // 2^14 = 16384 slots
		LogQ:             []int{50, 40, 40, 40, 40},   // Ciphertext modulus
		LogP:             []int{50},                    // Key-switch modulus
		LogSlots:         13,                           // 2^13 = 8192 slots
		H:                192,                          // Hamming weight
		Sigma:            rlwe.DefaultSigma,            // Gaussian sampling
		RingType:         ring.Standard,
		DefaultScale:     rlwe.NewScale(1 << 40),      // Scale for CKKS (not used in BFV)
		NTTFlag:          true,
	})
	if err != nil {
		return nil, fmt.Errorf("failed to create parameters: %w", err)
	}

	// Generate keys
	kgen := rlwe.NewKeyGenerator(params)
	sk, pk := kgen.GenKeyPairNew()

	// Create cryptographic primitives
	encryptor := rlwe.NewEncryptor(params, pk)
	decryptor := rlwe.NewDecryptor(params, sk)
	evaluator := heint.NewEvaluator(params, rlwe.NewMemEvaluationKeySet(kgen.GenRelinearizationKeyNew(sk)))

	keyManager := &KeyManager{
		keys:     make(map[string]*KeyPair),
		keyStore: keyStore,
	}

	// Initialize default key pair
	defaultKeyID := "default-game-key"
	keyPair := &KeyPair{
		ID:         defaultKeyID,
		PublicKey:  pk,
		PrivateKey: sk,
		CreatedAt:  0, // Will be set by store
	}

	if err := keyManager.StoreKeyPair(keyPair); err != nil {
		return nil, fmt.Errorf("failed to store default key pair: %w", err)
	}

	engine := &HomomorphicEngine{
		params:     params,
		encryptor:  encryptor,
		decryptor:  decryptor,
		evaluator:  evaluator,
		keyManager: keyManager,
	}

	engine.operationPool = sync.Pool{
		New: func() interface{} {
			return &heint.Plaintext{}
		},
	}

	slog.Info("Homomorphic encryption engine initialized",
		"logN", params.LogN(),
		"logSlots", params.LogSlots(),
		"ciphertextModuli", len(params.Q()),
	)

	return engine, nil
}

// EncryptGameData encrypts game data using homomorphic encryption
func (e *HomomorphicEngine) EncryptGameData(data interface{}, dataType string, keyID string) (*EncryptedData, error) {
	// Convert data to plaintext
	plaintext, err := e.dataToPlaintext(data)
	if err != nil {
		return nil, fmt.Errorf("failed to convert data to plaintext: %w", err)
	}

	// Encrypt
	ciphertext := heint.NewCiphertext(e.params, 1)
	if err := e.encryptor.Encrypt(plaintext, ciphertext); err != nil {
		return nil, fmt.Errorf("encryption failed: %w", err)
	}

	// Serialize ciphertext
	ciphertextBytes, err := ciphertext.MarshalBinary()
	if err != nil {
		return nil, fmt.Errorf("failed to serialize ciphertext: %w", err)
	}

	// Create schema for data structure
	schema, _ := json.Marshal(map[string]interface{}{
		"type": dataType,
		"encrypted": true,
	})

	encryptedData := &EncryptedData{
		ID:         uuid.New().String(),
		KeyID:      keyID,
		Ciphertext: ciphertextBytes,
		DataType:   dataType,
		Schema:     schema,
		CreatedAt:  0, // Will be set by timestamp
	}

	slog.Info("Game data encrypted successfully",
		"data_id", encryptedData.ID,
		"data_type", dataType,
		"key_id", keyID,
		"ciphertext_size", len(ciphertextBytes),
	)

	return encryptedData, nil
}

// DecryptGameData decrypts homomorphically encrypted data
func (e *HomomorphicEngine) DecryptGameData(encryptedData *EncryptedData) (interface{}, error) {
	// Deserialize ciphertext
	ciphertext := heint.NewCiphertext(e.params, 1)
	if err := ciphertext.UnmarshalBinary(encryptedData.Ciphertext); err != nil {
		return nil, fmt.Errorf("failed to deserialize ciphertext: %w", err)
	}

	// Decrypt
	plaintext := e.operationPool.Get().(*heint.Plaintext)
	defer e.operationPool.Put(plaintext)

	if err := e.decryptor.Decrypt(ciphertext, plaintext); err != nil {
		return nil, fmt.Errorf("decryption failed: %w", err)
	}

	// Convert back to data
	data, err := e.plaintextToData(plaintext, encryptedData.DataType)
	if err != nil {
		return nil, fmt.Errorf("failed to convert plaintext to data: %w", err)
	}

	slog.Info("Game data decrypted successfully",
		"data_id", encryptedData.ID,
		"data_type", encryptedData.DataType,
	)

	return data, nil
}

// AddEncryptedValues performs homomorphic addition on encrypted integers
func (e *HomomorphicEngine) AddEncryptedValues(ct1, ct2 *EncryptedData) (*EncryptedData, error) {
	if ct1.DataType != "integer" || ct2.DataType != "integer" {
		return nil, fmt.Errorf("addition only supported for integer types")
	}

	// Deserialize ciphertexts
	ciphertext1 := heint.NewCiphertext(e.params, 1)
	ciphertext2 := heint.NewCiphertext(e.params, 1)

	if err := ciphertext1.UnmarshalBinary(ct1.Ciphertext); err != nil {
		return nil, fmt.Errorf("failed to deserialize ciphertext1: %w", err)
	}
	if err := ciphertext2.UnmarshalBinary(ct2.Ciphertext); err != nil {
		return nil, fmt.Errorf("failed to deserialize ciphertext2: %w", err)
	}

	// Perform homomorphic addition
	result := heint.NewCiphertext(e.params, 1)
	if err := e.evaluator.Add(ciphertext1, ciphertext2, result); err != nil {
		return nil, fmt.Errorf("homomorphic addition failed: %w", err)
	}

	// Serialize result
	resultBytes, err := result.MarshalBinary()
	if err != nil {
		return nil, fmt.Errorf("failed to serialize result: %w", err)
	}

	operationLog := []OperationLog{
		{Operation: "add", Timestamp: 0, ResultID: ""},
	}

	resultData := &EncryptedData{
		ID:           uuid.New().String(),
		KeyID:        ct1.KeyID, // Assume same key
		Ciphertext:   resultBytes,
		DataType:     "integer",
		OperationLog: operationLog,
		CreatedAt:    0,
	}

	slog.Info("Homomorphic addition performed",
		"result_id", resultData.ID,
		"operand1_id", ct1.ID,
		"operand2_id", ct2.ID,
	)

	return resultData, nil
}

// MultiplyEncryptedValues performs homomorphic multiplication on encrypted integers
func (e *HomomorphicEngine) MultiplyEncryptedValues(ct1, ct2 *EncryptedData) (*EncryptedData, error) {
	if ct1.DataType != "integer" || ct2.DataType != "integer" {
		return nil, fmt.Errorf("multiplication only supported for integer types")
	}

	// Deserialize ciphertexts
	ciphertext1 := heint.NewCiphertext(e.params, 1)
	ciphertext2 := heint.NewCiphertext(e.params, 1)

	if err := ciphertext1.UnmarshalBinary(ct1.Ciphertext); err != nil {
		return nil, fmt.Errorf("failed to deserialize ciphertext1: %w", err)
	}
	if err := ciphertext2.UnmarshalBinary(ct2.Ciphertext); err != nil {
		return nil, fmt.Errorf("failed to deserialize ciphertext2: %w", err)
	}

	// Perform homomorphic multiplication
	result := heint.NewCiphertext(e.params, 1)
	if err := e.evaluator.Mul(ciphertext1, ciphertext2, result); err != nil {
		return nil, fmt.Errorf("homomorphic multiplication failed: %w", err)
	}

	// Relinearize to reduce ciphertext size
	if err := e.evaluator.Relinearize(result, result); err != nil {
		slog.Warn("Relinearization failed, continuing with larger ciphertext", "error", err)
	}

	// Serialize result
	resultBytes, err := result.MarshalBinary()
	if err != nil {
		return nil, fmt.Errorf("failed to serialize result: %w", err)
	}

	operationLog := []OperationLog{
		{Operation: "multiply", Timestamp: 0, ResultID: ""},
	}

	resultData := &EncryptedData{
		ID:           uuid.New().String(),
		KeyID:        ct1.KeyID,
		Ciphertext:   resultBytes,
		DataType:     "integer",
		OperationLog: operationLog,
		CreatedAt:    0,
	}

	slog.Info("Homomorphic multiplication performed",
		"result_id", resultData.ID,
		"operand1_id", ct1.ID,
		"operand2_id", ct2.ID,
	)

	return resultData, nil
}

// GeneratePlayerKeyPair generates a new key pair for a player
func (e *HomomorphicEngine) GeneratePlayerKeyPair(playerID string) (string, error) {
	kgen := rlwe.NewKeyGenerator(e.params)
	sk, pk := kgen.GenKeyPairNew()

	keyPair := &KeyPair{
		ID:         uuid.New().String(),
		PublicKey:  pk,
		PrivateKey: sk,
		CreatedAt:  0, // Will be set by store
	}

	if err := e.keyManager.StoreKeyPair(keyPair); err != nil {
		return "", fmt.Errorf("failed to store key pair: %w", err)
	}

	slog.Info("Player key pair generated",
		"player_id", playerID,
		"key_id", keyPair.ID,
	)

	return keyPair.ID, nil
}

// GetSupportedOperations returns list of supported homomorphic operations
func (e *HomomorphicEngine) GetSupportedOperations() []string {
	return []string{
		"encrypt",
		"decrypt",
		"add",
		"multiply",
		"compare", // Future implementation
		"aggregate", // Future implementation
	}
}

// dataToPlaintext converts Go data to Lattigo plaintext
func (e *HomomorphicEngine) dataToPlaintext(data interface{}) (*heint.Plaintext, error) {
	plaintext := heint.NewPlaintext(e.params, e.params.MaxLevel())

	switch v := data.(type) {
	case int:
		values := make([]int64, e.params.LogSlots())
		values[0] = int64(v)
		if err := plaintext.Encode(values, e.params); err != nil {
			return nil, err
		}
	case []int:
		values := make([]int64, len(v))
		for i, val := range v {
			values[i] = int64(val)
		}
		if err := plaintext.Encode(values, e.params); err != nil {
			return nil, err
		}
	case float64:
		// For floating point, we'd use CKKS scheme, but for now convert to int
		values := make([]int64, e.params.LogSlots())
		values[0] = int64(math.Round(v))
		if err := plaintext.Encode(values, e.params); err != nil {
			return nil, err
		}
	default:
		return nil, fmt.Errorf("unsupported data type: %T", data)
	}

	return plaintext, nil
}

// plaintextToData converts Lattigo plaintext back to Go data
func (e *HomomorphicEngine) plaintextToData(plaintext *heint.Plaintext, dataType string) (interface{}, error) {
	values := make([]int64, e.params.LogSlots())
	if err := plaintext.Decode(values, e.params); err != nil {
		return nil, err
	}

	switch dataType {
	case "integer":
		return int(values[0]), nil
	case "integer_array":
		result := make([]int, 0)
		for _, v := range values {
			if v != 0 { // Filter out zero padding
				result = append(result, int(v))
			}
		}
		return result, nil
	default:
		return nil, fmt.Errorf("unsupported data type: %s", dataType)
	}
}

// StoreKeyPair stores a key pair
func (km *KeyManager) StoreKeyPair(keyPair *KeyPair) error {
	km.mu.Lock()
	defer km.mu.Unlock()

	km.keys[keyPair.ID] = keyPair

	// Persist to store
	if km.keyStore != nil {
		if err := km.keyStore.StoreKeyPair(keyPair); err != nil {
			return err
		}
	}

	return nil
}

// GetKeyPair retrieves a key pair
func (km *KeyManager) GetKeyPair(keyID string) (*KeyPair, error) {
	km.mu.RLock()
	if keyPair, exists := km.keys[keyID]; exists {
		km.mu.RUnlock()
		return keyPair, nil
	}
	km.mu.RUnlock()

	// Try to load from store
	if km.keyStore != nil {
		return km.keyStore.GetKeyPair(keyID)
	}

	return nil, fmt.Errorf("key pair not found: %s", keyID)
}