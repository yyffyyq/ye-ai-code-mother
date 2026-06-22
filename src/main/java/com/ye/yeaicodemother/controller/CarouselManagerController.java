package com.ye.yeaicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.ye.yeaicodemother.common.BaseResponse;
import com.ye.yeaicodemother.common.ResultUtils;
import com.ye.yeaicodemother.exception.BusinessException;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.exception.ThrowUtils;
import com.ye.yeaicodemother.model.dto.carouselManager.CarouselDescriptionDTO;
import com.ye.yeaicodemother.model.dto.carouselManager.CarouselManagerDto;
import com.ye.yeaicodemother.model.vo.Carousel.CarouselDescriptionAndImageUrlVO;
import com.ye.yeaicodemother.model.vo.CarouselManagerVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ye.yeaicodemother.model.entity.carousel.CarouselManager;
import com.ye.yeaicodemother.service.CarouselManagerService;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

/**
 *  控制层。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */
@RestController
@RequestMapping("/carouselManager")
public class CarouselManagerController {

    @Autowired
    private CarouselManagerService carouselManagerService;

    /// 这里增加一个获取轮播图list根据location_type的id
    @GetMapping("/getCarouselBylocationType/{location_type}")
    @Operation(summary = "根据位置获取轮播图列表", description = "传入位置类型的ID（例如：1-首页，2-副页），返回该位置下的所有轮播图数据")
    public BaseResponse<List<CarouselManagerVO>> getCarouselBylocationType(@PathVariable("location_type") Integer location_type) {
        //依旧先判断传入的值
        if(location_type==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求为空");
        }
        //调用Service服务
        //获取值Result并输出出去
        List<CarouselManagerVO> Result = carouselManagerService.selectByLocationType(location_type);
        //返回值用于回显
        return ResultUtils.success(Result);
    }
    /**
     * 上传轮播图。
     * @param file 轮播图名称
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @Operation(summary = "上传轮播图", description = "用于上传图片的接口，返回值为图片存储位置")
    public BaseResponse<String> upload(@RequestParam("file") MultipartFile file,
                                       HttpServletRequest request) {
        // 1. 简单的文件参数非空校验
        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR);
        //2.判断结束对Service类进行数据库操作
        String imageUrl = carouselManagerService.upload(file,request);
        return ResultUtils.success(imageUrl);
    }
    @PostMapping("saveCarousel")
    @Operation(summary = "保存信息如数据库", description = "将提交的数据表单保存到数据库中")
    public BaseResponse<Long> saveCarousel(@RequestBody CarouselManagerDto carouselManagerDto,HttpServletRequest request) {
        //1.先判断请求是否为空
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        //2.调用Service服务
        long Result = carouselManagerService.save_myself(carouselManagerDto);

        //3.返回成功值
        return ResultUtils.success(Result);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键删除", description = "用于根据图片id删除轮播图图片")
    public BaseResponse<String> remove(@PathVariable Long id) {
        //还是先判断是否为空值
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR);
        //调用服务去逻辑删除，并清空sortorder
        String Reulst = carouselManagerService.RemoveById(id);
        //返回前端值
        return ResultUtils.success(Reulst);
    }

    /**
     * 根据主键更新。
     *
     * @param carouselManagerDto
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @Operation(summary = "根据主键更新", description = "用于根据图片id修改轮播图图片")
    public BaseResponse<CarouselManagerVO> update(@RequestBody CarouselManagerDto carouselManagerDto,HttpServletRequest request) {
        //判断是否为空
        ThrowUtils.throwIf(carouselManagerDto == null, ErrorCode.PARAMS_ERROR);
        //去实现类
        CarouselManagerVO carouselManagerVO = carouselManagerService.updateCoarouselInfo(carouselManagerDto);
        //返回值
        return ResultUtils.success(carouselManagerVO);
    }


    /**
     * 根据主键获取。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键查询", description = "用于根据图片id查询对应轮播图图片")
    public CarouselManager getInfo(@PathVariable Long id) {
        return carouselManagerService.getById(id);
    }


    /**
     * 分页查询。
     * @param carouselManagerDto 请求参数
     * @return 分页返回对象
     */
    @PostMapping("/list/page/vo")
    @Operation(summary = "分页查询", description = "查几个几页怎么查根据发送的请求决定")
    public BaseResponse<Page<CarouselManagerVO>> ListCarouselManagerPageVo(@RequestBody CarouselManagerDto carouselManagerDto) {
        //先判断有是否为空请求
        ThrowUtils.throwIf(carouselManagerDto == null, ErrorCode.PARAMS_ERROR);
        //赋值pageNum和pageSize
        //这里是集成了/common/PageRequest.java
        /// 设置页号和每页数量
        long pageNum = carouselManagerDto.getPageNum();
        long pageSize = carouselManagerDto.getPageSize();
        /// 将第几页每页多少发出
        Page<CarouselManager> carouselManagerPage = carouselManagerService.page(Page.of(pageNum, pageSize),
                /// 查询请求
                carouselManagerService.getQueryWrapper(carouselManagerDto));

        //封装脱敏
        /// 固定写法
        Page<CarouselManagerVO> carouselManagerVOPage = new Page<>(pageNum, pageSize, carouselManagerPage.getTotalRow());
        /// 封装返回值
        List<CarouselManagerVO> carouselManagerVOList = carouselManagerService.getCarouselVoList(carouselManagerPage.getRecords());
        carouselManagerVOPage.setRecords(carouselManagerVOList);
        //返回数据
        return ResultUtils.success(carouselManagerVOPage);
    }

    @PostMapping("/batchUpdate/ByIdList")
    @Operation(summary = "批量更新", description = "批量更新传入数据sortorder")
    public BaseResponse<String> batchUpdateByIdList(@RequestBody List<Long> listId) {
        //判断是否为空传入的值
        ThrowUtils.throwIf(listId == null, ErrorCode.PARAMS_ERROR);
        // 批量更新数据库
        boolean success = carouselManagerService.updateBatchById(listId);

        if (success) {
            return ResultUtils.success("成功");
        } else {
            return ResultUtils.success("错误");
        }

    }


    @GetMapping("/getByLocationType/{carouselLocationType}")
    @Operation(summary = "不分页查询", description = "根据位置类型查询轮播图")
    public BaseResponse<List<CarouselDescriptionAndImageUrlVO>> getByLocationType(@PathVariable Integer carouselLocationType) {

        // 参数校验
        ThrowUtils.throwIf(carouselLocationType == null, ErrorCode.PARAMS_ERROR);

        // 调用 Service 层查询
        List<CarouselDescriptionAndImageUrlVO> carouselManagerVOList = carouselManagerService.getCarouselVoListByLocationTypeId(carouselLocationType);

        // 返回成功结果
        return ResultUtils.success(carouselManagerVOList);
    }

    /**
     * 这里需要一个接口来上传图片描述和活动时间设置
     */
    @PostMapping("/editDescription")
    @Operation(summary = "编辑图片描述和活动时间", description = "编辑图片描述和活动时间")
    public BaseResponse<Boolean> editDescription(@RequestBody CarouselDescriptionDTO carouselDescriptionDTO) {
        /// 参数校验
        ThrowUtils.throwIf(carouselDescriptionDTO == null, ErrorCode.PARAMS_ERROR);
        /// 创建方法去存入数据库
        boolean Result = carouselManagerService.setDescription(carouselDescriptionDTO);
        /// 返回成功
        return ResultUtils.success(Result);

    }

    /**
     * 封装返回用于回显描述
     */
    @GetMapping("/getByIdForDescription/{id}")
    @Operation(summary = "获取图片描述和时间", description = "用于数据回显")
    public BaseResponse<CarouselDescriptionAndImageUrlVO> getByIdForDescription(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR);
        CarouselDescriptionAndImageUrlVO Result = carouselManagerService.getByIdForDescription(id);
        return ResultUtils.success(Result);
    }
}
