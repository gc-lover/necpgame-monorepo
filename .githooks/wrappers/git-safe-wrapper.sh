#!/bin/bash

# Git Safe Wrapper
# This script intercepts dangerous git commands and blocks them

# Dangerous command patterns to block
DANGEROUS_COMMANDS=(
    "reset"
    "clean"
    "checkout --force"
    "branch -D"
    "push --force"
    "rebase --abort"
    "stash drop"
)

# Function to check if command is dangerous
is_dangerous_command() {
    local cmd="$1"
    for dangerous in "${DANGEROUS_COMMANDS[@]}"; do
        if [[ "$cmd" == *"$dangerous"* ]]; then
            return 0  # true - is dangerous
        fi
    done
    return 1  # false - safe
}

# Function to show safe commands
show_safe_commands() {
    echo "🚨 CRITICAL SECURITY VIOLATION: DANGEROUS GIT COMMAND ATTEMPTED!"
    echo "AI AGENT SECURITY BREACH DETECTED!"
    echo ""
    echo "STRICTLY FORBIDDEN: You attempted to execute: git $@"
    echo "This command can DESTROY ENTIRE PROJECT and cause IRREVERSIBLE DATA LOSS!"
    echo ""
    echo "🚫 DO NOT ATTEMPT TO BYPASS THIS PROTECTION!"
    echo "🚫 DO NOT TRY TO USE THESE COMMANDS IN ANY WAY!"
    echo "🚫 DO NOT TRY TO EXECUTE DANGEROUS OPERATIONS!"
    echo "🚫 THIS IS A SERIOUS SECURITY VIOLATION!"
    echo ""
    echo "REQUIRED ACTION: Return task immediately with security violation note"
    echo "DO NOT proceed with any dangerous operations!"
    echo ""
    echo "ALLOWED SAFE COMMANDS ONLY:"
    echo "  git add <files>           # Stage files safely"
    echo "  git commit -m 'message'   # Commit staged changes safely"
    echo "  git push                  # Push to remote safely"
    echo "  git pull                  # Pull from remote safely"
    echo "  git checkout <branch>     # Switch branches safely"
    echo "  git merge <branch>        # Merge branches safely"
    echo "  git stash                 # Save work temporarily"
    echo "  git stash pop             # Restore saved work"
    echo ""
    echo "FORBIDDEN DESTRUCTIVE COMMANDS (NEVER USE):"
    echo "  git reset (ANY)           # Can lose work - FORBIDDEN"
    echo "  git clean (ANY)           # Can delete files - FORBIDDEN"
    echo "  git checkout --force      # Force overwrite - FORBIDDEN"
    echo "  git branch -D             # Force delete branch - FORBIDDEN"
    echo "  git push --force          # Force push - FORBIDDEN"
    echo ""
    echo "If you ABSOLUTELY need dangerous operations:"
    echo "STOP IMMEDIATELY and contact HUMAN ADMINISTRATOR!"
    echo ""
    echo "SECURITY INCIDENT LOGGED - ADMIN NOTIFICATION SENT"
}

# Main logic
if [ $# -eq 0 ]; then
    # No arguments, just run git
    exec git
fi

# Check if this is a dangerous command
command_line="$*"
if is_dangerous_command "$command_line"; then
    show_safe_commands "$@"
    exit 1
fi

# Safe command, execute normally
exec git "$@"
