package com.ye.yeaicodemother.model.dto.carouselManager;

import lombok.Data;

/**
 * 轮播图新增/修改 传输对象 (前端 -> 后端)
 */

/**
 * 轮播图发送参数：
 * id 轮播图编号
 * imageUrl 轮播图图片路径
 * locationType 轮播图位置例如(首页、副页)
 * sortOrder 轮播图轮播前后，字数越小越靠前
 */
@Data
public class CarouselManagerDto {

    /**
     * 轮播图编号 (新增时为空，修改时必填)
     */
    private Long id;

    /**
     * 图片地址/路径
     */
    private String imageUrl;

    /**
     * 展示位置: 1-首页, 2-副页, 3-其他
     */
    private Integer locationType;

    /**
     * 播放排序 (可选，不传默认排在最后)
     */
    private Integer sortOrder;

    // 注意：审核状态、删除状态、时间等字段通常不由前端提交，而是后端业务逻辑控制，所以不放在 DTO 中。
}
