# Real-Time Spatial Audio System Protocol

## Overview

Protocol Buffers definition for real-time spatial audio system providing immersive 3D sound for game events. Designed for MMOFPS games requiring <50ms audio latency with HRTF, Doppler effect, and occlusion.

## Issue: #2003

## Features

### 1. 3D Spatial Audio
- Position-based audio sources
- Distance attenuation
- Rolloff curves
- Min/max distance control

### 2. HRTF (Head-Related Transfer Function)
- Realistic 3D audio positioning
- Left/right ear simulation
- Interaural time difference (ITD)
- Interaural level difference (ILD)

### 3. Doppler Effect
- Velocity-based pitch shifting
- Realistic sound for moving sources
- Speed of sound calculation
- Dynamic pitch adjustment

### 4. Audio Occlusion
- Sound blocked by obstacles
- Raycasting for occlusion detection
- Reverb for occluded sounds
- Dynamic occlusion factor

### 5. Audio Source Types
- Weapon sounds (fire, reload)
- Explosions and impacts
- Vehicle engines
- Environmental sounds
- NPC voices and footsteps
- Player actions
- Ambient sounds

## Architecture

### Audio Processing Pipeline
```
Audio Source → Spatial Processing → HRTF → Doppler → Occlusion → Client
     │                │              │        │          │
     │                │              │        │          │
  Position        Distance        ITD/ILD   Velocity   Obstacles
  Velocity       Attenuation      Head      Pitch      Reverb
```

### Performance

- **Audio Latency**: <50ms P99
- **Spatial Accuracy**: <1m position error
- **Concurrent Sources**: 100+ per player
- **Update Rate**: 60 Hz (16ms intervals)
- **Bandwidth**: ~50-100 Kbps per player

## Integration

This protocol integrates with:
- `realtime-gateway-service-go` - Audio streaming server
- `voice-chat-service-go` - Separate voice chat (not spatial audio)
- Client-side audio engine (UE5)

## Use Cases

1. **Weapon Sounds**: Realistic 3D positioning of gunfire
2. **Explosions**: Distance-based impact sounds
3. **Vehicles**: Doppler effect for moving vehicles
4. **NPCs**: Spatial positioning of NPC voices
5. **Environment**: Ambient sounds with occlusion
6. **Player Actions**: Footsteps, interactions with spatial audio

## Difference from Voice Chat

- **Voice Chat**: Player-to-player communication (WebRTC, SFU)
- **Spatial Audio**: Game event sounds (weapons, explosions, environment)

Both systems can run simultaneously but serve different purposes.
