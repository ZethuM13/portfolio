const rows = document.querySelector('#service-rows');
const emptyState = document.querySelector('#empty-state');
const search = document.querySelector('#search');
const dialog = document.querySelector('#service-dialog');
const form = document.querySelector('#service-form');
const formMessage = document.querySelector('#form-message');

let services = [];

async function loadServices(query = '') {
  const response = await fetch(`/api/services${query ? `?q=${encodeURIComponent(query)}` : ''}`);
  if (!response.ok) throw new Error('Unable to load the service catalogue.');
  services = await response.json();
  renderServices();
}

function renderServices() {
  rows.innerHTML = services.map(service => `
    <tr>
      <td>${escapeHtml(service.name)}</td>
      <td>${escapeHtml(service.owner)}</td>
      <td>${escapeHtml(service.domain)}</td>
      <td><span class="criticality">${escapeHtml(service.criticality)}</span></td>
      <td><span class="badge ${service.status.toLowerCase()}">${escapeHtml(service.status)}</span></td>
    </tr>
  `).join('');
  emptyState.hidden = services.length !== 0;
  updateMetrics();
}

function updateMetrics() {
  document.querySelector('#total-count').textContent = services.length;
  document.querySelector('#healthy-count').textContent = services.filter(item => item.status === 'HEALTHY').length;
  document.querySelector('#attention-count').textContent = services.filter(item => item.status !== 'HEALTHY').length;
  document.querySelector('#critical-count').textContent = services.filter(item => item.criticality === 'CRITICAL').length;
}

function escapeHtml(value) {
  return String(value).replace(/[&<>'"]/g, char => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[char]));
}

let searchTimer;
search.addEventListener('input', () => {
  clearTimeout(searchTimer);
  searchTimer = setTimeout(() => loadServices(search.value.trim()).catch(showPageError), 180);
});

document.querySelector('#open-dialog').addEventListener('click', () => dialog.showModal());
document.querySelector('#close-dialog').addEventListener('click', closeDialog);
document.querySelector('#cancel-dialog').addEventListener('click', closeDialog);

function closeDialog() {
  form.reset();
  formMessage.textContent = '';
  dialog.close();
}

form.addEventListener('submit', async event => {
  event.preventDefault();
  formMessage.textContent = '';
  const payload = Object.fromEntries(new FormData(form).entries());
  try {
    const response = await fetch('/api/services', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(payload)
    });
    const result = await response.json();
    if (!response.ok) throw new Error(result.error || 'Unable to register the service.');
    closeDialog();
    search.value = '';
    await loadServices();
  } catch (error) {
    formMessage.textContent = error.message;
  }
});

function showPageError(error) {
  rows.innerHTML = `<tr><td colspan="5">${escapeHtml(error.message)}</td></tr>`;
}

loadServices().catch(showPageError);
