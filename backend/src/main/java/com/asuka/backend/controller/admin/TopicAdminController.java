package com.asuka.backend.controller.admin;

import com.asuka.backend.pojo.dto.TopicSaveDTO;
import com.asuka.backend.result.Result;
import com.asuka.backend.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/topics")
@Slf4j
@Tag(name = "管理端专题管理")
public class TopicAdminController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/list")
    @Operation(summary = "获取所有专题")
    public Result list() {
        return Result.success(topicService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取专题详情")
    public Result detail(@PathVariable Integer id) {
        return Result.success(topicService.getTopicById(id));
    }

    @PostMapping
    @Operation(summary = "新增专题")
    public Result create(@RequestBody TopicSaveDTO dto) {
        Integer id = topicService.createTopic(dto);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新专题")
    public Result update(@PathVariable Integer id, @RequestBody TopicSaveDTO dto) {
        topicService.updateTopic(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除专题")
    public Result delete(@PathVariable Integer id) {
        topicService.deleteTopic(id);
        return Result.success();
    }
}
