package com.ye.yeaicodemother.service.impl;

import ch.qos.logback.core.spi.ErrorCodes;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.ye.yeaicodemother.exception.BusinessException;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.model.dto.carouselManager.CarouselManagerDto;
import com.ye.yeaicodemother.model.entity.CarouselManager;
import com.ye.yeaicodemother.mapper.CarouselManagerMapper;
import com.ye.yeaicodemother.model.vo.CarouselManagerVO;
import com.ye.yeaicodemother.service.CarouselManagerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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
    @Autowired
    private CarouselManagerMapper carouselManagerMapper;

    /**
     * 图片上传功能
     * @param file 轮播图名称
     * @param request
     * @return
     */
    @Override
    public String upload(MultipartFile file, HttpServletRequest request) {
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
        File folder = new File(uploadDir+"/tmp/");
        if (!folder.exists()) {
            folder.mkdirs(); // 如果目录不存在，自动创建(包括多级父目录)
        }
        //4. 构建最终的绝对文件路径
        File destFile = new File(folder, newFileName);
        // 5.将上传的临时文件放到指定文件夹中
        try{
            file.transferTo(destFile);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.CAROUSESLMANAGER_ERROR);
        }
        //6.返回成功的提示,并返回存储路径用户在填写信息的时候可以看到图片
        return "/tmp/" + newFileName;
    }

    @Override
    public long save_myself(CarouselManagerDto carouselManagerDto) {
        //获取轮播图的参数
        String imageUrl = carouselManagerDto.getImageUrl();
        Integer locationType = carouselManagerDto.getLocationType();
        Integer sortOrder = carouselManagerDto.getDisplayOrder();
        //校验参数是否为空
        if(StrUtil.hasBlank(imageUrl)||locationType==null||sortOrder==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //查询设置的locationType和sortOrder是否与其他的有冲突，
        //注意：这个需要locationType和sortOrder合在一起查询
        // 使用 Lambda 表达式防误写字段名，组合查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(CarouselManager::getLocationType).eq(locationType)
                .and(CarouselManager::getDisplayOrder).eq(sortOrder);
        //调用 IService 提供的 count 方法统计符合条件的记录数
        long count = this.count(queryWrapper);
        //判断冲突
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该位置的排序序号已被占用，请更换排序或位置");
        }
        if (imageUrl != null && imageUrl.startsWith("/tmp/")) {
            // 提取纯文件名，例如 "tmp/abc.jpg" 提取出 "abc.jpg"
            String fileName = imageUrl.replace("/tmp/", "");
            Path sourcePath = Paths.get(uploadDir, "tmp", fileName);
            Path targetPath = Paths.get(uploadDir, "images", fileName);
            try {
                // 如果 images 文件夹不存在，先创建它
                if (!Files.exists(targetPath.getParent())) {
                    Files.createDirectories(targetPath.getParent());
                }

                // 将文件从 tmp 移动到 images 目录下 (REPLACE_EXISTING 表示如果同名就覆盖)
                if (Files.exists(sourcePath)) {
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    // 移动成功后，把准备存入数据库的路径更新为正式路径
                    imageUrl = "/images/" + fileName;
                } else {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "临时文件不存在，请重新上传图片");
                }
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片保存失败，服务器文件操作异常");
            }
        }
        //判断之后将数据存储到数据库中
        CarouselManager carouselManager = new CarouselManager();
        carouselManager.setImageUrl(imageUrl);
        carouselManager.setLocationType(locationType);
        carouselManager.setDisplayOrder(sortOrder);
        boolean saveResult = this.save(carouselManager);
        if(!saveResult){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"保存失败，数据库错误");
        }
        return carouselManager.getId();
    }

    /**
     * 获取轮播图通过位置信息
     * @param locationType 轮播图位置id
     * @return
     */
    @Override
    public List<CarouselManagerVO> selectByLocationType(Integer locationType) {
        //构建查询语句 where location_type = ?
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(CarouselManager::getLocationType).eq(locationType)
                .orderBy(CarouselManager::getDisplayOrder).asc();;
        //查询并返回
        /// 直接就把获取到的值赋值给封装方法
        return carouselManagerMapper.selectListByQueryAs(queryWrapper,CarouselManagerVO.class);
    }

    /**
     * 分页查询所有数据
     * @param carouselManagerDto
     * @return
     */
    @Override
    public QueryWrapper getQueryWrapper(CarouselManagerDto carouselManagerDto) {
        ///判断请求是否为空
        if(carouselManagerDto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        ///取值用于判断，为之后拓展where查询准备
        Long id = carouselManagerDto.getId();
        String imageUrl = carouselManagerDto.getImageUrl();
        Integer locationType = carouselManagerDto.getLocationType();
        ///创建sql对话框
        QueryWrapper queryWrapper = QueryWrapper.create();
        /// 判断id是否为空，不查询id
        if(id!=null){
            queryWrapper.eq(CarouselManager::getId,id);
        }
        /// 判断imageUrl是否为空，不查询imageUrl
        if(StrUtil.isNotBlank(imageUrl)){
            queryWrapper.like(CarouselManager::getImageUrl,imageUrl);
        }
        /// 判断locationType是否为空，不查询locationType
        if(locationType!=null){
            queryWrapper.eq(CarouselManager::getLocationType,locationType);
        }
        /// 最后按照权重排序返回
        queryWrapper.orderBy(CarouselManager::getDisplayOrder).asc();
        /// 返回数据
        return queryWrapper;
    }

    /**
     * 封装
     * @param records 需要封装值
     * @return
     */
    @Override
    public List<CarouselManagerVO> getCarouselVoList(List<CarouselManager> records) {
        /// 判断请求封装的值是否为空
        if(CollUtil.isEmpty(records)){
            /// 为空就返回空列表
            return new ArrayList<>();
        }
        /// 返回封装通过方法getCarouselVo()
        // 这个是lamda表达式
        return records.stream()
                .map(this::getCarouselVo)
                .collect(Collectors.toList());
    }

    @Override
    public CarouselManagerVO getCarouselVo(CarouselManager carouselManager) {
        //判断参数是否为空
        if(carouselManager == null){
            return null;
        }
        /// 创建封装对象
        CarouselManagerVO carouselManagerVO = new CarouselManagerVO();
        /// 封装
        BeanUtils.copyProperties(carouselManager,carouselManagerVO);
        /// 返回VO值
        return carouselManagerVO;
    }

    /**
     * 修改轮播图信息
     * @param carouselManagerDto
     * @return
     */
    @Override
    public CarouselManagerVO updateCoarouselInfo(CarouselManagerDto carouselManagerDto) {
        //将值提取出来用于后续的sql语句
        Long id = carouselManagerDto.getId();
        String imageUrl = carouselManagerDto.getImageUrl();
        Integer displayOrder = carouselManagerDto.getDisplayOrder();
        if(id==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"缺少目标");
        }
        if (imageUrl != null && imageUrl.startsWith("/tmp/")) {
            // 提取纯文件名，例如 "tmp/abc.jpg" 提取出 "abc.jpg"
            String fileName = imageUrl.replace("/tmp/", "");
            Path sourcePath = Paths.get(uploadDir, "tmp", fileName);
            Path targetPath = Paths.get(uploadDir, "images", fileName);
            try {
                // 如果 images 文件夹不存在，先创建它
                if (!Files.exists(targetPath.getParent())) {
                    Files.createDirectories(targetPath.getParent());
                }

                // 将文件从 tmp 移动到 images 目录下 (REPLACE_EXISTING 表示如果同名就覆盖)
                if (Files.exists(sourcePath)) {
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    // 移动成功后，把准备存入数据库的路径更新为正式路径
                    imageUrl = "/images/" + fileName;
                } else {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "临时文件不存在，请重新上传图片");
                }
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片保存失败，服务器文件操作异常");
            }
        }
        //判断哪些值为空，之后对对应的值进行sql的alt修改
        UpdateChain.of(CarouselManager.class)
                .set(CarouselManager::getImageUrl, imageUrl)
                .set(CarouselManager::getDisplayOrder, displayOrder)
                .where(CarouselManager::getId).eq(id)
                .update();
        //封装值
        CarouselManagerVO carouselManagerVO = new CarouselManagerVO();
        BeanUtils.copyProperties(carouselManagerDto,carouselManagerVO);
        //返回值
        return carouselManagerVO;
    }

    /**
     * 逻辑删除轮播图
     * @param id 需要删除的图片id
     * @return
     */
    @Override
    public String RemoveById(Long id) {
        try{
            //直接调用方法删除
            UpdateChain.of(CarouselManager.class)
                    .set(CarouselManager::getDisplayOrder, -1)
                    .set(CarouselManager::getIsDeleted, 1)
                    .set(CarouselManager::getUpdateTime, LocalDateTime.now())
                    .where(CarouselManager::getId).eq(id)
                    .update();
            return "删除成功";
        }catch (BusinessException e){
            return "删除失败："+e.getMessage();
        }
    }
}
