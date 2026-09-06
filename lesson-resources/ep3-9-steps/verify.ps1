param()
$ErrorActionPreference = "Stop"
$stepsRoot = $PSScriptRoot
$repoRoot = [IO.Path]::GetFullPath((Join-Path $stepsRoot "../.."))
$coreRoot = Join-Path $repoRoot "lesson-resources/ep3-9-oop-core"
$utf8 = New-Object System.Text.UTF8Encoding($false, $true)
function Read-Utf8([string] $Path) {
    return $utf8.GetString([IO.File]::ReadAllBytes($Path)).Replace([string][char]13, "")
}
$parts = @("00-start", "01-read", "02-add-delete", "03-maintenance")
$coreFiles = @("model/FactoryDevice.java", "model/Machine.java", "model/MachineStatus.java", "model/Maintainable.java", "model/SensorReading.java", "service/SmartFactoryService.java")
foreach ($part in $parts) {
    $project = Join-Path $stepsRoot $part
    [xml] $pom = Read-Utf8 (Join-Path $project "pom.xml")
    if ($pom.project.properties.'maven.compiler.release' -ne "21") { throw "Wrong Java version: $part" }
    foreach ($file in $coreFiles) {
        $expected = Read-Utf8 (Join-Path $coreRoot $file)
        $actual = Read-Utf8 (Join-Path $project "src/main/java/smartfactory/$file")
        if ($expected -cne $actual) { throw "Core source differs: $part/$file" }
    }
    $source = Read-Utf8 (Join-Path $project "src/main/java/smartfactory/desktop/DashboardApp.java")
    $css = Read-Utf8 (Join-Path $project "src/main/resources/smartfactory/desktop/dashboard.css")
    if ($source -notmatch 'totalLabel\.textProperty\(\)\.bind\(machineCount\.asString') { throw "Missing total binding: $part" }
    if ($part -ne "00-start" -and $source -match 'MachineRow|refreshSummary|countStatus\(') { throw "Old UI data remains: $part" }
    if ($part -ne "00-start" -and $css -notmatch '\.status-offline') { throw "Missing offline style: $part" }
    if ($part -eq "03-maintenance" -and $source -notmatch 'machineTable\.refresh\(\)') { throw "Missing table refresh" }
    Write-Output "PASS source kit: $part"
}
$lessonFiles = @("ep09-service-crud.md", "ep09a-service-table.md", "ep09b-service-add-delete.md", "ep09c-service-maintenance.md", "ep10-task-timeline.md")
$linkCount = 0
foreach ($lesson in $lessonFiles) {
    $lessonPath = Join-Path $repoRoot "docs/playlist-03-java-desktop/$lesson"
    $content = Read-Utf8 $lessonPath
    if ($content.Contains([string][char]0xfffd)) { throw "Invalid text: $lesson" }
    if ($content.Contains(('$' + '{dir}'))) { throw "Unresolved run path: $lesson" }
    foreach ($match in [regex]::Matches($content, '\]\(([^)]+)\)')) {
        $target = $match.Groups[1].Value
        if ($target -match '^(https?:|#)') { continue }
        $relative = [Uri]::UnescapeDataString(($target -split '[?#]', 2)[0])
        $resolved = [IO.Path]::GetFullPath((Join-Path (Split-Path $lessonPath) $relative))
        if (-not (Test-Path -LiteralPath $resolved)) { throw ("Broken link in " + $lesson + ": " + $target) }
        $linkCount++
    }
}
Write-Output "PASS lesson links: $linkCount"
Write-Output "Static checks only. Compile or run each Maven project separately."

