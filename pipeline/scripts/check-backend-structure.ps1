param(
    [Parameter(Mandatory = $true)]
    [string]$ProjectRoot
)

if (-not (Test-Path $ProjectRoot)) {
    Write-Error "Каталог не найден: $ProjectRoot"
    exit 1
}

$controllers = Get-ChildItem -Path $ProjectRoot -Recurse -Filter "*Controller.java" | Where-Object { $_.FullName -like "*\controller\*" }
$violations = @()

foreach ($controller in $controllers) {
    $text = Get-Content -LiteralPath $controller.FullName -Raw
    if ($text -notmatch "implements\s+\w+Api") {
        $violations += "Контроллер без реализации API интерфейса: $($controller.FullName)"
    }
    if ($text -match "@(GetMapping|PostMapping|PutMapping|DeleteMapping|PatchMapping)\(") {
        $violations += "В контроллере обнаружены Spring MVC аннотации (должны наследоваться из интерфейса): $($controller.FullName)"
    }
}

if ($violations.Count -gt 0) {
    $violations | ForEach-Object { Write-Error $_ }
    exit 1
}

Write-Output "Структура backend контроллеров соответствует правилам."

