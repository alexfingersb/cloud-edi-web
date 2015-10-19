@ECHO OFF

set CURDIR=%CD%
set SERVICE_NAME=CloudEDI
set SERVICE_DISPLAY=Agent - %SERVICE_NAME%

if "%PROCESSOR_ARCHITECTURE%" == "AMD64" (
  set SERVICE_EXE=%CURDIR%\winservice\service-win64.exe
) else (
  set SERVICE_EXE=%CURDIR%\winservice\service-win32.exe
)

"%SERVICE_EXE%" //DS//"%SERVICE_NAME%"
