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
        // 专题列表直接返回展示对象，避免将实体字段暴露给接口调用方。
        return topicMapper.getAll();
    }

    @Override
    public TopicVO getTopicById(Integer id) {
        // 先查询实体，未找到时返回空值，由统一响应层决定具体表现。
        Topic topic = topicMapper.getById(id);
        if (topic == null) return null;
        // 将持久化实体转换为专题详情 VO。
        return TopicVO.builder()
                .id(topic.getId())
                .topicName(topic.getTopicName())
                .description(topic.getDescription())
                .build();
    }

    @Override
    public Integer createTopic(TopicSaveDTO dto) {
        log.info("新增专题:{}", dto.getTopicName());
        // 新建专题同时记录创建时间和更新时间，保持时间字段完整。
        Topic topic = Topic.builder()
                .topicName(dto.getTopicName())
                .description(dto.getDescription())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        topicMapper.insert(topic);
        // MyBatis 回填新专题主键，返回给前端作为资源标识。
        return topic.getId();
    }

    @Override
    public void updateTopic(Integer id, TopicSaveDTO dto) {
        log.info("更新专题:{}", id);
        // 更新只覆盖可编辑字段和更新时间，不改动创建时间。
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
        // 按主键删除专题。
        topicMapper.deleteById(id);
    }
}
