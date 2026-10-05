[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [ValidateSet('3.10-1', '3.10-2', '3.10-3A', '3.10-3B', '3.10-4', '3.11-1', '3.11-2', '3.11-3', '3.11-4', '3.11-5', '3.11-3-summary', '3.11-4-actions', '3.12', '3.12-1', '3.12-2', '3.12-3', '3.13', '3.13-1', '3.13-2', '3.14-1', '3.14-2', '3.14-3', '3.14-4', '3.15-1', '3.15-2', '3.15-3', '3.15-4', '3.16', '3.16-1', '3.16-2')]
    [string]$Episode,
    [switch]$BackupExisting
)

$ErrorActionPreference = 'Stop'
$repoRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..')).TrimEnd('\')
$starts = @{
    '3.10-1'  = 'lesson-resources/ep3-9-steps/03-maintenance'
    '3.10-2'  = 'lesson-resources/ep3-10-steps/01-sensor-button'
    '3.10-3A' = 'lesson-resources/ep3-10-steps/02-task-thread'
    '3.10-3B' = 'lesson-resources/ep3-10-steps/03a-sensor-result'
    '3.10-4'  = 'lesson-resources/ep3-10-steps/03b-sensor-safety'

    '3.11-1' = 'lesson-resources/ep3-10-steps/04-auto-sensor'
    '3.11-2' = 'lesson-resources/ep3-11-steps/01-window'
    '3.11-3' = 'lesson-resources/ep3-11-steps/02-button'
    '3.11-4' = 'lesson-resources/ep3-11-steps/04-summary'
    '3.11-5' = 'lesson-resources/ep3-11-steps/06-actions'
    '3.11-3-summary' = 'lesson-resources/ep3-11-steps/03-table'
    '3.11-4-actions' = 'lesson-resources/ep3-11-steps/05-add'
    '3.12' = 'lesson-resources/ep3-11-steps/07-sensor'
    '3.12-1' = 'lesson-resources/ep3-11-steps/07-sensor'
    '3.12-2' = 'lesson-resources/ep3-12-16-steps/12a-tests'
    '3.12-3' = 'lesson-resources/ep3-12-16-steps/12b-module'
    '3.13' = 'lesson-resources/ep3-12-16-steps/12b-module'
    '3.13-1' = 'lesson-resources/ep3-12-16-steps/12b-module'
    '3.13-2' = 'lesson-resources/ep3-12-16-steps/13a-search-id'
    '3.14-1' = 'lesson-resources/ep3-12-16-steps/13b-search-all'
    '3.14-2' = 'lesson-resources/ep3-12-16-steps/14a-status'
    '3.14-3' = 'lesson-resources/ep3-12-16-steps/14b-maintenance'
    '3.14-4' = 'lesson-resources/ep3-12-16-steps/14c-clear'
    '3.15-1' = 'lesson-resources/ep3-12-16-steps/14d-sort'
    '3.15-2' = 'lesson-resources/ep3-12-16-steps/15a-core-edit'
    '3.15-3' = 'lesson-resources/ep3-12-16-steps/15b-select-cancel'
    '3.15-4' = 'lesson-resources/ep3-12-16-steps/15c-save'
    '3.16' = 'lesson-resources/ep3-12-16-steps/15d-refresh'
    '3.16-1' = 'lesson-resources/ep3-12-16-steps/15d-refresh'
    '3.16-2' = 'lesson-resources/ep3-12-16-steps/16a-spacing'
}
$lessons = @{
    '3.10-1'  = 'ep10a-sensor-button.md'
    '3.10-2'  = 'ep10b-task-thread.md'
    '3.10-3A' = 'ep10c-sensor-task-result.md'
    '3.10-3B' = 'ep10c2-sensor-task-safety.md'
    '3.10-4'  = 'ep10d-auto-sensor-timeline.md'

    '3.11-1' = 'ep11a-fxml-window.md'
    '3.11-2' = 'ep11b-fxml-controller.md'
    '3.11-3' = 'ep11c-service-table-summary.md'
    '3.11-4' = 'ep11d-form-crud.md'
    '3.11-5' = 'ep11e-sensor-lifecycle.md'
    '3.11-3-summary' = 'ep11c-service-table-summary.md'
    '3.11-4-actions' = 'ep11d-form-crud.md'
    '3.12' = 'ep12-thai-package-iot.md'
    '3.12-1' = 'ep12-thai-package-iot.md'
    '3.12-2' = 'ep12-thai-package-iot.md'
    '3.12-3' = 'ep12-thai-package-iot.md'
    '3.13' = 'ep13-search-filter.md'
    '3.13-1' = 'ep13-search-filter.md'
    '3.13-2' = 'ep13-search-filter.md'
    '3.14-1' = 'ep14-steps/01-status.md'
    '3.14-2' = 'ep14-steps/02-maintenance.md'
    '3.14-3' = 'ep14-steps/03-clear.md'
    '3.14-4' = 'ep14-steps/04-sort.md'
    '3.15-1' = 'ep15-steps/01-core.md'
    '3.15-2' = 'ep15-steps/02-select.md'
    '3.15-3' = 'ep15-steps/03-save.md'
    '3.15-4' = 'ep15-steps/04-refresh.md'
    '3.16' = 'ep16-scene-builder-optional.md'
    '3.16-1' = 'ep16-scene-builder-optional.md'
    '3.16-2' = 'ep16-scene-builder-optional.md'
}

function Get-RepoPath([string]$RelativePath) {
    $full = [IO.Path]::GetFullPath((Join-Path $repoRoot $RelativePath))
    if (-not $full.StartsWith($repoRoot + '\', [StringComparison]::OrdinalIgnoreCase)) {
        throw "Path must stay inside this repository: $full"
    }
    return $full
}

function Assert-NoReparsePath([string]$Path) {
    $cursor = $Path
    while ($cursor -and ($cursor -eq $repoRoot -or
            $cursor.StartsWith($repoRoot + '\', [StringComparison]::OrdinalIgnoreCase))) {
        if (Test-Path -LiteralPath $cursor) {
            $item = Get-Item -LiteralPath $cursor -Force
            if (($item.Attributes -band [IO.FileAttributes]::ReparsePoint) -ne 0) {
                throw "Symbolic links and junctions are not supported here: $cursor"
            }
        }
        if ($cursor -eq $repoRoot) { break }
        $cursor = [IO.Path]::GetDirectoryName($cursor)
    }
}

function Assert-PlainTree([string]$Path) {
    $item = Get-Item -LiteralPath $Path -Force
    if (($item.Attributes -band [IO.FileAttributes]::ReparsePoint) -ne 0) {
        throw "Symbolic links and junctions are not supported here: $Path"
    }
    if ($item.PSIsContainer) {
        foreach ($child in Get-ChildItem -LiteralPath $Path -Force) {
            Assert-PlainTree $child.FullName
        }
    }
}

$source = Get-RepoPath $starts[$Episode]
$practiceRoot = Get-RepoPath 'practice'
$destination = Get-RepoPath 'practice/smart-factory-dashboard'
Assert-NoReparsePath $source
Assert-NoReparsePath $destination
foreach ($required in @('pom.xml', 'src/main/java/smartfactory/desktop/DashboardApp.java',
        'src/main/resources/smartfactory/desktop/dashboard.css')) {
    if (-not (Test-Path -LiteralPath (Join-Path $source $required) -PathType Leaf)) {
        throw "Starter file is missing: $source/$required"
    }
}
if ($Episode -notlike '3.10-*' -and $Episode -ne '3.11-1') {
    foreach ($required in @('src/main/java/smartfactory/ui/DesktopApp.java',
            'src/main/resources/smartfactory/ui/dashboard-view.fxml')) {
        if (-not (Test-Path -LiteralPath (Join-Path $source $required) -PathType Leaf)) {
            throw "FXML starter file is missing: $source/$required"
        }
    }
}
Assert-PlainTree $source

if (Test-Path -LiteralPath $destination) {
    if (-not (Test-Path -LiteralPath $destination -PathType Container)) {
        throw "The destination is not a directory: $destination"
    }
    if (-not $BackupExisting) {
        throw 'Existing practice was not changed. Close the app, save your files, then add -BackupExisting to keep the old project and prepare this episode.'
    }
    Assert-PlainTree $destination
}

$stamp = (Get-Date -Format 'yyyyMMdd-HHmmss') + '-' + [Guid]::NewGuid().ToString('N').Substring(0, 8)
$staging = Get-RepoPath ('practice/.prepare-' + $stamp)
$backupRoot = Get-RepoPath 'practice/_backups'
$backup = Get-RepoPath ('practice/_backups/smart-factory-dashboard-' + $stamp)
Assert-NoReparsePath $staging
Assert-NoReparsePath $backupRoot
if ((Test-Path -LiteralPath $staging) -or (Test-Path -LiteralPath $backup)) {
    throw 'A temporary or backup path already exists. Run the command again.'
}

$movedExisting = $false
try {
    [IO.Directory]::CreateDirectory($practiceRoot) | Out-Null
    [IO.Directory]::CreateDirectory($staging) | Out-Null
    Copy-Item -LiteralPath (Join-Path $source 'pom.xml') -Destination (Join-Path $staging 'pom.xml')
    Copy-Item -LiteralPath (Join-Path $source 'src') -Destination (Join-Path $staging 'src') -Recurse
    $lesson = 'docs/playlist-03-java-desktop/' + $lessons[$Episode]
    @(
        '# Lesson start',
        '',
        "Prepared for: EP $Episode",
        "Source: $($starts[$Episode])",
        "Lesson: $lesson",
        "Prepared at: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')",
        '',
        'This records the starting point, not your current progress.',
        'Keep learning in this folder. Preparing another start does not merge your edits.'
    ) | Set-Content -LiteralPath (Join-Path $staging 'LESSON-START.md') -Encoding UTF8

    # Recheck immediately before moving anything. No recursive delete is used.
    Assert-NoReparsePath $destination
    Assert-NoReparsePath $backupRoot
    if (Test-Path -LiteralPath $destination) {
        if (-not $BackupExisting) { throw 'Practice appeared during preparation; no files were replaced.' }
        Assert-PlainTree $destination
        [IO.Directory]::CreateDirectory($backupRoot) | Out-Null
        Move-Item -LiteralPath $destination -Destination $backup
        $movedExisting = $true
    }
    if (Test-Path -LiteralPath $destination) { throw 'Destination is occupied; prepared files were not installed.' }
    Move-Item -LiteralPath $staging -Destination $destination
} catch {
    if ($movedExisting -and -not (Test-Path -LiteralPath $destination)) {
        Move-Item -LiteralPath $backup -Destination $destination
        Write-Warning 'Preparation failed. The original practice project was restored.'
    }
    if (Test-Path -LiteralPath $staging) {
        Write-Warning "Prepared temporary files were retained at: $staging"
    }
    throw
}

Write-Host "Ready to start EP $Episode"
Write-Host "Project: $destination"
Write-Host "Lesson: $(Join-Path $repoRoot $lesson)"
if ($movedExisting) { Write-Host "Previous project: $backup" }
Write-Host 'Run from the repository root: .\mvnw.cmd -f .\practice\smart-factory-dashboard\pom.xml javafx:run'
