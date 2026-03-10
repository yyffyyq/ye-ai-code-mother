package com.ye.yeaicodemother.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  实体类。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("carousel_manager")
public class CarouselManager implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 轮播图编号 (主键)
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 图片地址/路径 (前端展示必用)
     */
    private String imageUrl;

    /**
     * 展示位置: 1-首页, 2-副页, 3-其他
     */
    private Integer locationType;

    /**
     * 播放排序 (数字越小越靠前，控制轮播顺序)
     */
    private Integer sortOrder;

    /**
     * 审核状态: 0-待审核, 1-已通过, 2-已驳回
     */
    private Integer auditStatus;

    /**
     * 上传时间 (系统自动生成)
     */
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间 (系统自动更新)
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除: 0-正常, 1-已删除
     */
    private Integer isDeleted;

}
