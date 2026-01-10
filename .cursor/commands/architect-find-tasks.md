# Architect Agent: Find Tasks Command

## Command
```
/architect-find-tasks
```

## Description
Finds all available Architect agent tasks for system architecture design.

## Usage
Execute this command to see what architecture tasks need to be designed.

## Task Types
- **System Architecture**: Overall system design, microservices
- **Domain Design**: Enterprise-grade domain boundaries
- **Performance Architecture**: Scaling, load balancing, data models
- **Integration Design**: API contracts, data synchronization

## Implementation
```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"Architect" Status:"Todo"'
});
```

## Response Format
Returns architecture tasks with complexity and priority indicators.

## Next Steps
Use `/architect-validate-result #issue` to check task readiness.