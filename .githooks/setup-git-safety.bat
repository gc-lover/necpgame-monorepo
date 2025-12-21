@echo off
REM Git Safety Setup Script for Windows
REM This script configures git hooks and safety measures

echo Setting up Git Safety Hooks...

REM Configure git to use our custom hooks
git config core.hooksPath .githooks

REM Create symbolic links or copy hooks to .git/hooks if needed
if exist .git\hooks (
    echo Copying hooks to .git\hooks...
    copy .githooks\pre-commit .git\hooks\pre-commit 2>nul
    copy .githooks\pre-push .git\hooks\pre-push 2>nul
    copy .githooks\commit-msg .git\hooks\commit-msg 2>nul
)

REM Set up environment variable for git wrapper (optional)
REM This would require modifying PATH, which might be too intrusive
REM Instead, we'll rely on the hooks for now

echo.
echo Git Safety Setup Complete!
echo.
echo Safety features activated:
echo - Pre-commit hook: Blocks commits with dangerous command traces
echo - Pre-push hook: Additional safety check before pushing
echo - Commit-msg hook: Prevents dangerous commands in commit messages
echo.
echo To use the git wrapper (optional):
echo - Add .githooks\wrappers to your PATH
echo - Or call .githooks\wrappers\git-safe-wrapper.bat instead of git
echo.
pause
