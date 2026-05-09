const pathParts = window.location.pathname.split('/').filter(Boolean);

// Détecte automatiquement le sous-dossier GitHub Pages
const BASE_PATH =
  window.location.hostname.includes('github.io')
    ? `/${pathParts[0]}/`
    : '/';

const SITE_CONFIG = {
  // Identité du site
  title: "DocProject",
  subtitle: "FrameLab Desktop Documentation",
  tagline: "Tout ce qu'il faut savoir sur le projet de l'application desktop FrameLab.",

  // Liens header
  github_url:  "https://github.com/matmathmat/framelab-desktop",
  website_url: "https://framelab.mathmonportfolio.be/",
  
  // Sections
  sections: [
    {
      id:          "projet",
      title:       "Le Projet",
      icon:        "cube",
      description: "Présentation du projet FrameLab, de son fonctionnement, de son architecture et de ses différents composants.",
      file:        `${BASE_PATH}docs/projet/index.md`
    },
    {
      id:          "installation",
      title:       "Installation",
      icon:        "bolt",
      description: "Instructions détaillées pour déployer l'application de bureau FrameLab.",
      file:        `${BASE_PATH}docs/installation/index.md`
    },
    {
      id:          "guide",
      title:       "Guide d'utilisation",
      icon:        "book",
      description: "Manuels complets pour les utilisateurs standards et les administrateurs de la plateforme.",
      file:        `${BASE_PATH}docs/guide/index.md`
    }
  ]
};