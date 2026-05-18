package com.asuka.backend.service.impl;

import com.asuka.backend.mapper.TopicMapper;
import com.asuka.backend.pojo.dto.TopicSaveDTO;
import com.asuka.backend.pojo.entity.Topic;
import com.asuka.backend.pojo.vo.TopicVO;
import com.asuka.backend.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public TopicVO getTopicById(Integer id) {
        Topic topic = topicMapper.getById(id);
        if (topic == null) return null;
        return TopicVO.builder()
                .id(topic.getId())
                .topicName(topic.getTopicName())
                .description(topic.getDescription())
                .build();
    }

    @Override
    public Integer createTopic(TopicSaveDTO dto) {
        log.info("新增专题:{}", dto.getTopicName());
        Topic topic = Topic.builder()
                .topicName(dto.getTopicName())
                .description(dto.getDescription())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        topicMapper.insert(topic);
        return topic.getId();
    }

    @Override
    public void updateTopic(Integer id, TopicSaveDTO dto) {
        log.info("更新专题:{}", id);
        Topic topic = Topic.builder()
                .id(id)
                .topicName(dto.getTopicName())
                .description(dto.getDescription())
                .updateTime(LocalDateTime.now())
                .build();
        topicMapper.update(topic);
    }

    @Override
    public void deleteTopic(Integer id) {
        log.info("删除专题:{}", id);
        topicMapper.deleteById(id);
    }
}
