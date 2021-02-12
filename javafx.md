# Installation de JavaFX pour compilation avec IntelliJ

### 1. Télécharger le SDK de JavaFX
JavaFX est disponible à [cette adresse](https://gluonhq.com/products/javafx/). La version utilisée pour ce projet est la version 15.0.1. 
Au besoin, voici les liens de téléchargement de la version [Windows](https://download2.gluonhq.com/openjfx/15.0.1/openjfx-15.0.1_windows-x64_bin-sdk.zip), [Mac](https://download2.gluonhq.com/openjfx/15.0.1/openjfx-15.0.1_osx-x64_bin-sdk.zip) et [Linux](https://download2.gluonhq.com/openjfx/15.0.1/openjfx-15.0.1_linux-x64_bin-sdk.zip).
Une fois le SDK téléchargé, il est nécessaire de le décompresser (à un endroit facile d'accès).

### 2. Vérifier que le plugin JavaFX pour IntelliJ est installé
Pour accèder à la liste des plugins installés depuis un projet, il suffit de taper la combinaison `Ctrl+Alt+S`, puis de séléctionner Plugins depuis le menu. Les plugins sont également accessibles depuis la fenêtre d'acceuil d'IntelliJ. Passer sur l'onglet "Installés", et taper dans la barre de recherche "JavaFX". Si aucun plugin ne s'affiche, revenir sur l'onglet marketplace et installer le plugin "JavaFX".

### 3. Ajouter la bibliothèque JavaFX au projet
Ouvrir les paramètres du projet en appuyant sur les touches `Ctrl+Alt+Maj+S`. Ouvrir la section bibliothèques, cliquer sur le symbole `+`, séléctionner Java, puis trouver le dossier décompressé de JavaFX (étape 1) et séléctionner le dossier `lib`. Séléctionner les modules dans lequels importer la bibliothèque, et cliquer sur `OK` puis sur `Appliquer` et encore une fois sur `OK`.

### 4. Modifier les profils Run/Debug du projet
Cliquer sur "Modifier les configurations", à gauche du bouton d'execution :  
![Bouton d'édition des configurations](https://i.imgur.com/FP5iPKO.png)  
Pour chaque profil, effectuer la combinaison de touches `Alt+V`, puis dans le champ "Options de VM" qui vient d'apparaitre, saisir la configuration suivante : 
```
--module-path </path/to/javafx/sdk> --add-modules javafx.controls,javafx.fxml
```
Remplacer `</path/to/javafx/sdk>` par le chemin d'accès du dossier `lib` précédemment séléctionné à l'étape 3. Cliquer sur `Appliquer` puis `OK`. Le projet est prêt à être compilé.