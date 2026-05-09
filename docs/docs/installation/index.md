# Installation

Suivez ces étapes pour installer le projet sur votre machine locale pour le développement.

---

## Application desktop

### Téléchargement direct

La version compilée de l'application est disponible en téléchargement direct :

[https://github.com/matmathmat/framelab-desktop/releases/latest/](https://github.com/matmathmat/framelab-desktop/releases/latest/)

---

### Construire sa propre version

Si vous souhaitez faire tourner l'application en local (par exemple pour la connecter à votre propre instance de FrameLab Web), vous pouvez construire vous-même l'application.

#### Prérequis

Avant de commencer, vous devez disposer d'une instance de FrameLab Web (frontend + backend) fonctionnelle.

Pour mettre en place FrameLab Web, suivez le guide d'installation disponible ici :
[https://matmathmat.github.io/framelab-front/docs/doc.html?section=installation](https://matmathmat.github.io/framelab-front/docs/doc.html?section=installation)

Vous aurez également besoin de :

* [IntelliJ IDEA](https://www.jetbrains.com/idea/) (Community ou Ultimate)
* [Java 21](https://adoptium.net/)
* [Maven](https://maven.apache.org/)
* Git

---

#### Clonage du dépôt

```bash
git clone https://github.com/matmathmat/framelab-desktop.git
```

---

#### Configuration du fichier `service.txt`

Une fois le projet cloné, ouvrez le fichier suivant :

```
FrameLabDesktop\src\main\resources\fr\framelab\service.txt
```

Modifiez-le pour y renseigner les URLs de votre instance FrameLab Web :

```
apiUrl=http://localhost:3000
frontUrl=http://localhost:5173
```

| Clé        | Description                               |
| ---------- | ----------------------------------------- |
| `apiUrl`   | URL du backend Express de FrameLab Web    |
| `frontUrl` | URL du frontend SvelteKit de FrameLab Web |

---

#### Construction et lancement

1. Ouvrez le projet dans **IntelliJ IDEA**.
2. Laissez IntelliJ importer automatiquement les dépendances Maven.
3. Cliquez sur **Run** pour lancer l'application, ou utilisez **Build > Build Project** pour compiler.

L'application se connectera automatiquement à votre instance FrameLab Web via les URLs renseignées dans `service.txt`.