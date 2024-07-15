/**
 * ShiroConfigurer.java
 * @author     duyvk
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.config;

import co.ipicorp.saas.core.web.security.ApiWebSessionManager;
import co.ipicorp.saas.core.web.security.BypassSecurityFilter;
import co.ipicorp.saas.core.web.security.SessionExpirationHandler;
import co.ipicorp.saas.retailerapi.security.RetailerAppRealm;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.Cookie.SameSiteOptions;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import grass.micro.apps.component.SystemConfiguration;
import grass.micro.apps.util.PropertiesConstants;
import grass.micro.apps.web.security.RestShiroLogoutFilter;
import grass.micro.apps.web.security.SpringCacheManagerWrapper;

/**
 * ShiroConfigurer.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 * @package grass.micro.apps.cloudcustomer.config
 */
@Configuration()
public class ShiroConfigurer {
    
    @Autowired
    private SystemConfiguration systemConfiguration;
    
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        JavaUuidSessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();
        return sessionIdGenerator;
    }

    /**
     * {@link SimpleCookie} implement cookie with a sid.
     * Use for SessionManager
     * @return {@link SimpleCookie} instance.
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie(RetailerSessionInfo.APP_RETAILER_SESSION_ID_KEY);
        sessionIdCookie.setHttpOnly(true);
        sessionIdCookie.setMaxAge(-1);
        String enable = systemConfiguration.getProperty("shiro.enable");
        if ("true".equals(enable)) {
        	sessionIdCookie.setSecure(true);
        	sessionIdCookie.setSameSite(SameSiteOptions.STRICT);
        }
        return sessionIdCookie;
    }

    /**
     * {@link EnterpriseCacheSessionDAO} initialization.
     * @return {@link EnterpriseCacheSessionDAO}
     */
    @Bean
    public EnterpriseCacheSessionDAO sessionDao() {
        EnterpriseCacheSessionDAO sessionDao = new EnterpriseCacheSessionDAO();
        String activeSessionsCacheName = systemConfiguration.getProperty("shiro.activeSessionsCacheName");
        sessionDao.setActiveSessionsCacheName(activeSessionsCacheName);
        sessionDao.setSessionIdGenerator(sessionIdGenerator());
        return sessionDao;
    }

    /**
     * Web Session Management initialization.
     * @return {@link DefaultWebSessionManager} instance.
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new ApiWebSessionManager();
        int timeout = PropertiesConstants.APP_DEFAULT_SESSION_TIMEOUT;
        try {
            timeout = Integer
                    .parseInt(systemConfiguration.getProperty(PropertiesConstants.APP_SESSION_TIMEOUT_KEY));
        } catch (Exception ex) {
            // do nothing
        }

        sessionManager.setGlobalSessionTimeout(timeout * 60 * 1000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionDAO(sessionDao());
        sessionManager.setSessionIdCookieEnabled(false);
//        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdUrlRewritingEnabled(false); // Fix SHIRO-361 issue
        sessionManager.setSessionListeners(Arrays.asList(sessionExpirationHandler()));
        return sessionManager;
    }

    @Bean
    public SessionListener sessionExpirationHandler() {
        SessionListener listener = new SessionExpirationHandler();
        return listener;
    }

    /**
     * {@link WebSecurityManager} initialization.
     * @return {@link DefaultWebSecurityManager}
     */
    @Bean
    public DefaultWebSecurityManager securityManager(SpringCacheManagerWrapper springCached) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager() {
            @Override
            protected void beforeLogout(Subject subject) {
                super.removeRequestIdentity(subject);
            }    
        };
        securityManager.setRealm(appRealm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(springCached);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }
    
    @Bean
    public SpringCacheManagerWrapper springCached(HazelcastCacheManager hzCacheManager) {
        SpringCacheManagerWrapper rs = new SpringCacheManagerWrapper();
        rs.setCacheManager(hzCacheManager);
        return rs;
    }
    
    @Bean
    public HazelcastCacheManager hzCacheManager(HazelcastInstance hazelcastInstance) {
        HazelcastCacheManager rs = new HazelcastCacheManager();
        rs.setHazelcastInstance(hazelcastInstance);
        return rs;
    }
    
    @Bean
    public BypassSecurityFilter securityFilter() {
        BypassSecurityFilter securityFilter = new BypassSecurityFilter();
        securityFilter.setSessionInfoKey(Constants.APP_SESSION_INFO_KEY);
        return securityFilter;
    }

    @Bean
    public RetailerAppRealm appRealm() {
        RetailerAppRealm appRealm = new RetailerAppRealm();
        return appRealm;
    }

    /**
     * Main Filter for Apache Shiro.
     * 
     * @throws Exception if Any error occur in initialize phrase.
     */
    @Bean
    public AbstractShiroFilter shiroFilter(DefaultWebSecurityManager securityManager) throws Exception {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl(ControllerAction.APP_LOGIN_ACTION);

        Map<String, String> filterChainDefinitionMapping = new LinkedHashMap<>();
        filterChainDefinitionMapping.put(ControllerAction.APP_COMMON_ACTION, "anon");
        filterChainDefinitionMapping.put(ControllerAction.APP_AUTH_RESET_PASSWORD_ACTION, "anon");
        filterChainDefinitionMapping.put(ControllerAction.APP_AUTH_FORGOT_PASSWORD_ACTION, "anon");
        filterChainDefinitionMapping.put(ControllerAction.APP_LOGIN_ACTION, "authc");
        filterChainDefinitionMapping.put(ControllerAction.APP_LOGOUT_ACTION, "logout");
        filterChainDefinitionMapping.put("/**", "user");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);

        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        filters.put("anon", new AnonymousFilter());
        filters.put("logout", new RestShiroLogoutFilter(Constants.APP_SESSION_INFO_KEY));
        filters.put("authc", new PassThruAuthenticationFilter());
        filters.put("user", securityFilter());
        shiroFilter.setFilters(filters);

        return (AbstractShiroFilter) shiroFilter.getObject();
    }
    
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }
}