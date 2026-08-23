param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]] $MavenArguments
)

$ErrorActionPreference = "Stop"
$mavenVersion = "3.9.16"
$distributionUrl = "https://downloads.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$expectedSha512 = "ed41650d42485cfc243fad22158caf9cbb5dc408ce7a09ddb94dd42a019de929ca43065bfa450612cf12bf78b5cafa3884b96c090de326ff590448c933454af3"
$wrapperRoot = Join-Path $env:USERPROFILE ".m2\wrapper\dists\apache-maven-$mavenVersion"
$mavenHome = Join-Path $wrapperRoot "apache-maven-$mavenVersion"
$mavenCommand = Join-Path $mavenHome "bin\mvn.cmd"

if (-not (Test-Path -LiteralPath $mavenCommand)) {
    $systemTemp = [IO.Path]::GetFullPath([IO.Path]::GetTempPath())
    $workDirectory = Join-Path $systemTemp ("java-smart-solutions-maven-" + [guid]::NewGuid())
    $resolvedWorkDirectory = [IO.Path]::GetFullPath($workDirectory)
    if (-not $resolvedWorkDirectory.StartsWith($systemTemp, [StringComparison]::OrdinalIgnoreCase)) {
        throw "Temporary directory is outside the system temp directory"
    }

    try {
        New-Item -ItemType Directory -Force -Path $resolvedWorkDirectory | Out-Null
        $archive = Join-Path $resolvedWorkDirectory "apache-maven.zip"
        Write-Host "Downloading Apache Maven $mavenVersion..."
        Invoke-WebRequest -UseBasicParsing -Uri $distributionUrl -OutFile $archive

        $stream = [IO.File]::OpenRead($archive)
        try {
            $sha512 = [Security.Cryptography.SHA512]::Create()
            $actualSha512 = ([BitConverter]::ToString($sha512.ComputeHash($stream))).Replace("-", "").ToLowerInvariant()
        } finally {
            if ($null -ne $sha512) {
                $sha512.Dispose()
            }
            $stream.Dispose()
        }
        if ($actualSha512 -ne $expectedSha512) {
            throw "Apache Maven checksum verification failed"
        }

        Expand-Archive -LiteralPath $archive -DestinationPath $resolvedWorkDirectory -Force
        New-Item -ItemType Directory -Force -Path $wrapperRoot | Out-Null
        Move-Item -LiteralPath (Join-Path $resolvedWorkDirectory "apache-maven-$mavenVersion") `
                -Destination $mavenHome
    } finally {
        if (Test-Path -LiteralPath $resolvedWorkDirectory) {
            Remove-Item -LiteralPath $resolvedWorkDirectory -Recurse -Force
        }
    }
}

& $mavenCommand @MavenArguments
exit $LASTEXITCODE
