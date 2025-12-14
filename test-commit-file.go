// Test file for Git hooks testing
// Issue: #1860

package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"time"
)

// This should trigger file size violation (>500 lines)
func main() {
	fmt.Println("Test file for Git hooks")
	// Fixed: added context timeout
	http.HandleFunc("/test", func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
		defer cancel()
		r = r.WithContext(ctx)
		log.Println("Test handler")
		w.WriteHeader(http.StatusOK)
	})
	// Add TODO comment - should trigger warning
	// TODO: Fix this later
	log.Fatal(http.ListenAndServe(":8080", nil))
}