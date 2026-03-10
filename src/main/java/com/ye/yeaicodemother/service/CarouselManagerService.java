package com.ye.yeaicodemother.service;

import com.mybatisflex.core.service.IService;
import com.ye.yeaicodemother.model.dto.carouselManager.CarouselManagerDto;
import com.ye.yeaicodemother.model.entity.CarouselManager;
import com.ye.yeaicodemother.model.vo.CarouselManagerVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 *  服务层。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */
public interface CarouselManagerService extends IService<CarouselManager> {

    /**
     * 轮播图上传抽象方法
     * @param file 轮播图名称
     * @param request
     */
    String upload(MultipartFile file, HttpServletRequest request);



    /**
     * 将图片上传信息保存到数据库中
     * @param carouselManagerDto 轮播图信息
     * @return
     */
    long save_myself(CarouselManagerDto carouselManagerDto);

    /**
     * 获取轮播图信息通过轮播图位置id
     * @param locationType
     * @return
     */
    List<CarouselManagerVO> selectByLocationType(Integer locationType);
}
