@echo off
setlocal
cd /d "%~dp0\.."

where java >nul 2>nul
if errorlevel 1 (
  echo Java was not found on this computer.
  echo Open Visual Studio Code, run java -version, and share the result with Codex.
  pause
  exit /b 1
)

echo Starting ServicePlus...
start "ServicePlus Server" cmd /k java --source 17 --add-modules jdk.httpserver backend\src\com\serviceplus\Main.java
timeout /t 3 /nobreak >nul
start "" http://localhost:8080

echo ServicePlus should now be open in your browser.
echo Keep the ServicePlus Server window open while using the application.
endlocal
