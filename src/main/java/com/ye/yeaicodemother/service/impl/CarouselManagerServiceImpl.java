package com.ye.yeaicodemother.service.impl;

import cn.hutool.core.lang.UUID;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.ye.yeaicodemother.exception.BusinessException;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.model.entity.CarouselManager;
import com.ye.yeaicodemother.mapper.CarouselManagerMapper;
import com.ye.yeaicodemother.service.CarouselManagerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


/**
 *  服务层实现。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */
@Service
public class CarouselManagerServiceImpl extends ServiceImpl<CarouselManagerMapper, CarouselManager>  implements CarouselManagerService{
    // 从配置文件读取目录
    @Value("${file.upload-dir}")
    private String uploadDir;
    // 从配置文件读取域名
    @Value("${file.domain}")
    private String domain;

    @Override
    public void upload(MultipartFile file, HttpServletRequest request) {
        //1.拿到上传文件的名字，并提取其后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        //2.加上uuid避免重复然后
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        //3.下载到指定的文件夹里
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs(); // 如果目录不存在，自动创建(包括多级父目录)
        }
        //4. 构建最终的绝对文件路径
        File destFile = new File(folder, newFileName);
        //将上传的临时文件放到指定文件夹中
        try{
            file.transferTo(destFile);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.CAROUSESLMANAGER_ERROR);
        }
        System.out.println("✅ 图片上传成功！已保存到物理路径: " + destFile.getAbsolutePath());
        //5.将轮播图的存储路径放到数据库imageUrl中方便之后去拿

        //6.将request的请求也放到数据库中

        //6.返回成功的提示
    }
}
