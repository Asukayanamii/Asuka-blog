import MarkdownIt from 'markdown-it'
import markdownItKatex from '@vscode/markdown-it-katex'

// 兼容 CJS/ESM 互操作：Vite 预打包后默认导出即为插件函数，Node 环境下则包裹在 .default 中。
const katexPlugin = markdownItKatex.default ?? markdownItKatex

/**
 * 复刻 md-editor-v3 内部 HeadingPlugin 的标题 id 注入规则：
 * 以标题原文作为 id、不去重，与编辑器 onHtmlChanged 输出的锚点保持一致，
 * 从而保证“编辑器保存”与“一键刷新”生成的 HTML 产物完全一致。
 */
function injectHeadingId(md) {
  md.renderer.rules.heading_open = (tokens, idx, options, env, self) => {
    const token = tokens[idx]
    const text = (tokens[idx + 1]?.children || [])
      .reduce(
        (acc, c) => acc + (['text', 'code_inline', 'math_inline'].includes(c.type) ? c.content : ''),
        ''
      )
    // 与 md-editor-v3 相同：仅对块级（level===0）标题注入 id。
    if (token.map && token.level === 0) {
      token.attrSet('id', text)
    }
    // markdown-it 默认 heading 渲染规则为 renderToken，不存在预设的 rules.heading_open，需直接调用。
    return md.renderer.renderToken(tokens, idx, options)
  }
}

/** 每次调用创建全新渲染器，避免标题锚点去重状态跨文档复用。 */
function createMarkdownIt() {
  const md = new MarkdownIt({ html: true, linkify: true, breaks: true })
  md.use(katexPlugin, { throwOnError: false, strict: false })
  injectHeadingId(md)
  return md
}

/**
 * 将 Markdown 渲染为 HTML，供管理端“一键刷新”批量重渲染使用。
 * 渲染配置与 md-editor-v3 编辑器同源（markdown-it + KaTeX），产出可存入 content_html。
 */
export function renderMarkdown(source) {
  return createMarkdownIt().render(source || '')
}
