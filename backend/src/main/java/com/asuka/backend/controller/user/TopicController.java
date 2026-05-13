package com.asuka.backend.controller.user;

import com.asuka.backend.pojo.entity.Topic;
import com.asuka.backend.pojo.vo.TopicVO;
import com.asuka.backend.result.Result;
import com.asuka.backend.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@Slf4j
@Tag(name = "专题管理")
@RequestMapping("/user/topics")
public class TopicController {
    @Autowired
    private TopicService topicService;


    @GetMapping("/list")
    @Operation(summary = "获取所有专题")
    public Result getAll() {
        log.info("获取所有专题");
        ArrayList<TopicVO> list = topicService.getAll();
        return Result.success(list);
    }
}
