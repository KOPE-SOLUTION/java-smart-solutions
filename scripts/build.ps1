$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$outputDirectory = Join-Path $projectRoot "out"
$sourceFiles = @(
    Get-ChildItem -Path (Join-Path $projectRoot "src\main\java") -Recurse -Filter "*.java"
    Get-ChildItem -Path (Join-Path $projectRoot "src\test\java") -Recurse -Filter "*.java"
)

New-Item -ItemType Directory -Force -Path $outputDirectory | Out-Null
& javac -encoding UTF-8 -d $outputDirectory $sourceFiles.FullName
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
Write-Host "Build completed: $outputDirectory"

