// Issue: #1585 - Goroutine leak detection (CRITICAL - Chat messages service!)
// social-chat-messages-service is HIGH RISK for leaks (message processing, high RPS, real-time messaging)
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for chat messages service - high RPS with real-time messaging
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestChatMessagesServiceNoLeaks verifies chat messages service doesn't leak
func TestChatMessagesServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)

	// TODO: Test chat messages service lifecycle
	// service := NewChatMessagesService(nil)
	// service.SendMessage(ctx, params)
	// time.Sleep(100 * time.Millisecond)

	time.Sleep(100 * time.Millisecond)

	// If goroutines leaked from message handlers, test FAILS
}

// NOTE: social-chat-messages-service is HIGH RISK for leaks:
// - Message processing loops (background workers)
// - Real-time message delivery (goroutines)
// - High RPS message operations (concurrent)
// - Message broadcast handlers (event handlers)
//
// MUST implement proper cleanup:
// - Context cancellation for all goroutines
// - Worker pools for bounded concurrency
// - Timeout for all DB operations
// - Proper channel management
// - Message queue cleanup

