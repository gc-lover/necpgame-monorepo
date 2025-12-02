# Agent Workflow Guide - Part 2: Client, Content & QA Agents

**See Part 1:** `.cursor/AGENT_WORKFLOW_PART1.md`

---

## UI/UX Designer

**In:** `UI/UX - Todo`

**Flow:**
1. Search: `Status:"UI/UX - Todo"`
2. Update: `UI/UX - In Progress` (`dae97d56`)
3. Work: Create design docs, wireframes, UX mechanics
4. Handoff: `UE5 - Todo` (`fa5905fb`)

---

## UE5 Developer

**In:** `UE5 - Todo`

**Flow:**
1. Search: `Status:"UE5 - Todo"`
2. Update: `UE5 - In Progress` (`9396f45a`)
3. Work: Create C++ classes, implement UI, integrate with backend
4. Handoff: `QA - Todo` (`86ca422e`)

---

## Content Writer

**In:** `Content Writer - Todo`

**Flow:**
1. Search: `Status:"Content Writer - Todo"`
2. Update: `Content Writer - In Progress` (`cf5cf6bb`)
3. Work: Create quest YAML, develop lore, validate YAML
4. Handoff: `Backend - Todo` (`72d37d44`) for DB import

**Important:** Content Writer validates YAML. Backend imports to DB.

---

## QA/Testing

**In:** `QA - Todo`

**Flow:**
1. Search: `Status:"QA - Todo"`
2. Update: `QA - In Progress` (`251c89a6`)
3. Work: Create test cases, run tests, find bugs
4. Handoff:
   - **Bugs found:** `Backend - Returned` (`40f37190`) or `UE5 - Returned` (`855f4872`)
   - **OK, needs balance:** `Game Balance - Todo` (`d48c0835`)
   - **OK, no balance:** `Release - Todo` (`ef037f05`)

---

## Performance Engineer

**In:** `Performance - Todo`

**Flow:**
1. Search: `Status:"Performance - Todo"`
2. Update: `Performance - In Progress` (`1674ad2c`)
3. Work: Profile code, optimize performance, create benchmarks
4. Return: `Backend - Todo` (`72d37d44`) or `UE5 - Todo` (`fa5905fb`)

---

## Game Balance

**In:** `Game Balance - Todo`

**Flow:**
1. Search: `Status:"Game Balance - Todo"`
2. Update: `Game Balance - In Progress` (`a67748e9`)
3. Work: Balance weapons/armor/skills, tune economy, adjust difficulty
4. Handoff: `Release - Todo` (`ef037f05`)

---

## Release

**In:** `Release - Todo`

**Flow:**
1. Search: `Status:"Release - Todo"`
2. Update: `Release - In Progress` (`67671b7e`)
3. Work: Create release notes, prepare deploy, setup monitoring
4. Complete: `Done` (`98236657`) + close Issue

---

## Workflow Routes

**System tasks:**
```
Todo → Idea Writer → Architect → Database → API Designer → 
Backend → Network → Security → DevOps → UE5 → QA → 
Game Balance → Release → Done
```

**Content quests:**
```
Todo → Idea Writer → Content Writer → Backend (import) → 
QA → Release → Done
```

**UI/UX tasks:**
```
Todo → Idea Writer → UI/UX Designer → UE5 → QA → 
Release → Done
```

---

## Checklist

### On task start:
- [ ] Find via `mcp_github_list_project_items`
- [ ] **WARNING Update status to In Progress**
- [ ] Read Issue completely
- [ ] Check input data

### During work:
- [ ] Do work per agent responsibility
- [ ] Add `Issue: #{number}` to all files
- [ ] Commit with `[agent]` prefix
- [ ] Check acceptance criteria

### On handoff:
- [ ] **WARNING Update status to next agent**
- [ ] **WARNING Add comment with work description**
- [ ] Include Issue number in comment
- [ ] Include PR if applicable

---

**See also:**
- `.cursor/AGENT_WORKFLOW_QUICK.md` - quick reference table
- `.cursor/GITHUB_PROJECT_CONFIG.md` - status IDs
- `.cursor/HANDOFF_COMMENT_TEMPLATES.md` - comment templates

