package com.asuka.backend.service;

import com.asuka.backend.pojo.dto.TopicSaveDTO;
import com.asuka.backend.pojo.vo.TopicVO;

import java.util.ArrayList;

public interface TopicService {
    /** 查询全部专题。 */
    ArrayList<TopicVO> getAll();

    /** 查询专题详情。 */
    TopicVO getTopicById(Integer id);

    /** 新增专题。 */
    Integer createTopic(TopicSaveDTO dto);

    /** 更新专题。 */
    void updateTopic(Integer id, TopicSaveDTO dto);

    /** 删除专题。 */
    void deleteTopic(Integer id);
}
