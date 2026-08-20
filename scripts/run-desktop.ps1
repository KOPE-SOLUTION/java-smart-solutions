$projectRoot = Split-Path -Parent $PSScriptRoot
& (Join-Path $PSScriptRoot "build.ps1")
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& java "-Dfile.encoding=UTF-8" -cp (Join-Path $projectRoot "out") smartfactory.ui.DesktopApp
