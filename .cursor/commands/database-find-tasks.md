# Database Agent: Find Tasks Command

## Command
```
/database-find-tasks
```

## Description
Finds all available Database agent tasks in the GitHub Project.

## Usage
Execute this command to see what database-related tasks are available for work.

## Task Types
- **Schema Design**: Creating new tables, indexes, constraints
- **Migration Creation**: Writing Liquibase migration scripts
- **Performance Optimization**: Index optimization, query tuning
- **Content Import**: Applying data migrations for quests/NPCs

## Implementation
```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"DB" Status:"Todo"'
});
```

## Response Format
Returns tasks categorized by type:
- Schema/Migration tasks
- Performance optimization tasks
- Content migration tasks

## Next Steps
Use appropriate validation commands based on task type:
- Schema tasks: `/database-validate-result #123`
- Performance: `/database-refactor-schema {table}`
- Content: `/database-apply-content-migration`