
Add-Type -AssemblyName System.Drawing

$sourcePath = "C:\Users\MK Shaon\.gemini\antigravity\brain\f6849511-67bd-49c5-9db3-083f146c482a\dayline_icon_light_clean_1766395279918.png"
$baseDir = "e:\Cursor Play ground\Dayline\app\src\main\res"

$sizes = @{
    "mipmap-mdpi" = 48
    "mipmap-hdpi" = 72
    "mipmap-xhdpi" = 96
    "mipmap-xxhdpi" = 144
    "mipmap-xxxhdpi" = 192
}

if (-not (Test-Path $sourcePath)) {
    Write-Error "Source image not found: $sourcePath"
    exit 1
}

$sourceImage = [System.Drawing.Image]::FromFile($sourcePath)

foreach ($kvp in $sizes.GetEnumerator()) {
    $dirName = $kvp.Key
    $size = $kvp.Value
    $targetDir = Join-Path $baseDir $dirName

    if (-not (Test-Path $targetDir)) {
        New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
    }

    # Create resized bitmap
    $rect = New-Object System.Drawing.Rectangle 0, 0, $size, $size
    $destImage = New-Object System.Drawing.Bitmap $size, $size
    $destImage.SetResolution($sourceImage.HorizontalResolution, $sourceImage.VerticalResolution)
    $graphics = [System.Drawing.Graphics]::FromImage($destImage)
    $graphics.CompositingMode = [System.Drawing.Drawing2D.CompositingMode]::SourceCopy
    $graphics.CompositingQuality = [System.Drawing.Drawing2D.CompositingQuality]::HighQuality
    $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $graphics.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality

    $wrapMode = [System.Drawing.Imaging.ImageAttributes]::new()
    $wrapMode.SetWrapMode([System.Drawing.Drawing2D.WrapMode]::TileFlipXY)
    $graphics.DrawImage($sourceImage, $rect, 0, 0, $sourceImage.Width, $sourceImage.Height, [System.Drawing.GraphicsUnit]::Pixel, $wrapMode)

    # Save as PNG (replacing old files)
    $targetPath = Join-Path $targetDir "ic_launcher.png"
    $targetPathRound = Join-Path $targetDir "ic_launcher_round.png"
    
    $destImage.Save($targetPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $destImage.Save($targetPathRound, [System.Drawing.Imaging.ImageFormat]::Png)

    Write-Host "Created $targetPath ($size x $size)"

    # Delete old webp files if they exist
    $oldWebp = Join-Path $targetDir "ic_launcher.webp"
    $oldWebpRound = Join-Path $targetDir "ic_launcher_round.webp"
    if (Test-Path $oldWebp) { Remove-Item $oldWebp -Force }
    if (Test-Path $oldWebpRound) { Remove-Item $oldWebpRound -Force }

    $graphics.Dispose()
    $destImage.Dispose()
}

$sourceImage.Dispose()
Write-Host "Icon update complete."
