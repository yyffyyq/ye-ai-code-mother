package com.ye.yeaicodemother.core.parser;

import com.ye.yeaicodemother.exception.BusinessException;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.model.enums.CodeGenTypeEnum;

/**
 * 代码解析器的执行器
 * 根据代码生成类执行相应的解析逻辑
 *
 * tip：在没有创建这个之前，我只能用@Resource的注释方法来是创建接口实现的那种，
 * 现在有了这个应该就可以直接通过判断类型直接调用了
 *
 */
public class CodeParserExecutor {

    private static final HtmlCodeParser htmlParser = new HtmlCodeParser();
    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();



    /**
     * 执行代码解析
     * @param codeContent 代码内容
     * @param codeGenTypeEnum 代码生成类型
     * @return 解析结果（HtmlCodeResult 或者 MultiFileCodeResult）
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum){
        return switch (codeGenTypeEnum){
            case HTML-> htmlParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持这个类型的代码生成");
        };
    }
}
