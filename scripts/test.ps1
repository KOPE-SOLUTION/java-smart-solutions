& chcp.com 65001 | Out-Null
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new()
& (Join-Path $PSScriptRoot "check-encoding.ps1")
if (-not $?) { exit 1 }
& (Join-Path $PSScriptRoot "build.ps1")
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& (Join-Path $PSScriptRoot "maven.ps1") -q `
    "-Dexec.mainClass=smartfactory.SmartFactoryTest" `
    "-Dexec.classpathScope=test" `
    exec:java
exit $LASTEXITCODE
