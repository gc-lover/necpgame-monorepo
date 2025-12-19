// Verify Quest Import - Simple validation script
// Usage: go run scripts/verify-quest-import.go

package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"strings"
)

const questPath = "knowledge/canon/lore/timeline-author/quests/europe/paris/2020-2029/quest-006-montmartre-artists.yaml"

func main() {
	fmt.Println("🔍 Verifying Quest Import")
	fmt.Println("==========================")

	// Read YAML file
	yamlContent, err := ioutil.ReadFile(questPath)
	if err != nil {
		log.Fatalf("❌ Failed to read quest file: %v", err)
	}

	fmt.Printf("OK Quest file loaded: %s (%d bytes)\n", questPath, len(yamlContent))

	// Basic validation - check for required fields
	content := string(yamlContent)

	requiredFields := []string{
		"metadata:",
		"id:",
		"quest_definition:",
		"objectives:",
		"rewards:",
	}

	fmt.Println("\n📋 Checking required fields:")
	allPresent := true
	for _, field := range requiredFields {
		if strings.Contains(content, field) {
			fmt.Printf("  OK %s\n", field)
		} else {
			fmt.Printf("  ❌ %s - MISSING\n", field)
			allPresent = false
		}
	}

	if !allPresent {
		log.Fatal("❌ Quest validation failed - missing required fields")
	}

	// Extract quest ID
	questID := ""
	lines := strings.Split(content, "\n")
	for _, line := range lines {
		if strings.HasPrefix(line, "  id:") {
			parts := strings.SplitN(line, ":", 2)
			if len(parts) == 2 {
				questID = strings.TrimSpace(parts[1])
				break
			}
		}
	}

	if questID == "" {
		log.Fatal("❌ Could not extract quest ID")
	}

	fmt.Printf("\n🎯 Quest ID: %s\n", questID)

	// Simulate API request structure
	requestData := map[string]interface{}{
		"quest_id": questID,
		"yaml_content": map[string]interface{}{
			"content": content, // In real scenario, this would be JSON
		},
	}

	jsonData, err := json.MarshalIndent(requestData, "", "  ")
	if err != nil {
		log.Fatalf("❌ Failed to marshal request: %v", err)
	}

	fmt.Println("\n📤 Simulated API Request Structure:")
	fmt.Println(string(jsonData))

	fmt.Println("\nOK Quest validation successful!")
	fmt.Println("🎉 Quest is ready for import via POST /gameplay/quests/content/reload")

	// Issue: #616
}
