# Backend Integration (Part 1: Entities & Repositories)

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:49  
**Часть:** 1 из 3

---

## Навигация

- **Part 1:** Entities & Repositories (этот файл)
- **Part 2:** [Services](./backend-integration-part2-services.md)
- **Part 3:** [Controllers & WebSocket](./backend-integration-part3-api.md)

---

## Entities (JPA)

### Quest.java

```java
@Entity
@Table(name = "quests")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Quest {
    @Id
    @Column(length = 100)
    private String id;
    
    @Column(nullable = false, length = 200)
    private String name;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private QuestType type;
    
    @Column(name = "min_level", nullable = false)
    private Integer minLevel = 1;
    
    @Type(type = "jsonb")
    @Column(name = "required_quests", columnDefinition = "jsonb")
    private List<String> requiredQuests;
    
    @Type(type = "jsonb")
    @Column(name = "required_flags", columnDefinition = "jsonb")
    private Map<String, Object> requiredFlags;
    
    @Column(name = "has_branches")
    private Boolean hasBranches = false;
    
    @Column(nullable = false, length = 20)
    private String era;
    
    @Column(length = 100)
    private String region;
    
    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL)
    private List<QuestBranch> branches;
    
    // Getters, setters, constructors
}

public enum QuestType {
    MAIN, SIDE, FACTION, DAILY, WEEKLY, EVENT, DYNAMIC, ROMANTIC
}
```

### QuestBranch.java

```java
@Entity
@Table(name = "quest_branches")
public class QuestBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;
    
    @Column(name = "branch_id", length = 50)
    private String branchId;
    
    @Column(name = "branch_name", length = 200)
    private String branchName;
    
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> conditions;
    
    @Type(type = "jsonb")
    @Column(name = "reputation_changes", columnDefinition = "jsonb")
    private Map<String, Integer> reputationChanges;
    
    @Type(type = "jsonb")
    @Column(name = "sets_flags", columnDefinition = "jsonb")
    private List<String> setsFlags;
    
    @Type(type = "jsonb")
    @Column(name = "unlocks_quests", columnDefinition = "jsonb")
    private List<String> unlocksQuests;
    
    @Type(type = "jsonb")
    @Column(name = "locks_quests", columnDefinition = "jsonb")
    private List<String> locksQuests;
    
    // Getters, setters
}
```

### PlayerFlag.java

```java
@Entity
@Table(name = "player_flags")
public class PlayerFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "character_id", nullable = false)
    private UUID characterId;
    
    @Column(name = "flag_key", length = 100, nullable = false)
    private String flagKey;
    
    @Type(type = "jsonb")
    @Column(name = "flag_value", columnDefinition = "jsonb", nullable = false)
    private Object flagValue;
    
    @Column(name = "set_by_quest", length = 100)
    private String setByQuest;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    // Getters, setters
}
```

---

## Repositories

### QuestRepository.java

```java
@Repository
public interface QuestRepository extends JpaRepository<Quest, String> {
    
    @Query("SELECT q FROM Quest q WHERE q.era = :era AND q.isActive = true")
    List<Quest> findByEra(@Param("era") String era);
    
    @Query("SELECT q FROM Quest q WHERE q.type = :type")
    List<Quest> findByType(@Param("type") QuestType type);
    
    @Query("SELECT q FROM Quest q WHERE q.minLevel <= :level")
    List<Quest> findByLevelRange(@Param("level") Integer level);
}
```

### PlayerFlagRepository.java

```java
@Repository
public interface PlayerFlagRepository extends JpaRepository<PlayerFlag, Long> {
    
    List<PlayerFlag> findByCharacterId(UUID characterId);
    
    Optional<PlayerFlag> findByCharacterIdAndFlagKey(UUID characterId, String flagKey);
    
    @Query("SELECT CASE WHEN COUNT(pf) > 0 THEN TRUE ELSE FALSE END " +
           "FROM PlayerFlag pf WHERE pf.characterId = :characterId " +
           "AND pf.flagKey = :key")
    boolean hasFlag(@Param("characterId") UUID characterId, 
                    @Param("key") String key);
}
```

---

## ➡️ Продолжение

**Следующий:** [Part 3 - Controllers & API](./backend-integration-part3-api.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:49) - Part 2 (Entities & Repos)

