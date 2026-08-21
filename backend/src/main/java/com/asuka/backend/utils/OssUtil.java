package com.asuka.backend.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.asuka.backend.exception.MarkdownRenderException;
import com.asuka.backend.properties.FormulaProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/** 阿里云 OSS 通用上传工具，仅负责对象存储读写，不承载文章业务。 */
@Component
public class OssUtil {
    private final FormulaProperties properties;

    public OssUtil(FormulaProperties properties) {
        this.properties = properties;
    }

    /** 上传图片或文件；对象存在时直接复用，避免重复上传相同公式。 */
    public String upload(String objectKey, InputStream content, long contentLength, String contentType) {
        validateConfiguration();
        OSS client = new OSSClientBuilder().build(properties.getOssEndpoint(),
                properties.getOssAccessKeyId(), properties.getOssAccessKeySecret());
        try {
            if (!client.doesObjectExist(properties.getOssBucket(), objectKey)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(contentLength);
                metadata.setContentType(contentType);
                client.putObject(new PutObjectRequest(properties.getOssBucket(), objectKey, content, metadata));
            }
            return properties.getPublicBaseUrl().replaceAll("/$", "") + "/" + objectKey;
        } catch (Exception e) {
            throw new MarkdownRenderException("上传阿里云 OSS 资源失败: " + objectKey, e);
        } finally {
            client.shutdown();
        }
    }

    private void validateConfiguration() {
        StringBuilder missing = new StringBuilder();
        appendMissing(missing, "endpoint", properties.getOssEndpoint());
        appendMissing(missing, "bucket", properties.getOssBucket());
        appendMissing(missing, "accessKeyId", properties.getOssAccessKeyId());
        appendMissing(missing, "accessKeySecret", properties.getOssAccessKeySecret());
        appendMissing(missing, "publicBaseUrl", properties.getPublicBaseUrl());
        if (missing.length() > 0) {
            throw new MarkdownRenderException("已检测到数学公式，但阿里云 OSS 配置不完整，缺少: " + missing);
        }
    }

    private void appendMissing(StringBuilder missing, String name, String value) {
        if (isBlank(value)) {
            if (missing.length() > 0) {
                missing.append(", ");
            }
            missing.append("blog.formula.").append(name);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
