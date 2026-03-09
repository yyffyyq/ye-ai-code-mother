package com.ye.yeaicodemother.core;

import com.ye.yeaicodemother.ai.AiCodeGeneratorService;
import com.ye.yeaicodemother.ai.model.HtmlCodeResult;
import com.ye.yeaicodemother.ai.model.MultiFileCodeResult;
import com.ye.yeaicodemother.core.parser.CodeParser;
import com.ye.yeaicodemother.exception.BusinessException;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * 代码生成门面类，组合代码生成和保持功能
 */
@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    //创建一个HTML的Parser解析器
    @Resource
    private CodeParser<HtmlCodeResult> htmlCodeResultCodeParser;

    //创建一个多文件的Parser解析器
    @Resource
    private CodeParser<MultiFileCodeResult> multiFileCodeResultCodeParser;


    /**
     * 统一入口：根据类型生成并保存代码
     * @param userMessage 用户提示词
     * @param codeGenTypeEnum 生成代码类型
     * @return
     */
    public File generatorAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum){
        if(codeGenTypeEnum == null){
            //报错，生成类型种类缺失
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return switch (codeGenTypeEnum){
            case HTML -> generateAndSaveHtmlCode(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCode(userMessage);
            default ->{
                String errorMessage = "不支持的生成类型："+codeGenTypeEnum.getValue();
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,errorMessage);
                }
            };
    }

    /**
     * 统一入口：根据类型生成并保存代码(流式)
     * @param userMessage 用户提示词
     * @param codeGenTypeEnum 生成代码类型
     * @return
     */
    public Flux<String> generatorAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum){
        if(codeGenTypeEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return switch (codeGenTypeEnum){
            case HTML -> generateAndSaveHtmlCodeStream(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCodeStream(userMessage);
            default ->{
                String errorMessage = "不支持的生成类型："+codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.PARAMS_ERROR,errorMessage);
            }
        };
    }
    /**
     * 生成HTML模式的代码保存（流式）
     * @param userMessage 用户提示词
     * @return 返回目录
     */
    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
        //调用大模型
        //返回结果
        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        //定义一个字符串拼接器，用于当流式传输返回所有的代码之后，再保存
        StringBuilder codeBuilder = new StringBuilder();
        return result.doOnNext(chunk -> {
            //实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(()->{
            try{
                //流式返回完成后，保存代码
                String completeHtmlCode = codeBuilder.toString();
                //解析代码为对象
                HtmlCodeResult htmlCodeResult = htmlCodeResultCodeParser.parseCode(completeHtmlCode);
                //保存代码到文件
                File saveDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                log.info("保存成功，目录为：{}",saveDir.getAbsolutePath());
            }
            catch (Exception e){
                log.info("保存代码失败：{}",e.getMessage());
            }
        });
    }

    /**
     * 生成多文件模式的代码保存（流式）
     * @param userMessage 用户提示词
     * @return 返回目录
     */
    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        //调用大模型,返回结果
        Flux<String> result = aiCodeGeneratorService.generateSystemCodeStream(userMessage);
        //定义一个字符串拼接器，用于当流式传输返回所有的代码之后，再保存
        StringBuilder codeBuilder = new StringBuilder();
        return result.doOnNext(chunk -> {
            //实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(()->{
            try{
                //流式返回完成后，保存代码
                String completeMultiFiles = codeBuilder.toString();
                //解析代码为对象
                MultiFileCodeResult multiFileCodeResult = multiFileCodeResultCodeParser.parseCode(completeMultiFiles);
                //保存代码到文件
                File saveDir = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
                log.info("文件创建完成，目录为：{}",saveDir.getAbsolutePath());
            }
            catch (Exception e){
                log.info("保存失败:{}",e.getMessage());
            }
        });
    }



    /**
     * 生成HTML 模式的代码并保存
     * @param userMessage 用户提示词
     * @return 返回的目录
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
    }

    /**
     * 生成多文件模式的代码并保存
     * @param userMessage 用户提示词
     * @return 保存目录
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult result = aiCodeGeneratorService.generateSystemCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(result);
    }

}
