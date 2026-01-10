# Backend Agent: Find Tasks Command

## Command
```
/backend-find-tasks
```

## Description
Finds all available Backend agent tasks in the GitHub Project that are ready to be worked on.

## Usage
Execute this command when you want to see what Backend tasks are available for work.

## Implementation
```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"Backend" Status:"Todo"'
});
```

## Response Format
Returns list of project items with:
- Item ID
- Issue number
- Title
- Description
- Current status and agent

## Next Steps
After finding tasks, use `/backend-validate-result #issue_number` to check if the task is still valid.