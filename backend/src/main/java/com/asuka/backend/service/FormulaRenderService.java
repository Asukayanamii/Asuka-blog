package com.asuka.backend.service;

/** 将纯 LaTeX 源码转换为可上传的位图。 */
public interface FormulaRenderService {
    RenderedFormula render(String latex, boolean block);

    record RenderedFormula(byte[] bytes, int width, int height, boolean block) {
    }
}
