@ECHO OFF

set CURDIR=%CD%
set SERVICE_NAME=CloudEDI
set SERVICE_DISPLAY=Agent - %SERVICE_NAME%

if "%PROCESSOR_ARCHITECTURE%" == "AMD64" (
  set SERVICE_EXE=%CURDIR%\winservice\service-win64.exe
) else (
  set SERVICE_EXE=%CURDIR%\winservice\service-win32.exe
)

set SERVICE_CONFIG=%CURDIR%\winservice\service-config.exe
set CLASSPATH=%CURDIR%\agent.jar


setlocal

if exist "%CURDIR%\jre" (
  set JAVA_HOME=%CURDIR%
)

if "%JAVA_HOME%" == "" goto noJavaHome


rem Procurando pela jvm.dll 'server'
set JVM=%JAVA_HOME%\jre\bin\server\jvm.dll

if exist "%JVM%" goto foundJvm
rem Procurando pela jvm.dll 'client'
set JVM=%JAVA_HOME%\jre\bin\client\jvm.dll


:noJavaHome

if exist "%JVM%" goto foundJvm

@echo ...
@echo Warning: A jvm.dll nao foi encontrada nem no 'server' e nem no 'client'.
@echo          A variavel de ambiente JAVA_HOME nao esta definida
@echo          Ela e necessaria para a execucao deste aplicativo
@echo          O servico ira tentar encontrar a instalacao do java no registro do sistema (windows registry)
@echo ...


set JVM=auto

:foundJvm

@echo Usando JVM: "%JVM%"


"%SERVICE_EXE%" //IS//"%SERVICE_NAME%"^
 --Install="%SERVICE_EXE%"^
 --Description="%SERVICE_DISPLAY%"^
 --Type interactive^
 --Jvm="%JVM%"^
 --Classpath="%CLASSPATH%"^
 --StartMode=jvm^
 --StartClass=br.com.it3.agent.Agent^
 --StartPath="%CURDIR%"^
 --StopMode=jvm^
 --StopClass=br.com.it3.agent.Agent^
 --LogPath="%CURDIR%\logs"^
 --StdOutput=auto^
 --StdError=auto^
 --JvmMs 256^
 --JvmMx 256

if not errorlevel 1 goto installed
@echo Erro: Falha ao instalar o servico "%SERVICE_NAME%"
goto end

:installed
@echo O servico "%SERVICE_NAME%" foi instalado com sucesso.

@echo Iniciando o servico
"%SERVICE_EXE%" //ES//"%SERVICE_NAME%"

:end
