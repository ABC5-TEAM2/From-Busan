package com.frombusan.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.frombusan.interceptor.LogInceptor;
import com.frombusan.interceptor.LoginCheckInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer {
	//url
    private String[] excludePaths = {"/", "/js/**","/member/join",
    		"/member/login", "/member/logout", "/css/**", 
    		"/*.ico", "/error","/image/**",
    		"/festival/**","/tourist/**","/restaurant/**"
    		};

	  public void addInterceptors(InterceptorRegistry registry) {
	        // 인터셉터를 등록한다.
//	        registry.addInterceptor(new LogInceptor())
//	                // 인터셉터의 호출 순서를 지정. 낮을수록 먼저 호출된다.
//	                .order(1)
//	                // 인터셉터를 적용할 URL 패턴을 지정
//	                .addPathPatterns("/**")
//	                // 인터셉터에서 제외할 패턴을 지정
//	                .excludePathPatterns("/css/**", "/*.ico", "/error","/image/**");

	        registry.addInterceptor(new LoginCheckInterceptor())
	                .order(1)
	                .addPathPatterns("/**")
	                .excludePathPatterns(excludePaths);
	    }
	
}
