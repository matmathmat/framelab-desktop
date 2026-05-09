# Guide d'utilisation

Bienvenue dans le guide d'utilisation de l'application desktop FrameLab.

---

## Compatibilité

L'application fonctionne sur tout système supportant **Java 21** et **JavaFX 21**.

---

## Avant de commencer

Si vous avez téléchargé directement l'application, vous devez d'abord créer un compte sur la plateforme FrameLab Web :

[https://framelab.mathmonportfolio.be/](https://framelab.mathmonportfolio.be/)

![Accueil connecté](./../images/preview/desktop_logged.png)

Si vous faites tourner votre propre instance de FrameLab Web et FrameLab Desktop en local, utilisez directement le compte créé sur votre instance.

---

## Connexion

Au lancement de l'application, vous arrivez sur l'écran de connexion.

![Connexion](./../images/preview/login.png)

Renseignez l'adresse e-mail et le mot de passe de votre compte, puis cliquez sur **Se connecter**.

Une fois connecté, vos informations de session sont sauvegardées de façon sécurisée. Vous n'aurez pas à vous reconnecter à chaque ouverture de l'application.

### Mode invité

Si vous ne possédez pas encore de compte, vous pouvez vous connecter en tant qu'invité. Ce mode vous permet de tester librement l'éditeur, mais vous ne pourrez pas soumettre de participation à un challenge.

---

## Menu d'accueil

![Accueil](./../images/preview/home.png)

Une fois connecté, vous arrivez sur le menu d'accueil, organisé en plusieurs zones.

**À gauche** se trouvent trois boutons de navigation :

* le bouton **Accueil** pour revenir à cette page ;
* le bouton **Challenges** pour consulter le challenge en cours ;
* le bouton **Se déconnecter** pour fermer la session.

**Au centre en haut** est affiché le challenge en cours. Cliquez dessus pour créer un nouveau projet de retouche associé à ce challenge.

**Au centre en bas** se trouve l'entraînement du jour. Chaque jour, une image aléatoire est mise à disposition accompagnée d'une série de défis correspondant à des opérations à appliquer à l'image dans un ordre précis. Si vous réussissez à reproduire toutes les opérations attendues, vous gagnez des points. Le nombre de points attribués diminue selon le nombre de tentatives. C'est un excellent moyen de prendre en main l'éditeur avant de se lancer dans un challenge.

**À droite** se trouve l'historique de vos projets en cours. Cliquez sur un projet pour le reprendre.

### Démarrer un projet

Lorsque vous cliquez sur le challenge en cours ou sur un entraînement, l'application vous invite à saisir un titre pour votre projet avant d'ouvrir l'éditeur.

---

## Éditeur de retouche

![Éditeur](./../images/preview/editor.png)

L'éditeur est organisé en plusieurs zones.

### Barre d'outils — en haut à gauche

Trois menus déroulants donnent accès aux opérations d'image rapides :

* **Amélioration** — luminosité, contraste, saturation, teinte ;
* **Filtre** — noir et blanc, sépia, négatif, flou, accentuation ;
* **Transformation** — rotation, symétrie, redimensionnement, recadrage.

Ces opérations s'appliquent immédiatement sur le calque actif.

### Actions — en haut à droite

* **Sauvegarder** — enregistre l'état actuel du projet en base locale ;
* **Envoyer** — soumet votre image au challenge (visible uniquement si vous participez à un challenge et que vous n'avez pas encore soumis de participation) ;
* **Voir ma participation** — ouvre votre participation dans le navigateur (visible si vous avez déjà soumis) ;
* **Valider entraînement** — disponible en mode entraînement uniquement, déclenche la vérification des opérations effectuées.

### Zone centrale

L'espace central affiche côte à côte :

* **L'image de référence** — l'image originale du challenge, non modifiable ;
* **L'image en cours de modification** — c'est ici que toutes vos retouches apparaissent en temps réel.

### Outils de dessin — à gauche

Trois outils sont disponibles pour intervenir directement sur l'image :

* **Crayon** — dessin main libre ;
* **Emoji** — sélection puis placement d'un emoji sur l'image, redimensionnable avec la molette de la souris ;
* **Gomme** — efface les tracés crayon et les emojis posés.

> Ces outils ne fonctionnent que sur un **calque transparent**. Si le calque actif est un calque image, les outils de dessin sont inactifs.

### Barre d'actions — en bas à gauche

* **Réinitialiser** — annule toutes les modifications du calque actif et revient à son état d'origine ;
* **Historique** — permet de revenir en arrière sur les actions effectuées sur le calque actif. L'historique conserve les **30 dernières actions**. Attention : la fusion de calques n'est pas réversible via l'historique ;
* **Objectifs** — visible en mode entraînement uniquement, affiche la liste des opérations à réaliser dans l'ordre.

### Affichage de référence — en bas au centre

Le bouton **Masquer / Afficher l'image de référence** permet de cacher le panneau gauche pour disposer de plus d'espace sur l'image en cours de modification.

### Zoom — en bas à droite

* Le **pourcentage de zoom** affiché indique le niveau de zoom actuel ;
* Les boutons **Zoomer** et **Dézoomer** permettent d'ajuster l'affichage.

### Panneau des calques — à droite

Le panneau des calques liste tous les calques du projet dans leur ordre d'empilement.

Deux types de calques existent :

* **Calque transparent** — calque vide sur lequel il est possible de dessiner, d'écrire ou de poser des emojis ;
* **Calque image** — calque contenant une copie de l'image de référence, sur lequel on peut appliquer des filtres et des transformations.

Pour chaque calque, vous pouvez :

* l'**afficher ou le masquer** via la case à cocher ;
* modifier son **opacité** avec le curseur ;
* **monter ou descendre** son ordre dans la pile avec les boutons fléchés ;
* le **supprimer** (un projet doit conserver au minimum un calque).

En bas du panneau, trois boutons permettent :

* d'**ajouter un calque transparent** ;
* d'**ajouter un calque image** ;
* de **fusionner les calques visibles** en un seul calque.

> La fusion est une opération irréversible : une fois les calques fusionnés, il n'est pas possible de les séparer à nouveau ni d'annuler cette action via l'historique.