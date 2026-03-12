package com.ye.yeaicodemother.model.dto.carouselManager;

import lombok.Data;

@Data
public class CarouselDescriptionDTO {

    /**
     * 图片id
     */
    private Long id;

    /**
     * 图片描述
     */
    private String Description;

    /**
     * 活动时间描述
     */
    private String DescriptionTime;
    /**
     * 图片超链接
     */
    private String hrefUrl;
}
