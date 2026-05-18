# Spring Boot 3 整合 MyBatis-Plus 实战教程

## 环境准备

确保你已经安装了以下工具：

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- IDE（推荐 IntelliJ IDEA）

## 项目初始化

### 创建 Spring Boot 项目

使用 Spring Initializr 创建项目，选择以下依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
        <version>3.5.5</version>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
</dependencies>
```

## 配置数据源

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/myblog
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

## 创建实体类

```java
@Data
@TableName("blog_article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String summary;

    @TableField("content_md")
    private String contentMd;

    @TableField("content_html")
    private String contentHtml;

    private Long topicId;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

## Mapper 层

```java
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    Page<ArticleListVO> pageQuery(ArticlePageQueryDTO dto);
}
```

## 分页查询原理解析

> MyBatis-Plus 的分页功能基于拦截器实现，在执行 SQL 前自动拼接 COUNT 查询和 LIMIT 语句。

### 执行流程

1. 拦截器拦截 PreparedStatement
2. 判断是否为 SELECT 查询
3. 自动生成 COUNT 查询获取总数
4. 修改原 SQL 添加 LIMIT 分页
5. 执行两条 SQL 并封装结果

## 常见问题

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| 分页不生效 | 未配置分页插件 | 添加 `PaginationInnerInterceptor` |
| COUNT 查询慢 | 表数据量过大 | 优化索引或关闭自动 COUNT |
| 类型转换异常 | 实体字段类型不匹配 | 检查数据库字段类型 |

## 性能优化建议

1. **合理设置分页大小**：避免一次查询过多数据
2. **使用索引覆盖**：SELECT 只查询需要的字段
3. **缓存热点数据**：使用 Redis 缓存频繁查询的结果

```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
}
```

![MyBatis-Plus 架构图](https://picsum.photos/seed/mybatis/800/400)
