package com.ye.yeaicodemother.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.ye.yeaicodemother.exception.BusinessException;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.model.dto.carouselManager.CarouselManagerDto;
import com.ye.yeaicodemother.model.entity.CarouselManager;
import com.ye.yeaicodemother.mapper.CarouselManagerMapper;
import com.ye.yeaicodemother.service.CarouselManagerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 *  服务层实现。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */
@Service
public class CarouselManagerServiceImpl extends ServiceImpl<CarouselManagerMapper, CarouselManager>  implements CarouselManagerService{
    // 从配置文件读取目录
    @Value("${file.upload-dir}")
    private String uploadDir;
    // 从配置文件读取域名
    @Value("${file.domain}")
    private String domain;

    @Override
    public File upload(MultipartFile file, HttpServletRequest request) {
        //1.拿到上传文件的名字，并提取其后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        ///==============================做一个文件上传判断,限制只能上传图片格式文件====================================
        /// 做一个列表放入图片格式类型后缀
        List<String> validImageSuffixes = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp");
        /// 对上传图片后缀提取并判断
        if (!validImageSuffixes.contains(suffix)) {
            // 后缀不在允许的列表中，直接抛出异常拦截
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只允许上传图片格式的文件");
        }


        //2.加上uuid避免重复然后
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        //3.下载到指定的文件夹里
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs(); // 如果目录不存在，自动创建(包括多级父目录)
        }
        //4. 构建最终的绝对文件路径
        File destFile = new File(folder, newFileName);
        //将上传的临时文件放到指定文件夹中
        try{
            file.transferTo(destFile);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.CAROUSESLMANAGER_ERROR);
        }
        //6.返回成功的提示,并返回存储路径用户在填写信息的时候可以看到图片
        return destFile;
    }

    @Override
    public long save_myself(CarouselManagerDto carouselManagerDto) {
        //获取轮播图的参数
        String imageUrl = carouselManagerDto.getImageUrl();
        Integer locationType = carouselManagerDto.getLocationType();
        Integer sortOrder = carouselManagerDto.getSortOrder();
        //校验参数是否为空
        if(StrUtil.hasBlank(imageUrl)||locationType==null||sortOrder==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //查询设置的locationType和sortOrder是否与其他的有冲突，
        //注意：这个需要locationType和sortOrder合在一起查询
        // 使用 Lambda 表达式防误写字段名，组合查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(CarouselManager::getLocationType).eq(locationType)
                .and(CarouselManager::getSortOrder).eq(sortOrder);
        //调用 IService 提供的 count 方法统计符合条件的记录数
        long count = this.count(queryWrapper);
        //判断冲突
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该位置的排序序号已被占用，请更换排序或位置");
        }
        //判断之后将数据存储到数据库中
        CarouselManager carouselManager = new CarouselManager();
        carouselManager.setImageUrl(imageUrl);
        carouselManager.setLocationType(locationType);
        carouselManager.setSortOrder(sortOrder);
        boolean saveResult = this.save(carouselManager);
        if(!saveResult){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"保存失败，数据库错误");
        }
        return carouselManager.getId();
    }
}
