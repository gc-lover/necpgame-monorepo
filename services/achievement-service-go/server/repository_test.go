// Issue: #391
package server

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestNewRepository(t *testing.T) {
	// Test with invalid connection string
	_, err := NewRepository("invalid://connection")
	assert.Error(t, err)

	// Test with valid connection string (but may not connect)
	connStr := "postgres://user:pass@localhost:5432/test?sslmode=disable"
	repo, err := NewRepository(connStr)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer repo.Close()

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)

	// Test connection pool settings
	stats := repo.db.Stats()
	assert.Equal(t, 25, stats.MaxOpenConnections)
}

func TestRepository_Close(t *testing.T) {
	connStr := "postgres://user:pass@localhost:5432/test?sslmode=disable"
	repo, err := NewRepository(connStr)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}

	// Test close
	err = repo.Close()
	assert.NoError(t, err)

	// Test double close should not panic
	err = repo.Close()
	// May return error, but should not panic
}

func TestRepository_ConnectionPoolSettings(t *testing.T) {
	connStr := "postgres://user:pass@localhost:5432/test?sslmode=disable"
	repo, err := NewRepository(connStr)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer repo.Close()

	stats := repo.db.Stats()
	
	// Check pool settings match our configuration
	assert.Equal(t, 25, stats.MaxOpenConnections)
	
	// Test that connection is working
	err = repo.db.Ping()
	if err != nil {
		t.Skipf("Database ping failed: %v", err)
	}
}

func TestRepository_ConnectionLifetime(t *testing.T) {
	connStr := "postgres://user:pass@localhost:5432/test?sslmode=disable"
	repo, err := NewRepository(connStr)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer repo.Close()

	// Test connection settings are applied
	// We can't directly test the lifetime, but we can ensure the repository was created
	assert.NotNil(t, repo.db)
	
	// Verify the settings indirectly through stats
	stats := repo.db.Stats()
	assert.Equal(t, 25, stats.MaxOpenConnections)
}

// Edge case tests
func TestRepository_EdgeCases(t *testing.T) {
	t.Run("Empty connection string", func(t *testing.T) {
		_, err := NewRepository("")
		assert.Error(t, err)
	})

	t.Run("Invalid protocol", func(t *testing.T) {
		_, err := NewRepository("mysql://user:pass@localhost:3306/test")
		assert.Error(t, err)
	})

	t.Run("Invalid host", func(t *testing.T) {
		// This should fail on ping, not on connection string parsing
		_, err := NewRepository("postgres://user:pass@nonexistent-host:5432/test?sslmode=disable")
		assert.Error(t, err)
	})
}

func BenchmarkNewRepository(b *testing.B) {
	connStr := "postgres://user:pass@localhost:5432/test?sslmode=disable"
	
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		repo, err := NewRepository(connStr)
		if err != nil {
			b.Skipf("Database connection failed: %v", err)
		}
		if repo != nil {
			repo.Close()
		}
	}
}
