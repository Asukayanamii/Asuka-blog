<template>
    <div class="common-layout">
        <el-container>
            <el-main>
                <section class="page-shell">
                    <nav :class="['top-nav', { hidden: isNavHidden }]">
                        <div class="nav-brand">Asuka's Blog</div>
                        <div class="nav-links">
                            <a href="#">首页</a>
                            <a href="#">专题</a>
                            <a href="#">最新</a>
                            <a href="#">关于</a>
                        </div>
                    </nav>
                    <div class="hero">
                        <div class="hero-copy">
                            <p class="eyebrow">技术日志</p>
                            <h1>在这里记录前端、后端与设计的幻想</h1>
                            <p class="hero-description">
                                这里是你的技术小天地，兼具开发力。文章、工具、部署笔记与灵感合集都被包装成萌萌哒的二次元笔记。
                            </p>
                            <div class="hero-actions">
                                <a href="#posts" class="btn btn-primary">浏览最新文章</a>
                                <a href="#topics" class="btn btn-secondary">探索专题</a>
                            </div>
                        </div>

                        <div class="hero-visual">
                            <div class="floating-card">
                                <div class="screen-header">
                                    <span></span><span></span><span></span>
                                </div>
                                <div class="screen-body">
                                    <div class="code-line line-1"></div>
                                    <div class="code-line line-2"></div>
                                    <div class="code-line line-3"></div>
                                    <div class="code-line line-4"></div>
                                </div>
                            </div>
                            <div class="petal petal-1"></div>
                            <div class="petal petal-2"></div>
                            <div class="petal petal-3"></div>
                            <div class="petal petal-4"></div>
                        </div>
                    </div>

                    <section id="topics" class="features">
                        <div class="section-header">
                            <span>内容分类</span>
                            <h2>樱花次元里的技术专题</h2>
                        </div>
                        <div class="feature-grid">
                            <article class="feature-card">
                                <h3>前端魔法</h3>
                                <p>Vue、React、TypeScript 与动效实现，帮你把界面打造成二次元可视小说。</p>
                            </article>
                            <article class="feature-card">
                                <h3>后端秘笈</h3>
                                <p>API、部署、性能优化与数据库设计，让你的项目稳定又灵动。</p>
                            </article>
                            <article class="feature-card">
                                <h3>工具仓库</h3>
                                <p>精选开发工具、插件与自动化实践，提升开发效率，打造少女心工作流。</p>
                            </article>
                            <article class="feature-card">
                                <h3>界面灵感</h3>
                                <p>色彩、排版与交互方案，结合二次元风格与技术美学。</p>
                            </article>
                        </div>
                    </section>

                    <section id="posts" class="posts">
                        <div class="posts-heading">
                            <div>
                                <span>最新更新</span>
                                <h2>最近发布的文章</h2>
                            </div>
                            <p>粉色科技日志，带你在轻柔樱花雨中阅读技术与创作心得。</p>
                        </div>

                        <div class="post-grid">
                            <article class="post-card">
                                <span class="post-tag">前端</span>
                                <h3>用 Vue 3 打造萌系组件库</h3>
                                <p>从样式系统到交互动画，一步步构建具有二次元风格的组件页面。</p>
                                <a href="#">阅读详情 →</a>
                            </article>
                            <article class="post-card">
                                <span class="post-tag">部署</span>
                                <h3>在 GitHub Pages 上发布你的粉色博客</h3>
                                <p>简易部署流程与优化建议，让你的樱花主题博客稳定上线。</p>
                                <a href="#">阅读详情 →</a>
                            </article>
                            <article class="post-card">
                                <span class="post-tag">灵感</span>
                                <h3>从手绘到 UI：甜美科技设计指南</h3>
                                <p>结合二次元插画和界面设计，创建让用户忍不住停留的页面体验。</p>
                                <a href="#">阅读详情 →</a>
                            </article>
                        </div>
                    </section>

                    <section class="subscribe">
                        <div class="subscribe-card">
                            <div>
                                <p>加入粉色技术社</p>
                                <h2>让你的技术笔记变成二次元故事</h2>
                            </div>
                            <button type="button">订阅更新</button>
                        </div>
                    </section>
                </section>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const isNavHidden = ref(false)
let lastScrollY = 0
let ticking = false

const handleScroll = () => {
  const currentScroll = window.scrollY
  if (Math.abs(currentScroll - lastScrollY) < 10) {
    ticking = false
    return
  }

  isNavHidden.value = currentScroll > lastScrollY && currentScroll > 120
  lastScrollY = currentScroll <= 0 ? 0 : currentScroll
  ticking = false
}

const onScroll = () => {
  if (!ticking) {
    window.requestAnimationFrame(handleScroll)
    ticking = true
  }
}

onMounted(() => {
  lastScrollY = window.scrollY
  window.addEventListener('scroll', onScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})
</script>

<style scoped>
.page-shell {
    min-height: 100vh;
    padding: 6rem 0 0;
    background: radial-gradient(circle at top left, rgba(91, 124, 255, 0.12), transparent 28%),
        radial-gradient(circle at bottom right, rgba(255, 120, 198, 0.12), transparent 30%),
        linear-gradient(135deg, #f0f4ff 0%, #ffe8f5 50%, #f0f8ff 100%);
    color: #2a3a5f;
    overflow-x: hidden;
    width: 100vw;
    position: relative;
    left: 50%;
    right: 50%;
    margin-left: -50vw;
    margin-right: -50vw;
}

.top-nav {
    position: fixed;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 100vw;
    z-index: 50;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0.85rem 2rem;
    background: rgba(255, 255, 255, 0.22);
    backdrop-filter: blur(16px);
    border-bottom: 1px solid rgba(255, 255, 255, 0.45);
    transition: transform 0.3s ease, opacity 0.3s ease;
}

.top-nav.hidden {
    transform: translate(-50%, -100%);
    opacity: 0;
}

.nav-brand {
    font-size: 1.35rem;
    font-weight: 800;
    color: #3b4d8f;
    letter-spacing: 0.18em;
}

.nav-links {
    display: flex;
    gap: 1.4rem;
}

.nav-links a {
    text-decoration: none;
    color: #3b4d8f;
    font-weight: 600;
    background: transparent;
    outline: none;
    border-radius: 999px;
    transition: color 0.2s ease, transform 0.2s ease, background 0.2s ease;
}

.nav-links a:hover,
.nav-links a:focus {
    color: #5b7cff;
    transform: translateY(-1px);
    background: transparent;
}

.nav-links a:focus-visible {
    outline: 2px solid rgba(91, 124, 255, 0.3);
    outline-offset: 3px;
}


.hero {
    display: grid;
    gap: 2rem;
    align-items: center;
    width: 100%;
    padding: 2rem 1.5rem;
    margin-bottom: 2rem;
}

.eyebrow {
    display: inline-block;
    margin-bottom: 1rem;
    padding: 0.45rem 1rem;
    border-radius: 999px;
    background: linear-gradient(135deg, rgba(91, 124, 255, 0.15), rgba(255, 120, 198, 0.15));
    color: #5b7cff;
    font-size: 0.95rem;
    font-weight: 700;
}

h1 {
    font-size: clamp(2.5rem, 5vw, 4rem);
    line-height: 1.02;
    letter-spacing: -0.04em;
    margin-bottom: 1rem;
}

.hero-description {
    max-width: 640px;
    margin-bottom: 1.8rem;
    color: #4a5a7f;
    font-size: 1.05rem;
    line-height: 1.8;
}

.hero-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
}

.btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 170px;
    padding: 0.95rem 1.5rem;
    border-radius: 999px;
    border: none;
    font-weight: 700;
    transition: transform 0.25s ease, box-shadow 0.25s ease;
    text-decoration: none;
}

.btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 18px 40px rgba(91, 124, 255, 0.25);
}

.btn-primary {
    color: #fff;
    background: linear-gradient(135deg, #5b7cff 0%, #ff78c6 100%);
}

.btn-secondary {
    color: #5b7cff;
    background: rgba(255, 255, 255, 0.95);
    border: 2px solid #5b7cff;
}

.hero-visual {
    position: relative;
    display: flex;
    justify-content: center;
}

.floating-card {
    position: relative;
    width: min(100%, 360px);
    padding: 1.7rem;
    border-radius: 32px;
    background: rgba(255, 255, 255, 0.94);
    box-shadow: 0 28px 80px rgba(91, 124, 255, 0.12);
    border: 1px solid rgba(91, 124, 255, 0.2);
    backdrop-filter: blur(8px);
    overflow: hidden;
}

.screen-header {
    display: flex;
    gap: 0.6rem;
    margin-bottom: 1.5rem;
}

.screen-header span {
    width: 0.8rem;
    height: 0.8rem;
    border-radius: 999px;
    background: linear-gradient(135deg, #5b7cff, #ff78c6);
}

.screen-body {
    padding: 1rem 0;
    display: grid;
    gap: 0.95rem;
}

.code-line {
    height: 0.9rem;
    border-radius: 999px;
    background: linear-gradient(90deg, rgba(91, 124, 255, 0.7), rgba(255, 120, 198, 0.7));
}

.line-1 {
    width: 90%;
}

.line-2 {
    width: 70%;
}

.line-3 {
    width: 80%;
}

.line-4 {
    width: 60%;
}

.petal {
    position: absolute;
    width: 60px;
    height: 60px;
    background: rgba(255, 255, 255, 0.65);
    border-radius: 50% 50% 50% 25%;
    filter: blur(0.35px);
    animation: float 8s ease-in-out infinite;
}

.petal::before,
.petal::after {
    content: "";
    position: absolute;
    width: 22px;
    height: 22px;
    background: rgba(255, 202, 230, 0.95);
    border-radius: 50%;
}

.petal::before {
    top: 10px;
    left: 8px;
}

.petal::after {
    bottom: 10px;
    right: 8px;
}

.petal-1 {
    top: -18%;
    left: 12%;
    animation-duration: 10s;
}

.petal-2 {
    top: 15%;
    right: 18%;
    animation-duration: 12s;
}

.petal-3 {
    bottom: 5%;
    left: 14%;
    animation-duration: 9s;
}

.petal-4 {
    bottom: 18%;
    right: 10%;
    animation-duration: 11s;
}

@keyframes float {

    0%,
    100% {
        transform: translate(0, 0) rotate(0deg);
    }

    50% {
        transform: translate(10px, -18px) rotate(6deg);
    }
}

.features,
.posts,
.subscribe {
    width: 100%;
    padding: 2rem 1.5rem;
    box-sizing: border-box;
}

.section-header {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin-bottom: 2rem;
}

.section-header span,
.posts-heading span {
    display: inline-flex;
    padding: 0.45rem 0.9rem;
    border-radius: 999px;
    background: linear-gradient(135deg, rgba(91, 124, 255, 0.12), rgba(255, 120, 198, 0.12));
    color: #5b7cff;
    font-weight: 700;
    font-size: 0.95rem;
}

.section-header h2,
.posts-heading h2 {
    font-size: clamp(1.8rem, 3vw, 2.4rem);
    margin: 0;
}

.feature-grid,
.post-grid {
    display: grid;
    gap: 1.4rem;
}

.feature-grid {
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
}

.feature-card,
.post-card {
    padding: 1.5rem;
    border-radius: 28px;
    background: rgba(255, 255, 255, 0.95);
    box-shadow: 0 24px 60px rgba(91, 124, 255, 0.08);
    border: 1px solid rgba(91, 124, 255, 0.15);
}

.feature-card h3,
.post-card h3 {
    margin-bottom: 0.85rem;
    color: #5b7cff;
}

.feature-card p,
.post-card p {
    color: #5a6a8a;
    line-height: 1.75;
}

.post-card {
    position: relative;
}

.post-tag {
    display: inline-flex;
    align-items: center;
    padding: 0.45rem 0.75rem;
    border-radius: 999px;
    background: linear-gradient(135deg, rgba(91, 124, 255, 0.15), rgba(255, 120, 198, 0.15));
    color: #5b7cff;
    font-size: 0.88rem;
    font-weight: 700;
    margin-bottom: 1rem;
}

.post-card a {
    display: inline-flex;
    margin-top: 1.2rem;
    color: #5b7cff;
    font-weight: 700;
    text-decoration: none;
}

.subscribe {
    margin-top: 0;
    padding-bottom: 3rem;
}

.subscribe-card {
    display: flex;
    flex-direction: column;
    gap: 1.3rem;
    padding: 2rem;
    border-radius: 32px;
    background: linear-gradient(135deg, rgba(91, 124, 255, 0.08), rgba(255, 120, 198, 0.08));
    box-shadow: 0 30px 80px rgba(91, 124, 255, 0.1);
    border: 1px solid rgba(91, 124, 255, 0.2);
}

.subscribe-card h2 {
    margin: 0;
    font-size: clamp(1.8rem, 3vw, 2.6rem);
}

.subscribe-card button {
    align-self: flex-start;
    padding: 1rem 1.8rem;
    border-radius: 999px;
    border: none;
    font-size: 1rem;
    font-weight: 700;
    background: linear-gradient(135deg, #5b7cff 0%, #ff78c6 100%);
    color: #fff;
    cursor: pointer;
    transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.subscribe-card button:hover {
    transform: translateY(-2px);
    box-shadow: 0 18px 40px rgba(91, 124, 255, 0.3);
}

@media (max-width: 640px) {

    .hero,
    .features,
    .posts,
    .subscribe {
        padding: 1.5rem 1rem !important;
    }

    h1 {
        font-size: clamp(1.8rem, 4vw, 2.5rem);
    }

    .hero-actions {
        flex-direction: column;
        gap: 0.8rem;
    }

    .btn {
        min-width: auto;
        width: 100%;
    }

    .feature-grid {
        grid-template-columns: 1fr;
    }

    .post-grid {
        grid-template-columns: 1fr;
    }

    .posts-heading {
        flex-direction: column;
        gap: 0.5rem;
    }

    .section-header h2,
    .posts-heading h2 {
        font-size: clamp(1.5rem, 4vw, 2rem);
    }

    .subscribe-card {
        padding: 1.5rem;
    }
}

@media (min-width: 641px) and (max-width: 768px) {

    .hero,
    .features,
    .posts,
    .subscribe {
        padding: 1.8rem 1.5rem !important;
    }

    .hero {
        gap: 1.5rem;
        margin-bottom: 2rem;
    }

    .feature-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (min-width: 769px) and (max-width: 1023px) {

    .hero,
    .features,
    .posts,
    .subscribe {
        padding: 2rem 2rem !important;
    }

    .feature-grid {
        grid-template-columns: repeat(2, 1fr);
    }

    .post-grid {
        grid-template-columns: repeat(2, minmax(0, 1fr));
    }
}

@media (min-width: 900px) {
    .hero {
        grid-template-columns: 1.1fr 0.9fr;
        gap: 3rem;
    }

    .posts-heading {
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        gap: 1rem;
    }

    .post-grid {
        grid-template-columns: repeat(3, minmax(0, 1fr));
    }
}

@media (min-width: 1024px) {

    .hero,
    .features,
    .posts,
    .subscribe {
        padding: 2.5rem 3rem !important;
    }
}

@media (min-width: 1200px) {

    .hero,
    .features,
    .posts,
    .subscribe {
        padding: 3rem 4rem !important;
    }

    .hero .hero-copy {
        max-width: 620px;
    }

    .feature-grid {
        grid-template-columns: repeat(4, minmax(220px, 1fr));
    }
}
</style>
