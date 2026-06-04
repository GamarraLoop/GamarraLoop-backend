# Cargar variables del archivo .env
if (Test-Path .env) {
    Get-Content .env | Where-Object { $_ -match '=' -and $_ -notmatch '^#' } | ForEach-Object {
        $name, $value = $_.Split('=', 2)
        [Environment]::SetEnvironmentVariable($name.Trim(), $value.Trim())
    }
    Write-Host "Variables de entorno cargadas desde .env" -ForegroundColor Green
} else {
    Write-Host "No se encontro el archivo .env" -ForegroundColor Yellow
}

# Ejecutar Spring Boot
Write-Host "Iniciando el servidor de GamarraLoop..." -ForegroundColor Cyan
.\mvnw.cmd spring-boot:run
