<template>
  <div class="about-page">
    <div class="about-card">
      <div class="avatar-placeholder">
        <span>Asuka</span>
      </div>
      <h1>关于我</h1>
      <p class="bio">
        一名热爱技术的开发者，
        这里记录了我的技术学习笔记、项目实战经验以及一些日常思考。
      </p>
    </div>

    <div class="info-grid">
      <div class="info-card">
        <h3>联系方式</h3>
        <ul class="contact-list">
          <li>
            <span class="label">邮箱</span>
            <span class="value placeholder">your-email@example.com</span>
          </li>
          <li>
            <span class="label">GitHub</span>
            <span class="value placeholder">github.com/your-username</span>
          </li>
          <li>
            <span class="label">博客</span>
            <span class="value placeholder">your-blog-url.com</span>
          </li>
        </ul>
      </div>

      <div class="info-card">
        <h3>社交平台</h3>
        <div class="qrcode-placeholder">
          <div class="qrcode-grid">
            <span></span><span></span><span></span><span></span>
            <span></span><span></span><span></span><span></span>
            <span></span><span></span><span></span><span></span>
            <span></span><span></span><span></span><span></span>
          </div>
          <p class="qrcode-label">微信 · 待填写</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.about-page {
  max-width: 700px;
  margin: 0 auto;
  padding: 2rem 1.5rem 4rem;
}

.about-card {
  text-align: center;
  margin-bottom: 2.5rem;
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin: 0 auto 1.5rem;
  background: linear-gradient(135deg, #5b7cff 0%, #ff78c6 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 1.4rem;
  font-weight: 800;
  letter-spacing: 0.05em;
}

.about-card h1 {
  font-size: 2rem;
  margin: 0 0 1rem;
  color: #2a3a5f;
}

.bio {
  color: #5a6a8a;
  line-height: 1.8;
  font-size: 1.05rem;
  margin: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.info-card {
  background: rgba(255, 255, 255, 0.92);
  border-radius: 24px;
  padding: 1.5rem;
  border: 1px solid rgba(91, 124, 255, 0.12);
  box-shadow: 0 20px 50px rgba(91, 124, 255, 0.08);
}

.info-card h3 {
  margin: 0 0 1.2rem;
  font-size: 1.1rem;
  color: #2a3a5f;
}

.contact-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.contact-list li {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
  padding: 0.6rem 0;
  border-bottom: 1px solid rgba(91, 124, 255, 0.08);
}

.contact-list li:last-child {
  border-bottom: none;
}

.label {
  font-size: 0.8rem;
  color: #8a9bb8;
  font-weight: 600;
}

.value {
  font-size: 0.9rem;
  color: #2a3a5f;
}

.value.placeholder {
  color: #b0c0d8;
  font-style: italic;
}

.qrcode-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.8rem;
  padding: 0.5rem 0;
}

.qrcode-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 4px;
  width: 120px;
  height: 120px;
  padding: 8px;
  background: #fff;
  border: 2px solid #e0e8f0;
  border-radius: 12px;
}

.qrcode-grid span {
  border-radius: 2px;
  background: #2a3a5f;
}

.qrcode-grid span:nth-child(odd) {
  background: #2a3a5f;
}

.qrcode-grid span:nth-child(even) {
  background: #e8ecf4;
}

.qrcode-label {
  margin: 0;
  font-size: 0.85rem;
  color: #8a9bb8;
}

@media (max-width: 640px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
