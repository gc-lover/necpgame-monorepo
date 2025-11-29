# Порты всех сервисов

Справочник портов для правильной настройки Dockerfile и docker-compose.yml.

## Сервисы с портами

| Сервис | HTTP порт | Metrics порт | Docker-compose HTTP | Docker-compose Metrics |
|--------|-----------|--------------|---------------------|------------------------|
| achievement-service-go | 8083 | 9093 | 8085 | 9098 |
| admin-service-go | 8097 | 9097 | 8090 | 9103 |
| character-service-go | 8087 | 9092 | 8087 | 9096 |
| inventory-service-go | 8083 | 9093 | 8085 | 9094 |
| movement-service-go | 8086 | 9091 | 8086 | 9095 |
| social-service-go | 8084 | 9094 | 8084 | 9097 |
| economy-service-go | 8086 | 9096 | 8086 | 9099 |
| support-service-go | 8094 | 9097 | 8087 | 9100 |
| reset-service-go | 8088 | 9098 | 8088 | 9101 |
| gameplay-service-go | 8083 | 9093 | 8083 | 9102 |
| clan-war-service-go | 8092 | 9092 | 8092 | 9104 |
| companion-service-go | 8089 | 9099 | 8089 | 9105 |
| voice-chat-service-go | 8096 | 9096 | 8091 | 9106 |
| housing-service-go | 8090 | 9090 | 8093 | 9107 |
| realtime-gateway-go | 18080 | 9090 | 18080 | 9093 |
| ws-lobby-go | 18081 | 9090 | 18081 | 9091 |
| matchmaking-go | - | 9090 | - | 9092 |

Примечание: Порты в docker-compose.yml могут отличаться от портов в коде из-за маппинга.

