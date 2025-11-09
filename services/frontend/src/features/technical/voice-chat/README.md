# Voice Chat Feature
Панель управления голосовыми каналами: создание, участники, качество и spatial audio.

**OpenAPI:** social/voice/voice-chat.yaml | **Роут:** /technical/voice-chat

## UI
- `VoiceChatPage` — SPA (380 / flex / 320), фильтры типа/владельца, auto moderation toggle
- Компоненты:
  - `VoiceChannelCard`
  - `VoiceParticipantCard`
  - `VoiceControlsCard`
  - `SpatialAudioCard`
  - `VoiceChannelSettingsCard`
  - `VoiceQualityCard`

## Возможности
- Просмотр каналов (guild/party/raid/proximity) и загрузки
- Список участников с mute/deafen статусами
- Управление устройствами, шумоподавлением и spatial audio
- Настройки канала (quality preset, auto close, роли)
- Мониторинг качества (bitrate, packet loss, jitter)
- Компактная cyberpunk сетка на одном экране

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**


