package com.ye.yeaicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.ye.yeaicodemother.model.dto.carouselManager.CarouselDescriptionDTO;
import com.ye.yeaicodemother.model.dto.carouselManager.CarouselManagerDto;
import com.ye.yeaicodemother.model.entity.CarouselManager;
import com.ye.yeaicodemother.model.vo.Carousel.CarouselDescriptionAndImageUrlVO;
import com.ye.yeaicodemother.model.vo.Carousel.CarouselDescriptionVO;
import com.ye.yeaicodemother.model.vo.Carousel.CarouselImageUrlVO;
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


    /**
     * 分页查询所有数据
     * @param carouselManagerDto 请求体
     * @return 返回值CarouselManage类型
     */
    QueryWrapper getQueryWrapper(CarouselManagerDto carouselManagerDto);

    /**
     * 对返回前端的值进行封装
     * @param records 需要封装值
     * @return 返回值类型为List<CarouselManagerVO>列表</>
     */
    List<CarouselManagerVO> getCarouselVoList(List<CarouselManager> records);

    /**
     * 一个一个封装的方法
     * @param carouselManager
     * @return
     */
    CarouselManagerVO getCarouselVo(CarouselManager carouselManager);

    /**
     * 根据轮播图id修改
     * @param carouselManagerDto
     * @return
     */
    CarouselManagerVO updateCoarouselInfo(CarouselManagerDto carouselManagerDto);

    /**
     * 根据用户id进行逻辑删除
     * 清空sortorder避免影响需要上传的图片
     * @param id 需要删除的图片id
     * @return
     */
    String RemoveById(Long id);

    /**
     * 获取最大的sortorder的值
     * @return
     */
    Integer MaxSortOrderNumber(Integer locationType);

    /**
     * // 批量更新数据库
     * @param listId
     * @return
     */
    boolean updateBatchById(List<Long> listId);

    /**
     * 通过位置localtionID查询并封装
     * @param carouselLocationType 轮播图位置参数
     * @return
     */
    List<CarouselDescriptionAndImageUrlVO> getCarouselVoListByLocationTypeId(Integer carouselLocationType);

    /**
     * 将描述存入数据库
     * @param carouselDescriptionDTO 描述的值
     * @return
     */
    boolean setDescription(CarouselDescriptionDTO carouselDescriptionDTO);

    /**
     * 获取图片描述
     * @param id 图片id
     * @return
     */
    CarouselDescriptionVO getByIdForDescription(Long id);
}
