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
%JAVA_PATH% --module-path %JAVA_FX_LIB_PATH% --add-modules javafx.controls,javafx.media,javafx.fxml -Dfile.encoding=UTF-8 -classpath %CD%\out\production\Client;%JAVA_FX_LIB_PATH%\src.zip;%JAVA_FX_LIB_PATH%\javafx-swt.jar;%JAVA_FX_LIB_PATH%\javafx.web.jar;%JAVA_FX_LIB_PATH%\javafx.base.jar;%JAVA_FX_LIB_PATH%\javafx.fxml.jar;%JAVA_FX_LIB_PATH%\javafx.media.jar;%JAVA_FX_LIB_PATH%\javafx.swing.jar;%JAVA_FX_LIB_PATH%\javafx.controls.jar;%JAVA_FX_LIB_PATH%\javafx.graphics.jar Apps.MainApp
GOTO :eof
:processline
IF %id:~0,1% NEQ # (SET "%id:~0,-1%")
GOTO :eof
:eof
