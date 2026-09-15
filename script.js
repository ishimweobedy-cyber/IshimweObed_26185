const menuToggle = document.getElementById('menuToggle');
const navLinksEl = document.getElementById('navLinks');
const pages = document.querySelectorAll('.page');
const navAnchors = navLinksEl.querySelectorAll('a');

function showPage(id) {
  pages.forEach(p => p.classList.remove('active'));
  navAnchors.forEach(a => a.classList.remove('active'));

  const target = document.getElementById(id);
  if (target) {
    target.classList.add('active');
    target.scrollTop = 0;
  }

  const activeLink = navLinksEl.querySelector(`a[href="#${id}"]`);
  if (activeLink) activeLink.classList.add('active');
}

// Nav link clicks
navAnchors.forEach(link => {
  link.addEventListener('click', e => {
    e.preventDefault();
    const id = link.getAttribute('href').replace('#', '');
    showPage(id);
    navLinksEl.classList.remove('open');
  });
});

// data-page buttons (e.g. "View My Work")
document.querySelectorAll('[data-page]').forEach(btn => {
  btn.addEventListener('click', e => {
    e.preventDefault();
    showPage(btn.getAttribute('data-page'));
  });
});

// Mobile menu toggle
menuToggle.addEventListener('click', () => {
  navLinksEl.classList.toggle('open');
});

// Contact form — opens mailto with the filled details
const contactForm = document.getElementById('contactForm');
if (contactForm) {
  contactForm.addEventListener('submit', e => {
    e.preventDefault();
    const name    = document.getElementById('senderName').value.trim();
    const email   = document.getElementById('senderEmail').value.trim();
    const message = document.getElementById('senderMessage').value.trim();
    const subject = encodeURIComponent(`Message from ${name}`);
    const body    = encodeURIComponent(`Name: ${name}\nEmail: ${email}\n\n${message}`);
    window.location.href = `mailto:obedyishimwe@gmail.com?subject=${subject}&body=${body}`;
    document.getElementById('formSuccess').classList.add('show');
    contactForm.reset();
    setTimeout(() => document.getElementById('formSuccess').classList.remove('show'), 5000);
  });
}

// Footer year
document.getElementById('year').textContent = new Date().getFullYear();

// Show home on load
showPage('home');
