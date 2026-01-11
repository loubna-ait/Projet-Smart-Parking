@echo off
echo ========================================
echo   Execution des tests Smart Parking
echo ========================================
echo.

REM Compiler les tests
echo [1/2] Compilation des tests...
call mvnw.cmd test-compile -q
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Echec de la compilation
    pause
    exit /b 1
)

echo [2/2] Execution de MainTest...
echo.

REM Recuperer le classpath
call mvnw.cmd dependency:build-classpath -Dmdep.outputFile=cp.txt -q

REM Lire le classpath
set /p CP=<cp.txt

REM Executer MainTest
java -cp "target/classes;target/test-classes;%CP%" ma.emsi.tests.MainTest

REM Nettoyer
del cp.txt

echo.
echo ========================================
pause
