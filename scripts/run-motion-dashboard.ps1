& chcp.com 65001 | Out-Null
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new()
& (Join-Path $PSScriptRoot "maven.ps1") -q javafx:run `
    "-Djavafx.mainClass=smartfactory.dashboard/smartfactory.motion.MotionDashboardApp"
exit $LASTEXITCODE
