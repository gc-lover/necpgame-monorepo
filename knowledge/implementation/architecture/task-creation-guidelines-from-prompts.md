<!-- Issue: #1876 -->

# Task Creation Guidelines: From Prompts to Actionable Tasks

## Overview

This document provides architectural guidelines for transforming prompts from `prompt.md` into actionable tasks using the MCP workflow. It aligns with `AGENT_SIMPLE_GUIDE.md` and `CONTENT_WORKFLOW.md` to ensure consistent task creation and execution.

## Architecture Context

### Universal Agent Model

The NECPGAME project uses a **Universal Agent Architecture** where each agent can perform 14 specialized roles:

```
Universal Agent
├── 14 Specialized Roles
│   ├── Idea Writer → Architect → API Designer → Backend
│   ├── Content Writer → Backend (import) → QA
│   ├── UI/UX → UE5 → QA
│   └── Performance/Security/Network/DevOps/QA/Release
└── Autonomous Execution
    ├── MCP Workflow Integration
    ├── GitHub Projects Management
    └── File Placement Rules
```

### Workflow Architecture

**System Workflow (Technical):**
```
Idea → Architect → DB → API → Backend → Network → Security → DevOps → UE5 → QA → Release
```

**Content Workflow (Creative):**
```
Idea → Content Writer → Backend (import) → QA → Release
```

## Task Creation Framework

### 1. Prompt Analysis Phase

#### Input: Raw Prompt
- **Source:** `prompt.md` (universal agent definition)
- **Contains:** 14 agent roles, workflow pipelines, technical requirements
- **Format:** High-level mission and role definitions

#### Analysis Steps:
1. **Identify Agent Role:** Match prompt requirements to one of 14 roles
2. **Determine Workflow Type:** System vs Content vs UI workflow
3. **Extract Technical Requirements:** Performance, SOLID, security, file placement
4. **Map to MCP Operations:** GitHub Projects API calls

### 2. Task Structuring Phase

#### Task Components (from AGENT_SIMPLE_GUIDE):

```yaml
Task:
  id: "project_item_id"
  title: "[Role] Description (context)"
  body: |
    Detailed requirements aligned with agent rules
    Performance requirements if applicable
    File placement specifications
  status: "Todo" | "In Progress" | "Review" | "Blocked" | "Returned" | "Done"
  agent: "Architect" | "Backend" | "Content" | "UI/UX" | etc.
  labels: ["performance", "security", "content", "system"]
```

#### Status Flow Architecture:
```
Todo → In Progress → Todo (next agent) → ... → Done
                    ↓
               Blocked (if dependencies)
                    ↓
               Todo (after resolution)
```

### 3. MCP Workflow Integration

#### GitHub Projects Operations:

**Task Discovery:**
```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"{Agent}" Status:"Todo"'
});
```

**Task Assignment:**
```javascript
// Status: Todo → In Progress
updated_field: { id: 239690516, value: '83d488e7' }

// Agent assignment
updated_field: { id: 243899542, value: '{agent_id}' }
```

**Task Completion:**
```javascript
// Status: In Progress → Todo (next agent)
updated_field: [
  { id: 239690516, value: 'f75ad846' }, // Todo
  { id: 243899542, value: '{next_agent_id}' } // Next agent
]
```

### 4. Agent Role Mapping

#### From Prompt.md Roles to MCP Agents:

| Prompt Role | MCP Agent | Field Value | Use Case |
|-------------|-----------|-------------|----------|
| Idea Writer | Idea | `8c3f5f11` | Concept creation |
| Architect | Architect | `d109c7f9` | System design |
| API Designer | API | `6aa5d9af` | OpenAPI specs |
| Backend | Backend | `1fc13998` | Go services |
| Database | DB | `1e745162` | SQL schemas |
| Content Writer | Content | `d3cae8d8` | YAML content |
| UI/UX Designer | UI/UX | `98c65039` | Interface design |
| UE5 Developer | UE5 | `56920475` | C++ Unreal |
| QA | QA | `3352c488` | Testing |
| Performance | Performance | `d16ede50` | Optimization |
| Security | Security | `12586c50` | Security audit |
| Network | Network | `c60ebab1` | Protocols |
| DevOps | DevOps | `7e67a39b` | Infrastructure |
| Release | Release | `f5878f68` | Deployment |

## Content vs System Task Differentiation

### Content Tasks (CONTENT_WORKFLOW.md)

**Pattern:** Content creation with import
**Workflow:** Content Writer → Backend (import) → QA
**Import Methods:**
- **API Import:** `POST /api/v1/gameplay/quests/content/reload`
- **SQL Migration:** Generate `V*__data_*.sql` files
- **Decision Logic:** Backend agent chooses method based on volume

**Validation:** QA checks via API, not labels

### System Tasks (Technical Pipeline)

**Pattern:** Technical implementation
**Workflow:** Full pipeline from Idea to Release
**Focus:** Performance, scalability, reliability
**Validation:** Automated checks, performance benchmarks

## Implementation Guidelines

### 1. Task Creation Rules

#### Title Format:
```
[{Role}] {Action}: {Description}
Examples:
- [Architect] Design: Player inventory system
- [Backend] Implement: Quest API endpoints
- [Content] Create: Night City district lore
```

#### Body Requirements:
- **Clear Objective:** What needs to be done
- **Technical Context:** Performance requirements, dependencies
- **File Locations:** Specific paths according to placement rules
- **Success Criteria:** How to validate completion
- **Next Agent:** Who should receive the task

### 2. Agent Autonomy Rules

#### Decision Making:
- **Autonomous:** Agents work independently using rules
- **No Approvals:** Execute based on prompt.md guidelines
- **Self-Validation:** Check own work before handoff

#### Communication:
- **Comments Only:** Brief status updates
- **Format:** `[OK] {Result}. Handed off to {NextAgent}. Issue: #{number}`
- **No Reports:** Focus on execution, not documentation

### 3. File Placement Architecture

#### Directory Structure (from prompt.md):
```
knowledge/          # Content (YAML)
├── canon/         # Lore, quests, NPCs
├── content/       # Game assets
└── implementation/# Technical docs

services/          # Go microservices
├── {service}-go/
└── generated code

proto/openapi/     # API specifications
infrastructure/     # DB migrations, K8s
k8s/              # Kubernetes manifests
client/UE5/       # Unreal Engine code
scripts/          # Automation tools
```

#### Critical Rules:
- **Root Clean:** Only `README.md`, `CHANGELOG.md`, config files
- **Type-Based:** Each file type in designated directory
- **No Temporary Files:** All files serve permanent purpose

## Performance Integration

### Optimization Requirements (PERFORMANCE_ENFORCEMENT.md)

#### Blocker Level (Cannot Proceed):
- No context timeouts
- No DB connection pools
- Goroutine leaks
- No structured logging
- No health/metrics endpoints

#### Validation Command:
```bash
/backend-validate-optimizations #123
```

#### Workflow Integration:
- **Backend Agent:** Must pass validation before handoff
- **Architect Role:** Define performance targets in architecture
- **All Agents:** Consider performance implications

## Quality Assurance

### Validation Points:

1. **Task Clarity:** Can agent understand requirements?
2. **Workflow Alignment:** Correct next agent assignment?
3. **File Placement:** Correct directory structure?
4. **Performance Ready:** Optimization requirements included?
5. **MCP Compatible:** Uses correct API calls and IDs?

### Common Issues:

- **Wrong Agent Assignment:** Content task assigned to System agent
- **Missing Dependencies:** Task created without required prerequisites
- **Incorrect Status Flow:** Skipping required workflow steps
- **File Placement Violations:** Files in wrong directories

## Implementation Examples

### Example 1: System Task Creation

**Input Prompt:** "Create player inventory system with real-time updates"

**Output Tasks:**
1. **Architect:** Design inventory architecture
2. **DB:** Create inventory tables
3. **API:** Design inventory endpoints
4. **Backend:** Implement Go service
5. **Network:** Add WebSocket support
6. **QA:** Test integration

### Example 2: Content Task Creation

**Input Prompt:** "Create Night City district lore"

**Output Tasks:**
1. **Content Writer:** Create YAML lore files
2. **Backend:** Import via API or SQL
3. **QA:** Validate via API endpoints

## Conclusion

This guidelines document provides the architectural framework for transforming high-level prompts from `prompt.md` into concrete, actionable tasks that integrate seamlessly with the MCP workflow. By following these guidelines, agents can maintain consistency, ensure proper workflow progression, and deliver high-quality results aligned with the project's technical and creative requirements.

**Key Success Factors:**
- Strict adherence to workflow pipelines
- Proper agent role assignment
- File placement discipline
- Performance-first mentality
- Autonomous execution with minimal communication