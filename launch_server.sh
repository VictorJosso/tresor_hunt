#usr/bin/bash

set -o allexport
[[ -f .env ]] && source .env
set +o allexport

if [ -z $JAVA_PATH ]
then echo "Merci de completer le fichier .env avant d'executer ce script".
exit 1
fi

${JAVA_PATH} -Dfile.encoding=UTF-8 -classpath out/production/Serveur TresorHuntServer
