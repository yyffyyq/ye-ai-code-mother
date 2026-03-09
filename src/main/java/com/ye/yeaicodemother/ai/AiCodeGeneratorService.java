package com.ye.yeaicodemother.ai;

import com.ye.yeaicodemother.ai.model.HtmlCodeResult;
import com.ye.yeaicodemother.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;


/**
 * 具体实现流程：
 * 1、在Factory里的扫描什么的都结束之后到这里看具体实现
 * 2、调用了这个接口的抽象方法之后，框架支持的，显示扫描我提前准备的提示词Prompt的内容
 * 3、然后直接写抽象方法generateHtmlCode，它会自己生成大模型返回的东西
 * 总结：这就是LangChain4j框架
 */
public interface AiCodeGeneratorService {


    /**
     * 生成代码
     * @param userMessage 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(value = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);


    /**
     * 生成多文件代码
     * @param userMessage 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(value = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateSystemCode(String userMessage);


    /**
     *
     */

    /**
     * 生成代码-流式输出
     * @param userMessage 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(value = "prompt/codegen-multi-file-system-prompttxt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    /**
     * 生成多文件代码-流式输出
     * @param userMessage 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(value = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateSystemCodeStream(String userMessage);

}
