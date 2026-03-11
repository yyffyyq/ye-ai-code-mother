package com.ye.yeaicodemother.model.vo;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 作为Carousel轮播图被查询之后返回值的封装
 */
@Data
public class CarouselManagerVO {
    /**
     * 轮播图编号 (主键)
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
     * 上传时间 (系统自动生成)
     */
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间 (系统自动更新)
     */
    private LocalDateTime updateTime;
}
