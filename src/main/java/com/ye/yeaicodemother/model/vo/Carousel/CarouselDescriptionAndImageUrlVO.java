package com.ye.yeaicodemother.model.vo.Carousel;

import com.mybatisflex.annotation.Column;
import lombok.Data;

@Data
public class CarouselDescriptionAndImageUrlVO {
    /**
     * 把id也拿过来
     */
    private Long id;
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
    /**
     * 描述内容
     */
    private String Description;

    /**
     * 描述活动时间
     */
    private String DescriptionTime;
    /**
     * 图片超链接
     */
    private String hrefUrl;
}
