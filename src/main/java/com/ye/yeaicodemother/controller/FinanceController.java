package com.ye.yeaicodemother.controller;

import com.ye.yeaicodemother.annotation.AuthCheck;
import com.ye.yeaicodemother.common.BaseResponse;
import com.ye.yeaicodemother.common.ResultUtils;
import com.ye.yeaicodemother.constant.UserConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人事财务计算公司内部员工工资相关代码业务逻辑区域
 */
@RestController
@RequestMapping("/finance")
public class FinanceController {

    @GetMapping("/")
    @AuthCheck(mustRole = UserConstant.FINACION_ROLE)
    public BaseResponse<String> healthCheck(){
        return ResultUtils.success("财务权限显示成功");
    }
}
