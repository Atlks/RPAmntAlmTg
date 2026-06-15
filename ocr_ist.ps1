$ErrorActionPreference = "Stop"

$installDir = "C:\Program Files\Tesseract-OCR"
$url = "https://github.com/UB-Mannheim/tesseract/releases/latest/download/tesseract-ocr-w64-setup.exe"
$installer = "$env:TEMP\tesseract-installer.exe"

Write-Host "Downloading with curl..."
curl.exe -L $url -o $installer

Write-Host "Installing..."
Start-Process -FilePath $installer -ArgumentList "/S /D=$installDir" -Wait

Write-Host "Adding PATH..."
$envPath = [Environment]::GetEnvironmentVariable("Path","Machine")

if ($envPath -notlike "*Tesseract-OCR*") {
    [Environment]::SetEnvironmentVariable(
        "Path",
        "$envPath;$installDir",
        "Machine"
    )
}

Write-Host "Done. Check version:"
& "$installDir\tesseract.exe" -v