document.addEventListener("DOMContentLoaded", () => {
  // Met à jour les infos du site depuis config.js
  document.getElementById('hero-title').textContent = SITE_CONFIG.subtitle;
  document.getElementById('hero-tagline').textContent = SITE_CONFIG.tagline;
  document.getElementById('github-link').href = SITE_CONFIG.github_url;
  document.getElementById('get-link').href = SITE_CONFIG.website_url;

  // Bibliothèque d'icônes SVG simples
  const icons = {
    book: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path></svg>',
    bolt: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"></polygon></svg>',
    code: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="16 18 22 12 16 6"></polyline><polyline points="8 6 2 12 8 18"></polyline></svg>',
    cube: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4A2 2 0 0 0 13 20l7-4A2 2 0 0 0 21 16z"></path><polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline><line x1="12" y1="22.08" x2="12" y2="12"></line></svg>'
  };

  const grid = document.getElementById('sections-grid');

  // Générer les cartes
  SITE_CONFIG.sections.forEach((section, index) => {
    // Redirection vers doc.html en passant l'id de la section dans l'URL
    const cardHTML = `
      <a href="docs/doc.html?section=${section.id}" class="section-card" style="animation-delay: ${index * 0.05}s">
        <div class="card-header">
          <div class="card-icon">${icons[section.icon] || icons['book']}</div>
          <h3 class="card-title">${section.title}</h3>
        </div>
        <p class="card-desc">${section.description}</p>
      </a>
    `;

    grid.insertAdjacentHTML('beforeend', cardHTML);
  });
});