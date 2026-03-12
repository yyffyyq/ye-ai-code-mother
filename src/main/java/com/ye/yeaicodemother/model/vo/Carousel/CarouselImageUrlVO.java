package com.ye.yeaicodemother.model.vo.Carousel;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
public class CarouselImageUrlVO {


    /**
     * 图片地址/路径 (前端展示必用)
     */
    private String imageUrl;


    /**
     * 播放排序 (数字越小越靠前，控制轮播顺序)
     */
    @Column("sort_order")
    private Integer displayOrder;

    /**
     * 逻辑删除: 0-正常, 1-已删除
     */
    private Integer isDeleted;

}
