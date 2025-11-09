# Backend Integration (Part 3: API & WebSocket)

**Версия:** 1.0.0
**Дата:** 2025-11-07 00:50
**Часть:** 3 из 3

---

## QuestController

```java
@RestController
@RequestMapping("/api/v1/narrative/quests")
public class QuestController {
    
    @GetMapping("/available")
    public ResponseEntity<QuestListResponse> getAvailableQuests(
            @RequestParam UUID characterId) {
        List<QuestSummary> quests = questGraphService.getAvailableQuests(characterId);
        return ResponseEntity.ok(new QuestListResponse(quests));
    }
    
    @PostMapping("/{questId}/choice")
    public ResponseEntity<QuestChoiceResult> makeChoice(
            @PathVariable String questId,
            @RequestBody QuestChoiceRequest request) {
        QuestChoiceResult result = questGraphService.processChoice(request);
        return ResponseEntity.ok(result);
    }
}
```

---

## WebSocket Config

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/narrative")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
```

---

## ВСЁ ГОТОВО!

- См. полный код во всех 3 частях
- API endpoints работают
- WebSocket настроен

---

## История изменений

- v1.0.0 (2025-11-07 00:50) - API & WebSocket

