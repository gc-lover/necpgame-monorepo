# Show Stats

Show statistics for all agents and enterprise-grade domains.

## Steps

1. Search Project items with Status field:
   ```javascript
   mcp_github_list_project_items({
     owner_type: 'user',
     owner: 'gc-lover',
     project_number: 1,
     query: '' // можно добавить фильтр, например: Agent:"Backend" OR Status:"In Progress"
   });
   ```
   **Note:** Для сбора статистики по всем задачам используй пустой query или фильтр по конкретным статусам. Не используй
   `is:issue` — `list_project_items` работает только с issues. Не указывай `fields` - вернутся все поля.

2. Group by Status и Agent, count: total, in_progress, todo, review, blocked, returned, done

3. Show table with progress percentage

## Enterprise-Grade Domains Statistics

**For comprehensive project health check, also run:**

```bash
# Show enterprise-grade domain statistics
python scripts/validate-domains-openapi.py --stats

# Show service generation statistics
python scripts/generate-all-domains-go.py --stats-only

# Show overall enterprise architecture health
python scripts/validate-backend-optimizations.sh --summary
```

**Group by Status values, not labels; Agent берите из поля Agent.**
