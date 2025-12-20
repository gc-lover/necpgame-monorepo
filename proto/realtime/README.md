# Protocol Buffers for Realtime Network Communication

**Issue:** #142109960  
**Агент:** Network Engineer

## Описание

Директория содержит Protocol Buffer определения для realtime сетевой коммуникации, включая базовые сообщения, управление
зонами, тикрейт и переключение протоколов.

## Файлы

### `realtime.proto`

Базовые сообщения для realtime коммуникации:

- Heartbeat/HeartbeatAck - для измерения задержки
- Echo/EchoAck - для тестирования соединения
- PlayerInput - входные данные от игрока
- EntityState - состояние игровой сущности
- GameSnapshot/GameDelta - состояние игры
- ClientMessage/ServerMessage - обертки для сообщений

### `network-config.proto`

Конфигурационные сообщения для сетевой инфраструктуры:

- `TickRateConfig` - конфигурация тикрейта
- `ZoneConfig` - конфигурация зоны
- `ProtocolSwitchRequest/Response/Status` - переключение протоколов
- `ZoneJoinRequest/Response` - присоединение к зоне
- `TickRateUpdate` - обновление тикрейта
- `AdaptiveTickRateConfig` - конфигурация адаптивного тикрейта
- `NetworkMetrics` - метрики сети
- `SpatialPartitioningConfig` - конфигурация spatial partitioning
- `SpatialShardingConfig` - конфигурация spatial sharding
- `ClusterConfig` - конфигурация кластера
- `NetworkOptimizationConfig` - общая конфигурация оптимизации

### `network-messages.proto`

Расширенные сообщения для сетевой коммуникации:

- `NetworkConfigMessage` - обертка для конфигурационных сообщений
- `ExtendedServerMessage` - расширенные серверные сообщения (включая новые типы)
- `ExtendedClientMessage` - расширенные клиентские сообщения (включая новые типы)

### `udp-reliability.proto`

Протокол надежности поверх UDP:

- `UDPPacketHeader` - заголовок UDP пакета (sequence number, ACK, flags)
- `UDPAck/UDPNack` - подтверждения и отрицательные подтверждения пакетов
- `UDPConnectionRequest/Ack/Confirm` - установление UDP соединения (3-way handshake)
- `UDPHeartbeat/HeartbeatAck` - heartbeat для поддержания соединения
- `UDPReliablePacket/UDPUnreliablePacket` - надежные и ненадежные пакеты
- `UDPRetransmitRequest` - запрос повторной передачи пакетов
- `UDPConnectionStats` - статистика UDP соединения
- `UDPPacket` - обертка для всех типов UDP пакетов

## Использование

### Генерация кода

Для Go:

```bash
protoc --go_out=. --go_opt=paths=source_relative \
  proto/realtime/realtime.proto \
  proto/realtime/network-config.proto \
  proto/realtime/network-messages.proto \
  proto/realtime/udp-reliability.proto
```

Для C++ (UE5):

```bash
protoc --cpp_out=. \
  proto/realtime/realtime.proto \
  proto/realtime/network-config.proto \
  proto/realtime/network-messages.proto \
  proto/realtime/udp-reliability.proto
```

## Структура сообщений

### Тикрейт

```protobuf
message TickRateConfig {
  int32 base = 1;
  int32 min = 2;
  int32 max = 3;
  bool adaptive = 4;
  int32 current = 5;
}
```

### Управление зонами

```protobuf
message ZoneConfig {
  string zone_id = 1;
  string zone_type = 2;
  string protocol = 3;
  TickRateConfig tickrate = 4;
  int32 max_players = 5;
  int32 current_players = 6;
  int32 latency_target_ms = 7;
}
```

### Переключение протоколов

```protobuf
message ProtocolSwitchRequest {
  string session_id = 1;
  string current_protocol = 2;
  string target_protocol = 3;
  string zone_id = 4;
  string reason = 5;
}

message ProtocolSwitchResponse {
  bool success = 1;
  string new_protocol = 2;
  string udp_endpoint = 3;
  int32 udp_port = 4;
  string session_token = 5;
  int64 switch_time_ms = 6;
}
```

### UDP Надежность

```protobuf
message UDPPacketHeader {
  uint32 sequence_number = 1;
  uint32 ack_number = 2;
  uint64 ack_bitfield = 3;
  uint32 flags = 4;
  uint32 payload_type = 5;
}

message UDPAck {
  uint32 ack_number = 1;
  uint64 ack_bitfield = 2;
  uint64 timestamp_ms = 3;
}
```

## Версионирование

Все proto файлы используют `proto3` синтаксис и следуют правилам backward compatibility:

- Новые поля добавляются в конец
- Удаление полей запрещено (помечаются как deprecated)
- Изменение типов полей запрещено

## Связанные файлы

- `infrastructure/network/tickrate-config.yaml` - конфигурация тикрейта
- `infrastructure/network/adaptive-tickrate-config.yaml` - адаптивный тикрейт
- `infrastructure/envoy/envoy-hybrid-network.yaml` - конфигурация Envoy

