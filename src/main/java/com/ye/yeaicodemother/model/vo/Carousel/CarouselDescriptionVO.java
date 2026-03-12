package com.ye.yeaicodemother.model.vo.Carousel;

import lombok.Data;

/**
 * 轮播图Description描述回显使用
 */
@Data
public class CarouselDescriptionVO {
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
