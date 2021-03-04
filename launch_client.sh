#usr/bin/bash

set -o allexport
[[ -f .env ]] && source .env
set +o allexport
${JAVA_PATH} --module-path ${JAVA_FX_LIB_PATH} --add-modules javafx.controls,javafx.fxml -Djava.library.path=${JAVA_FX_LIB_PATH} -Dfile.encoding=UTF-8 -classpath out/production/Client:${JAVA_FX_LIB_PATH}/src.zip:${JAVA_FX_LIB_PATH}/javafx-swt.jar:${JAVA_FX_LIB_PATH}/javafx.web.jar:${JAVA_FX_LIB_PATH}/javafx.base.jar:${JAVA_FX_LIB_PATH}/javafx.fxml.jar:${JAVA_FX_LIB_PATH}/javafx.media.jar:${JAVA_FX_LIB_PATH}/javafx.swing.jar:${JAVA_FX_LIB_PATH}/javafx.controls.jar:${JAVA_FX_LIB_PATH}/javafx.graphics.jar Apps.MainApp