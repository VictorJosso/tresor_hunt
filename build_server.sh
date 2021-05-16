#usr/bin/bash

set -o allexport
[[ -f .env ]] && source .env
set +o allexport

if [ -z $JAVA_PATH ] || [ -z $JAVAC_PATH ]
then echo "Merci de completer le fichier .env avant d'executer ce script".
exit 1
fi
$JAVAC_PATH -d out/production/Serveur $(find Serveur/src/ -name "*.java")
