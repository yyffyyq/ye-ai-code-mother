package com.ye.yeaicodemother.core;

import com.ye.yeaicodemother.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generatorAndSaveCode(){
        File file = aiCodeGeneratorFacade.generatorAndSaveCode("生成一个登录页面,一共不超过20行代码", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull((file));
    }


    @Test
    void generatorAndSaveCodeStream() {
        Flux<String> codeSteam =  aiCodeGeneratorFacade.generatorAndSaveCodeStream("生成一个登录页面,一共不超过30行代码,并且给我做一个简单的登录测试，用户admin，密码123456", CodeGenTypeEnum.MULTI_FILE);
        //阻塞等待所有数据收集完成
        List<String> result = codeSteam.collectList().block();
        //验证结果
        Assertions.assertNotNull(result);
        //拼接字符串，实现完整内容
        String completeContent = String.join("",result);
        Assertions.assertNotNull(completeContent);
    }
}