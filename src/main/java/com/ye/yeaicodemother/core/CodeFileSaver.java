package com.ye.yeaicodemother.core;



import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ye.yeaicodemother.ai.model.HtmlCodeResult;
import com.ye.yeaicodemother.ai.model.MultiFileCodeResult;
import com.ye.yeaicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 保持代码文件
 */
public class CodeFileSaver {
    //定义文件保存的根目录
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir")+"/tmp/code_output";

    /**
     * 保存HTML网页代码
     * @param htmlCodeResult
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult){
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        saveFile(baseDirPath,"index.html",htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 保存多文件网页代码
     * @param multiFileCodeResult
     * @return
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult multiFileCodeResult){
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        saveFile(baseDirPath,"index.html", multiFileCodeResult.getHtmlCode());
        saveFile(baseDirPath,"style.css",multiFileCodeResult.getCssCode());
        saveFile(baseDirPath,"script.js", multiFileCodeResult.getJsCode());
        return new File(baseDirPath);
    }


    /**
     * 构建文件的唯一路径（tmp/code_output/bizType_雪花ID）
     * @param bizType 代码生成的类型（html还是多文件）
     * @return
     */
    private static String buildUniqueDir(String bizType){
        //使用hutool工具来拼接并且设置成String类型返回
        String uniqueDirName = StrUtil.format("{}_{}",bizType,IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator+uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }
    /**
     * 保持单个文件
     * @param dirPath
     * @param filename
     * @param content
     */
    private static void saveFile(String dirPath,String filename,String content){
        String filePath = dirPath + File.separator+filename;
        FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
    }


}
