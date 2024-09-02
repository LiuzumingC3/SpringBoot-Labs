package org.lzm.config;


import org.lzm.common.interceptor.FirstInterceptor;
import org.lzm.common.interceptor.SecondInterceptor;
import org.lzm.common.interceptor.ThirdInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


@Configuration
public class SpringMVCConfiguration implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public FirstInterceptor firstInterceptor() {
        return new FirstInterceptor();
    }

    @Bean
    public SecondInterceptor secondInterceptor() {
        return new SecondInterceptor();
    }

    @Bean
    public ThirdInterceptor thirdInterceptor() {
        return new ThirdInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器一
        registry.addInterceptor(this.firstInterceptor()).addPathPatterns("/**");
        // 拦截器二
        registry.addInterceptor(this.secondInterceptor()).addPathPatterns("/users/current_user");
        // 拦截器三
        registry.addInterceptor(this.thirdInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public ServletRegistrationBean getServlet() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                //super.doGet(req, resp);
                logger.info("doGet..[uri:{}]", req.getRequestURI());
            }
        });
        servlet.setUrlMappings(Collections.singleton("test/01"));
        return servlet;
    }

    @Bean
    public FilterRegistrationBean testFilter01() {
        FilterRegistrationBean filter = new FilterRegistrationBean(new Filter() {

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                logger.info("[doFilter]");
                filterChain.doFilter(servletRequest, servletResponse);
            }
        });
        filter.setUrlPatterns(Collections.singletonList("/test/*"));
        return filter;
    }

    @Bean
    public ServletListenerRegistrationBean<?> testListener01() {
        ServletListenerRegistrationBean<ServletContextListener> bean = new ServletListenerRegistrationBean<>(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                //ServletContextListener.super.contextInitialized(sce);
                logger.info("[contextInitialized]");
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                ServletContextListener.super.contextDestroyed(sce);
            }
        });
        return bean;
    }


}
