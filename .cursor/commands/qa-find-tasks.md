# QA Agent: Find Tasks Command

## Command
```
/qa-find-tasks
```

## Description
Finds all available QA testing tasks.

## Usage
Execute this command to see what needs testing (functional, performance, integration).

## Task Types
- **Functional Testing**: API endpoints, game mechanics
- **Performance Testing**: Load testing, benchmarks
- **Integration Testing**: End-to-end workflows
- **Content Testing**: Imported quests, NPCs, dialogues

## Implementation
```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"QA" Status:"Todo"'
});
```

## Response Format
Returns testing tasks with priority and complexity.

## Next Steps
Use `/qa-validate-result #issue` after completing tests.