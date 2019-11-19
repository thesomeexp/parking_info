package com.someexp.parking_info.config;

import com.someexp.parking_info.interceptor.CSRFInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Bean
    public CSRFInterceptor getCSRFInterceptor() {
        return new CSRFInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getCSRFInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/home", "/login", "/register", "/listNearbyInfos",
                        "/admin", "/error*");
    }
}
