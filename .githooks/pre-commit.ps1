# NECPGAME Pre-commit Hook (PowerShell)
# Runs architecture validation before allowing commits

Write-Host "🔍 Running NECPGAME Architecture Validation..." -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

# Check for emergency override
$committerName = git config user.name
$committerEmail = git config user.email

if ($committerName -eq "AI_AGENT_EMERGENCY" -and $committerEmail -eq "emergency@necpgame.invalid") {
    Write-Host "WARNING: Emergency commit override detected. Proceeding without validation." -ForegroundColor Yellow
    Write-Host "WARNING: This should only be used in critical situations by authorized personnel." -ForegroundColor Yellow
    exit 0
}

# Check for emoji and special characters first
Write-Host "🔍 Running Emoji Ban Check..." -ForegroundColor Cyan
if (Test-Path "scripts/validate-emoji-ban.bat") {
    try {
        $stagedFiles = git diff --cached --name-only
        if ($stagedFiles) {
            $emojiCheck = & cmd /c "scripts\validate-emoji-ban.bat $stagedFiles" 2>&1
            $emojiExitCode = $LASTEXITCODE
            Write-Host $emojiCheck
            if ($emojiExitCode -ne 0) {
                Write-Host ""
                Write-Host "🚨 COMMIT BLOCKED: Emoji/special character violation detected!" -ForegroundColor Red
                Write-Host "Please remove all emoji and special Unicode characters from your code." -ForegroundColor Red
                Write-Host ""
                Write-Host "Common fixes:" -ForegroundColor Yellow
                Write-Host "• Replace 😀 with :smile:" -ForegroundColor Yellow
                Write-Host "• Replace 🚫 with [FORBIDDEN]" -ForegroundColor Yellow
                Write-Host "• Remove decorative symbols ★ ♦ ♠ ♥" -ForegroundColor Yellow
                Write-Host "• Use plain ASCII text in comments" -ForegroundColor Yellow
                exit 1
            }
        }
    } catch {
        Write-Host "WARNING  Could not run emoji validation" -ForegroundColor Yellow
    }
} else {
    Write-Host "WARNING  Emoji validation script not found, skipping emoji check" -ForegroundColor Yellow
}

Write-Host "OK Emoji Ban Check: No forbidden characters found" -ForegroundColor Green

# Check if validation script exists
if (-not (Test-Path "scripts/validate-architecture.sh")) {
    Write-Host "WARNING  Validation script not found, skipping validation" -ForegroundColor Yellow
    exit 0
}

# Run validation (assuming bash is available via WSL or Git Bash)
try {
    & bash scripts/validate-architecture.sh
    $exitCode = $LASTEXITCODE
} catch {
    Write-Host "WARNING  Could not run bash validation script" -ForegroundColor Yellow
    exit 0
}

# Check exit code
if ($exitCode -ne 0) {
    Write-Host ""
    Write-Host "❌ Commit blocked due to validation errors" -ForegroundColor Red
    Write-Host "Please fix the issues and try again" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "OK All validations passed - proceeding with commit" -ForegroundColor Green
exit 0