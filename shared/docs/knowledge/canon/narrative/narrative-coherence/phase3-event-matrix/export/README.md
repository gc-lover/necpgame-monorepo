# YAML → JSON Export Scripts

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:31

---

## Краткое описание

Скрипты для конвертации YAML файлов графа событий в JSON для backend integration.

---

## Доступные скрипты

### Python (рекомендуется)

```bash
# Install dependencies
pip install pyyaml

# Run converter
python convert-quest-graph.py
```

### Node.js

```bash
# Install dependencies
npm install js-yaml

# Run converter
node convert-quest-graph.js
```

---

## Что конвертируется

**Input files (YAML):**
1. `phase2-narrative/connections/side-quests-matrix.yaml`
2. `phase2-narrative/connections/quest-triggers.yaml`
3. `phase2-narrative/connections/quest-blockers.yaml`
4. `phase3-event-matrix/graph/quest-dependencies.yaml`

**Output files (JSON):**
1. `export/side-quests-matrix.json`
2. `export/quest-triggers.json`
3. `export/quest-blockers.json`
4. `export/quest-dependencies-full.json`

---

## Формат output JSON

### Quest Graph Example

```json
{
  "metadata": {
    "version": "1.0.0",
    "total_quests": 550
  },
  "nodes": [
    {
      "id": "MQ-2020-001",
      "name": "Первые шаги",
      "era": "2020-2030",
      "type": "main"
    }
  ],
  "edges": [
    {
      "from": "MQ-2020-001",
      "to": "MQ-2020-002",
      "type": "unlocks",
      "timing": "immediate"
    }
  ],
  "statistics": {
    "total_nodes": 550,
    "total_edges": 1200
  }
}
```

---

## Backend Integration

### Java Spring Boot

```java
@Service
public class QuestGraphService {
    private QuestGraph graph;
    
    @PostConstruct
    public void init() {
        // Load JSON
        ObjectMapper mapper = new ObjectMapper();
        graph = mapper.readValue(
            new File("quest-dependencies-full.json"),
            QuestGraph.class
        );
    }
    
    public List<Quest> getAvailableQuests(UUID characterId) {
        // Use graph to determine available quests
        return graph.getNodes().stream()
            .filter(q -> isAvailable(q, characterId))
            .collect(Collectors.toList());
    }
}
```

---

## Проверка output

После конвертации проверьте:

```bash
# Check file sizes
ls -lh export/

# Validate JSON
cat export/quest-dependencies-full.json | jq '.statistics'

# Count nodes
cat export/quest-dependencies-full.json | jq '.nodes | length'

# Count edges
cat export/quest-dependencies-full.json | jq '.edges | length'
```

Expected output:
```
Nodes: 550+
Edges: 1200+
Files: 4 JSON files
Total size: ~500KB-1MB
```

---

## История изменений

- v1.0.0 (2025-11-07 00:31) - Export scripts created

