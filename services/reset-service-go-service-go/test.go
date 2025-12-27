package main

import (
	"fmt"
	"net/http"
)

func main() {
	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(200)
		fmt.Fprintf(w, `{"status": "healthy"}`)
	})

	fmt.Println("Starting test server on :8081")
	http.ListenAndServe(":8081", nil)
}
