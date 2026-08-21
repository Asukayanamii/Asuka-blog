package com.asuka.backend.service.impl;

import com.asuka.backend.service.ObjectStorageService;
import com.asuka.backend.utils.OssUtil;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/** 阿里云 OSS 实现；公式对象按哈希命名，因此存在时可直接复用。 */
@Service
public class AliyunOssObjectStorageService implements ObjectStorageService {
    private final OssUtil ossUtil;

    public AliyunOssObjectStorageService(OssUtil ossUtil) {
        this.ossUtil = ossUtil;
    }

    @Override
    public String putIfAbsent(String objectKey, InputStream content, long contentLength, String contentType) {
        return ossUtil.upload(objectKey, content, contentLength, contentType);
    }
}
