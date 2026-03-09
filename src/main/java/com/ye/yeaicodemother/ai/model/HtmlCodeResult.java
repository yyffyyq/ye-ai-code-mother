package com.ye.yeaicodemother.ai.model;

import jdk.jfr.Description;
import lombok.Data;

/**
 * HTML代码结果
 */
@Description("生成HTML代码文件的结果")
@Data
public class HtmlCodeResult {
    /*
    HTML 代码部分
     */
    //添加注解可以让模型更清楚这个东西怎么用
    @Description("HTML代码")
    private String htmlCode;

    /*
    描述
     */
    @Description("生成代码描述")
    private String description;
}
