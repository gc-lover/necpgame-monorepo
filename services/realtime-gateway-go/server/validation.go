// Package server Issue: #141889273
package server

import "unicode"

// isValidPlayerID validates player ID format
func isValidPlayerID(id string) bool {
	if len(id) == 0 || len(id) > 20 {
		return false
	}
	if len(id) >= 2 && id[0] == 'p' {
		for i := 1; i < len(id); i++ {
			if !((id[i] >= '0' && id[i] <= '9') || (id[i] >= 'a' && id[i] <= 'f') || (id[i] >= 'A' && id[i] <= 'F')) {
				return false
			}
		}
		return true
	}
	for _, r := range id {
		if !unicode.IsPrint(r) && r != '\n' && r != '\r' && r != '\t' {
			return false
		}
	}
	return true
}
