# La chasse aux trésors
Voici notre projet de PI4. Il a été conçu avec Java 15 et JavaFX 15 (la version 15 n'est plus disponible en téléchargement, mais la version 16 fonctionnera parfaitement).
Pour télécharger JavaFX, rendez-vous sur le site de [JavaFX](https://gluonhq.com/products/javafx/), ou retrouvez aux adresses suivantes la version 15 que nous avons utilisée : [Windows](https://download2.gluonhq.com/openjfx/15.0.1/openjfx-15.0.1_windows-x64_bin-sdk.zip), [Mac](https://download2.gluonhq.com/openjfx/15.0.1/openjfx-15.0.1_osx-x64_bin-sdk.zip) et [Linux](https://download2.gluonhq.com/openjfx/15.0.1/openjfx-15.0.1_linux-x64_bin-sdk.zip)

## Compilation et execution
Pour compiler et executer notre projet, nous avons fait le choix de ne pas utiliser de gestionnaire de dépendance tel que Gradle ou Maven. Nous fournissons au jury les fichiers nécessaires pour évaluer le projet.

### Paramétrage 
Il conviendra de commencer par paramétrer les scripts. Pour cela, vous trouverez un fichier `.env` à la racine du projet. Ce fichier contient trois variables : `JAVA_PATH`, `JAVA_FX_LIB_PATH` et `JAVAC_PATH`. Il faudra alors donner dans la première variable le chemin d'accès de l'éxecutable java 15, dans la deuxième le chemin d'accès du repertoire `lib` du dossier décompressé de JavaFX précédemment téléchargé et dans la dernière le chemin d'accès de l'éxecutable javac.

### Sous Windows
Nous fournissons 4 fichiers pour faire tourner notre projet sous windows :
- `build_client.bat` qui permet de compiler le client
- `launch_client.bat` qui permet de lancer le client
- `build_server.bat` qui permet de compiler le serveur
- `launch_server.bat` qui permet d'executer le serveur

### Sous Mac et Linux
Nous fournissons également 4 fichiers pour faire tourner notre projet sous Mac/Linux:
- `build_client.sh` qui permet de compiler le client
- `launch_client.sh` qui permet de lancer le client
- `build_server.sh` qui permet de compiler le serveur
- `launch_server.sh` qui permet d'executer le serveur` 

Il ne faudra pas oublier d'accorder les droits d'execution à ces fichiers (`chmod +x [file]`)

### Pare-feu
Au cas où votre installation serait munie d'un pare-feu, notre serveur écoute sur le port 7236. Nous vous proposons également de vous connecter à une instance hébergée sur un serveur de Google, dont l'adresse est `tresor.josso.fr`. C'est l'adresse par défaut que le client utilisera. 

## Configuration en jeu
Pour configurer le serveur de jeu, sur l'écran de connexion du client, vous pouvez cliquer sur le bouton "paramètres" ci dessous : 

![mainscreen](https://imgur.com/hQIrYW5.png)

Ce bouton vous ouvrira une fenêtre qui vous permettra de choisir l'adresse et le port de votre serveur : 

![paramscreen](https://imgur.com/n3mHcKf.png)

Nous souhaitons attirer votre attention sur le paramètre "Serveur amélioré". Il conviendra de laisser chochée cette case lorsque vous vous connecterez au serveur développé par nos soins, mais de la décocher si vous désirez utiliser un serveur tiers. En effet, ce paramètre indique au client qu'il peut utiliser certaines commandes spécifiques que nous avons rajouté pour enrichir les fonctionnalités de base (par exemple, définir un nombre de joueurs maximum dans une partie, demander au serveur d'ajouter des robots, mais aussi d'informer le serveur que nous avons décidé de quitter une partie avant son début)