& chcp.com 65001 | Out-Null
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new()
$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot

& (Join-Path $PSScriptRoot "maven.ps1") -q -DskipTests compile
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

& java `
    "-Dfile.encoding=UTF-8" `
    "-Dstdout.encoding=UTF-8" `
    "-Dstderr.encoding=UTF-8" `
    -cp (Join-Path $projectRoot "target\classes") `
    smartfactory.oop.OopDemo
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
