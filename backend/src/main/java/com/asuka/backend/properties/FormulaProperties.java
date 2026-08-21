package com.asuka.backend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** 数学公式图片和对象存储的运行参数。 */
@Data
@ConfigurationProperties(prefix = "blog.formula")
public class FormulaProperties {
    private int fontSize = 20;
    private int dpi = 150;
    // Increment when image rendering semantics change, so old OSS objects are not reused.
    private String renderVersion = "2";
    private String ossEndpoint;
    private String ossRegion;
    private String ossBucket;
    private String ossAccessKeyId;
    private String ossAccessKeySecret;
    private String publicBaseUrl;
    private String objectPrefix = "blog/formulas";
}
