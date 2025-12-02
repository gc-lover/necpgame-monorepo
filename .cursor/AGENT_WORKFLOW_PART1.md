# Agent Workflow Guide - Part 1: Core Concepts & System Agents

## Quick Start

**See:** `.cursor/AGENT_WORKFLOW_QUICK.md` for quick reference

## Universal Algorithm

1. **Find:** `Status:"{MyAgent} - Todo"`
2. **WARNING СРАЗУ update:** `{MyAgent} - In Progress`
3. **Work:** Create files with `Issue: #123`, commit with `[agent]`
4. **WARNING Handoff:** Update to `{NextAgent} - Todo` + comment

---

## Idea Writer

**In:** `Idea Writer - Todo`

**Flow:**
1. Search: `Status:"Idea Writer - Todo"`
2. Update: `Idea Writer - In Progress` (`d9960d37`)
3. Work: Create concept, lore, quest concepts
4. Handoff based on type:
   - UI task: `UI/UX - Todo` (`49689997`)
   - Content quest: `Content Writer - Todo` (`c62b60d3`)
   - System: `Architect - Todo` (`799d8a69`)

---

## Architect

**In:** `Architect - Todo`

**Flow:**
1. Search: `Status:"Architect - Todo"`
2. Update: `Architect - In Progress` (`02b1119e`)
3. Work: Design architecture, define components
4. Handoff: `Database - Todo` (`58644d24`)

---

## Database Engineer

**In:** `Database - Todo`

**Flow:**
1. Search: `Status:"Database - Todo"`
2. Update: `Database - In Progress` (`91d49623`)
3. Work: Design DB schema, create Liquibase migrations
4. Handoff: `API Designer - Todo` (`3eddfee3`)

---

## API Designer

**In:** `API Designer - Todo`

**Flow:**
1. Search: `Status:"API Designer - Todo"`
2. Update: `API Designer - In Progress` (`ff20e8f2`)
3. Work: Create OpenAPI spec, define endpoints/schemas
4. Handoff: `Backend - Todo` (`72d37d44`)

---

## Backend Developer

**In:** `Backend - Todo`

**Flow:**
1. Search: `Status:"Backend - Todo"`
2. Update: `Backend - In Progress` (`7bc9d20f`)
3. Work: Generate code (oapi-codegen), implement handlers, tests
   - **Content quest:** Import YAML to DB via `/api/v1/gameplay/quests/content/reload`
4. Handoff:
   - Content: `QA - Todo` (`86ca422e`) after DB import
   - System: `Network - Todo` (`944246f3`)

---

## Network Engineer

**In:** `Network - Todo`

**Flow:**
1. Search: `Status:"Network - Todo"`
2. Update: `Network - In Progress` (`88b75a08`)
3. Work: Configure Envoy, optimize protocols, implement tickrate
4. Handoff: `Security - Todo` (`3212ee50`)

---

## Security Engineer

**In:** `Security - Todo`

**Flow:**
1. Search: `Status:"Security - Todo"`
2. Update: `Security - In Progress` (`187ede76`)
3. Work: Security audit, validate inputs, integrate anti-cheat
4. Handoff: `DevOps - Todo` (`ea62d00f`)

---

## DevOps Engineer

**In:** `DevOps - Todo`

**Flow:**
1. Search: `Status:"DevOps - Todo"`
2. Update: `DevOps - In Progress` (`f5a718a4`)
3. Work: Create Docker images, K8s manifests, setup observability
4. Handoff: `UE5 - Todo` (`fa5905fb`)

---

## Technical Implementation

**Constants:**
```javascript
const PROJECT_CONFIG = {
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  status_field_id: 239690516
};
```

**Update status:**
```javascript
mcp_github_update_project_item({
  owner_type: PROJECT_CONFIG.owner_type,
  owner: PROJECT_CONFIG.owner,
  project_number: PROJECT_CONFIG.project_number,
  item_id: project_item_id,  // from list_project_items
  updated_field: {
    id: 239690516,
    value: STATUS_OPTIONS['{Agent} - {State}']  // from GITHUB_PROJECT_CONFIG.md
  }
});
```

**Add comment:**
```javascript
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,  // NOT item_id!
  body: `OK {Work} ready. Handed off to {NextAgent}\n\nIssue: #${issue_number}`
});
```

---

**Continue in:** `.cursor/AGENT_WORKFLOW_PART2.md`

