@ECHO OFF

set CURDIR=%CD%
set SERVICE_NAME=agente
set SERVICE_DISPLAY=CloudEDI - %SERVICE_NAME%

if "%PROCESSOR_ARCHITECTURE%" == "AMD64" (
	set SERVICE_EXE=%CURDIR%\winservice\service-win64.exe
) else (
	set SERVICE_EXE=%CURDIR%\winservice\service-win32.exe
)

set SERVICE_CONFIG=%CURDIR%\winservice\service-config.exe
set CLASSPATH=%CURDIR%\ediclientws.jar

  
"%SERVICE_EXE%" //SS//"%SERVICE_NAME%"
"%SERVICE_EXE%" //DS//"%SERVICE_NAME%"

@echo agente desinstalado!