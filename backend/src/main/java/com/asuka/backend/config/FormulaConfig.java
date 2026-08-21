package com.asuka.backend.config;

import com.asuka.backend.properties.FormulaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** 注册公式渲染配置，具体客户端由 OSS 实现按需创建。 */
@Configuration
@EnableConfigurationProperties(FormulaProperties.class)
public class FormulaConfig {
}
