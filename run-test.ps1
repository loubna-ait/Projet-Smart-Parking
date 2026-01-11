# Script PowerShell pour executer les tests Smart Parking
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Execution des tests Smart Parking" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Compiler les tests
Write-Host "[1/2] Compilation des tests..." -ForegroundColor Yellow
& .\mvnw.cmd test-compile -q
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERREUR: Echec de la compilation" -ForegroundColor Red
    exit 1
}

Write-Host "[2/2] Execution de MainTest..." -ForegroundColor Yellow
Write-Host ""

# Recuperer le classpath
& .\mvnw.cmd dependency:build-classpath "-Dmdep.outputFile=cp.txt" -q

# Lire le classpath
$cp = Get-Content cp.txt

# Executer MainTest
java -cp "target/classes;target/test-classes;$cp" ma.emsi.tests.MainTest

# Nettoyer
Remove-Item cp.txt

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
