package main

import (
	"testing"
)

func TestGetEnvDefault(t *testing.T) {
	t.Setenv("MOVEMENT_TEST_ENV", "")
	if got := getEnv("MOVEMENT_TEST_ENV", "default"); got != "default" {
		t.Fatalf("expected default value, got %q", got)
	}
}

func TestGetEnvOverride(t *testing.T) {
	t.Setenv("MOVEMENT_TEST_ENV", "custom")
	if got := getEnv("MOVEMENT_TEST_ENV", "default"); got != "custom" {
		t.Fatalf("expected env override, got %q", got)
	}
}


