package com.ye.yeaicodemother.model.entity.carousel;

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
@Table("carousel_location")
public class CarouselLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 位置ID (主鍵)
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 位置名稱 (例如：首頁、副頁、其他)
     */
    private String name;

    /**
     * 位置描述
     */
    private String description;

    /**
     * 創建時間
     */
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    private LocalDateTime updateTime;

}
