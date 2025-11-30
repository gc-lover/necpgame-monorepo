# GitHub Project Configuration

**Единый источник параметров проекта для всех агентов**

## Project Parameters

Все агенты используют эти параметры для работы с GitHub Project через MCP:

- **Owner Type:** `user`
- **Owner:** `gc-lover`
- **Project Number:** `1`
- **Project Node ID:** `PVT_kwHODCWAw84BIyie`
- **Status Field ID:** `239690516`
- **Repository:** `gc-lover/necpgame-monorepo`

## Usage in Commands

В командах агентов использовать эти значения:

```javascript
await mcp_github_list_project_items({
  owner_type: 'user',        // из этого конфига
  owner: 'gc-lover',         // из этого конфига
  project_number: 1,         // из этого конфига
  query: 'is:issue Status:"{Agent} - Todo" OR Status:"{Agent} - In Progress"',
  fields: ['Status', 'Title']
});
```

**Важно:** Если параметры проекта изменятся, обновить их здесь и во всех командах агентов.

## Field IDs

- **Status Field ID:** `239690516`
- **Status Field Node ID:** `PVTSSF_lAHODCWAw84BIyiezg5JYxQ`

## Project Details

- **Project Name:** NECPGAME Development
- **Project Node ID:** `PVT_kwHODCWAw84BIyie`
- **Project Number:** 1
- **Owner:** gc-lover

