// Issue: #150 - Skill Bucket Matcher Tests
package server

import (
	"testing"

	"github.com/google/uuid"
)

func TestSkillBucketMatcher_AddToQueue(t *testing.T) {
	matcher := NewSkillBucketMatcher()

	entry := &QueueEntry{
		ID:           uuid.New(),
		PlayerID:     uuid.New(),
		ActivityType: "pvp_5v5",
		Rating:       1500,
	}

	matcher.AddToQueue(entry)

	// Check queue size
	size := matcher.GetQueueSize("pvp_5v5", 1500)
	if size != 1 {
		t.Errorf("Expected queue size 1, got %d", size)
	}
}

func TestSkillBucketMatcher_GetQueueSize(t *testing.T) {
	matcher := NewSkillBucketMatcher()

	// Add 10 players with similar ratings
	for i := 0; i < 10; i++ {
		entry := &QueueEntry{
			ID:           uuid.New(),
			PlayerID:     uuid.New(),
			ActivityType: "pvp_5v5",
			Rating:       1500 + i, // 1500-1509
		}
		matcher.AddToQueue(entry)
	}

	// All should be in same bucket (1500-1999)
	size := matcher.GetQueueSize("pvp_5v5", 1505)
	if size != 10 {
		t.Errorf("Expected queue size 10, got %d", size)
	}
}

func TestSkillBucketMatcher_RemoveFromQueue(t *testing.T) {
	matcher := NewSkillBucketMatcher()

	entry := &QueueEntry{
		ID:           uuid.New(),
		PlayerID:     uuid.New(),
		ActivityType: "pvp_5v5",
		Rating:       1500,
	}

	matcher.AddToQueue(entry)
	matcher.RemoveFromQueue(entry)

	size := matcher.GetQueueSize("pvp_5v5", 1500)
	if size != 0 {
		t.Errorf("Expected queue size 0 after removal, got %d", size)
	}
}

// Benchmark O(1) skill bucket matching
func BenchmarkSkillBucketMatcher_GetQueueSize(b *testing.B) {
	matcher := NewSkillBucketMatcher()

	// Populate with 10k players
	for i := 0; i < 10000; i++ {
		entry := &QueueEntry{
			ID:           uuid.New(),
			PlayerID:     uuid.New(),
			ActivityType: "pvp_5v5",
			Rating:       1000 + (i % 2000), // Spread 1000-3000
		}
		matcher.AddToQueue(entry)
	}

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_ = matcher.GetQueueSize("pvp_5v5", 1750)
	}
	
	// Expected: <1μs per operation, 0 allocs
}

// Benchmark AddToQueue
func BenchmarkSkillBucketMatcher_AddToQueue(b *testing.B) {
	matcher := NewSkillBucketMatcher()

	entries := make([]*QueueEntry, b.N)
	for i := 0; i < b.N; i++ {
		entries[i] = &QueueEntry{
			ID:           uuid.New(),
			PlayerID:     uuid.New(),
			ActivityType: "pvp_5v5",
			Rating:       1500 + (i % 1000),
		}
	}

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		matcher.AddToQueue(entries[i])
	}
	
	// Expected: <500ns per operation, 1-2 allocs
}

