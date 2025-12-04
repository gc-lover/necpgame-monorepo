// Issue: #1604
package server

import "time"

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

