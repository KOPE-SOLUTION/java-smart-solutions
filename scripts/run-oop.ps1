$projectRoot = Split-Path -Parent $PSScriptRoot
& chcp.com 65001 | Out-Null
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new()
& (Join-Path $PSScriptRoot "build.ps1")
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& java "-Dfile.encoding=UTF-8" -cp (Join-Path $projectRoot "out") smartfactory.oop.OopDemo
