package com.ye.yeaicodemother.model.vo.Carousel;

import com.mybatisflex.annotation.Column;
import lombok.Data;

/**
 * 用于分装返回批量修改之后的值
 */
@Data
public class CarouselBatchupByIdVO {
    /**
     * 轮播图编号 (主键)
     */
    private Long id;
    /**
     * 播放排序 (数字越小越靠前，控制轮播顺序)
     */
    @Column("sort_order")
    private Integer displayOrder;
}
