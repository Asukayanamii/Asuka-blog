package com.asuka.backend.service;

import java.io.InputStream;

/** 对象存储的最小抽象，避免业务层依赖阿里云 SDK。 */
public interface ObjectStorageService {
    String putIfAbsent(String objectKey, InputStream content, long contentLength, String contentType);
}
