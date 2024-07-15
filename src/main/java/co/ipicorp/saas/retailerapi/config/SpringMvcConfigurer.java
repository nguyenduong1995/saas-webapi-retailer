/**
 * SpringMvcConfigurer.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.config;

import co.ipicorp.saas.core.web.interceptor.AppAuthenticationInterceptor;
import co.ipicorp.saas.retailerapi.util.Constants;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.Locale;

import grass.micro.apps.model.util.CurrentUserGetter;
import grass.micro.apps.model.util.EntityUpdateTracker;
import grass.micro.apps.web.interceptor.RequestHeaderLocalChangeInterceptor;
import grass.micro.apps.web.interceptor.RequestLoggingInterceptor;
import grass.micro.apps.web.interceptor.StringPostProcessor;
import grass.micro.apps.web.util.ContextHolderCurrentUserGetterStrategry;
import grass.micro.apps.web.util.RequestEntityUpdateTrackerStrategy;

/**
 * SpringMvcConfigurer.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
@Configuration
@EnableWebMvc
public class SpringMvcConfigurer implements WebMvcConfigurer {
    
    /**
     * Message Source Initialization. (Read messages_{country}.properties). Sample: messaages_ja.properties for
     * Japanese.
     * 
     * @return {@link MessageSource} instance.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:define-text", "classpath:common-error-message", "classpath:error-message", "classpath:permissions");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        // messageSource.setFileEncodings(properties);
        // # -1 : never reload, 0 always reload
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

    /**
     * L10N Resolver base on Session.
     * 
     * @return {@link SessionLocaleResolver} instance.
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.US);
        return resolver;
    }
    
    /**
     * LocaleChangeInterceptor initialization.
     * 
     * @return {@link CustomLocalChangeInterceptor}
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new RequestHeaderLocalChangeInterceptor();
        return interceptor;
    }
    
    /**
     * initialize CommonsMultipartResolver.
     * 
     * @return {@link CommonsMultipartResolver}
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(3000000000L);
        return resolver;
    }
      
    /**
     * WebContentInterceptor (no cache).
     * @return WebContentInterceptor
     */
    @Bean
    public WebContentInterceptor webContentInterceptor() {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.setCacheSeconds(0);
        interceptor.setCacheControl(CacheControl.noCache());
        return interceptor;
    }
    
    @Bean
    public RequestLoggingInterceptor requestLoggingInterceptor() {
        return new RequestLoggingInterceptor();
    }
    
    @Bean
    public AppAuthenticationInterceptor appAuthenticationInterceptor() {
        return new AppAuthenticationInterceptor();
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        ContextHolderCurrentUserGetterStrategry strategy = new ContextHolderCurrentUserGetterStrategry();
        strategy.setSessionKey(Constants.APP_SESSION_INFO_KEY);
        CurrentUserGetter.getInstance().insertCurrentUserGetterStrategy(strategy);
        EntityUpdateTracker.getInstance().setEntityUpdateTrackerStrategy(new RequestEntityUpdateTrackerStrategy());
        //configurer.enable();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webContentInterceptor());
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(requestLoggingInterceptor());
        registry.addInterceptor(appAuthenticationInterceptor());
    }
    
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        int timeout = 24 * 60 * 60 * 1000;
        configurer.setDefaultTimeout(timeout);
        configurer.setTaskExecutor(asyncTaskExecutor());
    }
    
    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new SimpleAsyncTaskExecutor("async");
    }
    
    /**
     * Add StringPostProcessor to replace all \\r\\n in form submission to \\n.
     * Make Line Break to 1 character instead of 2 characters.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringPostProcessor());
    }
    
}
