package com.ye.yeaicodemother.model.vo;

import lombok.Data;

/**
 * 作为Carousel轮播图被查询之后返回值的封装
 */
@Data
public class CarouselManagerVO {
    /**
     * 图片地址/路径 (前端展示必用)
     */
    private String imageUrl;
    /**
     * 播放排序 (数字越小越靠前，控制轮播顺序)
     */
    private Integer sortOrder;
    /**
     * 逻辑删除: 0-正常, 1-已删除
     */
    private Integer isDeleted;
}
