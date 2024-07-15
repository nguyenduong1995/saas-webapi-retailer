/**
 * 
 */
package co.ipicorp.saas.retailerapi;

import co.ipicorp.saas.retailerapi.config.ShiroConfigurer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import java.nio.charset.Charset;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import grass.micro.apps.web.security.SimpleCorsFilter;

/**
 * @author hieumicro
 *
 */
@EnableAutoConfiguration(exclude = { ThymeleafAutoConfiguration.class, ErrorMvcAutoConfiguration.class })
@ComponentScan(basePackages = {"co.ipicorp.saas.retailerapi", "co.ipicorp.saas.nrms", "co.ipicorp.saas.core", "grass.micro.apps"})
@SpringBootApplication
@EnableAsync
@Configuration
public class RetailerApiApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(RetailerApiApplication.class, args);
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
    
    @Bean
    public FilterRegistrationBean<SimpleCorsFilter> corsFilter() {
        SimpleCorsFilter corsFilter = new SimpleCorsFilter();
        FilterRegistrationBean<SimpleCorsFilter> registration = new FilterRegistrationBean<SimpleCorsFilter>();
        registration.setFilter(corsFilter);
        registration.addUrlPatterns("/*");
        registration.setName("SimpleCorsFilter");
        return registration;
    }
    
    /**
     * APACHE SHIRO "shiroFilter" configuration.
     * 
     * @return {@link FilterRegistrationBean} instance
     * @see ShiroConfigurer
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> shiroFilterRegistration() {
        DelegatingFilterProxy shiroFilter = new DelegatingFilterProxy();
        shiroFilter.setTargetBeanName("shiroFilter");

        FilterRegistrationBean<DelegatingFilterProxy> registration = new FilterRegistrationBean<>();
        registration.setFilter(shiroFilter);
        registration.addUrlPatterns("/*");
        registration.setName("shiroFilterRegistration");
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
        registration.setMatchAfter(false);
        registration.setAsyncSupported(true);
        registration.addInitParameter("targetFilterLifecycle", "true");
        return registration;
    }
    
    /**
     * Customize {@link DispatcherServlet}.
     * activate throwExceptionIfNoHandlerFound
     * @return DispatcherServlet
     */
    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.setThrowExceptionIfNoHandlerFound(true);
        return servlet;
    }

    /**
     * Define {@link ErrorPageFilter}.
     * @return ErrorPageFilter
     */
    @Bean
    public ErrorPageFilter errorPageFilter() {
        ErrorPageFilter filter = new ErrorPageFilter();
        filter.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error"));
        filter.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error"));
        filter.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error"));
        filter.addErrorPages(new ErrorPage(HttpStatus.NOT_ACCEPTABLE, "/error"));
        filter.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error"));
        filter.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error"));
        filter.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error"));
        return filter;
    }

    /**
     * Register {@link ErrorPageFilter}.
     * @return ErrorPageFilter
     */
    @Bean
    public FilterRegistrationBean<ErrorPageFilter> disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean<ErrorPageFilter> filterRegistrationBean = new FilterRegistrationBean<ErrorPageFilter>();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RetailerApiApplication.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }
    
    @Bean 
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}