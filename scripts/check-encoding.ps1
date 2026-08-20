$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$utf8Strict = [System.Text.UTF8Encoding]::new($false, $true)
$allowedExtensions = @(".java", ".md", ".ps1")
$invalidFiles = [System.Collections.Generic.List[string]]::new()

$files = @(
    Get-ChildItem -LiteralPath $projectRoot -Recurse -File |
        Where-Object {
            $_.FullName -notlike "*\out\*" -and
            ($allowedExtensions -contains $_.Extension -or $_.Name -eq ".editorconfig")
        }
)

foreach ($file in $files) {
    try {
        $text = $utf8Strict.GetString([System.IO.File]::ReadAllBytes($file.FullName))
        if ($text.Contains([char]0xFFFD)) {
            $invalidFiles.Add("Replacement character found: $($file.FullName)")
        }
    } catch {
        $invalidFiles.Add("Invalid UTF-8: $($file.FullName)")
    }
}

if ($invalidFiles.Count -gt 0) {
    $invalidFiles | ForEach-Object { Write-Error $_ }
    exit 1
}

Write-Host "Encoding check passed: $($files.Count) UTF-8 files"
