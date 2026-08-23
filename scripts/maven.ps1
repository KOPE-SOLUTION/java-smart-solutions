param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]] $MavenArguments
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$wrapper = Join-Path $projectRoot "mvnw.cmd"

if (Test-Path -LiteralPath $wrapper) {
    & $wrapper @MavenArguments
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    & mvn @MavenArguments
} else {
    throw "Maven was not found. Keep mvnw.cmd in the repository root."
}

if ($LASTEXITCODE -ne 0) {
    throw "Maven failed with exit code $LASTEXITCODE."
}
