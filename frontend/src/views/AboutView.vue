<script setup>
const contactEmail = import.meta.env.VITE_CONTACT_EMAIL || '3184311912@qq.com'
const contactGithub = import.meta.env.VITE_CONTACT_GITHUB || 'https://github.com/Asukayanamii'
</script>

<template>
  <div class="about-page">
    <header class="about-banner">
      <h1>关于我</h1>
    </header>

    <main class="about-content">
      <section class="author-card">
        <img src="/avatar.jpg" alt="Asuka" class="avatar" />
        <h2>Asuka</h2>
        <p class="bio">一名热爱技术的开发者， 这里记录了我的技术学习笔记、项目实战经验以及一些日常思考。</p>
      </section>

      <div class="info-grid">
        <section class="info-card">
          <h3>联系方式</h3>
          <ul class="contact-list">
            <li><span class="label">邮箱</span><span class="value">{{ contactEmail }}</span></li>
            <li><span class="label">GitHub</span><a :href="contactGithub" target="_blank" rel="noopener noreferrer" class="value link">{{ contactGithub }}</a></li>
          </ul>
        </section>

        <section class="info-card social-card">
          <h3>社交平台</h3>
          <img src="/img/wx.png" alt="微信" class="wx-qrcode" />
          <p>微信</p>
        </section>
      </div>
    </main>
  </div>
</template>

<style scoped>
.about-banner {
  display: grid;
  min-height: 310px;
  padding: 7rem 1.5rem 3rem;
  color: #fff;
  text-align: center;
  place-items: center;
  background: linear-gradient(rgba(20, 30, 39, 0.42), rgba(20, 30, 39, 0.55)), url('/hero-banner.jpeg') center / cover;
}

.about-banner h1 {
  margin: 0;
  font-family: 'Noto Serif SC', serif;
  font-size: clamp(2rem, 4vw, 3.25rem);
}

.about-content {
  width: min(820px, calc(100% - 2rem));
  margin: 2.8rem auto 4rem;
}

.author-card,
.info-card {
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
}

.author-card { padding: 2.4rem 1.5rem; text-align: center; }
.avatar { width: 94px; height: 94px; object-fit: cover; border: 4px solid #fff; border-radius: 50%; box-shadow: 0 3px 12px rgba(0, 0, 0, 0.18); }
.author-card h2 { margin: 1rem 0 0.55rem; color: #333; font-size: 1.35rem; }
.bio { max-width: 500px; margin: 0 auto; color: #666; font-size: 0.94rem; line-height: 1.9; }

.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-top: 1.5rem; }
.info-card { padding: 1.5rem; }
.info-card h3 { margin: 0 0 1rem; color: #333; font-size: 1rem; }
.contact-list { padding: 0; margin: 0; list-style: none; }
.contact-list li { display: grid; gap: 0.25rem; padding: 0.8rem 0; border-top: 1px solid var(--blog-line); }
.label { color: var(--blog-muted); font-size: 0.76rem; }
.value { color: var(--blog-ink); font-size: 0.88rem; overflow-wrap: anywhere; }
.link { color: var(--blog-blue); text-decoration: none; }
.link:hover { text-decoration: underline; }
.social-card { text-align: center; }
.social-card h3 { text-align: left; }
.wx-qrcode { width: 145px; border: 1px solid var(--blog-line); }
.social-card p { margin: 0.65rem 0 0; color: var(--blog-muted); font-size: 0.82rem; }

@media (max-width: 620px) {
  .about-banner { min-height: 270px; padding-top: 6rem; }
  .about-content { width: min(100% - 1.25rem, 560px); margin-top: 1.7rem; }
  .info-grid { grid-template-columns: 1fr; }
}
</style>
