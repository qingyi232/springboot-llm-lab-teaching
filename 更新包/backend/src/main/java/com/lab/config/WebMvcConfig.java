package com.lab.config;

import com.lab.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/users/login", "/users/register",
                        "/student/login", "/student/register",
                        "/teacher/login", "/teacher/register",
                        "/labAdmin/login", "/labAdmin/register",
                        "/file/**", "/uploads/**",
                        "/notice/list", "/notice/detail/**",
                        "/labInfo/list", "/labInfo/detail/**",
                        "/labType/list",
                        "/courseType/list",
                        "/courseMaterial/front/list", "/courseMaterial/detail/**",
                        "/video/front/list", "/video/detail/**",
                        "/forum/list", "/forum/detail/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absPath = new java.io.File(uploadPath).getAbsolutePath();
        if (!absPath.endsWith(java.io.File.separator)) {
            absPath += java.io.File.separator;
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absPath);
    }
}
