package com.ye.yeaicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.ye.yeaicodemother.common.BaseResponse;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.exception.ThrowUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ye.yeaicodemother.model.entity.CarouselManager;
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


    /**
     * 上传轮播图。
     * @param file 轮播图名称
     * @param request 轮播图状态设置请求
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 1. 简单的文件参数非空校验
        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR);
//        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        //2.判断结束对Service类进行数据库操作
        carouselManagerService.upload(file,request);
        return null;
    }

    /**
     * 保存。
     *
     * @param carouselManager 
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody CarouselManager carouselManager) {
        return carouselManagerService.save(carouselManager);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
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
    public boolean update(@RequestBody CarouselManager carouselManager) {
        return carouselManagerService.updateById(carouselManager);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<CarouselManager> list() {
        return carouselManagerService.list();
    }

    /**
     * 根据主键获取。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
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
    public Page<CarouselManager> page(Page<CarouselManager> page) {
        return carouselManagerService.page(page);
    }

}
