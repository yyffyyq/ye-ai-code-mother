package com.ye.yeaicodemother.model.vo;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarouseLocationTypeVO {
    /**
     * 位置名稱 (例如：首頁、副頁、其他)
     */
    private String name;

    /**
     * 位置描述
     */
    private String description;
}
