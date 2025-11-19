package main

import (
	"flag"
	"fmt"
	"log"
	"net/url"
	"os"
	"os/signal"
	"time"

	"github.com/gorilla/websocket"
)

func main() {
	serverAddr := flag.String("addr", "127.0.0.1:18080", "WebSocket server address")
	token := flag.String("token", "test-token", "Auth token")
	flag.Parse()

	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt)

	u := url.URL{
		Scheme:   "ws",
		Host:     *serverAddr,
		Path:     "/ws",
		RawQuery: fmt.Sprintf("token=%s", *token),
	}

	log.Printf("Connecting to %s (imitating UE5 connection)...", u.String())

	dialer := websocket.Dialer{
		HandshakeTimeout: 10 * time.Second,
		ReadBufferSize:   4096,
		WriteBufferSize:  4096,
	}

	headers := make(map[string][]string)
	headers["User-Agent"] = []string{"UnrealEngine/5.0"}

	conn, resp, err := dialer.Dial(u.String(), headers)
	if err != nil {
		log.Fatalf("Failed to connect: %v (Response: %+v)", err, resp)
	}
	defer conn.Close()

	log.Printf("✓ Connected successfully!")
	log.Printf("  Remote address: %s", conn.RemoteAddr().String())
	log.Printf("  Response status: %s", resp.Status)

	done := make(chan struct{})

	go func() {
		defer close(done)
		for {
			messageType, message, err := conn.ReadMessage()
			if err != nil {
				if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
					log.Printf("WebSocket error: %v", err)
				}
				return
			}

			switch messageType {
			case websocket.TextMessage:
				log.Printf("Received text message: %s", string(message))
			case websocket.BinaryMessage:
				log.Printf("Received binary message: %d bytes", len(message))
				if len(message) > 0 {
					log.Printf("  First 20 bytes (hex): %x", message[:min(20, len(message))])
				}
			case websocket.PingMessage:
				log.Printf("Received ping")
			case websocket.PongMessage:
				log.Printf("Received pong")
			}
		}
	}()

	ticker := time.NewTicker(5 * time.Second)
	defer ticker.Stop()

	log.Println("Waiting for messages (press Ctrl+C to exit)...")

	for {
		select {
		case <-done:
			log.Println("Connection closed by server")
			return

		case t := <-ticker.C:
			err := conn.WriteMessage(websocket.TextMessage, []byte(fmt.Sprintf("ping %s", t.Format(time.RFC3339))))
			if err != nil {
				log.Printf("Write error: %v", err)
				return
			}
			log.Println("Sent ping message")

		case <-interrupt:
			log.Println("Interrupt received, closing connection...")

			err := conn.WriteMessage(websocket.CloseMessage, websocket.FormatCloseMessage(websocket.CloseNormalClosure, ""))
			if err != nil {
				log.Printf("Write close error: %v", err)
				return
			}

			select {
			case <-done:
			case <-time.After(time.Second):
			}
			return
		}
	}
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

