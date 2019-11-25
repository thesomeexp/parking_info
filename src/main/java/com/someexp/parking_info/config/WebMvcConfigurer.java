package com.someexp.parking_info.config;

import com.someexp.parking_info.interceptor.CSRFInterceptor;
import com.someexp.parking_info.interceptor.LoginInterceptor;
import com.someexp.parking_info.interceptor.RAInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public RAInterceptor getRAInterceptor() {
        return new RAInterceptor();
    }

    @Bean
    public CSRFInterceptor getCSRFInterceptor() {
        return new CSRFInterceptor();
    }

    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 重放攻击拦截器, 登录验证拦截器, CSRF拦截器
        registry.addInterceptor(getRAInterceptor()).addPathPatterns("/**").excludePathPatterns("/",
                "/data**");
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/",
                "/data**", "/login", "/register", "/listNearbyInfos");
        registry.addInterceptor(getCSRFInterceptor()).addPathPatterns("/**").excludePathPatterns("/",
                "/data**", "/login", "/register", "/listNearbyInfos");

    }
}
