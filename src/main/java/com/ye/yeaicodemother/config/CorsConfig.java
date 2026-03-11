package com.ye.yeaicodemother.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域与静态资源映射配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // 从配置文件读取目录
    @Value("${file.upload-dir}")
    private String uploadDir;
    // 从配置文件读取域名
    @Value("${file.domain}")
    private String domain;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 放行哪些域名
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 解决新上传图片无法即时访问的问题
         * 将 /images/** 的请求映射到物理磁盘路径
         */
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:"+uploadDir+"/images/");
        ///临时文件夹路径映像
        registry.addResourceHandler("/tmp/**")
                .addResourceLocations("file:"+uploadDir+"/tmp/");
    }
}

