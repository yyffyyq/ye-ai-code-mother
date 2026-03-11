package com.ye.yeaicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.ye.yeaicodemother.common.BaseResponse;
import com.ye.yeaicodemother.common.ResultUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.ye.yeaicodemother.model.entity.carousel.CarouselLocation;
import com.ye.yeaicodemother.service.CarouselLocationService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 轮播图位置管理部分
 * 控制层。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */

//TODO 这里我需要再做一个查询locationType的判断，在轮播图中设置
@RestController
@RequestMapping("/carouselLocation")
public class CarouselLocationController {

    @Autowired
    private CarouselLocationService carouselLocationService;

    /**
     * 保存。
     *
     * @param carouselLocation 
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody CarouselLocation carouselLocation) {
        return carouselLocationService.save(carouselLocation);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Integer id) {
        return carouselLocationService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param carouselLocation 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody CarouselLocation carouselLocation) {
        return carouselLocationService.updateById(carouselLocation);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public BaseResponse<List<CarouselLocation>> list() {
        List<CarouselLocation> Result =  carouselLocationService.list();
        return ResultUtils.success(Result);
    }

    /**
     * 根据主键获取。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public CarouselLocation getInfo(@PathVariable Integer id) {
        return carouselLocationService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<CarouselLocation> page(Page<CarouselLocation> page) {
        return carouselLocationService.page(page);
    }

}
