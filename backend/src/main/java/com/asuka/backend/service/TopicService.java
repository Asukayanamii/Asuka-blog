package com.asuka.backend.service;

import com.asuka.backend.pojo.dto.TopicSaveDTO;
import com.asuka.backend.pojo.vo.TopicVO;

import java.util.ArrayList;

public interface TopicService {
    ArrayList<TopicVO> getAll();

    TopicVO getTopicById(Integer id);

    Integer createTopic(TopicSaveDTO dto);

    void updateTopic(Integer id, TopicSaveDTO dto);

    void deleteTopic(Integer id);
}
