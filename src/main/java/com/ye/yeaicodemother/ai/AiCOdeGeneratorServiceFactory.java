package com.ye.yeaicodemother.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 服务创建工厂
 */
@Configuration
public class AiCOdeGeneratorServiceFactory {

    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    /**
     * 创建AI代码生产服务
     * @return
     */
    /**
     * 整理一下这个运行逻辑
     * 1、是@Resource这个AiCodeGeneratorService的类
     * 2、调用这个资源的时候调用它的抽象方法
     * 3、调用这个抽象方法的时候会经过这个Factory类
     * 4、在这个Factory类里定义了这个抽象类
     * 5、这个LangChain4j的框架会根据我这个写入的class去扫描接口
     * 6、然后就到接口看看是怎么实现的
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService(){
        return AiServices.builder(AiCodeGeneratorService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}
