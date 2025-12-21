# ACTIVATE GIT PROTECTION

## MAXIMUM SECURITY SETUP

To prevent agents from accidentally destroying project progress, activate ALL protection layers:

### 1. Git Hooks (File/Commit Level)
```bash
git config core.hooksPath .githooks
```

### 2. Terminal Protection (Command Level)
Activate in EVERY terminal session:

**Linux/Mac:**
```bash
source .githooks/activate-terminal-safety.sh
```

**Windows:**
```cmd
call .githooks\activate-terminal-safety.bat
```

### 3. Verify Protection
Test that dangerous commands are blocked:
```bash
git reset HEAD~1  # Should be BLOCKED (ANY reset)
git clean -n      # Should be BLOCKED (ANY clean)
git --version     # Should work (safe command)
```

## PROTECTION LEVELS

1. **Agent Rules** - forbids dangerous commands in code
2. **Git Hooks** - blocks commits containing dangerous commands
3. **Terminal Function** - intercepts ALL git commands in session

## EMERGENCY DISABLE (ADMINISTRATORS ONLY)

If you MUST use dangerous commands:
```bash
# Temporarily disable hooks
git config --unset core.hooksPath

# Temporarily disable terminal protection
unset -f git  # Linux/Mac
# Or close terminal and open new one

# Do dangerous operation
git reset --hard HEAD~1  # EXAMPLE - USE CAREFULLY!

# Restore protection
git config core.hooksPath .githooks
source .githooks/activate-terminal-safety.sh
```

## REMEMBER: PREVENTION IS BETTER THAN RECOVERY

One wrong command can destroy weeks of work. Always activate protection!
