// Issue: #53
package main

import (
	"context"
	"testing"
	"time"
)

func TestInMemoryStateStore_UpsertAndGet(t *testing.T) {
	store := newInMemoryStateStore()
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Millisecond)
	defer cancel()

	req := stateMutationRequest{Key: "k1", Category: "c1", Value: []byte(`{"v":1}`)}
	entry, err := store.upsert(ctx, req)
	if err != nil {
		t.Fatalf("upsert err: %v", err)
	}
	if entry.Version != 1 {
		t.Fatalf("expected version 1, got %d", entry.Version)
	}

	got, ok, err := store.get(ctx, "k1")
	if err != nil || !ok {
		t.Fatalf("get err=%v ok=%v", err, ok)
	}
	if string(got.Value) != string(req.Value) {
		t.Fatalf("value mismatch: %s", string(got.Value))
	}
}

func TestInMemoryStateStore_VersionConflict(t *testing.T) {
	store := newInMemoryStateStore()
	ctx := context.Background()

	if _, err := store.upsert(ctx, stateMutationRequest{Key: "k2", Category: "c", Value: []byte(`{}`)}); err != nil {
		t.Fatalf("initial upsert err: %v", err)
	}

	conflictReq := stateMutationRequest{
		Key:             "k2",
		Category:        "c",
		Value:           []byte(`{}`),
		ExpectedVersion: uint64Ptr(5),
	}
	if _, err := store.upsert(ctx, conflictReq); err == nil {
		t.Fatalf("expected conflict error")
	}
}

func TestInMemoryEventStore_Limit(t *testing.T) {
	store := newInMemoryEventStore(2)
	ctx := context.Background()

	for i := 0; i < 3; i++ {
		_ = store.save(ctx, stateEvent{ID: string('a' + rune(i))})
	}

	events, err := store.list(ctx, 10)
	if err != nil {
		t.Fatalf("list err: %v", err)
	}
	if len(events) != 2 {
		t.Fatalf("expected 2 events, got %d", len(events))
	}
	if events[0].ID == events[1].ID {
		t.Fatalf("events should be distinct")
	}
}

func uint64Ptr(v uint64) *uint64 {
	return &v
}



















