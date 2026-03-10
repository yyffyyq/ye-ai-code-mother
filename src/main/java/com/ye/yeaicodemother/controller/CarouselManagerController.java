package com.ye.yeaicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.ye.yeaicodemother.common.BaseResponse;
import com.ye.yeaicodemother.common.ResultUtils;
import com.ye.yeaicodemother.exception.BusinessException;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.exception.ThrowUtils;
import com.ye.yeaicodemother.model.dto.carouselManager.CarouselManagerDto;
import com.ye.yeaicodemother.model.vo.CarouselManagerVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ye.yeaicodemother.model.entity.CarouselManager;
import com.ye.yeaicodemother.service.CarouselManagerService;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
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
    public boolean remove(@PathVariable Long id) {
        return carouselManagerService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param carouselManager 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @Operation(summary = "根据主键更新", description = "用于根据图片id修改轮播图图片")
    public boolean update(@RequestBody CarouselManager carouselManager) {
        return carouselManagerService.updateById(carouselManager);
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
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    @Operation(summary = "分页查询", description = "   ")
    public Page<CarouselManager> page(Page<CarouselManager> page) {
        return carouselManagerService.page(page);
    }
}
