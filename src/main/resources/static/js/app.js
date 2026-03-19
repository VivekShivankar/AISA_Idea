// AISA_Idea — Main JavaScript

document.addEventListener('DOMContentLoaded', function () {
  // Date chip
  const chip = document.getElementById('dchip');
  if (chip) chip.textContent = new Date().toLocaleDateString('en-IN', { weekday: 'short', day: 'numeric', month: 'short' });

  // Auto dismiss flash messages
  document.querySelectorAll('.msg.ok,.msg.err').forEach(m => {
    if (m.textContent.trim()) setTimeout(() => m.style.display = 'none', 4500);
  });
});

// Toast
function showToast(msg, type) {
  const t = document.getElementById('toast');
  if (!t) return;
  t.textContent = msg; t.className = 'toast show ' + (type || 'ok');
  setTimeout(() => t.className = 'toast', 3500);
}

// Calc preview on Add Fuel Log page
function calcPreview() {
  const fl = parseFloat(document.getElementById('fuelLiters')?.value);
  const fp = parseFloat(document.getElementById('fuelPrice')?.value);
  const prev = parseFloat(document.getElementById('prevOdometer')?.value);
  const curr = parseFloat(document.getElementById('currOdometer')?.value);
  const el = document.getElementById('calc-preview');
  if (!el) return;
  if (fl > 0 && prev > 0 && curr > prev) {
    const d = curr - prev, mi = d / fl, c = fl * (fp || 0);
    document.getElementById('cp-dist').textContent = d.toFixed(1) + ' km';
    document.getElementById('cp-mil').textContent = mi.toFixed(2) + ' km/L';
    document.getElementById('cp-cost').textContent = '₹' + c.toFixed(2);
    el.style.display = 'flex';
  } else { el.style.display = 'none'; }
}

// Trip search filter
function filterTrips() {
  const q = (document.getElementById('trip-search')?.value || '').toLowerCase();
  document.querySelectorAll('#trips-body tr').forEach(row => {
    row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
  });
}

// Animate ring score
function animateRing(id, score) {
  const el = document.getElementById(id);
  if (el) el.style.strokeDashoffset = (251.2 - (score / 100) * 251.2).toString();
}

// Animate Z-score marker
function animateZMarker(id, zScore) {
  const el = document.getElementById(id);
  if (el) {
    const pct = Math.min(Math.max(((zScore + 3) / 6) * 100, 0), 100);
    el.style.left = pct + '%';
  }
}

// Animate traffic bars
function animateTrafficBars(low, med, high) {
  const maxVal = Math.max(low, med, high, 1);
  setTimeout(() => {
    const l = document.getElementById('tbar-l');
    const m = document.getElementById('tbar-m');
    const h = document.getElementById('tbar-h');
    if (l) l.style.width = (low / maxVal * 100) + '%';
    if (m) m.style.width = (med / maxVal * 100) + '%';
    if (h) h.style.width = (high / maxVal * 100) + '%';
  }, 300);
}

// Mileage trend chart
function drawTrendChart(canvasId, data, labels) {
  const canvas = document.getElementById(canvasId);
  if (!canvas || data.length < 2) return;
  canvas.width = canvas.parentElement.offsetWidth || 600;
  canvas.height = 170;
  const ctx = canvas.getContext('2d');
  const W = canvas.width, H = canvas.height;
  const pad = { t: 18, r: 18, b: 36, l: 50 };
  const cW = W - pad.l - pad.r, cH = H - pad.t - pad.b;
  const minV = Math.min(...data) * 0.92, maxV = Math.max(...data) * 1.06;
  const tx = i => pad.l + (i / (data.length - 1)) * cW;
  const ty = v => pad.t + cH - ((v - minV) / (maxV - minV || 1)) * cH;
  // Grid
  ctx.strokeStyle = '#1e3047'; ctx.lineWidth = 0.6;
  for (let i = 0; i <= 4; i++) {
    const y = pad.t + (cH / 4) * i;
    ctx.beginPath(); ctx.moveTo(pad.l, y); ctx.lineTo(pad.l + cW, y); ctx.stroke();
    const val = maxV - (i / 4) * (maxV - minV);
    ctx.fillStyle = '#3d5a7a'; ctx.font = '10px Inter,sans-serif'; ctx.textAlign = 'right';
    ctx.fillText(val.toFixed(1), pad.l - 6, y + 4);
  }
  // Area fill
  const gr = ctx.createLinearGradient(0, pad.t, 0, pad.t + cH);
  gr.addColorStop(0, 'rgba(59,130,246,0.28)'); gr.addColorStop(1, 'rgba(59,130,246,0)');
  ctx.beginPath(); ctx.moveTo(tx(0), ty(data[0]));
  for (let i = 1; i < data.length; i++) {
    const cx = (tx(i - 1) + tx(i)) / 2;
    ctx.bezierCurveTo(cx, ty(data[i - 1]), cx, ty(data[i]), tx(i), ty(data[i]));
  }
  ctx.lineTo(tx(data.length - 1), pad.t + cH);
  ctx.lineTo(tx(0), pad.t + cH); ctx.closePath();
  ctx.fillStyle = gr; ctx.fill();
  // Line
  ctx.beginPath(); ctx.moveTo(tx(0), ty(data[0]));
  for (let i = 1; i < data.length; i++) {
    const cx = (tx(i - 1) + tx(i)) / 2;
    ctx.bezierCurveTo(cx, ty(data[i - 1]), cx, ty(data[i]), tx(i), ty(data[i]));
  }
  ctx.strokeStyle = '#3b82f6'; ctx.lineWidth = 2.5; ctx.stroke();
  // Dots + labels
  data.forEach((v, i) => {
    ctx.beginPath(); ctx.arc(tx(i), ty(v), 5, 0, Math.PI * 2);
    ctx.fillStyle = '#3b82f6'; ctx.fill();
    ctx.strokeStyle = '#0f1c2e'; ctx.lineWidth = 2; ctx.stroke();
    ctx.fillStyle = '#7b9cc4'; ctx.font = '10px Inter,sans-serif'; ctx.textAlign = 'center';
    ctx.fillText(labels[i], tx(i), H - 6);
  });
}

// Servicing progress bar
function animateSvcBar(id, pct, color) {
  setTimeout(() => {
    const el = document.getElementById(id);
    if (el) { el.style.width = pct + '%'; el.style.background = color; }
  }, 300);
}

// ── CHATBOT ──────────────────────────────────────────
const chatbot = {
  open: false,
  init() {
    const fab = document.getElementById('chatbot-fab');
    const win = document.getElementById('chatbot-window');
    const closeBtn = document.getElementById('chat-close');
    const sendBtn = document.getElementById('chat-send');
    const input = document.getElementById('chat-input');
    if (!fab || !win) return;

    fab.addEventListener('click', () => { this.open = !this.open; win.classList.toggle('open', this.open); if (this.open) input?.focus(); });
    closeBtn?.addEventListener('click', () => { this.open = false; win.classList.remove('open'); });
    sendBtn?.addEventListener('click', () => this.send());
    input?.addEventListener('keydown', e => { if (e.key === 'Enter') this.send(); });
    document.querySelectorAll('.chat-sug').forEach(s => s.addEventListener('click', () => { if (input) input.value = s.textContent; this.send(); }));
  },
  send() {
    const input = document.getElementById('chat-input');
    const msgs = document.getElementById('chat-messages');
    if (!input || !msgs) return;
    const text = input.value.trim();
    if (!text) return;
    input.value = '';
    // User bubble
    const userMsg = document.createElement('div');
    userMsg.className = 'chat-msg user'; userMsg.textContent = text; msgs.appendChild(userMsg);
    // Typing indicator
    const typing = document.createElement('div');
    typing.className = 'chat-msg bot'; typing.innerHTML = '<div class="chat-typing"><span></span><span></span><span></span></div>';
    msgs.appendChild(typing); msgs.scrollTop = msgs.scrollHeight;
    // API call
    fetch('/api/chatbot', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ message: text }) })
      .then(r => r.json()).then(data => {
        msgs.removeChild(typing);
        const botMsg = document.createElement('div');
        botMsg.className = 'chat-msg bot'; botMsg.textContent = data.reply || 'Sorry, I could not process that.';
        msgs.appendChild(botMsg); msgs.scrollTop = msgs.scrollHeight;
      }).catch(() => {
        msgs.removeChild(typing);
        const botMsg = document.createElement('div');
        botMsg.className = 'chat-msg bot'; botMsg.textContent = '⚠ Connection error. Please try again.';
        msgs.appendChild(botMsg); msgs.scrollTop = msgs.scrollHeight;
      });
  }
};
document.addEventListener('DOMContentLoaded', () => chatbot.init());
