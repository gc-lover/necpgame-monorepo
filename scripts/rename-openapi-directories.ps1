# Скрипт для переименования директорий proto/openapi без "-service" в названии

$basePath = "C:\NECPGAME\proto\openapi"

# Директории для переименования
$directoriesToRename = @(
    "analysis",
    "arena",
    "auth-expansion",
    "companion",
    "cosmetic",
    "cyberspace",
    "cyberspace-easter-eggs",
    "economy",
    "faction",
    "guild-system",
    "integration",
    "misc",
    "progression",
    "referral",
    "social",
    "specialized",
    "system",
    "webrtc",
    "world-cities",
    "world-regions"
)

foreach ($dir in $directoriesToRename) {
    $oldPath = Join-Path $basePath $dir
    $newPath = Join-Path $basePath ($dir + "-service")

    if (Test-Path $oldPath) {
        Write-Host "Renaming $dir to $($dir + '-service')"
        Rename-Item -Path $oldPath -NewName $newPath -ErrorAction SilentlyContinue
    } else {
        Write-Host "Directory $dir not found, skipping"
    }
}

Write-Host "Directory renaming completed"










