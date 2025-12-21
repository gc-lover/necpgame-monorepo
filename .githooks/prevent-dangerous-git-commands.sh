#!/bin/bash
# Git Hook: Prevent Dangerous Git Commands for AI Agents
# Purpose: Block destructive git operations that can break history

# Colors
RED='\033[0;31m'
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# Get the current git command being executed
COMMAND="$@"

# List of dangerous commands that are FORBIDDEN for AI agents
DANGEROUS_PATTERNS=(
    "reset --hard"
    "reset HEAD~"
    "reset --soft"
    "reset --mixed"
    "push --force"
    "push -f"
    "rebase -i"
    "rebase --interactive"
    "filter-branch"
    "reflog delete"
    "reflog expire"
    "commit --amend"
    "commit --no-verify"
    "commit -n"
    "revert --no-commit"
    "cherry-pick --abort"
    "clean -fd"
    "clean -fdx"
)

# Check if command contains dangerous patterns
for pattern in "${DANGEROUS_PATTERNS[@]}"; do
    if echo "$COMMAND" | grep -q "$pattern"; then
        echo ""
        echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo -e "${RED}[GIT_HOOK_ERROR] DANGEROUS_GIT_COMMAND_BLOCKED${NC}"
        echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo ""
        echo -e "${YELLOW}BLOCKED COMMAND:${NC} git $COMMAND"
        echo ""
        echo -e "${RED}REASON:${NC} This command can destroy git history or cause data loss"
        echo ""
        echo -e "${YELLOW}DANGEROUS PATTERN DETECTED:${NC} '$pattern'"
        echo ""
        echo -e "${GREEN}ALLOWED OPERATIONS FOR AI AGENTS:${NC}"
        echo "  [OK] git add <file>"
        echo "  [OK] git commit -m \"message\" (with ALL quality checks)"
        echo "  [OK] git push"
        echo "  [OK] git status"
        echo "  [OK] git diff"
        echo "  [OK] git log"
        echo "  [OK] git branch"
        echo "  [OK] git checkout <branch>"
        echo "  [OK] git pull"
        echo ""
        echo -e "${RED}FORBIDDEN OPERATIONS:${NC}"
        echo "  [ERROR] git reset --hard (destroys uncommitted changes)"
        echo "  [ERROR] git reset HEAD~ (rewrites history)"
        echo "  [ERROR] git push --force (overwrites remote history)"
        echo "  [ERROR] git rebase (rewrites history)"
        echo "  [ERROR] git commit --amend (rewrites history)"
        echo "  [ERROR] git commit --no-verify (bypasses quality checks - FORBIDDEN)"
        echo "  [ERROR] git commit -n (bypasses quality checks - FORBIDDEN)"
        echo "  [ERROR] git filter-branch (mass history rewrite)"
        echo "  [ERROR] git clean -fd (deletes untracked files)"
        echo ""
        echo -e "${YELLOW}FOR AI AGENTS:${NC}"
        echo "  [WARNING] CRITICAL: You MUST NEVER bypass quality checks!"
        echo "  [WARNING] FORBIDDEN: 'git commit --no-verify' or 'git commit -n'"
        echo "  [WARNING] REQUIRED: All commits must pass architecture validation"
        echo ""
        echo "  Quality checks ensure:"
        echo "  - Code follows SOLID principles"
        echo "  - No security vulnerabilities"
        echo "  - Performance requirements met"
        echo "  - Files are within size limits"
        echo ""
        echo "  If validation fails:"
        echo "  - Fix the reported issues"
        echo "  - Do NOT use --no-verify to bypass"
        echo "  - Create a proper commit that passes all checks"
        echo ""
        echo "  If you made a mistake:"
        echo "  - Create a new commit that fixes it (git revert)"
        echo "  - Do NOT try to rewrite history"
        echo "  - Do NOT use destructive commands"
        echo ""
        echo "  If you need to undo changes:"
        echo "  - Use 'git revert <commit>' instead of reset"
        echo "  - Create a new commit instead of amending"
        echo ""
        echo -e "${RED}POLICY:${NC} AI agents MUST preserve git history AND code quality at all times"
        echo ""
        echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo ""
        
        exit 1
    fi
done

# Command is safe, allow it
exit 0

