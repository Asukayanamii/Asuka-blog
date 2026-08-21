package com.asuka.backend.pojo.vo;

import lombok.Data;

/** 存量文章回填所需的最小数据集。 */
@Data
public class ArticleMarkdownRow {
    private Integer id;
    private String contentMd;
}
