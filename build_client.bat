@echo off
FOR /f "tokens=*" %%a IN (.env) DO SET id=%%a & call :processline %%a
IF "%JAVA_PATH%" == "" (
ECHO Merci de completer le fichier .env avant d'executer ce script
GOTO :eof
)
IF "%JAVAC_PATH%" == "" (
ECHO Merci de completer le fichier .env avant d'executer ce script
GOTO :eof
)
IF "%JAVA_FX_LIB_PATH%" == "" (
ECHO Merci de completer le fichier .env avant d'executer ce script
GOTO :eof
)
dir /s /b Client\src\*.java > sources.txt & %JAVAC_PATH% --module-path %JAVA_FX_LIB_PATH% --add-modules javafx.controls,javafx.fxml -d out/production/Client @sources.txt & del sources.txt & copy Client\src\views\*.fxml out\production\Client\views\ & copy Client\src\assets\* out\production\Client\
GOTO :eof
:processline
IF %id:~0,1% NEQ # (SET "%id:~0,-1%")
GOTO :eof
:eof
