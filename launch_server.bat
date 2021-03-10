@echo off
FOR /f "tokens=*" %%a IN (.env) DO SET id=%%a & call :processline %%a
IF "%JAVA_PATH%" == "" (
ECHO Merci de completer le fichier .env avant d'executer ce script
GOTO :eof
)
IF "%JAVA_FX_LIB_PATH%" == "" (
ECHO Merci de completer le fichier .env avant d'executer ce script
GOTO :eof
)
%JAVA_PATH% -Dfile.encoding=UTF-8 -classpath %CD%\out\production\Serveur TresorHuntServer
GOTO :eof
:processline
IF %id:~0,1% NEQ # (SET "%id:~0,-1%")
GOTO :eof
:eof
