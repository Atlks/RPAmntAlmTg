@echo off
echo Building Telegram Monitor Alarm...
cd /d C:\prj\mntAlmTg
call mvn clean package
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo.
    echo Next steps:
    echo 1. Place alarm.mp3 in project root
    echo 2. Add pattern images to patterns\ folder
    echo 3. Run: java -jar target\mntAlmTg-1.0-SNAPSHOT-jar-with-dependencies.jar
    echo.
) else (
    echo Build failed!
)
pause
