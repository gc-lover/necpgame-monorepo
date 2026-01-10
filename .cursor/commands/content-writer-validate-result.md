# Content Writer Agent: Validate Result Command

## Command
```
/content-writer-validate-result #123
```

## Description
Validates that content YAML files are properly structured and ready for import.

## Usage
Execute this command after creating quest/NPC/dialogue YAML files to ensure they meet requirements.

## Validation Checks

### YAML Structure
- [ ] Valid YAML syntax
- [ ] Required fields present (metadata, content)
- [ ] Proper nesting and references

### Content Quality
- [ ] Lore consistency with existing canon
- [ ] Dialogue branching logic
- [ ] Quest objectives clarity
- [ ] NPC interactions completeness

### File Size
- [ ] No file exceeds 1000 lines
- [ ] Content split appropriately

## Implementation
```bash
# Validate YAML syntax
yamllint knowledge/canon/lore/.../quest-*.yaml

# Check structure
python scripts/validation/validate-all-quests.py

# Content validation
python scripts/validation/validate-quest-yaml.py {file_path}
```

## Response Format
```
[CONTENT VALIDATION] Checking YAML files...

✅ YAML syntax valid for all files
✅ Required fields present
✅ Lore consistency verified
✅ File sizes within limits (<1000 lines)
⚠️  Minor: Some dialogue branches could be expanded

[RESULT] Content validation PASSED
Ready for Backend import
```

## Next Steps
- Fix any issues found
- Re-run validation
- Handoff to Backend when all checks pass