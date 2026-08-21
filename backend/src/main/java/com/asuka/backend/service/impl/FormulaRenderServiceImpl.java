package com.asuka.backend.service.impl;

import com.asuka.backend.exception.MarkdownRenderException;
import com.asuka.backend.properties.FormulaProperties;
import com.asuka.backend.service.FormulaRenderService;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/** 使用 JLaTeXMath 将公式绘制成固定白底 PNG，保证暗色主题下公式仍然清晰。 */
@Service
public class FormulaRenderServiceImpl implements FormulaRenderService {
    private final FormulaProperties properties;

    public FormulaRenderServiceImpl(FormulaProperties properties) {
        this.properties = properties;
    }

    @Override
    public RenderedFormula render(String latex, boolean block) {
        try {
            // JLaTeXMath does not define every AMS command used by web Markdown.
            // These aliases preserve the visual meaning while keeping rendering deterministic.
            String compatibleLatex = latex.replace("\\oiint", "\\iint");
            TeXFormula formula = new TeXFormula(compatibleLatex);
            TeXIcon icon = formula.createTeXIcon(
                    block ? TeXConstants.STYLE_DISPLAY : TeXConstants.STYLE_TEXT,
                    properties.getFontSize()
            );
            icon.setInsets(new Insets(2, 2, 2, 2));
            // 使用 RGB 而不是透明 ARGB，避免公式黑色文字落在暗色页面背景上时失去对比度。
            BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            icon.paintIcon(null, graphics, 0, 0);
            graphics.dispose();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, "png", output);
            return new RenderedFormula(output.toByteArray(), image.getWidth(), image.getHeight(), block);
        } catch (Exception e) {
            throw new MarkdownRenderException("LaTeX 公式渲染失败: " + latex, e);
        }
    }

    /** 渲染结果封装，供对象存储层上传而不暴露图形库类型。 */
}
