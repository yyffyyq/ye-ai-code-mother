package com.ye.yeaicodemother.ai.model;


import jdk.jfr.Description;
import lombok.Data;

/**
 * 多文件代码结果
 */
@Description("生成多文件代码文件的结果")
@Data
public class MultiFileCodeResult {

    /**
     * html代码
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * css代码
     */
    @Description("CSS代码")
    private String cssCode;

    /**
     * JS代码
     */
    @Description("JS代码")
    private String jsCode;

    /**
     * 描述
     */
    @Description("生成代码描述")
    private String description;
}
