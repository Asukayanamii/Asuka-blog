package com.asuka.backend.service.impl;

import com.asuka.backend.mapper.TopicMapper;
import com.asuka.backend.pojo.vo.TopicVO;
import com.asuka.backend.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicMapper topicMapper;

    @Override
    @Operation(summary = "获取所有专题")
    public ArrayList<TopicVO> getAll() {
        return topicMapper.getAll();
    }
}
