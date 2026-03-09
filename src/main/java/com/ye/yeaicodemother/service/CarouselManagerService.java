package com.ye.yeaicodemother.service;

import com.mybatisflex.core.service.IService;
import com.ye.yeaicodemother.model.entity.CarouselManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 *  服务层。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */
public interface CarouselManagerService extends IService<CarouselManager> {

    /**
     * 轮播图上传抽象方法
     * @param file 轮播图名称
     * @param request 轮播图状态请求体
     */
    void upload(MultipartFile file, HttpServletRequest request);
}
