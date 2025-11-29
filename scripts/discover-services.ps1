#!/usr/bin/env pwsh
# Обнаружение всех Go сервисов в проекте

$ErrorActionPreference = "Continue"

$services = Get-ChildItem -Path "services" -Directory | Where-Object { $_.Name -match "-go$" } | ForEach-Object { $_.Name } | Sort-Object

$servicesJson = ($services | ConvertTo-Json -Compress)

Write-Output $servicesJson

