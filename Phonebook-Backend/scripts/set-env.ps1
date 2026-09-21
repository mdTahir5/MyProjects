# ------------------------------------------------------------------
# Loads backend/.env into the current PowerShell session.
# Usage:  .\scripts\set-env.ps1      (note the leading dot = dot-source)
# ------------------------------------------------------------------
$ErrorActionPreference = 'Stop'

$envFile = Join-Path $PSScriptRoot '..\.env'
if (-not (Test-Path $envFile)) {
    Write-Warning "No .env found at $envFile - falling back to .env.example"
    $envFile = Join-Path $PSScriptRoot '..\.env.example'
}

Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line -eq '' -or $line.StartsWith('#')) { return }
    $idx = $line.IndexOf('=')
    if ($idx -lt 1) { return }
    $name = $line.Substring(0, $idx).Trim()
    $value = $line.Substring($idx + 1).Trim()
    [System.Environment]::SetEnvironmentVariable($name, $value, 'Process')
}

Write-Host "Environment loaded from $envFile" -ForegroundColor Green
