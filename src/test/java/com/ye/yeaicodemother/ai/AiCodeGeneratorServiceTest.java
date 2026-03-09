package com.ye.yeaicodemother.ai;

import com.ye.yeaicodemother.ai.model.HtmlCodeResult;
import com.ye.yeaicodemother.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiCodeGeneratorServiceTest {
    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode(){
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("做一个欢迎页面，不超过20行");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateSystemCode(){
        MultiFileCodeResult result = aiCodeGeneratorService.generateSystemCode("做一个评论区，不超过50行");
        Assertions.assertNotNull(result);
    }
}