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

# List of dangerous commands that are WARNING for AI agents (allowed with caution)
DANGEROUS_PATTERNS=(
    "filter-branch"
    "reflog delete"
    "reflog expire"
)

# Commands that are allowed for development workflow
ALLOWED_PATTERNS=(
    "reset --hard"
    "reset HEAD~"
    "reset --soft"
    "reset --mixed"
    "push --force"
    "push -f"
    "rebase -i"
    "rebase --interactive"
    "commit --amend"
    "revert --no-commit"
    "cherry-pick --abort"
    "clean -fd"
    "clean -fdx"
)

# Check if command contains dangerous patterns (block)
for pattern in "${DANGEROUS_PATTERNS[@]}"; do
    if echo "$COMMAND" | grep -q "$pattern"; then
        echo ""
        echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo -e "${RED}[GIT_HOOK_ERROR] CRITICAL_COMMAND_BLOCKED${NC}"
        echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo ""
        echo -e "${YELLOW}BLOCKED COMMAND:${NC} git $COMMAND"
        echo ""
        echo -e "${RED}REASON:${NC} This command can permanently destroy repository history"
        echo ""
        echo -e "${YELLOW}PATTERN DETECTED:${NC} '$pattern'"
        echo ""
        echo -e "${RED}COMMAND FORBIDDEN:${NC} Never use this command"
        echo ""
        echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo ""
        exit 1
    fi
done

# Check if command contains allowed risky patterns (warn but allow)
for pattern in "${ALLOWED_PATTERNS[@]}"; do
    if echo "$COMMAND" | grep -q "$pattern"; then
        echo ""
        echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo -e "${YELLOW}[GIT_HOOK_WARNING] RISKY_COMMAND_DETECTED${NC}"
        echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo ""
        echo -e "${GREEN}ALLOWED COMMAND:${NC} git $COMMAND"
        echo ""
        echo -e "${YELLOW}WARNING:${NC} This command can modify git history"
        echo ""
        echo -e "${GREEN}STATUS:${NC} Command allowed for development workflow"
        echo ""
        echo -e "${GREEN}RECOMMENDATION:${NC} Use with caution, ensure you know what you're doing"
        show_workflow_info
        echo ""
        echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo ""
        # Don't exit - allow the command
    fi
done

# Command is safe, allow it
exit 0

# Additional development workflow information (shown for risky commands)
show_workflow_info() {
    echo ""
    echo -e "${GREEN}ALLOWED OPERATIONS FOR DEVELOPMENT:${NC}"
    echo "  OK git add <file>"
    echo "  OK git commit -m \"message\""
    echo "  OK git push"
    echo "  OK git push --force (use with caution)"
    echo "  OK git reset --hard <commit> (use with caution)"
    echo "  OK git reset HEAD~ (use with caution)"
    echo "  OK git rebase -i (use with caution)"
    echo "  OK git commit --amend (use with caution)"
    echo "  OK git status"
    echo "  OK git diff"
    echo "  OK git log"
    echo "  OK git branch"
    echo "  OK git checkout <branch>"
    echo "  OK git merge <branch>"
    echo "  OK git pull"
    echo "  OK git clean -fd (use with caution)"
    echo ""
    echo -e "${RED}FORBIDDEN OPERATIONS:${NC}"
    echo "  ❌ git filter-branch (destroys history permanently)"
    echo "  ❌ git reflog delete (destroys reflog permanently)"
    echo "  ❌ git reflog expire (destroys reflog permanently)"
    echo ""
    echo -e "${YELLOW}DEVELOPMENT POLICY:${NC}"
    echo "  Commands are allowed for efficient development workflow"
    echo "  Use version control commands as needed for development"
    echo "  Be cautious with history-modifying commands"
    echo "  Always ensure you understand the impact of commands"
    echo ""
}

