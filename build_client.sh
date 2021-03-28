#usr/bin/bash

set -o allexport
[[ -f .env ]] && source .env
set +o allexport

if [ -z $JAVA_PATH ] || [ -z $JAVAC_PATH ]
then echo "Merci de completer le fichier .env avant d'executer ce script".
exit 1
fi
$JAVAC_PATH --module-path $JAVA_FX_LIB_PATH --add-modules javafx.controls,javafx.media,javafx.fxml -d out/production/Client $(find Client/src/ -name "*.java") && cp Client/src/views/*.fxml out/production/Client/views/ && cp Client/src/assets/* out/production/Client/
