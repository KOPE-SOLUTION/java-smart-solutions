& chcp.com 65001 | Out-Null
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new()
& (Join-Path $PSScriptRoot "maven.ps1") -q -DskipTests compile `
    "-Dexec.mainClass=smartfactory.basic.BasicDemo" exec:java
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
