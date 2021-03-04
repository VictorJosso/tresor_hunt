#usr/bin/bash

set -o allexport
[[ -f .env ]] && source .env
set +o allexport
${JAVA_PATH} -Dfile.encoding=UTF-8 -classpath out/production/Serveur TresorHuntServer