# Quick Workflow Reference

## Universal Algorithm

1. **Find task:** `Status:"{MyAgent} - Todo"`
2. **WARNING СРАЗУ update:** `{MyAgent} - In Progress`
3. **Do work:** Create files (with `Issue: #123`), commit with `[agent]` prefix
4. **WARNING Handoff:** Update to `{NextAgent} - Todo` + add comment

## Status Values (from GITHUB_PROJECT_CONFIG.md)

| Agent | In Progress | Next Agent | Value |
|-------|------------|------------|-------|
| Idea Writer | `d9960d37` | Architect/Content/UI | `799d8a69`/`c62b60d3`/`49689997` |
| Architect | `02b1119e` | Database | `58644d24` |
| Database | `91d49623` | API Designer | `3eddfee3` |
| API Designer | `ff20e8f2` | Backend | `72d37d44` |
| Backend | `7bc9d20f` | Network/QA | `944246f3`/`86ca422e` |
| Network | `88b75a08` | Security | `3212ee50` |
| Security | `187ede76` | DevOps | `ea62d00f` |
| DevOps | `f5a718a4` | UE5 | `fa5905fb` |
| UE5 | `9396f45a` | QA | `86ca422e` |
| UI/UX | `dae97d56` | UE5 | `fa5905fb` |
| Content Writer | `cf5cf6bb` | Backend | `72d37d44` |
| QA | `251c89a6` | Game Balance/Release | `d48c0835`/`ef037f05` |
| Game Balance | `a67748e9` | Release | `ef037f05` |
| Release | `67671b7e` | Done | `98236657` |

## Common Params

```javascript
owner_type: 'user'
owner: 'gc-lover'
project_number: 1
status_field_id: 239690516
```

**Full details:** `.cursor/AGENT_WORKFLOW_COMPLETE.md`

