# Backend Integration (Part 2: Services)

**Версия:** 1.0.0
**Дата:** 2025-11-07 00:49
**Часть:** 2 из 3

---

## QuestGraphService

### Основные методы

```java
@Service
@Slf4j
public class QuestGraphService {
    
    @Autowired
    private QuestRepository questRepository;
    
    @Autowired
    private PlayerFlagRepository flagRepository;
    
    private Map<String, QuestNode> questGraph;
    
    @PostConstruct
    public void loadQuestGraph() {
        ObjectMapper mapper = new ObjectMapper();
        QuestGraphData data = mapper.readValue(
            new ClassPathResource("data/narrative/quest-dependencies-full.json").getFile(),
            QuestGraphData.class
        );
        questGraph = buildGraph(data);
    }
    
    public boolean isQuestAvailable(String questId, UUID characterId) {
        Quest quest = questRepository.findById(questId).orElseThrow();
        
        if (!checkPrerequisites(quest, characterId)) return false;
        if (!checkRequiredFlags(quest, characterId)) return false;
        if (isQuestBlocked(questId, characterId)) return false;
        
        return true;
    }
    
    public List<QuestSummary> getAvailableQuests(UUID characterId) {
        String era = getCurrentEra();
        return questRepository.findByEra(era).stream()
            .filter(q -> isQuestAvailable(q.getId(), characterId))
            .map(this::toQuestSummary)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public QuestChoiceResult processChoice(UUID characterId, String questId, 
                                           Integer nodeId, String choiceId) {
        // Apply consequences
        // Set flags
        // Unlock quests
        // Return result
    }
}
```

---

## WorldStateService

### Основные методы

```java
@Service
public class WorldStateService {
    
    public WorldStateView getWorldState(UUID characterId, String serverId) {
        // Combine personal + server + faction states
    }
    
    @Transactional
    public VoteResult castVote(UUID characterId, String serverId,
                               String stateKey, Object voteValue) {
        // Save vote
        // Check threshold
        // Apply change if reached
    }
}
```

---

## История изменений

- v1.0.0 (2025-11-07 00:49) - Services

