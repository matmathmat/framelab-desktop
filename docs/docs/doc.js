// Initialiser mermaid
mermaid.initialize({ startOnLoad: false, theme: 'default' });

marked.use({	
  renderer: {
    // pour mermaid et le code 
    code({ text, lang }) {
      if (lang === 'mermaid') {
        return `<div class="mermaid">${text}</div>`;
      }
      return `<pre><code class="language-${lang ?? ''}">${text}</code></pre>`;
    },
    link({ href, title, text }) {
      // Si le lien commence par / et qu'on est sur GitHub Pages, on préfixe avec BASE_PATH
      if (href.startsWith('/') && window.location.hostname.includes('github.io')) {
        href = BASE_PATH + href.replace(/^\//, '');
      }
      const titleAttr = title ? ` title="${title}"` : '';
      return `<a href="${href}"${titleAttr}>${text}</a>`;
    }
  }
});

document.addEventListener("DOMContentLoaded", async () => {
  // Récupérer la section dans l'URL (ex: doc.html?section=guide)
  const urlParams = new URLSearchParams(window.location.search);
  const sectionId = urlParams.get('section') || SITE_CONFIG.sections[0].id;
  
  const contentDiv = document.getElementById('doc-content');
  const sidebarDiv = document.getElementById('sidebar');

  // Charger TOUS les fichiers markdown en arrière-plan pour générer la sidebar globale
  const allSectionsData = await Promise.all(
    SITE_CONFIG.sections.map(async (sec) => {
      try {
        const res = await fetch(sec.file);
        const md = res.ok ? await res.text() : "";
        return { ...sec, md };
      } catch {
        return { ...sec, md: "" };
      }
    })
  );

  // Afficher le contenu de la section ACTIVE au centre
  const activeSectionData = allSectionsData.find(s => s.id === sectionId);

  if (!activeSectionData || !activeSectionData.md) {
    contentDiv.innerHTML = '<div class="doc-error"><h3>Erreur 404</h3><p>Section introuvable ou fichier vide.</p></div>';
  } else {
    contentDiv.innerHTML = marked.parse(activeSectionData.md);
	
	// Déclencher mermaid sur les nouveaux éléments
	await mermaid.run({ nodes: contentDiv.querySelectorAll('.mermaid') });

    // Ajouter des IDs aux titres pour que les liens d'ancre (scroll) fonctionnent
    contentDiv.querySelectorAll('h2, h3').forEach(heading => {
      const id = heading.textContent.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/\s+/g, '-').replace(/[^\w-]/g, '');
      heading.id = id;
    });

    // Si on arrive sur la page avec un #ancre, on force le scroll
    if (window.location.hash) {
      setTimeout(() => {
        const el = document.querySelector(window.location.hash);
        if (el) el.scrollIntoView({ behavior: 'smooth' });
      }, 150);
    }
  }

  // Icônes de la sidebar
  const icons = {
    book: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path></svg>',
    bolt: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"></polygon></svg>',
    code: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="16 18 22 12 16 6"></polyline><polyline points="8 6 2 12 8 18"></polyline></svg>',
    cube: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4A2 2 0 0 0 13 20l7-4A2 2 0 0 0 21 16z"></path><polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline><line x1="12" y1="22.08" x2="12" y2="12"></line></svg>'
  };

  // Générer la Sidebar globale
  sidebarDiv.innerHTML = '';

  allSectionsData.forEach(sec => {
    const sectionDiv = document.createElement('div');
    sectionDiv.className = 'sidebar-section';

    const isActive = sec.id === sectionId;

    // Niveau 1 : Le titre de la section (H1)
    const secHeader = document.createElement('a');
    secHeader.href = `doc.html?section=${sec.id}`;
    secHeader.className = `sidebar-section-btn ${isActive ? 'active' : ''}`;
    secHeader.innerHTML = `
      <span class="sidebar-section-icon">
        ${icons[sec.icon] || icons['book']}
      </span>
      ${sec.title}
    `;
    sectionDiv.appendChild(secHeader);

    // Conteneur pour H2 et H3
    const pagesList = document.createElement('div');
    pagesList.className = 'sidebar-pages open';
    pagesList.style.maxHeight = 'none';

    if (sec.md) {
      // On analyse le Markdown pour extraire les H2 et H3
      const tokens = marked.lexer(sec.md);
      const headings = tokens.filter(t => t.type === 'heading' && (t.depth === 2 || t.depth === 3));

      headings.forEach(heading => {
        const text = heading.text;
        const id = text.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/\s+/g, '-').replace(/[^\w-]/g, '');
        
        // Le lien combine la page ET l'ancre (ex: doc.html?section=guide#creer-un-compte)
        const targetUrl = `doc.html?section=${sec.id}#${id}`;

        const link = document.createElement('a');
        link.href = targetUrl;
        link.textContent = text;

        if (heading.depth === 2) {
          // Niveau 2 : sous-titre
          link.className = 'sidebar-h2-link';
        } else {
          // Niveau 3 : sous-sous-titre
          link.className = 'sidebar-page-link';
        }

        // Effet actif au clic
        link.addEventListener('click', () => {
          document.querySelectorAll('.sidebar-h2-link, .sidebar-page-link').forEach(l => l.classList.remove('active'));
          link.classList.add('active');
        });

        pagesList.appendChild(link);
      });
    }

    sectionDiv.appendChild(pagesList);
    sidebarDiv.appendChild(sectionDiv);
  });

  // Gestion du menu mobile
  const mobileBtn = document.getElementById('mobile-menu-btn');
  mobileBtn.addEventListener('click', () => {
    sidebarDiv.classList.toggle('open');
  });
});