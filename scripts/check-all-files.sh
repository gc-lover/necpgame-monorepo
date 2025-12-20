#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

source "$PROJECT_ROOT/.github/file-size-config.sh"

REPORT_FILE="$PROJECT_ROOT/file-size-report.md"

echo "🔍 Checking ALL files in repository..."
echo "Max lines per file: $MAX_LINES"
echo ""

FAILED_FILES=()
PASSED_FILES=()
EXCLUDED_FILES=()
TOTAL_CHECKED=0

while IFS= read -r -d '' file; do
  rel_file="${file#$PROJECT_ROOT/}"
  
  if ! check_file_extension "$rel_file"; then
    continue
  fi
  
  # Skip bundled/generated files
  if [[ "$rel_file" =~ bundled\.yaml$ ]] || [[ "$rel_file" =~ oas_.*\.go$ ]] || [[ "$rel_file" =~ .*changelog.*\.yaml$ ]] || [[ "$rel_file" =~ readiness-tracker\.yaml$ ]] || [[ "$rel_file" =~ docker-compose\.yml$ ]] || [[ "$rel_file" =~ _gen\.go$ ]] || [[ "$rel_file" =~ \.pb\.go$ ]]; then
    EXCLUDED_FILES+=("$rel_file")
    echo "⏭️  Excluded: $rel_file"
    continue
  fi

  if is_excluded "$rel_file"; then
    EXCLUDED_FILES+=("$rel_file")
    echo "⏭️  Excluded: $rel_file"
    continue
  fi
  
  LINES=$(wc -l < "$file" 2>/dev/null || echo "0")
  TOTAL_CHECKED=$((TOTAL_CHECKED + 1))
  
  if [ "$LINES" -gt "$MAX_LINES" ]; then
    FAILED_FILES+=("$rel_file:$LINES")
    echo "❌ $rel_file has $LINES lines (exceeds by $((LINES - MAX_LINES)) lines)"
  else
    PASSED_FILES+=("$rel_file:$LINES")
    echo "OK $rel_file has $LINES lines (OK)"
  fi
done < <(find "$PROJECT_ROOT" -type f \
  \( -name "*.md" -o -name "*.mdx" -o \
     -name "*.yaml" -o -name "*.yml" -o \
     -name "*.json" -o -name "*.proto" -o \
     -name "*.go" -o -name "*.cpp" -o \
     -name "*.h" -o -name "*.hpp" -o \
     -name "*.java" -o -name "*.kt" -o \
     -name "*.ts" -o -name "*.tsx" -o \
     -name "*.js" -o -name "*.jsx" -o \
     -name "*.py" -o -name "*.rs" -o \
     -name "*.sql" -o -name "*.sh" -o \
     -name "*.bat" -o -name "*.ps1" -o \
     -name "*.c" -o -name "*.cs" \) \
  -not -path "*/\.git/*" \
  -not -path "*/node_modules/*" \
  -not -path "*/vendor/*" \
  -not -path "*/target/*" \
  -not -path "*/build/*" \
  -not -path "*/dist/*" \
  -print0)

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 Summary:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Total checked: $TOTAL_CHECKED files"
echo "Passed: ${#PASSED_FILES[@]} files"
echo "Failed: ${#FAILED_FILES[@]} files"
echo "Excluded: ${#EXCLUDED_FILES[@]} files"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

echo ""
echo "📝 Generating report: $REPORT_FILE"

cat > "$REPORT_FILE" << EOF
# File Size Check Report

**Generated:** $(date)  
**Max lines per file:** $MAX_LINES  
**Total files checked:** $TOTAL_CHECKED

## Summary

- OK **Passed:** ${#PASSED_FILES[@]} files
- ❌ **Failed:** ${#FAILED_FILES[@]} files
- ⏭️  **Excluded:** ${#EXCLUDED_FILES[@]} files

---

EOF

if [ ${#FAILED_FILES[@]} -gt 0 ]; then
  cat >> "$REPORT_FILE" << EOF
## ❌ Files Exceeding Limit

These files need to be split into smaller files:

| File | Lines | Exceeds By | Priority |
|------|-------|------------|----------|
EOF

  for entry in "${FAILED_FILES[@]}"; do
    IFS=':' read -r file lines <<< "$entry"
    exceeds=$((lines - MAX_LINES))
    
    if [ "$exceeds" -gt 500 ]; then
      priority="🔴 Critical"
    elif [ "$exceeds" -gt 300 ]; then
      priority="🟠 High"
    else
      priority="🟡 Medium"
    fi
    
    echo "| \`$file\` | $lines | $exceeds | $priority |" >> "$REPORT_FILE"
  done
  
  cat >> "$REPORT_FILE" << EOF

### Recommendations

1. **Critical files (>1100 lines):** Split immediately into 3-4 files
2. **High priority (>800 lines):** Split into 2-3 files
3. **Medium priority (>600 lines):** Split into 2 files

### How to Split Files

**Go files:**
- Separate handlers by resource/domain
- Extract service layer logic
- Move models to separate files
- Split large repositories by entity

**Example:**
\`\`\`bash
# Instead of:
handlers.go (1000 lines)

# Split into:
handlers_users.go (300 lines)
handlers_orders.go (300 lines)
handlers_products.go (300 lines)
handlers_common.go (100 lines)
\`\`\`

---

EOF
else
  echo "OK **All files pass the size check!**" >> "$REPORT_FILE"
  echo "" >> "$REPORT_FILE"
fi

if [ ${#EXCLUDED_FILES[@]} -gt 0 ]; then
  cat >> "$REPORT_FILE" << EOF
## ⏭️ Excluded Files

These files are excluded from checks (generated code, vendor, etc.):

<details>
<summary>Show excluded files (${#EXCLUDED_FILES[@]} files)</summary>

EOF

  for file in "${EXCLUDED_FILES[@]}"; do
    echo "- \`$file\`" >> "$REPORT_FILE"
  done
  
  echo "</details>" >> "$REPORT_FILE"
  echo "" >> "$REPORT_FILE"
fi

cat >> "$REPORT_FILE" << EOF
---

## Configuration

Settings from \`.github/file-size-config.sh\`:

- **Max lines:** $MAX_LINES
- **Checked extensions:** ${FILE_EXTENSIONS[*]}
- **Excluded patterns:** ${EXCLUDED_PATTERNS[*]}

To change the limit, edit \`.github/file-size-config.sh\`
EOF

echo ""
echo "OK Report generated: $REPORT_FILE"
echo ""

if [ ${#FAILED_FILES[@]} -gt 0 ]; then
  echo "❌ Found ${#FAILED_FILES[@]} files exceeding the limit"
  echo "📄 See $REPORT_FILE for details"
  exit 1
else
  echo "OK All files pass the size check!"
  exit 0
fi

