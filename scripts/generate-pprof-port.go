// Issue: #1584
// Deterministic pprof port generator based on service name hash
// Generates unique ports in range 6060-6999 for each service

package main

import (
	"crypto/sha256"
	"fmt"
	"os"
)

const (
	// Port range for pprof: 6060-6999 (940 ports available)
	PPROF_PORT_MIN   = 6060
	PPROF_PORT_MAX   = 6999
	PPROF_PORT_RANGE = PPROF_PORT_MAX - PPROF_PORT_MIN + 1
)

// generatePort generates a deterministic port number from service name
// Uses SHA256 hash to ensure uniform distribution
func generatePort(serviceName string) int {
	// Create hash from service name
	hash := sha256.Sum256([]byte(serviceName))

	// Convert first 4 bytes to uint32
	hashValue := uint32(hash[0])<<24 | uint32(hash[1])<<16 | uint32(hash[2])<<8 | uint32(hash[3])

	// Map to port range
	port := PPROF_PORT_MIN + int(hashValue%PPROF_PORT_RANGE)

	return port
}

func main() {
	if len(os.Args) < 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <service-name>\n", os.Args[0])
		fmt.Fprintf(os.Stderr, "Example: %s matchmaking-go\n", os.Args[0])
		os.Exit(1)
	}

	serviceName := os.Args[1]
	port := generatePort(serviceName)

	// Output in format suitable for YAML/config
	if len(os.Args) > 2 && os.Args[2] == "--yaml" {
		fmt.Printf("  %s: %d\n", serviceName, port)
	} else {
		fmt.Printf("%d\n", port)
	}
}
