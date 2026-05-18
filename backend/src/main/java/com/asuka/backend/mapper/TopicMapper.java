package com.asuka.backend.mapper;

import com.asuka.backend.pojo.entity.Topic;
import com.asuka.backend.pojo.vo.TopicVO;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface TopicMapper {
    @Operation(summary = "获取所有专题")
    ArrayList<TopicVO> getAll();

    Topic getById(Integer id);

    void insert(Topic topic);

    void update(Topic topic);

    void deleteById(Integer id);
}
