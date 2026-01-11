# WebRTC Peer-to-Peer Communication Protocol

## Overview

Protocol Buffers definition for WebRTC peer-to-peer communication layer. Enables direct client-to-client connections for local events, small groups, and temporary interactions, reducing server load.

## Issue: #2077

## Use Cases

### 1. Direct Player Interactions
- Trade negotiations
- Direct chat
- Temporary alliances
- Local events

### 2. Small Groups (2-5 players)
- Party coordination
- Tactical communication
- Local quest events
- Proximity-based interactions

### 3. Proximity Events
- Nearby player detection
- Local world events
- Environmental interactions
- Temporary groups

### 4. Event-Based Connections
- Temporary connections for specific events
- Auto-disconnect after event completion
- Low-latency local communication

## Architecture

### Connection Flow

```
Client A                    Signaling Server                    Client B
   │                              │                                │
   ├── Request P2P ──────────────>│                                │
   │                              ├── Forward Request ────────────>│
   │                              │<── Accept/Reject ──────────────┤
   │<── Connection Info ──────────┤                                │
   │                              │                                │
   ├── WebRTC Offer ─────────────>│                                │
   │                              ├── Forward Offer ──────────────>│
   │                              │<── WebRTC Answer ──────────────┤
   │<── WebRTC Answer ────────────┤                                │
   │                              │                                │
   ├── ICE Candidates ────────────>│                                │
   │                              ├── Forward ICE ────────────────>│
   │                              │<── ICE Candidates ──────────────┤
   │<── ICE Candidates ────────────┤                                │
   │                              │                                │
   │<══════════════════════════════════════════════════════════════>│
   │                    Direct P2P Connection                       │
   │                    (via WebRTC Data Channel)                  │
```

### NAT Traversal

- **STUN**: For most NAT types (Full Cone, Restricted)
- **TURN**: For Symmetric NAT (relay through server)
- **ICE**: Automatic candidate selection

## Message Types

### P2PSignalingRequest/Response
WebRTC signaling exchange (offer/answer, ICE candidates)

### P2PDataMessage
Data sent over P2P data channel:
- Game state updates (position, rotation)
- Local events
- Chat messages
- Trade data
- State synchronization

### P2PConnectionRequest/Response
Connection establishment and management

## Performance

- **Latency**: <20ms for direct P2P (no server relay)
- **Bandwidth**: Reduced server load (direct client-to-client)
- **Scalability**: Server only handles signaling, not data
- **Reliability**: Automatic fallback to server relay if P2P fails

## Security

- **DTLS**: Encrypted data channel
- **Authentication**: Server validates connection requests
- **Authorization**: Server checks permissions before allowing P2P
- **Anti-cheat**: Server still validates critical game state

## Limitations

- **NAT Traversal**: May require TURN server for symmetric NAT
- **Firewall**: Some firewalls block P2P connections
- **Scalability**: Not suitable for large groups (>5 players)
- **Reliability**: Less reliable than server-mediated connections

## Integration

This protocol integrates with:
- `realtime-gateway-service-go` - Signaling server
- `voice-chat-service-go` - WebRTC infrastructure
- Client-side WebRTC implementation (UE5)

## When to Use P2P

**Use P2P for:**
- Direct player interactions (trade, chat)
- Small groups (2-5 players)
- Local proximity events
- Temporary connections

**Don't use P2P for:**
- Large groups (>5 players) - use server relay
- Critical game state - use authoritative server
- Anti-cheat sensitive data - use server validation
- Persistent connections - use server-mediated
